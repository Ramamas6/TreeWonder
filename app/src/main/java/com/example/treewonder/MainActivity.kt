package com.example.treewonder


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SERVER_BASE_URL = "https://treewonder.cleverapps.io/"

class MainActivity : AppCompatActivity() {

    private val trees = Trees()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(SERVER_BASE_URL)
        .build()
    private val treeService = retrofit.create(TreeService::class.java)

    // Needed to send it the result of RequestPermissions for localisation
    private var mapFragment: MapsFragment = MapsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        setUpToolBar()
        setUpTabLayout()

        // Display initial map fragment
        displayMapFragment()
    }

    private fun displayListFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.a_main_fragment,
            TreeListFragment.newInstance(trees.getAllTrees())
        )
        transaction.commit()
    }

    private fun displayMapFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.a_main_fragment,
            mapFragment
        )
        transaction.commit()
    }

    private fun displayFavoriteFragment() {

    }

    private fun displaySettings() {

    }

    fun getTrees(): ArrayList<Tree> {
        return trees.getAllTrees()
    }

    /**
     * Get the result of an activity
     */
    @Suppress("DEPRECATION")
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val treeCreated = result.data?.getSerializableExtra(TREE_KEY) as Tree
            Toast.makeText(this, treeCreated.name, Toast.LENGTH_LONG).show()
            treeService.createTree(treeCreated)
                .enqueue {
                    onResponse = {
                        val treeFromServer: Tree? = it.body()
                        if(treeFromServer != null) {
                            trees.addTree(treeFromServer)
                        }
                        displayMapFragment()
                    }
                    onFailure = {
                        Toast.makeText(this@MainActivity, it?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    /**
     * Initialize the data when opening the app
     */
    private fun initData() {
        /** Get every tree from the API **/
        treeService.getAllTrees()
            .enqueue(object : Callback<List<Tree>> {
                override fun onResponse(call: Call<List<Tree>>, response: Response<List<Tree>>) {
                    val allTrees: List<Tree> = response.body()!!
                    trees.addTrees(allTrees)
                    mapFragment.displayTrees(trees.getAllTrees())
                }
                override fun onFailure(call: Call<List<Tree>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setUpToolBar() {
        // Display toolbar
        var bar = findViewById<Toolbar>(R.id.a_main_toolbar)
        setSupportActionBar(bar)
        // Get toolbar buttons and set listeners
        val addTreeButton = findViewById<FloatingActionButton>(R.id.a_main_btn_create_tree)
        addTreeButton.setOnClickListener{
            val intent = Intent(this, CreateTreeActivity::class.java)
            this.startForResult.launch(intent)
        }
        val updateButton = findViewById<FloatingActionButton>(R.id.a_main_btn_update)
        updateButton.setOnClickListener{initData()}
        val settingsButton = findViewById<FloatingActionButton>(R.id.a_main_btn_settings)
        settingsButton.setOnClickListener{displaySettings()}
    }

    private fun setUpTabLayout() {
        // Create tabLayout (with map initially selected)
        var tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
        tabLayout.addTab(tabLayout.newTab().setText("LIST"), 0, false)
        tabLayout.addTab(tabLayout.newTab().setText("MAP"), 1, true)
        tabLayout.addTab(tabLayout.newTab().setText("FAVORITES"), 2, false)

        // Create tabLayout listener
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> displayListFragment()
                    1 -> displayMapFragment()
                    2 -> displayFavoriteFragment()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /** Localisation permission **/
        if (requestCode == 2) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                mapFragment.getLocation() // Set map location as current localisation
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
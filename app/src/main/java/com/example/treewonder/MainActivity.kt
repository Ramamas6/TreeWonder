package com.example.treewonder


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
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

        setUpToolBar() // Create the ToolBar menu
        setUpTabLayout() // Create the navigation bar
        displayMapFragment() // Display initial map fragment
        initData() // Load the trees from the API
    }

    @SuppressLint("SetTextI18n")
    private fun displayListFragment(){
        val emptyText = findViewById<TextView>(R.id.a_main_empty)
        if(trees.size() == 0) {
            emptyText.visibility = View.VISIBLE
            if(isInternetEnabled(this))
                emptyText.text = "oops ! It seem there is no tree to display...\nPlease use the refresh button or contact the administrators if this doesn't work."
            else
                emptyText.text = "Oops! It looks like you don't have an internet connection...\nPlease activate the connection and use the refresh button."
        }
        else {emptyText.visibility = View.GONE}
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
        if(isInternetEnabled(this)) Toast.makeText(this, "Internet activated", Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, "Internet Denied", Toast.LENGTH_SHORT).show()
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
        if(isInternetEnabled(this)) {
            treeService.getAllTrees().enqueue(object : Callback<List<Tree>> {
                override fun onResponse(call: Call<List<Tree>>, response: Response<List<Tree>>) {
                    val allTrees: List<Tree> = response.body()!!
                    trees.addTrees(allTrees)
                    mapFragment.displayTrees(trees.getAllTrees())
                    when (findViewById<TabLayout>(R.id.a_main_tabs).selectedTabPosition) {
                        0 -> displayListFragment()
                        1 -> displayMapFragment()
                        2 -> displayFavoriteFragment()
                    }
                }
                override fun onFailure(call: Call<List<Tree>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        else {Toast.makeText(this, "Unable to load data: no internet connection detected", Toast.LENGTH_SHORT).show()}
    }

    private fun setUpToolBar() {
        // Display toolbar
        setSupportActionBar(findViewById(R.id.a_main_toolbar))
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
        val tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
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
        if (requestCode == 2) {this.mapFragment.requestLocationResult(grantResults)}
    }

}
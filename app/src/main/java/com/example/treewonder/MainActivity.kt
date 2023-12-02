package com.example.treewonder


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.activity.result.ActivityResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TREE_KEY = "tree-key"
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

    fun getTrees(): ArrayList<Tree> {
        return trees.getAllTrees()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        displayMapFragment()

        val addTreeButton = findViewById<FloatingActionButton>(R.id.a_main_btn_create_tree)
        addTreeButton.setOnClickListener{
            val intent = Intent(this, CreateTreeActivity::class.java)
            this.startForResult.launch(intent)
        }

        val buttonList = findViewById<Button>(R.id.a_main_button_list)
        buttonList.setOnClickListener{ displayListFragment() }

        val buttonMap = findViewById<Button>(R.id.a_main_button_map)
        buttonMap.setOnClickListener { displayMapFragment() }
    }

    /**
     * Display the fragment with list of all trees
     */
    private fun displayListFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.a_main_fragment,
            TreeListFragment.newInstance(trees.getAllTrees())
        )
        transaction.commit()
    }

    /**
     * Display the fragment with the map
     */
    private fun displayMapFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.a_main_fragment,
            mapFragment
        )
        transaction.commit()
    }

    /**
     * Get the result of an activity
     */
    @Suppress("DEPRECATION")
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val treeCreated = result.data?.getSerializableExtra(TREE_KEY)
            treeService.createTree(treeCreated as Tree)
                .enqueue {
                    onResponse = {
                        val treeFromServer: Tree? = it.body()
                        trees.addTree(treeFromServer!!)
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

package com.ismin.treewonder


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.gson.GsonBuilder
import com.ismin.treewonder.createTree.CreateTreeActivity
import com.ismin.treewonder.createTree.TREE_KEY
import com.ismin.treewonder.mainFragments.MapsFragment
import com.ismin.treewonder.mainFragments.SettingsFragment
import com.ismin.treewonder.mainFragments.TreeFragment
import com.ismin.treewonder.mainFragments.TreeListFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


private const val SERVER_BASE_URL = "https://treewonder.cleverapps.io/"
private const val FILENAME = "favorites.txt"

class MainActivity : AppCompatActivity() {
    // Map of trees
    private val trees = Trees()
    // List of favorites
    private val favoritesList = mutableListOf<Int>()

    private val gson = GsonBuilder()
        .registerTypeAdapter(Tree::class.java, TreeSerializer())
        .create()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(SERVER_BASE_URL)
        .build()

    private val treeService = retrofit.create(TreeService::class.java)

    // Needed to send it the result of RequestPermissions for localisation
    private val mapFragment: MapsFragment = MapsFragment()
    private var initialPosition: LatLng? = null
    private var listPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val file = File(this.filesDir, FILENAME)
        if (!file.exists()) file.appendText("") // Creates the file if it doesn't exists yet

        initFavorites() // Initialize the list of favorites
        setUpToolBar() // Create the ToolBar menu
        setUpTabLayout() // Create the navigation bar
        initData() // Load the trees from the API
    }

    @SuppressLint("SetTextI18n")
    private fun displayListFragment(){
        // Manage empty text
        val emptyText = findViewById<TextView>(R.id.a_main_empty)
        if(trees.size() == 0) {
            emptyText.visibility = View.VISIBLE
            if(isInternetEnabled(this))
                emptyText.text = getString(R.string.MainActivity_noTreesInList)
            else
                emptyText.text = getString(R.string.MainActivity_noInternetInList)
        }
        else {emptyText.visibility = View.GONE}
        // Display fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.a_main_fragment,
            TreeListFragment.newInstance(trees.getAllTrees(), ArrayList(favoritesList), 0, true),
            "TreeListFragment")
        transaction.commit()
        // Manage button
        val fragmentButton = findViewById<FloatingActionButton>(R.id.f_main_btn)
        fragmentButton.setImageResource(R.drawable.ic_menu_search)
        val searchBar = findViewById<EditText>(R.id.a_main_search)
        fragmentButton.setOnClickListener{
            if(searchBar.visibility == View.INVISIBLE) searchBar.visibility = View.VISIBLE
            else searchBar.visibility = View.INVISIBLE
        }
        searchBar.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) searchInList(searchBar) }
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(searchBar.windowToken, 0)
                searchInList(searchBar)
                true
            } else false
        }
    }

    private fun searchInList(searchBar: EditText) {
        searchBar.visibility = View.INVISIBLE
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.a_main_fragment,
            TreeListFragment.newInstance(trees.getAllTrees(searchBar.text.toString()),
                ArrayList(favoritesList), 0, true),
            "TreeListFragment")
        transaction.commit()
    }

    private fun displayMapFragment(){
        // Display fragment
        val transaction = supportFragmentManager.beginTransaction()
        mapFragment.setInitialPosition(initialPosition)
        transaction.replace(R.id.a_main_fragment, mapFragment, "MapsFragment")
        transaction.commit()
        initialPosition = null
        // Manage button
        val fragmentButton = findViewById<FloatingActionButton>(R.id.f_main_btn)
        fragmentButton.setImageResource(R.drawable.ic_menu_mylocation)
        fragmentButton.setOnClickListener{mapFragment.setCameraOnLocation()}
        findViewById<EditText>(R.id.a_main_search).visibility = View.INVISIBLE
    }

    private fun displayFavoriteFragment() {
        val favoriteTrees = trees.getTrees(favoritesList)
        // Manage empty text
        val emptyText = findViewById<TextView>(R.id.a_main_empty)
        if(favoriteTrees.size == 0) {
            emptyText.visibility = View.VISIBLE
            emptyText.text = getString(R.string.favorites_empty)
        }
        else {emptyText.visibility = View.GONE}
        // Display fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.a_main_fragment,
            TreeListFragment.newInstance(favoriteTrees, ArrayList(favoritesList), listPosition, false),
            "FavoritesFragment")
        transaction.commit()
        listPosition = 0
        // Manage button
        val fragmentButton = findViewById<FloatingActionButton>(R.id.f_main_btn)
        fragmentButton.setImageResource(R.drawable.ic_menu_delete)
        fragmentButton.setOnClickListener { deleteFavorites() }
        findViewById<EditText>(R.id.a_main_search).visibility = View.INVISIBLE
    }

    private fun displaySettingsFragment() {
        val settingsFragment = supportFragmentManager.findFragmentByTag("SettingsFragment") as? SettingsFragment
        if (settingsFragment == null) {
            // Display fragment
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.a_main_fragment, SettingsFragment(), "SettingsFragment")
            transaction.commit()
            // Manage button
            val fragmentButton = findViewById<FloatingActionButton>(R.id.f_main_btn)
            fragmentButton.setImageResource(R.drawable.ic_menu_delete)
            fragmentButton.setOnClickListener { deleteLocalData() }
            findViewById<EditText>(R.id.a_main_search).visibility = View.INVISIBLE
        }
        else {
            val tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
            tabLayout.selectTab(tabLayout.getTabAt(tabLayout.selectedTabPosition))
        }
    }

    fun displayTreeFragment(tree: Tree) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.a_main_fragment, TreeFragment.newInstance(tree, favoritesList.contains(tree.id)), "TreeFragment")
        transaction.commit()
        // Manage button
        val fragmentButton = findViewById<FloatingActionButton>(R.id.f_main_btn)
        fragmentButton.setImageResource(R.drawable.ic_menu_delete)
        fragmentButton.setOnClickListener { deleteTree(tree.id) }
        findViewById<EditText>(R.id.a_main_search).visibility = View.INVISIBLE
    }

    /** Adds or removes a tree from the favorites
     * @param id id of the tree to add/remove from favorites
     * @param actualise whether the screen must be actualised or not afterwards
     */
    fun changeFavorites(id: Int, actualise: Boolean, lastPosition: Int = 0) {
        // Read favorites
        val contents = File(this.filesDir, FILENAME).readText() // Read file
        val index = contents.indexOf("$id ")
        // This id is not already in favorites: adds it
        if (index == -1) {
            this.openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
                it.write(contents.plus("$id ").toByteArray())
            }
            favoritesList.add(id)
        }
        // This id is already in favorites: deletes it
        else {
            this.openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
                it.write(contents.removeRange(index, index + id.toString().length + 1).toByteArray())
            }
            favoritesList.remove(id)
        }
        // Actualise screen if necessary
        if(actualise) {
            listPosition = lastPosition
            val tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
            tabLayout.selectTab(tabLayout.getTabAt(tabLayout.selectedTabPosition))
        }
    }

    private fun deleteFavorites() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage(getString(R.string.MainActivity_deleteFavorites))
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                favoritesList.clear()
                this.openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
                    it.write("".toByteArray())
                    displayFavoriteFragment()
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss()}
        val alert = builder.create()
        alert.show()
    }

    private fun deleteTree(id: Int) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage(getString(R.string.MainActivity_deleteTree))
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                trees.remove(id)
                displayListFragment()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss()}
        val alert = builder.create()
        alert.show()
    }

    private fun deleteLocalData() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage(getString(R.string.MainActivity_deleteAllTrees))
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                trees.clear()
                Toast.makeText(this,
                    getString(R.string.MainActivity_deleteAllTress_confirmation), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss()}
        val alert = builder.create()
        alert.show()
    }

    fun getTrees(): ArrayList<Tree> {
        return trees.getAllTrees()
    }

    fun teleportToPosition(loc: LatLng?) {
        initialPosition = loc
        val tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
        tabLayout.selectTab(tabLayout.getTabAt(1))
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
                        val tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
                        tabLayout.selectTab(tabLayout.getTabAt(tabLayout.selectedTabPosition))
                    }
                    onFailure = {
                        val tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
                        tabLayout.selectTab(tabLayout.getTabAt(tabLayout.selectedTabPosition))
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
                    val tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
                    tabLayout.selectTab(tabLayout.getTabAt(tabLayout.selectedTabPosition))
                }
                override fun onFailure(call: Call<List<Tree>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        else {Toast.makeText(this, getString(R.string.MainActivity_noInternet), Toast.LENGTH_SHORT).show()}
    }

    private fun initFavorites() {
        val favorites = File(this.filesDir, FILENAME).readText()
        if(favorites.isNotEmpty())
            favorites.dropLast(1).split(" ").map { favoritesList.add(it.toInt()) }
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
        settingsButton.setOnClickListener{displaySettingsFragment()}
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
            override fun onTabReselected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> displayListFragment()
                    1 -> displayMapFragment()
                    2 -> displayFavoriteFragment()
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /** Localisation permission **/
        if (requestCode == 2) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Thank you, your location was sold on the dark web", Toast.LENGTH_SHORT).show()
                // Case map fragment open : set camera on localisation
                if (supportFragmentManager.findFragmentByTag("MapsFragment") as? MapsFragment != null)
                    mapFragment.setCameraOnLocation()
            }
        }
    }

}
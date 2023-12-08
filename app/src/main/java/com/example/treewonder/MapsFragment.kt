package com.example.treewonder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.squareup.picasso.Picasso
import java.util.concurrent.Executors


class MapsFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<LandMark>
    // Used to manage the case when treeData from API is ready before the map
    private var treeInit = false

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        // Setup clusterManager
        clusterManager = ClusterManager(context, googleMap)
        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)
        // Setup default camera
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
        val paris = LatLng(48.8589384,2.2646343)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(paris))
        // Set camera on localisation
        getLocation(this.requireActivity()) { result ->
            if (result != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(result))
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
            }
            else {}
        }

        // Add markers
        val myExecutor = Executors.newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())
        myExecutor.execute {
            val imageUrl = "https://upload.wikimedia.org/wikipedia/commons/a/a8/Tour_Eiffel_Wikimedia_Commons.jpg"
            val bitmapImage = Picasso.get().load(imageUrl).resize(100, 200).get()
            myHandler.post {
                googleMap.addMarker(MarkerOptions().position(paris).title("Paris").icon(BitmapDescriptorFactory.fromBitmap(bitmapImage)))
            }
        }
        if(treeInit) {
            // tree data from API was ready before: we have to add them now
            var trees = (activity as MainActivity).getTrees()
            trees.forEach { value -> addLandMark(value)}
        }

    }


    fun displayTrees(trees: ArrayList<Tree>) {
        if(::googleMap.isInitialized) {
            trees.forEach { value -> addLandMark(value)}
        }
        treeInit = true
    }


    private fun addLandMark(tree: Tree) {
        if(tree.latitude != null && tree.longitude != null) {
            val newLandMark = LandMark(tree.latitude, tree.longitude, tree.name, tree.summary)
            clusterManager.addItem(newLandMark)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun setCameraOnLocation() {
        getLocation(this.requireActivity()) { result ->
            if (result != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(result))
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
            }
            else {}
        }
    }
}
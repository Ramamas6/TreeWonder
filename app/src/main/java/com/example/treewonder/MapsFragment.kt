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
    private var initialPosition: LatLng? = null

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        // Setup clusterManager
        clusterManager = ClusterManager(context, googleMap)
        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)
        // Set camera on localisation
        setCameraOnLocation(initialPosition)
        // Add markers
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
            val newLandMark = LandMark(tree.latitude, tree.longitude, tree.name, tree.outstandingQualification)
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

    fun setCameraOnLocation(loc: LatLng? = null) {
        if(loc == null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(48.8589384,2.2646343)))
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
            getLocation(this.requireActivity()) { result ->
                if (result != null) {
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(result))
                }
            }
        }
        else {
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc))

        }
    }

    fun setInitialPosition(loc: LatLng?) {
        initialPosition = loc
    }

}
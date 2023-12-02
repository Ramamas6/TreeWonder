package com.example.treewonder

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.fragment.app.Fragment
import android.Manifest
import android.app.Activity
import android.location.Location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
class MapsFragment : Fragment() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    // Used to manage the case when treeData from API is ready before the map
    private var treeInit = false

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        map.moveCamera(CameraUpdateFactory.zoomTo(14f))
        val paris = LatLng(48.8589384,2.2646343)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(paris))
        getLocation()
        googleMap.addMarker(MarkerOptions().position(paris).title("Paris").snippet("Default position"))
        if(treeInit) {
            var trees = (activity as MainActivity).getTrees()
            // tree data from API was ready before: we have to add them now
            trees.forEach { value -> googleMap.addMarker(
                MarkerOptions().position(LatLng(value.latitude, value.longitude)).title(value.name).snippet(value.summary)
            )}
        }
    }

    fun displayTrees(trees: ArrayList<Tree>) {
        treeInit = true
        if(::googleMap.isInitialized) {
            trees.forEach { value -> googleMap.addMarker(
                MarkerOptions().position(LatLng(value.latitude, value.longitude)).title(value.name).snippet(value.summary)
            )}
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

    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this.requireActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this.requireActivity()) { task ->
                    val location: Location? = task.result
                    if(location != null) {
                        val loc = LatLng(location.latitude, location.longitude)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
                    }
                }
            } else {
                Toast.makeText(this.activity, "Please turn on location", Toast.LENGTH_LONG).show()
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this.requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            2)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

}
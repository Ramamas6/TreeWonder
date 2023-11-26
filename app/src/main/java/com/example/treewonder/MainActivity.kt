package com.example.treewonder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

private lateinit var fusedLocationClient: FusedLocationProviderClient

class MainActivity : TreeCreator, AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

    private val floatingActionButton: FloatingActionButton by lazy {
        findViewById(R.id.a_main_btn_create_tree)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton.setOnClickListener{
            displayCreatedTreeFragment()
        }

        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.a_main_lyt_fragment, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)

    }

    private fun displayCreatedTreeFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.a_main_lyt_fragment,
            CreateTreeFragment()
        )
        transaction.commit()
        floatingActionButton.visibility = View.GONE
    }

    override fun onMapReady(map: GoogleMap){
        googleMap = map
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        map.moveCamera(CameraUpdateFactory.zoomTo(14f))
        getLocation()
        val paris = LatLng(48.8589384,2.2646343)
        map.addMarker(MarkerOptions().position(paris).title("Paris").snippet("Default position"))
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if(location != null) {
                        val loc = LatLng(location.latitude, location.longitude)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            2)
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResults: IntArray) {
        if (requestCode == 2) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun onTreeCreated(tree: Tree) {
        TODO("Not yet implemented")
    }

}

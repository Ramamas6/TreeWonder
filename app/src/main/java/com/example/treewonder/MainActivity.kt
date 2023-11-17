package com.example.treewonder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = SupportMapFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.a_main_lyt, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap){
        val gardanne = LatLng(
            43.4525982, 5.4717363
        )
        map.addMarker(
            MarkerOptions().position(gardanne).title("Gardanne").snippet("QG des ISMIN")
        )

        map.moveCamera(CameraUpdateFactory.zoomTo(14f))
        map.moveCamera(CameraUpdateFactory.newLatLng(gardanne))
    }
}
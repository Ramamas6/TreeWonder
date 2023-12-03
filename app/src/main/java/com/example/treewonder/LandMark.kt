package com.example.treewonder

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.squareup.picasso.Picasso
import java.util.concurrent.Executors

class LandMark(lat: Double, lng: Double, title: String, snippet: String) : ClusterItem {

    private val position: LatLng
    private val title: String
    private val snippet: String

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return title
    }

    override fun getSnippet(): String {
        return snippet
    }

    init {
        position = LatLng(lat, lng)
        this.title = title
        this.snippet = snippet
    }
}
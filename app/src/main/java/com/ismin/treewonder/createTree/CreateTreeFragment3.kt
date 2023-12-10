package com.ismin.treewonder.createTree

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.ismin.treewonder.R
import com.ismin.treewonder.Tree


class CreateTreeFragment3 : Fragment(), OnMapReadyCallback {

    private lateinit var listener: TreeCreator

    private var name: String = ""
    private var commonName: String = ""
    private var botanicName: String = ""
    private var height: Int = 0
    private var circumference: Int = 0
    private var developmentStage: String = ""
    private var plantationYear: Int = 0
    private var outstandingQualification: String = ""
    private var summary: String = ""
    private var description: String = ""
    private var type: String = ""
    private var species: String = ""
    private var variety: String = ""
    private var sign: String = ""
    private var picture: String = ""
    private var longitude = 0.0
    private var latitude = 0.0
    private var address: String = ""

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var btnSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_tree3, container, false)

        //Init map view
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        //Get data from the parent fragment
        val treeData: Bundle? = arguments
        if (treeData != null) {
            name = treeData.getString("name", "")
            commonName = treeData.getString("commonName", "")
            botanicName = treeData.getString("botanicName", "")
            height = treeData.getInt("height", 0)
            circumference = treeData.getInt("circumference", 0)
            developmentStage = treeData.getString("developmentStage", "")
            plantationYear = treeData.getInt("plantationYear", 0)
            outstandingQualification = treeData.getString("outstandingQualification", "")
            summary = treeData.getString("summary", "")
            description = treeData.getString("description", "")
            type = treeData.getString("type", "")
            species = treeData.getString("species", "")
            variety = treeData.getString("variety", "")
            sign = treeData.getString("sign", "")
            picture = treeData.getString("picture", "")
        }
        btnSave = view.findViewById(R.id.f_create_tree_btn_save)

        btnSave.setOnClickListener {

            val projection = googleMap.projection
            val visibleRegion: LatLngBounds = projection.visibleRegion.latLngBounds
            val centerScreenLatLng = visibleRegion.center
            latitude = centerScreenLatLng.latitude
            longitude = centerScreenLatLng.longitude
            val tree = Tree(1,
                            name,
                            commonName,
                            botanicName,
                            height,
                            circumference,
                            developmentStage,
                            plantationYear,
                            outstandingQualification,
                            summary,
                            description,
                            type,
                            species,
                            variety,
                            sign,
                            picture,
                            latitude,
                            longitude,
                            address
            )

            this.listener.onTreeCreated(tree)
        }
        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is TreeCreator) {
            this.listener = context
        }else{
            throw IllegalStateException("$context must implement TreeCreator")
        }
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val paris = LatLng(48.858844, 2.294350)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 12f))
    }
}
package com.example.treewonder

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.io.Serializable
import java.lang.reflect.Type


data class Tree(
    val id: Int,
    val name: String,
    val commonName: String,
    val botanicName: String,
    val height: Int,
    val circumference: Int,
    val developmentStage: String,
    val plantationYear: Int,
    val outstandingQualification: String,
    val summary: String,
    val description: String,
    val type: String,
    val species: String,
    val variety: String,
    val sign: String,
    val picture: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
): Serializable


class TreeSerializer : JsonSerializer<Tree> {
    override fun serialize(src: Tree, typeOfSrc: java.lang.reflect.Type?, context: JsonSerializationContext?): JsonObject {
        val treeObject = JsonObject()
        treeObject.addProperty("name", src.name)
        if(src.commonName.isNotBlank())
            treeObject.addProperty("commonName", src.commonName)
        if(src.botanicName.isNotBlank())
            treeObject.addProperty("botanicName", src.botanicName)
        if(src.height != 0)
            treeObject.addProperty("height", src.height)
        if(src.circumference != 0)
            treeObject.addProperty("circumference", src.circumference)
        treeObject.addProperty("developmentStage", src.developmentStage)
        if(src.plantationYear != 0)
            treeObject.addProperty("plantationYear", src.plantationYear)
        if(src.outstandingQualification.isNotBlank())
            treeObject.addProperty("outstandingQualification", src.outstandingQualification)
        if(src.summary.isNotBlank())
            treeObject.addProperty("summary", src.summary)
        if(src.description.isNotBlank())
            treeObject.addProperty("description", src.description)
        if(src.type.isNotBlank())
            treeObject.addProperty("type", src.type)
        if(src.species.isNotBlank())
            treeObject.addProperty("species", src.species)
        if(src.variety.isNotBlank())
            treeObject.addProperty("variety", src.variety)

        if (src.sign.isNotBlank()) {
            treeObject.addProperty("sign", src.sign)
        }

        if (src.picture.isNotBlank()) {
            treeObject.addProperty("picture", src.picture)
        }
        if(src.latitude != 0.0)
            treeObject.addProperty("latitude", src.latitude)
        if(src.longitude != 0.0)
            treeObject.addProperty("longitude", src.longitude)
        if(src.address.isNotBlank())
            treeObject.addProperty("address", src.address)

        return treeObject
    }
}
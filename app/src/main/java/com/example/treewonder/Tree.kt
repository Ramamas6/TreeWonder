package com.example.treewonder

import java.io.Serializable

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
    val addressBis: String,
): Serializable

package com.example.treewonder

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Functions for retrofit
 */

fun<T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
    val callBackKt = CallBackKt<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

class CallBackKt<T>: Callback<T> {

    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)
    }

}

/**
 * Functions to activate and return localisation
 */

fun getLocation(activity: Activity, callback: (LatLng?) -> Unit) {
    var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        if (isLocationEnabled(activity)) {
            mFusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
                val location: Location? = task.result
                if(location != null) {
                    callback.invoke(LatLng(location.latitude, location.longitude))
                } else {
                    callback.invoke(null)
                }
            }
        } else {
            Toast.makeText(activity, "Please turn on location", Toast.LENGTH_LONG).show()
            callback.invoke(null)
        }
    } else {
        requestPermissions(activity)
        callback.invoke(null)
    }
}

private fun requestPermissions(activity: Activity) {
    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),2)
}

private fun isLocationEnabled(activity: Activity): Boolean {
    val locationManager: LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER)
}



/**
 * Function returning if internet is enabled and activated
 */
fun isInternetEnabled(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}

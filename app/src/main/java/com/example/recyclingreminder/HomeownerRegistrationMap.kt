package com.example.recyclingreminder

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class HomeownerRegistrationMap : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        val TAG = "final"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        val HOMEOWNERS = "homeowners"
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var currentSnippet: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeowner_registration_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.homeowner_registration_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.i(GarbageCollectorDashboardActivity.TAG, "onMapReady")

        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap.uiSettings.setZoomControlsEnabled(true)
        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            placeMarkerOnMap(MarkerOptions().position(latLng))
        }

        mMap.setInfoWindowAdapter(object: GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View? {
                var v: View? = null
                try {
                    v = layoutInflater.inflate(R.layout.info_window, null)
                    var infoSnippets: TextView = v.findViewById(R.id.info_snippet)
                    infoSnippets.setText("Confirm: " + marker.snippet + "?")
                    currentSnippet = marker.snippet
                } catch (e: Exception) {e.printStackTrace()}

                return v
            }
        })

        mMap.setOnInfoWindowClickListener { marker ->
            var intent = Intent()
            intent.putExtra("ADDRESS", marker.snippet)

            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        setUpMap()
    }

    private fun setUpMap() {
        Log.i(GarbageCollectorDashboardActivity.TAG, "setUpMap")

        // request permission if do not have
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        mMap.isMyLocationEnabled = true // enables the my-location layer which draws a light blue
        // dot on the user’s location. It also adds a button to the
        // map that, when tapped, centers the map on the user’s location.

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(38.990016, -76.936253), 18f))
    }

    private fun placeMarkerOnMap(markerOptions: MarkerOptions) {
        Log.i(GarbageCollectorDashboardActivity.TAG, "placeMarkerOnMap")

        val address = getAddress(markerOptions.position)
        markerOptions.snippet(address)

        mMap.addMarker(markerOptions)
    }

    private fun getAddress(latLng: LatLng): String {
        Log.i(GarbageCollectorDashboardActivity.TAG, "getAddress")

        // Creates a Geocoder object to turn a latitude and longitude coordinate into an address and vice versa.
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        var addressText = ""

        try {
            // Asks the geocoder to get the address from the location passed to the method.
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addressText = addresses[0].subThoroughfare + " " + addresses[0].thoroughfare
        } catch (e: IOException) {
            Log.e(GarbageCollectorDashboardActivity.TAG, e.localizedMessage)
        }

        return addressText
    }
}
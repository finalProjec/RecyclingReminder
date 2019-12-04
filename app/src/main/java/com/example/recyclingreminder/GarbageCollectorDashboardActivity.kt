package com.example.recyclingreminder

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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


class GarbageCollectorDashboardActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    companion object {
        val TAG = "final"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var markers: HashMap<MarkerOptions, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garbage_collector_dashboard)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.i(TAG, "onMapReady")

        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        /*// Add a marker in Sydney and move the camera
        val collegePark = LatLng(38.98582939, -76.937329584)
        mMap.addMarker(MarkerOptions().position(collegePark).title("College Park"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(collegePark, 16.0f))*/

        mMap.uiSettings.setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)

        mMap.setInfoWindowAdapter(object: GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View? {
                var v: View? = null
                try {
                    v = layoutInflater.inflate(R.layout.info_window, null)
                    var infoSnippets: TextView = v.findViewById(R.id.info_snippet)
                    infoSnippets.setText(marker.snippet)

                    //set button listener here
                    var infoButton: Button = v.findViewById(R.id.info_button)
                    infoButton.setOnClickListener {  }
                } catch (e: Exception) {e.printStackTrace()}

                return v
            }
        })

        mMap.setOnInfoWindowClickListener { marker ->
            Toast.makeText(this, "Selected", Toast.LENGTH_SHORT).show();
        }




        //might need to hard code markers for each house
        generateMarkers()


        setUpMap()
    }


    private fun setUpMap() {
        Log.i(TAG, "setUpMap")

        // request permission if do not have
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true // enables the my-location layer which draws a light blue
        // dot on the user’s location. It also adds a button to the
        // map that, when tapped, centers the map on the user’s location.

        this.markers.forEach { key, _ ->
            placeMarkerOnMap(key)
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(38.972747, -76.937518), 18f))



//        // gives you the most recent location currently available.
//        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
//            // If you were able to retrieve the the most recent location, then move the camera to
//            // the user’s current location.
//            if (location != null) {
//                lastLocation = location
//                val currentLatLng = LatLng(38.972747, -76.937518)
//                placeMarkerOnMap(currentLatLng)
//                placeMarkerOnMap(LatLng(38.973857 + 0.0002, -76.942229 + 0.0002))
//                //placeMarkerOnMap(LatLng(location.latitude + 0.002, location.longitude + 0.001))
//                //placeMarkerOnMap(LatLng(location.latitude + 0.003, location.longitude + 0.001))
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
//            }
//        }
    }

    private fun placeMarkerOnMap(markerOptions: MarkerOptions) {
        Log.i(TAG, "placeMarkerOnMap")

        markerOptions.snippet(this.markers[markerOptions])

        mMap.addMarker(markerOptions)
    }

    private fun getAddress(latLng: LatLng): String {
        Log.i(TAG, "getAddress")

        // Creates a Geocoder object to turn a latitude and longitude coordinate into an address and vice versa.
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // Asks the geocoder to get the address from the location passed to the method.
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            // If the response contains any address, then append it to a string and return.
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, e.localizedMessage)
        }

        return addressText
    }

    private fun generateMarkers() {
        this.markers = HashMap<MarkerOptions, String>()

        markers[MarkerOptions().position(LatLng(38.972747, -76.937518))] = "6707 Baltimore Ave"
        markers[MarkerOptions().position(LatLng(38.972764, -76.937239))] = "4503 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972731, -76.936997))] = "4505 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972726, -76.936771))] = "4507 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972701, -76.936599))] = "4509 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972697, -76.936390))] = "4511 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972634, -76.936165))] = "4513 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972663, -76.935945))] = "4601 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972663, -76.935730))] = "4603 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972642, -76.935499))] = "4605 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972621, -76.935279))] = "4607 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972608, -76.934962))] = "4609 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972579, -76.934726))] = "4611 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972596, -76.934517))] = "4613 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972613, -76.934297))] = "4615 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972613, -76.934297))] = "4615 Amherst Rd"
        markers[MarkerOptions().position(LatLng(38.972263, -76.934265))] = "6704 Rhode Island Ave"
    }

    override fun onMarkerClick(p0: Marker?) = false
}

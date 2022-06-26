package com.example.tripy

import android.Manifest
import android.R.attr.password
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.tripy.MySingleton.rememberLastCurrentLatLong
import com.example.tripy.databinding.FragmentMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.math.roundToInt
import kotlin.properties.Delegates


object MySingleton {
    lateinit var rememberLastCurrentLatLong: LatLng

}

class MainFragment : Fragment(),OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener{
    companion object{
        var flag = 0
    }
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentMainBinding
    private lateinit var locationRequest: LocationRequest
    private lateinit var currentLatLong : LatLng
    private lateinit var  description : String
    private var fragmentFilterArrayList = ArrayList<String>()   // saving in array list the categories the use chose in FilterAttraction fragment


    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val TAG = "MapActivity"
    private val TAG_DB = "Database"
    private val REQUEST_CHECK_SETTINGS = 10001

    private lateinit var mMap:GoogleMap

    // current location vars
    private lateinit var lastLocation1 : Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // firebase realtime database variable
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        /*
        if(isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            requestLocationPermission()
        }

         */
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()

        if(isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            requestLocationPermission()
        }


        return binding.root
    }

    // checking if google services install on the device
    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(requireContext())
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(requireActivity(), status, 2404)?.show()
            }
            return false
        }
        Log.d(TAG, "isServicesOK: Google Play Services is working")
        return true
    }

    private fun requestLocationPermission()
    {
        Log.d(TAG, "In requestLocationPermission")

        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION))
        {
            android.app.AlertDialog.Builder(requireContext()).setTitle("" +
                    "Location permission denied")
                    .setMessage("Location permission required for the app to work properly")
                    .setPositiveButton("ok") { dialogInterface, i ->
                        Log.d(TAG, "if was true")
                        requestPermissions(permissions,LOCATION_PERMISSION_REQUEST_CODE)
                    }

                    .setNegativeButton("cancel") { dialogInterface, i -> dialogInterface.dismiss() }
                    .create().show()
        }
        else
        {
            Log.d(TAG, "if was false")
            requestPermissions(permissions,LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        Log.d(TAG, "In onRequestPermission")
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG,"Permission granted");
                checkEnableGps()
            }
            else
            {
                Log.d(TAG,"Permission denied")
            }
        }
    }

    // This function will call only if they the user enabled Location permission
    private fun checkEnableGps() {
        Log.d(TAG, "checkEnableGps: in function")
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY // high accuracy location
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        // Check if the device GPS enable or not
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(requireActivity())
                .checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                Log.d(TAG, "GPS already on")
                initMap()

                // This e is for the ApiException error in case we get one
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        // this error means device gps is turned off and open dialog to turn on location. Answer we'll be handle onActivityResult
                        val resolvableApiException = e as ResolvableApiException
                        startIntentSenderForResult(resolvableApiException.resolution.intentSender, REQUEST_CHECK_SETTINGS, null, 0, 0,0,null)
                    } catch (ex: SendIntentException) {
                        ex.printStackTrace()
                    }
                    //Device does not have location so we'll ignore it
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    // Handle the dialog answer from checkEnableGps()
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(TAG, "onActivityResult: GPS ON")
                    initMap()
                }
                Activity.RESULT_CANCELED -> {
                    Log.d(TAG, "onActivityResult: GPS is required  to be turned on ")
                }
            }
        }
    }

    private fun initMap()
    {
        Log.d(TAG, "initMap: initializing map")
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        Log.d(TAG, "onMapReady: map is ready")
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = false
        getDeviceCurrentLocation()

        // 1 = אומר שהמשתמש נכנס לעמוד של הפילטרים , אם הוא לא בחר כלום אז הפונצנקציה שמראה את כל האטרקציות נקראת , אחרת הוא מפלטר לפי הפונקציה השנייה עם המערך של הקטגוריות שהוא בחר שחוזר מהפרגמנט
        if(flag == 1) {
            fragmentFilterArrayList = retrieveCategoryFilter()
            if(fragmentFilterArrayList.isNotEmpty())
                readDataFromFirebaseByCategory(mMap,fragmentFilterArrayList)
            else
                readDataFromFirebase(mMap)
        }
        else
            readDataFromFirebase(mMap)

        mMap.setOnInfoWindowClickListener(this)

    }


    private fun getDeviceCurrentLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location")
        //lateinit var currentLatLong: LatLng

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "getDeviceLocation: no permission")
            return
        }
        mMap.isMyLocationEnabled = true
      //  Log.d(TAG, "getDeviceLocation: $myVariable")
       // rememberLastLocation = fusedLocationProviderClient.getLastLocation()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if(location != null)
            {
                Log.d(TAG, "getDeviceLocation: In if(location != null)")
                lastLocation1 = location
               // rememberLastLocation = lastLocation
                currentLatLong = LatLng(location.latitude,location.longitude)
                rememberLastCurrentLatLong = currentLatLong
                moveCamera(currentLatLong,11f)
                // lat = location.latitude
            }
            else
                Log.d(TAG, "getDeviceCurrentLocation: current location is null")
        }

    }


    private fun moveCamera(latLng: LatLng, zoom: Float) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
    }


    fun isRTL(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            (context.resources.configuration.layoutDirection
                    === View.LAYOUT_DIRECTION_RTL)
            // Another way:
            // Define a boolean resource as "true" in res/values-ldrtl
            // and "false" in res/values
            // return context.getResources().getBoolean(R.bool.is_right_to_left);
        } else {
            false
        }
    }

    // read the data and add a marker on the map
    private fun readDataFromFirebaseByCategory(googleMap: GoogleMap,categoryFilterArrayList : ArrayList<String>) {
        Log.w(TAG_DB, "In readDataFromFirebaseByCategory")
        lateinit var  distance : String
       // val categoryFilterArrayList: ArrayList<String> = retrieveCategoryFilter()
        googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(requireContext()))

        database = FirebaseDatabase.getInstance().getReference("Attractions")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnap in dataSnapshot.children) {
                    val hebrewCategory = dataSnap.child("Category").value.toString()
                    val englishCategoryName = dataSnap.child("English Category").value.toString()

                    for (categoryFilter in categoryFilterArrayList)
                        if (categoryFilter == englishCategoryName || categoryFilter == hebrewCategory) {

                            val hebrewName = dataSnap.child("Hebrew Name").value.toString()

                            val englishAttName = dataSnap.child("Name").value.toString()

                            val latitude = dataSnap.child("latitude").value.toString().toDouble()

                            val longitude = dataSnap.child("Longitude").value.toString().toDouble()
                            //latLngCoordinates.add(LatLng(latitude,longitude))

                            //description = dataSnap.child("description").value.toString()

                            if (::currentLatLong.isInitialized) {
                                //Log.d(TAG_DB, "getDeviceLocation: if = True --> lat = ${currentLatLong.latitude}, ${currentLatLong.longitude}")
                                distance = getDistance(currentLatLong.latitude, currentLatLong.longitude, latitude, longitude)
                            } else {
                                //Log.d(TAG_DB, "getDeviceLocation: if = false --> lat = ${rememberLastCurrentLatLong.latitude}, ${rememberLastCurrentLatLong.longitude}")
                                currentLatLong = rememberLastCurrentLatLong
                                distance = getDistance(currentLatLong.latitude, currentLatLong.longitude, latitude, longitude)
                            }


                            // googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(requireContext()))

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isRTL(activity!!)) {
                                val snippet = " שם אטרקציה:  $hebrewName\n קטגוריה:  $hebrewCategory\n מרחק ליעד:  $distance\n"
                                googleMap.addMarker(MarkerOptions().position(LatLng(latitude, longitude))
                                        .title(hebrewName)
                                        .snippet(snippet))
                            } else {
                                val snippet = "Attraction Name: $englishAttName\nCategory: $englishCategoryName\ndistance: $distance\n"
                                googleMap.addMarker(MarkerOptions().position(LatLng(latitude, longitude))
                                        .title(englishAttName)
                                        .snippet(snippet))
                            }
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG_DB, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    // retrieve the specific categories the user chose from the fragment FilterAttraction
    private fun retrieveCategoryFilter() : ArrayList<String> {
        var retrieveCategoryArrayList = ArrayList<String>()
        if (flag == 1) { // in case the user did not pick any category filter (all checkboxes are empty)
            retrieveCategoryArrayList = requireArguments().getStringArrayList("categoryFilerKey") as ArrayList<String>
            return retrieveCategoryArrayList
        }
        return retrieveCategoryArrayList
        }


    private fun readDataFromFirebase(googleMap: GoogleMap)
    {
        lateinit var  distance : String
        Log.w(TAG_DB, "In readDataFromFirebase")
        googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(requireContext()))

        database = FirebaseDatabase.getInstance().getReference("Attractions")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnap in dataSnapshot.children) {
                    val category = dataSnap.child("Category").value.toString()
                    val englishCategoryName = dataSnap.child("English Category").value.toString()

               //     if (selectedCategory == englishCategoryName) {
                  //      count++
                       // Log.w(TAG_DB, "$count")

                        val hebrewName = dataSnap.child("Hebrew Name").value.toString()

                        val englishAttName = dataSnap.child("Name").value.toString()

                        val latitude = dataSnap.child("latitude").value.toString().toDouble()

                        val longitude = dataSnap.child("Longitude").value.toString().toDouble()
                        //latLngCoordinates.add(LatLng(latitude,longitude))

                      //  description = dataSnap.child("description").value.toString()

                    if (::currentLatLong.isInitialized) {
                        //Log.d(TAG_DB, "getDeviceLocation: if = True --> lat = ${currentLatLong.latitude}, ${currentLatLong.longitude}")
                        distance = getDistance(currentLatLong.latitude, currentLatLong.longitude, latitude, longitude)
                    } else {
                        //Log.d(TAG_DB, "getDeviceLocation: if = false --> lat = ${rememberLastCurrentLatLong.latitude}, ${rememberLastCurrentLatLong.longitude}")
                        currentLatLong = rememberLastCurrentLatLong
                        distance = getDistance(currentLatLong.latitude, currentLatLong.longitude, latitude, longitude)
                    }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isRTL(activity!!)) {
                            val snippet = " שם אטרקציה:  $hebrewName\n קטגוריה:  $category\n מרחק ליעד:  $distance\n"
                            googleMap.addMarker(MarkerOptions().position(LatLng(latitude, longitude))
                                    .title(hebrewName)
                                    .snippet(snippet))
                        } else {
                            val snippet = "Attraction Name: $englishAttName\nCategory: $englishCategoryName\ndistance: $distance\n"
                            googleMap.addMarker(MarkerOptions().position(LatLng(latitude, longitude))
                                    .title(englishAttName)
                                    .snippet(snippet))
                        }
             //       }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG_DB, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }



    override fun onInfoWindowClick(p0: Marker) {
        //Log.w(TAG_DB, "onInfoWindowClick: In function, description = ${p0.title})" )
        /* TODO : Option 1: Click on the info window will show more info about the attraction
                  Option 2: Click on the info window will open the browser on the blog website with more details about the attractions
                  Option 3: Click on the info window will make route from our location to the attraction
        */

        //val intent = Intent(Intent.ACTION_VIEW, Uri.parse(description))
        // startActivity(intent)
    }





    private fun getDistance(startLat: Double, startLon: Double, endLat: Double, endLon: Double): String
    {
        val results = FloatArray(1)
        val roundOff : Float
        val distanceInKm : Float
        Location.distanceBetween(startLat, startLon, endLat, endLon, results)

        if(results[0]>1000)
        {
            distanceInKm = results[0] / 1000
            roundOff = ((distanceInKm * 100.0).roundToInt() / 100.0).toFloat()
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isRTL(activity!!))
                "$roundOff קילומטר "
            else
                "$roundOff kilometer"
        }
        else
        {
            distanceInKm = results[0] / 1000
            roundOff = ((distanceInKm * 100.0).roundToInt() / 100.0).toFloat()
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isRTL(activity!!))
                "$roundOff מטרים "
            else
                "$roundOff meter"
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.drawer_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                // navigate to settings screen
                firebaseAuth.signOut()
                findNavController(binding.root).navigate(R.id.action_mainFragment_to_loginFragment)
                flag = 0
                true
            }
            R.id.helpus -> {
                findNavController(binding.root).navigate(R.id.action_mainFragment_to_help_us_improve)
                flag = 0
                return true
            }
            R.id.update -> {
                findNavController(binding.root).navigate(R.id.action_mainFragment_to_fragment_keep_us_posted)
                flag = 0
                return true
            }
            R.id.filter -> {
                flag = 1
                Log.w("filter", "in tafrit flag = $flag")
                findNavController(binding.root).navigate(R.id.action_mainFragment_to_filtterAttraction)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}


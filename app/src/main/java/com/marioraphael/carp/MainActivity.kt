package com.marioraphael.carp.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayout
import com.marioraphael.carp.R

class MainActivity : AppCompatActivity() {override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
    val viewPager: ViewPager = findViewById(R.id.view_pager)
    viewPager.adapter = sectionsPagerAdapter
    val tabs: TabLayout = findViewById(R.id.tabs)
    tabs.setupWithViewPager(viewPager)
}
}

private val TAB_TITLES = arrayOf(
    R.string.tab_text_map,
    R.string.tab_text_driver_profiles,
    R.string.tab_text_home,
    R.string.tab_text_contact,
    R.string.tab_text_about
)

/**

A [FragmentPagerAdapter] that returns a fragment corresponding to

one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return MapFragment.newInstance()
            1 -> return DriverProfilesFragment.newInstance()
            2 -> return HomeFragment.newInstance()
            3 -> return ContactFragment.newInstance()
            4 -> return AboutFragment.newInstance()
            else -> return PlaceholderFragment.newInstance(position + 1)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 5
    }
}

class MapFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as MapFragment

        // Set up OnMapReadyCallback
        mapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
            // Set options for the map
            val mapOptions = GoogleMapOptions()
            mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true)

            // Add the map to the fragment
            googleMap.addMapOptions(mapOptions)

            // Set up marker for current location
            val currentLocation = LatLng(-34.0, 151.0)
            val markerOptions = MarkerOptions()
                .position(currentLocation)
                .title("Current Location")
            googleMap.addMarker(markerOptions)

            // Move the camera to the current location
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 12f)
            googleMap.moveCamera(cameraUpdate)
        })

        return rootView





class DriverProfilesFragment : Fragment() {
    // Declare views
    private lateinit var textViewName: TextView
    private lateinit var textViewLocation: TextView
    private lateinit var imageViewProfile: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewError: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_driver_profiles, container, false)


        textViewName = rootView.findViewById(R.id.text_view_name)
        textViewLocation = rootView.findViewById(R.id.text_view_location)
        imageViewProfile = rootView.findViewById(R.id.image_view_profile)
        progressBar = rootView.findViewById(R.id.progress_bar)
        textViewError = rootView.findViewById(R.id.text_view_error)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://fake-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getDriverProfile()
            .enqueue(object : Callback<DriverProfile> {
                override fun onResponse(call: Call<DriverProfile>, response: Response<DriverProfile>) {
                    if (response.isSuccessful) {

                        val driverProfile = response.body()
                        val name = driverProfile?.name
                        val location = driverProfile?.location
                        val profileImageUrl = driverProfile?.profileImageUrl

                        textViewName.text = name
                        textViewLocation.text = location
                        Glide.with(this@DriverProfilesFragment)
                            .load(profileImageUrl)
                            .into(imageViewProfile)


                        progressBar.visibility = View.GONE
                        textViewName.visibility = View.VISIBLE
                        textViewLocation.visibility = View.VISIBLE
                        imageViewProfile.visibility = View.VISIBLE
                    } else {

                        handleError()
                    }
                }

                override fun onFailure(call: Call<DriverProfile>, t: Throwable) {

                    handleError()
                }
            })

        return rootView
    }



class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView
    }companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}

class ContactFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_contact, container, false)
        return rootView
    }companion object {
        fun newInstance(): ContactFragment {
            return ContactFragment()
        }
    }
}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_about, container, false)

        // Get reference to database helper
        databaseHelper = DatabaseHelper(requireContext())

        // Query database for statistics
        val db = databaseHelper.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        // Process the query results and display the statistics in the fragment
        // For example:
        val numRidesTextView = rootView.findViewById<TextView>(R.id.num_rides)
        if (cursor.moveToFirst()) {
            numRidesTextView.text = cursor.getInt(cursor.getColumnIndex(COL_NUM_RIDES)).toString()
        }
        cursor.close()

        return rootView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val date = "2022-01-01"
        val numRides = 10
        val milesDriven = 100.0
        databaseHelper.insertStat(date, numRides, milesDriven)
    private fun handleError() {
        // Hide progress bar and show error message
        progressBar.visibility = View.GONE
        textViewError.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(): DriverProfilesFragment {
            return DriverProfilesFragment()
        }
    }

}
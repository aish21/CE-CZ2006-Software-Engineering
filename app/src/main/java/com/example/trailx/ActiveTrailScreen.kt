package com.example.trailx

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.activity_active_trail_screen.*
import org.json.JSONObject
import java.util.*
import com.android.volley.toolbox.StringRequest as StringRequest1


@Suppress("DEPRECATED_IDENTITY_EQUALS", "DEPRECATION")
class ActiveTrailScreen : AppCompatActivity(), OnMapReadyCallback, PermissionsListener, SensorEventListener {

    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private lateinit var mapboxMap: MapboxMap
    private lateinit var mapView:MapView
    private lateinit var routeCoordinates:List<Point>
    private var weather_url1 = "https://api.weatherbit.io/v2.0/current?&city=Singapore&country=Singapore&key=c53e108c3fe945af89c27144a19863a9"
    //var api_id1 = "c53e108c3fe945af89c27144a19863a9"
    private lateinit var weatherIcon:ImageView
    internal lateinit var timer:TextView
    private lateinit var start:Button
    private lateinit var pause:Button
    private lateinit var reset:Button
    val userFireBase = Firebase.auth.currentUser
    val userDatabase = UserDatabase()
    val user = userFireBase?.email?.let { userDatabase.getUserByEmail(it) }
    val height = user?.height
    val weight = user?.weight
    val age = user?.age
    internal var MillisecondTime:Long = 0
    internal var StartTime:Long = 0
    internal var TimeBuff:Long = 0
    internal var UpdateTime = 0L
    internal lateinit var handler:Handler
    internal var Seconds:Int = 0
    internal var Minutes:Int = 0
    internal var MilliSeconds:Int = 0
    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null
    private var accel: Sensor? = null
    private val TEXT_NUM_STEPS = "Number of Steps: "
    private var numSteps = 0
    private var firstCall = false
    var step_count: TextView? = null
    var calories: TextView? = null
    private var runnable:Runnable = object:Runnable {
        override fun run() {
            if(global.checkState == 0){
                MillisecondTime = SystemClock.uptimeMillis() - StartTime
                UpdateTime = TimeBuff + MillisecondTime
                Seconds = (UpdateTime / 1000).toInt()
                Minutes = Seconds / 60
                Seconds = Seconds % 60
                MilliSeconds = (UpdateTime % 1000).toInt()
                timer.text = ("" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds))
                handler.postDelayed(this, 0)
            } else{
                MillisecondTime = SystemClock.uptimeMillis() - StartTime
                UpdateTime = TimeBuff + MillisecondTime
                Seconds = (UpdateTime / 1000).toInt()
                Minutes = global.sec / 60
                Seconds = global.sec % 60
                MilliSeconds = (UpdateTime % 1000).toInt()
                timer.text = ("" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds))
                handler.postDelayed(this, 0)
            }
        }
    }

    fun step() {
        var cal = 0.0
        numSteps++
        step_count?.text = TEXT_NUM_STEPS + numSteps
        val step = step_count?.text.toString()
        cal = Integer.parseInt(step).toDouble() * 0.4 * weight!!.toDouble() * age!!.toDouble() / (height!! * height.toDouble())
        calories?.text = cal.toString()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onSensorChanged(event: SensorEvent) {
        if (!firstCall) {
            global.initialSteps = event.values[0].toInt()
            firstCall = true
        }
        else {
            step_count?.text = (event.values[0] - global.initialSteps).toString()
            //val cal = Integer.parseInt(step_count?.text as String)* 0.4 * weight!!.toDouble() * age!!.toDouble() / (height!! * height.toDouble())
            //calories?.text = cal.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token))
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION) , 1)
        }

        setContentView(R.layout.activity_active_trail_screen)
        supportActionBar?.hide()
        timer = findViewById<TextView>(R.id.timer_active_trail)
        start = findViewById<Button>(R.id.play_bt_active_trail)
        pause = findViewById<Button>(R.id.pause_bt_active_trail)
        reset = findViewById<Button>(R.id.end_bt_active_trail)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Log.d("Sensors", "No Step Counter")
        }
        else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        step_count = findViewById<TextView>(R.id.distance_active_trail)
        step_count?.text = TEXT_NUM_STEPS
        calories = findViewById<TextView>(R.id.calories_active_trail)
        calories?.text = "0.0"

        // This contains the MapView in XML and needs to be called after the access token is configured.
        mapView = findViewById(R.id.mapView_active_trail)
        mapView_active_trail?.onCreate(savedInstanceState)

        mapView.getMapAsync(this)


        weatherIcon = findViewById<ImageView>(R.id.weather_icon_active_trail)
        getWeather()

        handler = Handler()
        start.setOnClickListener {
            StartTime = SystemClock.uptimeMillis()
            handler.postDelayed(runnable, 0)
            reset.isEnabled = false
        }
        pause.setOnClickListener {
            TimeBuff += MillisecondTime
            handler.removeCallbacks(runnable)
            reset.isEnabled = true
        }
        reset.setOnClickListener {
            MillisecondTime = 0L
            StartTime = 0L
            TimeBuff = 0L
            UpdateTime = 0L
            Seconds = 0
            Minutes = 0
            MilliSeconds = 0
            timer.text = "00:00:00"
        }

        val back_to_home_bt_bar = findViewById<Button>(R.id.back_to_home_bt_active_trail)
        back_to_home_bt_bar.setOnClickListener {
            val intent_back_to_home_bt_bar = Intent(this, HomeScreen::class.java)
            global.checkState = 1
            TimeBuff += MillisecondTime
            handler.removeCallbacks(runnable)
            reset.isEnabled = true
            global.sec = Seconds
            startActivity(intent_back_to_home_bt_bar)
        }
        val settings_bt_bar = findViewById<Button>(R.id.settings_bt_active_trail)
        settings_bt_bar.setOnClickListener {
            val intent_settings_bt_bar = Intent(this, SettingsScreen::class.java)
            global.checkState = 1
            TimeBuff += MillisecondTime
            handler.removeCallbacks(runnable)
            reset.isEnabled = true
            global.sec = Seconds
            startActivity(intent_settings_bt_bar)
        }
        val discover_new_trails_bt_bar =
            findViewById<Button>(R.id.discover_new_trails_bt_active_trail)
        discover_new_trails_bt_bar.setOnClickListener {
            val intent_discover_new_trails_bt_bar = Intent(
                this,
                DiscoverNewTrailsScreen::class.java
            )
            global.checkState = 1
            TimeBuff += MillisecondTime
            handler.removeCallbacks(runnable)
            reset.isEnabled = true
            global.sec = Seconds
            startActivity(intent_discover_new_trails_bt_bar)
        }
        val active_trail_bt_bar = findViewById<Button>(R.id.active_trail_bt_active_trail)
        active_trail_bt_bar.setOnClickListener {
            val intent_active_trail_bt_bar = Intent(this, ActiveTrailScreen::class.java)
            global.checkState = 1
            TimeBuff += MillisecondTime
            handler.removeCallbacks(runnable)
            reset.isEnabled = true
            global.sec = Seconds
            startActivity(intent_active_trail_bt_bar)
        }
        val my_trails_bt_bar = findViewById<Button>(R.id.my_trails_bt_active_trail)
        my_trails_bt_bar.setOnClickListener {
            val intent_my_trails_bt_bar = Intent(this, MyTrailsScreen::class.java)
            global.checkState = 1
            TimeBuff += MillisecondTime
            handler.removeCallbacks(runnable)
            reset.isEnabled = true
            global.sec = Seconds
            startActivity(intent_my_trails_bt_bar)
        }
        val music_bt_bar = findViewById<Button>(R.id.music_bt_active_trail)
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar = Intent(this, MusicScreen::class.java)
            global.checkState = 1
            TimeBuff += MillisecondTime
            handler.removeCallbacks(runnable)
            reset.isEnabled = true
            global.sec = Seconds
            startActivity(intent_music_bt_bar)
        }
    }

    @SuppressLint("LogNotTimber")
    fun getWeather(){
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1

        // Request a string response from the provided URL.
        val stringReq = StringRequest1(Request.Method.GET, url,
            { response ->
                Log.e("lat", response.toString())

                // get the JSON object
                val obj = JSONObject(response)

                // get the Array from obj of name - "data"
                val arr = obj.getJSONArray("data")
                Log.e("lat obj1", arr.toString())

                // get the JSON object from the array at index position 0
                val obj2 = arr.getJSONObject(0)
                Log.e("lat obj2", obj2.toString())

                // set the temperature and the city name using getString() function
                //Log.e("code", obj3.getString("code"))
                //Toast.makeText(this, obj2.getString("temp"), Toast.LENGTH_LONG).show()
                //textView.text = obj2.getString("temp") + " deg Celcius in " + obj2.getString("city_name")

                val finalvalueweather = obj2.getJSONObject("weather")
                Log.e("finalvalueweather", finalvalueweather.toString())
                Log.e("code", finalvalueweather.getString("code"))

                val weathercode: Int = finalvalueweather.getString("code").toInt()

                if (weathercode == 200 || weathercode == 201 || weathercode == 202 || weathercode == 230 || weathercode == 231 || weathercode == 232 || weathercode == 233) {
                    weatherIcon.setImageResource(R.drawable.thunderstorm)
                } else if (weathercode == 300 || weathercode == 301 || weathercode == 302 || weathercode == 500 || weathercode == 501 || weathercode == 502 || weathercode == 511 || weathercode == 520 || weathercode == 521 || weathercode == 522) {
                    weatherIcon.setImageResource(R.drawable.rainy)
                } else if (weathercode == 801 || weathercode == 802 || weathercode == 803 || weathercode == 804 || weathercode == 900) {
                    weatherIcon.setImageResource(R.drawable.cloudy)
                } else {
                    weatherIcon.setImageResource(R.drawable.sunny)
                }
            },
            // In case of any error
            { Toast.makeText(this, "That didn't work!!", Toast.LENGTH_LONG).show() })
        queue.add(stringReq)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.OUTDOORS) {
            enableLocationComponent(it)

            if(global.choice == 1){
                initRouteCoordinates01()
                global.telok = true
            } else if(global.choice == 2){
                initRouteCoordinates03()
                global.bukit = true
            }else if(global.choice == 3){
                initRouteCoordinates02()
                global.bedok = true
            }else{
                Toast.makeText(this, "No Active Trail at the moment!", Toast.LENGTH_SHORT).show()
            }
            // Create the LineString from the list of coordinates and then make a GeoJSON
            // FeatureCollection so we can add the line to our map as a layer.
            it.addSource(
                GeoJsonSource(
                    "line-source",
                    FeatureCollection.fromFeatures(
                        arrayOf<Feature>(
                            Feature.fromGeometry(
                                LineString.fromLngLats(routeCoordinates)
                            )
                        )
                    )
                )
            )
            // The layer properties for our line. This is where we make the line dotted, set the
            // color, etc.
            it.addLayer(
                LineLayer("linelayer", "line-source").withProperties(
                    PropertyFactory.lineDasharray(arrayOf<Float>(0.01f, 2f)),
                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                    PropertyFactory.lineWidth(5f),
                    PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(
                this,
                loadedMapStyle
            )
                .locationComponentOptions(customLocationComponentOptions)
                .build()

            // Get an instance of the LocationComponent and then adjust its settings
            mapboxMap.locationComponent.apply {
                // Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)

                // Enable to make the LocationComponent visible
                isLocationComponentEnabled = true

                // Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING

                // Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap.style!!)
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    fun initRouteCoordinates01() {
        // Telok Ayer - ClubStreet
        routeCoordinates = ArrayList<Point>()
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.847904, 1.281251))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.847890, 1.281257))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.847869, 1.281268))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.847840, 1.281288))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.847825, 1.281299))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.847800, 1.281312))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84776, 1.28134))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84763, 1.28143))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84757, 1.28142))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84751, 1.28142))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84746, 1.28142))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84742, 1.28142))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84735, 1.28144))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84701, 1.2809))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84694, 1.28077))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84689, 1.28064))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84671, 1.28018))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84667, 1.28006))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84666, 1.28001))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84646, 1.28008))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84637, 1.28012))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84634, 1.28014))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84632, 1.28017))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84623, 1.28032))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84616, 1.28042))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84615, 1.2805))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84607, 1.28063))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84597, 1.28077))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84585, 1.28095))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28137))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28139))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.2814))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8456, 1.28143))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28147))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84558, 1.28148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.2815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.28151))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8455247, 1.2815249))
    }

    fun initRouteCoordinates02() {
        // Bedok Reservoir loop
        routeCoordinates = ArrayList<Point>()
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93384,
                1.33979
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93382,
                1.33974
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9338,
                1.33971
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93378,
                1.33969
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93375,
                1.33966
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93371,
                1.33962
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93368,
                1.33959
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93364,
                1.33956
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93359,
                1.33952
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93355,
                1.33949
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93353,
                1.33947
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93349,
                1.33945
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93344,
                1.33941
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93344,
                1.33941
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93345,
                1.33937
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93346,
                1.33935
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93347,
                1.33931
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93347,
                1.33924
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93345,
                1.3391
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93344,
                1.33904
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93341,
                1.33892
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93339,
                1.33889
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93338,
                1.33886
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93333,
                1.33877
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93328,
                1.33873
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93323,
                1.33867
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93298,
                1.33846
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93294,
                1.33843
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9329,
                1.33839
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93281,
                1.33834
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93273,
                1.33829
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93267,
                1.33827
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9326,
                1.33825
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93251,
                1.33823
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93243,
                1.33822
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93238,
                1.33821
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9323,
                1.33822
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93218,
                1.33822
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93208,
                1.33824
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93193,
                1.33827
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93177,
                1.3383
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93157,
                1.33835
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93079,
                1.33857
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93015,
                1.3388
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92963,
                1.33897
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92912,
                1.33914
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92878,
                1.33924
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9286,
                1.3393
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9282,
                1.33944
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92807,
                1.33947
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92796,
                1.3395
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92787,
                1.33951
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92768,
                1.33954
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92751,
                1.33956
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92748,
                1.33956
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92743,
                1.33955
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92738,
                1.33954
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92732,
                1.33951
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92722,
                1.33947
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92677,
                1.33924
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92632,
                1.339
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92603,
                1.33886
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92598,
                1.33884
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92594,
                1.33882
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92589,
                1.3388
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92583,
                1.33879
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92578,
                1.33879
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92574,
                1.33879
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9257,
                1.33879
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92564,
                1.33879
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92556,
                1.3388
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92548,
                1.33881
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92518,
                1.33888
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92485,
                1.33896
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92455,
                1.33904
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92395,
                1.33919
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92387,
                1.33921
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92309,
                1.33939
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92306,
                1.3394
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9222,
                1.3396
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92215,
                1.33961
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92202,
                1.33964
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92188,
                1.33968
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92177,
                1.33972
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92168,
                1.33975
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9216,
                1.33979
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92144,
                1.33987
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92129,
                1.33996
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92112,
                1.34007
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92096,
                1.34019
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92088,
                1.34027
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92081,
                1.34033
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92073,
                1.34042
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92066,
                1.34051
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92055,
                1.34066
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92036,
                1.34103
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92023,
                1.34129
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92006,
                1.34165
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91994,
                1.34191
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91985,
                1.34208
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91978,
                1.34219
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91973,
                1.34228
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91969,
                1.34234
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91965,
                1.3424
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91934,
                1.34296
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91922,
                1.34321
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9192,
                1.34337
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9192,
                1.34349
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9192,
                1.34352
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91937,
                1.34396
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91956,
                1.34424
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9197,
                1.34446
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.91978,
                1.34458
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9198,
                1.34462
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92008,
                1.34519
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92016,
                1.34533
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92025,
                1.34541
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92036,
                1.34548
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92051,
                1.34554
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92081,
                1.34561
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92108,
                1.34569
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92142,
                1.34579
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92194,
                1.34594
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92264,
                1.34613
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92433,
                1.34658
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92433,
                1.34658
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92459,
                1.34715
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92459,
                1.34715
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92522,
                1.34722
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92528,
                1.34722
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92531,
                1.34722
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92533,
                1.34722
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92535,
                1.34722
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92538,
                1.34722
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92544,
                1.34721
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92549,
                1.34719
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92556,
                1.34718
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92562,
                1.34715
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92572,
                1.3471
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92584,
                1.34704
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92596,
                1.34698
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92612,
                1.3469
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92634,
                1.34678
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92637,
                1.34676
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92663,
                1.34662
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92682,
                1.34649
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92705,
                1.34631
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92717,
                1.34621
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92727,
                1.34612
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92734,
                1.34608
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92744,
                1.34601
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92753,
                1.34593
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92759,
                1.34589
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92769,
                1.34583
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92773,
                1.34582
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92775,
                1.3458
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9278,
                1.34578
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92784,
                1.34576
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92787,
                1.34575
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92789,
                1.34574
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92795,
                1.3457
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.928,
                1.34567
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92815,
                1.34554
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92823,
                1.34547
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9283,
                1.3454
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92838,
                1.34535
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9284,
                1.34534
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92842,
                1.34533
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92843,
                1.34532
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92845,
                1.34532
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92847,
                1.34532
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9285,
                1.34532
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92855,
                1.34532
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92858,
                1.34532
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92862,
                1.34532
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92865,
                1.34531
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92869,
                1.3453
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92872,
                1.34529
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92873,
                1.34529
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92877,
                1.34527
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92881,
                1.34525
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92896,
                1.34518
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92898,
                1.34517
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92901,
                1.34516
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92904,
                1.34514
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92923,
                1.34505
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92932,
                1.34501
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92943,
                1.34495
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9295,
                1.34491
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92955,
                1.34487
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92959,
                1.34483
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92962,
                1.34481
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92965,
                1.34478
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92967,
                1.34476
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92971,
                1.34471
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92975,
                1.34465
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.92994,
                1.34428
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93006,
                1.34407
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9301,
                1.34399
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93015,
                1.34392
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93019,
                1.34389
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9304,
                1.34369
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93051,
                1.34359
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93064,
                1.34346
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93091,
                1.34317
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93104,
                1.34303
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93119,
                1.34288
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93126,
                1.34281
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93146,
                1.34267
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93159,
                1.34257
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93162,
                1.34255
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93173,
                1.34246
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93187,
                1.34235
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93227,
                1.34205
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93246,
                1.3419
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93248,
                1.34189
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93252,
                1.34186
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93269,
                1.34175
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9328,
                1.34168
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93291,
                1.3416
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93305,
                1.34148
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93318,
                1.34138
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93331,
                1.34127
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93333,
                1.34126
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93345,
                1.34116
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93346,
                1.34114
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9335,
                1.34111
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93369,
                1.34095
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9337,
                1.34094
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93373,
                1.34091
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93375,
                1.34088
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93378,
                1.34084
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93382,
                1.34078
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93383,
                1.34075
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93392,
                1.34062
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93395,
                1.34056
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93398,
                1.34051
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.934,
                1.34048
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93401,
                1.34046
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93402,
                1.34043
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93403,
                1.34039
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93404,
                1.34034
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93405,
                1.34028
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93406,
                1.34023
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93408,
                1.34019
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93411,
                1.3401
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93412,
                1.3401
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93414,
                1.34005
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9343,
                1.33982
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93442,
                1.33966
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93442,
                1.33966
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9344,
                1.33963
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9344,
                1.3396
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.9344,
                1.3396
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93443,
                1.33956
            )
        )
        (routeCoordinates as ArrayList<Point>).add(
            Point.fromLngLat(
                103.93445,
                1.33955
            )
        )
    }

    fun initRouteCoordinates03() {
        // Bukit Batok Nature Park
        routeCoordinates = ArrayList<Point>()
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76481, 1.35065))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76478, 1.35066))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76477, 1.35066))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76476, 1.35066))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76474, 1.35066))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76473, 1.35065))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76471, 1.35065))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7647, 1.35063))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76468, 1.35062))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76468, 1.3506))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76467, 1.35058))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76466, 1.35057))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76466, 1.35055))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76466, 1.35053))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76465, 1.35045))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76434, 1.35012))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76407, 1.34986))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76393, 1.34973))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76373, 1.34958))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76368, 1.34955))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76326, 1.34926))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76283, 1.34904))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76263, 1.34893))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76246, 1.34885))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76238, 1.34881))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7623, 1.34876))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.762, 1.34855))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76185, 1.34845))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76183, 1.34843))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7614, 1.34813))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76129, 1.34807))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76103, 1.34792))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76096, 1.34788))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76092, 1.34787))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76092, 1.34787))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76092, 1.34782))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76093, 1.34769))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76093, 1.34769))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76117, 1.3475))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7612, 1.34743))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76129, 1.34737))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76148, 1.34725))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76174, 1.34711))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76189, 1.34706))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76223, 1.34695))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76225, 1.34695))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76252, 1.3469))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76262, 1.34689))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76279, 1.34689))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76298, 1.3469))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76315, 1.34691))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76331, 1.34693))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76342, 1.34695))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76351, 1.34699))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76355, 1.34701))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76366, 1.34709))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76372, 1.34715))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76398, 1.34739))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76425, 1.34763))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76434, 1.34772))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76442, 1.34776))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76449, 1.34779))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76455, 1.34781))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76461, 1.34783))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76467, 1.34784))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76472, 1.34784))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76473, 1.34784))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76476, 1.34784))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76483, 1.34785))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76486, 1.34784))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7649, 1.34783))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76494, 1.34782))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76499, 1.34781))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76506, 1.34778))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76514, 1.34775))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76538, 1.3476))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76538, 1.3476))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76545, 1.34777))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76551, 1.34793))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76558, 1.3481))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76564, 1.34827))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76569, 1.34839))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76571, 1.34844))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76575, 1.34853))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76585, 1.3487))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76586, 1.34872))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76586, 1.34874))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76586, 1.34876))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76586, 1.34878))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76586, 1.3488))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76586, 1.34881))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76586, 1.34883))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76585, 1.34885))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76581, 1.34892))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76581, 1.34892))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76597, 1.3491))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76616, 1.34931))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76617, 1.34932))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76618, 1.34934))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76619, 1.34936))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76619, 1.34939))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76616, 1.3498))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76616, 1.3498))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76596, 1.34979))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76592, 1.34978))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76589, 1.34978))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76587, 1.34977))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76586, 1.34976))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76583, 1.34973))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76582, 1.34971))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76575, 1.34958))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76573, 1.34954))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76572, 1.34952))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76562, 1.34942))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76559, 1.34939))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76544, 1.34925))
        /*(routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76536,1.34917))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76532,1.34913))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7653,1.3491))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76528,1.34909))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76527,1.34908))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76525,1.34908))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76522,1.34908))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76517,1.34908))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76504,1.34906))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76502,1.34905))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76486,1.34902))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76482,1.34901))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76467,1.34897))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76464,1.34897))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76461,1.34897))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76458,1.34897))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76456,1.34898))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76453,1.34899))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76452,1.349))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76449,1.34901))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76444,1.34904))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76439,1.34907))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76436,1.34909))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76417,1.34926))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76417,1.34926))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76423,1.34932))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76428,1.34937))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76429,1.34939))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76432,1.34942))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76433,1.34943))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76433,1.34943))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76442,1.34944))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76447,1.34945))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76451,1.34946))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76454,1.34946))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76458,1.34947))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76463,1.34949))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76469,1.34951))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76474,1.34954))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76477,1.34955))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7648,1.34957))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76482,1.3496))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76485,1.34965))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76489,1.3497))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76489,1.34971))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7649,1.34974))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76491,1.34976))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7649,1.34977))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7649,1.34978))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7649,1.3498))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7649,1.34984))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76489,1.34988))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76489,1.3499))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76489,1.34992))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76489,1.34994))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7649,1.34994))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7649,1.34995))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76491,1.34997))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76493,1.34999))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76494,1.35))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76496,1.35003))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76497,1.35003))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76498,1.35004))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76499,1.35004))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76501,1.35004))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76502,1.35004))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76503,1.35003))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76504,1.35003))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76505,1.35003))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76506,1.35002))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76508,1.35002))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76509,1.35001))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7651,1.35))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76511,1.35))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76512,1.35))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76513,1.35))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76514,1.35))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76516,1.35))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76517,1.35))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76521,1.35001))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76522,1.35001))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76523,1.35001))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76525,1.35002))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76526,1.35002))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76527,1.35002))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76527,1.35003))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76528,1.35003))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76529,1.35004))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7653,1.35005))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7653,1.35006))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76531,1.35007))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76531,1.35008))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76532,1.3501))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76532,1.35011))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76532,1.35012))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76531,1.35013))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76531,1.35014))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7653,1.35015))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76529,1.35016))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76528,1.35017))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76527,1.35018))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76524,1.3502))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7652,1.35024))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76519,1.35025))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76519,1.35026))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76519,1.35027))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76519,1.35028))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7652,1.35029))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76521,1.3503))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76522,1.35031))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76524,1.35032))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76525,1.35032))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76527,1.35033))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76528,1.35033))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7653,1.35033))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76532,1.35032))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76534,1.35032))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76536,1.35032))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76549,1.35027))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76552,1.35027))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76554,1.35026))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76556,1.35026))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76558,1.35027))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76559,1.35028))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7656,1.35028))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76562,1.3503))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76563,1.35031))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76571,1.35045))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76573,1.35049))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76581,1.35058))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76589,1.3507))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76591,1.35072))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76593,1.35073))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76595,1.35074))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76597,1.35075))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76599,1.35075))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76603,1.35076))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76607,1.35077))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76611,1.35079))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76614,1.3508))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76618,1.35081))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7662,1.35082))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76624,1.35082))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76628,1.35081))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76632,1.35081))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(03.76636,1.3508))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7664,1.3508))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76643,1.3508))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76646,1.35081))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76649,1.35083))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76654,1.35084))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76659,1.35084))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76662,1.35084))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76666,1.35084))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76667,1.35084))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7667,1.35083))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76674,1.35082))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76676,1.35082))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76679,1.35082))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.7668,1.35083))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.76687,1.35086))*/
    }

    override fun onResume() {
        super.onResume()
        mapView_active_trail?.onResume()
        var stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Log.d("Sensors", "No Step Counter")
        }
        else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }
    override fun onStart() {
        super.onStart()
        mapView_active_trail?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView_active_trail?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView_active_trail?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView_active_trail?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView_active_trail?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView_active_trail?.onSaveInstanceState(outState)
    }
}
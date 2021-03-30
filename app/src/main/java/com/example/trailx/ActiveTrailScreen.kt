
package com.example.trailx

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_active_trail_screen.*
import android.graphics.Color
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import java.util.ArrayList


class ActiveTrailScreen : AppCompatActivity() {

    private lateinit var mapView:MapView
    private lateinit var routeCoordinates:List<Point>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token))

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_active_trail_screen)
        mapView = findViewById(R.id.mapView_active_trail)
        mapView_active_trail?.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.OUTDOORS) { style ->
                initRouteCoordinates02()
                // Create the LineString from the list of coordinates and then make a GeoJSON
                // FeatureCollection so we can add the line to our map as a layer.
                style.addSource(
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
                style.addLayer(
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
        supportActionBar?.hide()
        val back_to_home_bt_bar = findViewById<Button>(R.id.back_to_home_bt_active_trail)
        back_to_home_bt_bar.setOnClickListener {
            val intent_back_to_home_bt_bar = Intent(this, HomeScreen::class.java)
            startActivity(intent_back_to_home_bt_bar)
        }
        val settings_bt_bar = findViewById<Button>(R.id.settings_bt_active_trail)
        settings_bt_bar.setOnClickListener {
            val intent_settings_bt_bar = Intent(this, SettingsScreen::class.java)
            startActivity(intent_settings_bt_bar)
        }
        val discover_new_trails_bt_bar =
            findViewById<Button>(R.id.discover_new_trails_bt_active_trail)
        discover_new_trails_bt_bar.setOnClickListener {
            val intent_discover_new_trails_bt_bar =
                Intent(this, DiscoverNewTrailsScreen::class.java)
            startActivity(intent_discover_new_trails_bt_bar)
        }
        val active_trail_bt_bar = findViewById<Button>(R.id.active_trail_bt_active_trail)
        active_trail_bt_bar.setOnClickListener {
            val intent_active_trail_bt_bar = Intent(this, ActiveTrailScreen::class.java)
            startActivity(intent_active_trail_bt_bar)
        }
        val my_trails_bt_bar = findViewById<Button>(R.id.my_trails_bt_active_trail)
        my_trails_bt_bar.setOnClickListener {
            val intent_my_trails_bt_bar = Intent(this, MyTrailsScreen::class.java)
            startActivity(intent_my_trails_bt_bar)
        }
        val music_bt_bar = findViewById<Button>(R.id.music_bt_active_trail)
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar = Intent(this, MusicScreen::class.java)
            startActivity(intent_music_bt_bar)
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
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.84558, 1.28148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.2815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.28151))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8455247, 1.2815249))
    }

    fun initRouteCoordinates02() {
        // Bedok Reservoir loop
        routeCoordinates = ArrayList<Point>()
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93384,
            1.33979))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93382,
            1.33974))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9338,
            1.33971))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93378,
            1.33969))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93375,
            1.33966))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93371,
            1.33962))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93368,
            1.33959))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93364,
            1.33956))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93359,
            1.33952))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93355,
            1.33949))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93353,
            1.33947))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.93349,
            1.33945))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93344,
            1.33941))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93344,
            1.33941))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93345,
            1.33937))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93346,
            1.33935))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93347,
            1.33931))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93347,
            1.33924))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93345,
            1.3391))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93344,
            1.33904))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93341,
            1.33892))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93339,
            1.33889))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93338,
            1.33886))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93333,
            1.33877))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93328,
            1.33873))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93323,
            1.33867))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93298,
            1.33846))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93294,
            1.33843))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.9329,
            1.33839))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93281,
            1.33834))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93273,
            1.33829))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93267,
            1.33827))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9326,
            1.33825))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93251,
            1.33823))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93243,
            1.33822))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.93238,
            1.33821))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9323,
            1.33822))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93218,
            1.33822))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93208,
            1.33824))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93193,
            1.33827))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93177,
            1.3383))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93157,
            1.33835))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93079,
            1.33857))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93015,
            1.3388))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92963,
            1.33897))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.92912,
            1.33914))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92878,
            1.33924))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9286,
            1.3393))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9282,
            1.33944))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92807,
            1.33947))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92796,
            1.3395))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92787,
            1.33951))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.92768,
            1.33954))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92751,
            1.33956))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92748,
            1.33956))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92743,
            1.33955))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92738,
            1.33954))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92732,
            1.33951))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92722,
            1.33947))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92677,
            1.33924))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92632,
            1.339))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92603,
            1.33886))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92598,
            1.33884))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92594,
            1.33882))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92589,
            1.3388))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92583,
            1.33879))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92578,
            1.33879))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92574,
            1.33879))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9257,
            1.33879))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92564,
            1.33879))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92556,
            1.3388))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92548,
            1.33881))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92518,
            1.33888))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92485,
            1.33896))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92455,
            1.33904))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92395,
            1.33919))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.92387,
            1.33921))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92309,
            1.33939))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92306,
            1.3394))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9222,
            1.3396))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92215,
            1.33961))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92202,
            1.33964))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92188,
            1.33968))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92177,
            1.33972))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92168,
            1.33975))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9216,
            1.33979))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92144,
            1.33987))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92129,
            1.33996))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92112,
            1.34007))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92096,
            1.34019))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92088,
            1.34027))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92081,
            1.34033))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92073,
            1.34042))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92066,
            1.34051))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92055,
            1.34066))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92036,
            1.34103))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92023,
            1.34129))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92006,
            1.34165))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91994,
            1.34191))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91985,
            1.34208))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91978,
            1.34219))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91973,
            1.34228))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91969,
            1.34234))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91965,
            1.3424))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91934,
            1.34296))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91922,
            1.34321))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9192,
            1.34337))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9192,
            1.34349))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9192,
            1.34352))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91937,
            1.34396))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91956,
            1.34424))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9197,
            1.34446))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.91978,
            1.34458))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9198,
            1.34462))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92008,
            1.34519))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92016,
            1.34533))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.92025,
            1.34541))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.92036,
            1.34548))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92051,
            1.34554))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92081,
            1.34561))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92108,
            1.34569))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92142,
            1.34579))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92194,
            1.34594))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92264,
            1.34613))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92433,
            1.34658))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92433,
            1.34658))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92459,
            1.34715))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92459,
            1.34715))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92522,
            1.34722))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92528,
            1.34722))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92531,
            1.34722))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92533,
            1.34722))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92535,
            1.34722))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92538,
            1.34722))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92544,
            1.34721))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92549,
            1.34719))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92556,
            1.34718))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92562,
            1.34715))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92572,
            1.3471))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92584,
            1.34704))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92596,
            1.34698))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92612,
            1.3469))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92634,
            1.34678))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92637,
            1.34676))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92663,
            1.34662))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92682,
            1.34649))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92705,
            1.34631))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92717,
            1.34621))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92727,
            1.34612))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92734,
            1.34608))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92744,
            1.34601))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92753,
            1.34593))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92759,
            1.34589))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92769,
            1.34583))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92773,
            1.34582))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92775,
            1.3458))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.9278,
            1.34578))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92784,
            1.34576))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92787,
            1.34575))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92789,
            1.34574))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92795,
            1.3457))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.928,
            1.34567))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92815,
            1.34554))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92823,
            1.34547))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9283,
            1.3454))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92838,
            1.34535))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9284,
            1.34534))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92842,
            1.34533))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92843,
            1.34532))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92845,
            1.34532))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92847,
            1.34532))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9285,
            1.34532))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92855,
            1.34532))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92858,
            1.34532))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92862,
            1.34532))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92865,
            1.34531))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92869,
            1.3453))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92872,
            1.34529))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92873,
            1.34529))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92877,
            1.34527))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92881,
            1.34525))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92896,
            1.34518))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92898,
            1.34517))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92901,
            1.34516))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92904,
            1.34514))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92923,
            1.34505))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92932,
            1.34501))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92943,
            1.34495))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.9295,
            1.34491))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92955,
            1.34487))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92959,
            1.34483))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92962,
            1.34481))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92965,
            1.34478))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92967,
            1.34476))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92971,
            1.34471))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.92975,
            1.34465))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.92994,
            1.34428))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93006,
            1.34407))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9301,
            1.34399))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93015,
            1.34392))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93019,
            1.34389))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9304,
            1.34369))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93051,
            1.34359))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93064,
            1.34346))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93091,
            1.34317))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93104,
            1.34303))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93119,
            1.34288))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93126,
            1.34281))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93146,
            1.34267))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93159,
            1.34257))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93162,
            1.34255))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93173,
            1.34246))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93187,
            1.34235))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93227,
            1.34205))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93246,
            1.3419))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93248,
            1.34189))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93252,
            1.34186))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93269,
            1.34175))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9328,
            1.34168))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93291,
            1.3416))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93305,
            1.34148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93318,
            1.34138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93331,
            1.34127))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93333,
            1.34126))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93345,
            1.34116))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93346,
            1.34114))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9335,
            1.34111))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93369,
            1.34095))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9337,
            1.34094))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93373,
            1.34091))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93375,
            1.34088))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93378,
            1.34084))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93382,
            1.34078))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93383,
            1.34075))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93392,
            1.34062))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93395,
            1.34056))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.93398,
            1.34051))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.934,
            1.34048))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93401,
            1.34046))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93402,
            1.34043))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93403,
            1.34039))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93404,
            1.34034))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93405,
            1.34028))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93406,
            1.34023))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93408,
            1.34019))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93411,
            1.3401))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93412,
            1.3401))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93414,
            1.34005))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9343,
            1.33982))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93442,
            1.33966))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93442,
            1.33966))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9344,
            1.33963))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9344,
            1.3396))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.9344,
            1.3396))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93443,
            1.33956))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.93445,
            1.33955))
    }

    fun initRouteCoordinates03() {
        // Telok Ayer - ClubStreet
        routeCoordinates = ArrayList<Point>()
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85171,
            1.36022))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85159,
            1.36006))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85157,
            1.36003))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85154,
            1.36))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85146,
            1.35996))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85138,
            1.35993))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85117,
            1.35991))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85122,
            1.35971))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85122,
            1.3595))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85119,
            1.35933))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85116,
            1.3592))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85114,
            1.35913))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85111,
            1.35906))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8511,
            1.35904))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85107,
            1.35899))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85102,
            1.35893))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85092,
            1.35884))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85085,
            1.35879))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85082,
            1.35875))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8508,
            1.35872))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8508,
            1.35869))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8508,
            1.35865))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85081,
            1.3586))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85081,
            1.35854))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85081,
            1.35852))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85081,
            1.35852))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85076,
            1.35853))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85071,
            1.35855))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85068,
            1.35856))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85068,
            1.35856))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85067,
            1.35854))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85066,
            1.35853))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85064,
            1.3585))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85059,
            1.35846))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85054,
            1.35843))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.85049,
            1.3584))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85045,
            1.35837))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85044,
            1.35835))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85044,
            1.35833))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85043,
            1.3583))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85044,
            1.35824))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85045,
            1.35819))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85044,
            1.35815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85043,
            1.35812))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85043,
            1.35811))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85041,
            1.35809))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85038,
            1.35807))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85034,
            1.35805))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85029,
            1.35804))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85024,
            1.35805))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85014,
            1.35807))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.85007,
            1.35806))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84979,
            1.35827))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84972,
            1.35832))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84964,
            1.35839))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84958,
            1.35845))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84952,
            1.35856))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84947,
            1.35866))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84945,
            1.35872))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84944,
            1.35881))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84942,
            1.35889))
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
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.84558, 1.28148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.2815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.28151))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8455247, 1.2815249))
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
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.84558, 1.28148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.2815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.28151))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8455247, 1.2815249))
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
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.84558, 1.28148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.2815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.28151))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8455247, 1.2815249))
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
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.84558, 1.28148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.2815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.28151))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8455247, 1.2815249))
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
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.84558, 1.28148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.2815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.28151))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8455247, 1.2815249))
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
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.84558, 1.28148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.2815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.28151))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8455247, 1.2815249))
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
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat( 103.84558, 1.28148))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.2815))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84556, 1.28151))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.84559, 1.28138))
        (routeCoordinates as ArrayList<Point>).add(Point.fromLngLat(103.8455247, 1.2815249))
    }

    override fun onResume() {
        super.onResume()
        mapView_active_trail?.onResume()
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
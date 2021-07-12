package com.raquezha.heograpiya

import android.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.GeoPolygon
import com.here.android.mpa.common.OnEngineInitListener
import com.here.android.mpa.common.RoadElement
import com.here.android.mpa.mapping.AndroidXMapFragment
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.mapping.Map.FleetFeature
import com.here.android.mpa.mapping.MapRoute
import com.here.android.mpa.routing.CoreRouter
import com.here.android.mpa.routing.DrivingDirection
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.RouteOptions
import com.here.android.mpa.routing.RoutePlan
import com.here.android.mpa.routing.RouteResult
import com.here.android.mpa.routing.RouteWaypoint
import com.here.android.mpa.routing.Router
import com.here.android.mpa.routing.RoutingError
import com.here.android.mpa.routing.RoutingZone
import java.util.EnumSet

/**
 * This class encapsulates the properties and functionality of the Map view.A route calculation from
 * south of Berlin to the north of Berlin.
 */
class MapFragmentView(private val m_activity: AppCompatActivity) {

    private var mapFragment: AndroidXMapFragment? = null
    private var createRouteButton: Button? = null
    private var map: Map? = null
    private var mapRoute: MapRoute? = null
    private var isExcludeRoutingZones = false
    private var addAvoidedAreas = false
    private val mMapFragment: AndroidXMapFragment?
        get() = m_activity.supportFragmentManager.findFragmentById(R.id.mapfragment) as AndroidXMapFragment?

    private fun initMapFragment() {
        /* Locate the mapFragment UI element */
        mapFragment = mMapFragment
        if (mapFragment != null) {
            /* Initialize the AndroidXMapFragment, results will be given via the called back. */
            mapFragment!!.init { error ->
                if (error == OnEngineInitListener.Error.NONE) {
                    /* get the map object */
                    map = mapFragment!!.map
                    assert(map != null)
                    map!!.setCenter(
                        GeoCoordinate(13.6221908,123.1919845, 0.0),
                        Map.Animation.NONE
                    )

                    /* Set the zoom level to the average between min and max zoom level. */
                    map!!.zoomLevel = (map!!.maxZoomLevel + map!!.minZoomLevel) / 2
                } else {
                    AlertDialog.Builder(m_activity)
                        .setMessage("Error : " + error.name + "\n\n" + error.details)
                        .setTitle(R.string.engine_init_error)
                        .setNegativeButton(android.R.string.cancel) { _, _ -> m_activity.finish() }
                        .create()
                        .show()
                }
            }
        }
    }

    private fun initCreateRouteButton() {
        createRouteButton = m_activity.findViewById<View>(R.id.btnCreateRoute) as AppCompatButton
        createRouteButton!!.setOnClickListener {
            if(mapRoute != null)
                map?.removeMapObject((mapRoute)!!)
            mapRoute = null
            createRoute(emptyList())
        }
    }

    private fun createRoute(excludedRoutingZones: List<RoutingZone>) {

        /* Initialize a CoreRouter */
        val coreRouter = CoreRouter()

        /* Initialize a RoutePlan */
        val routePlan = RoutePlan()

        /*
         * Initialize a RouteOption. HERE Mobile SDK allow users to define their own parameters for the
         * route calculation,including transport modes,route types and route restrictions etc.Please
         * refer to API doc for full list of APIs
         */
        val routeOptions = RouteOptions()

        /* Other transport modes are also available e.g Pedestrian */
        routeOptions.transportMode = RouteOptions.TransportMode.SCOOTER

        /* Disable highway in this route. */
        routeOptions.setHighwaysAllowed(false)

        /* Calculate the shortest route available. */
        routeOptions.routeType = RouteOptions.Type.SHORTEST

        /* Calculate 1 route. */routeOptions.routeCount = 1
        /* Exclude routing zones. */
        if (excludedRoutingZones.isNotEmpty()) {
            routeOptions.excludeRoutingZones(
                toStringIds(excludedRoutingZones)
            )
        }

        if (addAvoidedAreas) {
            val dynamicPenalty = DynamicPenalty()
            // There are two option to avoid certain areas during routing
            // 1. Add banned area using addBannedArea API
            val geoPolygon = GeoPolygon()
            geoPolygon.add(
                listOf(
                    GeoCoordinate(52.631692, 13.437591),
                    GeoCoordinate(52.631905, 13.437787),
                    GeoCoordinate(52.632577, 13.438357)
                )
            )
            // Note, the maximum supported number of banned areas is 20.
            dynamicPenalty.addBannedArea(geoPolygon)

            // 1. Add banned road link using addRoadPenalty API
            // Note, map data needs to be present to get RoadElement by the GeoCoordinate.
            val roadElement = RoadElement
                .getRoadElement(GeoCoordinate(52.406611, 13.194916), "MAC")
            if (roadElement != null) {
                dynamicPenalty.addRoadPenalty(
                    roadElement, DrivingDirection.DIR_BOTH,
                    0 /*new speed*/
                )
            }
            coreRouter.setDynamicPenalty(dynamicPenalty)
        }

        /* Finally set the route option */
        routePlan.routeOptions = routeOptions

        /* Define waypoints for the route */
        /* START: South of Berlin */
        val startPoint = RouteWaypoint(GeoCoordinate(13.6221908,123.1919845))
        /* END: North of Berlin */
        val destination = RouteWaypoint(GeoCoordinate(13.664829,123.2810053))

        /* Add both waypoints to the route plan */routePlan.addWaypoint(startPoint)
        routePlan.addWaypoint(destination)

        /* Trigger the route calculation,results will be called back via the listener */
        coreRouter.calculateRoute(
            routePlan,

            object : Router.Listener<List<RouteResult>, RoutingError> {
                override fun onProgress(i: Int) {
                    /* The calculation progress can be retrieved in this callback. */
                }

                override fun onCalculateRouteFinished(
                    routeResults: List<RouteResult>,
                    routingError: RoutingError
                ) {
                    /* Calculation is done. Let's handle the result */
                    if (routingError == RoutingError.NONE) {
                        val route = routeResults[0].route
                        if (isExcludeRoutingZones && excludedRoutingZones.isEmpty()) {
                            // Here we exclude all available routing zones in the route.
                            // Also RoutingZoneRestrictionsChecker can be used to get
                            // available routing zones for specific RoadElement.
                            createRoute(route.routingZones)
                        } else {
                            /* Create a MapRoute so that it can be placed on the map */
                            mapRoute = MapRoute(route)

                            /* Show the maneuver number on top of the route */
                            mapRoute!!.isManeuverNumberVisible = true

                            /* Add the MapRoute to the map */
                            map!!.addMapObject(mapRoute!!)

                            /*
                             * We may also want to make sure the map view is orientated properly
                             * so the entire route can be easily seen.
                             */
                            map!!.zoomTo(
                                (route.boundingBox)!!,
                                Map.Animation.NONE,
                                Map.MOVE_PRESERVE_ORIENTATION
                            )
                        }
                    } else {
                        Toast.makeText(
                            m_activity,
                            "Error:route calculation returned error code: $routingError",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked
        when (item.itemId) {
            ITEM_ID_SHOW_ZONES -> {
                val features: EnumSet<FleetFeature> = when {
                    item.isChecked -> EnumSet.of(FleetFeature.ENVIRONMENTAL_ZONES)
                    else -> EnumSet.noneOf(FleetFeature::class.java)
                }
                map!!.fleetFeaturesVisible = features
            }
            ITEM_ID_EXCLUDE_IN_ROUTING -> {
                isExcludeRoutingZones = item.isChecked
                if (mapRoute != null) {
                    Toast.makeText(
                        m_activity, "Please recalculate the route to apply this setting",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            ITEM_ID_ADD_AVOIDED_AREAS -> {
                addAvoidedAreas = item.isChecked
                if (mapRoute != null) {
                    Toast.makeText(
                        m_activity, "Please recalculate the route to apply this setting",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return true
    }

    fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(
            0,
            ITEM_ID_SHOW_ZONES,
            Menu.NONE,
            "Show environmental zones"
        ).isCheckable = true
        menu.add(
            0,
            ITEM_ID_EXCLUDE_IN_ROUTING,
            Menu.NONE,
            "Exclude all zones in routing"
        ).isCheckable = true
        menu.add(
            0,
            ITEM_ID_ADD_AVOIDED_AREAS,
            Menu.NONE,
            "Add avoided areas"
        ).isCheckable = true
        return true
    }

    companion object {
        private const val ITEM_ID_SHOW_ZONES = 1
        private const val ITEM_ID_EXCLUDE_IN_ROUTING = 2
        private const val ITEM_ID_ADD_AVOIDED_AREAS = 3
        fun toStringIds(excludedRoutingZones: List<RoutingZone>): List<String> {
            val ids = ArrayList<String>()
            for (zone: RoutingZone in excludedRoutingZones) {
                ids.add(zone.id)
            }
            return ids
        }
    }

    init {
        initMapFragment()
        /*
         * We use a button in this example to control the route calculation
         */
        initCreateRouteButton()
    }
}
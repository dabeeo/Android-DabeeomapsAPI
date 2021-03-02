package com.dabeeo.indoormap.manager

import androidx.fragment.app.FragmentManager
import com.dabeeo.imsdk.common.error.IMError
import com.dabeeo.imsdk.imenum.TransType
import com.dabeeo.imsdk.map.MapView
import com.dabeeo.imsdk.model.gl.Marker
import com.dabeeo.imsdk.navigation.*
import com.dabeeo.imsdk.navigation.data.NodeData
import com.dabeeo.imsdk.navigation.data.Path
import com.dabeeo.imsdk.navigation.data.Route
import com.dabeeo.indoormap.common.ProjectDrawable
import com.dabeeo.indoormap.fragment.NavigationDialog
import org.rajawali3d.math.vector.Vector3


class NavigationManager(private val mapView: MapView) {

    private var originLocation: Location? = null
    private var destinationLocation: Location? = null

    private var originMarker: Marker? = null
    private var destinationMarker: Marker? = null

    var isNavigating = false

    fun showNavigationDialog(x: Double, y: Double, floorLevel: Int, fragmentManager: FragmentManager) {
        if(isNavigating) {
            stopNavigation()
            return
        }

        if(originLocation == null || destinationLocation == null) {
            val navigationDialog = NavigationDialog("$x, $y")
            navigationDialog.show(fragmentManager, "navigation")
            navigationDialog.onOrigin  = {
                originLocation = Location(Vector3(x, y, 0.0), floorLevel, "")
                if(originLocation != null && destinationLocation != null) {
                    requestPath(originLocation!!, destinationLocation!!)
                }
            }
            navigationDialog.onDestination  = {
                destinationLocation= Location(Vector3(x, y, 0.0), floorLevel, "");
                if(originLocation != null && destinationLocation != null) {
                    requestPath(originLocation!!, destinationLocation!!)
                }
            }
        }
    }

    private fun requestPath(origin: Location, destination: Location) {
        mapView.setOnNavigationListener(object : IMNavigationListener {
            override fun onPathResult(pathResult: PathResult) {
                if (pathResult.isSuccess) {
                    mapView.drawPath(pathResult.pathData!!)
                    addNavigationMarker(origin, destination)
                }
            }

            override fun onStart() {
                isNavigating = true
            }

            override fun onFinish() {
                isNavigating = false
                originLocation = null
                destinationLocation = null
            }

            override fun onCancel() {
                isNavigating = false
                originLocation = null
                destinationLocation = null
            }

            override fun onUpdate(
                currentRoute: Route,
                currentPath: Path,
                currentNodeData: NodeData,
                snapPosition: Vector3
            ) {
            }

            override fun onRescan() {
            }

            override fun onError(e: IMError?) {
            }
        })
        mapView.findPath(
            origin.originPosition.x, origin.originPosition.y, origin.floorLevel,
            destination.originPosition.x, destination.originPosition.y, destination.floorLevel,
            ArrayList(), TransType.ALL
        )
//        mapView.startNavigation()
    }

    private fun stopNavigation() {
        mapView.cancelNavigation()
        mapView.removeMarker()
    }

    fun addNavigationMarker(origin: Location, destination: Location) {
        originMarker = mapView.addMarker(
            ProjectDrawable.icon_pin,
            origin.originPosition.x,
            origin.originPosition.y,
            108.0,
            111.0,
            origin.floorLevel
        )
        destinationMarker = mapView.addMarker(
            ProjectDrawable.icon_pin,
            destination.originPosition.x,
            destination.originPosition.y,
            108.0,
            111.0,
            destination.floorLevel
        )
        mapView.drawMarker()
    }

}
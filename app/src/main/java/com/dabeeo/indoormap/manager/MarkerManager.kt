package com.dabeeo.indoormap.manager

import android.view.View
import androidx.fragment.app.FragmentManager
import com.dabeeo.imsdk.map.MapView
import com.dabeeo.imsdk.model.gl.Marker
import com.dabeeo.indoormap.fragment.MarkerOptionDialog
import java.util.*


class MarkerManager(private val mapView: MapView) {

    private val markerMaps = HashMap<Int, ArrayList<Marker>>()

    fun getMarkers(): ArrayList<Marker> {
        val markers: ArrayList<Marker> = ArrayList()
        val keys: Iterator<Int> = markerMaps.keys.iterator()
        while (keys.hasNext()) {
            val key = keys.next()
            markers.addAll(markerMaps[key]!!)
        }
        return markers
    }

    fun getMarkersByFloor(floor: Int): ArrayList<Marker> {
        var markers = ArrayList<Marker>()
        val keys: Iterator<Int> = markerMaps.keys.iterator()
        while (keys.hasNext()) {
            val key = keys.next()
            if (key == floor) {
                markers = markerMaps[key]!!
            }
        }
        return markers
    }

    fun addMarker(
        resource: Int,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        floorLevel: Int
    ): Marker? {
        val marker = mapView.addMarker(resource, x, y, width, height, floorLevel)
        marker?.run {
            this.id = "id-" + getMarkers().size
            this.tag = "${this.id}-${getMarkers().size}" as Object
            var markers = markerMaps[floorLevel]
            if (markers == null) {
                markers = ArrayList()
                markerMaps[floorLevel] = markers
            }
            markers.add(marker)
        }
        return marker
    }

    fun drawMarker() {
        mapView.drawMarker()
    }

    fun drawMarker(marker: Marker) {
        mapView.drawMarker()
    }

    fun updateMarker(marker: Marker) {
        mapView.updateMarker(marker)
    }

    fun removeMarker() {
        mapView.removeMarker()
    }

    fun removeMarker(marker: Marker) {
        mapView.removeMarker(marker)
    }

    fun showOptionDialog(marker: Marker, fragmentManager: FragmentManager) {
        val markerOptionDialog = MarkerOptionDialog(marker, false)
        markerOptionDialog.show(fragmentManager, "markerOption")
        markerOptionDialog.onAddMarker  = {
            drawMarker()
        }
        markerOptionDialog.onCancel  = {
            removeMarker(marker)
        }
    }

    fun showSetOptionDialog(marker: Marker, fragmentManager: FragmentManager) {
        val markerOptionDialog = MarkerOptionDialog(marker, true)
        markerOptionDialog.show(fragmentManager, "markerOption")
        markerOptionDialog.onUpdateMarker  = {
            updateMarker(it)
        }
    }


}
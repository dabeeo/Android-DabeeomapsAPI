package com.dabeeo.indoormap.fragment

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dabeeo.imsdk.animation.AnimationValue
import com.dabeeo.imsdk.imenum.AnimationType
import com.dabeeo.imsdk.map.MapCallback
import com.dabeeo.imsdk.map.MapView
import com.dabeeo.imsdk.map.interfaces.IMAnimationListener
import com.dabeeo.imsdk.map.interfaces.IMMarkerListener
import com.dabeeo.imsdk.map.interfaces.IMMoveListener
import com.dabeeo.imsdk.model.common.FloorInfo
import com.dabeeo.imsdk.model.gl.Marker
import com.dabeeo.imsdk.model.map.Poi
import com.dabeeo.indoormap.BaseFragment
import com.dabeeo.indoormap.adapter.FloorListAdapter
import com.dabeeo.indoormap.common.Commons
import com.dabeeo.indoormap.common.ProjectDrawable
import com.dabeeo.indoormap.common.ProjectLayout
import com.dabeeo.indoormap.databinding.FragmentMapBinding
import com.dabeeo.indoormap.manager.MarkerManager
import com.dabeeo.indoormap.manager.NavigationManager
import com.dabeeo.indoormap.views.RoundButton

class MapFragment : BaseFragment<FragmentMapBinding>(), View.OnClickListener, IMMoveListener,
    IMMarkerListener, IMAnimationListener {

    private lateinit var floorAdapter: FloorListAdapter
    private var is2D = true
    private lateinit var mapView: MapView
    private lateinit var markerManager: MarkerManager
    private lateinit var navigationManager: NavigationManager

    private var isRemoveMarker = false
    private var isOptionMarker = false

    companion object {
        fun newInstance() = MapFragment()
    }

    override fun getLayoutResourceId(): Int {
        return ProjectLayout.fragment_map
    }

    override fun setupViews() {
//        binding.vm = viewModel
        binding.executePendingBindings()
        mapView = binding.mapView
        markerManager = MarkerManager(mapView)
        navigationManager = NavigationManager(mapView)

        syncMap(Commons.clientId, Commons.secretKey)

        binding.apiToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (checkedId == binding.mapApiButton.id) {
                binding.mapApiLayout.visibility = View.VISIBLE
                binding.markerApiLayout.visibility = View.GONE
            } else if (checkedId == binding.markerApiButton.id) {
                binding.mapApiLayout.visibility = View.GONE
                binding.markerApiLayout.visibility = View.VISIBLE
            }
        }

        binding.modeButton.setText("2D")
        binding.zoomInButton.setRound(RoundButton.ROUND.TOP)
        binding.zoomOutButton.setRound(RoundButton.ROUND.BOTTOM)
        binding.zoomInButton.setImage(ProjectDrawable.btn_zoom_in)
        binding.zoomOutButton.setImage(ProjectDrawable.btn_zoom_out)

        binding.zoomInButton.setOnClickListener(this)
        binding.zoomOutButton.setOnClickListener(this)
        binding.modeButton.setOnClickListener(this)
        binding.addRotate.setOnClickListener(this)
        binding.minusRotate.setOnClickListener(this)
        binding.rotateActive.setOnClickListener(this)
        binding.zoomActive.setOnClickListener(this)
        binding.poiTilt.setOnClickListener(this)
        binding.maxZoomUp.setOnClickListener(this)
        binding.maxZoomDown.setOnClickListener(this)
        binding.minZoomUp.setOnClickListener(this)
        binding.minZoomDown.setOnClickListener(this)
        binding.minusTilt.setOnClickListener(this)
        binding.plusTilt.setOnClickListener(this)

        binding.addMarkerButton.setOnClickListener(this)
        binding.optionButton.setOnClickListener(this)
        binding.removeMarkerButton.setOnClickListener(this)
        binding.removeAllMarkerButton.setOnClickListener(this)

        binding.shootMarkerButton.setOnClickListener(this)
        binding.addCancelButton.setOnClickListener(this)

        mapView.setOnMoveListener(this)
        mapView.setOnMarkerListener(this)
        mapView.setOnAnimationListener(this)

    }

    private fun syncMap(clientId: String, secretKey: String) {
        showProgress()
        mapView.startMap(clientId, secretKey, mapListener)
    }

    private fun syncMap(mapId: String) {
        showProgress()
        mapView.startMapByMapId(mapId, mapListener)
    }

    private val mapListener = object : MapCallback {
        override fun onSuccess(floors: List<FloorInfo>) {
            hideProgress()
            context?.run {
                floorAdapter = FloorListAdapter(floors as ArrayList<FloorInfo>)
                val linearLayoutManager = LinearLayoutManager(this)
                binding.floorRecyclerView.layoutManager = linearLayoutManager
                binding.floorRecyclerView.adapter = floorAdapter
                floorAdapter.onClickItem = {
                    mapView.setFloor(it.level)
                }
            }

            mapView.setMaxZoom(5.0)
            mapView.setMinZoom(1.0)

            binding.limitZoomLevelTextView.text = "maxZoom = ${mapView.getMaxZoom()} / minZoom = ${mapView.getMinZoom()}"
            binding.rotateActive.text = "rotate = ${mapView.getUseRotateGesture()}"
            binding.zoomActive.text = "zoom = ${mapView.getUseZoomGesture()}"
//                binding.poiTilt.text = "poiTilt = ${mapView.fixPoiTilt}"
            onZoomLevel(mapView.getZoomLevel())
            onRotation(mapView.getAngle())
        }

        override fun onError(e: Exception) {
            showAlert("onError : $e")
            hideProgress()
        }

        override fun changeFloor(floorOrder: Int) {
            floorAdapter.selectFloor(floorOrder)
            binding.floorRecyclerView.layoutManager?.scrollToPosition(floorOrder - 1)
        }

        override fun onClick(x: Double, y: Double, poi: Poi?) {
        }

        override fun onLongClick(x: Double, y: Double, poi: Poi?) {
            mapView.moveTo(x, y, true)
        }
    }

    override fun onMove(x: Double, y: Double) {
        binding.locationTextView.text = "coord = " + x.toInt() + ", " + y.toInt()
    }

    override fun onZoomLevel(zoomLevel: Double) {
        binding.zoomLevelTextView.text = "zoomLevel = $zoomLevel"
    }

    override fun onRotation(rotation: Double) {
        binding.rotationTextView.text = "angle = " + rotation.toInt()
    }

    override fun onMarkerClick(marker: Marker) {
        when {
            isRemoveMarker -> {
                markerManager.removeMarker(marker)
                isRemoveMarker = false
            }
            isOptionMarker -> {
                markerManager.showSetOptionDialog(marker, childFragmentManager)
                isOptionMarker = false
            }
            else -> {
//                mapView.translate(marker.x, marker.y, true)
//                showAlert(marker.toString())
            }
        }
    }

    override fun onMarkerLongClick(marker: Marker) {
//        navigationManager.showNavigationDialog(marker.x, marker.y, marker.floorLevel, childFragmentManager)
    }

    override fun onAnimationStart(animationType: AnimationType) {
        Log.i("IMAnimationListener", "onAnimationStart = $animationType")
    }

    override fun onAnimationUpdate(animationValue: AnimationValue) {
        Log.i("IMAnimationListener", "onAnimationUpdate = " +
                "${animationValue.animationType}, " +
                "${animationValue.x.toInt()}, " +
                "${animationValue.y.toInt()}, " +
                "${animationValue.zoomLevel}, " +
                "${animationValue.angle.toInt()}")
        onZoomLevel(animationValue.zoomLevel)
        onRotation(animationValue.angle)
        onMove(animationValue.x, animationValue.y)
    }

    override fun onAnimationEnd(animationType: AnimationType) {
        Log.i("IMAnimationListener", "onAnimationEnd = $animationType")
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.destroy()
    }

    override fun onClick(v: View) {
        when(v) {
            binding.zoomInButton -> {
                mapView.setZoomLevel(mapView.getZoomLevel() - 0.1, true)
            }
            binding.zoomOutButton -> {
                mapView.setZoomLevel(mapView.getZoomLevel() + 0.1, true)
            }
            binding.addRotate -> {
                Log.i("ClickListener", "Rotate = ${mapView.getAngle()} + 10")
                mapView.setAngle(mapView.getAngle() + 10, true)
            }
            binding.minusRotate -> {
                Log.i("ClickListener", "Rotate = ${mapView.getAngle()} - 10")
                mapView.setAngle(mapView.getAngle() - 10, true)
            }
            binding.rotateActive -> {
                mapView.setUseRotateGesture(!mapView.getUseRotateGesture())
                binding.rotateActive.text = "rotate = " + mapView.getUseRotateGesture()
            }
            binding.zoomActive -> {
                mapView.setUseZoomGesture(!mapView.getUseZoomGesture())
                binding.zoomActive.text = "zoom = " + mapView.getUseZoomGesture()
            }
            binding.maxZoomUp -> {
                mapView.setMaxZoom(mapView.getMaxZoom() + 0.1)
                binding.limitZoomLevelTextView.text = "maxZoom = ${mapView.getMaxZoom()} / minZoom = ${mapView.getMinZoom()}"
            }
            binding.maxZoomDown -> {
                mapView.setMaxZoom(mapView.getMaxZoom() - 0.1)
                binding.limitZoomLevelTextView.text = "maxZoom = ${mapView.getMaxZoom()} / minZoom = ${mapView.getMinZoom()}"
            }
            binding.minZoomUp -> {
                mapView.setMinZoom(mapView.getMinZoom() + 0.1)
                binding.limitZoomLevelTextView.text = "maxZoom = ${mapView.getMaxZoom()} / minZoom = ${mapView.getMinZoom()}"
            }
            binding.minZoomDown -> {
                mapView.setMinZoom(mapView.getMinZoom() - 0.1)
                binding.limitZoomLevelTextView.text = "maxZoom = ${mapView.getMaxZoom()} / minZoom = ${mapView.getMinZoom()}"
            }
            binding.addMarkerButton -> {
                binding.shootLayout.visibility = View.VISIBLE
                binding.markerApiLayout.visibility = View.GONE
                isRemoveMarker = false
                isOptionMarker = false
            }
            binding.removeMarkerButton -> {
                isOptionMarker = false
                if(markerManager.getMarkersByFloor(mapView.getFloorLevel()).size == 0) {
                    showAlert("삭제 할 마커가 없습니다.")
                } else {
                    showAlert("삭제 할 마커를 션택하세요.")
                    isRemoveMarker = true
                }
            }
            binding.removeAllMarkerButton -> {
                mapView.removeMarker()
            }
            binding.optionButton -> {
                isRemoveMarker = false
                isOptionMarker = true
                showAlert("옵션 변경 할 마커를 선택하세요.")
            }

            binding.shootMarkerButton -> {
                binding.shootLayout.visibility = View.GONE
                val marker = markerManager.addMarker(
                    ProjectDrawable.icon_pin,
                    mapView.getCenter().x,
                    mapView.getCenter().y,
                    100.0,
                    100.0,
                    mapView.getFloorLevel()
                )
                marker?.run {
                    markerManager.showOptionDialog(this, childFragmentManager)
                }
                binding.markerApiLayout.visibility = View.VISIBLE
            }
            binding.addCancelButton -> {
                binding.shootLayout.visibility = View.GONE
                binding.markerApiLayout.visibility = View.VISIBLE
            }

            binding.modeButton -> {
                is2D = !is2D
                val mode = if (is2D) {
                    binding.tiltLayout.visibility = View.GONE
                    "2D"
                } else {
                    binding.tiltLayout.visibility = View.VISIBLE
                    "3D"
                }
                mapView.setCameraMode(is2D)
                binding.modeButton.setText(mode)
            }

            binding.minusTilt -> {
                mapView.setMapTilt(mapView.getMapTilt() - 22.5, true)
            }
            binding.plusTilt -> {
                mapView.setMapTilt(mapView.getMapTilt() + 22.5, true)
            }
            binding.poiTilt -> {
//                mapView.fixPoiTilt = !mapView.fixPoiTilt
//                binding.poiTilt.text = "poiTilt = ${mapView.fixPoiTilt}"
            }
        }
    }
}

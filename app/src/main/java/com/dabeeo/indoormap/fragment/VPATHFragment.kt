package com.dabeeo.indoormap.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dabeeo.imsdk.ar.ARView
import com.dabeeo.imsdk.imenum.LocationStatus
import com.dabeeo.imsdk.location.LocationCallback
import com.dabeeo.imsdk.map.MapCallback
import com.dabeeo.imsdk.map.MapView
import com.dabeeo.imsdk.model.common.FloorInfo
import com.dabeeo.imsdk.model.map.Poi
import com.dabeeo.indoormap.BaseFragment
import com.dabeeo.indoormap.adapter.FloorListAdapter
import com.dabeeo.indoormap.common.Commons
import com.dabeeo.indoormap.common.ProjectDrawable
import com.dabeeo.indoormap.common.ProjectLayout
import com.dabeeo.indoormap.databinding.FragmentVpathBinding
import com.dabeeo.indoormap.manager.MarkerManager
import com.dabeeo.indoormap.manager.NavigationManager
import com.dabeeo.indoormap.views.RoundButton

class VPATHFragment() : BaseFragment<FragmentVpathBinding>(), View.OnClickListener {

    private lateinit var floorAdapter: FloorListAdapter
    private lateinit var mapView: MapView
    private lateinit var markerManager: MarkerManager
    private lateinit var navigationManager: NavigationManager
    private lateinit var cameraView: ARView

    companion object {
        fun newInstance() = VPATHFragment()
    }

    override fun getLayoutResourceId(): Int {
        return ProjectLayout.fragment_vpath
    }

    override fun setupViews() {
//        binding.vm = viewModel
        binding.executePendingBindings()
        mapView = binding.mapView
        markerManager = MarkerManager(mapView)
        navigationManager = NavigationManager(mapView)

        cameraView = binding.cameraView
//        cameraView = childFragmentManager.findFragmentById(R.id.cameraView) as CameraView

        syncMap(Commons.clientId, Commons.secretKey)

        binding.zoomInButton.setRound(RoundButton.ROUND.TOP)
        binding.zoomOutButton.setRound(RoundButton.ROUND.BOTTOM)

        binding.zoomInButton.setImage(ProjectDrawable.btn_zoom_in)
        binding.zoomOutButton.setImage(ProjectDrawable.btn_zoom_out)

        binding.locationButton.setRound(RoundButton.ROUND.ALL)
        binding.locationButton.setText("위치")

        binding.zoomInButton.setOnClickListener(this)
        binding.zoomOutButton.setOnClickListener(this)
        binding.locationButton.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        cameraView.onResume()
    }

    override fun onPause() {
        super.onPause()
        cameraView.onPause()
    }

    private fun syncMap(clientId: String, secretKey: String) {
        showProgress()
        mapView.startMap(clientId, secretKey, mapListener)
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
//            mapView.connectCamera(cameraView)
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
            binding.locationButton -> {
                showProgress()
                binding.mapView.startPosition(cameraView, object: LocationCallback{
                    override fun onError(e: Exception) {
                        hideProgress()
                    }

                    override fun onChangeFloor(floorLevel: Int) {
                    }

                    override fun initLocation(x: Double, y: Double, angle: Double, floorLevel: Int) {
                        hideProgress()
                        mapView.moveTo(x, y, false)
                    }

                    override fun onLocation(x: Double, y: Double, angle: Double, floorLevel: Int) {
                        mapView.moveTo(x, y, false)
                    }

                    override fun onLocationStatus(status: LocationStatus) {
                    }
                })

            }
        }
    }
}

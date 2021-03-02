package com.dabeeo.indoormap.fragment

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dabeeo.component.utils.intents.IntentUtils
import com.dabeeo.imsdk.model.network.MapRow
import com.dabeeo.indoormap.BaseFragment
import com.dabeeo.indoormap.activity.MapActivity
import com.dabeeo.indoormap.adapter.MapListAdapter
import com.dabeeo.indoormap.common.ProjectLayout
import com.dabeeo.indoormap.common.ProjectString
import com.dabeeo.indoormap.databinding.FragmentMapListBinding
import com.dabeeo.indoormap.manager.MapManager
import com.dabeeo.indoormap.vm.MapListViewModel
import com.dabeeo.indoormap.vm.createViewModel
import io.reactivex.internal.operators.flowable.FlowableTakeWhile


class MapListFragment : BaseFragment<FragmentMapListBinding>() {

    companion object {
        fun newInstance() = MapListFragment()
    }

    private val viewModel by createViewModel<MapListViewModel>()

    private var mapManager = MapManager()
    private var mapRowList = ArrayList<MapRow>()
    private lateinit var adapter: MapListAdapter

    override fun getLayoutResourceId(): Int {
        return ProjectLayout.fragment_map_list
    }

    override fun setupViews() {
        binding.vm = viewModel
        binding.executePendingBindings()

        binding.mapSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val keyword = charSequence.toString()
                if(keyword.isEmpty()) {
                    adapter.setData(mapRowList)
                } else {
                    adapter.setData(mapManager.getMapList(keyword))
                }

            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun showMapList(mapRowList: List<MapRow>) {
        val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.mapListRecyclerView.addItemDecoration(divider)
        binding.mapListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapter = MapListAdapter(mapRowList as ArrayList<MapRow>)
        binding.mapListRecyclerView.adapter = adapter
        adapter.onClickItem = {
            val map = HashMap<String, String>()
            map[getString(ProjectString.intent_key_mapId)] = it.id
            intent(MapActivity::class.java, map, IntentUtils.EFFECT.SLIDE_LEFT_RIGHT)
        }

    }

    fun setMapList(mapList: List<MapRow>) {
        viewModel.mapList.observe(this, {
            showMapList(it)
        })
        mapRowList = mapList as ArrayList<MapRow>
        mapManager.setMapRowList(mapRowList)
        viewModel.mapList.postValue(mapRowList)

    }

}

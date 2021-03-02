package com.dabeeo.indoormap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dabeeo.imsdk.model.network.MapRow
import com.dabeeo.indoormap.R
import com.dabeeo.indoormap.databinding.ItemMapListBinding

class MapListAdapter(var list: ArrayList<MapRow>) :
    RecyclerView.Adapter<MapListAdapter.MapItemViewHolder>() {

    lateinit var onClickItem: (MapRow) -> Unit

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_map_list,
            parent,
            false
        ) as ItemMapListBinding
        return MapItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MapItemViewHolder, position: Int) {
        holder.onBindViewHolder(list[position])
    }

    fun setData(mapRowList: ArrayList<MapRow>) {
        list = mapRowList
        notifyDataSetChanged()
    }

    inner class MapItemViewHolder(binding: ItemMapListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var viewBinding = binding
        fun onBindViewHolder(item: MapRow) {
            viewBinding.item = item
            viewBinding.mapNameTextView.text = item.name
            viewBinding.itemLayout.setOnClickListener {
                onClickItem.invoke(item)
            }
        }
    }
}




package com.dabeeo.indoormap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dabeeo.imsdk.model.common.FloorInfo
import com.dabeeo.indoormap.databinding.ItemFloorListBinding
import com.dabeeo.indoormap.views.RoundButton

class FloorListAdapter(private var floors: ArrayList<FloorInfo>) : RecyclerView.Adapter<FloorListAdapter.ViewHolder>() {
    private var selectedFloorOrder = -1
    private var selectedFloor: RoundButton? = null
    lateinit var onClickItem: (FloorInfo) -> Unit

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFloorListBinding =
                ItemFloorListBinding.inflate(LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {
        val floor: FloorInfo = floors!![position]
        viewholder.bind(floor, position)
    }

    override fun getItemCount(): Int {
        return floors!!.size
    }

    fun selectFloor(floorOrder: Int) {
        selectedFloorOrder = floorOrder
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: ItemFloorListBinding) : RecyclerView.ViewHolder(binding.root),
            View.OnClickListener {

        private lateinit var floor: FloorInfo
        private var index: Int = 0

        fun bind(floor: FloorInfo, position: Int) {
            this.floor = floor
            this.index = position
            binding.floorButton.setNone()
            binding.floorButton.setText(floor.name[0].text)
            binding.floorButton.getTextView().setOnClickListener(this)

            if(selectedFloorOrder == -1) {
                return
            }

            val isSelectedFloor = floor.level == selectedFloorOrder
            if(isSelectedFloor) {
                selectedFloor = binding.floorButton
            }
            binding.floorButton.getTextView().isSelected = isSelectedFloor
        }

        override fun onClick(v: View) {
            when(v) {
                binding.floorButton.getTextView() -> {
                    selectedFloor?.run {
                        this.getTextView().isSelected = false
                    }
                    selectedFloor = binding.floorButton
                    selectedFloor!!.getTextView().isSelected = true
                    onClickItem.invoke(floor)
                }
            }
        }
    }

}
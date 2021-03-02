package com.dabeeo.indoormap.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.dabeeo.indoormap.R


class MaxHeightRecyclerView: RecyclerView {

    var maxHeight: Int = 0

    constructor(context: Context) : super(context) {
    }

    constructor(
            context: Context,
            attrs: AttributeSet?
    ) : super(context, attrs) {
        if (attrs != null) {
            val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView)
            maxHeight = styledAttrs.getDimensionPixelSize(R.styleable.MaxHeightRecyclerView_maxHeight,234*3) //200 is a default value
            styledAttrs.recycle()
        }
    }

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView)
            maxHeight = styledAttrs.getDimensionPixelSize(R.styleable.MaxHeightRecyclerView_maxHeight,234*3) //200 is a default value
            styledAttrs.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

//    fun setDatas(floors: List<FloorInfo>) {
//        adapter = FloorAdapter(context, floors as ArrayList<FloorInfo>)
//
//        val linearLayoutManager = LinearLayoutManager(context)
//        binding.floorRecyclerView.layoutManager = linearLayoutManager
//        binding.floorRecyclerView.adapter = adapter
//        binding.floorRecyclerView.addOnScrollListener(scrollListener)
//    }


//    fun setOnSelectFloorListener(listener : ISelectFloorListener) {
//        adapter.setOnSelectFloorListener(listener)
//    }
//
//    fun selectFloor(floorOrder: Int) {
//        adapter.selectFloor(floorOrder)
//    }


//    fun updateScroll() {
//        val scrollStatus: Boolean =
//            this.layoutManager!!.canScrollVertically()
//        if (!scrollStatus) {
//            this.layoutManager!!.setScrollEnable(true)
//        }
//        val offset: Int = this.layoutManager!!.computeVerticalScrollOffset()
//        val extent: Int = this.layoutManager!!.computeVerticalScrollExtent()
//        val range: Int = this.layoutManager!!.computeVerticalScrollRange()
//        if (!scrollStatus) {
//            this.layoutManager!!.setScrollEnable(false)
//        }
//    }

    private val scrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
//                    updateScroll()
                }
            }


}
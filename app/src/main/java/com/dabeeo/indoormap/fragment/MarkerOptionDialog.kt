package com.dabeeo.indoormap.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.dabeeo.component.utils.common.ConvertUtils
import com.dabeeo.imsdk.model.gl.Marker
import com.dabeeo.indoormap.common.ProjectLayout
import com.dabeeo.indoormap.databinding.DialogMarkerOptionBinding

class MarkerOptionDialog(private val marker: Marker, private val isOption: Boolean) : DialogFragment(), View.OnClickListener {
    var onAddMarker: ((Marker) -> Unit)? = null
    var onUpdateMarker: ((Marker) -> Unit)? = null
    var onCancel: ((Marker) -> Unit)? = null

    private lateinit var binding: DialogMarkerOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            ProjectLayout.dialog_marker_option,
            container,
            false
        )

        if(isOption) {
            binding.fiexdRotationCheckBox.isChecked = marker.fixedRotation
            binding.fiexdZoomCheckBox.isChecked = marker.fixedZoom
            binding.rotationEditText.setText(marker.rotation.toInt().toString())
        }


        binding.addMarkerButton.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)

        binding.fiexdRotationCheckBox.setOnCheckedChangeListener { p0, p1 ->
            marker.fixedRotation = p1
        }

        binding.fiexdZoomCheckBox.setOnCheckedChangeListener { p0, p1 ->
            marker.fixedZoom = p1
        }

        binding.rotationEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = p0.toString()
                if(input.isNotEmpty()) {
                    var castAngle = input.toInt()
                    if(castAngle <= 0) {
                        castAngle += 360
                        binding.rotationEditText.setText(castAngle.toString())
                        binding.rotationEditText.setSelection(castAngle.toString().length)
                    } else if(castAngle > 360) {
                        castAngle %= 360
                        binding.rotationEditText.setText(castAngle.toString())
                        binding.rotationEditText.setSelection(castAngle.toString().length)
                    }
                    marker.rotation = castAngle.toDouble()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        context?.run {
            val metrics: DisplayMetrics = this.resources.displayMetrics
            val width = metrics.widthPixels
            val params: WindowManager.LayoutParams = dialog!!.window!!.attributes
            params.width = (width - ConvertUtils.dpToPx(this, 46.0f)).toInt()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog!!.window!!.attributes = params
        }
    }

    override fun onClick(v: View) {
        when (v) {
            binding.addMarkerButton -> {
                if(isOption) {
                    onUpdateMarker!!.invoke(marker)
                }else {
                    onAddMarker!!.invoke(marker)
                }
                dismiss()
            }
            binding.cancelButton -> {
                if(!isOption) {
                    onCancel!!.invoke(marker)
                }
                dismiss()
            }
        }
    }
}
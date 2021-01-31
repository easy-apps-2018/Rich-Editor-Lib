package com.easyapps.richeditorlib.windows

import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.easyapps.richeditorlib.helpers.Helper
import com.easyapps.richeditorlib.interfaces.FontSizeListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider

class SizeSlider(
        item: MaterialButton,
        private val fontSizeListener: FontSizeListener,
        private var size: Float
) :
    PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, Helper.getPxByDp(80)) {

    init {
        val linearLayout = LinearLayout(item.context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.BOTTOM
            orientation = LinearLayout.VERTICAL
        }

        val slider = Slider(item.context).apply {
            valueFrom = 14f
            valueTo = 32f
            stepSize = 2f
            value = size
            addOnChangeListener { _, value, _ ->
                size = value
            }
        }

        setOnDismissListener {
            fontSizeListener.onFontSizeChanged(size)
            item.isChecked = false
        }

        linearLayout.addView(slider)
        isOutsideTouchable = true
        contentView = linearLayout
        isFocusable = true
        showAsDropDown(item, 0, 0)
    }
}
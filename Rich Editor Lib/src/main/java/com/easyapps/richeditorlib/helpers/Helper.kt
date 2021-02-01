package com.easyapps.richeditorlib.helpers

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.core.graphics.drawable.DrawableCompat
import com.easyapps.richeditorlib.R
import com.easyapps.richeditorlib.interfaces.StyleListener

object Helper {

    const val ZERO_WIDTH_SPACE_STR = "\u200B"
    const val BULLET = "\u2022"
    const val LEADING_MARGIN = 30f
    const val ZERO_WIDTH_SPACE_INT = 8203
    const val ZERO_WIDTH_SPACE_STR_ESCAPE = "&#8203;"

    fun itemCheckStatus(
        styleListener: StyleListener,
        isChecked: Boolean,
        iconTint: Int? = null
    ) {
        styleListener.apply {
            setChecked(isChecked)
            getStyleButton().apply {
                this.isChecked = isChecked
                iconTint?.let { color ->
                    setTint(icon, color)
                }
            }
        }
    }

    fun setTint(drawable: Drawable, color: Int) {
        (drawable as? LayerDrawable)?.getDrawable(1)?.let {
            DrawableCompat.setTint(it, color)
        }
    }

    fun getColors(context: Context): ArrayList<Int?> {
        val colorList = ArrayList<Int?>()
        context.resources.getStringArray(R.array.colors).forEach { color ->
            colorList.add(Color.parseColor(color))
        }
        return colorList
    }

    fun getColor(color: Int?): Int {
        var tmp = 0
        try {
            tmp = Color.parseColor("#" + Integer.toHexString(color!!))
        } catch (e: IllegalArgumentException) {
        }
        return tmp
    }

    fun getPxByDp(dp: Int): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

    fun getScreenWidth(): Int = Resources.getSystem().displayMetrics.widthPixels
}

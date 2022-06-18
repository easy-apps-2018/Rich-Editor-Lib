package com.easyapps.richeditorlib.windows

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easyapps.richeditorlib.R
import com.easyapps.richeditorlib.helpers.Helper
import com.easyapps.richeditorlib.interfaces.ForegroundColorListener
import com.google.android.material.button.MaterialButton

class ColorBar(
    item: MaterialButton,
    private val foregroundColorListener: ForegroundColorListener? = null
) :
    PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) {

    init {
        isOutsideTouchable = true
        isFocusable = true
        contentView = RecyclerView(item.context).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = ColorAdapter(Helper.getColors(item.context))
        }
        setOnDismissListener {
            item.isChecked = false
        }
        showAsDropDown(item, 0, -250)
    }

    inner class ColorAdapter(private val colors: ArrayList<Int?>) :
        RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

        inner class ColorViewHolder(val button: MaterialButton) : RecyclerView.ViewHolder(button)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
            val button = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_button, parent, false) as MaterialButton
            return ColorViewHolder(button)
        }

        override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
            colors[position]?.let { color ->
                holder.button.apply {
                    id = color
                    setBackgroundColor(Helper.getColor(color))
                    addOnCheckedChangeListener { button, _ ->
                        foregroundColorListener?.onColorChanged(button.id)
                    }
                }
            }
        }

        override fun getItemCount(): Int = colors.size
    }
}
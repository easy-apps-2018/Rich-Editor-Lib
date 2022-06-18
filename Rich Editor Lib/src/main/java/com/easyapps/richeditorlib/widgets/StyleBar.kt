package com.easyapps.richeditorlib.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginRight
import androidx.core.view.setPadding
import com.easyapps.richeditorlib.R
import com.easyapps.richeditorlib.interfaces.StyleListener
import com.easyapps.richeditorlib.styles.*
import com.google.android.material.button.MaterialButton

class StyleBar : HorizontalScrollView {

    private var container: LinearLayout
    private var styleItems = ArrayList<StyleListener>()
    private var bold: MaterialButton
    private var italic: MaterialButton
    private var underline: MaterialButton
    /*private var strikethrough: MaterialButton
    private var size: MaterialButton*/
    private var fontColor: MaterialButton

    private var colorPrimary: MaterialButton
    private var colorSecondary: MaterialButton
    private var colorTertiary: MaterialButton

    var colors = arrayOf(
        android.R.color.holo_red_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_green_light,
    )

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet? = null) :
            super(context, attrs, android.R.attr.horizontalScrollViewStyle)

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    init {
        isFillViewport = true
        isHorizontalScrollBarEnabled = false
        container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        addView(container)

        bold = getItem(R.id.bold, R.drawable.ic_bold)
        italic = getItem(R.id.italic, R.drawable.ic_italic)
        underline = getItem(R.id.underline, R.drawable.ic_underline)
        /*strikethrough = getItem(R.id.strike_through, R.drawable.ic_strikethrough)
        size = getItem(R.id.size, R.drawable.ic_size)*/
        fontColor = getItem(R.id.font_color, R.drawable.ic_text_format)
        colorPrimary = getColor(colors[0], 1002)
        colorSecondary = getColor(colors[1], 1003)
        colorTertiary = getColor(colors[2], 1004)
    }

    fun setEditText(editText: RichEditText) {
        styleItems.apply {
            add(StyleBold(bold, editText))
            add(StyleItalic(italic, editText))
            add(StyleUnderline(underline, editText))
            /*add(StyleStrikethrough(strikethrough, editText))
            add(StyleFontSize(this@StyleBar.size, editText))*/
            add(StyleForegroundColor(fontColor, editText))
            add(StyleSingleColor(colorPrimary, editText, colors[0]))
            add(StyleSingleColor(colorSecondary, editText, colors[1]))
            add(StyleSingleColor(colorTertiary, editText, colors[2]))
        }
    }

    fun getStyleItems(): ArrayList<StyleListener> = styleItems

    private fun getColor(
        color: Int,
        itemId: Int
    ): MaterialButton =  View.inflate(context, R.layout.item_button, container)
        .findViewById<MaterialButton>(R.id.item_button).apply {
            id = itemId
            setBackgroundColor(ContextCompat.getColor(context, color))
            isCheckable = true
        }

    private fun getItem(
            itemId: Int,
            itemIcon: Int
    ): MaterialButton = View.inflate(context, R.layout.item_button, container)
            .findViewById<MaterialButton>(R.id.item_button).apply {
                id = itemId
                icon = ContextCompat.getDrawable(context, itemIcon)
                isCheckable = true
            }
}
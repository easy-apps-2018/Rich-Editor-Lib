package com.easyapps.richeditorlib.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
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
    private var strikethrough: MaterialButton
    private var size: MaterialButton
    private var fontColor: MaterialButton

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
        strikethrough = getItem(R.id.strike_through, R.drawable.ic_strikethrough)
        size = getItem(R.id.size, R.drawable.ic_size)
        fontColor = getItem(R.id.font_color, R.drawable.ic_text_format)
    }

    fun setEditText(editText: RichEditText) {
        styleItems.apply {
            add(StyleBold(bold, editText))
            add(StyleItalic(italic, editText))
            add(StyleUnderline(underline, editText))
            add(StyleStrikethrough(strikethrough, editText))
            add(StyleFontSize(this@StyleBar.size, editText))
            add(StyleForegroundColor(fontColor, editText))
        }
    }

    fun getStyleItems(): ArrayList<StyleListener> = styleItems

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
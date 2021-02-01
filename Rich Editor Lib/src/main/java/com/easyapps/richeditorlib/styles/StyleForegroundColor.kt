package com.easyapps.richeditorlib.styles

import android.graphics.Color
import android.text.Editable
import com.easyapps.richeditorlib.abstracts.DynamicStyle
import com.easyapps.richeditorlib.helpers.Helper
import com.easyapps.richeditorlib.interfaces.ForegroundColorListener
import com.easyapps.richeditorlib.spans.SpanForegroundColor
import com.easyapps.richeditorlib.widgets.RichEditText
import com.easyapps.richeditorlib.windows.ColorBar
import com.google.android.material.button.MaterialButton

class StyleForegroundColor(private val item: MaterialButton, private val editText: RichEditText) :
    DynamicStyle<SpanForegroundColor>(), ForegroundColorListener {

    private var foreColor = Color.BLACK
    private var colorBar: ColorBar? = null

    init {
        editText.foregroundColorListener = this
        item.setOnClickListener {
            colorBar = ColorBar(item, this)
        }
    }

    override fun changeSpanInsideStyle(
        editable: Editable,
        start: Int,
        end: Int,
        e: SpanForegroundColor
    ) {
        super.changeSpanInsideStyle(editable, start, end, e)
        if (e.foregroundColor != foreColor)
            applyNewStyle(editable, start, end, foreColor)
    }

    override fun newSpan(style: Int): SpanForegroundColor = SpanForegroundColor(style)

    override fun newSpan(): SpanForegroundColor = SpanForegroundColor(foreColor)

    override fun styleChangedHook(style: Int) {
        foreColor = style
    }

    override fun getStyleButton(): MaterialButton = item

    override fun setChecked(isChecked: Boolean) {}

    override fun getIsChecked(): Boolean = true

    override fun onColorChanged(color: Int, from: String?) {
        foreColor = color

        if (from == null) {
            editText.requestFocus()
            colorBar?.dismiss()
        }

        Helper.setTint(item.icon, color)
        item.isChecked = false

        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (end > start)
            applyNewStyle(editText.editableText, start, end, foreColor)
    }

}
package com.easyapps.richeditorlib.styles

import android.text.Editable
import com.easyapps.richeditorlib.abstracts.DynamicStyle
import com.easyapps.richeditorlib.interfaces.FontSizeListener
import com.easyapps.richeditorlib.spans.SpanFontSize
import com.easyapps.richeditorlib.widgets.RichEditText
import com.easyapps.richeditorlib.windows.SizeSlider
import com.google.android.material.button.MaterialButton

class StyleFontSize(private val item: MaterialButton, private val editText: RichEditText) : DynamicStyle<SpanFontSize>(), FontSizeListener {

    private var size = 18
    private var sizeSlider: SizeSlider? = null

    init {
        editText.fontSizeListener = this
        item.setOnClickListener {
            sizeSlider = SizeSlider(item, this, size.toFloat())
        }
    }

    override fun changeSpanInsideStyle(editable: Editable, start: Int, end: Int, e: SpanFontSize) {
        super.changeSpanInsideStyle(editable, start, end, e)
        if (e.size != size)
            applyNewStyle(editable, start, end, size)
    }

    override fun newSpan(style: Int): SpanFontSize = SpanFontSize(style)

    override fun newSpan(): SpanFontSize = SpanFontSize(size)

    override fun styleChangedHook(style: Int) {
        size = style
    }

    override fun getStyleButton(): MaterialButton = item

    override fun setChecked(isChecked: Boolean) { }

    override fun getIsChecked(): Boolean = true

    override fun onFontSizeChanged(size: Float, from: String?) {
        this.size = size.toInt()

        if (from == null) {
            item.isChecked = false
            editText.requestFocus()
            sizeSlider?.dismiss()
        }

        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (end > start)
            applyNewStyle(editText.editableText, start, end, this.size)
    }
}
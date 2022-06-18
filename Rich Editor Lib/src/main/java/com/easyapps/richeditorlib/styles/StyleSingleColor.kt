package com.easyapps.richeditorlib.styles

import androidx.core.content.ContextCompat
import com.easyapps.richeditorlib.abstracts.DynamicStyle
import com.easyapps.richeditorlib.interfaces.ForegroundColorListener
import com.easyapps.richeditorlib.spans.SpanBold
import com.easyapps.richeditorlib.spans.SpanForegroundColor
import com.easyapps.richeditorlib.widgets.RichEditText
import com.google.android.material.button.MaterialButton

class StyleSingleColor(
    private val item: MaterialButton,
    private val editText: RichEditText,
    private var color: Int
): DynamicStyle<SpanForegroundColor>() {

    init {
        item.setOnClickListener {
            editText.requestFocus()
            val color = ContextCompat.getColor(editText.context, color)
            editText.foreColor = color
            editText.foregroundColorListener?.onColorChanged(color, "SpanForegroundColor")

            val start = editText.selectionStart
            val end = editText.selectionEnd
            if (end > start)
                applyNewStyle(editText.editableText, start, end, color)
        }
    }

    override fun newSpan(style: Int): SpanForegroundColor = SpanForegroundColor(style)

    override fun styleChangedHook(style: Int) {
        color = style
    }

    override fun getStyleButton(): MaterialButton = item

    override fun setChecked(isChecked: Boolean) = Unit

    override fun getIsChecked(): Boolean = true

    override fun newSpan(): SpanForegroundColor = SpanForegroundColor(color)
}
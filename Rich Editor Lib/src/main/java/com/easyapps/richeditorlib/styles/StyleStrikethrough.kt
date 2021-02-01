package com.easyapps.richeditorlib.styles

import android.text.style.StrikethroughSpan
import com.easyapps.richeditorlib.abstracts.Style
import com.easyapps.richeditorlib.helpers.Helper
import com.easyapps.richeditorlib.widgets.RichEditText
import com.google.android.material.button.MaterialButton

class StyleStrikethrough(private val item: MaterialButton, editText: RichEditText) : Style<StrikethroughSpan>() {

    private var isChecked = false

    init {
        item.setOnClickListener {
            editText.requestFocus()
            isChecked = !isChecked
            Helper.itemCheckStatus(this, isChecked)
            applyStyle(editText.editableText, editText.selectionStart, editText.selectionEnd)
        }
    }

    override fun newSpan(): StrikethroughSpan = StrikethroughSpan()

    override fun getStyleButton(): MaterialButton = item

    override fun setChecked(isChecked: Boolean) {
        this.isChecked = isChecked
    }

    override fun getIsChecked(): Boolean = isChecked
}
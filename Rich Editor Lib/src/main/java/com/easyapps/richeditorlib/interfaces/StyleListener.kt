package com.easyapps.richeditorlib.interfaces

import android.text.Editable
import com.google.android.material.button.MaterialButton

interface StyleListener {
    fun applyStyle(editable: Editable, start: Int, end: Int)
    fun getStyleButton(): MaterialButton
    fun setChecked(isChecked: Boolean)
    fun getIsChecked(): Boolean
}
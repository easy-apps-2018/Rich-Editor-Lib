package com.easyapps.richeditorlib.spans

import android.text.style.AbsoluteSizeSpan
import com.easyapps.richeditorlib.interfaces.DynamicSpanListener

class SpanFontSize(size: Int) : AbsoluteSizeSpan(size, true), DynamicSpanListener {
    override fun getDynamicSpan(): Int = size
}
package com.easyapps.richeditorlib.spans

import android.text.style.ForegroundColorSpan
import com.easyapps.richeditorlib.interfaces.DynamicSpanListener

class SpanForegroundColor(color: Int) : ForegroundColorSpan(color), DynamicSpanListener {
    override fun getDynamicSpan(): Int = foregroundColor
}
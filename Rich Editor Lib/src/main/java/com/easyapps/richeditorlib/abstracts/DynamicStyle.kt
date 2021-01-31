package com.easyapps.richeditorlib.abstracts

import android.text.Editable
import android.text.Spanned
import com.easyapps.richeditorlib.interfaces.DynamicSpanListener

abstract class DynamicStyle<E : DynamicSpanListener> : Style<E>() {

    fun applyNewStyle(editable: Editable, start: Int, end: Int, currentStyle: Int?) {

        var startSpan: E? = null
        var startSpanStart = Int.MAX_VALUE
        var endSpan: E? = null
        var endSpanStart = -1
        var endSpanEnd = -1

        var detectStart = start
        if (start > 0)
            detectStart = start - 1

        var detectEnd = end
        if (end < editable.length)
            detectEnd = end + 1

        val existingSpans = editable.getSpans(detectStart, detectEnd, clazzE)
        if (existingSpans != null && existingSpans.isNotEmpty()) {
            for (span in existingSpans) {
                val spanStart = editable.getSpanStart(span)

                if (spanStart < startSpanStart) {
                    startSpanStart = spanStart
                    startSpan = span
                }

                if (spanStart >= endSpanStart) {
                    endSpanStart = spanStart
                    endSpan = span
                    val thisSpanEnd = editable.getSpanEnd(span)
                    if (thisSpanEnd > endSpanEnd) {
                        endSpanEnd = thisSpanEnd
                    }
                }
            }
            if (startSpan == null || endSpan == null)
                return

            if (end > endSpanEnd)
                endSpanEnd = end

            for (span in existingSpans)
                editable.removeSpan(span)

            val startSpanFeature = startSpan.getDynamicSpan()
            val endSpanFeature = endSpan.getDynamicSpan()

            when {
                startSpanFeature == currentStyle && endSpanFeature == currentStyle -> editable.setSpan(
                    newSpan(),
                    startSpanStart,
                    endSpanEnd,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                startSpanFeature == currentStyle -> editable.apply {
                    setSpan(
                        newSpan(startSpanFeature),
                        startSpanStart,
                        end,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                    setSpan(
                        newSpan(endSpanFeature),
                        end,
                        endSpanEnd,
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                }
                endSpanFeature == currentStyle -> editable.apply {
                    setSpan(
                        newSpan(startSpanFeature),
                        startSpanStart,
                        start,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                    setSpan(
                        newSpan(endSpanFeature),
                        start,
                        endSpanEnd,
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                }
                else -> {
                    editable.setSpan(
                        newSpan(startSpanFeature),
                        startSpanStart,
                        start,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                    if (endSpanEnd > end) {
                        editable.setSpan(
                            newSpan(endSpanFeature),
                            end,
                            endSpanEnd,
                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                        )
                    }
                    editable.setSpan(newSpan(), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }
        } else
            editable.setSpan(newSpan(), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    }

    override fun extendPreviousSpan(editable: Editable, eStart: Int) {
        val pSpans = editable.getSpans(eStart, eStart, clazzE)
        if (pSpans != null && pSpans.isNotEmpty()) {
            val lastSpan = pSpans[0]
            val start = editable.getSpanStart(lastSpan)
            val end = editable.getSpanEnd(lastSpan)
            editable.removeSpan(lastSpan)
            val lastSpanFeature = lastSpan.getDynamicSpan()
            styleChangedHook(lastSpanFeature)
            editable.setSpan(newSpan(lastSpanFeature), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    abstract fun newSpan(style: Int): E?

    abstract fun styleChangedHook(style: Int)
}
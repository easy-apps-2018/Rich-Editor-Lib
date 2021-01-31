package com.easyapps.richeditorlib.spans

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.style.QuoteSpan
import com.easyapps.richeditorlib.helpers.Helper


class SpanQuote: QuoteSpan() {

    override fun getLeadingMargin(first: Boolean): Int = Helper.LEADING_MARGIN.toInt() * 2

    override fun drawLeadingMargin(
        c: Canvas,
        p: Paint,
        x: Int,
        dir: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        first: Boolean,
        layout: Layout
    ) {

        c.translate(Helper.LEADING_MARGIN, 0f)
        val style = p.style
        val color = p.color
        p.style = Paint.Style.FILL
        p.color = Color.RED

        c.drawRect(
            x.toFloat(),
            top.toFloat(),
            x + dir * 2 + 5f,
            bottom.toFloat(),
            p
        )
        p.style = style
        p.color = color
        c.translate(-Helper.LEADING_MARGIN, 0f)
    }
}
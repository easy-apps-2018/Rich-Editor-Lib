package com.easyapps.richeditorlib.spans

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.Spanned
import com.easyapps.richeditorlib.helpers.Helper
import com.easyapps.richeditorlib.interfaces.SpanList

class SpanListNumber(private var thisNumber: Int) : SpanList {

    override fun getLeadingMargin(first: Boolean): Int = Helper.LEADING_MARGIN.toInt() + 80

    override fun drawLeadingMargin(
        c: Canvas?,
        p: Paint?,
        x: Int,
        dir: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence?,
        start: Int,
        end: Int,
        first: Boolean,
        layout: Layout?
    ) {
        if ((text as Spanned).getSpanStart(this) == start) {

            val style = p!!.style
            p.style = Paint.Style.FILL
            p.color = Color.BLACK

            val draw = if (thisNumber != -1)
                "$thisNumber."
            else
                Helper.BULLET

            c?.drawText(draw, (x + dir + Helper.LEADING_MARGIN), baseline.toFloat(), p)
            p.style = style
        }
    }
}
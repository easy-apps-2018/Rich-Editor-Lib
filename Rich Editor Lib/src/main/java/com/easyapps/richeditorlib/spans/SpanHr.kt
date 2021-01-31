package com.easyapps.richeditorlib.spans

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan
import com.easyapps.richeditorlib.helpers.Helper
import com.easyapps.richeditorlib.interfaces.Span

class SpanHr : ReplacementSpan(), Span {

    private val p = 1f
    private val width = Helper.getScreenWidth()

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int = (width * p).toInt()

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        paint.strokeWidth = 1f
        val lineY = top + (bottom - top) / 2
        canvas.drawLine(
            x + (width * (1 - p) / 2),
            lineY.toFloat(),
            x + (width * p),
            lineY.toFloat(),
            paint
        )

    }

    override fun getHtml(): String = "<hr />"
}
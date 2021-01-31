package com.easyapps.richeditorlib.events

import android.text.Selection
import android.text.Spannable
import android.text.method.ArrowKeyMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView


class MovementMethod : ArrowKeyMovementMethod() {

    fun getInstance(): MovementMethod = this

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action
        val offset = getTextOffset(widget, event)
        val link = buffer.getSpans(offset, offset, ClickableSpan::class.java)
        if (link.isNotEmpty()) {
            when (action) {
                MotionEvent.ACTION_UP -> link[0].onClick(widget as View)
                MotionEvent.ACTION_DOWN -> Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]))
            }
            return true
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    private fun getTextOffset(widget: TextView, event: MotionEvent): Int {
        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= widget.totalPaddingLeft
        y -= widget.totalPaddingTop
        x += widget.scrollX
        y += widget.scrollY
        val layout = widget.layout
        val line = layout.getLineForVertical(y)
        return layout.getOffsetForHorizontal(line, x.toFloat())
    }
}
package com.easyapps.richeditorlib.widgets

import android.content.Context
import android.text.util.Linkify
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.easyapps.richeditorlib.events.MovementMethod
import com.easyapps.richeditorlib.inner.Html

class RichTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(
            context,
            attrs,
            android.R.attr.textViewStyle
    )
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    init {
        Linkify.addLinks(this, Linkify.ALL)
        linksClickable = true
        autoLinkMask = Linkify.ALL
        movementMethod = MovementMethod().getInstance()
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
    }

    fun setHtml(html: String) {
        text = Html.fromHtml(html, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
    }
}
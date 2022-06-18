package com.easyapps.richeditorlib.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.util.Linkify
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.easyapps.richeditorlib.R
import com.easyapps.richeditorlib.helpers.Helper
import com.easyapps.richeditorlib.inner.Html
import com.easyapps.richeditorlib.interfaces.FontSizeListener
import com.easyapps.richeditorlib.interfaces.ForegroundColorListener
import com.easyapps.richeditorlib.interfaces.StyleListener
import com.easyapps.richeditorlib.spans.SpanFontSize
import com.easyapps.richeditorlib.spans.SpanUnderline

class RichEditText : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        attrs,
        android.R.attr.editTextStyle
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var monitoring = true
    private var bold = false
    private var italic = false
    private var underline = false
    private var strikethrough = false

    var foreColor: Int = Color.BLACK
    var hint: String = ""
    var fontSize: Float = 18f

    private var styleBar: StyleBar? = null
    var fontSizeListener: FontSizeListener? = null
    var foregroundColorListener: ForegroundColorListener? = null
    private var styles = ArrayList<StyleListener>()

    init {
        isFocusableInTouchMode = true
        Linkify.addLinks(this, Linkify.ALL)
        gravity = Gravity.START
        autoLinkMask = Linkify.ALL
        inputType = (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
                or EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
        setTextColor(foreColor)
        setHint(hint)
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        addTextChangedListener(textWatcher())
    }

    fun setStyleBar(styleBar: StyleBar) {
        this.styleBar = styleBar
        this.styleBar?.let { style ->
            styles = style.getStyleItems()
        }
    }

    fun setHtml(html: String) {
        monitoring = false
        editableText.append(Html.fromHtml(html, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH))
        monitoring = true
    }

    fun getHtml(): String = StringBuffer().apply {
        append("<html>\n")
        append("<body>\n")
        append(Html.toHtml(editableText, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH))
        append("</body>")
        append("\n</html>")
    }.toString().replace(Helper.ZERO_WIDTH_SPACE_STR_ESCAPE.toRegex(), "")

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)

        if (styleBar == null)
            return

        bold = false
        italic = false
        underline = false
        strikethrough = false

        //
        // Two cases:
        val editable = editableText
        if (selStart > 0 && selStart == selEnd) {
            //
            // 1. Selection is just a pure cursor
            for (styleSpan in editable.getSpans(selStart - 1, selEnd, CharacterStyle::class.java))
                detectSpan(styleSpan)
        } else {
            //
            // Selection is a range
            for (styleSpan in editable.getSpans(selStart, selEnd, CharacterStyle::class.java))
                if (editable.getSpanStart(styleSpan) <= selStart && editable.getSpanEnd(styleSpan) >= selEnd)
                    detectSpan(styleSpan)
        }

        //
        // Set style checked status
        Helper.apply {
            itemCheckStatus(styles[0], bold)
            itemCheckStatus(styles[1], italic)
            itemCheckStatus(styles[2], underline)
            //itemCheckStatus(styles[3], strikethrough)
            // Font size at styles[4]
            itemCheckStatus(styles[3], false, foreColor)
        }
        //foreColor = Color.BLACK
    }

    private fun detectSpan(styleSpan: CharacterStyle) {
        when (styleSpan) {
            is StyleSpan -> when (styleSpan.style) {
                Typeface.BOLD -> bold = true
                Typeface.ITALIC -> italic = true
            }
            is SpanUnderline -> underline = true
            is StrikethroughSpan -> strikethrough = true
            is SpanFontSize -> fontSizeListener?.onFontSizeChanged(
                styleSpan.size.toFloat(),
                "SpanFontSize"
            )
            is ForegroundColorSpan -> {
                foreColor = styleSpan.foregroundColor
                foregroundColorListener?.onColorChanged(foreColor, "SpanForegroundColor")
            }
        }
    }

    private fun textWatcher(): TextWatcher = object : TextWatcher {
        var startPos = 0
        var endPos = 0
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!monitoring)
                return
            startPos = start
            endPos = start + count
        }

        override fun afterTextChanged(s: Editable) {
            if (!monitoring)
                return
            for (style in styles)
                style.applyStyle(s, startPos, endPos)
            Linkify.addLinks(s, Linkify.ALL)
        }
    }
}
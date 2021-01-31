package com.easyapps.richeditorlib.abstracts

import android.text.Editable
import android.text.Spanned
import com.easyapps.richeditorlib.helpers.Helper
import com.easyapps.richeditorlib.interfaces.StyleListener
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class Style<E> : StyleListener {

    private val parameterizedType = this.javaClass.genericSuperclass as ParameterizedType
    var clazzE = parameterizedType.actualTypeArguments[0] as Class<E>

    override fun applyStyle(editable: Editable, start: Int, end: Int) {
        if (getIsChecked()) {
            val spans = editable.getSpans(start, end, clazzE)
            if (end > start) {
                //
                // User inputs or user selects a range
                var existingESpan: E? = null
                if (spans.isNotEmpty())
                    existingESpan = spans[0]

                if (existingESpan == null)
                    checkAndMergeSpan(editable, start, end, clazzE)
                else {
                    val existingESpanStart = editable.getSpanStart(existingESpan)
                    val existingESpanEnd = editable.getSpanEnd(existingESpan)
                    if (existingESpanStart <= start && existingESpanEnd >= end) {
                        // The selection is just within an existing E span
                        // Do nothing for this case
                        changeSpanInsideStyle(editable, start, end, existingESpan)
                    } else
                        checkAndMergeSpan(editable, start, end, clazzE)
                }
            } else {
                //
                // User deletes
                if (spans.isNotEmpty()) {
                    var span = spans[0]
                    var lastSpanStart = editable.getSpanStart(span)
                    for (e in spans) {
                        val lastSpanStartTmp = editable.getSpanStart(e)
                        if (lastSpanStartTmp > lastSpanStart) {
                            lastSpanStart = lastSpanStartTmp
                            span = e
                        }
                    }
                    val eStart = editable.getSpanStart(span)
                    val eEnd = editable.getSpanEnd(span)
                    if (eStart >= eEnd) {
                        editable.removeSpan(span)
                        extendPreviousSpan(editable, eStart)
                        setChecked(false)
                        Helper.itemCheckStatus(this, false)
                    }
                }
            }
        } else {
            when {
                //
                // User un-checks the style
                end > start -> {
                    //
                    // User inputs or user selects a range
                    val spans = editable.getSpans(start, end, clazzE)
                    if (spans.isNotEmpty()) {
                        spans[0]?.let { span ->
                            //
                            // User stops the style, and wants to show
                            // un-UNDERLINE characters
                            val ess = editable.getSpanStart(span) // ess == existing span start
                            val ese = editable.getSpanEnd(span) // ese = existing span end
                            when {
                                start >= ese -> {
                                    // User inputs to the end of the existing e span
                                    // End existing e span
                                    editable.apply {
                                        removeSpan(span)
                                        setSpan(
                                            span,
                                            ess,
                                            start - 1,
                                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                                        )
                                    }
                                }
                                start == ess && end == ese -> {
                                    // Case 1 desc:
                                    // *BBBBBB*
                                    // All selected, and un-check e
                                    editable.removeSpan(span)
                                }
                                start > ess && end < ese -> {
                                    // Case 2 desc:
                                    // BB*BB*BB
                                    // *BB* is selected, and un-check e
                                    editable.apply {
                                        removeSpan(span)
                                        setSpan(
                                            newSpan(),
                                            ess,
                                            start,
                                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                                        )
                                        setSpan(
                                            newSpan(),
                                            end,
                                            ese,
                                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                                        )
                                    }
                                }
                                start == ess && end < ese -> {
                                    // Case 3 desc:
                                    // *BBBB*BB
                                    // *BBBB* is selected, and un-check e
                                    editable.apply {
                                        removeSpan(span)
                                        editable.setSpan(
                                            newSpan(),
                                            end,
                                            ese,
                                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                                        )
                                    }
                                }
                                start > ess && end == ese -> {
                                    // Case 4 desc:
                                    // BB*BBBB*
                                    // *BBBB* is selected, and un-check e
                                    editable.apply {
                                        removeSpan(span)
                                        editable.setSpan(
                                            newSpan(),
                                            ess,
                                            start,
                                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                                        )
                                    }
                                }
                                else -> {
                                    // Do nothing by default
                                }
                            }
                        }
                    }
                }
                end == start -> {
                    //
                    // User changes focus position
                    // Do nothing for this case
                }
                else -> {
                    //
                    // User deletes
                    val spans = editable.getSpans(start, end, clazzE)
                    if (spans.isNotEmpty()) {
                        spans[0]?.let { span ->
                            val eStart = editable.getSpanStart(span)
                            val eEnd = editable.getSpanEnd(span)
                            if (eStart < eEnd) {
                                //
                                // Do nothing, the default behavior is to extend
                                // the span's area.
                                // The proceeding characters should be also
                                // UNDERLINE
                                setChecked(true)
                                Helper.itemCheckStatus(this, true)
                            }
                        }
                    }
                }
            }
        }
    }

    open fun extendPreviousSpan(editable: Editable, eStart: Int) {
        // Do nothing by default
    }

    open fun changeSpanInsideStyle(editable: Editable, start: Int, end: Int, e: E) {
        // Do nothing by default
    }

    private fun checkAndMergeSpan(editable: Editable, start: Int, end: Int, clazzE: Class<E>) {
        var leftSpan: E? = null
        val leftSpans = editable.getSpans(start, start, clazzE)
        if (leftSpans.isNotEmpty())
            leftSpan = leftSpans[0]

        var rightSpan: E? = null
        val rightSpans = editable.getSpans(end, end, clazzE)
        if (rightSpans.isNotEmpty())
            rightSpan = rightSpans[0]

        val leftSpanStart = editable.getSpanStart(leftSpan)
        val rightSpanEnd = editable.getSpanEnd(rightSpan)
        removeAllSpans(editable, start, end, clazzE)
        when {
            leftSpan != null && rightSpan != null -> editable.setSpan(
                newSpan(),
                leftSpanStart,
                rightSpanEnd,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
            leftSpan != null && rightSpan == null -> editable.setSpan(
                newSpan(),
                leftSpanStart,
                end,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
            leftSpan == null && rightSpan != null ->
                editable.setSpan(
                    newSpan(),
                    start,
                    rightSpanEnd,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            else -> editable.setSpan(newSpan(), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        }
    }

    private fun removeAllSpans(editable: Editable, start: Int, end: Int, clazzE: Class<E>) {
        for (span in editable.getSpans(start, end, clazzE))
            editable.removeSpan(span)
    }

    abstract fun newSpan(): E?
}
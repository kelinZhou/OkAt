package com.kelin.okat

import android.text.Selection
import android.text.SpanWatcher
import android.text.Spannable
import com.kelin.okat.callback.OkAtHandlerDelegate
import kotlin.math.abs
import kotlin.reflect.KClass

internal class SelectionSpanWatcher<S : Any, T : AtTarget>(private val kClass: KClass<S>, private val callback: OkAtHandlerDelegate<T>) : SpanWatcher {
    private var selStart = 0
    private var selEnd = 0
    override fun onSpanChanged(text: Spannable, what: Any, ostart: Int, oend: Int, nstart: Int, nend: Int) {
        if (what === Selection.SELECTION_END && selEnd != nstart) {
            selEnd = nstart
            text.getSpans(nstart, nend, kClass.java).firstOrNull()?.run {
                val spanStart = text.getSpanStart(this)
                val spanEnd = text.getSpanEnd(this)
                val index = if (abs(selEnd - spanEnd) > abs(selEnd - spanStart)) spanStart else spanEnd
                Selection.setSelection(text, Selection.getSelectionStart(text), index)
            }
        }

        if (what === Selection.SELECTION_START && selStart != nstart) {
            selStart = nstart
            text.getSpans(nstart, nend, kClass.java).firstOrNull()?.run {
                val spanStart = text.getSpanStart(this)
                val spanEnd = text.getSpanEnd(this)
                val index = if (abs(selStart - spanEnd) > abs(selStart - spanStart)) spanStart else spanEnd
                Selection.setSelection(text, index, Selection.getSelectionEnd(text))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSpanRemoved(text: Spannable?, what: Any?, start: Int, end: Int) {
        if (!text.isNullOrEmpty() && what is AtTargetSpan<*>) {
            callback.onAtChanged(OkAt.REASON_REMOVED, text.getSpans(0, text.length, AtTargetSpan::class.java).mapNotNull { (it as AtTargetSpan<*>).bindingData as? T })
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSpanAdded(text: Spannable?, what: Any?, start: Int, end: Int) {
        if (!text.isNullOrEmpty() && what is AtTargetSpan<*>) {
            callback.onAtChanged(OkAt.REASON_ADD, text.getSpans(0, text.length, AtTargetSpan::class.java).mapNotNull { (it as AtTargetSpan<*>).bindingData as? T })
        }
    }
}
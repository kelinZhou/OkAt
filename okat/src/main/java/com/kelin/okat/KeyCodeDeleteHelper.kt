package com.kelin.okat

import android.text.Selection
import android.text.Spannable
import android.view.KeyEvent
import android.widget.EditText

object KeyCodeDeleteHelper {
    fun onDelDown(text: Spannable, keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
            val selectionStart = Selection.getSelectionStart(text)
            val selectionEnd = Selection.getSelectionEnd(text)
            text.getSpans(selectionStart, selectionEnd, DataBindingSpan::class.java)
                .firstOrNull { text.getSpanEnd(it) == selectionStart }?.run {
                    return (selectionStart == selectionEnd).also {
                        val spanStart = text.getSpanStart(this)
                        val spanEnd = text.getSpanEnd(this)
                        Selection.setSelection(text, spanStart, spanEnd)
                    }
                }
        }
        return false
    }
}
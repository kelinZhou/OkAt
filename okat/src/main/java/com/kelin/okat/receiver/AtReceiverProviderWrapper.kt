package com.kelin.okat.receiver

import android.text.Selection
import android.widget.EditText
import com.kelin.okat.AtConfig

/**
 * **描述:** AtReceiver提供者。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2022/5/6 12:24 PM
 *
 * **版本:** v 1.0.0
 */
internal class AtReceiverProviderWrapper(private val targetView: EditText, private val atConfig: AtConfig, private val provider: AtReceiverProvider) : AtReceiverProvider {
    override val receiver: AtReceiver
        get() = provider.receiver ?: targetView.text.let {
            val start = Selection.getSelectionStart(it)
            AtReceiveHandler(atConfig, it, start, Selection.getSelectionEnd(it) - start)
        }
}
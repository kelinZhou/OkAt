package com.kelin.okat.callback

import com.kelin.okat.receiver.AtReceiver
import com.kelin.okat.AtTarget

/**
 * **描述:** OkAt的处理器的代理。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2020/11/26 7:50 PM
 *
 * **版本:** v 1.0.0
 */
internal class OkAtHandlerDelegate<T : AtTarget> : OkAtHandler<T> {
    private var onInputListener: ((receiver: AtReceiver) -> Unit)? = null
    private var onRemoveAtListener: ((reason: Int, currentContacts: List<T>) -> Unit)? = null

    override fun doAfterInputAt(l: (receiver: AtReceiver) -> Unit) {
        onInputListener = l
    }

    override fun doAfterAtChanged(l: (reason: Int, currentContacts: List<T>) -> Unit) {
        onRemoveAtListener = l
    }

    internal fun onInputAt(receiver: AtReceiver) {
        onInputListener?.invoke(receiver)
    }

    internal fun onAtChanged(reason: Int, currentContacts: List<T>) {
        onRemoveAtListener?.invoke(reason, currentContacts)
    }
}
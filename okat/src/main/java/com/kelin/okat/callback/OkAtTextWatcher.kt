package com.kelin.okat.callback

import android.text.Editable
import android.text.TextWatcher
import com.kelin.okat.*
import com.kelin.okat.receiver.AtReceiveHandler
import com.kelin.okat.receiver.AtReceiver
import com.kelin.okat.receiver.AtReceiverProvider

/**
 * **描述:** 用于监听文字的改变。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2022/4/22 12:01 PM
 *
 * **版本:** v 1.0.0
 */
internal class OkAtTextWatcher(private val config: AtConfig, private val okAtHandler: OkAtHandlerDelegate<out AtTarget>) : TextWatcher, AtReceiverProvider {

    override var receiver: AtReceiver? = null
        private set

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        if (text != null && text is Editable && count == 1 && text.toString().substring(start, start + count) == config.prefix.display) {
            okAtHandler.onInputAt(AtReceiveHandler(config, text, start, 1).also { receiver = it })
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
}
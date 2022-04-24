package com.kelin.okat.callback

import com.kelin.okat.AtReceiver

/**
 * **描述:** OkAt的处理器。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2020/11/26 7:44 PM
 *
 * **版本:** v 1.0.0
 */
interface OkAtHandler<T> {
    /**
     * 监听户输入At。
     * @param l 用户输入At后的回调，用于处理用户输入At后调起选择页面的逻辑。
     */
    fun doAfterInputAt(l: (receiver: AtReceiver) -> Unit)

    /**
     * 监听At的数量的改变。
     * @param l 当At被添加或移除时调用，回调中会返回改变的原因reason以及当前文本框中所有的At。
     */
    fun doAfterAtChanged(l: (reason: Int, currentContacts: List<T>) -> Unit)
}
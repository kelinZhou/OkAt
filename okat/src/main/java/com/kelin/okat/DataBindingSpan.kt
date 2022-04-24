package com.kelin.okat

/**
 * **描述:** 支持数据绑定的Span
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2020/11/25 10:02 AM
 *
 * **版本:** v 1.0.0
 */
interface DataBindingSpan<T> {
    /**
     * 用于展示的文本。
     */
    val spannedText: CharSequence

    /**
     * 与其绑定的数据。
     */
    val bindingData: T

    fun getRealSuffix(suffix: String): String
}
package com.kelin.okat

import androidx.annotation.ColorInt

/**
 * **描述:** 前缀At符号的映射关系。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2022/4/21 10:46 PM
 *
 * **版本:** v 1.0.0
 */
data class AtConfig(
    /**
     * At的文本颜色。
     */
    @get:ColorInt val atColor: Int,
    /**
     * 前缀映射关系。
     */
    val prefix: AtMapper,
    /**
     * 后缀映射关系。
     */
    val suffix: AtMapper
) {
    /**
     * At的点击事件。
     */
    internal var onTargetClickListener: ((target: AtTarget) -> Unit)? = null

    /**
     * 判断AtTarget是否支持点击。
     */
    internal val isTargetClickable: Boolean
        get() = onTargetClickListener != null

    /**
     * 判断前后缀是否相同。
     */
    val theSameBothEnds: Boolean
        get() = prefix.display == suffix.display
}

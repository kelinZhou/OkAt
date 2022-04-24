package com.kelin.okat

/**
 * **描述:** At符号的映射关系。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2022/4/21 10:46 PM
 *
 * **版本:** v 1.0.0
 */
open class AtMapper(
    /**
     * 显示样式，用于设置At显示到屏幕上的样子，不要与real相同，否则无法区分是用户手动输入的假At还是真正的At。
     */
    val display: String,
    /**
     * 实际样式，数据持久化时真正的At的值，不能与display相同，否则无法区分是用户手动输入的假At还是真正的At。
     */
    val real: String
)

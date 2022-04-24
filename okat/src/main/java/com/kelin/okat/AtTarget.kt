package com.kelin.okat

import java.io.Serializable

/**
 * **描述:** 描述At的目标。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2020-04-02 09:41
 *
 * **版本:** v 1.0.0
 */
interface AtTarget : Serializable {

    /**
     * At的目标对象的Id。
     */
    val id: String

    /**
     * 显示文字。
     */
    val text: String

    /**
     * At目标的类型。
     */
    val type: Int
        get() = 0
}
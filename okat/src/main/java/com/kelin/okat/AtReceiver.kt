package com.kelin.okat


/**
 * **描述:** At接收者
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2020/11/25 11:09 AM
 *
 * **版本:** v 1.0.0
 */
interface AtReceiver {
    fun receive(target: AtTarget)

    fun receive(targets: List<AtTarget>)
}
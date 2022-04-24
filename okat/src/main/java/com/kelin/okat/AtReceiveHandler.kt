package com.kelin.okat

import android.text.Editable

/**
 * **描述:** At处理器，用于处理接受到的AtTarget。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2020/11/26 12:59 PM
 *
 * **版本:** v 1.0.0
 */
class AtReceiveHandler(private val config: AtConfig, private val editable: Editable?, private val where: Int = -1) : AtReceiver {

    override fun receive(target: AtTarget) {
        if (editable != null) {
            if (where < 0) {
                editable.append(AtTargetSpan(config, target).spannedText)
            } else {
                editable.replace(where, where + 1, "")
                editable.insert(where, AtTargetSpan(config, target).spannedText)
            }
        }
    }

    override fun receive(targets: List<AtTarget>) {
        if (editable != null && targets.isNotEmpty()) {
            if (where < 0) {
                targets.forEach {
                    editable.append(AtTargetSpan(config, it).spannedText)
                }
            } else {
                editable.replace(where, where + 1, "")
                for (i in targets.lastIndex downTo 0) {
                    editable.insert(where, AtTargetSpan(config, targets[i]).spannedText)
                }
            }
        }
    }
}
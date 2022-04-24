package com.kelin.okat

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import java.lang.Exception

/**
 * **描述:** 绑定了AtTarget的Span
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2020/11/25 10:07 AM
 *
 * **版本:** v 1.0.0
 */
class AtTargetSpan<T : AtTarget>(private val config: AtConfig, private val target: T) : DataBindingSpan<T> {

    companion object {
        private const val separator = "<}{>"
        fun parse(config: AtConfig, res: String): CharSequence {
            return res.split(separator).let {
                try {
                    AtTargetSpan(config, DefAtTarget(it[1], it[0], it[2].toInt())).spannedText
                } catch (e: Exception) {
                    throw IllegalArgumentException(e)
                }
            }
        }
    }

    private val spanned by lazy {
        SpannableString("${config.prefix.display}${target.text}${config.suffix.display}").apply {
            setSpan(
                ForegroundColorSpan(config.atColor),
                0,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (config.isTargetClickable) {
                setSpan(RichClickableSpan(target), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    override val spannedText: CharSequence
        get() = SpannableString.valueOf(spanned)
            .apply { setSpan(this@AtTargetSpan, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

    override val bindingData: T
        get() = target

    override fun getRealSuffix(suffix: String): String {
        return "${separator}${target.id}${separator}${target.type}${suffix}"
    }

    private class DefAtTarget(override val id: String, override val text: String, override val type: Int) : AtTarget

    private inner class RichClickableSpan(private val clickable: AtTarget) : ClickableSpan() {
        override fun onClick(widget: View) {
            config.onTargetClickListener?.invoke(clickable)
        }

        override fun updateDrawState(ds: TextPaint) {
        }
    }
}
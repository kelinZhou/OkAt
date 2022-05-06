package com.kelin.okat

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import com.kelin.okat.callback.OkAtTextWatcher
import com.kelin.okat.callback.OkAtHandler
import com.kelin.okat.callback.OkAtHandlerDelegate
import com.kelin.okat.receiver.AtReceiverProvider
import com.kelin.okat.receiver.AtReceiverProviderWrapper

/**
 * **描述:** 输入框中的@支持帮助者
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2020/11/24 7:12 PM
 *
 * **版本:** v 1.0.0
 */
object OkAt {
    /**
     * 添加一个At。
     */
    const val REASON_ADD = 0x01

    /**
     * 移除一个At。
     */
    const val REASON_REMOVED = 0x02

    private val atMapperPool = HashMap<Int, AtConfig>()

    private val atReceiverProviderPool = HashMap<Int, AtReceiverProvider>()

    @Throws(NullPointerException::class)
    fun getPrefixAtMapper(targetView: View): AtMapper {
        return atMapperPool[targetView.hashCode()]?.prefix ?: throw NullPointerException("The AtMapper not found!")
    }

    @Throws(NullPointerException::class)
    fun getSuffixAtMapper(targetView: View): AtMapper {
        return atMapperPool[targetView.hashCode()]?.suffix ?: throw NullPointerException("The AtMapper not found!")
    }

    internal fun getAtConfig(targetView: View): AtConfig? {
        return atMapperPool[targetView.hashCode()]
    }

    /**
     * 为一个EditText设置支持OkAt。
     * @param targetView 要支持At的目标View。
     * @param prefix At的前缀映射关系。
     * @param suffix At的后缀映射关系。
     */
    @Throws(IllegalArgumentException::class)
    fun <T : AtTarget> attach(
        targetView: EditText,
        prefix: AtMapper = AtMapper("@", "#﹫#"),
        suffix: AtMapper = AtMapperNull(),
        @ColorInt atColor: Int = ContextCompat.getColor(targetView.context, R.color.colorAccent)
    ): OkAtHandler<T> {
        return attach(targetView, AtConfig(atColor, prefix, suffix))
    }

    fun <T : AtTarget> attach(targetView: EditText, config: AtConfig): OkAtHandler<T> {
        atMapperPool[targetView.hashCode()] = config
        if (config.isTargetClickable) {
            targetView.run {
                movementMethod = LinkMovementMethod()
                highlightColor = Color.TRANSPARENT
            }
        }
        val callback = OkAtHandlerDelegate<T>()
        targetView.setEditableFactory(NoCopySpanEditableFactory(SelectionSpanWatcher(DataBindingSpan::class, callback)))
        targetView.setOnKeyListener { v, keyCode, event ->
            if (v is EditText) {
                KeyCodeDeleteHelper.onDelDown(v.text, keyCode, event)
            } else {
                false
            }
        }
        targetView.addTextChangedListener(OkAtTextWatcher(config, callback).also {
            atReceiverProviderPool[targetView.hashCode()] = AtReceiverProviderWrapper(targetView, config, it)
        })
        return callback
    }

    /**
     * 向目标组件中添加一个At。
     * @param targetView 目标组件。
     * @param target 要添加的Target。
     */
    fun addAtTarget(targetView: EditText, target: AtTarget) {
        atReceiverProviderPool[targetView.hashCode()]?.receiver?.receive(target)
    }

    /**
     * 为一个支持OkAt的View解除关联，释放资源。
     */
    fun detach(targetView: View) {
        atMapperPool.remove(targetView.hashCode())
        atReceiverProviderPool.remove(targetView.hashCode())
    }

    fun getRealText(targetView: EditText): String? {
        return getAtConfig(targetView)?.let { config ->
            targetView.text?.let { text ->
                when {
                    text.isEmpty() -> null
                    text.contains(config.prefix.display) -> {
                        val result = StringBuilder(text.toString())
                        //先获取所有的Span然后降序遍历进行文本替换，注意：一定必须要降序遍历，否则当显示前后缀与真实前后缀字符长度不一致是将会导致Bug。
                        text.getSpans(0, text.length, DataBindingSpan::class.java)?.reversed()?.forEach { span ->
                            val start = text.getSpanStart(span)
                            val end = text.getSpanEnd(span)
                            result.replace(
                                start,
                                end,
                                text.substring(start, end).let { target ->
                                    if (config.theSameBothEnds) {
                                        StringBuilder(target).apply {
                                            replace(0, config.prefix.display.length, config.prefix.real)
                                            replace(length - config.suffix.display.length, length, span.getRealSuffix(config.suffix.real))
                                        }.toString()
                                    } else {
                                        target.replace(config.prefix.display, config.prefix.real).replace(config.suffix.display, span.getRealSuffix(config.suffix.real))
                                    }
                                }
                            )
                        }
                        result.toString()
                    }
                    else -> {
                        text.toString()
                    }
                }
            }
        }
    }

    /**
     * 获取目标View上所有的AtTarget。
     */
    fun getAtTargets(targetView: TextView): List<AtTarget> {
        return (targetView.text as? Spanned)?.let {
            it.getSpans(0, it.length, AtTargetSpan::class.java).map { span -> span.bindingData }
        } ?: emptyList()
    }

    /**
     * 为一个TextView设置支持OkAt。
     * @param targetView 要支持At的目标View。
     * @param prefix At的前缀映射关系。
     * @param suffix At的后缀映射关系。
     * @param targetClickListener 设置AtTarget是否可以被点击，如果希望可以被点击则设置点击事件，否者设置为null。
     */
    fun setText(
        targetView: TextView,
        text: String?,
        prefix: AtMapper = AtMapper("@", "#﹫#"),
        suffix: AtMapper = AtMapperNull(),
        @ColorInt atColor: Int = ContextCompat.getColor(targetView.context, R.color.colorAccent),
        targetClickListener: ((target: AtTarget) -> Unit)? = null
    ) {
        setText(targetView, text, AtConfig(atColor, prefix, suffix).apply { onTargetClickListener = targetClickListener })
    }

    fun setText(targetView: TextView, text: String?, config: AtConfig) {
        if (config.isTargetClickable) {
            targetView.run {
                movementMethod = LinkMovementMethod()
                highlightColor = Color.TRANSPARENT
            }
        }
        if (!text.isNullOrEmpty()) {
            Regex("""${config.prefix.real}.*?${config.suffix.real}""").findAll(text).let {
                val result = SpannableStringBuilder(text)
                it.forEach { r ->
                    val index = result.indexOf(r.value)
                    result.replace(index, index + r.value.length, AtTargetSpan.parse(config, r.value.replace(config.prefix.real, "").replace(config.suffix.real, "")))
                }
                targetView.text = result.toSpannable()
            }
        }
    }
}
package com.kelin.okat.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.Spannable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.kelin.okat.AtMapperNull
import com.kelin.okat.OkAt

/**
 * **描述:** 不能粘贴富文本的EditTextView。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2020/11/27 11:17 AM
 *
 * **版本:** v 1.0.0
 */
class NoSpanCanPasteEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle) : AppCompatEditText(context, attrs, defStyleAttr) {
    override fun onTextContextMenuItem(id: Int): Boolean {
        val mapper = OkAt.getAtConfig(this)
        if (id == android.R.id.paste && mapper != null) {
            //调用剪贴板
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.also {
                val str = it.primaryClip?.getItemAt(0)?.text
                val containsAt = str?.contains(mapper.prefix.real) == true //是否包含用于识别At的特殊字符
                if (str is Spannable || containsAt) {
                    it.setPrimaryClip(ClipData.newPlainText(null, str?.let { newStr ->
                        newStr.toString().let { s ->
                            if (containsAt) {
                                // 如果包含用于At的特殊字符则进行替换。
                                s.replace(mapper.prefix.real, mapper.prefix.display).let { replaced ->
                                    if (mapper.suffix is AtMapperNull) {
                                        replaced
                                    } else {
                                        replaced.replace(mapper.suffix.real, mapper.suffix.display)
                                    }
                                }
                            } else {
                                s
                            }
                        }
                    }))
                }
            }
        }
        return super.onTextContextMenuItem(id)
    }
}
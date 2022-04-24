package com.kelin.okatdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kelin.okat.AtMapper
import com.kelin.okat.AtTarget
import com.kelin.okat.AtReceiver
import com.kelin.okat.OkAt
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val random by lazy { Random(System.currentTimeMillis()) }

    private val names by lazy {
        arrayOf("王小丫", "李二狗", "韩梅梅", "李磊", "王华")
    }

    private val testTargets by lazy {
        ArrayList<TestTarget>().apply {
            for (i in 16..20) {
                add(TestTarget(i, names[i - 16]))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OkAt.attach<TestTarget>(etContent, AtMapper("#", "<#>"), AtMapper("#", "<#>")).run {
            doAfterInputAt {
                onSelectContactsToAt(it)
            }
            doAfterAtChanged { _, targets ->
                tvTargets.text = "您选择了：${if (targets.isEmpty()) "" else targets.joinToString("、") { "${it.text}(${it.age})" }}"
            }
        }
        btnGetReal.setOnClickListener {
            tvReal.text = OkAt.getRealText(etContent)?.also {
                OkAt.setText(tvDisplay, it, AtMapper("#", "<#>"), AtMapper("#", "<#>")){ target ->
                    Toast.makeText(this, "您点击了${target.text}", Toast.LENGTH_SHORT).show()
                }
            }
            OkAt.getAtTargets(tvDisplay).forEach {
                Log.d("==========", it.text)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        OkAt.detach(etContent)
    }

    private fun onSelectContactsToAt(receiver: AtReceiver) {
        receiver.receive(testTargets[random.nextInt(testTargets.size)])
    }
}

data class TestTarget(val age: Int, val name: String) : AtTarget {
    override val id: String
        get() = age.toString()

    override val text: String
        get() = name
}
package com.haoge.easyandroid.easy

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.haoge.easyandroid.EasyAndroid

/**
 * 一个简单易用的Toast封装类。用于提供易用的、多样式的Toast组件进行使用
 *
 * DATE: 2018/5/9
 *
 * AUTHOR: haoge
 */
class EasyToast private constructor(private val layoutId: Int = -1,
                                    private val tvId: Int = -1,
                                    private val duration: Int = Toast.LENGTH_SHORT,
                                    private val isDefault: Boolean = true) {

    var toast:Toast? = null
    var tv:TextView? = null

    fun show(resId:Int) {
        show(EasyAndroid.getApplicationContext().getString(resId))
    }

    fun show(message:String?, vararg any: Any) {
        if (TextUtils.isEmpty(message)) {
            return
        }

        var result = message as String
        if (any.isNotEmpty()) {
            result = String.format(message, any)
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            showInternal(result)
        } else {
            mainHandler.post { showInternal(result) }
        }
    }

    private fun showInternal(message: String) {
        createToastIfNeeded()

        if (isDefault) {
            toast?.setText(message)
            toast?.show()
        } else {
            tv?.text = message
            toast?.show()
        }
    }

    @SuppressLint("ShowToast")
    private fun createToastIfNeeded() {
        if (toast == null) {
            if (isDefault) {
                toast = Toast.makeText(EasyAndroid.getApplicationContext(), "", Toast.LENGTH_SHORT)
            } else {
                val container = LayoutInflater.from(EasyAndroid.getApplicationContext()).inflate(layoutId, null)
                tv = container.findViewById(tvId)
                toast = Toast(EasyAndroid.getApplicationContext())
                toast?.view = container
                toast?.duration = duration
            }
        }
    }

    companion object {

        internal val mainHandler by lazy { return@lazy Handler(Looper.getMainLooper()) }
        /**
         * 默认提供的Toast实例，在首次使用时进行加载。
         */
        val DEFAULT: EasyToast by lazy { default() }

        private fun default(): EasyToast {
            return EasyToast(isDefault = true)
        }

        fun create(layoutId: Int, tvId: Int, duration: Int): EasyToast {
            return EasyToast(layoutId, tvId, duration, false)
        }
    }
}
package com.letion.testa

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CC.sendCCResult
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.IComponent
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.app.Activity


/**
 * Created by liufeng on 2018/4/21.
 * @email w710989327@foxmail.com
 */
class TestAComponent : IComponent {
    /**
     * @return true:组件异步实现 false:组件同步实现
     */
    override fun onCall(cc: CC?): Boolean {
        val context = cc!!.context
        val intent = Intent(context, TestAActivity::class.java)
        if (context !is Activity) {
            //调用方没有设置context或app间组件跳转，context为application
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        sendCCResult(cc.callId, CCResult.success())
        return false
    }

    override fun getName(): String {
        return "ComponentA"
    }
}
package com.letion.testa

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.billy.cc.core.component.CC
import kotlinx.android.synthetic.main.testa_activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testa_activity_main)
        testa_call_test.setOnClickListener({
            startActivity(Intent(this, TestAActivity::class.java))
        })
    }
}

package com.letion.testa

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.test_kt_activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_kt_activity_main)
        testa_call_test.setOnClickListener({
            startActivity(Intent(this, TestAActivity::class.java))
        })
    }
}

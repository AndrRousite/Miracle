package com.letion.testa

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.testa_activity_test_a.*

class TestAActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testa_activity_test_a)
        texta_test_a.text = "Test A"
    }
}

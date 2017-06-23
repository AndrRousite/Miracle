package com.letion.miracle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
//    WaveViewByBezier waveViewByBezier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        waveViewByBezier = (WaveViewByBezier)findViewById(R.id.waveView);
//        waveViewByBezier.startAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        waveViewByBezier.pauseAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        waveViewByBezier.resumeAnimation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        waveViewByBezier.stopAnimation();
    }
}

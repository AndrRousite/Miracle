package com.letion.miracle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.letion.uikit.stateview.TEmptyLayout;

public class Main2Activity extends AppCompatActivity {
    //    WaveViewByBezier waveViewByBezier;
    TEmptyLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        waveViewByBezier = (WaveViewByBezier)findViewById(R.id.waveView);
//        waveViewByBezier.startAnimation();
        emptyLayout = TEmptyLayout.wrap(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loading, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_empty:
                emptyLayout.showEmpty();
                return true;
            case R.id.action_loading:
                emptyLayout.showLoading();
                return true;
            case R.id.action_content:
                emptyLayout.showContent();
                return true;
            case R.id.action_error:
                emptyLayout.showError();
                return true;
        }
        return false;
    }
}

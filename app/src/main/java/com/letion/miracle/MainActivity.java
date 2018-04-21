package com.letion.miracle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;

/**
 * @author wuqi
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.textA).setOnClickListener(v -> {
            CC.obtainBuilder("ComponentA").build().callAsyncCallbackOnMainThread((cc, result) -> {
                System.out.println(result);
            });

        });
    }
}

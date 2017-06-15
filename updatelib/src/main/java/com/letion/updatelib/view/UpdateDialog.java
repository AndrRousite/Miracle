package com.letion.updatelib.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.letion.updatelib.R;
import com.letion.updatelib.config.DownloadConfig;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class UpdateDialog extends Activity {
    private TextView yes, no;
    private TextView tv_version, tv_changelog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);

        yes = (TextView) findViewById(R.id.updatedialog_yes);
        no = (TextView) findViewById(R.id.updatedialog_no);
        tv_version = (TextView) findViewById(R.id.title);
        tv_changelog = (TextView) findViewById(R.id.updatedialog_text_changelog);
        tv_version.setText("发现新版本: V" + DownloadConfig.versionName);
        tv_changelog.setText("更新日志：\n" + DownloadConfig.changeLog);

        yes.setOnClickListener(arg0 -> {
            DownloadConfig.TOShowDownloadView = 2;
            finish();
        });
        no.setOnClickListener(arg0 -> {
            DownloadConfig.TOShowDownloadView = 1;
            if (DownloadConfig.ISManual) {
                DownloadConfig.LoadManual = false;
            }
            finish();
        });
    }
}

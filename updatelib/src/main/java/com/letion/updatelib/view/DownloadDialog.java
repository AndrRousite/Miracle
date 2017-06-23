package com.letion.updatelib.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.letion.updatelib.R;
import com.letion.updatelib.config.DownloadConfig;
import com.letion.updatelib.module.DownloadThread;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class DownloadDialog extends Activity {
    private ImageView closeImage;
    public ProgressBar progressBar;
    public TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download);
        closeImage = (ImageView) findViewById(R.id.downloaddialog_close);
        progressBar = (ProgressBar) findViewById(R.id.downloaddialog_progress);
        textView = (TextView) findViewById(R.id.downloaddialog_count);

        if (DownloadConfig.interceptFlag) DownloadConfig.interceptFlag = false;
        new DownloadThread(this).start();

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadConfig.TOShowDownloadView = 1;
                DownloadConfig.interceptFlag = true;
                if (DownloadConfig.ISManual) {
                    DownloadConfig.LoadManual = false;
                }
                finish();
            }
        });
    }
}

package com.letion.uikit.xrecycler;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.letion.uikit.R;
import com.letion.uikit.stateview.TLoadingView;

/**
 * Created by liu-feng on 2017/5/27.
 */
public class ClassicsRefreshFooterView extends LinearLayout implements IRefreshFooterView {

    private TextView mText;
    private TLoadingView mIvProgress;

    public ClassicsRefreshFooterView(Context context) {
        this(context, null);
    }

    public ClassicsRefreshFooterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicsRefreshFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_classics_footer, this);
        mText = (TextView) findViewById(R.id.msg);
        mIvProgress = (TLoadingView) findViewById(R.id.iv_progress);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mIvProgress.setVisibility(View.VISIBLE);
                mText.setText(getContext().getText(R.string.footer_type_loading));
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(getContext().getText(R.string.footer_type_loading));
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(getContext().getText(R.string.footer_type_not_more));
                mIvProgress.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR:
                mText.setText(getContext().getText(R.string.footer_type_error));
                mIvProgress.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void reset() {
        this.setVisibility(GONE);
    }
}

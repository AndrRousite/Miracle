package com.letion.miracle.view;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.letion.miracle.R;
import com.letion.uikit.xrecycler.IRefreshFooterView;

/**
 * Created by limxing on 16/7/23.
 * <p>
 * https://github.com/limxing
 * Blog: http://www.leefeng.me
 */
public class TRefreshFooterView extends LinearLayout implements IRefreshFooterView {
    private TextView mText;
    private TLoadView mIvProgress;

    public TRefreshFooterView(Context context) {
        this(context, null);
    }

    public TRefreshFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TRefreshFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout
                .layout_t_refresh_footer, this);
        mText = (TextView) findViewById(R.id.msg);
        mIvProgress = (TLoadView) moreView.findViewById(R.id.iv_progress);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT));
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

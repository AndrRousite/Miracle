package com.letion.uikit.xrecycler;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.letion.uikit.R;

/**
 * Created by liu-feng on 2017/5/27.
 */
public class YunRefreshHeaderView extends LinearLayout implements IRefreshHeaderView {
    private AnimationDrawable animationDrawable;
    private TextView msg;
    private int mState = STATE_NORMAL;
    private int mMeasuredHeight;
    private LinearLayout mContainer;

    public YunRefreshHeaderView(Context context) {
        this(context,null);
    }

    public YunRefreshHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YunRefreshHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_refresh_header, this);
        ImageView img = (ImageView) findViewById(R.id.img);

        animationDrawable = (AnimationDrawable) img.getDrawable();
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        msg = (TextView) findViewById(R.id.msg);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
        setGravity(Gravity.CENTER_HORIZONTAL);
        mContainer = (LinearLayout) findViewById(R.id.container);
        mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void move(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean release() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        if (getVisibleHeight() > mMeasuredHeight && mState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <= mMeasuredHeight) {
            //return;
        }
        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    @Override
    public void complete() {
        setState(STATE_DONE);
        new Handler().postDelayed(() -> reset(), 500);
    }

    public void reset() {
        smoothScrollTo(0);
        setState(STATE_NORMAL);
    }

    @Override
    public int getState() {
        return mState;
    }

    @Override
    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

    private void setVisibleHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    private void setState(int state) {
        if (state == mState) return;
        switch (state) {
            case STATE_NORMAL:
                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                msg.setText(R.string.header_type_normal);
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    if (!animationDrawable.isRunning()) {
                        animationDrawable.start();
                    }
                    msg.setText(R.string.header_type_release);
                }
                break;
            case STATE_REFRESHING:
                msg.setText(R.string.header_type_refreshing);
                break;
            case STATE_DONE:
                msg.setText(R.string.header_type_complete);
                break;
            default:
        }
        mState = state;
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(animation -> setVisibleHeight((int) animation.getAnimatedValue()));
        animator.start();
    }
}

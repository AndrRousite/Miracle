package com.letion.uikit.smartisan;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.letion.uikit.R;

/**
 * Created by liu-feng on 2017/6/7.
 */
public class LetionRefreshLayout extends LinearLayout {
    private static final float ZERO_FOR_COMPARE = 0.005f;

    //所有子View
    private RelativeLayout mHeaderLayout;
    private SmartisanCircleView mCircleView;
    private TextView mDescriptionTextView;
    private AdapterView<?> mAdapterView;  // list or grid
    private RecyclerView mRecyclerView;
    private ScrollView mScrollView;
    private WebView mWebView;

    //布局相关
    private boolean mIsLayoutLoaded;    //是否已加载过一次layout
    private int mHeaderHeight;
    private MarginLayoutParams mHeaderMargin;   //下拉头的布局参数
    private ValueAnimator mCircleAnimator;
    private ValueAnimator mPulledAnimator;

    //事件相关
    private int mCurrentStatus;
    private boolean mInterceptBoolean = false;      //当前事件是否拦截
    private float mLastMoveY;
    private float mPulledDistance;   //HeadView被下拉的距离，不是手指移动距离
    private float mAnimatorDistance;
    private float mRubRatio = 1.0f;     //摩擦系数
    private float mListDividerHeight;

    private PullToRefreshListener mListener;


    public LetionRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL); // 设置该布局容器为垂直方向

        mHeaderLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_smartisan_header, null, true);
        mCircleView = (SmartisanCircleView) mHeaderLayout.findViewById(R.id.smartisanView);
        mDescriptionTextView = (TextView) mHeaderLayout.findViewById(R.id.descriptionTextView);

        addView(mHeaderLayout, 0);


        mPulledDistance = 0;
        mAnimatorDistance = 0;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 确定布局容器中内部子视图类型
        initSubviewType();
    }

    private void initSubviewType() {
        int count = getChildCount();
        if (count < 2) return;  //xml 中没有子视图
        View childAt = getChildAt(1);
        if (childAt instanceof AdapterView<?>) {
            mAdapterView = (AdapterView<?>) childAt;
        } else if (childAt instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) childAt;
        } else if (childAt instanceof ScrollView) {
            mScrollView = (ScrollView) childAt;
        } else if (childAt instanceof WebView) {
            mWebView = (WebView) childAt;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !mIsLayoutLoaded) {
            mHeaderMargin = (MarginLayoutParams) mHeaderLayout.getLayoutParams();
            mHeaderHeight = mHeaderLayout.getHeight();
            mHeaderMargin.topMargin = -mHeaderHeight / 2;
            mHeaderMargin.bottomMargin = mHeaderMargin.topMargin;
            mHeaderLayout.setLayoutParams(mHeaderMargin);
//            this.setBackgroundColor(Color.argb(255, 255, 255, 255));       //把背景统一设置为白色

            mCurrentStatus = Status.STATUS_ORIGIN;
            updateLayoutAndText();

            mIsLayoutLoaded = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mInterceptBoolean = false;      //避免不下拉，只是往下翻列表。因为此时不知道手势往上还是往下。
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                View firstChildView = null;  //用于判断子布局容器是否没有元素
                if (mAdapterView != null) {
                    firstChildView = mAdapterView.getChildAt(0);
                } else if (mRecyclerView != null) {
                    firstChildView = mRecyclerView.getChildAt(0);
                } else if (mScrollView != null) {
                    firstChildView = mScrollView.getChildAt(0);
                } else if (mWebView != null) {
                    firstChildView = mWebView.getChildAt(0);
                }
                if (firstChildView == null || (firstChildView != null && firstChildView.getTop() == 0)) {  //万事俱备，只欠下拉。
                    float currentRawY = event.getRawY();
                    float tmpMoveY = currentRawY - mLastMoveY;
                    if ((isEqualZero(mPulledDistance) && tmpMoveY > 0) || (mPulledDistance > 0 && mCurrentStatus != Status.STATUS_REFRESHING)) {
                        //两种情况：第一次下拉、已经下拉过了。
                        // 如果首个元素的上边缘，距离父布局值为0，就说明ListView滚动到了最顶部，此时应该允许下拉刷新

                        Log.d("是否拦截", ":可下拉");
                        if (!mInterceptBoolean) {
                            mLastMoveY = event.getRawY();
                        }
                        mInterceptBoolean = true;
                    } else if (mCurrentStatus == Status.STATUS_REFRESHING && tmpMoveY > 0) {
                        mInterceptBoolean = false;
                    } else if (isEqualZero(mPulledDistance) && tmpMoveY < 0) {

                    } else {
                        mInterceptBoolean = false;
                    }
                } else {
                    mInterceptBoolean = false;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                mInterceptBoolean = false;
                break;
            }
            default:
                break;
        }

        mLastMoveY = y;
        Log.d("是否拦截", ":" + mInterceptBoolean);

        return mInterceptBoolean;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float currentRawY = event.getRawY();
                float tmpMoveY = currentRawY - mLastMoveY;

                if (mPulledDistance < 0 || isEqualZero(mPulledDistance)) {
                    mPulledDistance += tmpMoveY;
                } else {
                    if (mPulledDistance <= mHeaderHeight) {
                        mCurrentStatus = Status.STATUS_DISTANCE_UNFINISHED;
                        mRubRatio = 1.0f;
                    } else {
                        mCurrentStatus = Status.STATUS_DISTANCE_FINISHED;
                        mRubRatio = (2 - mPulledDistance / (float) mHeaderHeight) / 4;
                    }
                    mPulledDistance += mRubRatio * tmpMoveY;
                    mAnimatorDistance = mPulledDistance;
                    updateAllView(mCurrentStatus, mPulledDistance, mAnimatorDistance);
                    mLastMoveY = currentRawY;
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                Log.d("事件", "Action_up");
                if (mCurrentStatus == Status.STATUS_DISTANCE_FINISHED) {
                    mCurrentStatus = Status.STATUS_REFRESH_PREPARE;
                    updateAllView(mCurrentStatus, mPulledDistance, mAnimatorDistance);
                    if (mListener != null)
                        mListener.onRefresh();
                } else if (mCurrentStatus == Status.STATUS_DISTANCE_UNFINISHED) {
                    mCurrentStatus = Status.STATUS_DISTANCE_UNFINISHED_BACK;
                    updateAllView(mCurrentStatus, mPulledDistance, mAnimatorDistance);
                }
                break;
        }
        return true;
    }

    private void updateLayoutAndText() {
        float mockPulledDistance;
        if (mAdapterView == null && mRecyclerView == null && mScrollView == null && mWebView == null)
            return;
        if (mPulledDistance <= 0) {
            if (mAdapterView != null) {
                mAdapterView.scrollTo(0, (int) mPulledDistance);
            } else if (mRecyclerView != null) {
                mRecyclerView.scrollTo(0, (int) mPulledDistance);
            } else if (mScrollView != null) {
                mScrollView.scrollTo(0, (int) mPulledDistance);
            } else if (mWebView != null) {
                mWebView.scrollTo(0, (int) mPulledDistance);
            }
            mockPulledDistance = 0;
        } else {
            if (mAdapterView != null) {
                mAdapterView.scrollTo(0, 0);
            } else if (mRecyclerView != null) {
                mRecyclerView.scrollTo(0, 0);
            } else if (mScrollView != null) {
                mScrollView.scrollTo(0, 0);
            } else if (mWebView != null) {
                mWebView.scrollTo(0, 0);
            }
            mockPulledDistance = mPulledDistance - 0;
        }

        mHeaderMargin.topMargin = (int) (-mHeaderHeight / 2 + mockPulledDistance / 2);
        mHeaderMargin.bottomMargin = mHeaderMargin.topMargin;
        mHeaderLayout.setLayoutParams(mHeaderMargin);


        switch (mCurrentStatus) {

            case Status.STATUS_ORIGIN:
                mDescriptionTextView.setTextColor(Color.argb(0, 120, 120, 120));
                break;
            case Status.STATUS_DISTANCE_UNFINISHED:
            case Status.STATUS_DISTANCE_UNFINISHED_BACK:
                mDescriptionTextView.setText("下拉即可刷新...");
                if (mockPulledDistance < mHeaderHeight / 4) {
                    mDescriptionTextView.setTextColor(Color.argb(0, 120, 120, 120));
                } else {
                    mDescriptionTextView.setTextColor(Color.argb((int) ((mockPulledDistance - (float) mHeaderHeight / 4) / (mHeaderHeight * 3 / 4) * 255), 120, 120, 120));
                }
                break;
            case Status.STATUS_DISTANCE_FINISHED:
                mDescriptionTextView.setText("松开即可刷新...");
                mDescriptionTextView.setTextColor(Color.argb(255, 120, 120, 120));
                break;
            case Status.STATUS_REFRESH_PREPARE:
            case Status.STATUS_REFRESHING:
                mDescriptionTextView.setText("正在刷新列表...");
                mDescriptionTextView.setTextColor(Color.argb(255, 120, 120, 120));
                break;
            case Status.STATUS_REFRESH_FINISHED_CIRCLE:
//            case Status.STATUS_REFRESH_FINISHED_HIDE:
                mDescriptionTextView.setText("正在刷新列表...");
                if (mockPulledDistance < mHeaderHeight / 4) {
                    mDescriptionTextView.setTextColor(Color.argb(0, 120, 120, 120));
                } else {
                    mDescriptionTextView.setTextColor(Color.argb((int) ((mockPulledDistance - (float) mHeaderHeight / 4) / (mHeaderHeight * 3 / 4) * 255), 120, 120, 120));
                }
                break;
            default:
                break;
        }
    }

    public void updateAllView(int status, float pulledDistance, final float animatorDistance) {
        Log.d("更新视图", status + "," + pulledDistance);
        mCurrentStatus = status;
        mPulledDistance = pulledDistance;
        mAnimatorDistance = animatorDistance;
        mCircleView.setStatusAndAnimatorDistance(status, animatorDistance);

        updateLayoutAndText();

        switch (mCurrentStatus) {
            case Status.STATUS_ORIGIN:
                break;
            case Status.STATUS_DISTANCE_UNFINISHED:
                mCircleView.setStatusAndAnimatorDistance(Status.STATUS_DISTANCE_UNFINISHED, mAnimatorDistance);
                mCircleView.invalidate();
                break;
            case Status.STATUS_DISTANCE_UNFINISHED_BACK:
                resetCircleAnimator(mAnimatorDistance, 0, 300, 0, new UpdateHeaderViewCallback() {
                    @Override
                    public void onAnimationUpdate(float animatorValue) {
                        Log.d("属性动画过程值：", "是" + animatorValue);
                        mPulledDistance = animatorValue;
                        mAnimatorDistance = animatorValue;
                        mCircleView.setStatusAndAnimatorDistance(Status.STATUS_DISTANCE_UNFINISHED_BACK, mAnimatorDistance);
                        mCircleView.invalidate();
                        updateLayoutAndText();
                    }

                    @Override
                    public void onAnimationEnd() {
                        mCurrentStatus = Status.STATUS_ORIGIN;
                        mPulledDistance = 0;
                        mAnimatorDistance = mPulledDistance;
                        mCircleView.setStatusAndAnimatorDistance(Status.STATUS_ORIGIN, 0);
                    }
                });
                break;
            case Status.STATUS_DISTANCE_FINISHED:
                mCircleView.invalidate();
                break;
            case Status.STATUS_REFRESH_PREPARE:
                resetPullAnimator(mPulledDistance, mHeaderHeight, 200, 0, new UpdateHeaderViewCallback() {
                    @Override
                    public void onAnimationUpdate(float animatorValue) {
                        mPulledDistance = animatorValue;
                        updateLayoutAndText();

                    }

                    @Override
                    public void onAnimationEnd() {
                        mCurrentStatus = Status.STATUS_REFRESHING;
                        updateAllView(Status.STATUS_REFRESHING, mCircleView.getHeight(), mAnimatorDistance);
                    }
                });
                resetCircleAnimator(mPulledDistance, mPulledDistance + (float) (Math.PI * mCircleView.mCircleRadius * 2), 350, -1, new UpdateHeaderViewCallback() {
                    @Override
                    public void onAnimationUpdate(float animatorValue) {
                        mAnimatorDistance = animatorValue;
                        mCircleView.setStatusAndAnimatorDistance(Status.STATUS_REFRESHING, mAnimatorDistance);
                        mCircleView.invalidate();
                    }

                    @Override
                    public void onAnimationEnd() {
                    }
                });
                break;
            case Status.STATUS_REFRESHING:
                break;
            case Status.STATUS_REFRESH_FINISHED_CIRCLE:
                float doubleCircleCount = (mAnimatorDistance - mHeaderHeight / 2) / ((float) Math.PI * mCircleView.mCircleRadius * 2);
                float circleCount = doubleCircleCount / 2;
                float smallCircle = circleCount % 1;
                Log.d("刷新后", "smallCircle:" + smallCircle);
                if (smallCircle > 0.5) {
                    smallCircle -= 0.5;
                }
                float nonCircleCount = 0.5f - smallCircle;
                float toDoCircleDistance = nonCircleCount * ((float) Math.PI * mCircleView.mCircleRadius * 2);
                final float finalTotalDistance = (float) Math.PI * mCircleView.mCircleRadius + mCircleView.mLineLength + mHeaderHeight / 4;
                final float toDoTotalDistance = toDoCircleDistance + mCircleView.mLineLength + mHeaderHeight / 4;
                final float bili = mHeaderHeight / toDoTotalDistance;
                final float doneDistance = finalTotalDistance - toDoTotalDistance;
                mAnimatorDistance = doneDistance;

                resetCircleAnimator(mAnimatorDistance, finalTotalDistance, 200, 0, new UpdateHeaderViewCallback() {
                    @Override
                    public void onAnimationUpdate(float animatorValue) {
                        mAnimatorDistance = animatorValue;
                        mPulledDistance = (finalTotalDistance - animatorValue) * bili;
                        mCircleView.setStatusAndAnimatorDistance(Status.STATUS_REFRESH_FINISHED_CIRCLE, mAnimatorDistance);
                        mCircleView.invalidate();
                        updateLayoutAndText();
                    }

                    @Override
                    public void onAnimationEnd() {
                        mCurrentStatus = Status.STATUS_ORIGIN;
                        mPulledDistance = 0;
                        mAnimatorDistance = mPulledDistance;
                        mCircleView.setStatusAndAnimatorDistance(Status.STATUS_ORIGIN, 0);
                    }
                });
                break;
            default:
                break;
        }

    }

    private void resetCircleAnimator(float startValue, float endValue, int duration, int repeatCount, final UpdateHeaderViewCallback updateHeaderViewCallback) {
        if (mCircleAnimator != null && mCircleAnimator.isRunning()) {
            mCircleAnimator.cancel();
        }
        mCircleAnimator = ValueAnimator.ofFloat(startValue, endValue).setDuration(duration);
        mCircleAnimator.setInterpolator(new LinearInterpolator());
        mCircleAnimator.setRepeatCount(repeatCount);
        mCircleAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            updateHeaderViewCallback.onAnimationUpdate(animatedValue);
        });
        mCircleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateHeaderViewCallback.onAnimationEnd();
                animation.cancel();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mCircleAnimator.start();
    }

    private void resetPullAnimator(float startValue, float endValue, int duration, int repeatCount, final UpdateHeaderViewCallback updateHeaderViewCallback) {
        if (mPulledAnimator != null && mPulledAnimator.isRunning()) {
            mPulledAnimator.cancel();
        }
        mPulledAnimator = ValueAnimator.ofFloat(startValue, endValue).setDuration(duration);
        mPulledAnimator.setInterpolator(new LinearInterpolator());
        mPulledAnimator.setRepeatCount(repeatCount);
        mPulledAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            updateHeaderViewCallback.onAnimationUpdate(animatedValue);
        });
        mPulledAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateHeaderViewCallback.onAnimationEnd();
                animation.cancel();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mPulledAnimator.start();
    }

    public void finishRefreshing() {
        mCurrentStatus = Status.STATUS_REFRESH_FINISHED_CIRCLE;
        updateAllView(mCurrentStatus, mHeaderHeight, mAnimatorDistance);
    }

    private interface UpdateHeaderViewCallback {
        void onAnimationUpdate(float animatorValue);

        void onAnimationEnd();
    }

    private boolean isEqualZero(float floatValue) {
        if (floatValue < ZERO_FOR_COMPARE && floatValue > -ZERO_FOR_COMPARE) {
            return true;
        } else {
            return false;
        }
    }

    public interface PullToRefreshListener {
        void onRefresh();
    }

    public void setOnRefreshListener(PullToRefreshListener listener) {
        mListener = listener;
    }
}

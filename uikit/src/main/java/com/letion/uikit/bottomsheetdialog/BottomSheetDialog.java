package com.letion.uikit.bottomsheetdialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

/**
 * 解决使用BottomSheetDialog时状态栏变黑的问题
 * Created by liu-feng on 2017/10/20.
 */
public class BottomSheetDialog extends android.support.design.widget.BottomSheetDialog {
    public BottomSheetDialog(@NonNull Context context) {
        super(context);
        BottomSheetDialogFragment dd;
    }

    public BottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);

    }

    protected BottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener
            cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Activity owner = getOwnerActivity();
        if (owner != null) {
            int screenHeight = getScreenHeight(owner);
            int statusBarHeight = getStatusBarHeight(getContext());
            int dialogHeight = screenHeight - statusBarHeight;
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ?
                    ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }
    }

    private static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}

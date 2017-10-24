package com.letion.geetionlib.util;

import android.Manifest;
import android.os.Build;

import com.letion.geetionlib.vender.rxerrorhandler.RxErrorHandler;
import com.letion.geetionlib.vender.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu-feng on 17/10/2016 10:09
 */
public class PermissionUtil {

    private PermissionUtil() {
    }

    public interface RequestPermission {
        void onRequestPermissionSuccess();

        void onRequestPermissionFailure();
    }


    public static void requestPermission(final RequestPermission requestPermission, RxPermissions
            rxPermissions, RxErrorHandler errorHandler, String... permissions) {
        if (permissions == null || permissions.length == 0) return;

        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) { //过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }
        if (needRequest.size() == 0) {//全部权限都已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过,则开始申请
            rxPermissions
                    .request(needRequest.toArray(new String[needRequest.size()]))
                    .subscribe(new ErrorHandleSubscriber<Boolean>(errorHandler) {
                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                requestPermission.onRequestPermissionFailure();
                            }
                        }
                    });
        }

    }


    /**
     * 请求摄像头权限
     */
    public static void launchCamera(RequestPermission requestPermission, RxPermissions
            rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }


    /**
     * 请求外部存储的权限
     */
    public static void externalStorage(RequestPermission requestPermission, RxPermissions
            rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission
                .WRITE_EXTERNAL_STORAGE);
    }


    /**
     * 请求发送短信权限
     */
    public static void sendSms(RequestPermission requestPermission, RxPermissions rxPermissions,
                               RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission
                .SEND_SMS);
    }


    /**
     * 请求打电话权限
     */
    public static void callPhone(RequestPermission requestPermission, RxPermissions
            rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission
                .CALL_PHONE);
    }


    /**
     * 请求获取手机状态的权限
     */
    public static void readPhonestate(RequestPermission requestPermission, RxPermissions
            rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission
                .READ_PHONE_STATE);
    }

    /**
     * support android o permission manager
     */
    public static final class Permission {

        static final String[] CALENDAR;   // 读写日历。
        static final String[] CAMERA;     // 相机。
        static final String[] CONTACTS;   // 读写联系人。
        static final String[] LOCATION;   // 读位置信息。
        static final String[] MICROPHONE; // 使用麦克风。
        static final String[] PHONE;      // 读电话状态、打电话、读写电话记录。
        static final String[] SENSORS;    // 传感器。
        static final String[] SMS;        // 读写短信、收发短信。
        static final String[] STORAGE;    // 读写存储卡。

        static {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                CALENDAR = new String[]{};
                CAMERA = new String[]{};
                CONTACTS = new String[]{};
                LOCATION = new String[]{};
                MICROPHONE = new String[]{};
                PHONE = new String[]{};
                SENSORS = new String[]{};
                SMS = new String[]{};
                STORAGE = new String[]{};
            } else {
                CALENDAR = new String[]{
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR};

                CAMERA = new String[]{
                        Manifest.permission.CAMERA};

                CONTACTS = new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS};

                LOCATION = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION};

                MICROPHONE = new String[]{
                        Manifest.permission.RECORD_AUDIO};

                PHONE = new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.USE_SIP,
                        Manifest.permission.PROCESS_OUTGOING_CALLS};

                SENSORS = new String[]{
                        Manifest.permission.BODY_SENSORS};

                SMS = new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_WAP_PUSH,
                        Manifest.permission.RECEIVE_MMS};

                STORAGE = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
            }
        }

    }

}


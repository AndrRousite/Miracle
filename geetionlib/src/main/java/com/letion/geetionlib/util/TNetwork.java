
package com.letion.geetionlib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 描述: 网络工具类
 * 
 * @author liu-feng
 * @since 2013-7-11 下午4:25:46
 */
public class TNetwork {
    private static final String UNKNOWN = "unknown";
    private static final String NET_2G = "2G";
    private static final String NET_3G = "3G";
    private static final String NET_4G = "4G";
    private static final String WIFI = "wifi";
    private static final String NET_CMNET = "cmnet";
    private static final String NET_CMWAP = "cmwap";

    private TNetwork() {
    }

    /**
     * 当前是否有可用网络
     * 
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        return !(UNKNOWN.endsWith(getNetworkType(context)));
    }

    /**
     * 获取当前的网络类型
     * 
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            return UNKNOWN;
        }
        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return WIFI;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int netType = tm.getNetworkType();
        return getNetworkClass(netType);
    }

    public static String getNetworkClass(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NET_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NET_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NET_4G;
            default:
                return UNKNOWN;
        }
    }

    /**
     * 是否cmnet
     * 
     * @param context
     * @return
     */
    public static boolean isCmnet(Context context) {
        String currentNetworkType = getNetworkType(context);

        if (NET_2G.equals(currentNetworkType)) {
            try {
                ConnectivityManager connectMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectMgr != null) {
                    NetworkInfo mobNetInfo = connectMgr
                            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mobNetInfo != null && mobNetInfo.isConnected()) {
                        if ("cmnet".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        return false;
    }

    /**
     * 是否cmwap
     *
     * @param context
     * @return
     */
    public static boolean isCmwap(Context context) {
        String currentNetworkType = getNetworkType(context);

        if (NET_2G.equals(currentNetworkType)) {
            try {
                ConnectivityManager connectMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectMgr != null) {
                    NetworkInfo mobNetInfo = connectMgr
                            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mobNetInfo != null && mobNetInfo.isConnected()) {
                        if ("cmwap".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        return false;
    }

    /**
     * 获取本机ip地址
     * 
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

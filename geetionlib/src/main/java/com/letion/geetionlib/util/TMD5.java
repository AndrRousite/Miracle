package com.letion.geetionlib.util;


import java.security.MessageDigest;

public class TMD5 {
    /**
     * 获取MD5 值
     */
    public static String getMD5(String content) {
        try {
            content = new String(content.getBytes(), "utf-8");
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过指定的byte数组对摘要进行最后的更新，然后完成摘要计算
     *
     * @param digest
     * @return
     */
    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }
}

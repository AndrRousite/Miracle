package com.letion.geetionlib.base.integration;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu-feng on 2017/6/5.
 */
public final class ManifestParser {
    private static final String MODULE_VALUE = "ConfigModule";

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    public List<IConfigManager> parse() {
        List<IConfigManager> modules = new ArrayList<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (MODULE_VALUE.equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse IConfigManager", e);
        }

        return modules;
    }

    private static IConfigManager parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find IConfigManager implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate IConfigManager implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate IConfigManager implementation for " + clazz, e);
        }

        if (!(module instanceof IConfigManager)) {
            throw new RuntimeException("Expected instanceof IConfigManager, but found: " + module);
        }
        return (IConfigManager) module;
    }
}
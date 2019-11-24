package com.michaelfotiadis.ibeaconscanner.utils.log;

import android.util.Log;
import java.util.HashSet;
import java.util.Set;

public final class AppLog {
    private static final Set<String> CLASSNAME_TO_ESCAPE = getEscapedClassNames();
    private static final boolean INCLUDE_METHOD = false;
    private static final String LINE_PREFIX = "APP:";
    private static final int MAX_TAG_LENGTH = 50;
    private static final String PACKAGE_PREFIX = "com.michaelfotiadis.ibeaconscanner.";

    private static void dInternal(String str) {
    }

    private static void eInternal(String str, Exception exception) {
    }

    private static void iInternal(String str) {
    }

    private static void vInternal(String str) {
    }

    private static void wInternal(String str, Exception exception) {
    }

    private AppLog() {
    }

    public static void v(String str) {
        vInternal(str);
    }

    public static void i(String str) {
        iInternal(str);
    }

    public static void d(String str) {
        dInternal(str);
    }

    public static void e(String str) {
        eInternal(str, null);
    }

    public static void e(String str, Exception exception) {
        eInternal(str, exception);
    }

    public static void w(String str) {
        wInternal(str, null);
    }

    public static void w(String str, Exception exception) {
        wInternal(str, exception);
    }

    private static String calcTag() {
        String callingMethod = getCallingMethod();
        String str = "";
        if (callingMethod == null) {
            return str;
        }
        callingMethod = callingMethod.replace(PACKAGE_PREFIX, str);
        if ((callingMethod.length() > 50 ? 1 : null) != null) {
            int length = callingMethod.length();
            callingMethod = callingMethod.substring(length - 50, length);
        }
        return callingMethod;
    }

    private static String calcMessage(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LINE_PREFIX);
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    private static String getCallingMethod() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace != null) {
            for (StackTraceElement className : stackTrace) {
                String className2 = className.getClassName();
                if (className2 != null && !CLASSNAME_TO_ESCAPE.contains(className2)) {
                    return className2;
                }
            }
        }
        return null;
    }

    private static Set<String> getEscapedClassNames() {
        HashSet hashSet = new HashSet();
        hashSet.add("java.lang.Thread");
        hashSet.add("dalvik.system.VMStack");
        hashSet.add(Log.class.getName());
        hashSet.add(AppLog.class.getName());
        return hashSet;
    }
}

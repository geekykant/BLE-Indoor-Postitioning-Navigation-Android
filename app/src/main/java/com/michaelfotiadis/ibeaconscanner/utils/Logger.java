package com.michaelfotiadis.ibeaconscanner.utils;

import android.util.Log;

public class Logger {
    private static final boolean LOG_DEBUG = true;

    public static void i(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.i(str, stringBuilder.toString());
    }

    public static void i(String str, String str2, Throwable th) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.i(str, stringBuilder.toString(), th);
    }

    public static void d(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.d(str, stringBuilder.toString());
    }

    public static void d(String str, String str2, Throwable th) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.d(str, stringBuilder.toString(), th);
    }

    public static void e(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.e(str, stringBuilder.toString());
    }

    public static void e(String str, String str2, Throwable th) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.e(str, stringBuilder.toString(), th);
    }

    public static void v(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.v(str, stringBuilder.toString());
    }

    public static void v(String str, String str2, Throwable th) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.v(str, stringBuilder.toString(), th);
    }

    public static void w(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.v(str, stringBuilder.toString());
    }

    public static void w(String str, String str2, Throwable th) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("^");
        stringBuilder.append(str2);
        Log.v(str, stringBuilder.toString(), th);
    }
}

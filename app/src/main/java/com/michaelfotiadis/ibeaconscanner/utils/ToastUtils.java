package com.michaelfotiadis.ibeaconscanner.utils;

import android.app.Activity;
import android.content.Context;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;

public final class ToastUtils {
    private ToastUtils() {
    }

    public static SuperActivityToast makeProgressToast(Activity activity, String str) {
        SuperToast.cancelAllSuperToasts();
        SuperActivityToast superActivityToast = new SuperActivityToast((Context) activity, 4);
        superActivityToast.setAnimations(1);
        superActivityToast.setDuration(Style.DURATION_SHORT);
        superActivityToast.setColor(Style.blue().color);
        superActivityToast.setText(str);
        superActivityToast.setTextSize(16);
        superActivityToast.setTouchToDismiss(true);
        superActivityToast.show();
        return superActivityToast;
    }

    public static SuperActivityToast makeInfoToast(Activity activity, String str) {
        SuperToast.cancelAllSuperToasts();
        SuperActivityToast superActivityToast = new SuperActivityToast((Context) activity, 1);
        superActivityToast.setAnimations(1);
        superActivityToast.setDuration(Style.DURATION_SHORT);
        superActivityToast.setColor(Style.green().color);
        superActivityToast.setText(str);
        superActivityToast.setTextSize(16);
        superActivityToast.setTouchToDismiss(true);
        superActivityToast.show();
        return superActivityToast;
    }

    public static SuperActivityToast makeWarningToast(Activity activity, String str) {
        SuperToast.cancelAllSuperToasts();
        SuperActivityToast superActivityToast = new SuperActivityToast((Context) activity, 1);
        superActivityToast.setAnimations(1);
        superActivityToast.setDuration(Style.DURATION_SHORT);
        superActivityToast.setColor(Style.orange().color);
        superActivityToast.setText(str);
        superActivityToast.setTextSize(16);
        superActivityToast.setTouchToDismiss(true);
        superActivityToast.show();
        return superActivityToast;
    }
}

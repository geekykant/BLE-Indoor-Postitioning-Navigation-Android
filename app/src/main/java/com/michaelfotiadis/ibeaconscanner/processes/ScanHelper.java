package com.michaelfotiadis.ibeaconscanner.processes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.michaelfotiadis.ibeaconscanner.containers.CustomConstants.Payloads;
import com.michaelfotiadis.ibeaconscanner.datastore.Singleton;
import com.michaelfotiadis.ibeaconscanner.services.ScanService;
import com.michaelfotiadis.ibeaconscanner.utils.Logger;

public final class ScanHelper {
    private static final String TAG = ScanHelper.class.getSimpleName();

    private ScanHelper() {
    }

    public static void scanForIBeacons(Context context, int i, int i2) {
        if (isAlarmUp(context)) {
            Logger.e(TAG, "Not starting Service because alarm is already up");
            return;
        }
        Logger.d(TAG, "Generating service intent");
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Scan Time : ");
        stringBuilder.append(i);
        Logger.d(str, stringBuilder.toString());
        str = TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Gap Time : ");
        stringBuilder.append(i2);
        Logger.d(str, stringBuilder.toString());
        cancelService(context);
        Singleton.getInstance().setNumberOfScans(0);
        Intent intent = new Intent(context, ScanService.class);
        intent.putExtra(Payloads.PAYLOAD_1.getString(), i);
        intent.putExtra(Payloads.PAYLOAD_2.getString(), i2);
        Logger.d(TAG, "Attempting to start scan service");
        context.startService(intent);
    }

    public static void cancelService(Context context) {
        Logger.d(TAG, "Cancelling Service Alarm");
        ((AlarmManager) context.getSystemService("alarm")).cancel(PendingIntent.getService(context, 0, new Intent(context, ScanService.class), 268435456));
    }

    private static boolean isAlarmUp(Context context) {
        boolean z = false;
        if (PendingIntent.getBroadcast(context, 0, new Intent(context, ScanService.class), 536870912) != null) {
            z = true;
        }
        if (z) {
            Logger.d(TAG, "Alarm is already active");
        } else {
            Logger.d(TAG, "Alarm not set");
        }
        return z;
    }
}

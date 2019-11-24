package com.michaelfotiadis.ibeaconscanner;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.michaelfotiadis.ibeaconscanner.utils.Logger;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

public class BeaconApplication extends Application {
    private final String TAG = "BeaconApplication";

    public void onCreate() {
        Logger.d("BeaconApplication", "Starting Application");
        super.onCreate();
        Fabric.with(this, new Kit[]{new Crashlytics()});
    }
}

package com.michaelfotiadis.ibeaconscanner.tasks;

import com.michaelfotiadis.ibeaconscanner.datastore.Singleton;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorTask {
    private final OnBeaconDataChangedListener mOnDataChangedListener;
    private long mTimeOfLastUpdate = 0;
    private Timer mTimer;
    private final int mTimerFrequency = 1000;

    public interface OnBeaconDataChangedListener {
        void onDataChanged();
    }

    public MonitorTask(OnBeaconDataChangedListener onBeaconDataChangedListener) {
        this.mOnDataChangedListener = onBeaconDataChangedListener;
        this.mTimer = new Timer();
    }

    public void start() {
        this.mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                long timeOfLastUpdate = Singleton.getInstance().getTimeOfLastUpdate();
                if (MonitorTask.this.mTimeOfLastUpdate < timeOfLastUpdate) {
                    MonitorTask.this.mTimeOfLastUpdate = timeOfLastUpdate;
                    MonitorTask.this.onMonitoredDataChanged();
                }
            }
        }, 0, 1000);
    }

    public void stop() {
        this.mTimer.cancel();
    }

    private void onMonitoredDataChanged() {
        this.mOnDataChangedListener.onDataChanged();
    }
}

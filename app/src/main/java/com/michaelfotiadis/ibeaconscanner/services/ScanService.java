package com.michaelfotiadis.ibeaconscanner.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.IBinder;
import com.github.johnpersano.supertoasts.library.Style;
import com.michaelfotiadis.ibeaconscanner.containers.CustomConstants.Broadcasts;
import com.michaelfotiadis.ibeaconscanner.containers.CustomConstants.Payloads;
import com.michaelfotiadis.ibeaconscanner.datastore.Singleton;
import com.michaelfotiadis.ibeaconscanner.tasks.ScanAsyncTask;
import com.michaelfotiadis.ibeaconscanner.utils.Logger;
import java.util.Calendar;

public class ScanService extends Service {
    private final String TAG = toString();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothReceiver mBluetoothReceiver;
    private int mGapDuration;
    private int mScanDuration;
    private long mServiceDuration;
    private ScanAsyncTask mThread;
    private long mTimeEnd;
    private long mTimeStart;

    public class BluetoothReceiver extends BroadcastReceiver {
        private String TAG = "Bluetooth Receiver";

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                switch (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE)) {
                    case 10:
                        Logger.d(this.TAG, "Bluetooth Receiver State OFF");
                        Logger.d(this.TAG, "Stopping Service");
                        ScanService.this.stopSelf();
                        return;
                    case 11:
                        Logger.d(this.TAG, "Bluetooth Receiver State Turning ON");
                        return;
                    case 12:
                        Logger.d(this.TAG, "Bluetooth Receiver State ON");
                        return;
                    case 13:
                        Logger.d(this.TAG, "Bluetooth Receiver State Turning OFF");
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mTimeStart = Calendar.getInstance().getTimeInMillis();
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Started Scan Service at ");
        stringBuilder.append(this.mTimeStart);
        Logger.d(str, stringBuilder.toString());
    }

    public void onDestroy() {
        unregisterBluetoothReceiver();
        ScanAsyncTask scanAsyncTask = this.mThread;
        if (scanAsyncTask != null && scanAsyncTask.getStatus() == Status.RUNNING) {
            this.mThread.cancel(true);
        }
        this.mTimeEnd = Calendar.getInstance().getTimeInMillis();
        this.mServiceDuration = this.mTimeEnd - this.mTimeStart;
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Service duration in milliseconds : ");
        stringBuilder.append(this.mServiceDuration);
        Logger.d(str, stringBuilder.toString());
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Logger.d(this.TAG, "Handling service intent");
        if (intent == null || intent.getExtras() == null) {
            Logger.e(this.TAG, "No Extras for Service. Aborting...");
            return 2;
        }
        registerBluetoothReceiver();
        this.mScanDuration = intent.getIntExtra(Payloads.PAYLOAD_1.getString(), Style.DURATION_SHORT);
        this.mGapDuration = intent.getIntExtra(Payloads.PAYLOAD_2.getString(), Style.DURATION_SHORT);
        intent = new Intent(Broadcasts.BROADCAST_1.getString());
        Logger.i(this.TAG, "Broadcasting Scanning Status Started");
        sendBroadcast(intent);
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Received Scan Duration ");
        stringBuilder.append(this.mScanDuration);
        Logger.d(str, stringBuilder.toString());
        str = this.TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Received Gap Duration ");
        stringBuilder.append(this.mGapDuration);
        Logger.d(str, stringBuilder.toString());
        this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        str = this.TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Bluetooth Adapter : ");
        stringBuilder.append(this.mBluetoothAdapter.getAddress());
        Logger.d(str, stringBuilder.toString());
        str = this.TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Scan Mode of the adapter : ");
        stringBuilder.append(this.mBluetoothAdapter.getScanMode());
        Logger.d(str, stringBuilder.toString());
        str = this.TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Starting Scanner for ");
        stringBuilder.append(this.mScanDuration);
        Logger.d(str, stringBuilder.toString());
        ScanAsyncTask scanAsyncTask = this.mThread;
        if (scanAsyncTask == null || scanAsyncTask.getStatus() == Status.FINISHED) {
            Logger.i(this.TAG, "onStartCommand starting new thread");
            this.mThread = new ScanAsyncTask(this.mScanDuration, this);
            this.mThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
        } else {
            Logger.i(this.TAG, "onStartCommand NOT starting new thread");
        }
        Singleton.getInstance().setNumberOfScans(Singleton.getInstance().getNumberOfScans() + 1);
        str = this.TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("***Number of scans : ");
        stringBuilder.append(Singleton.getInstance().getNumberOfScans());
        Logger.e(str, stringBuilder.toString());
        intent = new Intent(this, getClass());
        intent.putExtra(Payloads.PAYLOAD_1.getString(), this.mScanDuration);
        intent.putExtra(Payloads.PAYLOAD_2.getString(), this.mGapDuration);
        PendingIntent service = PendingIntent.getService(this, 0, intent, 134217728);
        AlarmManager alarmManager = (AlarmManager) getSystemService("alarm");
        if (this.mGapDuration > 0) {
            Logger.i(this.TAG, "Scheduling Next Run");
            alarmManager.set(0, (Calendar.getInstance().getTimeInMillis() + ((long) this.mScanDuration)) + ((long) this.mGapDuration), service);
            str = this.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Next scan will occur in ");
            stringBuilder.append(this.mScanDuration);
            stringBuilder.append(this.mGapDuration);
            stringBuilder.append(" at ");
            stringBuilder.append(Calendar.getInstance().getTimeInMillis());
            stringBuilder.append(this.mScanDuration);
            stringBuilder.append(this.mGapDuration);
            Logger.i(str, stringBuilder.toString());
        } else {
            alarmManager.cancel(service);
            unregisterBluetoothReceiver();
        }
        return 2;
    }

    private void registerBluetoothReceiver() {
        Logger.d(this.TAG, "Registering Bluetooth Receiver");
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mBluetoothReceiver = new BluetoothReceiver();
        registerReceiver(this.mBluetoothReceiver, intentFilter);
    }

    private void unregisterBluetoothReceiver() {
        try {
            unregisterReceiver(this.mBluetoothReceiver);
            Logger.d(this.TAG, "Bluetooth Receiver Unregistered Successfully");
        } catch (Exception e) {
            String str = this.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bluetooth Receiver Already Unregistered. Exception : ");
            stringBuilder.append(e.getLocalizedMessage());
            Logger.d(str, stringBuilder.toString());
        }
    }
}

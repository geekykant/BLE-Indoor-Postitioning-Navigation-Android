package com.michaelfotiadis.ibeaconscanner.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.michaelfotiadis.ibeaconscanner.GetAbsoluteLocation;
import com.michaelfotiadis.ibeaconscanner.R;
import com.michaelfotiadis.ibeaconscanner.adapter.ExpandableListAdapter;
import com.michaelfotiadis.ibeaconscanner.containers.CustomConstants.Broadcasts;
import com.michaelfotiadis.ibeaconscanner.containers.CustomConstants.Payloads;
import com.michaelfotiadis.ibeaconscanner.datastore.Singleton;
import com.michaelfotiadis.ibeaconscanner.processes.ScanHelper;
import com.michaelfotiadis.ibeaconscanner.tasks.MonitorTask;
import com.michaelfotiadis.ibeaconscanner.tasks.MonitorTask.OnBeaconDataChangedListener;
import com.michaelfotiadis.ibeaconscanner.utils.BluetoothHelper;
import com.michaelfotiadis.ibeaconscanner.utils.Logger;
import com.michaelfotiadis.ibeaconscanner.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;

public class MainActivity extends BaseActivity implements OnChildClickListener, OnCheckedChangeListener {
    private static final int RESULT_SETTINGS = 1;
    private final String TAG = MainActivity.class.getSimpleName();
    ArrayList<BluetoothLeDevice> bl_list;
    private BluetoothHelper mBluetoothHelper;
    private ExpandableListView mExpandableListView;
    private boolean mIsScanRunning = false;
    private boolean mIsToastScanningNowShown;
    private boolean mIsToastStoppingScanShown;
    ExpandableListAdapter mListAdapter;
    HashMap<String, List<String>> mListDataChild;
    List<String> mListDataHeader;
    private MonitorTask mMonitorTask;
    private ResponseReceiver mScanReceiver;
    private SharedPreferences mSharedPrefs;
    private CharSequence mTextViewContents;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    public class ResponseReceiver extends BroadcastReceiver {
        private final String TAG = ResponseReceiver.class.getSimpleName();

        public void onReceive(Context context, Intent intent) {
            Logger.d(this.TAG, "On Receiver Result");
            MainActivity mainActivity;
            if (intent.getAction().equalsIgnoreCase(Broadcasts.BROADCAST_1.getString())) {
                Logger.i(this.TAG, "Scan Running");
                SuperToast.cancelAllSuperToasts();
                MainActivity.this.mIsScanRunning = true;
                MainActivity.this.mIsToastScanningNowShown = true;
                mainActivity = MainActivity.this;
                ToastUtils.makeProgressToast(mainActivity, mainActivity.getString(R.string.toast_scanning));
            } else if (intent.getAction().equalsIgnoreCase(Broadcasts.BROADCAST_2.getString())) {
                Logger.i(this.TAG, "Service Finished");
                SuperToast.cancelAllSuperToasts();
                MainActivity.this.mIsToastScanningNowShown = false;
                mainActivity = MainActivity.this;
                ToastUtils.makeInfoToast(mainActivity, mainActivity.getString(R.string.toast_completed));
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    public void onCheckedChanged(final CompoundButton compoundButton, boolean z) {
        if (z) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION",
                    Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionsResultAction() {
                public void onGranted() {
                    if (!MainActivity.this.mBluetoothHelper.isBluetoothLeSupported()) {
                        MainActivity mainActivity = MainActivity.this;
                        ToastUtils.makeWarningToast(mainActivity, mainActivity.getString(R.string.toast_no_le));
                        compoundButton.setChecked(false);
                    } else if (MainActivity.this.mBluetoothHelper.isBluetoothOn()) {
                        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.create().show();
                            return;
                        }

                        MainActivity.this.serviceToggle();
                    } else {
                        ScanHelper.cancelService(MainActivity.this);
                        MainActivity.this.mBluetoothHelper.askUserToEnableBluetoothIfNeeded();
                        compoundButton.setChecked(false);
                    }
                }

                public void onDenied(String str) {
                    MainActivity mainActivity = MainActivity.this;
                    ToastUtils.makeWarningToast(mainActivity, mainActivity.getString(R.string.toast_warning_permission_not_granted));
                    compoundButton.setChecked(false);
                }
            });
        } else if (this.mIsScanRunning) {
            serviceToggle();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        PermissionsManager.getInstance().notifyPermissionsChange(strArr, iArr);
    }

    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
        HashMap hashMap = this.mListDataChild;
        if (hashMap == null) {
            return false;
        }
        this.bl_list.add(Singleton.getInstance().getBluetoothLeDeviceForAddress((String) ((List) hashMap.get(this.mListDataHeader.get(i))).get(i2)));
        view.setBackgroundColor(-16711681);
        if (this.bl_list.size() == 3) {
            Toast.makeText(this, "3 Beacons selected!", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle(getString(R.string.app_name));

        this.bl_list = new ArrayList();
        findViewById(R.id.finalFab).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), GetAbsoluteLocation.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ARRAYLIST", MainActivity.this.bl_list);
                intent.putExtra("BUNDLE", bundle);
                startActivity(intent);
            }
        });
        this.mExpandableListView = (ExpandableListView) findViewById(R.id.listViewResults);
        this.mExpandableListView.setOnChildClickListener(this);
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (bundle != null) {
            this.mTextViewContents = bundle.getCharSequence(Payloads.PAYLOAD_1.toString());
            this.mIsScanRunning = bundle.getBoolean(Payloads.PAYLOAD_2.toString(), false);
            this.mIsToastScanningNowShown = bundle.getBoolean(Payloads.PAYLOAD_4.toString(), false);
            this.mIsToastStoppingScanShown = bundle.getBoolean(Payloads.PAYLOAD_5.toString(), false);
        }
        this.mBluetoothHelper = new BluetoothHelper(this);
        registerMonitorTask();
        registerResponseReceiver();
        SuperToast.cancelAllSuperToasts();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SwitchCompat switchCompat = (SwitchCompat) menu.findItem(R.id.action_toggle).getActionView().findViewById(R.id.switchForActionBar);
        switchCompat.setChecked(this.mIsScanRunning);
        switchCompat.setOnCheckedChangeListener(this);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            startPreferencesActivity();
        }
        return true;
    }

    public void serviceToggle() {
        SuperToast.cancelAllSuperToasts();
        if (this.mIsScanRunning) {
            ScanHelper.cancelService(this);
            this.mIsToastScanningNowShown = false;
            ToastUtils.makeInfoToast(this, getString(R.string.toast_interrupted));
            this.mIsScanRunning = false;
            return;
        }
        ScanHelper.scanForIBeacons(this, getScanTime(), getPauseTime());
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        removeReceivers();
        super.onDestroy();
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putCharSequence(Payloads.PAYLOAD_1.toString(), this.mTextViewContents);
        bundle.putBoolean(Payloads.PAYLOAD_2.toString(), this.mIsScanRunning);
        bundle.putBoolean(Payloads.PAYLOAD_4.toString(), this.mIsToastScanningNowShown);
        bundle.putBoolean(Payloads.PAYLOAD_5.toString(), this.mIsToastStoppingScanShown);
        super.onSaveInstanceState(bundle);
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        SuperToast.cancelAllSuperToasts();
        ScanHelper.cancelService(this);
        super.onPause();
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        SuperToast.cancelAllSuperToasts();
        handleResume();
    }

    private void handleResume() {
        if (this.mBluetoothHelper.isBluetoothOn() && this.mBluetoothHelper.isBluetoothLeSupported()) {
            Logger.i(this.TAG, "Bluetooth has been activated");
            if (this.mIsScanRunning) {
                Logger.d(this.TAG, "Restarting Scan Service");
                ScanHelper.scanForIBeacons(this, getScanTime(), getPauseTime());
            }
            if (this.mIsToastScanningNowShown) {
                ToastUtils.makeProgressToast(this, getString(R.string.toast_scanning));
            }
        } else {
            SuperToast.cancelAllSuperToasts();
            ToastUtils.makeProgressToast(this, getString(R.string.toast_waiting));
        }
        updateListData();
    }

    /* Access modifiers changed, original: protected */
    public void removeReceivers() {
        try {
            unregisterReceiver(this.mScanReceiver);
            Logger.d(this.TAG, "Scan Receiver Unregistered Successfully");
        } catch (Exception e) {
            String str = this.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Scan Receiver Already Unregistered. Exception : ");
            stringBuilder.append(e.getLocalizedMessage());
            Logger.d(str, stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void stopMonitorTask() {
        if (this.mMonitorTask != null) {
            Logger.d(this.TAG, "Monitor Task paused");
            this.mMonitorTask.stop();
        }
    }

    private int getPauseTime() {
        return Integer.parseInt(this.mSharedPrefs.getString(getString(R.string.pref_pausetime), String.valueOf(getResources().getInteger(R.integer.default_pausetime))));
    }

    private int getScanTime() {
        return Integer.parseInt(this.mSharedPrefs.getString(getString(R.string.pref_scantime), String.valueOf(getResources().getInteger(R.integer.default_scantime))));
    }

    private void notifyDataChanged() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (Singleton.getInstance().getAvailableDevicesList() != null) {
                    MainActivity.this.updateListData();
                }
            }
        });
    }

    private void registerMonitorTask() {
        Logger.d(this.TAG, "Starting Monitor Task");
        this.mMonitorTask = new MonitorTask(new OnBeaconDataChangedListener() {
            public void onDataChanged() {
                Logger.d(MainActivity.this.TAG, "Singleton Data Changed");
                MainActivity.this.notifyDataChanged();
            }
        });
        this.mMonitorTask.start();
    }

    private void registerResponseReceiver() {
        Logger.d(this.TAG, "Registering Response Receiver");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Broadcasts.BROADCAST_1.getString());
        intentFilter.addAction(Broadcasts.BROADCAST_2.getString());
        this.mScanReceiver = new ResponseReceiver();
        registerReceiver(this.mScanReceiver, intentFilter);
    }

    private void startPreferencesActivity() {
        Logger.d(this.TAG, "Starting Settings Activity");
        startActivityForResult(new Intent(this, ScanPreferencesActivity.class), 1);
    }

    private void updateListData() {
        this.bl_list.clear();
        Logger.d(this.TAG, "Updating List Data");
        this.mListDataHeader = new ArrayList();
        List list = this.mListDataHeader;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Available Devices (");
        stringBuilder.append(Singleton.getInstance().getAvailableDeviceListSize());
        stringBuilder.append(")");
        list.add(stringBuilder.toString());
        this.mListDataChild = new HashMap();
        this.mListDataChild.put(this.mListDataHeader.get(0), Singleton.getInstance().getDevicesAvailableAsStringList());
        this.mListAdapter = new ExpandableListAdapter(this, this.mListDataHeader, this.mListDataChild);
        Logger.d(this.TAG, "Setting Adapter");
        this.mExpandableListView.setAdapter(this.mListAdapter);
        this.mExpandableListView.expandGroup(0);
    }
}

package com.michaelfotiadis.ibeaconscanner.tasks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.michaelfotiadis.ibeaconscanner.containers.CustomConstants.Broadcasts;
import com.michaelfotiadis.ibeaconscanner.datastore.Singleton;
import com.michaelfotiadis.ibeaconscanner.utils.Logger;
import java.util.concurrent.ConcurrentHashMap;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;

public class ScanAsyncTask extends AsyncTask<Void, Void, Void> {
    private final String TAG = toString();
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private ConcurrentHashMap<String, BluetoothLeDevice> mDeviceMap;
    private LeScanCallback mLeScanCallback = new LeScanCallback() {
        private final String TAG = "LeScanCallback";

        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
            String str = "LeScanCallback";
            BluetoothLeDevice bluetoothLeDevice = new BluetoothLeDevice(bluetoothDevice, i, bArr, System.currentTimeMillis());
            try {
                IBeaconDevice iBeaconDevice = new IBeaconDevice(bluetoothLeDevice);
                String str2 = "Device ";
                StringBuilder stringBuilder;
                if (ScanAsyncTask.this.mDeviceMap.containsKey(bluetoothLeDevice.getAddress())) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(bluetoothLeDevice.getAddress());
                    stringBuilder.append(" updated.");
                    Logger.d(str, stringBuilder.toString());
                    ScanAsyncTask.this.mDeviceMap.remove(bluetoothLeDevice.getAddress());
                    ScanAsyncTask.this.mDeviceMap.put(bluetoothLeDevice.getAddress(), bluetoothLeDevice);
                    return;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(bluetoothLeDevice.getAddress());
                stringBuilder.append(" added.");
                Logger.d(str, stringBuilder.toString());
                ScanAsyncTask.this.mDeviceMap.put(bluetoothLeDevice.getAddress(), bluetoothLeDevice);
            } catch (Exception e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(bluetoothLeDevice.getAddress());
                stringBuilder2.append(" ");
                stringBuilder2.append(e.getLocalizedMessage());
                Logger.e(str, stringBuilder2.toString());
            }
        }
    };
    private final int scanTime;

    public ScanAsyncTask(int i, Context context) {
        this.scanTime = i;
        this.context = context;
    }

    /* Access modifiers changed, original: protected|varargs */
    public Void doInBackground(Void... voidArr) {
        this.mBluetoothManager = (BluetoothManager) this.context.getSystemService("bluetooth");
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
        try {
            Thread.sleep((long) this.scanTime);
        } catch (InterruptedException e) {
            cancel(true);
            e.printStackTrace();
        }
        this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
        return null;
    }

    /* Access modifiers changed, original: protected */
    public void onPreExecute() {
        Logger.i(this.TAG, "onPreExecute");
        this.mDeviceMap = new ConcurrentHashMap();
        super.onPreExecute();
    }

    /* Access modifiers changed, original: protected */
    public void onPostExecute(Void voidR) {
        Logger.i(this.TAG, "onPostExecute");
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Map contains ");
        stringBuilder.append(this.mDeviceMap.size());
        stringBuilder.append(" unique devices.");
        Logger.d(str, stringBuilder.toString());
        Singleton.getInstance().pruneDeviceList(this.mDeviceMap);
        Intent intent = new Intent(Broadcasts.BROADCAST_2.getString());
        Logger.i(this.TAG, "Broadcasting Scanning Status Finished");
        this.context.sendBroadcast(intent);
        super.onPostExecute(voidR);
    }

    /* Access modifiers changed, original: protected */
    public void onCancelled() {
        Logger.i(this.TAG, "onCancelled");
        super.onCancelled();
    }

    /* Access modifiers changed, original: protected */
    public void onCancelled(Void voidR) {
        Logger.i(this.TAG, "onCancelled (with result)");
        super.onCancelled(voidR);
    }
}

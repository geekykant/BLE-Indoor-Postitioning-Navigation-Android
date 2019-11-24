package com.michaelfotiadis.ibeaconscanner.datastore;

import com.michaelfotiadis.ibeaconscanner.utils.Logger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;

public class Singleton {
    private static final String TAG = Singleton.class.getSimpleName();
    private static volatile Singleton sInstance = null;
    private final ConcurrentHashMap<String, BluetoothLeDevice> mAvailableDevicesList = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, BluetoothLeDevice> mDeviceMap = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, BluetoothLeDevice> mDisappearingDevicesList = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, BluetoothLeDevice> mMovingCloserDevicesList = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, BluetoothLeDevice> mMovingFartherDevicesList = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, BluetoothLeDevice> mNewDevicesList = new ConcurrentHashMap();
    private int mNumberOfScans = 0;
    private long mTimeOfLastUpdate = 0;
    private final ConcurrentHashMap<String, BluetoothLeDevice> mUpdatedDevicesList = new ConcurrentHashMap();

    private Singleton() {
    }

    public int getNumberOfScans() {
        return this.mNumberOfScans;
    }

    public void setNumberOfScans(int i) {
        this.mNumberOfScans = i;
    }

    public BluetoothLeDevice getBluetoothLeDeviceForAddress(String str) {
        for (BluetoothLeDevice bluetoothLeDevice : this.mDeviceMap.values()) {
            if (bluetoothLeDevice.getAddress().equals(str)) {
                return bluetoothLeDevice;
            }
        }
        return null;
    }

    public void pruneDeviceList(Map<String, BluetoothLeDevice> map) {
        String str;
        StringBuilder stringBuilder;
        reportMapContents();
        this.mUpdatedDevicesList.clear();
        this.mNewDevicesList.clear();
        this.mMovingFartherDevicesList.clear();
        this.mMovingCloserDevicesList.clear();
        this.mDisappearingDevicesList.clear();
        for (BluetoothLeDevice bluetoothLeDevice : this.mDeviceMap.values()) {
            if (!map.containsKey(bluetoothLeDevice.getAddress())) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Device disappeared : ");
                stringBuilder.append(bluetoothLeDevice.getAddress());
                Logger.d(str, stringBuilder.toString());
                this.mDisappearingDevicesList.put(bluetoothLeDevice.getAddress(), bluetoothLeDevice);
            }
        }
        for (BluetoothLeDevice bluetoothLeDevice2 : map.values()) {
            this.mAvailableDevicesList.put(bluetoothLeDevice2.getAddress(), bluetoothLeDevice2);
            str = "With average RSSI : ";
            String str2;
            if (this.mDeviceMap.containsKey(bluetoothLeDevice2.getAddress())) {
                Logger.d(TAG, "Device has been updated!");
                str2 = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Device : ");
                stringBuilder.append(bluetoothLeDevice2.getAddress());
                Logger.d(str2, stringBuilder.toString());
                str2 = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(bluetoothLeDevice2.getRunningAverageRssi());
                Logger.d(str2, stringBuilder.toString());
                if (bluetoothLeDevice2.getRunningAverageRssi() <= ((BluetoothLeDevice) this.mDeviceMap.get(bluetoothLeDevice2.getAddress())).getRunningAverageRssi()) {
                    this.mMovingCloserDevicesList.put(bluetoothLeDevice2.getAddress(), bluetoothLeDevice2);
                } else {
                    this.mMovingFartherDevicesList.put(bluetoothLeDevice2.getAddress(), bluetoothLeDevice2);
                }
                this.mDeviceMap.put(bluetoothLeDevice2.getAddress(), bluetoothLeDevice2);
                this.mUpdatedDevicesList.put(bluetoothLeDevice2.getAddress(), bluetoothLeDevice2);
            } else {
                str2 = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("New Device : ");
                stringBuilder.append(bluetoothLeDevice2.getAddress());
                Logger.d(str2, stringBuilder.toString());
                str2 = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(bluetoothLeDevice2.getRunningAverageRssi());
                Logger.d(str2, stringBuilder.toString());
                this.mDeviceMap.put(bluetoothLeDevice2.getAddress(), bluetoothLeDevice2);
                this.mNewDevicesList.put(bluetoothLeDevice2.getAddress(), bluetoothLeDevice2);
            }
        }
        reportMapContents();
        this.mTimeOfLastUpdate = Calendar.getInstance().getTimeInMillis();
    }

    public long getTimeOfLastUpdate() {
        return this.mTimeOfLastUpdate;
    }

    public ConcurrentHashMap<String, BluetoothLeDevice> getAvailableDevicesList() {
        return this.mAvailableDevicesList;
    }

    public int getAvailableDeviceListSize() {
        return this.mAvailableDevicesList.size();
    }

    public int getNewDeviceListSize() {
        return this.mNewDevicesList.size();
    }

    public int getUpdatedDeviceListSize() {
        return this.mUpdatedDevicesList.size();
    }

    public int getMovingCloserDeviceListSize() {
        return this.mMovingCloserDevicesList.size();
    }

    public int getMovingFartherDeviceListSize() {
        return this.mMovingFartherDevicesList.size();
    }

    public int getDisappearingDeviceListSize() {
        return this.mDisappearingDevicesList.size();
    }

    public List<String> getDevicesAvailableAsStringList() {
        ArrayList arrayList = new ArrayList();
        for (BluetoothLeDevice address : this.mAvailableDevicesList.values()) {
            arrayList.add(address.getAddress());
        }
        return arrayList;
    }

    public List<String> getDevicesNewAsStringList() {
        ArrayList arrayList = new ArrayList();
        for (BluetoothLeDevice address : this.mNewDevicesList.values()) {
            arrayList.add(address.getAddress());
        }
        return arrayList;
    }

    public List<String> getDevicesUpdatedAsStringList() {
        ArrayList arrayList = new ArrayList();
        for (BluetoothLeDevice address : this.mUpdatedDevicesList.values()) {
            arrayList.add(address.getAddress());
        }
        return arrayList;
    }

    public List<String> getDevicesMovingCloserAsStringList() {
        ArrayList arrayList = new ArrayList();
        for (BluetoothLeDevice address : this.mMovingCloserDevicesList.values()) {
            arrayList.add(address.getAddress());
        }
        return arrayList;
    }

    public List<String> getDevicesMovingFartherAsStringList() {
        ArrayList arrayList = new ArrayList();
        for (BluetoothLeDevice address : this.mMovingFartherDevicesList.values()) {
            arrayList.add(address.getAddress());
        }
        return arrayList;
    }

    public List<String> getDevicesDisappearingAsStringList() {
        ArrayList arrayList = new ArrayList();
        for (BluetoothLeDevice address : this.mDisappearingDevicesList.values()) {
            arrayList.add(address.getAddress());
        }
        return arrayList;
    }

    private void reportMapContents() {
        Logger.d(TAG, "***Reporting updated devices");
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        String str2 = "Total devices in memory : ";
        stringBuilder.append(str2);
        stringBuilder.append(this.mDeviceMap.size());
        Logger.d(str, stringBuilder.toString());
        for (BluetoothLeDevice bluetoothLeDevice : this.mDeviceMap.values()) {
            String str3 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Device : ");
            stringBuilder2.append(bluetoothLeDevice.getAddress());
            Logger.d(str3, stringBuilder2.toString());
            str3 = TAG;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("With average RSSI : ");
            stringBuilder2.append(bluetoothLeDevice.getRunningAverageRssi());
            Logger.d(str3, stringBuilder2.toString());
            try {
                str3 = TAG;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("With Accuracy : ");
                stringBuilder2.append(new IBeaconDevice(bluetoothLeDevice).getAccuracy());
                Logger.d(str3, stringBuilder2.toString());
            } catch (Exception e) {
                String str4 = TAG;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Failed to cast IBeacon ");
                stringBuilder3.append(bluetoothLeDevice.getAddress());
                stringBuilder3.append(" ");
                stringBuilder3.append(e.getLocalizedMessage());
                Logger.e(str4, stringBuilder3.toString());
            }
        }
        str = TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append(str2);
        stringBuilder.append(this.mDeviceMap.size());
        Logger.d(str, stringBuilder.toString());
        str = TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Number of available devices on the last scan : ");
        stringBuilder.append(this.mAvailableDevicesList.size());
        Logger.d(str, stringBuilder.toString());
        str = TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Number of updated devices on the last scan : ");
        stringBuilder.append(this.mUpdatedDevicesList.size());
        Logger.d(str, stringBuilder.toString());
        str = TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Number of new devices on the last scan : ");
        stringBuilder.append(this.mNewDevicesList.size());
        Logger.d(str, stringBuilder.toString());
        str = TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Number of devices moving nearer : ");
        stringBuilder.append(this.mMovingCloserDevicesList.size());
        Logger.d(str, stringBuilder.toString());
        str = TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Number of devices moving farther away : ");
        stringBuilder.append(this.mMovingFartherDevicesList.size());
        Logger.d(str, stringBuilder.toString());
        str = TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Number of devices that disappeared : ");
        stringBuilder.append(this.mDisappearingDevicesList.size());
        Logger.d(str, stringBuilder.toString());
    }

    public static Singleton getInstance() {
        if (sInstance == null) {
            synchronized (Singleton.class) {
                if (sInstance == null) {
                    sInstance = new Singleton();
                }
            }
        }
        return sInstance;
    }
}

package com.michaelfotiadis.ibeaconscanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.michaelfotiadis.ibeaconscanner.R;
import com.michaelfotiadis.ibeaconscanner.containers.CustomConstants.Payloads;
import com.michaelfotiadis.ibeaconscanner.utils.TimeFormatter;

import java.util.Locale;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconType;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconUtils;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconManufacturerData;
import uk.co.alt236.bluetoothlelib.resolvers.CompanyIdentifierResolver;

public class DeviceActivity extends BaseActivity {
    /* Access modifiers changed, original: protected */
    public int getLayoutResource() {
        return R.layout.activity_device;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setDisplayHomeAsUpEnabled(true);
        BluetoothLeDevice bluetoothLeDevice = (BluetoothLeDevice) getIntent().getParcelableExtra(Payloads.PAYLOAD_1.toString());
        setTitle(bluetoothLeDevice.getName());
        populateDetails((ListView) findViewById(R.id.list_view), bluetoothLeDevice);
    }

    private void appendDeviceInfo(MergeAdapter mergeAdapter, BluetoothLeDevice bluetoothLeDevice) {
        View inflate = getLayoutInflater().inflate(R.layout.list_item_view_device_info, null);
        TextView textView = (TextView) inflate.findViewById(R.id.deviceAddress);
        ((TextView) inflate.findViewById(R.id.deviceName)).setText(bluetoothLeDevice.getName());
        textView.setText(bluetoothLeDevice.getAddress());
        mergeAdapter.addView(inflate);
    }

    private void appendHeader(MergeAdapter mergeAdapter, String str) {
        View inflate = getLayoutInflater().inflate(R.layout.list_item_view_header, null);
        ((TextView) inflate.findViewById(2131230922)).setText(str);
        mergeAdapter.addView(inflate);
    }

    private void appendSimpleText(MergeAdapter mergeAdapter, String str) {
        View inflate = getLayoutInflater().inflate(R.layout.list_item_view_textview, null);
        ((TextView) inflate.findViewById(R.id.data)).setText(str);
        mergeAdapter.addView(inflate);
    }

    private void appendIBeaconInfo(MergeAdapter mergeAdapter, IBeaconManufacturerData iBeaconManufacturerData) {
        View inflate = getLayoutInflater().inflate(R.layout.list_item_view_ibeacon_details, null);
        TextView textView = (TextView) inflate.findViewById(R.id.companyId);
        TextView textView2 = (TextView) inflate.findViewById(R.id.uuid);
        TextView textView3 = (TextView) inflate.findViewById(R.id.major);
        TextView textView4 = (TextView) inflate.findViewById(R.id.minor);
        TextView textView5 = (TextView) inflate.findViewById(R.id.txpower);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CompanyIdentifierResolver.getCompanyName(iBeaconManufacturerData.getCompanyIdentifier(), "Not Available"));
        String str = " (";
        stringBuilder.append(str);
        stringBuilder.append(hexEncode(iBeaconManufacturerData.getCompanyIdentifier()));
        String str2 = ")";
        stringBuilder.append(str2);
        textView.setText(stringBuilder.toString());
        textView2.setText(iBeaconManufacturerData.getUUID());
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(iBeaconManufacturerData.getMajor());
        stringBuilder2.append(str);
        stringBuilder2.append(hexEncode(iBeaconManufacturerData.getMajor()));
        stringBuilder2.append(str2);
        textView3.setText(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(iBeaconManufacturerData.getMinor());
        stringBuilder2.append(str);
        stringBuilder2.append(hexEncode(iBeaconManufacturerData.getMinor()));
        stringBuilder2.append(str2);
        textView4.setText(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(iBeaconManufacturerData.getCalibratedTxPower());
        stringBuilder2.append(str);
        stringBuilder2.append(hexEncode(iBeaconManufacturerData.getCalibratedTxPower()));
        stringBuilder2.append(str2);
        textView5.setText(stringBuilder2.toString());
        mergeAdapter.addView(inflate);
    }

    private void appendRssiInfo(MergeAdapter mergeAdapter, BluetoothLeDevice bluetoothLeDevice) {
        View inflate = getLayoutInflater().inflate(R.layout.list_item_view_rssi_info, null);
        TextView textView = (TextView) inflate.findViewById(R.id.lastRssi);
        ((TextView) inflate.findViewById(R.id.lastTimestamp)).setText(formatTime(bluetoothLeDevice.getTimestamp()));
        textView.setText(formatRssi(bluetoothLeDevice.getRssi()));
        mergeAdapter.addView(inflate);
    }

    private String formatRssi(int i) {
        return getString(R.string.formatter_db, new Object[]{String.valueOf(i)});
    }

    private void populateDetails(ListView listView, BluetoothLeDevice bluetoothLeDevice) {
        MergeAdapter mergeAdapter = new MergeAdapter();
        String str = "Device Info";
        if (bluetoothLeDevice == null) {
            appendHeader(mergeAdapter, str);
            appendSimpleText(mergeAdapter, "Invalid Device");
        } else {
            appendHeader(mergeAdapter, str);
            appendDeviceInfo(mergeAdapter, bluetoothLeDevice);
            if (BeaconUtils.getBeaconType(bluetoothLeDevice) == BeaconType.IBEACON) {
                IBeaconManufacturerData iBeaconManufacturerData = new IBeaconManufacturerData(bluetoothLeDevice);
                appendHeader(mergeAdapter, "iBeacon Data");
                appendIBeaconInfo(mergeAdapter, iBeaconManufacturerData);
            }
            appendHeader(mergeAdapter, "RSSI Info");
            appendRssiInfo(mergeAdapter, bluetoothLeDevice);
        }
        listView.setAdapter(mergeAdapter);
    }

    private static String formatTime(long j) {
        return TimeFormatter.getIsoDateTime(j);
    }

    private static String hexEncode(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0x");
        stringBuilder.append(Integer.toHexString(i).toUpperCase(Locale.US));
        return stringBuilder.toString();
    }
}

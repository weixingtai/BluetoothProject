package com.lumex.bluetoothproject.configuration;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by 阿泰Charles on 2016/12/03.
 */

public class DeviceConfiguration extends AppCompatActivity {
    private ArrayList<ADInfo> infos = new ArrayList<>();
    private ImageCycleView icvBanner;//轮播器
    private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};
    // 调试信息
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    // 适配器
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        setResult(Activity.RESULT_CANCELED);
        icvBanner = (ImageCycleView)findViewById(R.id.icv_config_banner);
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.config_list_item);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.config_list_item);
        //寻找和建立已配对设备列表
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);
        // 寻找和建立新发现的设备列表
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        // 注册时发送广播给设备
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // 注册完成发现广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        // 获取本地蓝牙适配器
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        // 得到一套目前配对设备
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n"
                        + device.getAddress());
            }
        }
        else {
            String noDevices = getResources().getText(R.string.none_paired)
                    .toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
//读取轮播器图片并设置到banner
        for(int i=0;i < imageUrls.length; i ++){
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("top-->" + i);
            infos.add(info);
        }
        icvBanner.setImageResources(infos,mBannerCycleViewListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        icvBanner.pushImageCycle();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        icvBanner.startImageCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        icvBanner.pushImageCycle();
    }
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");
        setProgressBarIndeterminateVisibility(true);
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        mBtAdapter.startDiscovery();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3){
            mBtAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.
                        getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n"
                            + device.getAddress());
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(
                            R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };



    /**
     * 监听ImageCycleView视图
     */
    private ImageCycleView.ImageCycleViewListener mBannerCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }

        @Override
        public void onImageClick(ADInfo info, int postion, View imageView) {
            Toast.makeText(DeviceConfiguration.this, "content->"+info.getContent(), Toast.LENGTH_SHORT).show();
        }
    };
}

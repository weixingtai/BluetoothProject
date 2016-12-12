package com.lumex.bluetoothproject.configuration;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 阿泰Charles on 2016/12/03.
 */

public class DeviceConfiguration extends AppCompatActivity {
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private static final String TAG = "BluetoothTest";
    //private static final boolean STATE_CONNECTED = true;
    public static BluetoothSocket socket = null;
    public static BluetoothSocket btSocket;
    public static AcceptThread serverThread;

    private UUID uuid ;
    private ListView lvConnect;//连接列表
    private ArrayAdapter<String> adtDevices;
    private List<String> lstDevices = new ArrayList<>();
    private BluetoothAdapter btAdapt;
    private ImageCycleView icvBanner;//轮播器
    private ArrayList<ADInfo> infos = new ArrayList<>();
    //private String[] bluetoothItem = {"通讯连接埠(COM1)","通讯连接埠(COM2)","Prolific USB-to-Serial Comm Port(COM5)"};
    private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        initView();
        initData();
        lvConnect.setOnItemClickListener(new ItemClickEvent());
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

    @Override
    protected void onDestroy() {

        this.unregisterReceiver(searchDevices);
        super.onDestroy();
        icvBanner.pushImageCycle();
        //android.os.Process.killProcess(android.os.Process.myPid());
        //serverThread.cancel();
        //serverThread.destroy();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        icvBanner = (ImageCycleView)findViewById(R.id.config_banner);
        lvConnect = (ListView)findViewById(R.id.lv_connect);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        adtDevices = new ArrayAdapter<>(DeviceConfiguration.this,
                android.R.layout.simple_list_item_1, lstDevices);
        lvConnect.setEmptyView(findViewById(R.id.lv_empty));
        lvConnect.setAdapter(adtDevices);
        uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
        //检查手机是否有蓝牙功能
        if(btAdapt == null){
            Toast.makeText(DeviceConfiguration.this, "很抱歉，您的手机没有蓝牙功能！", Toast.LENGTH_LONG).show();
            //finish();
            //return;
        }
        else{
            // 读取蓝牙状态并显示
            if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)
            {
                Toast.makeText(DeviceConfiguration.this, "蓝牙尚未打开,请先打开蓝牙功能！", Toast.LENGTH_LONG).show();
            }
            else if (btAdapt.getState() == BluetoothAdapter.STATE_ON){
                //蓝牙已开启，服务端监听
                Toast.makeText(DeviceConfiguration.this, "蓝牙搜索中！", Toast.LENGTH_LONG).show();
                //lstDevices.clear();
                btAdapt.startDiscovery();
                serverThread=new AcceptThread();
                serverThread.start();

            }

            // 注册Receiver来获取蓝牙设备相关的结果
            IntentFilter intent = new IntentFilter();
            // 用BroadcastReceiver来取得搜索结果
            intent.addAction(BluetoothDevice.ACTION_FOUND);
            intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(searchDevices, intent);
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

    private void manageConnectedSocket() {
        //setTitle("检测到蓝牙接入！");
        btSocket=socket;
        //打开控制继电器实例
    }

    /**
     * ListView 的item 点击事件
     */
    class ItemClickEvent implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String str = lstDevices.get(i);
            String[] values = str.split("\\|");
            String address=values[1];
            Log.e("address",values[1]);

            uuid = UUID.fromString(SPP_UUID);
            Log.e("uuid",uuid.toString());

            BluetoothDevice btDev = btAdapt.getRemoteDevice(address);//"00:11:00:18:05:45"

            int sdk = Integer.parseInt(Build.VERSION.SDK);
            if (sdk >= 10) {
                try {
                    btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
                } catch (IOException e) {
                    Log.e(TAG, " High: Connection failed.", e);
                }
            }
            else {
                try {
                    btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
                } catch (IOException e) {
                    Log.e(TAG, "Low: Connection failed.", e);
                }
            }

            Method m;
            try {
                m = btDev.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                btSocket = (BluetoothSocket) m.invoke(btDev, Integer.valueOf(1));
            } catch (SecurityException e1) {
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


            btAdapt.cancelDiscovery();
            try {
                //btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
                btSocket.connect();
                Log.e(TAG, " BT connection established, data transfer link open.");

                Toast.makeText(DeviceConfiguration.this, "连接成功!", Toast.LENGTH_SHORT).show();

                //setTitle("连接成功");

                //打开控制继电器实例
                //Intent intent = new Intent();
                //intent.setClass(DeviceConfiguration.this, RelayControl.class);
                //startActivity(intent);

            } catch (IOException e) {
                Log.e(TAG, " Connection failed.", e);
                Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT);
//                setTitle("连接失败..");
            }
            //Toast.makeText(DeviceConfiguration.this,l+" 连接成功！",Toast.LENGTH_SHORT).show();
        }
    }
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e(keyName, String.valueOf(b.get(keyName)));
            }
            //搜索设备时，取得设备的MAC地址
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String str= device.getName() + "|" + device.getAddress();

                if (lstDevices.indexOf(str) == -1)// 防止重复添加
                    lstDevices.add(str); // 获取设备名称和mac地址
                adtDevices.notifyDataSetChanged();
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


    class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;
        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp=null;
            try {
                //tmp = btAdapt.listenUsingRfcommWithServiceRecord("MyBluetoothApp", uuid);

                Log.e(TAG, "++BluetoothServerSocket established!++");
                Method listenMethod = btAdapt.getClass().getMethod("listenUsingRfcommOn", new Class[]{int.class});
                tmp = ( BluetoothServerSocket) listenMethod.invoke(btAdapt, Integer.valueOf( 1));

            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            serverSocket=tmp;
        }

        public void run() {

            // Keep listening until exception occurs or a socket is returned
            //mState!=STATE_CONNECTED
            while(true) {
                try {
                    if(serverSocket==null){
                        continue;
                    }
                    socket = serverSocket.accept();
                    Log.e(TAG, "++BluetoothSocket established! DataLink open.++");
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    manageConnectedSocket();
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) { }
        }
    }
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            serverThread.cancel();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}

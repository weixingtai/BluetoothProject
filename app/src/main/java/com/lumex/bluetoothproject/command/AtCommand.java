package com.lumex.bluetoothproject.command;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;
import com.lumex.bluetoothproject.adapter.CommandAdapter;
import com.lumex.bluetoothproject.configuration.BluetoothChatService;
import com.lumex.bluetoothproject.configuration.DeviceConfiguration;
import com.lumex.bluetoothproject.util.db.Command;
import com.lumex.bluetoothproject.util.db.CommandDao;
import com.lumex.bluetoothproject.util.db.DBManager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 阿泰Charles on 2016/12/03.
 * 指令添加到数据库，数据库使用GreenDAO
 * 单击为修改item
 * 长按为删除item
 * 通过button按钮添加item
 */

public class AtCommand extends AppCompatActivity {
    private static final String TAG = "BluetoothChat";
    private static boolean D = true;

    private static final String info = "junge";
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String BluetoothData = "fullscreen";
    public String filename = "";
    private String newCode = "";
    private String newCode2 = "";
    private String fmsg = "";
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private Button breakButton;
    private TextView state;

    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    private boolean dialogs;

    private int sum =1;
    private int UTF =1;

    String mmsg = "";
    String mmsg2 = "";


    private Button btnAppend;//添加命令
    private ListView lvCommand;//命令列表
    private CommandAdapter mCommandAdapter;
    private List<Command> commandItem = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command);
        state = (TextView)findViewById(R.id.tv_state);
        D = false;
        if (D)
            Log.e(TAG, "+++ ON CREATE +++");
        Log.i(info, "" + dialogs);
        initView();
        initData();
        breakButton = (Button) findViewById(R.id.button_break);
        // 得到当地的蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "未连接", Toast.LENGTH_LONG)
                    .show();
            finish();
            return;
        }

        /**
         * 通过 btnAppend 增加一个新命令
         */
        btnAppend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(AtCommand.this);
                LinearLayout commandDialog = (LinearLayout) inflater.inflate(R.layout.command_layout_dialog, null);
                final EditText edtCommandCaption = (EditText) commandDialog.findViewById(R.id.edt_command_caption);
                final EditText edtCommandContent = (EditText) commandDialog.findViewById(R.id.edt_command_content);

                new AlertDialog.Builder(AtCommand.this).setTitle("添加命令")
                        .setView(commandDialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String strCommand = edtCommandCaption.getText().toString();
                                String strContent = edtCommandContent.getText().toString();

                                if (strCommand.equals("") || strContent.equals("")) {
                                    Toast.makeText(getApplicationContext(),
                                            "command can not be null！" + strCommand, Toast.LENGTH_LONG).show();
                                } else {
                                    addCommand(null,1, strCommand, strContent);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        /**
         * 单击发送item
         */
        lvCommand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutInflater inflater = LayoutInflater.from(AtCommand.this);
                LinearLayout commandDialog = (LinearLayout) inflater.inflate(R.layout.command_layout_dialog_view, null);
                final TextView tvCommandContent = (TextView) commandDialog.findViewById(R.id.tv_command_content);
                Command oldCommand = findCommand(i);
                String oldCommandContent = oldCommand.getCommandContent();
                tvCommandContent.setText(oldCommandContent);
                int oldCommandType = oldCommand.getCommandType();
                if (oldCommandType == 1){
                    new AlertDialog.Builder(AtCommand.this).setTitle("发送命令")
                            .setView(commandDialog)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String message = tvCommandContent.getText().toString();
                                    try {
                                        message.getBytes("ISO_8859_1");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    sendMessage(message);

                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }else if(oldCommandType == 2){
                    new AlertDialog.Builder(AtCommand.this).setTitle("发送命令")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String message = tvCommandContent.getText().toString();
                                    try {
                                        message.getBytes("ISO_8859_1");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    sendMessage(message);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }

            }
        });

        /**
         * 长按删除item
         */
        lvCommand.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertDialog.Builder(AtCommand.this).setTitle("删除这个命令?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCommand(i);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnAppend = (Button) findViewById(R.id.btn_command_append);
        lvCommand = (ListView) findViewById(R.id.lv_command);
    }

    /**
     * 数据库取得并初始化ListView数据
     */
    private void initData() {
        commandItem = DBManager.getInstance(this).getDaoSession().getCommandDao().queryBuilder().build().list();
        mCommandAdapter = new CommandAdapter(this, commandItem);
        lvCommand.setAdapter(mCommandAdapter);
    }

    /**
     * 插入一个新的命令
     * @param id      command的id
     * @param caption command的标题
     * @param content command的内容
     */
    private void addCommand(Long id,int type, String caption, String content) {
        CommandDao commandDao = DBManager.getInstance(this).getDaoSession().getCommandDao();
        Command command = new Command(id, type, caption, content);
        commandDao.insert(command);

        commandItem.clear();
        commandItem.addAll(DBManager.getInstance(this).getDaoSession().getCommandDao().queryBuilder().build().list());
        mCommandAdapter.notifyDataSetChanged();
    }

    /**
     * 删除命令
     *
     * @param p item在commandItem中的位置
     */
    private void deleteCommand(int p) {
        CommandDao commandDao = DBManager.getInstance(this).getDaoSession().getCommandDao();
        Command findCommand = findCommand(p);
        if (findCommand != null) {
            commandDao.deleteByKey(findCommand.getId());
            commandItem.clear();
            commandItem.addAll(DBManager.getInstance(this).getDaoSession().getCommandDao().queryBuilder().build().list());
            mCommandAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 查找命令
     *
     * @param p item在commandItem中的位置
     * @return 返回查找结果Command 找到：command  找不到：null
     */
    private Command findCommand(int p) {
        Command command = null;
        String caption = commandItem.get(p).getCommandCaption();
        CommandDao commandDao = DBManager.getInstance(this).getDaoSession().getCommandDao();
        command = commandDao.queryBuilder().where(CommandDao.Properties.CommandCaption.eq(caption)).build().unique();
        return command;
    }

    private void updateCommand(String oldCaption, String newCaption, String newContent) {
        Command findCommand = DBManager.getInstance(this).getDaoSession().getCommandDao().queryBuilder()
                .where(CommandDao.Properties.CommandCaption.eq(oldCaption)).build().unique();
        if (findCommand != null) {
            findCommand.setCommandCaption(newCaption);
            findCommand.setCommandContent(newContent);
            DBManager.getInstance(this).getDaoSession().getCommandDao().update(findCommand);
            commandItem.clear();
            commandItem.addAll(DBManager.getInstance(this).getDaoSession().getCommandDao().queryBuilder().build().list());
            mCommandAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(AtCommand.this, "指令不存在！", Toast.LENGTH_SHORT);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if (D)
            Log.e(TAG, "++ ON START ++");
        //如果蓝牙可用启动蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mChatService == null)
                setupChat();
        }
    }
    //建立一个会话，发送消息
    private void setupChat() {
        Log.d(TAG, "setupChat()");
        // 初始化bluetoothchatservice执行蓝牙连接
        mChatService = new BluetoothChatService(this, mHandler);
    }

    public void onConnectButtonClicked(View v) {

        if (breakButton.getText().equals("连接")||breakButton.getText().equals("connect")) {
            Intent serverIntent = new Intent(this, DeviceConfiguration.class); // 跳转程序设置
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE); // 设置返回宏定义
            breakButton.setText("断开");

        } else {
            // 关闭连接端口
            try {
                // 关闭蓝牙
                breakButton.setText("连接");
                mChatService.stop();

            } catch (Exception e) {
            }
        }
        return;
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D)
            Log.e(TAG, "+ ON RESUME +");
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }
    public void onMyButtonClick(View view) {

        if (view.getId() == R.id.button_break) {

            onConnectButtonClicked(breakButton);
        }
    }
    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D)

            Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D)
            Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 蓝牙聊天服务站
//        if (mChatService != null)
//            mChatService.stop();
        if (D)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if (D)
            Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(
                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    /**
     * 发送一个消息
     *
     * @param message
     *            一个文本字符串发送.
     */
    private void sendMessage(String message) {
        // 检查是否连接一个设备
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "未连接", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // 检查是否输入了内容
        if (message.length() > 0) {
            // 得到消息字节传递给bluetoothChatService
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    // 处理程序，获取信息的bluetoothchatservice回来
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            state.setText("已连接");
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            state.setText("正在连接");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            state.setText("无法连接");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                case MESSAGE_READ:
                    break;
                case MESSAGE_DEVICE_NAME:
                    // 保存该连接装置的名字
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "已连接 " + mConnectedDeviceName, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    };
    public String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }
    /**
     * 将字符编码转换成UTF-8码
     */
    public String toUTF_8(String str) throws UnsupportedEncodingException {
        return this.changeCharset(str, "UTF_8");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // 当devicelistactivity返回连接装置
                if (resultCode == Activity.RESULT_OK) {
                    // 获得设备地址
                    String address = data.getExtras().getString(
                            DeviceConfiguration.EXTRA_DEVICE_ADDRESS);
                    // 把蓝牙设备对象
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // 试图连接到装置
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // 当请求启用蓝牙返回
                if (resultCode == Activity.RESULT_OK) {
                    // 蓝牙已启用，所以建立一个聊天会话
                    setupChat();
                } else {
                    // 用户未启用蓝牙或发生错误
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "发生错误",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}



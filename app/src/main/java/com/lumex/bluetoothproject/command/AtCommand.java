package com.lumex.bluetoothproject.command;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;
import com.lumex.bluetoothproject.adapter.CommandAdapter;
import com.lumex.bluetoothproject.util.db.Command;
import com.lumex.bluetoothproject.util.db.CommandDao;
import com.lumex.bluetoothproject.util.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 阿泰Charles on 2016/12/03.
 * 指令添加到数据库，数据库使用GreenDAO
 * 单击为修改item
 * 长按为删除item
 * 通过button按钮添加item
 */

public class AtCommand extends AppCompatActivity {

    private Button btnAppend;//添加命令
    private ListView lvCommand;//命令列表
    private CommandAdapter mCommandAdapter;
    private List<Command> commandItem = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command);
        initView();
        initData();

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
                                    Toast.makeText(getApplicationContext(), "command can not be null！" + strCommand, Toast.LENGTH_LONG).show();
                                } else {
                                    addCommand(null, strCommand, strContent);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        /**
         * 单击修改item
         */
        lvCommand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutInflater inflater = LayoutInflater.from(AtCommand.this);
                LinearLayout commandDialog = (LinearLayout) inflater.inflate(R.layout.command_layout_dialog, null);
                final EditText edtCommandCaption = (EditText) commandDialog.findViewById(R.id.edt_command_caption);
                final EditText edtCommandContent = (EditText) commandDialog.findViewById(R.id.edt_command_content);
                Command oldCommand = findCommand(i);
                final String oldCommandCaption = oldCommand.getCommandCaption();
                String oldCommandContent = oldCommand.getCommandContent();
                edtCommandCaption.setText(oldCommandCaption);
                edtCommandContent.setText(oldCommandContent);
                new AlertDialog.Builder(AtCommand.this).setTitle("修改命令")
                        .setView(commandDialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String strCommand = edtCommandCaption.getText().toString();
                                String strContent = edtCommandContent.getText().toString();

                                if (strCommand.equals("") || strContent.equals("")) {
                                    Toast.makeText(getApplicationContext(), "command can not be null！" + strCommand, Toast.LENGTH_LONG).show();
                                } else {

                                    updateCommand(oldCommandCaption, strCommand, strContent);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
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
     *
     * @param id      command的id
     * @param caption command的标题
     * @param content command的内容
     */
    private void addCommand(Long id, String caption, String content) {
        CommandDao commandDao = DBManager.getInstance(this).getDaoSession().getCommandDao();
        Command command = new Command(id, caption, content);
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
}

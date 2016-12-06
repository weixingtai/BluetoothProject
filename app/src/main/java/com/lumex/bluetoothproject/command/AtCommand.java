package com.lumex.bluetoothproject.command;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 阿泰Charles on 2016/12/03.
 */

public class AtCommand extends AppCompatActivity{
    private Button btnAppend;
    private ListView lvCommand;

    private List<String> commandItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command);
        commandItem = new ArrayList<String>();
        commandItem.add("COMMAND 1");
        commandItem.add("COMMAND 2");
        commandItem.add("COMMAND 3");
        final EditText edtNewCommand = new EditText(this);
        btnAppend = (Button)findViewById(R.id.btn_command_append);
        lvCommand = (ListView)findViewById(R.id.lv_command);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,commandItem);
        lvCommand.setAdapter(arrayAdapter);
        btnAppend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AtCommand.this).setTitle("Append a new command")
                        .setView(edtNewCommand)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String strCommand = edtNewCommand.getText().toString();
                                if (strCommand.equals("")) {
                                    Toast.makeText(getApplicationContext(), "command name can not be null！" + strCommand, Toast.LENGTH_LONG).show();
                                }
                                else {
                                    commandItem.add(strCommand);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        lvCommand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), l + " was click！", Toast.LENGTH_LONG).show();
            }
        });
    }
}

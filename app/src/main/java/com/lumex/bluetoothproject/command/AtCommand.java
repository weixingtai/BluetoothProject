package com.lumex.bluetoothproject.command;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.lumex.bluetoothproject.R;

/**
 * Created by 阿泰Charles on 2016/12/03.
 */

public class AtCommand extends AppCompatActivity{
    private Button btnAppend;
    private ListView lvCommand;

    private String[] commandItem = {"COMMAND 1","COMMAND 2","COMMAND 3"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command);

        btnAppend = (Button)findViewById(R.id.btn_command_append);
        lvCommand = (ListView)findViewById(R.id.lv_command);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,commandItem);
        lvCommand.setAdapter(arrayAdapter);


    }
}

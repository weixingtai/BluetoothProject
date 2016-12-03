package com.lumex.bluetoothproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.lumex.bluetoothproject.command.AtCommand;
import com.lumex.bluetoothproject.configuration.DeviceConfiguration;
import com.lumex.bluetoothproject.graphic.Graphic;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnConfig;
    private Button btnCommand;
    private Button btnGraphic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConfig = (Button)findViewById(R.id.btn_main_config);
        btnCommand = (Button)findViewById(R.id.btn_main_command);
        btnGraphic = (Button)findViewById(R.id.btn_main_graphic);
        btnConfig.setOnClickListener(this);
        btnCommand.setOnClickListener(this);
        btnGraphic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_main_config:
                Intent intentConfig = new Intent(MainActivity.this, DeviceConfiguration.class);
                startActivity(intentConfig);
                break;
            case R.id.btn_main_command:
                Intent intentCommand = new Intent(MainActivity.this, AtCommand.class);
                startActivity(intentCommand);
                break;
            case R.id.btn_main_graphic:
                Intent intentGraphic = new Intent(MainActivity.this, Graphic.class);
                startActivity(intentGraphic);
                break;
        }

    }
}

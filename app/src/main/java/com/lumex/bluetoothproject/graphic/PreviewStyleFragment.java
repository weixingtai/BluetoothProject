package com.lumex.bluetoothproject.graphic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;

/**
 * Created by 阿泰Charles on 2016/12/05.
 */

public class PreviewStyleFragment extends Fragment {
    private CheckBox chkLoop;
    private RadioGroup groupOperation;
    public PreviewStyleFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_fg_style,container,false);
        chkLoop = (CheckBox)view.findViewById(R.id.chk_style_loop);
        groupOperation = (RadioGroup)view.findViewById(R.id.group_style_operation);
        groupOperation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_style_c:
                        Toast.makeText(getActivity(),"C Style is select",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_style_hex:
                        Toast.makeText(getActivity(),"Hex is select",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        chkLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getActivity(),"Loop sent:"+b,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}

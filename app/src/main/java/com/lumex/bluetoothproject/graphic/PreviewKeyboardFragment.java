package com.lumex.bluetoothproject.graphic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;

/**
 * Created by 阿泰Charles on 2016/12/05.
 */

public class PreviewKeyboardFragment extends Fragment {
    public PreviewKeyboardFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_fg_keyboard,container,false);
        Button btnKeyBoard = (Button)view.findViewById(R.id.btn_preview_keyboard);
        btnKeyBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"keyboard called up",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

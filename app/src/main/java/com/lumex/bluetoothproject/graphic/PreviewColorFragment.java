package com.lumex.bluetoothproject.graphic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lumex.bluetoothproject.R;

/**
 * Created by 阿泰Charles on 2016/12/05.
 */

public class PreviewColorFragment extends Fragment {
    public PreviewColorFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_fg_color,container,false);
        Log.e("show", "color");
        return view;
    }
}

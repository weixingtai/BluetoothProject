package com.lumex.bluetoothproject.graphic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lumex.bluetoothproject.R;

/**
 * Created by 阿泰Charles on 2016/12/05.
 */

public class PreviewDelayFragment extends Fragment {
    public PreviewDelayFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_fg_delay,container,false);
        Log.e("show", "delay");
        final TextView tvTime = (TextView)view.findViewById(R.id.tv_delay_fg_time);
        SeekBar sbDelay = (SeekBar)view.findViewById(R.id.sb_delay_fg_delay);
        sbDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvTime.setText(i/10+" Seconds");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }
}

package com.lumex.bluetoothproject.graphic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lumex.bluetoothproject.R;

/**
 * Created by 阿泰Charles on 2016/12/05.
 */

public class PreviewColorFragment extends Fragment {
    private ColorPickerView colorPicker;
    private EditText edtEditor;

    public PreviewColorFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_fg_color,container,false);
        colorPicker = (ColorPickerView)view.findViewById(R.id.cpv_color_picker);
        edtEditor = (EditText) getActivity().findViewById(R.id.edt_graphic_editor);
        edtEditor.setTextColor(Color.BLACK);
        colorPicker.setOnColorChangedListenner(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color, int originalColor, float saturation) {
                Log.e("color", ""+color);
                edtEditor.setTextColor(color);
            }
        });

        return view;
    }
}

package com.lumex.bluetoothproject.graphic;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.lumex.bluetoothproject.R;

/**
 * Created by 阿泰Charles on 2016/12/05.
 */

public class PreviewTextFragment extends Fragment {
    private static int fontSize=18;
    //private static float TextScaleX=1.0f;
    private static float lineSpacing=1f;
    private Button btnAddSize;
    private Button btnReduceSize;
    private Button btnLineSpacingAdd;
    private Button btnLineSpacingReduce;
    private EditText edtEditor;
    private RadioGroup groupAlign;
    private Spinner fontType;

    public PreviewTextFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_fg_text,container,false);
        btnAddSize = (Button)view.findViewById(R.id.btn_text_add_size);
        btnReduceSize = (Button)view.findViewById(R.id.btn_text_reduce_size);
        btnLineSpacingAdd = (Button)view.findViewById(R.id.btn_text_add_spacing);
        btnLineSpacingReduce = (Button)view.findViewById(R.id.btn_text_reduce_spacing);
        edtEditor = (EditText) getActivity().findViewById(R.id.edt_graphic_editor);
        groupAlign = (RadioGroup) view.findViewById(R.id.group_text_align);
        fontType = (Spinner)view.findViewById(R.id.spn_text_fg_font);
        edtEditor.setTextSize(fontSize);
        fontType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Typeface fontNormal = Typeface.create(Typeface.SANS_SERIF,Typeface.NORMAL);
                        edtEditor.setTypeface(fontNormal);
                        break;
                    case 1:
                        Typeface fontItalic = Typeface.create(Typeface.SANS_SERIF,Typeface.ITALIC);
                        edtEditor.setTypeface(fontItalic);
                        break;
                    case 2:
                        Typeface fontBold = Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD);
                        edtEditor.setTypeface(fontBold);
                        break;
                    case 3:
                        Typeface fontBoldItalic = Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD_ITALIC);
                        edtEditor.setTypeface(fontBoldItalic);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //增加行间距
        btnLineSpacingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lineSpacing>3f){
                    lineSpacing=3f;
                }else {
                    float addLineSpacing = lineSpacing+0.2f;
                    edtEditor.setLineSpacing(lineSpacing,addLineSpacing);
                    lineSpacing = addLineSpacing;
                }
            }
        });
        //减少行间距
        btnLineSpacingReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lineSpacing<1f){
                    lineSpacing=1f;
                }else {
                    float reduceLineSpacing = lineSpacing-0.2f;
                    edtEditor.setLineSpacing(lineSpacing,reduceLineSpacing);
                    lineSpacing = reduceLineSpacing;
                }
            }
        });

        //选择对齐方式
        groupAlign.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_align_left:
                        edtEditor.setGravity(Gravity.LEFT);
                       //edtEditor.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

                        break;
                    case R.id.rb_align_center:
                        edtEditor.setGravity(Gravity.CENTER_HORIZONTAL);
                        break;
                    case R.id.rb_align_right:
                        edtEditor.setGravity(Gravity.RIGHT);
                        break;
                    case R.id.rb_align_vertical:
                        edtEditor.setGravity(Gravity.LEFT);
                        edtEditor.setEms(3);
                        break;
                }
            }
        });
        //加大字体
        btnAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(fontSize);
                if(fontSize<50) {
                    fontSize = fontSize + 1;
                    edtEditor.setTextSize(fontSize);
                }else {
                    fontSize = 50;
                }
                //edtEditor.setTextScaleX(++TextScaleX);
            }
        });
        //缩小字体
        btnReduceSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fontSize<5) {
                    fontSize=5;
                }else {
                    fontSize = fontSize - 1;
                    edtEditor.setTextSize(fontSize);
                }
                //edtEditor.setTextScaleX(--TextScaleX);
            }
        });
        return view;
    }
}

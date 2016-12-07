package com.lumex.bluetoothproject.graphic;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;

/**
 * Created by 阿泰Charles on 2016/12/03.
 */

public class Graphic extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private RadioGroup rgTabBar;
    private RadioButton rbText;
    private RadioButton rbColor;
    private RadioButton rbDelay;
    private RadioButton rbKeyboard;
    private RadioButton rbStyle;
    private ViewPager vpager;
    private EditText edtEditor;
    private PerformEdit mPerformEdit;
    private ImageView ivUndo;
    private ImageView ivRedo;
    private MyFragmentPagerAdapter mAdapter;
    private Button btnSave;
    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphic);


        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rbColor.setChecked(true);
    }

    private void bindViews() {
        rgTabBar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rbText = (RadioButton) findViewById(R.id.rb_text);
        rbColor = (RadioButton) findViewById(R.id.rb_color);
        rbDelay = (RadioButton) findViewById(R.id.rb_delay);
        rbKeyboard = (RadioButton) findViewById(R.id.rb_keyboard);
        rbStyle = (RadioButton) findViewById(R.id.rb_style);
        btnSave = (Button)findViewById(R.id.btn_top_save);
        ivUndo = (ImageView)findViewById(R.id.ib_graphic_undo);
        ivRedo = (ImageView)findViewById(R.id.ib_graphic_redo);
        edtEditor = (EditText)findViewById(R.id.edt_graphic_editor);
        mPerformEdit = new PerformEdit(edtEditor){
            @Override
            protected void onTextChanged(Editable s) {
                //文本发生改变,可以是用户输入或者是EditText.setText触发.(setDefaultText的时候不会回调)
                super.onTextChanged(s);
            }
        };
        ivUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPerformEdit.undo();
            }
        });
        ivRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPerformEdit.redo();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Graphic.this,"save successful",Toast.LENGTH_SHORT).show();
            }
        });
        rgTabBar.setOnCheckedChangeListener(this);
        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(1);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_keyboard:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_color:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_text:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_style:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
            case R.id.rb_delay:
                vpager.setCurrentItem(PAGE_FIVE);
                break;
        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rbKeyboard.setChecked(true);
                    break;
                case PAGE_TWO:
                    rbColor.setChecked(true);
                    break;
                case PAGE_THREE:
                    rbText.setChecked(true);
                    break;
                case PAGE_FOUR:
                    rbStyle.setChecked(true);
                    break;
                case PAGE_FIVE:
                    rbDelay.setChecked(true);
                    break;
            }
        }
    }
}
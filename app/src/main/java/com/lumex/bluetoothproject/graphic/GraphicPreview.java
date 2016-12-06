package com.lumex.bluetoothproject.graphic;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lumex.bluetoothproject.R;

/**
 * Created by 阿泰Charles on 2016/12/05.
 */

public class GraphicPreview extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private RadioGroup rgTabBar;
    private RadioButton rbText;
    private RadioButton rbColor;
    private RadioButton rbDelay;
    private RadioButton rbKeyboard;
    private RadioButton rbStyle;
    private ViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphic_preview);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rbKeyboard.setChecked(true);
    }

    private void bindViews() {
        rgTabBar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rbText = (RadioButton) findViewById(R.id.rb_text);
        rbColor = (RadioButton) findViewById(R.id.rb_color);
        rbDelay = (RadioButton) findViewById(R.id.rb_delay);
        rbKeyboard = (RadioButton) findViewById(R.id.rb_keyboard);
        rbStyle = (RadioButton) findViewById(R.id.rb_style);
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

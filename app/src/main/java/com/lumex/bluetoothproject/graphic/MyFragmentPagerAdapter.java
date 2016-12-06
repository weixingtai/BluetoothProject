package com.lumex.bluetoothproject.graphic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by 阿泰Charles on 2016/12/05.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 5;
    private PreviewColorFragment colorFragment = null;
    private PreviewDelayFragment delayFragment = null;
    private PreviewKeyboardFragment keyboardFragment = null;
    private PreviewStyleFragment styleFragment = null;
    private PreviewTextFragment textFragment = null;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        colorFragment = new PreviewColorFragment();
        delayFragment = new PreviewDelayFragment();
        keyboardFragment = new PreviewKeyboardFragment();
        styleFragment = new PreviewStyleFragment();
        textFragment = new PreviewTextFragment();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case GraphicPreview.PAGE_ONE:
                fragment = keyboardFragment;
                break;
            case GraphicPreview.PAGE_TWO:
                fragment = colorFragment;
                break;
            case GraphicPreview.PAGE_THREE:
                fragment = textFragment;
                break;
            case GraphicPreview.PAGE_FOUR:
                fragment = styleFragment;
                break;
            case GraphicPreview.PAGE_FIVE:
                fragment = delayFragment;
                break;
        }
        return fragment;
    }


}

package com.lumex.bluetoothproject.configuration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by 阿泰Charles on 2016/12/03.
 */

public class DeviceConfiguration extends AppCompatActivity {
    private ListView lvConnect;
    private  ImageCycleView icvBanner;
    private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
    private String[] bluetoothItem = {"通讯连接埠(COM1)","通讯连接埠(COM2)","Prolific USB-to-Serial Comm Port(COM5)"};

    private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        lvConnect = (ListView)findViewById(R.id.lv_connect);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.config_list_item,bluetoothItem);
        lvConnect.setAdapter(arrayAdapter);
        lvConnect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(DeviceConfiguration.this,l+" 连接成功！",Toast.LENGTH_SHORT).show();
            }
        });

        for(int i=0;i < imageUrls.length; i ++){
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("top-->" + i);
            infos.add(info);
        }
        icvBanner = (ImageCycleView)findViewById(R.id.config_banner);
        icvBanner.setImageResources(infos,mBannerCycleViewListener);
    }
    private ImageCycleView.ImageCycleViewListener mBannerCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }

        @Override
        public void onImageClick(ADInfo info, int postion, View imageView) {
            Toast.makeText(DeviceConfiguration.this, "content->"+info.getContent(), Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        icvBanner.startImageCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        icvBanner.pushImageCycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        icvBanner.pushImageCycle();
    }
}

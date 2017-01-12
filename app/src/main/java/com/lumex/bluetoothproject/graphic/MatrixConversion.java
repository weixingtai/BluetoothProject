package com.lumex.bluetoothproject.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.ImageView;

import static com.lumex.bluetoothproject.util.common.NumberConversion.BToH;

/**
 * Created by 阿泰Charles on 2017/01/11.
 */

public class MatrixConversion {

    public static String BmpToMatrix(ImageView imageView,int width, int height, int startX, int startY, String text, Typeface font, float size, int color){
        System.out.println("text: "+text+" font: "+font+" size: "+size+" color: "+color);
        String s="";
        String string="";
        String textSize = size+"px";
        //新建一张指定大小的画布
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //设置画笔的参数
        Paint paint = new Paint();
        paint.setTypeface(font);
        paint.setTextSize(size);
        paint.setColor(color);
        canvas.drawText(text,startX,startY,paint);
        imageView.setImageBitmap(bitmap);
        int[] pixels = new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(pixels,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());

        int []matrix = new int[bitmap.getWidth()*bitmap.getHeight()];
        String aa = "";
        for(int i = 0; i < pixels.length; i++){
            int clr = pixels[i];

            if (i%32==0){
                aa= aa+clr+"\n";
            }else{
                aa= aa+clr;
            }
            //System.out.println(aa);
            if (clr==0){
                matrix[i]=0;

            }else{
                matrix[i]=1;
            }
        }
        for (int j = 0;j<pixels.length;j++){
            s=s+matrix[j];
        }
        String[] arr = new String[s.length()/8];
        for (int k=0;k<s.length();k++){
            string =string + matrix[k];
            if ((k+1)%8==0){
                arr[k/8]=BToH(string);
                string="";
            }
        }

        String command = "Matrix["+arr.length+"] = {";
        for (int i=0;i<arr.length;i++){

            if (i==arr.length-1){
                command = command +arr[i]+"}";
            }else if ((i+1)%32==0){
                command = command +arr[i]+",\n";
            }else {
                command = command +arr[i]+",";
            }

        }
        System.out.println(command);
        return command;
    }
}

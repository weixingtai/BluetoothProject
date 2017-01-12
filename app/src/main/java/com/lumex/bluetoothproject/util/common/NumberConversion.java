package com.lumex.bluetoothproject.util.common;

/**
 * Created by 阿泰Charles on 2017/01/11.
 * 进制转换类
 */

public class NumberConversion {

    //十六进制转二进制
    public static String HToB(String a) {
        String b = Integer.toBinaryString(Integer.valueOf(toD(a, 16)));
        return b;
    }
    // 二进制转十六进制
    public static String BToH(String a) {
// 将二进制转为十进制再从十进制转为十六进制
        String b = Integer.toHexString(Integer.valueOf(toD(a, 2))).toUpperCase();
        String c = "";
        if (b.length()==1){
            c = "0x0"+b;
        }else {
            c = "0x"+b;
        }
        return c;
    }
    // 任意进制数转为十进制数
    public static String toD(String a, int b) {
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1))
                    * Math.pow(b, a.length() - i - 1));
        }
        return String.valueOf(r);
    }
    // 将十六进制中的字母转为对应的数字
    public static int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {
                i = u;
            }
        }
        if (a.equals("A")) {
            i = 10;
        }
        if (a.equals("B")) {
            i = 11;
        }
        if (a.equals("C")) {
            i = 12;
        }
        if (a.equals("D")) {
            i = 13;
        }
        if (a.equals("E")) {
            i = 14;
        }
        if (a.equals("F")) {
            i = 15;
        }
        return i;
    }


    // 将十进制中的数字转为十六进制对应的字母
    public static String formattingH(int a) {
        String i = String.valueOf(a);
        switch (a) {
            case 10: i = "A";
                break;
            case 11: i = "B";
                break;
            case 12: i = "C";
                break;
            case 13: i = "D";
                break;
            case 14: i = "E";
                break;
            case 15: i = "F";
                break;
        }
        return i;
    }
}

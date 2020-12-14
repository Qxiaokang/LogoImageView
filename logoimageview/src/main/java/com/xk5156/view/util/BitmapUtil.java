package com.xk5156.view.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xk5156.view.App;

/**
 * date: 2020/11/27
 * description: 图片处理工具类
 * version: 1.0
 * @author xk
 */
public class BitmapUtil {
    public static BitmapUtil bitmapUtil=null;
    private BitmapUtil(){

    }
    public static BitmapUtil getInstance(){
        if(bitmapUtil==null){
            synchronized (BitmapUtil.class){
                if(bitmapUtil==null){
                    synchronized (BitmapUtil.class){
                        bitmapUtil=new BitmapUtil();
                    }
                }
            }
        }
        return bitmapUtil;
    }
    /**
     * 从sdcard获取bitmapDrawable
     *
     * @param fileName 文件名
     */
    public Bitmap getLogoBitmapFromFile(String fileName) {
        String jpegName = App.getLogoPath() + fileName;
        Bitmap bp=null;
        try {
            bp= BitmapFactory.decodeFile(jpegName);
//            bp=convertBitmapToGrayscale(bp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bp;
    }
}

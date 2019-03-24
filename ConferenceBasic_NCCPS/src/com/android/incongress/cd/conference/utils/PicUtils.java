package com.android.incongress.cd.conference.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.incongress.cd.conference.adapters.NewHotListAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jacky on 2016/4/18.
 * 图片压缩工具类
 */
public class PicUtils {

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 960, 1600);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static String saveFile(Bitmap bm) throws IOException {
        String path = getSDPath() +"/uploadTemp/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + "temp_"+ System.currentTimeMillis() + ".jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        return myCaptureFile.getAbsolutePath();
    }

    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
    //获取图片的比列
    public static void getViewWidth(ImageView imageView,int proportion){

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        imageView.measure(w, h);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) imageView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

        linearParams.height = imageView.getMeasuredWidth()/proportion;

        imageView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }
    //drawable 着色
    public static void setImageViewColor(ImageView view, int colorResId) {
        //mutate()
        Drawable modeDrawable = view.getDrawable().mutate();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList = ColorStateList.valueOf(view.getResources().getColor(colorResId));
        DrawableCompat.setTintList(temp, colorStateList);
        view.setImageDrawable(temp);
    }
    //使用glide简单加载url图片
    public static void loadImageUrl(Context context, String url, ImageView view){
        Glide.with(context).load(url).error(R.drawable.img_error).placeholder(R.drawable.default_loading).into(view);
    }
    //使用glide简单加载file图片
    public static void loadImageFile(Context context, File file, ImageView view){
        Glide.with(context).load(file).error(R.drawable.img_error).placeholder(R.drawable.default_loading).into(view);
    }
    //使用glide简单加载file图片首页加载
    public static void loadHomeImageFile(Context context, File file, ImageView view){
        Glide.with(context).load(file).error(R.drawable.img_error).into(view);
    }
    public static void loadCircleImage(Context context,String url,final ImageView view){
        Glide.with(context).load(url)
                .placeholder(R.drawable.professor_default)
                .error(R.drawable.professor_default)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        view.setImageDrawable(resource);
                    }
                });
    }
    public static void loadLogoCircleImage(Context context,String url,final ImageView view){
        Glide.with(context).load(url)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        view.setImageDrawable(resource);
                    }
                });
    }

}

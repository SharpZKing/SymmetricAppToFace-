package com.sharp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zjfsharp on 2017/2/21.
 */
public class ToolUtils {

    /**
     * 将图片转成字符串进行存储
     * @param mContext
     * @param mProfile
     */
    public static void putImageToShare(FragmentActivity mContext, CircleImageView mProfile) {
        BitmapDrawable drawable = (BitmapDrawable) mProfile.getDrawable();

        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream osa = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,osa);

        byte[] byteArray = osa.toByteArray();
        /*Log.d("DESTORY",byteArray.length+"====");*/
        String imageStr = new String(Base64.encodeToString(byteArray,Base64.DEFAULT));
        ShareUtils.putString(mContext,"image_title",imageStr);
    }

    public static void getImageFromShareToImageView(FragmentActivity mContext, CircleImageView mProfile){
        String imgString = ShareUtils.getString(mContext, "image_title", "");
        if (imgString != null && !imgString.equals("")){
            byte[] bis = Base64.decode(imgString,Base64.DEFAULT);
            ByteArrayInputStream byStream = new ByteArrayInputStream(bis);

            Bitmap bitmap = BitmapFactory.decodeStream(byStream);
            mProfile.setImageBitmap(bitmap);
        }
    }


    public static void setFontType(Context mContext,TextView mSplash) {
        //Log.d("SPLASH", "setFontType: "+mContext.getAssets().toString());
        Typeface tf =  Typeface.createFromAsset(mContext.getAssets(),"fonts/FONT.TTF");
        mSplash.setTypeface(tf);
    }
}

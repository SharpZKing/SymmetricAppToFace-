package com.sharp.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zjfsharp on 2017/2/21.
 */
public class ShareUtils {


    private static  final String NAME = "FaceDtc";

    public static void putString(Context mContext,String key, String value){
        SharedPreferences sp  = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    public static String getString(Context mContext, String key, String defaultValue){
        SharedPreferences sp  = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getString(key,defaultValue);
    }

    public static void putInt(Context mContext,String key, int value){
        SharedPreferences sp  = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    public static int getInt(Context mContext, String key, int defaultValue){
        SharedPreferences sp  = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,defaultValue);
    }

    public static void putBoolean(Context mContext,String key, boolean value){
        SharedPreferences sp  = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static Boolean getBoolean(Context mContext, String key, boolean defaultValue){
        SharedPreferences sp  = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defaultValue);
    }

    public static void delOne(Context mContext, String key){
        mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    public static void delAll(Context mContext){
        mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }

}

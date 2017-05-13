package com.sharp.facedtc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.Setting;
import com.sharp.fragment.AccountFragment;
import com.sharp.fragment.HisdatasFragment;
import com.sharp.fragment.RecognizeFragment;


import java.lang.reflect.Field;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private LinearLayout mLlAccount;
    private LinearLayout mLlRecognize;
    private LinearLayout mLlHisdatas;

    private AccountFragment mAccountFragment;
    private RecognizeFragment mRecognizeFragment;
    private HisdatasFragment mHisdatasFragment;

    private TextView mAccountTextView;
    private TextView mRecognizeTextView;
    private TextView mHisdatasTextView;

    private int colorPressed;
    private int colorNormal;

    private int showIndex;
    private String SAVE_STATE_INDEX = "SAVE_STATE_INDEX";

    private final static int CHECK_INTERNET_PERMISSION = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initTop();
        initView();
        resetTextColor();
        mRecognizeTextView.setTextColor(colorPressed);
        if (savedInstanceState==null){
            Log.d("SAVE_STATE","normal");
            setSelected(1);
        }else{
            Log.d("SAVE_STATE","exception");
            int index = savedInstanceState.getInt(SAVE_STATE_INDEX);
            //Fragment fragment = getSupportFragmentManager().findFragmentById(index);
            //getSupportFragmentManager().beginTransaction().show(fragment);
            //Fragment fragment1,fragment2,fragment3;
            mAccountFragment = (AccountFragment) getSupportFragmentManager().findFragmentByTag(AccountFragment.class.getName());
            mRecognizeFragment = (RecognizeFragment) getSupportFragmentManager().findFragmentByTag(RecognizeFragment.class.getName());
            mHisdatasFragment =(HisdatasFragment) getSupportFragmentManager().findFragmentByTag(HisdatasFragment.class.getName());
            hideFragments(getSupportFragmentManager().beginTransaction());
            resetTextColor();
            if(index == 0){
                Log.d("SAVE_STATE","INDEX"+0);
                getSupportFragmentManager().beginTransaction().show(mAccountFragment).hide(mRecognizeFragment).hide(mHisdatasFragment).commit();
                mAccountTextView.setTextColor(colorPressed);
            }else if(index == 1){
                Log.d("SAVE_STATE","INDEX"+1);
                getSupportFragmentManager().beginTransaction().show(mRecognizeFragment).hide(mAccountFragment).hide(mHisdatasFragment).commit();
                mRecognizeTextView.setTextColor(colorPressed);
            }else if(index == 2){
                Log.d("SAVE_STATE","INDEX"+2);
                getSupportFragmentManager().beginTransaction().show(mHisdatasFragment).hide(mAccountFragment).hide(mRecognizeFragment).commit();
                mHisdatasTextView.setTextColor(colorPressed);
            }

        }


        Setting.setShowLog(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_INDEX,showIndex);
    }

    /**
     * 重新设置状态栏
     */
    protected void setStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final ViewGroup viewGroup = (ViewGroup) findViewById(R.id.topbar);
            final int statusHeight = getStatusHeight();
            viewGroup.post(new Runnable() {
                @Override
                public void run() {
                    int topBarHeight = viewGroup.getHeight();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewGroup.getLayoutParams();
                    layoutParams.height = statusHeight + topBarHeight;
                    viewGroup.setLayoutParams(layoutParams);
                }
            });
        }
    }

    /**
     * 得到状态栏高度
     * @return
     */
    protected int getStatusHeight(){
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object object = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(object).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return  0;
    }

    /**
     * 自定义Topbar 后处理顶栏
     */
    private void initTop(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initView(){
        mLlAccount = (LinearLayout) findViewById(R.id.ll_account);
        mLlAccount.setOnClickListener(this);

        mLlRecognize = (LinearLayout) findViewById(R.id.ll_recognize);
        mLlRecognize.setOnClickListener(this);

        mLlHisdatas = (LinearLayout) findViewById(R.id.ll_hisdatas);
        mLlHisdatas.setOnClickListener(this);

        mAccountTextView = (TextView) findViewById(R.id.tv_account);
        mRecognizeTextView = (TextView) findViewById(R.id.tv_recognize);
        mHisdatasTextView = (TextView) findViewById(R.id.tv_hisdatas);

        colorPressed = getResources().getColor(R.color.colorPressed);
        colorNormal = getResources().getColor(R.color.colorNormal);
    }


    /**
     * 隐藏不需要显示的fragment
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction){
        if (mAccountFragment!=null){
            transaction.hide(mAccountFragment);
        }
        if (mRecognizeFragment!=null){
            transaction.hide(mRecognizeFragment);
        }
        if (mHisdatasFragment!=null){
            transaction.hide(mHisdatasFragment);
        }
    }

    /**
     * 判断要切换到哪个导航页面
     * @param i
     */
    private void setSelected(int i){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        hideFragments(transaction);

        switch (i){
            case 0:
                if (mAccountFragment==null){
                    mAccountFragment = new AccountFragment();
//                    transaction.add(R.id.fl_container, mAccountFragment);
                    transaction.add(R.id.fl_container,mAccountFragment,mAccountFragment.getClass().getName());
                }else {
                    transaction.show(mAccountFragment);
                }
                showIndex = 0;
                break;
            case 1:
                if (mRecognizeFragment==null){
                    mRecognizeFragment = new RecognizeFragment();
//                    transaction.add(R.id.fl_container, mRecognizeFragment);
                    transaction.add(R.id.fl_container,mRecognizeFragment,mRecognizeFragment.getClass().getName());
                }else {
                    transaction.show(mRecognizeFragment);
                }
                showIndex = 1;
                break;

            case 2:
                if (mHisdatasFragment==null){
                    mHisdatasFragment = new HisdatasFragment();
//                    transaction.add(R.id.fl_container, mHisdatasFragment);
                    transaction.add(R.id.fl_container, mHisdatasFragment, mHisdatasFragment.getClass().getName());
                }else {
                    transaction.show(mHisdatasFragment);

                }
                showIndex = 2;
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 导航页面切换
     * @param v
     */
    @Override
    public void onClick(View v) {
        //Intent intent = null;
        resetTextColor();
        switch (v.getId()) {
            case R.id.ll_account:
                mAccountTextView.setTextColor(colorPressed);
                setSelected(0);
                break;
            case R.id.ll_recognize:
                mRecognizeTextView.setTextColor(colorPressed);
                setSelected(1);
                break;
            case R.id.ll_hisdatas:
                mHisdatasTextView.setTextColor(colorPressed);
                setSelected(2);
                break;
            default:
                break;
        }
    }

    private void resetTextColor(){
        mAccountTextView.setTextColor(colorNormal);
        mAccountTextView.setAlpha(0.9F);
        mRecognizeTextView.setTextColor(colorNormal);
        mRecognizeTextView.setAlpha(0.9F);
        mHisdatasTextView.setTextColor(colorNormal);
        mHisdatasTextView.setAlpha(0.9F);

    }

}

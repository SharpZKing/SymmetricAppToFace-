package com.sharp.facedtc;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sharp.util.ShareUtils;
import com.sharp.util.ToolUtils;

public class SplashActivity extends AppCompatActivity {

    private TextView mSplash;

    private static  final int HANDLER_SPLASH = 2000;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int tag = msg.what;
            switch (tag){
                case HANDLER_SPLASH :

                    if (isFirst()){
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                    /*startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();*/
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();


    }

    private void initView() {

        handler.sendEmptyMessageDelayed(HANDLER_SPLASH, 3000);

        mSplash = (TextView) findViewById(R.id.tv_splash);
        ToolUtils.setFontType(this,mSplash);
    }

    private boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this, "ISFIRST", true);
        if (isFirst){
            ShareUtils.putBoolean(this,  "ISFIRST", false);
            return true;
        }else{
            return false;
        }

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}

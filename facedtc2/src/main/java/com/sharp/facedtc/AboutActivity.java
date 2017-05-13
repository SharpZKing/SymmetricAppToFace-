package com.sharp.facedtc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharp.util.EncodingUtils;

public class AboutActivity extends Activity {

    private TextView mTitle;
    private ImageView mAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);

        initTop();

        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("关于我们");

        mAbout  = (ImageView) findViewById(R.id.show_about);

//        String content = "上海大学 -- 宋安平团队";
        String content = "三人小队 --- 华为创想杯我们来了！";
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(content,300,300, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_l));

        mAbout.setImageBitmap(qrCodeBitmap);

    }

    /**
     * 自定义Topbar 后处理顶栏
     */
    private void initTop(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}

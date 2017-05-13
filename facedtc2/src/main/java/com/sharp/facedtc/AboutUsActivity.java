package com.sharp.facedtc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.sharp.util.EncodingUtils;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView mAboutUsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mAboutUsImg = (ImageView) findViewById(R.id.show_about_us);

        String content = "上海大学 -- 宋安平团队";
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(content,300,300, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        mAboutUsImg.setImageBitmap(qrCodeBitmap);

    }
}

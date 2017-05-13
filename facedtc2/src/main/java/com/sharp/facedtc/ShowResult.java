package com.sharp.facedtc;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.sharp.beans.FaceResult;
import com.sharp.beans.SaveFaceResult;
import com.sharp.util.ShareUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShowResult extends Activity {

    FaceResult fr;

    private ImageView star4;
    private ImageView star5;
    private TextView mTimeRe;
    private TextView mAsymRe;
    private TextView mBrowRe;
    private TextView mEyeRe;
    private TextView mNoseRe;
    private TextView mMouRe;
    private TextView mConmentRe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_result);

        initTop();
        initView();
        initDatas();
    }

    private void initView() {

        mTimeRe = (TextView) findViewById(R.id.tv_time_re);
        mAsymRe = (TextView) findViewById(R.id.tv_asym_re);
        mEyeRe = (TextView) findViewById(R.id.tv_eye_re);
        mBrowRe = (TextView) findViewById(R.id.tv_eyebrow_re);
        mNoseRe = (TextView) findViewById(R.id.tv_nose_re);
        mMouRe = (TextView) findViewById(R.id.tv_mouth_re);
        mConmentRe = (TextView) findViewById(R.id.tv_comment_re);

        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
    }

    private void initDatas() {
        if (!ShareUtils.getString(ShowResult.this,"userId","-1").equals("-1")){

            List<SaveFaceResult> sfrList = DataSupport.where(" userid = ? and asymtime = ? ",
                    ShareUtils.getString(ShowResult.this,"userId","-1"),
                    (new SimpleDateFormat("yyyyMMdd")).format(new Date())).find(SaveFaceResult.class);

            if (sfrList!=null && sfrList.size()!=0){
                Log.d("TODAY","today");
                double faceAsym = 0.0;
                double faceEyeAngle = 0.0;
                double faceBrowAngle = 0.0;
                double faceNodeAngle = 0.0;
                double faceMouthAngle = 0.0;
                SaveFaceResult sfr  = null;
                for (int k=0; k<sfrList.size(); k++){
                    sfr = sfrList.get(k);
                    faceAsym += sfr.getAsymface();
                    faceBrowAngle += sfr.getEyebrowangle();
                    faceEyeAngle += sfr.getEyeangle();
                    faceNodeAngle += sfr.getNoseangle();
                    faceMouthAngle += sfr.getMouthangle();
                }

                mTimeRe.setText( (new SimpleDateFormat("yyyyMMdd")).format(new Date()) );
                mAsymRe.setText(formatDouble(faceAsym/sfrList.size())+"%");
                mBrowRe.setText( faceBrowAngle/sfrList.size()<1? "0"+formatDouble(faceBrowAngle/sfrList.size()):formatDouble(faceBrowAngle/sfrList.size()));
                mEyeRe.setText( faceEyeAngle/sfrList.size()<1?"0"+formatDouble(faceEyeAngle/sfrList.size()):formatDouble(faceEyeAngle/sfrList.size()) );
                mNoseRe.setText(faceNodeAngle/sfrList.size()<1?"0"+formatDouble(faceNodeAngle/sfrList.size()):formatDouble(faceNodeAngle/sfrList.size()) );
                mMouRe.setText(faceMouthAngle/sfrList.size()<1?"0"+formatDouble(faceMouthAngle/sfrList.size()):formatDouble(faceMouthAngle/sfrList.size()));


                int tag = 3;

                if (faceAsym/sfrList.size()<=70){
                    mConmentRe.setText("对称度偏低，请确认周边光照均匀下重测");
                }else if (sfr.getAsymface()<80){
                    mConmentRe.setText("对称度有待偏低有待改善");
                    tag = 4;
                }else if (sfr.getAsymface()<85){
                    mConmentRe.setText("对称度非常棒");
                    tag = 5;
                }else if (sfr.getAsymface()<95){
                    mConmentRe.setText("对称度优秀");
                    tag = 6;
                }else{
                    mConmentRe.setText("对称度接近完美Good！");
                    tag = 7;
                }
                if (tag == 4){
                    star4.setImageResource(R.drawable.half16);
                }

                if (tag == 5){
                    star4.setImageResource(R.drawable.red16);
                }

                if (tag == 6){
                    star4.setImageResource(R.drawable.red16);
                    star5.setImageResource(R.drawable.half16);
                }
                if (tag == 7){
                    star4.setImageResource(R.drawable.red16);
                    star5.setImageResource(R.drawable.red16);
                }


            }else{
                Toast.makeText(ShowResult.this, "登入后查看具体检测结果，请登入",Toast.LENGTH_SHORT).show();
            }


        }
    }

    private String formatDouble(double d){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
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

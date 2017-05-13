package com.sharp.facedtc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.sharp.beans.FaceResult;
import com.sharp.beans.SaveFaceResult;
import com.sharp.util.CalResult;
import com.sharp.util.ShareUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Result1Activity extends FragmentActivity implements View.OnClickListener {

    private ImageView mResultImageView;
    private String jsonStr;

    private TextView faceAsymTv, browTv, eyeTv, noseTv, mouthTv;

    private Button mSaveUpload;
    private Button mStepNext;

    private String step;

    String filepath ;



    //更新主线程UI
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            FaceResult fr = (FaceResult) msg.obj;
            faceAsymTv.setText(fr.getFaceAsym()+"");
            browTv.setText(fr.getBrowAngle()+"");
            eyeTv.setText(fr.getEyeAngle()+"");
            noseTv.setText(fr.getNoseAngle()+"");
            mouthTv.setText(fr.getMouthAngle()+"");

            if (!ShareUtils.getString(Result1Activity.this,"userId","-1").equals("-1")){

                List<SaveFaceResult> sfrList = DataSupport.where(" userid = ? and asymtime = ? and action = ? ",
                        ShareUtils.getString(Result1Activity.this,"userId","-1"),
                        step,(new SimpleDateFormat("yyyyMMdd")).format(new Date())).find(SaveFaceResult.class);

                if (sfrList!=null && sfrList.size()!=0){

                    SaveFaceResult sfr = sfrList.get(0);
                    sfr.setAsymface(fr.getFaceAsym());

                    sfr.setEyeangle(fr.getEyeAngle());
                    sfr.setMouthangle(fr.getMouthAngle());
                    sfr.setNoseangle(fr.getNoseAngle());
                    sfr.setEyebrowangle(fr.getBrowAngle());
                    sfr.setXunfeijs(jsonStr);

                    sfr.update(sfr.getId());

                    Toast.makeText(Result1Activity.this, "update success", Toast.LENGTH_SHORT).show();


                }else{


                    SaveFaceResult sfr = new SaveFaceResult();

                    sfr.setUserid(Integer.valueOf(ShareUtils.getString(Result1Activity.this,"userId","-1")));
                    int p = 0;
                    if (step.equals("STEP_ONE")){
//                    sfr.setAction(1);
                        p = 1;
                    }else if (step.equals("STEP_TWO")){
//                    sfr.setAction(2);
                        p = 2;
                    }else if (step.equals("STEP_THREE")){
//                    sfr.setAction(3);
                        p = 3;
                    }else if (step.equals("STEP_FOUR")){
//                    sfr.setAction(4);
                        p = 4;
                    }else if (step.equals("STEP_FIVE")){
//                    sfr.setAction(5);
                        p = 5;
                    }else if (step.equals("STEP_SIX")){
//                    sfr.setAction(6);
                        p = 6;
                    }
                    if(p!=0){
                        sfr.setAction(p);
                    }
                    sfr.setAsymface(fr.getFaceAsym());

                    Date date = new Date();
                    String atime = (new SimpleDateFormat("yyyyMMdd")).format(date);
                    sfr.setAsymtime(atime);


                    sfr.setEyeangle(fr.getEyeAngle());
                    sfr.setMouthangle(fr.getMouthAngle());
                    sfr.setNoseangle(fr.getNoseAngle());
                    sfr.setEyebrowangle(fr.getBrowAngle());
                    sfr.setXunfeijs(jsonStr);

                    if(sfr.save()){

                        String url = "http://106.14.74.59/index.php/Home/Index/uploadfileHandle";

                        HttpParams params = new HttpParams();
                        params.put("userid",ShareUtils.getString(Result1Activity.this,"userId","-1"));
                        if (p!=0){
                            params.put("action",p+"");
                        }
                        params.put("asymface",fr.getFaceAsym()+"");
                        params.put("eyeangle",fr.getEyeAngle()+"");
                        params.put("noseangle",fr.getNoseAngle()+"");
                        params.put("mouthangle",fr.getMouthAngle()+"");
                        params.put("eyebrowangle",fr.getBrowAngle()+"");
                        params.put("asymtime",(new SimpleDateFormat("yyyyMMddHHmmss")).format(date).toString());
                        params.put("xunfeiJs",jsonStr);
                        params.put("file",new File(filepath));

                        RxVolley.post(url, params, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
//                            super.onSuccess(t);
                               //Toast.makeText(Result1Activity.this, "success save", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int errorNo, String strMsg) {
                                super.onFailure(errorNo, strMsg);
                                //Log.i("ERROR",strMsg);
                            }
                        });


                    }else{
                        Toast.makeText(Result1Activity.this, "save fail", Toast.LENGTH_SHORT).show();
                    }


                }


            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result1);

        initView();
        initTop();

        //load image
        filepath = getIntent().getStringExtra("r1PicPath");
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(new File(filepath));
            bitmap = BitmapFactory.decodeStream(fis);
            Matrix matrix = new Matrix();
            matrix.setRotate(-90);
            Bitmap bmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            mResultImageView.setImageBitmap(bmp);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //calculate result
        jsonStr = getIntent().getStringExtra("pointXY");
        new CalResult(bitmap, handler, jsonStr).start();
    }

    private void initView(){

        Intent intent = getIntent();
        step = intent.getStringExtra("step_result");

        mResultImageView  = (ImageView)findViewById(R.id.result1_image);
        faceAsymTv = (TextView) findViewById(R.id.tv_faceAsym);
        browTv = (TextView) findViewById(R.id.tv_browangle);
        eyeTv = (TextView) findViewById(R.id.tv_eyeangle);
        noseTv = (TextView) findViewById(R.id.tv_noseangle);
        mouthTv = (TextView) findViewById(R.id.tv_mouthangle);
        if (step.equals("STEP_ONE")){
            ((TextView)findViewById(R.id.title)).setText("动作一测试结果");
        }else if (step.equals("STEP_TWO")){
            ((TextView)findViewById(R.id.title)).setText("动作二测试结果");
        }else if (step.equals("STEP_THREE")){
            ((TextView)findViewById(R.id.title)).setText("动作三测试结果");
        }else if (step.equals("STEP_FOUR")){
            ((TextView)findViewById(R.id.title)).setText("动作四测试结果");
        }else if (step.equals("STEP_FIVE")){
            ((TextView)findViewById(R.id.title)).setText("动作五测试结果");
        }else if (step.equals("STEP_SIX")){
            ((TextView)findViewById(R.id.title)).setText("动作六测试结果");
        }

        mSaveUpload = (Button) findViewById(R.id.save_upload);
        mSaveUpload.setOnClickListener(this);
        mStepNext = (Button) findViewById(R.id.tip_step_next);
        mStepNext.setOnClickListener(this);
        if (step.equals("STEP_SIX")){
            mStepNext.setText("查看所有结果");
        }else{
            mStepNext.setText("下一动作");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_upload:
                Toast.makeText(Result1Activity.this, "success save", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tip_step_next:
                if (!step.equals("STEP_SIX")){
                    Intent intent = new Intent(Result1Activity.this, ActionTip.class);
                    changeStep();
                    intent.putExtra("step",step);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "六次动作已全部检测完毕", Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(Result1Activity.this, ShowResult.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private void changeStep() {
        if (step.equals("STEP_ONE")){
            step = "STEP_TWO";
        }else if (step.equals("STEP_TWO")){
            step = "STEP_THREE";
        }else if (step.equals("STEP_THREE")){
            step = "STEP_FOUR";
        }else if (step.equals("STEP_FOUR")){
            step = "STEP_FIVE";
        }else if (step.equals("STEP_FIVE")){
            step = "STEP_SIX";
        }
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

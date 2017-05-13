package com.sharp.facedtc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ActionTip extends FragmentActivity implements View.OnClickListener{

    private TextView titleTv ;
    private Button btnGo;

    private String step = "STEP_ONE";

    private final static int CHECK_CAMERA_PERMISSION = 1010;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int code = msg.what;
            if (code==1 || code==2){
                Intent intent = new Intent(ActionTip.this, VideoDemo.class);
                intent.putExtra("video_step",step);
                startActivity(intent);
                finish();
            }else if(code==3){
                ActivityCompat.requestPermissions(ActionTip.this,new String[]{Manifest.permission.CAMERA},CHECK_CAMERA_PERMISSION);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initTop();

        Intent intent = getIntent();
        step = intent.getStringExtra("step");
        if (step.equals("STEP_ONE")){
            setContentView(R.layout.activity_action_tip);
            initView("STEP_ONE");
        }else if (step.equals("STEP_TWO")){
            setContentView(R.layout.activity_action_tip2);
            initView("STEP_TWO");
        }else if (step.equals("STEP_THREE")){
            setContentView(R.layout.activity_action_tip3);
            initView("STEP_THREE");
        }else if (step.equals("STEP_FOUR")){
            setContentView(R.layout.activity_action_tip4);
            initView("STEP_FOUR");
        }else if (step.equals("STEP_FIVE")){
            setContentView(R.layout.activity_action_tip5);
            initView("STEP_FIVE");
        }else if (step.equals("STEP_SIX")){
            setContentView(R.layout.activity_action_tip6);
            initView("STEP_SIX");
        }

    }

    private void initView(String stepTip){
        if (stepTip.equals("STEP_ONE")){
            titleTv = (TextView)findViewById(R.id.title);
            titleTv.setText("动作一提示");

            btnGo = (Button)findViewById(R.id.btn_video_demo);
            btnGo.setOnClickListener(this);
        }
        if (stepTip.equals("STEP_TWO")){
            titleTv = (TextView)findViewById(R.id.title);
            titleTv.setText("动作二提示");

            btnGo = (Button)findViewById(R.id.btn_video_2);
            btnGo.setOnClickListener(this);
        }
        if (stepTip.equals("STEP_THREE")){
            titleTv = (TextView)findViewById(R.id.title);
            titleTv.setText("动作三提示");

            btnGo = (Button)findViewById(R.id.btn_video_3);
            btnGo.setOnClickListener(this);
        }
        if (stepTip.equals("STEP_FOUR")){
            titleTv = (TextView)findViewById(R.id.title);
            titleTv.setText("动作四提示");

            btnGo = (Button)findViewById(R.id.btn_video_4);
            btnGo.setOnClickListener(this);
        }
        if (stepTip.equals("STEP_FIVE")){
            titleTv = (TextView)findViewById(R.id.title);
            titleTv.setText("动作五提示");

            btnGo = (Button)findViewById(R.id.btn_video_5);
            btnGo.setOnClickListener(this);
        }

        if (stepTip.equals("STEP_SIX")){
            titleTv = (TextView)findViewById(R.id.title);
            titleTv.setText("动作六提示");

            btnGo = (Button)findViewById(R.id.btn_video_6);
            btnGo.setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_video_demo:
            case R.id.btn_video_2:
            case R.id.btn_video_3:
            case R.id.btn_video_4:
            case R.id.btn_video_5:
            case R.id.btn_video_6:


                judegeCameraPermission();


                /*Intent intent = new Intent(ActionTip.this, VideoDemo.class);
                intent.putExtra("video_step",step);
                startActivity(intent);
                finish();*/
                break;
        }
    }

    private void judegeCameraPermission() {
        if(Build.VERSION.SDK_INT>=23){
            int checkPermissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if(checkPermissionCamera!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CHECK_CAMERA_PERMISSION);
                return;
            }else{
                handler.sendEmptyMessageDelayed(2,500);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case CHECK_CAMERA_PERMISSION:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(this, "摄像头权限请求成功",Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(1,500);
                }else{
                    /*Log.d("Camera",grantResults[0]+"===");
                    Log.d("Camera",PackageManager.PERMISSION_DENIED+"----");

                    Toast.makeText(this, "请务必开启摄像头权限，否则无法进行检测",Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(3,100);*/
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

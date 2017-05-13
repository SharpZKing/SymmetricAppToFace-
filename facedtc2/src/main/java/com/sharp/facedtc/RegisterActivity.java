package com.sharp.facedtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private TextView mTitle;

    private EditText mUsername;
    private EditText mPass;
    private EditText mRePass;
    private EditText mRealname;
    private EditText mPhone;
    private RadioGroup mSex;
    private CheckBox mXy;
    private TextView mXyLook;

    private Button mRegister;

    String gender = "男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        initTop();
        initView();
    }

    private void initView() {

        mXy = (CheckBox) findViewById(R.id.cb_xy);
        mXyLook = (TextView) findViewById(R.id.look_xy);
        mXyLook.setOnClickListener(this);


        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("注册");

        mUsername = (EditText) findViewById(R.id.et_username);
        mPass = (EditText) findViewById(R.id.et_password);
        mRePass = (EditText) findViewById(R.id.et_re_password);
        mSex = (RadioGroup) findViewById(R.id.sex);

        mSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male){
                    //                  gender[0] = "男";
                    gender ="男";
                }else if (checkedId == R.id.female){
//                                    gender[0] = "女";
                    gender = "女";
                }
            }
        });

        mRealname = (EditText) findViewById(R.id.et_realname);
        mPhone = (EditText) findViewById(R.id.et_phone);

        mRegister = (Button) findViewById(R.id.register);
        mRegister.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                String username = mUsername.getText().toString().trim();
                String password = mPass.getText().toString().trim();
                String rePassword = mRePass.getText().toString().trim();
                String realname = mRealname.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();

                //final String[] gender = {"男"};

                if(mXy.isChecked()){
                    if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(password)
                            & !TextUtils.isEmpty(rePassword) & !TextUtils.isEmpty(realname) & !TextUtils.isEmpty(phone) ){

                        if (!password.equals(rePassword)){
                            Toast.makeText(this, "两次密码不一致",Toast.LENGTH_SHORT).show();
                        }else{

                            String reg = "^1[3|4|5|8][0-9]\\d{8}$";
                            Pattern pattern = Pattern.compile(reg);
                            Matcher matcher = pattern.matcher(phone);
                            if(matcher.matches()){
                                String url = "http://106.14.74.59/index.php/Home/Index/enrollHandleandroid";

                                HttpParams params = new HttpParams();
                                params.putHeaders("User-Agent","okhttp/2.5.0");
                                params.put("username", username);
                                params.put("password", password);
                                params.put("sex", gender);
                                params.put("name",realname);
                                params.put("phone",phone);


                                RxVolley.post(url, params, new HttpCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                        String judge = parseJson(result.toString());

                                        if (judge == "SUCCESS"){
                                            Toast.makeText(RegisterActivity.this, "注册成功,可以登入了!",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else if (judge == "FAILURE"){
                                            Toast.makeText(RegisterActivity.this, "注册失败，请检查网络后重试!",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int errorNo, String strMsg) {
                                        super.onFailure(errorNo, strMsg);
                                        Toast.makeText(RegisterActivity.this, "Failure: "+strMsg,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(RegisterActivity.this, "请输入正确格式的联系方式",Toast.LENGTH_SHORT).show();

                            }


                        /*String url = "http://106.14.74.59/index.php/Home/Index/enrollHandle";

                        HttpParams params = new HttpParams();
                        params.putHeaders("User-Agent","rxvolley");
                        params.put("username", username);
                        params.put("password", password);
                        params.put("sex", gender);
                        params.put("name",realname);
                        params.put("phone",phone);


                        RxVolley.post(url, params, new HttpCallback() {
                            @Override
                            public void onSuccess(String result) {
                                String judge = parseJson(result);
                                if (judge == "SUCCESS"){
                                    Toast.makeText(RegisterActivity.this, "注册成功,可以登入了",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else if (judge == "FAILURE"){
                                    Toast.makeText(RegisterActivity.this, "注册失败，请检查网络后重试",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(int errorNo, String strMsg) {
                                super.onFailure(errorNo, strMsg);
                                Toast.makeText(RegisterActivity.this, "Failure: "+strMsg,Toast.LENGTH_SHORT).show();
                            }
                        });*/

                        }

                    }else{
                        Toast.makeText(this, "编辑项不能为空",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "请勾选协议后注册",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.look_xy:
                Intent intent = new Intent(RegisterActivity.this, XYAgreeActivity.class);
                startActivity(intent);
                break;
        }
    }

    private String parseJson(String result) {

        String judge = "";

        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString("success");
            System.out.println(code+"code");
            if (code.equals("1")) {
                judge = "SUCCESS";
//
                //judge = "FAILURE";
            }else{
                judge = "FAILURE";
                //judge = "SUCCESS";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return judge;

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


}

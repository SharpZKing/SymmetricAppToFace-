package com.sharp.facedtc;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.sharp.util.ShareUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

public class LoginActivity extends Activity implements View.OnClickListener {

    private TextView mTitle;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initTop();

        initView();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("登    入");

        mUsername = (EditText) findViewById(R.id.et_username_login);
        mPassword = (EditText) findViewById(R.id.et_password_login);
        mLoginBtn = (Button) findViewById(R.id.btnLogin);
        mLoginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:

                final String username = mUsername.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(password)){
                    final String url = "http://106.14.74.59/index.php/Home/Index/loginHandle";

                    HttpParams params = new HttpParams();
                    params.put("username", username);
                    params.put("password", password);

                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpClient client = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost(url);
                            NameValuePair pair1 = new BasicNameValuePair("name", username);
                            NameValuePair pair2 = new BasicNameValuePair("password", password);

                            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
                            pairs.add(pair1);
                            pairs.add(pair2);
                            try {
                                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                                httpPost.setEntity(requestEntity);
                                HttpResponse response = client.execute(httpPost);
                                Log.d("RESULTSSS",response.getStatusLine().getStatusCode()+"----");
                                if(response.getStatusLine().getStatusCode()==200){
                                    HttpEntity entity = response.getEntity();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                                    String result = reader.readLine();
                                    Log.d("RESULTSSS",result);
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }  catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();*/

                    RxVolley.post(url, params, new HttpCallback() {
                        @Override
                        public void onSuccess(String result) {
                            //Log.d("ResultU",result+"===");
                            parseJson(result);
                            finish();
                        }

                        @Override
                        public void onFailure(int errorNo, String strMsg) {
                            super.onFailure(errorNo, strMsg);
                            Toast.makeText(LoginActivity.this, "用户名或密码错误",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(this, "用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString("success");
            if ( "1".equals(code)){
                JSONObject object = jsonObject.getJSONObject("data");
                String id = object.getString("id");
                String username = object.getString("username");
                String name = object.getString("name");
                String phone = object.getString("phone");
                String sex = object.getString("sex");

                if (!TextUtils.isEmpty(id) & !TextUtils.isEmpty(username) &
                        !TextUtils.isEmpty(name) & !TextUtils.isEmpty(phone) & !TextUtils.isEmpty(sex)){
                    ShareUtils.putString(this, "userId", id);
                    ShareUtils.putString(this, "username", username);
                    ShareUtils.putString(this, "realname", name);
                    ShareUtils.putString(this, "phone", phone);
                    ShareUtils.putString(this, "sex", sex);

                    Toast.makeText(this, "登入成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "登入失败",Toast.LENGTH_SHORT).show();
                }

            }else if ("0".equals(code)){
                Toast.makeText(this, "登入失败,用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

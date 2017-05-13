package com.sharp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.sharp.facedtc.AboutActivity;
import com.sharp.facedtc.AboutUsActivity;
import com.sharp.facedtc.LoginActivity;
import com.sharp.facedtc.R;
import com.sharp.facedtc.RegisterActivity;
import com.sharp.facedtc.UpdateActivity;
import com.sharp.util.ShareUtils;
import com.sharp.util.ToolUtils;
import com.sharp.views.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Zjf on 2016/12/26.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    private Button mRegisterBtn;
    private Button mUpdateBtn;
    private Button mLogout;
    private CircleImageView mProfile;

    private Button mCancel;
    private Button mCamera;
    private Button mPicture;

    private CustomDialog dialog;

    private TextView mTvUser;
    private TextView mAboutUs;
    private TextView mUpdateApk;

    private LinearLayout ll_username;
    private LinearLayout ll_phone;
    private LinearLayout ll_sex;
    boolean canSeeInfo = false;

    private EditText mRealname;
    private EditText mPhone;
    private EditText mSex;

    private int versionCode;
    private String versionName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d("MMM","AccountFragment onCreateView");
        getActivity().getWindow().setSoftInputMode(   WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_account,container,false);
        initViews(view);
        return view;

    }

    private void initViews(View view) {
        mRegisterBtn = (Button) view.findViewById(R.id.btnRegister);
        mRegisterBtn.setOnClickListener(this);

        mUpdateBtn = (Button) view.findViewById(R.id.updateBtn);
        mUpdateBtn.setOnClickListener(this);
        mUpdateBtn.setVisibility(View.GONE);
        mProfile = (CircleImageView) view.findViewById(R.id.profile_image);
        mProfile.setOnClickListener(this);
        mLogout = (Button) view.findViewById(R.id.btnLogout);
        mLogout.setOnClickListener(this);

        mTvUser = (TextView) view.findViewById(R.id.tv_user_account);
        mTvUser.setOnClickListener(this);

        mAboutUs = (TextView) view.findViewById(R.id.look_about_us);
        mAboutUs.setOnClickListener(this);

        mUpdateApk = (TextView) view.findViewById(R.id.update_apk);
        mUpdateApk.setOnClickListener(this);

        ll_username = (LinearLayout) view.findViewById(R.id.ll_username);
        ll_phone = (LinearLayout) view.findViewById(R.id.ll_phone);
        ll_sex = (LinearLayout) view.findViewById(R.id.ll_sex);

        mRealname = (EditText) view.findViewById(R.id.et_username_account);
        mPhone = (EditText) view.findViewById(R.id.et_phone_account);
        mSex = (EditText) view.findViewById(R.id.et_sex_account);


        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //提示框以外点击无效
        dialog.setCancelable(false);
        mCamera = (Button) dialog.findViewById(R.id.btn_camera);
        mCamera.setOnClickListener(this);
        mPicture = (Button) dialog.findViewById(R.id.btn_picture);
        mPicture.setOnClickListener(this);
        mCamera = (Button) dialog.findViewById(R.id.btn_cancel);
        mCamera.setOnClickListener(this);

        /*boolean canSeeInfo = canSee();
        if (canSeeInfo){
            setSeeInfo(true);
            setEnables(false);
            mRegisterBtn.setVisibility(View.GONE);

        }else{
            setSeeInfo(false);
            mRegisterBtn.setVisibility(View.VISIBLE);
        }*/

        getAppInfo();

    }

    private void setSeeInfo(boolean b) {
        if (b){
            ll_username.setVisibility(View.VISIBLE);
        }else{
            ll_username.setVisibility(View.GONE);
        }

        if (b){
            ll_phone.setVisibility(View.VISIBLE);
        }else{
            ll_phone.setVisibility(View.GONE);
        }

        if (b){
            ll_sex.setVisibility(View.VISIBLE);
        }else{
            ll_sex.setVisibility(View.GONE);
        }

    }


    private boolean canSee(){
        return ShareUtils.getBoolean(getActivity(),"SEE_INFO",false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
            case R.id.tv_user_account:
                //setEnables(true);
                //mUpdateBtn.setVisibility(View.VISIBLE);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.profile_image:
                if (ShareUtils.getString(getActivity(),"userId","-1")!="-1"){
                    dialog.show();
                }else{
                    Toast.makeText(getActivity(),"请登入后设置头像", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                dialog.dismiss();
                toCamera();
                break;
            case R.id.btn_picture:
                dialog.dismiss();
                toPicture();
                break;
            case R.id.btnLogout:
                ShareUtils.delOne(getActivity(),"image_title");
                ShareUtils.delOne(getActivity(),"userId");
                ShareUtils.delOne(getActivity(),"realname");
                ShareUtils.delOne(getActivity(),"phone");
                ShareUtils.delOne(getActivity(),"sex");
                setSeeInfo(false);
                mLogout.setVisibility(View.GONE);
                mRegisterBtn.setVisibility(View.VISIBLE);
                mProfile.setImageResource(R.drawable.add_pic);
                mTvUser.setText("未登入");
                mTvUser.setEnabled(true);
                break;
            case R.id.look_about_us:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.update_apk:
                RxVolley.get("http://106.14.74.59/index.php/Home/Index/checkUpdate", new HttpCallback() {
                    @Override
                    public void onSuccess(String result) {
                        parseJson(result);
                    }
                });
                break;
        }
    }

    public void parseJson(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            String newVersionName = jsonObject.getString("versionName");
            int newVersionCode = jsonObject.getInt("versionCode");
            if (versionCode<newVersionCode){
                String updateInfo = jsonObject.getString("content")==null?"修复若干bug":jsonObject.getString("content");
                String updateUrl = jsonObject.getString("url")==null?"":jsonObject.getString("url");
                showUpdateDialog(updateInfo,updateUrl);
            }else{
                Toast.makeText(getActivity(),"无新版本更新",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showUpdateDialog(String updateInfo,final String url) {
        new AlertDialog.Builder(getActivity()).setTitle("有新版本了")
                .setMessage(updateInfo).setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), UpdateActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public  static final int REQUEST_IMAGE_CODE = 10001;
    public static final int REQUEST_CAMERA_CODE = 10002;
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int RESULT_REQUEST_CODE = 102;
    public File tempFile = null;


    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        //intent.putExtra("type","image/*");
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_CODE);

    }

    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra("output", PHOTO_IMAGE_FILE_NAME);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME)));

        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=getActivity().RESULT_CANCELED){
            switch (requestCode){
                case REQUEST_CAMERA_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case REQUEST_IMAGE_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }

    }

    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            mProfile.setImageBitmap(bitmap);
        }
        ToolUtils.putImageToShare(getActivity(),mProfile);
    }

    /**
     * 裁剪图片
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.e("E","uri == null");
            return;
        }


        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private void setEnables(boolean b) {
        mRealname.setEnabled(b);
        mPhone.setEnabled(b);
        mSex.setEnabled(b);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("MMM","AccountFragment onResum");


        String userID = ShareUtils.getString(getActivity(),"userId","-1");
        if (userID!=null && !"-1".equals(userID)){
            ShareUtils.putBoolean(getActivity(),"SEE_INFO", true);

        }else{
            ShareUtils.putBoolean(getActivity(),"SEE_INFO", false);
        }


        canSeeInfo = canSee();
        if (canSeeInfo){
            setSeeInfo(true);

            ToolUtils.getImageFromShareToImageView(getActivity(),mProfile);

            String realName = ShareUtils.getString(getActivity(),"realname","");
            String phone = ShareUtils.getString(getActivity(),"phone","");
            String sex = ShareUtils.getString(getActivity(),"sex","");

            mTvUser.setEnabled(false);
            mTvUser.setText(realName);

            if (realName!=null && realName.trim()!=null){
                mRealname.setText(realName);
            }

            if (phone!=null && phone.trim()!=null){
                mPhone.setText(phone);
            }
            sex = sex + "";
//            Log.d("SEXSEX",sex);

            if (sex!=null && sex.trim()!=null){

                if(sex.equals("1")){
                    mSex.setText("男");
                }else if(sex.equals("0")){
                    mSex.setText("女");
                }

            }

            setEnables(false);
            mRegisterBtn.setVisibility(View.GONE);
            mLogout.setVisibility(View.VISIBLE);

        }else{
            setSeeInfo(false);
            mLogout.setVisibility(View.GONE);
            mRegisterBtn.setVisibility(View.VISIBLE);
        }
    }

    public void getAppInfo(){
        PackageManager pm = getActivity().getPackageManager();
        PackageInfo info = null;
        try{
            info = pm.getPackageInfo(getActivity().getPackageName(),0);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        if(info!=null){
            versionCode = info.versionCode;
            versionName = info.versionName;
        }
    }

    @Override
    public void onDestroy() {
       /* Log.d("DESTORY","destory before....");
        ToolUtils.putImageToShare(getActivity(),mProfile);
        Log.d("DESTORY","destory after....");*/
        super.onDestroy();

    }
}

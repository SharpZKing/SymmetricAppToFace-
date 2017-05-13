package com.sharp.facedtc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.util.Accelerometer;
import com.sharp.util.FaceRect;
import com.sharp.util.FaceUtil;
import com.sharp.util.ParseResult;


public class VideoDemo extends Activity{


	private final static String TAG = VideoDemo.class.getSimpleName();
	private SurfaceView mPreviewSurface;
	private SurfaceView mFaceSurface;
	private Camera mCamera;
	private int mCameraId = CameraInfo.CAMERA_FACING_FRONT;

	// Camera nv21格式预览帧的尺寸，默认设置640*480
	private int PREVIEW_WIDTH = 640;
	private int PREVIEW_HEIGHT = 480;

	// 预览帧数据存储数组和缓存数组
	private byte[] nv21;
	private byte[] buffer;
	private  int[] rgb;
	// 缩放矩阵
	private Matrix mScaleMatrix = new Matrix();

	private Accelerometer mAcc;

	private FaceDetector mFaceDetector;
	private boolean mStopTrack;
	private Toast mToast;
	private long mLastClickTime;
	private int isAlign = 1;

	private String mPicPath;

	private Button capture;
	private String btnResult = "";

	private Handler handler = new Handler();

	private TextView mTipTv;
	private String step = "STEP_ONE";

	/*Camera.PictureCallback mPictureCallback = new Camera.PictureCallback(){

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(new File(mPicPath));
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Intent intent = new Intent(VideoDemo.this,Result1Activity.class);
			intent.putExtra("r1PicPath",mPicPath);
			intent.putExtra("pointXY",btnResult);
			mStopTrack = true;

			//decodeYUV420SP(buffer,PREVIEW_WIDTH,PREVIEW_HEIGHT);
			new Thread(new Runnable() {
				@Override
				public void run() {
					decodeYUV420SP(buffer,PREVIEW_WIDTH,PREVIEW_HEIGHT);
				}
			}).start();

			startActivity(intent);
			VideoDemo.this.finish();

		}
	};*/


	/**
	 * 拍照
	 * @param view
     */
	public void capture(View view){
		/*Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPictureFormat(ImageFormat.JPEG);

		List<Camera.Size> sizeList = parameters.getSupportedPictureSizes();
		if (sizeList.size()>1){
			Iterator<Camera.Size> itor = sizeList.iterator();
			while (itor.hasNext()){
				Camera.Size cur = itor.next();
				Log.d("support", cur.width+"==="+cur.height);
			}
		}

		parameters.setPreviewSize(PREVIEW_WIDTH,PREVIEW_HEIGHT);

		parameters.setPictureSize(1280,720);
		parameters.set("jpeg-quality", 85);
		mCamera.setParameters(parameters);

		mCamera.autoFocus(new Camera.AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if (success){
					int direction = Accelerometer.getDirection();
					boolean frontCamera = (CameraInfo.CAMERA_FACING_FRONT == mCameraId);
					if (frontCamera) {
						direction = (4 - direction)%4;
					}
					if(mFaceDetector == null) {
						showTip("本SDK不支持离线视频流检测");
					}
					String result = mFaceDetector.trackNV21(buffer, PREVIEW_WIDTH, PREVIEW_HEIGHT, isAlign, direction);
					btnResult = result;
					mCamera.takePicture(null,null,mPictureCallback);
				}

			}
		});*/

		int direction = Accelerometer.getDirection();
		boolean frontCamera = (CameraInfo.CAMERA_FACING_FRONT == mCameraId);
		if (frontCamera) {
			direction = (4 - direction)%4;
		}
		if(mFaceDetector == null) {
			showTip("本SDK不支持离线视频流检测");
		}
		String result = mFaceDetector.trackNV21(buffer, PREVIEW_WIDTH, PREVIEW_HEIGHT, isAlign, direction);
		btnResult = result;

		Intent intent = new Intent(VideoDemo.this,Result1Activity.class);
		intent.putExtra("r1PicPath",mPicPath);
		intent.putExtra("pointXY",btnResult);
		intent.putExtra("step_result",step);
		mStopTrack = true;

		decodeYUV420SP(buffer,PREVIEW_WIDTH,PREVIEW_HEIGHT);
		startActivity(intent);
		VideoDemo.this.finish();

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_demo);


		mTipTv = (TextView) findViewById(R.id.tv_tip);
		Intent intent = getIntent();
		step = intent.getStringExtra("video_step");
		if (step.equals("STEP_ONE")){
			mTipTv.setText(R.string.tip1);
		}else if (step.equals("STEP_TWO")){
			mTipTv.setText(R.string.tip2);
		}else if (step.equals("STEP_THREE")){
			mTipTv.setText(R.string.tip3);
		}else if (step.equals("STEP_FOUR")){
			mTipTv.setText(R.string.tip4);
		}else if (step.equals("STEP_FIVE")){
			mTipTv.setText(R.string.tip5);
		}else if (step.equals("STEP_SIX")){
			mTipTv.setText(R.string.tip6);
		}
	
		initUI();
		initTop();
		initParameter();

		nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
		buffer = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
		rgb = new int[PREVIEW_WIDTH*PREVIEW_HEIGHT];
		mAcc = new Accelerometer(VideoDemo.this);
		mFaceDetector = FaceDetector.createDetector(VideoDemo.this, null);
		
	}



	private void initParameter(){
		String parent = Environment.getExternalStorageDirectory().getPath();
		mPicPath = parent + "/" + "re.png";
	}
	
	
	private Callback mPreviewCallback = new Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			closeCamera();
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			openCamera();
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			mScaleMatrix.setScale(width/(float)PREVIEW_HEIGHT, height/(float)PREVIEW_WIDTH);
		}
	};
	
	private void setSurfaceSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		int width = metrics.widthPixels;
		int height = (int) (width * PREVIEW_WIDTH / (float)PREVIEW_HEIGHT);
		//Toast.makeText(VideoDemo.this,width+"==="+height,Toast.LENGTH_SHORT).show();  //1080---1440
		LayoutParams params = new LayoutParams(width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		mPreviewSurface.setLayoutParams(params);
		mFaceSurface.setLayoutParams(params);
	}

	@SuppressLint("ShowToast")
	@SuppressWarnings("deprecation")
	private void initUI() {
		mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);
		mFaceSurface = (SurfaceView) findViewById(R.id.sfv_face);
		
		mPreviewSurface.getHolder().addCallback(mPreviewCallback);
		mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mFaceSurface.setZOrderOnTop(true);
		mFaceSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		capture = (Button)findViewById(R.id.capture);
		
		// 点击SurfaceView，切换摄相头
		mFaceSurface.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 只有一个摄相头，不支持切换
				if (Camera.getNumberOfCameras() == 1) {
					showTip("只有后置摄像头，不能切换");
					return;
				}
				closeCamera();
				if (CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
					mCameraId = CameraInfo.CAMERA_FACING_BACK;
				} else {
					mCameraId = CameraInfo.CAMERA_FACING_FRONT;
				}
				openCamera();
			}
		});
		
		// 长按SurfaceView 500ms后松开，摄相头聚集
		mFaceSurface.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mLastClickTime = System.currentTimeMillis();
					break;
				case MotionEvent.ACTION_UP:
					if (System.currentTimeMillis() - mLastClickTime > 500) {
						mCamera.autoFocus(null);
						return true;
					}
					break;
					
				default:
					break;
				}
				return false;
			}
		});

		
		setSurfaceSize();
		mToast = Toast.makeText(VideoDemo.this, "", Toast.LENGTH_SHORT);
	}
	
	private void openCamera() {
		if (null != mCamera) {
			return;
		}
		
		if (!checkCameraPermission()) {
			showTip("摄像头权限未打开，请打开后再试");
			mStopTrack = true;
			return;
		}
		
		// 只有一个摄相头，打开后置
		if (Camera.getNumberOfCameras() == 1) {
			mCameraId = CameraInfo.CAMERA_FACING_BACK;
		}
		
		try {
			mCamera = Camera.open(mCameraId);
			if (CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
				showTip("前置摄像头已开启，点击可切换");
			} else {
				showTip("后置摄像头已开启，点击可切换");
			}
		} catch (Exception e) {
			e.printStackTrace();
			closeCamera();
			return;
		}
		
		Parameters params = mCamera.getParameters();
		params.setPreviewFormat(ImageFormat.NV21);
		params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
		mCamera.setParameters(params);
		
		// 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
		mCamera.setDisplayOrientation(90);
		mCamera.setPreviewCallback(new PreviewCallback() {
			
			@Override
			public void onPreviewFrame(byte[] data, Camera camera) {
				System.arraycopy(data, 0, nv21, 0, data.length);
				Log.d("resultss", "nv21::"+data.length+"");
			}
		});
		
		try {
			mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
			mCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeCamera() {
		if (null != mCamera) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
	
	private boolean checkCameraPermission() {
		int status = checkPermission(permission.CAMERA, Process.myPid(), Process.myUid());
		if (PackageManager.PERMISSION_GRANTED == status) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (null != mAcc) {
			mAcc.start();
		}
		
		mStopTrack = false;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int l1, r1, t1, b1;
				while (!mStopTrack) {
					if (null == nv21) {
						continue;
					}
					
					synchronized (nv21) {
						System.arraycopy(nv21, 0, buffer, 0, nv21.length);
					}
					
					// 获取手机朝向，返回值0,1,2,3分别表示0,90,180和270度
					int direction = Accelerometer.getDirection();
					boolean frontCamera = (CameraInfo.CAMERA_FACING_FRONT == mCameraId);
					// 前置摄像头预览显示的是镜像，需要将手机朝向换算成摄相头视角下的朝向。
					// 转换公式：a' = (360 - a)%360，a为人眼视角下的朝向（单位：角度）
					if (frontCamera) {
						// SDK中使用0,1,2,3,4分别表示0,90,180,270和360度
						direction = (4 - direction)%4;
					}

					if(mFaceDetector == null) {
						showTip("本SDK不支持离线视频流检测");
						break;
					}
					
					String result = mFaceDetector.trackNV21(buffer, PREVIEW_WIDTH, PREVIEW_HEIGHT, isAlign, direction);
					/*l1 = ParseResult.getFaceXYByJs(result,"top");
					r1 = ParseResult.getFaceXYByJs(result,"bottom");
					if (480-(r1 - l1) >300){
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(VideoDemo.this, "太远了，请靠近一点", Toast.LENGTH_SHORT).show();
							}
						});
					}*/
					
					FaceRect[] faces = ParseResult.parseResult(result);
					
					Canvas canvas = mFaceSurface.getHolder().lockCanvas();
					if (null == canvas) {
						continue;
					}
					
					canvas.drawColor(0, PorterDuff.Mode.CLEAR);
					canvas.setMatrix(mScaleMatrix);

					if( faces.length <=0 ) {
						mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
						continue;
					}
					
					if (null != faces && frontCamera == (CameraInfo.CAMERA_FACING_FRONT == mCameraId)) {
						for (FaceRect face: faces) {
							face.bound = FaceUtil.RotateDeg90(face.bound, PREVIEW_WIDTH, PREVIEW_HEIGHT);
							if (face.point != null) {
								for (int i = 0; i < face.point.length; i++) {
									face.point[i] = FaceUtil.RotateDeg90(face.point[i], PREVIEW_WIDTH, PREVIEW_HEIGHT);
								}
							}
							FaceUtil.drawFaceRect(canvas, face, PREVIEW_WIDTH, PREVIEW_HEIGHT,
									frontCamera, false);
						}
					} else {
						Log.d(TAG, "faces:0");
					}
					
					mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
				}
			}
		}).start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		closeCamera();
		if (null != mAcc) {
			mAcc.stop();
		}
		mStopTrack = true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mFaceDetector.destroy();
	}
	
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 自定义Topbar 后处理顶栏
	 */
	private void initTop(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}


	/**
	 * 将NV21数据直接转换成为RGB图像并保存到文件中
	 * @param yuv420sp
	 * @param width
	 * @param height
	 */
	public void decodeYUV420SP(byte[] yuv420sp, int width, int height) {
		final int frameSize = width * height;

		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0) y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0) r = 0; else if (r > 262143) r = 262143;
				if (g < 0) g = 0; else if (g > 262143) g = 262143;
				if (b < 0) b = 0; else if (b > 262143) b = 262143;

				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(rgb,PREVIEW_WIDTH,PREVIEW_HEIGHT,Bitmap.Config.ARGB_8888);
		String result = Environment.getExternalStorageDirectory().getPath()+"/re.png";
		File f = new File(result);
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG,90,out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

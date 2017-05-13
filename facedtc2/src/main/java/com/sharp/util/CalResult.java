package com.sharp.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sharp.beans.Face;
import com.sharp.beans.FacePoint;
import com.sharp.beans.FaceResult;

import java.text.DecimalFormat;

/**
 * Created by Zjf on 2017/1/4.
 */
public class CalResult extends Thread{

    private Bitmap bitmap;
    private Handler handler;
    private String jsonStr;
    private FaceResult fr;

    public CalResult(Bitmap bitmap, Handler handler, String jsonStr){
        this.bitmap = bitmap;
        this.handler = handler;
        this.jsonStr = jsonStr;
        fr = new FaceResult();
    }

    @Override
    public void run() {
        Face face = ParseResult.formatResult(jsonStr);
        calculateFaceAsym(bitmap, face);
        calculateAngles(face);

        Message message = handler.obtainMessage();
        message.obj = fr;
        handler.sendMessage(message);
    }

    /**
     *
     * @param bitmap
     * @param face
     */
    private void calculateFaceAsym(Bitmap bitmap, Face face){

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int left = face.getLeft();
        int right = face.getRight();
        int top = face.getTop();
        int bottom = face.getBottom();

        int[] pixels = new int[width*height];
        int temp;
        int color, r, g, b;
        int gray1, gray2;
        float tempG1 = 255;
        float tempG2 = 255;
        float tempAsym = 1;
        float leftAsym = 0;
        int leftCount = 0;
        float faceAsym = 1;

        int centerLineY = (face.getLeft_eye_center().getFpY() + face.getRight_eye_center().getFpY())/2;

        bitmap.getPixels(pixels,0,width,0,0,width,height);
        /*for(int k=0; k<width*height; k++){
            Log.d("fuck", pixels[k]+"==>");
        }*/
        int m = 0;
        for (int i=top; i<centerLineY; i++){
            for (int j=left; j<right; j++){
                if (2*centerLineY - i<bottom){

                    color = pixels[width*i+j];
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);
                    gray1 = (int)(0.3 * r + 0.59 * g + 0.11 * b);

                    color = pixels[(2*centerLineY-i)*width+j];
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);
                    gray2 = (int)(0.3 * r + 0.59 * g + 0.11 * b);

                    tempG1 = 255;
                    tempG2 = 255;
                    tempG1 = gray1;
                    tempG2 = gray2;
                    tempAsym = 1;
                    if ( tempG2!=0){
                        tempAsym = tempG1 / tempG2;

                    }
                    if (tempG2==0){
                        tempG2 = tempG1;
                        Log.d("leftAsym", tempG1+"==="+tempG2);
                    }
                    if (tempAsym > 1) {
                        tempAsym = 1 / tempAsym;
                    }
                    leftAsym += tempAsym;

                    leftCount ++;
                    gray1 = 0;
                    gray2 = 0;

                }
            }
        }
        leftAsym /= leftCount;
        faceAsym = leftAsym;
        if (faceAsym > 1) {
            faceAsym = 1/faceAsym;
        }
        /*fr.setFaceAsym(faceAsym*100);*/
        fr.setFaceAsym(Float.parseFloat(formatDouble(faceAsym*100)));
    }

    private void calculateAngles(Face face){
        //Log.d("angle", "ssss");
        double angle = FacePoint.getAngle(face.getLeft_eye_center(),face.getRight_eye_center());
        //计算眉毛
        double angle1 = FacePoint.getAngle(face.getLeft_eyebrow_middle(),face.getRight_eyebrow_middle());
        /*fr.setBrowAngle(Math.abs(angle1-angle));*/
        fr.setBrowAngle(Double.parseDouble(formatDouble(Math.abs(angle1-angle))) );

        //计算眼睛
        angle1 = FacePoint.getAngle(face.getLeft_eye_left_corner(),face.getRight_eye_right_corner());
        /*fr.setEyeAngle(Math.abs(angle1-angle));*/
        fr.setEyeAngle(Double.parseDouble(formatDouble(Math.abs(angle1-angle)))  );

        //计算鼻子
        angle1 = FacePoint.getAngle(face.getNose_left(),face.getNose_right());
        /*fr.setNoseAngle(Math.abs(angle1-angle));*/
        fr.setNoseAngle(Double.parseDouble(formatDouble( Math.abs(angle1-angle)))  );

        //计算嘴巴
        angle1 = FacePoint.getAngle(face.getMouth_left_corner(),face.getMouth_right_corner());
        /*fr.setMouthAngle(Math.abs(angle1-angle));*/
        fr.setMouthAngle(Double.parseDouble(formatDouble( Math.abs(angle1-angle))) );
    }

    private String formatDouble(double d){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
    }

}

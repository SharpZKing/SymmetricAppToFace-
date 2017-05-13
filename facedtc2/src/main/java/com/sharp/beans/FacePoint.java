package com.sharp.beans;

import com.iflytek.thirdparty.F;

/**
 * Created by Zjf on 2016/12/31.
 */
public class FacePoint {
    private int fpX;
    private int fpY;

    public FacePoint(int x, int y){
        this.fpX = x;
        this.fpY = y;
    }

    public int getFpX() {
        return fpX;
    }

    public void setFpX(int fpX) {
        this.fpX = fpX;
    }

    public int getFpY() {
        return fpY;
    }

    public void setFpY(int fpY) {
        this.fpY = fpY;
    }

    /**
     * 对外提供计算角度的方法
     * @param p1
     * @param p2
     * @return float 角度
     */
    public static double getAngle(FacePoint p1, FacePoint p2){
        double angle = 0;

        if ((p1.fpY-p2.fpY)!=0){
            angle = Math.abs(Math.atan(((p1.fpX-p2.fpX)*1.0)/(p1.fpY - p2.fpY)));
        }else{
            angle = 0;
        }

        return  angle*180/Math.PI;
    }
}

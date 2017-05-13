package com.sharp.beans;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zjfsharp on 2017/2/27.
 */
public class SaveFaceResult extends DataSupport {

    private int id;

    private int userid;

    private int action;

    private double asymface;

    private String imgUrl;

    private String asymtime;

    private double eyeangle;

    private double mouthangle;

    private double noseangle;

    private double eyebrowangle;

    private String xunfeijs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public double getAsymface() {
        return asymface;
    }

    public void setAsymface(double asymface) {
        this.asymface = asymface;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAsymtime() {
        return asymtime;
    }

    public void setAsymtime(String asymtime) {
        this.asymtime = asymtime;
    }

    public double getEyeangle() {
        return eyeangle;
    }

    public void setEyeangle(double eyeangle) {
        this.eyeangle = eyeangle;
    }

    public double getMouthangle() {
        return mouthangle;
    }

    public void setMouthangle(double mouthangle) {
        this.mouthangle = mouthangle;
    }

    public double getNoseangle() {
        return noseangle;
    }

    public void setNoseangle(double noseangle) {
        this.noseangle = noseangle;
    }

    public double getEyebrowangle() {
        return eyebrowangle;
    }

    public void setEyebrowangle(double eyebrowangle) {
        this.eyebrowangle = eyebrowangle;
    }

    public String getXunfeijs() {
        return xunfeijs;
    }

    public void setXunfeijs(String xunfeijs) {
        this.xunfeijs = xunfeijs;
    }
}

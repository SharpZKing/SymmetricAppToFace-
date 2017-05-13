package com.sharp.beans;

import android.provider.Settings;
import android.util.Log;

/**
 * Created by Zjf on 2016/12/31.
 */
public class Face {

    private int left;
    private int right;
    private int top;
    private int bottom;

    private FacePoint left_eyebrow_middle;
    private FacePoint right_eyebrow_middle;

    private FacePoint left_eye_center;
    private FacePoint right_eye_center;

    private FacePoint left_eye_left_corner;
    private FacePoint right_eye_right_corner;

    private FacePoint nose_left;
    private FacePoint nose_right;

    private FacePoint mouth_left_corner;
    private FacePoint mouth_right_corner;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottpm) {
        this.bottom = bottpm;
    }

    public FacePoint getLeft_eyebrow_middle() {
        return left_eyebrow_middle;
    }

    public void setLeft_eyebrow_middle(FacePoint left_eyebrow_middle) {
        this.left_eyebrow_middle = left_eyebrow_middle;
    }

    public FacePoint getRight_eyebrow_middle() {
        return right_eyebrow_middle;
    }

    public void setRight_eyebrow_middle(FacePoint right_eyebrow_middle) {
        this.right_eyebrow_middle = right_eyebrow_middle;
    }

    public FacePoint getLeft_eye_center() {
        return left_eye_center;
    }

    public void setLeft_eye_center(FacePoint left_eye_center) {
        this.left_eye_center = left_eye_center;
    }

    public FacePoint getRight_eye_center() {
        return right_eye_center;
    }

    public void setRight_eye_center(FacePoint right_eye_center) {
        this.right_eye_center = right_eye_center;
    }

    public FacePoint getLeft_eye_left_corner() {
        return left_eye_left_corner;
    }

    public void setLeft_eye_left_corner(FacePoint left_eye_left_corner) {
        this.left_eye_left_corner = left_eye_left_corner;
    }

    public FacePoint getRight_eye_right_corner() {
        return right_eye_right_corner;
    }

    public void setRight_eye_right_corner(FacePoint right_eye_right_corner) {
        this.right_eye_right_corner = right_eye_right_corner;
    }

    public FacePoint getNose_left() {
        return nose_left;
    }

    public void setNose_left(FacePoint nose_left) {
        this.nose_left = nose_left;
    }

    public FacePoint getNose_right() {
        return nose_right;
    }

    public void setNose_right(FacePoint nose_right) {
        this.nose_right = nose_right;
    }

    public FacePoint getMouth_left_corner() {
        return mouth_left_corner;
    }

    public void setMouth_left_corner(FacePoint mouth_left_corner) {
        this.mouth_left_corner = mouth_left_corner;
    }

    public FacePoint getMouth_right_corner() {
        return mouth_right_corner;
    }

    public void setMouth_right_corner(FacePoint mouth_right_corner) {
        this.mouth_right_corner = mouth_right_corner;
    }

    @Override
    public String toString() {
        return left+"=="+right+"=="+bottom+"=="+top;
    }
}

package com.sharp.util;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.graphics.Point;
import android.text.TextUtils;

import com.iflytek.thirdparty.F;
import com.sharp.beans.Face;
import com.sharp.beans.FacePoint;

public class ParseResult {

	/**
	 * 离线人脸框结果解析方法
	 * @param json
	 * @return
	 */
	static public FaceRect[] parseResult(String json){
		FaceRect[] rect = null;
		if(TextUtils.isEmpty(json)) {
			return null;
		}
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);
			// 获取每个人脸的结果
			JSONArray items = joResult.getJSONArray("face");
			// 获取人脸数目
			rect = new FaceRect[items.length()];
			for(int i=0; i<items.length(); i++) {
				
				JSONObject position = items.getJSONObject(i).getJSONObject("position");
				// 提取关键点数据
				rect[i] = new FaceRect();
				rect[i].bound.left = position.getInt("left");
				rect[i].bound.top = position.getInt("top");
				rect[i].bound.right = position.getInt("right");
				rect[i].bound.bottom = position.getInt("bottom");
				
				try {
					JSONObject landmark = items.getJSONObject(i).getJSONObject("landmark");
					int keyPoint = landmark.length();
					rect[i].point = new Point[keyPoint];
					Iterator it = landmark.keys();
					int point = 0;
					while (it.hasNext() && point < keyPoint) {
						String key = (String) it.next();
						JSONObject postion = landmark.getJSONObject(key);
						rect[i].point[point] = new Point(postion.getInt("x"), postion.getInt("y"));
						point++;
					}
				} catch (JSONException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rect;
	}

	/**
	 * 通过字符串得到当前的某个坐标的位置
	 * @param jsonStr
	 * @param dst
     * @return
     */
	public static int getFaceXYByJs(String jsonStr, String dst){
		int result = 0;

		if (TextUtils.isEmpty(jsonStr)) {
			return 200; //暂时随意取个值就行，足够大就可以
		}
		try {
			JSONTokener tokener = new JSONTokener(jsonStr);
			JSONObject joResult = new JSONObject(tokener);
			// 获取每个人脸的结果
			JSONArray items = joResult.getJSONArray("face");

			for(int i=0; i<items.length(); i++) {
				JSONObject position = items.getJSONObject(i).getJSONObject("position");
				result = position.getInt(dst);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将获取的人脸关键点坐标格式化为人脸的实例对象
	 * @param jsonStr   人脸坐标点关键信息
	 * @return
     */
	public static Face formatResult(String jsonStr){
		Face face = null;

		if (TextUtils.isEmpty(jsonStr)) {
			return null;
		}
		try {
			JSONTokener tokener = new JSONTokener(jsonStr);
			JSONObject joResult = new JSONObject(tokener);
			// 获取每个人脸的结果
			JSONArray items = joResult.getJSONArray("face");

			for(int i=0; i<items.length(); i++) {

				JSONObject position = items.getJSONObject(i).getJSONObject("position");
				// 提取关键点数据
				face = new Face();
				face.setLeft(position.getInt("left"));
				face.setBottom(position.getInt("bottom"));
				face.setRight(position.getInt("right"));
				face.setTop(position.getInt("top"));

				try {
					JSONObject landmark = items.getJSONObject(i).getJSONObject("landmark");
					JSONObject obj = landmark.getJSONObject("left_eyebrow_middle");
					face.setLeft_eyebrow_middle(new FacePoint(obj.getInt("x"), obj.getInt("y")));
					obj = landmark.getJSONObject("right_eyebrow_middle");
					face.setRight_eyebrow_middle(new FacePoint(obj.getInt("x"), obj.getInt("y")));
					obj = landmark.getJSONObject("left_eye_center");
					face.setLeft_eye_center(new FacePoint(obj.getInt("x"), obj.getInt("y")));
					obj = landmark.getJSONObject("right_eye_center");
					face.setRight_eye_center(new FacePoint(obj.getInt("x"), obj.getInt("y")));
					obj = landmark.getJSONObject("left_eye_left_corner");
					face.setLeft_eye_left_corner(new FacePoint(obj.getInt("x"), obj.getInt("y")));
					obj = landmark.getJSONObject("right_eye_right_corner");
					face.setRight_eye_right_corner(new FacePoint(obj.getInt("x"), obj.getInt("y")));
					obj = landmark.getJSONObject("nose_left");
					face.setNose_left(new FacePoint(obj.getInt("x"), obj.getInt("y")));
					obj = landmark.getJSONObject("nose_right");
					face.setNose_right(new FacePoint(obj.getInt("x"), obj.getInt("y")));
					obj = landmark.getJSONObject("mouth_left_corner");
					face.setMouth_left_corner(new FacePoint(obj.getInt("x"), obj.getInt("y")));
					obj = landmark.getJSONObject("mouth_right_corner");
					face.setMouth_right_corner(new FacePoint(obj.getInt("x"), obj.getInt("y")));

				} catch (JSONException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return face;
	}



}
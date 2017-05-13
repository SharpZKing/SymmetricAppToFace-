package com.sharp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharp.beans.SaveFaceResult;
import com.sharp.facedtc.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjfsharp on 2017/2/28.
 */
public class ResultListViewAdapter  extends BaseAdapter{


    private Context mContext;
    private List<SaveFaceResult> mList;
    private SaveFaceResult sfr;

    private LayoutInflater inflater;

    public ResultListViewAdapter(Context mContext, List<SaveFaceResult> mList){
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.mList = mList;
    }

    public void onDateChange(List<SaveFaceResult> apk_list) {
        this.mList = apk_list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_item_card,null);
            holder = new ViewHolder();
            holder.star4 = (ImageView) convertView.findViewById(R.id.star4);
            holder.star5 = (ImageView) convertView.findViewById(R.id.star5);
            holder.mAsymTv = (TextView) convertView.findViewById(R.id.tv_asym);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.mCommentTv = (TextView) convertView.findViewById(R.id.tv_comment);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        SaveFaceResult sfr = mList.get(position);
        /*holder.mAsymTv.setText(sfr.getAsymface()+"");*/
        holder.mAsymTv.setText(formatDouble4(sfr.getAsymface()));
        holder.mTimeTv.setText(sfr.getAsymtime());
        int tag = 3;

        if (sfr.getAsymface()<=70){
            holder.mCommentTv.setText("对称度偏低，请确认周边光照均匀下重测");
        }else if (sfr.getAsymface()<80){
            holder.mCommentTv.setText("对称度有待偏低有待改善");
            tag = 4;
        }else if (sfr.getAsymface()<85){
            holder.mCommentTv.setText("对称度非常棒");
            tag = 5;
        }else if (sfr.getAsymface()<95){
            holder.mCommentTv.setText("对称度优秀");
            tag = 6;
        }else{
            holder.mCommentTv.setText("对称度接近完美Good！");
            tag = 7;
        }
        if (tag == 4){
            holder.star4.setImageResource(R.drawable.half16);
        }

        if (tag == 5){
            holder.star4.setImageResource(R.drawable.red16);
        }

        if (tag == 6){
            holder.star4.setImageResource(R.drawable.red16);
            holder.star5.setImageResource(R.drawable.half16);
        }
        if (tag == 7){
            holder.star4.setImageResource(R.drawable.red16);
            holder.star5.setImageResource(R.drawable.red16);
        }

        return convertView;
    }

    class ViewHolder{
        public ImageView star4;
        public ImageView star5;
        public TextView mAsymTv;
        public TextView mTimeTv;
        public TextView mCommentTv;
    }

    private String formatDouble4(double d) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
    }

}

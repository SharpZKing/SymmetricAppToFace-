package com.sharp.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharp.adapter.ResultListViewAdapter;
import com.sharp.beans.SaveFaceResult;
import com.sharp.facedtc.R;
import com.sharp.util.ShareUtils;
import com.sharp.views.RefreshListView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Zjf on 2016/12/26.
 */
public class HisdatasFragment extends Fragment implements RefreshListView.IReflashListener {

    /*private ListView listView;*/
    private RefreshListView listView;
    private List<SaveFaceResult> datas = new ArrayList<>();
    private TextView mShow;
    ResultListViewAdapter adapter;


    private LinearLayout llshow;
    private LineChartView lineCharts;

    private List<String> times2 = new ArrayList<>();
    private List<SaveFaceResult> datas2 = new ArrayList<SaveFaceResult>();
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues  = new ArrayList<AxisValue>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hisdatas,container,false);
        mShow = (TextView) view.findViewById(R.id.nodatas);
        listView = (RefreshListView) view.findViewById(R.id.all_result);
        llshow = (LinearLayout) view.findViewById(R.id.llshow);
        lineCharts = (LineChartView)view.findViewById(R.id.line_chart2);
        if (ShareUtils.getString(getActivity(), "userId","-1").equals("-1")){
            mShow.setVisibility(View.VISIBLE);
            lineCharts.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            llshow.setVisibility(View.GONE);
            mShow.setText("您还未登入，登入后数据可见(数据暂时保存在本地确保账号在本机登入)");

        }else{
            llshow.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            lineCharts.setVisibility(View.VISIBLE);
            mShow.setVisibility(View.GONE);

            initDatas();
            adapter = new ResultListViewAdapter(getActivity(),datas);
            listView.setAdapter(adapter);
            listView.setInterface(this);


        }


        initDatas2();
        getAxisXLabels();
        getAxisPoints();
        initLineCharts();

        return view;

    }

    private void initDatas() {
        datas.clear();

        if (ShareUtils.getString(getActivity(), "userId","-1").equals("-1")){
            mShow.setVisibility(View.VISIBLE);
            lineCharts.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            llshow.setVisibility(View.GONE);
            mShow.setText("您还未登入，登入后数据可见(数据暂时保存在本地确保账号在本机登入)");
        }else{
            SaveFaceResult sfr = null;

            Cursor cursor = DataSupport.findBySQL("select distinct asymtime from savefaceresult where userid = '"+ ShareUtils.getString(getActivity(), "userId","-1") +"' order by asymtime desc");

            if (cursor.moveToFirst()== false){

            }else{
                cursor.moveToFirst();
                List<String> times = new ArrayList<>();
                do{

                    if(!times.contains(cursor.getString(cursor.getColumnIndex("asymtime")))){
                        times.add(cursor.getString(cursor.getColumnIndex("asymtime")));
                    }

                }while (cursor.moveToNext());


                //获取最后的数据
                for (int i=0; i<times.size(); i++){
                    String wh = "asymtime = '"+times.get(i) + "' ";
                    List<SaveFaceResult> list = DataSupport.where(wh).find(SaveFaceResult.class);
                    sfr = new SaveFaceResult();
                    double avg = 0.0;
                    int sum = 0;
                    for (SaveFaceResult s: list){
                        avg = avg + s.getAsymface();
                        sum ++;
                    }
                    sfr.setAsymface(avg/sum);
                    sfr.setAsymtime(times.get(i));

                    datas.add(sfr);
                }


            }
        }

    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ShareUtils.getString(getActivity(), "userId","-1").equals("-1")){
                    mShow.setVisibility(View.VISIBLE);
                    lineCharts.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    llshow.setVisibility(View.GONE);
                    mShow.setText("您还未登入，登入后数据可见(数据暂时保存在本地确保账号在本机登入)");

                }else{
                    llshow.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    lineCharts.setVisibility(View.VISIBLE);
                    mShow.setVisibility(View.GONE);

                    initDatas();
                    if(adapter==null){
                        adapter = new ResultListViewAdapter(getActivity(),datas);
                        listView.setAdapter(adapter);
                        listView.setInterface(HisdatasFragment.this);
                    }else{
                        adapter.onDateChange(datas);
                        listView.refreshComplete();
                    }




                    times2.clear();
                    datas2.clear();
                    mAxisXValues.clear();
                    mPointValues.clear();
                    initDatas2();
                    getAxisXLabels();
                    getAxisPoints();
                    initLineCharts();


                }



                /*datas.clear();

                initDatas();

                adapter.onDateChange(datas);

                listView.refreshComplete();

                times2.clear();
                datas2.clear();
                mAxisXValues.clear();
                mPointValues.clear();
                initDatas2();
                getAxisXLabels();
                getAxisPoints();
                initLineCharts();*/
            }
        },2000);
    }










    private void getAxisXLabels() {
        for(int i=0; i<times2.size(); i++){

            mAxisXValues.add(new AxisValue(i).setLabel(times2.get(i).substring(4,6)+"-"+times2.get(i).substring(6,8) ));
        }
    }

    private void getAxisPoints() {
        for(int i=0; i<datas2.size(); i++){
            mPointValues.add(new PointValue(i,(float)datas2.get(i).getAsymface()));
        }
    }

    private void initLineCharts() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#Fd2440"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);


        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.RED);  //设置字体颜色
        axisX.setName("时间");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        //axisY.setName("对称度");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移
        lineCharts.setInteractive(true);
        lineCharts.setZoomType(ZoomType.HORIZONTAL);
        lineCharts.setMaxZoom((float) 2);//最大方法比例
        lineCharts.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineCharts.setLineChartData(data);
        lineCharts.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(lineCharts.getMaximumViewport());
        v.left = 0;
        v.right= 4;
        lineCharts.setCurrentViewport(v);
    }

    private void initDatas2() {

        if (ShareUtils.getString(getActivity(), "userId","-1").equals("-1")){
//            Toast.makeText(this,"您还未登入，登入后上传数据后可见",Toast.LENGTH_SHORT).show();
        }else{
            SaveFaceResult sfr = null;

            Cursor cursor = DataSupport.findBySQL("select distinct asymtime from savefaceresult where userid = '"+ ShareUtils.getString(getActivity(), "userId","-1") +"' order by asymtime desc");

            if (cursor.moveToFirst()== false){

            }else{
                cursor.moveToFirst();

                do{
                    times2.add(cursor.getString(cursor.getColumnIndex("asymtime")));
                }while (cursor.moveToNext());

                //获取最后的数据
                for (int i=0; i<times2.size(); i++){
                    String wh = "asymtime = '"+times2.get(i) + "' ";
                    List<SaveFaceResult> list = DataSupport.where(wh).find(SaveFaceResult.class);
                    sfr = new SaveFaceResult();
                    double avg = 0.0;
                    int sum = 0;
                    for (SaveFaceResult s: list){
                        avg = avg + s.getAsymface();
                        sum ++;
                    }
                    sfr.setAsymface(avg/sum);
                    sfr.setAsymtime(times2.get(i));

                    datas2.add(sfr);
                }

            }
        }

//        datas.add(sfr);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onRefresh();
    }
}

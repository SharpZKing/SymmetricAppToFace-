package com.sharp.facedtc;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.thirdparty.P;
import com.sharp.util.ToolUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager mViewPager;
    private View view1, view2, view3;

    private List<View> views = new ArrayList<View>();

    //跳过
    private ImageView iv_back;

    //小圆点
    private ImageView point1, point2, point3;

    private Button mGoBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {

        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);

        setPointImg(true,false,false);

        view1 = View.inflate(this,R.layout.item_guide_one,null);
        TextView tv1 = (TextView) view1.findViewById(R.id.item_tv_one);
        if (tv1!=null){
            ToolUtils.setFontType(this,tv1);
        }
        TextView tv1_2 = (TextView) view1.findViewById(R.id.item_tv_one_2);
        if (tv1_2!=null){
            ToolUtils.setFontType(this,tv1_2);
        }

        view2 =  View.inflate(this,R.layout.item_guide_two,null);
        TextView tv2 = (TextView) view2.findViewById(R.id.item_tv_two);
        if (tv2!=null){
            ToolUtils.setFontType(this, tv2);
        }
        TextView tv2_2 = (TextView) view2.findViewById(R.id.item_tv_two_2);
        if (tv2_2!=null){
            ToolUtils.setFontType(this, tv2_2);
        }
        TextView tv2_3 = (TextView) view2.findViewById(R.id.item_tv_two_3);
        if (tv2_3!=null){
            ToolUtils.setFontType(this, tv2_3);
        }

        view3 =  View.inflate(this,R.layout.item_guide_three,null);
        TextView tv3 = (TextView) view3.findViewById(R.id.item_tv_three);
        if (tv3!=null){
            ToolUtils.setFontType(this, tv3);
        }
        TextView tv3_2 = (TextView) view3.findViewById(R.id.item_tv_three_2);
        if (tv3_2!=null){
            ToolUtils.setFontType(this, tv3_2);
        }

        views.add(view1);
        views.add(view2);
        views.add(view3);

        mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setPointImg(true,false,false);
                    iv_back.setVisibility(View.VISIBLE);
                }else if (position == 1){
                    setPointImg(false,true,false);
                    iv_back.setVisibility(View.VISIBLE);
                }else if (position == 2){
                    setPointImg(false,false,true);
                    iv_back.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view==o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
                //super.destroyItem(container, position, object);
            }
        });

        iv_back = (ImageView) findViewById(R.id.back);
        iv_back.setOnClickListener(this);

        mGoBtn = (Button) view3.findViewById(R.id.go_home_btn);
        mGoBtn.setOnClickListener(this);

    }

    private void setPointImg(boolean is1, boolean is2, boolean is3) {
        if (is1){
            point1.setBackgroundResource(R.drawable.point_on);
        }else{
            point1.setBackgroundResource(R.drawable.point_off);
        }
        if (is2){
            point2.setBackgroundResource(R.drawable.point_on);
        }else{
            point2.setBackgroundResource(R.drawable.point_off);
        }
        if (is3){
            point3.setBackgroundResource(R.drawable.point_on);
        }else{
            point3.setBackgroundResource(R.drawable.point_off);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                /*Intent intent1 = new Intent(GuideActivity.this, ShowResult.class);
                startActivity(intent1);
                finish();
                break;*/
            case R.id.go_home_btn:
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                //Intent intent = new Intent(GuideActivity.this, ShowResult.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}

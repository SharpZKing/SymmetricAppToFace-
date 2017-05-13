package com.sharp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sharp.facedtc.ActionTip;
import com.sharp.facedtc.R;
import com.sharp.facedtc.VideoDemo;

/**
 * Created by Zjf on 2016/12/26.
 */
public class RecognizeFragment extends Fragment {

    private Button recogBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recognize,container,false);
        initView(view);
        initEvent(view);
        return view;

    }

    private void initView(View view){
        recogBtn = (Button)view.findViewById(R.id.btn_video_demo);
    }

    private void initEvent(View view){
        recogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), VideoDemo.class);
                Intent intent = new Intent(getActivity(), ActionTip.class);
                intent.putExtra("step","STEP_ONE");
                startActivity(intent);
            }
        });
    }

}

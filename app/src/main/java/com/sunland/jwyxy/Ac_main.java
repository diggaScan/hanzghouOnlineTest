package com.sunland.jwyxy;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_person_stats.PersonStatsReqBean;
import com.sunland.jwyxy.bean.i_person_stats.PersonStatsResBean;
import com.sunland.jwyxy.bean.i_person_stats.PersonalStat;
import com.sunlandgroup.def.bean.result.ResultBase;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_main extends CheckSelfPermissionActivity {

    @BindView(R.id.test)
    public TextView textView;
    @BindView(R.id.back_press)
    public ImageView iv_nav_back;
    @BindView(R.id.test_container)
    public RelativeLayout test_container;
    @BindView(R.id.review_container)
    public RelativeLayout review_container;
    @BindView(R.id.toolbar_title)
    public TextView toolbar_title;
    @BindView(R.id.total_exercise)
    public TextView tv_total_exercise;
    @BindView(R.id.total_paper)
    public TextView tv_total_paper;
    @BindView(R.id.accuracy_mean)
    public TextView tv_accuracy_mean;
    @BindView(R.id.result_mean)
    public TextView tv_result_mean;
    @BindView(R.id.middle)
    public TextView tv_history;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("警务云学院");
        iv_nav_back.setVisibility(View.GONE);
        initWindow();
        queryHzydjw(Dictionary.PERSON_STATS);

    }

    @Override
    public BaseRequestBean assembleRequestObj(String reqName) {
        PersonStatsReqBean personStatsReqBean = new PersonStatsReqBean();
        assembleBasicObj(personStatsReqBean);
        personStatsReqBean.setJyid(LocalInfo.jyid);
        personStatsReqBean.setJymc("test");
        return personStatsReqBean;
    }

    @Override
    public int setToolbarLayout() {
        return R.layout.toolbar_test_main;
    }

    @Override
    public int setContentLayut() {
        return R.layout.ac_test_main;
    }


    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        switch (reqName) {
            case Dictionary.PERSON_STATS:
                PersonStatsResBean personStatsResBean = (PersonStatsResBean) bean;
                PersonalStat personalStat = personStatsResBean.getPersonalStats();
                if (personalStat == null) {
                    Toast.makeText(this, "未获得统计数据", Toast.LENGTH_SHORT).show();
                } else {
                    initScoreBoard(personalStat);

                }
        }
    }

    private void initWindow() {
        View decorview = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        toolbar.setFitsSystemWindows(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        rootview.setBackgroundResource(R.drawable.online_test_background);
    }


    public void initScoreBoard(PersonalStat personalStat) {


        tv_total_exercise.setText(String.valueOf(personalStat.getDtzs()) + "个");
        tv_accuracy_mean.setText(personalStat.getZql() + "%");
        tv_total_paper.setText(String.valueOf(personalStat.getYzsj()) + "套");
        tv_result_mean.setText((personalStat.getPjcj()) + "分");


    }

    @OnClick({R.id.test_container, R.id.review_container, R.id.middle})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.review_container:
                Ac_review_mode.startActivity(this);
                break;
            case R.id.test_container:
                Bundle bundle = new Bundle();
                bundle.putString("jyid", LocalInfo.jyid);
                hop2Activity(Ac_paper_list.class, bundle);
                break;
            case R.id.middle:
                hop2Activity(Ac_history.class);
                break;
        }
    }

}

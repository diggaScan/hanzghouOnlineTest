package com.sunland.jwyxy.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.jwyxy.R;
import com.sunland.jwyxy.V_config;
import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_person_stats.PersonStatsReqBean;
import com.sunland.jwyxy.bean.i_person_stats.PersonStatsResBean;
import com.sunland.jwyxy.bean.i_person_stats.PersonalStat;
import com.sunlandgroup.def.bean.result.ResultBase;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_main extends Ac_base_query {

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
    @BindView(R.id.loading_layout)
    public FrameLayout loading_layout;


    private int backPressed_num = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarLayout(R.layout.toolbar_test_main);
        setContentLayout(R.layout.ac_test_main);
        toolbar_title.setText("警务云学院");
        toolbar.setVisibility(View.GONE);
        iv_nav_back.setVisibility(View.GONE);
        initWindow();
        queryHzydjwNoDialog(V_config.PERSON_STATS);

    }

    @Override
    public BaseRequestBean assembleRequestObj(String reqName) {
        PersonStatsReqBean personStatsReqBean = new PersonStatsReqBean();
        assembleBasicObj(personStatsReqBean);
        personStatsReqBean.setJyid(V_config.YHDM);
        personStatsReqBean.setJymc(V_config.JYMC);
        return personStatsReqBean;
    }

    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        switch (reqName) {
            case V_config.PERSON_STATS:
                PersonStatsResBean personStatsResBean = (PersonStatsResBean) bean;
                if (personStatsResBean == null) {
                    toolbar.setVisibility(View.VISIBLE);
                    loading_layout.setVisibility(View.GONE);
                    Toast.makeText(this, "后台接口异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                PersonalStat personalStat = personStatsResBean.getPersonalStats();
                if (personalStat == null) {
                    Toast.makeText(this, "未获得统计数据", Toast.LENGTH_SHORT).show();
                } else {
                    initScoreBoard(personalStat);
                    toolbar.setVisibility(View.VISIBLE);
                    loading_layout.setVisibility(View.GONE);
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
        tv_total_paper.setText(String.valueOf(personalStat.getYzsj()) + "套");

        if(personalStat.getZql()==null||personalStat.getZql().isEmpty()||personalStat.getZql().equals("null")){
            tv_accuracy_mean.setText("未知");
        }else {
            tv_accuracy_mean.setText(personalStat.getZql() + "%");
        }

        if(personalStat.getPjcj()==null||personalStat.getPjcj().isEmpty()||personalStat.getPjcj().equals("null")){
            tv_result_mean.setText("未知");
        }else {
            tv_result_mean.setText(personalStat.getPjcj() + "分");
        }

    }

    @OnClick({R.id.test_container, R.id.review_container, R.id.middle})
    public void onClick(View view) {
        int id = view.getId();
        Bundle bundle = new Bundle();
        switch (id) {
            case R.id.review_container:
                hop2Activity(Ac_review_mode.class);
                break;
            case R.id.test_container:
                bundle.putString("jyid", V_config.YHDM);
                hop2Activity(Ac_paper_list.class, bundle);
                break;
            case R.id.middle:
                hop2Activity(Ac_history.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressed_num != 1) {
            backPressed_num++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressed_num--;
                }
            }, 2500);
            Toast.makeText(this, "再按一次，退出应用", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}

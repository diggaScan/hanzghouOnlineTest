package com.sunland.jwyxy.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunland.jwyxy.R;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_review_mode extends Ac_base {

    @BindView(R.id.toolbar_title)
    public TextView toolbar_title;
    @BindView(R.id.back_press)
    public ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarLayout(R.layout.toolbar_test_main);
        setContentLayout(R.layout.ac_review_mode);
        initToolbar();
    }

    private void initToolbar() {
        toolbar_title.setText("错题回顾");
        toolbar_title.setTextColor(Color.BLACK);
        iv_back.setImageResource(R.drawable.ic_arrow_back_black);
    }

    @OnClick({R.id.back_press, R.id.order_container, R.id.random_container})
    public void onClick(View view) {
        int id = view.getId();
        Bundle bundle = new Bundle();

        switch (id) {
            case R.id.back_press:
                onBackPressed();
                break;
            case R.id.order_container:
                bundle.putInt("ctzl", 1);//个人错题
                hop2Activity(Ac_review_frame.class, bundle);
                break;
            case R.id.random_container:
                bundle.putInt("ctzl", 2);//全员错题
                hop2Activity(Ac_review_frame.class, bundle);
                break;
        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, Ac_review_mode.class);
        activity.startActivity(intent);
    }
}

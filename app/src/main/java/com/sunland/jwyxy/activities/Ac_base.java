package com.sunland.jwyxy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sunland.jwyxy.MyApplication;
import com.sunland.jwyxy.R;

import butterknife.ButterKnife;

public abstract class Ac_base extends AppCompatActivity {

    public Toolbar toolbar;
    public LinearLayout container;
    public LinearLayout rootview;
    public MyApplication mApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_base_test);
        mApplication = (MyApplication) getApplication();
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.container);
        rootview = findViewById(R.id.rootView);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.inflate(setToolbarLayout(), toolbar, true);
        layoutInflater.inflate(setContentLayut(), container, true);
        ButterKnife.bind(this);
    }

    public abstract int setToolbarLayout();

    public abstract int setContentLayut();

    public void hop2Activity(Class<? extends Ac_base> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void hop2Activity(Class<? extends Ac_base> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

}

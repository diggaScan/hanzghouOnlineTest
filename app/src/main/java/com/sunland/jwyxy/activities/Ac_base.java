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
import com.sunland.jwyxy.utils.DialogUtils;

import butterknife.ButterKnife;
import cn.com.cybertech.pdk.OperationLog;

public abstract class Ac_base extends AppCompatActivity {

    public Toolbar toolbar;
    public LinearLayout container;
    public LinearLayout rootview;
    public MyApplication mApplication;
    public DialogUtils dialogUtils;

    private LayoutInflater layoutInflater;

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
        layoutInflater = LayoutInflater.from(this);
        dialogUtils = DialogUtils.getInstance();
    }

    public void setToolbarLayout(int layout) {
        layoutInflater.inflate(layout, toolbar, true);
    }

    public void setContentLayout(int layout) {
        layoutInflater.inflate(layout, container, true);
        ButterKnife.bind(this);
    }


    public void saveLog(int operateType, int operationResult, String operateCondition) {
        OperationLog.saveLog(this
                , getTitle().toString()
                , "com.sunland.jwyxy"
                , "jwyxy"
                , operateType
                , OperationLog.OperationResult.CODE_SUCCESS
                , 1
                , operateCondition);
    }

    public String appendString(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
            if (i != strings.length - 1) {
                sb.append("@");
            }
        }
        return sb.toString();
    }

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

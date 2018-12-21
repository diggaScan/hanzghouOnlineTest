package com.sunland.jwyxy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.jwyxy.R;
import com.sunland.jwyxy.V_config;
import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_paper_list.PaperInfo;
import com.sunland.jwyxy.bean.i_paper_list.PaperListReqBean;
import com.sunland.jwyxy.bean.i_paper_list.PaperListResBean;
import com.sunland.jwyxy.config_utils.recycler_config.MyAdapter;
import com.sunland.jwyxy.config_utils.recycler_config.Rv_Item_decoration;
import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_paper_list extends Ac_base_query {

    @BindView(R.id.toolbar_title)
    public TextView toolbar_title;
    @BindView(R.id.back_press)
    public ImageView iv_back;
    @BindView(R.id.recycle_view)
    public RecyclerView rv_papers;

    private List<PaperInfo> paper_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarLayout(R.layout.toolbar_test_main);
        setContentLayout(R.layout.ac_paper_list);
        initToolbar();
        handleIntent();
        queryHzydjw(V_config.PAPER_LIST);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {

            }
        }
    }

    @Override
    public BaseRequestBean assembleRequestObj(String reqName) {
        PaperListReqBean paperListReqBean = new PaperListReqBean();
        assembleBasicObj(paperListReqBean);
        paperListReqBean.setJyid(V_config.jyid);
        return paperListReqBean;
    }

    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        PaperListResBean paperListResBean = (PaperListResBean) bean;
        if (paperListResBean == null) {
            Toast.makeText(this, "后台接口异常", Toast.LENGTH_SHORT).show();
            return;
        }
        paper_list = paperListResBean.getPaperInfo();
        if (paper_list == null || paper_list.isEmpty()) {
            Toast.makeText(this, "试卷列表为空", Toast.LENGTH_SHORT).show();
            return;
        }
        initRecycleView();
    }

    private void initToolbar() {
        toolbar_title.setText("真题测验");
        toolbar_title.setTextColor(Color.BLACK);
        iv_back.setImageResource(R.drawable.ic_arrow_back_black);
    }

    public void initRecycleView() {
        MyAdapter myAdapter = new MyAdapter(this, paper_list);
        myAdapter.setOnItemClickedListener(new MyAdapter.OnItemClickedListener() {
            @Override
            public void onClicked(int position) {
                PaperInfo paperInfo = paper_list.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("xfsjid", paperInfo.getXfsjid());
                bundle.putString("sjmc", paperInfo.getSjmc());
                bundle.putInt("kssc", paperInfo.getKssc());
                bundle.putInt("jgx", paperInfo.getJgx());
                hop2Activity(Ac_paper_frame.class, bundle);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_papers.setLayoutManager(llm);
        rv_papers.setAdapter(myAdapter);
        rv_papers.addItemDecoration(new Rv_Item_decoration(this));
    }


    @OnClick(R.id.back_press)
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back_press:
                onBackPressed();
                break;
        }
    }

}

package com.sunland.jwyxy.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunland.jwyxy.V_config;
import com.sunland.jwyxy.LocalInfo;
import com.sunland.jwyxy.R;
import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_history_paper.HistoryPaperInfo;
import com.sunland.jwyxy.bean.i_history_paper.HistoryPaperReqBean;
import com.sunland.jwyxy.bean.i_history_paper.HistoryPaperResBean;
import com.sunland.jwyxy.config_utils.recycler_config.Rv_Item_decoration;
import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_history extends Ac_base_query {

    @BindView(R.id.toolbar_title)
    public TextView toolbar_title;
    @BindView(R.id.back_press)
    public ImageView iv_back;
    @BindView(R.id.history_papers)
    public RecyclerView rv_papers;

    private List<HistoryPaperInfo> paper_list;
    private MyRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        queryHzydjw(V_config.HISTORY_PAPER);
    }

    private void initToolbar() {
        toolbar_title.setText("过往试卷");
        toolbar_title.setTextColor(Color.BLACK);
        iv_back.setImageResource(R.drawable.ic_arrow_back_black);
    }

    @Override
    public BaseRequestBean assembleRequestObj(String reqName) {
        HistoryPaperReqBean historyPaperReqBean = new HistoryPaperReqBean();
        assembleBasicObj(historyPaperReqBean);
        historyPaperReqBean.setJyid(LocalInfo.jyid);
        return historyPaperReqBean;
    }

    @Override
    public int setToolbarLayout() {
        return R.layout.toolbar_test_main;
    }

    @Override
    public int setContentLayut() {
        return R.layout.ac_history;
    }

    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        HistoryPaperResBean historyPaperResBean = (HistoryPaperResBean) bean;
        paper_list = historyPaperResBean.getHistoryPaperInfo();
        initRv();
    }

    private void initRv() {
        adapter = new MyRvAdapter(paper_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_papers.setLayoutManager(manager);
        rv_papers.setAdapter(adapter);
        rv_papers.addItemDecoration(new Rv_Item_decoration(this));
    }


    @OnClick(R.id.back_press)
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back_press:
                finish();
                break;
        }
    }

    class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.MyViewHolder> {
        List<HistoryPaperInfo> dataSet;
        LayoutInflater inflater;

        public MyRvAdapter(List<HistoryPaperInfo> dataSet) {
            super();
            this.dataSet = dataSet;
            inflater = getLayoutInflater();
        }

        @NonNull
        @Override
        public MyRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.rv_history_paper_item, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRvAdapter.MyViewHolder myViewHolder, int i) {
            HistoryPaperInfo info = dataSet.get(i);
            myViewHolder.tv_dtsj.setText(info.getDtsj());
            myViewHolder.tv_kscj.setText(info.getKscj() + "分");
            myViewHolder.tv_ksms.setText(info.getKsms() + "分钟");
            if (info.getSftg() == 1) {
                myViewHolder.tv_sftg.setText("未通过");
                myViewHolder.tv_sftg.setTextColor(Color.RED);
            } else {
                myViewHolder.tv_sftg.setText("通过");
                myViewHolder.tv_sftg.setTextColor(getResources().getColor(R.color.light_green));
            }

            myViewHolder.tv_sjmc.setText(info.getSjmc());
            myViewHolder.tv_sjzf.setText(info.getSjzf() + "分");
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_sjmc;
            TextView tv_kscj;
            TextView tv_dtsj;
            TextView tv_ksms;
            TextView tv_sjzf;
            TextView tv_sftg;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_sjmc = itemView.findViewById(R.id.paper_title);
                tv_kscj = itemView.findViewById(R.id.kscj);
                tv_dtsj = itemView.findViewById(R.id.dtsj);
                tv_ksms = itemView.findViewById(R.id.ksms);
                tv_sjzf = itemView.findViewById(R.id.sjzf);
                tv_sftg = itemView.findViewById(R.id.sftg);
            }
        }
    }
}

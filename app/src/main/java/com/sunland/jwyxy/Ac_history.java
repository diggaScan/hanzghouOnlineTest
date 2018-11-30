package com.sunland.jwyxy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_history_paper.HistoryPaperInfo;
import com.sunland.jwyxy.bean.i_history_paper.HistoryPaperReqBean;
import com.sunland.jwyxy.bean.i_history_paper.HistoryPaperResBean;
import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

public class Ac_history extends Ac_base_query {

    private List<HistoryPaperInfo> paper_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryHzydjw(Dictionary.HISTORY_PAPER);
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
        HistoryPaperResBean historyPaperResBean = new HistoryPaperResBean();
        paper_list = historyPaperResBean.getHistoryPaperInfo();
    }

    class MyRvAdpter extends RecyclerView.Adapter<MyRvAdpter.MyViewHolder> {
        List<HistoryPaperInfo> dataSet;
        LayoutInflater inflater;

        public MyRvAdpter(List<HistoryPaperInfo> dataSet) {
            super();
            this.dataSet = dataSet;
            inflater = getLayoutInflater();
        }

        @NonNull
        @Override
        public MyRvAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.rv_history_paper_item, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRvAdpter.MyViewHolder myViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}

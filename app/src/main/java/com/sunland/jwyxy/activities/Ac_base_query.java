package com.sunland.jwyxy.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunland.jwyxy.V_config;
import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_error_question.ErrorQuestionResBean;
import com.sunland.jwyxy.bean.i_history_paper.HistoryPaperResBean;
import com.sunland.jwyxy.bean.i_paper_detail.PaperDetailResBean;
import com.sunland.jwyxy.bean.i_paper_list.PaperListResBean;
import com.sunland.jwyxy.bean.i_person_stats.PersonStatsResBean;
import com.sunland.jwyxy.bean.i_submit_error.SubmitErrorResBean;
import com.sunland.jwyxy.bean.i_submit_paper.SubmitPaperResBean;
import com.sunlandgroup.Global;
import com.sunlandgroup.def.bean.result.ResultBase;
import com.sunlandgroup.network.OnRequestCallback;
import com.sunlandgroup.network.RequestManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Ac_base_query extends Ac_base implements OnRequestCallback {

    public RequestManager mRequestManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestManager = new RequestManager(this, this);
    }

    public void queryHzydjw(String reqName) {
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName
                , assembleRequestObj(reqName), 15000);
        mRequestManager.postRequest();
    }

    public void queryHzydjwNoDialog(String reqName) {
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName
                , assembleRequestObj(reqName), 15000);
        mRequestManager.postRequestWithoutDialog();
    }

    public void assembleBasicObj(BaseRequestBean requestBean) {
        requestBean.setYhdm(V_config.YHDM);
        requestBean.setImei(V_config.imei);
        requestBean.setImsi(V_config.imsi1);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pda_time = simpleDateFormat.format(date);
        requestBean.setPdaTime(pda_time);
        requestBean.setGpsX(V_config.gpsX);
        requestBean.setGpsY(V_config.gpsY);
    }

    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        onDataResponse(reqId, reqName, (ResultBase) bean);
    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        switch (reqName) {
            case V_config.PERSON_STATS:
                return PersonStatsResBean.class;
            case V_config.PAPER_LIST:
                return PaperListResBean.class;
            case V_config.PAPER_DETAIL:
                return PaperDetailResBean.class;
            case V_config.ERROR_QUESTION:
                return ErrorQuestionResBean.class;
            case V_config.SUBMIT_PAPER_INFO:
                return SubmitPaperResBean.class;
            case V_config.HISTORY_PAPER:
                return HistoryPaperResBean.class;
            case V_config.SUBMIT_ERROR_QUIZS:
                return SubmitErrorResBean.class;
        }
        return null;
    }


    public abstract BaseRequestBean assembleRequestObj(String reqName);

    public abstract void onDataResponse(String reqId, String reqName, ResultBase bean);
}

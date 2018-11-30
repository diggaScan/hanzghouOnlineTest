package com.sunland.jwyxy;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_error_question.ErrorQuestionResBean;
import com.sunland.jwyxy.bean.i_history_paper.HistoryPaperResBean;
import com.sunland.jwyxy.bean.i_paper_detail.PaperDetailResBean;
import com.sunland.jwyxy.bean.i_paper_list.PaperListResBean;
import com.sunland.jwyxy.bean.i_person_stats.PersonStatsResBean;
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

    public void assembleBasicObj(BaseRequestBean baseRequestBean) {
        baseRequestBean.setYhdm("test");
        baseRequestBean.setImei(Global.imei);
        baseRequestBean.setImsi(Global.imsi1);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pda_time = simpleDateFormat.format(date);
        baseRequestBean.setPdaTime(pda_time);
        baseRequestBean.setGpsx("");
        baseRequestBean.setGpsY("");
    }

    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        onDataResponse(reqId, reqName, (ResultBase) bean);
    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        switch (reqName) {
            case Dictionary.PERSON_STATS:
                return PersonStatsResBean.class;
            case Dictionary.PAPER_LIST:
                return PaperListResBean.class;
            case Dictionary.PAPER_DETAIL:
                return PaperDetailResBean.class;
            case Dictionary.ERROR_QUESTION:
                return ErrorQuestionResBean.class;
            case Dictionary.SUBMIT_PAPER_INFO:
                return SubmitPaperResBean.class;
            case Dictionary.HISTORY_PAPER:
                return HistoryPaperResBean.class;
        }
        return null;
    }

    @Override
    public int setToolbarLayout() {
        return 0;
    }

    @Override
    public int setContentLayut() {
        return 0;
    }

    public abstract BaseRequestBean assembleRequestObj(String reqName);

    public abstract void onDataResponse(String reqId, String reqName, ResultBase bean);
}

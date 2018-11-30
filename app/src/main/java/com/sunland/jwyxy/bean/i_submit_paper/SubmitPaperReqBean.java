package com.sunland.jwyxy.bean.i_submit_paper;

import java.util.List;

public class SubmitPaperReqBean {
    private String jyid;//警员id
    private String jymc;// 警员名称
    private int xfsjid;// 下发试卷id
    private List<SubmitQuestionInfo> submitQuestionInfo;//试题题目信息列表

    public String getJyid() {
        return jyid;
    }

    public void setJyid(String jyid) {
        this.jyid = jyid;
    }

    public String getJymc() {
        return jymc;
    }

    public void setJymc(String jymc) {
        this.jymc = jymc;
    }

    public int getXfsjid() {
        return xfsjid;
    }

    public void setXfsjid(int xfsjid) {
        this.xfsjid = xfsjid;
    }

    public List<SubmitQuestionInfo> getSubmitQuestionInfo() {
        return submitQuestionInfo;
    }

    public void setSubmitQuestionInfo(List<SubmitQuestionInfo> submitQuestionInfo) {
        this.submitQuestionInfo = submitQuestionInfo;
    }
}

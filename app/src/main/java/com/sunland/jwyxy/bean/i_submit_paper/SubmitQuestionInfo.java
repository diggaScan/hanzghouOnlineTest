package com.sunland.jwyxy.bean.i_submit_paper;

public class SubmitQuestionInfo {
    private int tmid; //题目id
    private String daxx;//答案选项（例：单选：A，多选：AB）

    public int getTmid() {
        return tmid;
    }

    public void setTmid(int tmid) {
        this.tmid = tmid;
    }

    public String getDaxx() {
        return daxx;
    }

    public void setDaxx(String daxx) {
        this.daxx = daxx;
    }
}

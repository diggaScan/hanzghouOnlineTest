package com.sunland.jwyxy.bean.i_submit_paper;

public class ResultQuestionInfo {
    private int tmid;//	 题目id
    private String zqxx;//	正确的选项id：A\B\C
    private int sfzq;// 是否是正确答案,0表示为错误，1表示为正确.

    public int getTmid() {
        return tmid;
    }

    public void setTmid(int tmid) {
        this.tmid = tmid;
    }

    public String getZqxx() {
        return zqxx;
    }

    public void setZqxx(String zqxx) {
        this.zqxx = zqxx;
    }

    public int getSfzq() {
        return sfzq;
    }

    public void setSfzq(int sfzq) {
        this.sfzq = sfzq;
    }
}

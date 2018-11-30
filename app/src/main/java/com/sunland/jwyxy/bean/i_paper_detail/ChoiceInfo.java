package com.sunland.jwyxy.bean.i_paper_detail;

public class ChoiceInfo {
    private String xxid;//	选项A\B\C
    private String xxnr;//	 选项内容
    private int sfzq;//	 是否是正确答案,0表示为错误，1表示为正确.

    public String getXxid() {
        return xxid;
    }

    public void setXxid(String xxid) {
        this.xxid = xxid;
    }

    public String getXxnr() {
        return xxnr;
    }

    public void setXxnr(String xxnr) {
        this.xxnr = xxnr;
    }

    public int getSfzq() {
        return sfzq;
    }

    public void setSfzq(int sfzq) {
        this.sfzq = sfzq;
    }
}

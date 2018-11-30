package com.sunland.jwyxy.bean.i_paper_list;

public class PaperInfo {
    private String sjmc;// 试卷名称
    private int xfsjid;//	 下发试卷id
    private int tmsl;// 题目数量
    private int sjzf;// 试卷总分
    private String kksj;//	 开考时间(考生被允许答题的最早时间，不是考生答题开始的时间)
    private String jssj;// 考试结束时间(考生被允许答题的最晚时间，不是考生答题结束时的时间)
    private int kssc;// 考试时长
    String sjlx;// 试卷类型
    private int jgx;// 试卷及格分数线

    public String getSjmc() {
        return sjmc;
    }

    public void setSjmc(String sjmc) {
        this.sjmc = sjmc;
    }

    public int getXfsjid() {
        return xfsjid;
    }

    public void setXfsjid(int xfsjid) {
        this.xfsjid = xfsjid;
    }

    public int getTmsl() {
        return tmsl;
    }

    public void setTmsl(int tmsl) {
        this.tmsl = tmsl;
    }

    public int getSjzf() {
        return sjzf;
    }

    public void setSjzf(int sjzf) {
        this.sjzf = sjzf;
    }

    public String getKksj() {
        return kksj;
    }

    public void setKksj(String kksj) {
        this.kksj = kksj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public int getKssc() {
        return kssc;
    }

    public void setKssc(int kssc) {
        this.kssc = kssc;
    }

    public String getSjlx() {
        return sjlx;
    }

    public void setSjlx(String sjlx) {
        this.sjlx = sjlx;
    }

    public int getJgx() {
        return jgx;
    }

    public void setJgx(int jgx) {
        this.jgx = jgx;
    }
}

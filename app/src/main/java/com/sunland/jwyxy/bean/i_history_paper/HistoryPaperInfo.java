package com.sunland.jwyxy.bean.i_history_paper;

public class HistoryPaperInfo {
    private int sjid;//Int 试卷id
    private String sjmc;//	String 试卷名称
    private String kscj;//	long考试成绩
    private String dtsj;//	String 答题时间点（考生作答的时间）
    private long ksms;//long考试耗时
    private int sjzf;//	int 试卷总分
    private int sftg;//	long是否通过（0：通过，1：未通过）

    public int getSjid() {
        return sjid;
    }

    public void setSjid(int sjid) {
        this.sjid = sjid;
    }

    public String getSjmc() {
        return sjmc;
    }

    public void setSjmc(String sjmc) {
        this.sjmc = sjmc;
    }

    public String getKscj() {
        return kscj;
    }

    public void setKscj(String kscj) {
        this.kscj = kscj;
    }

    public String getDtsj() {
        return dtsj;
    }

    public void setDtsj(String dtsj) {
        this.dtsj = dtsj;
    }

    public long getKsms() {
        return ksms;
    }

    public void setKsms(long ksms) {
        this.ksms = ksms;
    }

    public int getSjzf() {
        return sjzf;
    }

    public void setSjzf(int sjzf) {
        this.sjzf = sjzf;
    }

    public int getSftg() {
        return sftg;
    }

    public void setSftg(int sftg) {
        this.sftg = sftg;
    }
}

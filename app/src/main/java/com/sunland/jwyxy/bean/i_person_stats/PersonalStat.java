package com.sunland.jwyxy.bean.i_person_stats;

public class PersonalStat {
    private int dtzs;// 答题总数
    private int yzsj;//	 已做试卷数量
    private String pjcj;//	 平均成绩
    private String zql;// 正确率
    private String kshs;//	 平均考试耗时

    public int getDtzs() {
        return dtzs;
    }

    public void setDtzs(int dtzs) {
        this.dtzs = dtzs;
    }

    public int getYzsj() {
        return yzsj;
    }

    public void setYzsj(int yzsj) {
        this.yzsj = yzsj;
    }

    public String getPjcj() {
        return pjcj;
    }

    public void setPjcj(String pjcj) {
        this.pjcj = pjcj;
    }

    public String getZql() {
        return zql;
    }

    public void setZql(String zql) {
        this.zql = zql;
    }

    public String getKshs() {
        return kshs;
    }

    public void setKshs(String kshs) {
        this.kshs = kshs;
    }
}

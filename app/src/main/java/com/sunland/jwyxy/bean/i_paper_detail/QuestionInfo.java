package com.sunland.jwyxy.bean.i_paper_detail;

import java.util.List;

public class QuestionInfo implements Comparable<QuestionInfo> {
    private int tmid;// 题目id
    private String tmmc;//题目名称（题干）
    private String tmlx;// 题目类型,
    private String tifl;// 题型分类1单选2多选3判断
    private int tmxh;//题目序号
    private List<ChoiceInfo> choiceInfo;//选项信息列表

    public int getTmxh() {
        return tmxh;
    }

    public void setTmxh(int tmxh) {
        this.tmxh = tmxh;
    }

    public int getTmid() {
        return tmid;
    }

    public void setTmid(int tmid) {
        this.tmid = tmid;
    }

    public String getTmmc() {
        return tmmc;
    }

    public void setTmmc(String tmmc) {
        this.tmmc = tmmc;
    }

    public String getTmlx() {
        return tmlx;
    }

    public void setTmlx(String tmlx) {
        this.tmlx = tmlx;
    }

    public String getTifl() {
        return tifl;
    }

    public void setTifl(String tifl) {
        this.tifl = tifl;
    }

    public List<ChoiceInfo> getChoiceInfo() {
        return choiceInfo;
    }

    public void setChoiceInfo(List<ChoiceInfo> choiceInfo) {
        this.choiceInfo = choiceInfo;
    }

    @Override
    public int compareTo(QuestionInfo o) {
        if (this.tmxh==0) {
            return o.tifl.compareTo(this.tifl);
        }else {
            return this.tmxh-o.tmxh;
        }

    }
}

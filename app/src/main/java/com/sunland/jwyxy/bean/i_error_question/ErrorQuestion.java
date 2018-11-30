package com.sunland.jwyxy.bean.i_error_question;

import com.sunland.jwyxy.bean.i_paper_detail.ChoiceInfo;

import java.util.List;

public class ErrorQuestion {
    private int tmid;// 题目id
    private String tmmc;// 题目名称（题干）
    private String tmlx;// 题目类型
    private String tifl;// 题型分类
    private List<ChoiceInfo> choiceInfo;// 选项信息列表

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
}

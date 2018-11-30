package com.sunland.jwyxy.bean.i_error_question;

import com.sunland.jwyxy.bean.BaseRequestBean;

public class ErrorQuestionReqBean extends BaseRequestBean {
    private String jyid;    //警员id
    private int ctzl;// 错题种类 1代表个人错题，2代表全部人员错题，目前就这两种。

    public String getJyid() {
        return jyid;
    }

    public void setJyid(String jyid) {
        this.jyid = jyid;
    }

    public int getCtzl() {
        return ctzl;
    }

    public void setCtzl(int ctzl) {
        this.ctzl = ctzl;
    }
}

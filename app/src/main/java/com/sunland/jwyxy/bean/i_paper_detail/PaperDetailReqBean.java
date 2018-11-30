package com.sunland.jwyxy.bean.i_paper_detail;

import com.sunland.jwyxy.bean.BaseRequestBean;

public class PaperDetailReqBean extends BaseRequestBean {
    private String jyid;//	 警员id
    private int xfsjid;// 下发试卷id

    public String getJyid() {
        return jyid;
    }

    public void setJyid(String jyid) {
        this.jyid = jyid;
    }

    public int getXfsjid() {
        return xfsjid;
    }

    public void setXfsjid(int xfsjid) {
        this.xfsjid = xfsjid;
    }
}

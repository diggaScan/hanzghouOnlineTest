package com.sunland.jwyxy.bean.i_paper_list;

import com.sunland.jwyxy.bean.BaseRequestBean;

public class PaperListReqBean extends BaseRequestBean {
    private String jyid;// 警员id

    public String getJyid() {
        return jyid;
    }

    public void setJyid(String jyid) {
        this.jyid = jyid;
    }
}

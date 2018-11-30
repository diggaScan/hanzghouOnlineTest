package com.sunland.jwyxy.bean.i_person_stats;

import com.sunland.jwyxy.bean.BaseRequestBean;

public class PersonStatsReqBean extends BaseRequestBean {
    private String jymc;
    private String jyid;

    public String getJymc() {
        return jymc;
    }

    public void setJymc(String jymc) {
        this.jymc = jymc;
    }

    public String getJyid() {
        return jyid;
    }

    public void setJyid(String jyid) {
        this.jyid = jyid;
    }
}

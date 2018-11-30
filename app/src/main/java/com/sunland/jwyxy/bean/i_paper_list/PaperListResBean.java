package com.sunland.jwyxy.bean.i_paper_list;

import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

public class PaperListResBean extends ResultBase {
    private List<PaperInfo> paperInfo;

    public List<PaperInfo> getPaperInfo() {
        return paperInfo;
    }

    public void setPaperInfo(List<PaperInfo> paperInfo) {
        this.paperInfo = paperInfo;
    }
}

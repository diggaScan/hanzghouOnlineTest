package com.sunland.jwyxy.bean.i_history_paper;

import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

public class HistoryPaperResBean extends ResultBase {
    private List<HistoryPaperInfo> historyPaperInfo;

    public List<HistoryPaperInfo> getHistoryPaperInfo() {
        return historyPaperInfo;
    }

    public void setHistoryPaperInfo(List<HistoryPaperInfo> historyPaperInfo) {
        this.historyPaperInfo = historyPaperInfo;
    }
}

package com.sunland.jwyxy.bean.i_paper_detail;

import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

public class PaperDetailResBean extends ResultBase {
    private List<QuestionInfo> questionInfo;
    private int sjsclx;//（1：随机生成，20：自动生成+非乱序，21：自动生成+乱序，30：手动生成+非乱序，31：手动生成+乱序）

    public List<QuestionInfo> getQuestionInfo() {
        return questionInfo;
    }

    public void setQuestionInfo(List<QuestionInfo> questionInfo) {
        this.questionInfo = questionInfo;
    }

    public int getSjsclx() {
        return sjsclx;
    }

    public void setSjsclx(int sjlx) {
        this.sjsclx = sjlx;
    }
}

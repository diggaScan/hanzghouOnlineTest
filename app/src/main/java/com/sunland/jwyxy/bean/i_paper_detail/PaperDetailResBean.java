package com.sunland.jwyxy.bean.i_paper_detail;

import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

public class PaperDetailResBean extends ResultBase {
    private List<QuestionInfo> questionInfo;

    public List<QuestionInfo> getQuestionInfo() {
        return questionInfo;
    }

    public void setQuestionInfo(List<QuestionInfo> questionInfo) {
        this.questionInfo = questionInfo;
    }
}

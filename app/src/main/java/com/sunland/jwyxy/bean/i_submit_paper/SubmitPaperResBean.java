package com.sunland.jwyxy.bean.i_submit_paper;

import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

public class SubmitPaperResBean extends ResultBase {
    public String kscj;// 考试成绩
    public List<ResultQuestionInfo> resultQuestionInfo;// 批改后的每一题的结果信息

    public String getKscj() {
        return kscj;
    }

    public void setKscj(String kscj) {
        this.kscj = kscj;
    }

    public List<ResultQuestionInfo> getResultQuestionInfo() {
        return resultQuestionInfo;
    }

    public void setResultQuestionInfo(List<ResultQuestionInfo> resultQuestionInfo) {
        this.resultQuestionInfo = resultQuestionInfo;
    }
}

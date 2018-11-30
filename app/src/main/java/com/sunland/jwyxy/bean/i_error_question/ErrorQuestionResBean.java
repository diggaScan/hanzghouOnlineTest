package com.sunland.jwyxy.bean.i_error_question;

import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

public class ErrorQuestionResBean  extends ResultBase {
    private List<ErrorQuestion> errorQuestionInfo;

    public List<ErrorQuestion> getErrorQuestionInfo() {
        return errorQuestionInfo;
    }

    public void setErrorQuestionInfo(List<ErrorQuestion> errorQuestionInfo) {
        this.errorQuestionInfo = errorQuestionInfo;
    }
}

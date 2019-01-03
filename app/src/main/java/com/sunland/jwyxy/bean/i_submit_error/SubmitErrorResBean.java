package com.sunland.jwyxy.bean.i_submit_error;

import com.sunland.jwyxy.bean.i_submit_paper.ResultQuestionInfo;
import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.List;

public class SubmitErrorResBean extends ResultBase {
    private List<ResultQuestionInfo> resultQuestionInfo;//	List 批改后的每一题的结果信息

    public List<ResultQuestionInfo> getResultQuestionInfo() {
        return resultQuestionInfo;
    }

    public void setResultQuestionInfo(List<ResultQuestionInfo> resultQuestionInfo) {
        this.resultQuestionInfo = resultQuestionInfo;
    }
}

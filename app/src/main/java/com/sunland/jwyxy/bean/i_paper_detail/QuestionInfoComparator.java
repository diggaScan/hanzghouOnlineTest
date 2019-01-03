package com.sunland.jwyxy.bean.i_paper_detail;

import java.util.Comparator;

public class QuestionInfoComparator implements Comparator<QuestionInfo> {
    @Override
    public int compare(QuestionInfo o1, QuestionInfo o2) {
        if (o1.getTifl().equals("3")) {
            return -1;
        }
        if (o2.getTifl().equals("3")) {
            return 1;
        }
        return o1.getTifl().compareTo(o2.getTifl());
    }
}

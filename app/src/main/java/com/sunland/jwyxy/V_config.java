package com.sunland.jwyxy;

import android.os.Build;

public class V_config {


    // 1.考试个人统计信息
    public static final String PERSON_STATS = "queryPersonalStatsInfo";
    // 2. 获取考试列表信息
    public static final String PAPER_LIST = "queryPaperList";
    // 3. 查询试卷详情接口
    public static final String PAPER_DETAIL = "queryPaperDetail";
    // 4. 查询错题信息列表
    public static final String ERROR_QUESTION = "queryErrorQuestionList";
    // 5. 提交试卷信息
    public static final String SUBMIT_PAPER_INFO = "savePaperInfo";
    // 6. 查询个人历史试卷信息
    public static final String HISTORY_PAPER = "queryHisPaperInfo";
    // 7. 登录接口
    public final static String USER_LOGIN = "userLogin";
    // 8. 免密登录接口
    public final static String MM_USER_LOGIN = "userMMLogin";
    // 9. 错题练习提交接口
    public final static String SUBMIT_ERROR_QUIZS = "practiceErrorQuestion";

    //本机信息
    public final static String BRAND = Build.BRAND;//手机品牌
    public final static String MODEL = Build.MODEL + " " + Build.VERSION.SDK_INT;//手机型号
    public final static String OS = "android" + Build.VERSION.SDK_INT;//手机操作系统
    public static String imei = "";
    public static String imsi1 = " ";
    public static String imsi2 = "";
    public static String gpsX = "";//经度
    public static String gpsY = "";//纬度
    public static String gpsinfo = gpsX + gpsY;
    //用户代码
    public static String YHDM;
    public final static String DLMK = "警务云学院";
    //    public static String jyid = "012146";//测试用111772
    public static String JYMC;//测试用

}

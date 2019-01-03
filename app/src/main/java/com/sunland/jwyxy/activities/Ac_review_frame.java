package com.sunland.jwyxy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.jwyxy.Frg_error_quiz;
import com.sunland.jwyxy.Frg_quiz;
import com.sunland.jwyxy.R;
import com.sunland.jwyxy.V_config;
import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_error_question.ErrorQuestion;
import com.sunland.jwyxy.bean.i_error_question.ErrorQuestionReqBean;
import com.sunland.jwyxy.bean.i_error_question.ErrorQuestionResBean;
import com.sunland.jwyxy.bean.i_submit_error.SubmitErrorReqBean;
import com.sunland.jwyxy.bean.i_submit_error.SubmitErrorResBean;
import com.sunland.jwyxy.bean.i_submit_paper.ResultQuestionInfo;
import com.sunland.jwyxy.bean.i_submit_paper.SubmitQuestionInfo;
import com.sunland.jwyxy.config_utils.DialogUtils;
import com.sunland.jwyxy.config_utils.viewpager_config.MyFragmentAdapter;
import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_review_frame extends Ac_base_query implements Frg_quiz.CommChannel {

    public static final String FLAG = "Ac_review_frame";

    private String mTitle;

    @BindView(R.id.toolbar_title)
    public TextView toolbar_title;
    @BindView(R.id.back_press)
    public ImageView iv_back;
    @BindView(R.id.quiz_container)
    public ViewPager vp_container;
    @BindView(R.id.count_down)
    public TextView tv_count_down;
    @BindView(R.id.submit)
    public Button btn_submit;
    @BindView(R.id.answer_tab)
    public ImageView iv_tab;
    @BindView(R.id.loading_layout)
    public FrameLayout loading_layout;
    private MyFragmentAdapter<Frg_error_quiz> myFragmentAdapter;
    private DialogUtils dialogUtils;
    private boolean hasGetAnswers;

    private HashMap<Integer, Integer> answer_sheet = new HashMap<>();

    private int[] answers;
    private List<String> kinds;
    private int total_quizzes;
    private List<String> titles;
    private List<String[]> choices;
    private int paper_code;
    private List<Frg_error_quiz> fragments;
    private List<SubmitQuestionInfo> answer_list;//提交的错题答案列表
    private int ctzl;
    private List<ErrorQuestion> question_list;
    private SparseArray<SubmitQuestionInfo> sparseArray = new SparseArray<>();
    private List<ResultQuestionInfo> result_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarLayout(R.layout.toolbar_test_main);
        setContentLayout(R.layout.ac_paper_frame);
        question_list = new ArrayList<>();
        answer_list = new ArrayList<>();
        handIntent();
        dialogUtils = new DialogUtils(this);
        dialogUtils.initDialog();
        queryHzydjwNoDialog(V_config.ERROR_QUESTION);
        btn_submit.setVisibility(View.GONE);
        iv_back.setVisibility(View.GONE);
        initWidget();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDecorView();
    }

    @Override
    public BaseRequestBean assembleRequestObj(String reqName) {
        switch (reqName) {
            case V_config.ERROR_QUESTION:
                ErrorQuestionReqBean errorQuestionReqBean = new ErrorQuestionReqBean();
                assembleBasicObj(errorQuestionReqBean);
                errorQuestionReqBean.setCtzl(ctzl);
                errorQuestionReqBean.setJyid(V_config.YHDM);
                return errorQuestionReqBean;
            case V_config.SUBMIT_ERROR_QUIZS:
                SubmitErrorReqBean submitPaperReqBean = new SubmitErrorReqBean();
                assembleBasicObj(submitPaperReqBean);
                submitPaperReqBean.setErrType(Integer.valueOf(ctzl));
                submitPaperReqBean.setJyid(V_config.YHDM);
                for (int i = 0; i < sparseArray.size(); i++) {
                    answer_list.add(sparseArray.get(sparseArray.keyAt(i)));
                }
                submitPaperReqBean.setSubmitQuestionInfo(answer_list);
                return submitPaperReqBean;
        }
        return null;
    }

    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        switch (reqName) {
            case V_config.ERROR_QUESTION:
                btn_submit.setVisibility(View.VISIBLE);
                iv_back.setVisibility(View.VISIBLE);
                loading_layout.setVisibility(View.GONE);
                ErrorQuestionResBean errorQuestionResBean = (ErrorQuestionResBean) bean;
                if (errorQuestionResBean == null) {
                    Toast.makeText(this, "错题接口异常", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                question_list = errorQuestionResBean.getErrorQuestionInfo();
                if (question_list == null || question_list.isEmpty()) {
                    Toast.makeText(this, "无相关错题", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                } else {
                    initViewPager();
                }
                break;
            case V_config.SUBMIT_ERROR_QUIZS:
                hasGetAnswers = true;//已获取答案
                vp_container.setCurrentItem(0);
                iv_back.setVisibility(View.VISIBLE);
                loading_layout.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setText("确  定");
                SubmitErrorResBean submitErrorResBean = (SubmitErrorResBean) bean;
                if (submitErrorResBean == null) {
                    Toast.makeText(this, "后台接口异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                result_list = submitErrorResBean.getResultQuestionInfo();
                if (result_list == null || result_list.isEmpty()) {
                    Toast.makeText(this, "获取题目答案异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Frg_error_quiz frg_error_quiz : fragments) {
                    for (ResultQuestionInfo resultQuestionInfo : result_list) {
                        if (frg_error_quiz.tmid == resultQuestionInfo.getTmid()) {
                            frg_error_quiz.setResult(resultQuestionInfo.getSfzq(), resultQuestionInfo.getZqxx());
                        }
                    }
                }
                break;
        }
    }

    private void initDecorView() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN);
        toolbar.setFitsSystemWindows(false);
    }

    private void handIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                ctzl = bundle.getInt("ctzl", -1);
            }
        }
    }

    private void initWidget() {
        toolbar_title.setText(ctzl == 1 ? "个人错题" : "全员错题");
        toolbar_title.setTextColor(Color.BLACK);
        iv_back.setImageResource(R.drawable.ic_close_black);
        tv_count_down.setVisibility(View.GONE);
        iv_tab.setVisibility(View.GONE);
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        for (int i = 0; i < question_list.size(); i++) {
            Frg_error_quiz error_quiz = new Frg_error_quiz();
            error_quiz.setQuestion(question_list.get(i), i + 1, question_list.size());
            fragments.add(error_quiz);
        }
        myFragmentAdapter = new MyFragmentAdapter<>(getSupportFragmentManager(), fragments);
        vp_container.setAdapter(myFragmentAdapter);
        vp_container.setOffscreenPageLimit(question_list.size());
    }

    @Override
    public void addAnswer(final int position, int answer) {
        answer_sheet.put(position, answer);

    }

    @Override
    public void removeAnswer(int position) {

    }

    @Override
    public void scrollToNext(int position, boolean hasChoosen) {
        final int pos = position;
        if (position < total_quizzes && hasChoosen)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    vp_container.setCurrentItem(pos);
                }
            }, 200);
    }

    @Override
    public HashMap<Integer, Integer> getAnswerSheet() {
        return answer_sheet;
    }

    @Override
    public void notifyDataSetChange() {

    }

    @OnClick({R.id.back_press, R.id.submit, R.id.answer_tab})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back_press:
                exit();
                break;
            case R.id.submit:
                if (hasGetAnswers) {
                    exit();
                } else {
                    submit();
                }
                break;
        }
    }

    private void submit() {

        dialogUtils.setTitle("确定要提交试卷?");
        if (answer_sheet.size() < question_list.size()) {
            dialogUtils.setDescription("已做习题将被提交");
        } else {
            dialogUtils.setDescription("你已完成所有试题");
        }
        dialogUtils.setOnCancelListener(new DialogUtils.OnCancelListener() {
            @Override
            public void onCancel() {
                dialogUtils.dismiss();
            }
        });

        dialogUtils.setOnConfirmListener(new DialogUtils.onConfirmListener() {
            @Override
            public void onConfirm() {
                dialogUtils.dismiss();
                loading_layout.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.GONE);
                iv_back.setVisibility(View.GONE);
                queryHzydjwNoDialog(V_config.SUBMIT_ERROR_QUIZS);
            }
        });
        dialogUtils.show();
    }

    @Override
    public void submitAnswer(SubmitQuestionInfo submitQuestionInfo, int tmid) {
        sparseArray.put(tmid, submitQuestionInfo);
    }

    private void exit() {
        dialogUtils.setTitle("确定退出错题练习吗?");
        if (hasGetAnswers) {
            dialogUtils.setDescription("题目已提交");
        } else {
            dialogUtils.setDescription("答案将不会被提交");
        }

        dialogUtils.setOnCancelListener(new DialogUtils.OnCancelListener() {
            @Override
            public void onCancel() {
                dialogUtils.dismiss();
            }
        });
        dialogUtils.setOnConfirmListener(new DialogUtils.onConfirmListener() {
            @Override
            public void onConfirm() {
                dialogUtils.dismiss();
                finish();
            }
        });
        dialogUtils.show();
    }

    @Override
    public void onBackPressed() {
        exit();
    }
}

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunland.jwyxy.V_config;
import com.sunland.jwyxy.Frg_quiz;
import com.sunland.jwyxy.LocalInfo;
import com.sunland.jwyxy.R;
import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_paper_detail.PaperDetailReqBean;
import com.sunland.jwyxy.bean.i_paper_detail.PaperDetailResBean;
import com.sunland.jwyxy.bean.i_paper_detail.QuestionInfo;
import com.sunland.jwyxy.bean.i_submit_paper.ResultQuestionInfo;
import com.sunland.jwyxy.bean.i_submit_paper.SubmitPaperReqBean;
import com.sunland.jwyxy.bean.i_submit_paper.SubmitPaperResBean;
import com.sunland.jwyxy.bean.i_submit_paper.SubmitQuestionInfo;
import com.sunland.jwyxy.config_utils.DialogUtils;
import com.sunland.jwyxy.config_utils.MyCountDownTimer;
import com.sunland.jwyxy.config_utils.answer_sheet.AnswerSheet;
import com.sunland.jwyxy.config_utils.answer_sheet.OnRvItemClickedListener;
import com.sunland.jwyxy.config_utils.answer_sheet.Rv_answer_sheet_adapter;
import com.sunland.jwyxy.config_utils.viewpager_config.MyFragmentAdapter;
import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class Ac_paper_frame extends Ac_base_query implements Frg_quiz.CommChannel {

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
    public ImageView answer_tab;
    @BindView(R.id.loading_icon)
    public RelativeLayout rl_loading_icon;
    private boolean isTabClicked;
    private boolean hasGetAnswers;

    private MyCountDownTimer myCountDownTimer;
    private MyFragmentAdapter<Frg_quiz> myFragmentAdapter;
    private Rv_answer_sheet_adapter rvanswersheetadapter;

    private HashMap<Integer, Integer> answer_sheet = new HashMap<>();

    private SparseArray<SubmitQuestionInfo> sparseArray = new SparseArray<>();
    private AnswerSheet mSheet;
    private int xfsjid;
    private String mTitle;
    private int kssc;
    private List<Frg_quiz> fragments;
    private List<QuestionInfo> questionList;//试卷问题列表
    private List<SubmitQuestionInfo> answer_list;//提交的答案列表
    private List<ResultQuestionInfo> result_list;//批改后的题目对错列表
    private int sjsclx;//有序1 无序2
    private String kscj;//考试成绩
    private int jgx;//及格线


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        answer_list = new ArrayList<>();
        handIntent();
        initToolbar();
        questionList = new ArrayList<>();
        queryHzydjw(V_config.PAPER_DETAIL);
    }

    @Override
    public int setToolbarLayout() {
        return R.layout.toolbar_test_main;
    }

    @Override
    public int setContentLayut() {
        return R.layout.ac_paper_frame;
    }


    private void handIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                xfsjid = bundle.getInt("xfsjid", -1);
                mTitle = bundle.getString("sjmc");
                kssc = bundle.getInt("kssc");
                jgx = bundle.getInt("jgx");
            }
        }
    }

    @Override
    public BaseRequestBean assembleRequestObj(String reqName) {
        switch (reqName) {
            case V_config.PAPER_DETAIL:
                PaperDetailReqBean paperDetailReqBean = new PaperDetailReqBean();
                assembleBasicObj(paperDetailReqBean);
                paperDetailReqBean.setJyid(LocalInfo.jyid);
                paperDetailReqBean.setXfsjid(xfsjid);
                return paperDetailReqBean;
            case V_config.SUBMIT_PAPER_INFO:
                SubmitPaperReqBean submitPaperReqBean = new SubmitPaperReqBean();
                assembleBasicObj(submitPaperReqBean);
                submitPaperReqBean.setJyid(LocalInfo.jyid);
                submitPaperReqBean.setJymc(LocalInfo.jymc);
                submitPaperReqBean.setXfsjid(xfsjid);

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
            case V_config.PAPER_DETAIL:
                PaperDetailResBean paperDetailResBean = (PaperDetailResBean) bean;
                questionList = paperDetailResBean.getQuestionInfo();
                sjsclx = paperDetailResBean.getSjsclx();
                if (questionList == null || questionList.size() == 0) {
                    final DialogUtils dialogUtils = new DialogUtils(this);
                    dialogUtils.initDialog();
                    dialogUtils.setTitle("还未到开考时间");
                    dialogUtils.setDescription("请查看开考时间");
                    dialogUtils.setOnConfirmListener(new DialogUtils.onConfirmListener() {
                        @Override
                        public void onConfirm() {
                            dialogUtils.dismiss();
                            finish();
                        }
                    });
                    dialogUtils.show();
                } else {
                    initViewPager();
                    initAnswetSheet();
                    initCountDownTimer();
                }
                break;
            case V_config.SUBMIT_PAPER_INFO:
                hasGetAnswers = true;//已获取答案
                rl_loading_icon.setVisibility(View.GONE);
                SubmitPaperResBean submitPaperResBean = (SubmitPaperResBean) bean;
                if (submitPaperResBean == null) {
                    return;
                }
                result_list = submitPaperResBean.getResultQuestionInfo();
                kscj = submitPaperResBean.getKscj();
                for (Frg_quiz frg_quiz : fragments) {
                    for (ResultQuestionInfo resultQuestionInfo : result_list) {
                        if (frg_quiz.tmid == resultQuestionInfo.getTmid()) {
                            frg_quiz.setResult(resultQuestionInfo.getSfzq(), resultQuestionInfo.getZqxx());
                        }
                    }
                }
                btn_submit.setText("确  定");
                tv_count_down.setVisibility(View.GONE);
                final DialogUtils dialogUtils = new DialogUtils(this);
                dialogUtils.initDialog();
                dialogUtils.setTitle("本次测验成绩：" + submitPaperResBean.getKscj());
                dialogUtils.setOnConfirmListener(new DialogUtils.onConfirmListener() {
                    @Override
                    public void onConfirm() {
                        dialogUtils.dismiss();
                    }
                });
                if (submitPaperResBean.getKscj().compareTo(String.valueOf(jgx)) < 0) {
                    dialogUtils.setDescription("本次测试不及格！");
                } else {
                    dialogUtils.setDescription("本次测试通过！");
                }
                dialogUtils.show();
        }
    }


    private void initCountDownTimer() {
        // TODO: 2018/12/3/003 结束后强行提交
        myCountDownTimer = new MyCountDownTimer(kssc * 60 * 1000, 1000, Ac_paper_frame.this, tv_count_down);
        myCountDownTimer.start();
    }

    private void initDecorView() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN);
        toolbar.setFitsSystemWindows(false);
    }


    private void initToolbar() {
        toolbar_title.setText(mTitle);
        toolbar_title.setTextColor(Color.BLACK);
        iv_back.setImageResource(R.drawable.ic_close_black);
    }

    private void initViewPager() {
        fragments = new ArrayList<>();

        Collections.sort(questionList);

        int num = questionList.size();
        for (int i = 0; i < num; i++) {
            Frg_quiz frg_quiz = new Frg_quiz();
            QuestionInfo questionInfo = questionList.get(i);
            int tmid = questionInfo.getTmid();
            frg_quiz.setQuestion(questionInfo);

            //初始答案
            SubmitQuestionInfo info = new SubmitQuestionInfo();
            info.setTmid(tmid);
            info.setDaxx("");
            sparseArray.put(tmid, info);

            frg_quiz.setSequence(i + 1);
            frg_quiz.setNum(num);
            frg_quiz.setJgx(jgx);
            fragments.add(frg_quiz);
        }
        myFragmentAdapter = new MyFragmentAdapter<>(getSupportFragmentManager(), fragments);
        vp_container.setAdapter(myFragmentAdapter);
        vp_container.setOffscreenPageLimit(1000);
    }

    private void initAnswetSheet() {
        mSheet = new AnswerSheet(this);
        rvanswersheetadapter = new Rv_answer_sheet_adapter(this, answer_sheet, questionList.size());
        rvanswersheetadapter.setOnRvItemClickedListener(new OnRvItemClickedListener() {
            @Override
            public void onItemSelected(int position) {
                vp_container.setCurrentItem(position, false);
            }
        });
        mSheet.setAdapter(rvanswersheetadapter);
        mSheet.initSheet();
    }

    @Override
    public void submitAnswer(SubmitQuestionInfo submitQuestionInfo, int tmid) {
        sparseArray.put(tmid, submitQuestionInfo);
    }

    @Override
    public void addAnswer(final int position, int answer) {
        answer_sheet.put(position, answer);
    }

    @Override
    public void removeAnswer(int position) {
        answer_sheet.remove(position);
    }

    @Override
    public HashMap<Integer, Integer> getAnswerSheet() {
        return answer_sheet;
    }

    @Override
    public void scrollToNext(final int position, boolean hasChoosen) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vp_container.setCurrentItem(position);
            }
        }, 200);
    }

    @OnClick({R.id.back_press, R.id.submit, R.id.answer_tab})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back_press:
                if (isTabClicked) {
                    showAnswerSheet();
                }
                if (hasGetAnswers) {
                    exit();
                } else {
                    backPressed();
                }

                break;
            case R.id.submit:
                if (isTabClicked) {
                    showAnswerSheet();
                }
                if (hasGetAnswers) {
                    exit();
                } else {
                    submit();
                }
                break;
            case R.id.answer_tab:
                showAnswerSheet();
        }
    }

    private void exit() {
        final DialogUtils dialogUtils = new DialogUtils(this);
        dialogUtils.initDialog();
        dialogUtils.setTitle("确定退出吗?");
        dialogUtils.setDescription("本次考试已记录");
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
                hop2Activity(Ac_main.class);
                finish();
            }
        });
        dialogUtils.show();
    }

    public void backPressed() {
        final DialogUtils dialogUtils = new DialogUtils(this);
        dialogUtils.initDialog();
        dialogUtils.setTitle("请完成考试!");
        dialogUtils.setDescription("退出依旧会记录考试成绩");
        dialogUtils.setOnConfirmListener(new DialogUtils.onConfirmListener() {
            @Override
            public void onConfirm() {
                dialogUtils.dismiss();
            }
        });
        dialogUtils.show();
    }

    @Override
    public void notifyDataSetChange() {
        rvanswersheetadapter.notifyDataSetChanged();
    }

    private void submit() {
        final DialogUtils dialogUtils = new DialogUtils(this);
        dialogUtils.initDialog();
        dialogUtils.setTitle("确定要提交试卷!");
        if (answer_sheet.size() < questionList.size()) {
            dialogUtils.setDescription("你还有题目未完成");
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
                queryHzydjwNoDialog(V_config.SUBMIT_PAPER_INFO);
                rl_loading_icon.setVisibility(View.VISIBLE);
            }
        });
        dialogUtils.show();
    }

    private void showAnswerSheet() {
        if (isTabClicked) {
            answer_tab.setBackgroundResource(R.drawable.answer_sheet);
            mSheet.hide();
            isTabClicked = false;
        } else {
            answer_tab.setBackgroundResource(R.drawable.answer_sheet_clicked);
            mSheet.show();
            isTabClicked = true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        initDecorView();
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }
}

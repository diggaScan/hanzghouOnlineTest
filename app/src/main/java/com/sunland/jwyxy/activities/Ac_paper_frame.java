package com.sunland.jwyxy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.jwyxy.Frg_quiz;
import com.sunland.jwyxy.R;
import com.sunland.jwyxy.V_config;
import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_paper_detail.PaperDetailReqBean;
import com.sunland.jwyxy.bean.i_paper_detail.PaperDetailResBean;
import com.sunland.jwyxy.bean.i_paper_detail.QuestionInfo;
import com.sunland.jwyxy.bean.i_paper_detail.QuestionInfoComparator;
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
    @BindView(R.id.quiz_container)
    public ViewPager vp_container;
    @BindView(R.id.count_down)
    public TextView tv_count_down;
    @BindView(R.id.submit)
    public Button btn_submit;
    @BindView(R.id.answer_tab)
    public ImageView answer_tab;
    @BindView(R.id.loading_layout)
    public FrameLayout loading_layout;
    @BindView(R.id.back_press)
    public ImageView iv_back_press;
    private DialogUtils dialogUtils;
    private boolean isTabClicked;
    private boolean hasGetAnswers;
    private boolean submitOnDestroy;
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
    private boolean hasShownDialog;
    private Vibrator vibrator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarLayout(R.layout.toolbar_test_main);
        setContentLayout(R.layout.ac_paper_frame);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        answer_list = new ArrayList<>();
        handIntent();
        dialogUtils = new DialogUtils(this);
        dialogUtils.initDialog();
        initToolbar();
        questionList = new ArrayList<>();
        queryHzydjwNoDialog(V_config.PAPER_DETAIL);
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
                paperDetailReqBean.setJyid(V_config.YHDM);
                paperDetailReqBean.setXfsjid(xfsjid);
                return paperDetailReqBean;
            case V_config.SUBMIT_PAPER_INFO:
                SubmitPaperReqBean submitPaperReqBean = new SubmitPaperReqBean();
                assembleBasicObj(submitPaperReqBean);
                submitPaperReqBean.setJyid(V_config.YHDM);
                submitPaperReqBean.setJymc(V_config.JYMC);
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
                if (paperDetailResBean == null) {
                    loading_layout.setVisibility(View.GONE);
                    Toast.makeText(this, "后台接口异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                sjsclx = paperDetailResBean.getSjsclx();
                questionList = paperDetailResBean.getQuestionInfo();
                if (questionList == null || questionList.size() == 0) {
                    btn_submit.setVisibility(View.GONE);
                    answer_tab.setVisibility(View.GONE);
                    loading_layout.setVisibility(View.GONE);

                    dialogUtils.setTitle("还未到开考时间");
                    dialogUtils.setDescription("请查看开考时间");
                    dialogUtils.setOnConfirmListener(new DialogUtils.onConfirmListener() {
                        @Override
                        public void onConfirm() {
                            dialogUtils.dismiss();
                            finish();
                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogUtils.show();
                        }
                    }, 300);

                } else {
                    initViewPager();
                    initAnswetSheet();
                    initCountDownTimer();
                    loading_layout.setVisibility(View.GONE);
                }
                break;
            case V_config.SUBMIT_PAPER_INFO:
//                if (submitOnDestroy) {
//                    return;
//                }
                hasGetAnswers = true;//已获取答案
                loading_layout.setVisibility(View.GONE);
                tv_count_down.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setText("确  定");
                myCountDownTimer.cancel();
                vibrator.cancel();
                SubmitPaperResBean submitPaperResBean = (SubmitPaperResBean) bean;
                if (submitPaperResBean == null) {
                    Toast.makeText(this, "后台接口异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                result_list = submitPaperResBean.getResultQuestionInfo();
                kscj = submitPaperResBean.getKscj();
                if (result_list == null || result_list.isEmpty()) {
                    Toast.makeText(this, "获取题目答案异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Frg_quiz frg_quiz : fragments) {
                    for (ResultQuestionInfo resultQuestionInfo : result_list) {
                        if (frg_quiz.tmid == resultQuestionInfo.getTmid()) {
                            frg_quiz.setResult(resultQuestionInfo.getSfzq(), resultQuestionInfo.getZqxx());
                        }
                    }
                }


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
                vp_container.setCurrentItem(0);
        }
    }


    private void initCountDownTimer() {
        // TODO: 2018/12/3/003 结束后强行提交
        myCountDownTimer = new MyCountDownTimer(kssc * 60 * 1000, 1000, Ac_paper_frame.this, tv_count_down);
        myCountDownTimer.setWarningBound(60 * 1000);
        myCountDownTimer.setOnFinishListener(new MyCountDownTimer.OnFinishListener() {
            @Override
            public void onFinish() {
                loading_layout.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.GONE);
                queryHzydjwNoDialog(V_config.SUBMIT_PAPER_INFO);
            }

            @Override
            public void onWarning() {
                tv_count_down.setTextColor(Color.RED);
                vibrator.vibrate(new long[]{500, 1000, 500, 1000}, -1);
            }
        });
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
        iv_back_press.setVisibility(View.GONE);
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        Collections.sort(questionList, new QuestionInfoComparator());

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

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tag", "onStop: ");
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //用户强制关闭界面时提交考试答案
//        if (!hasGetAnswers) {
//            submitOnDestroy = true;
//            queryHzydjwNoDialog(V_config.SUBMIT_PAPER_INFO);
//        }
    }

    public void backPressed() {

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
                loading_layout.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.GONE);

                queryHzydjwNoDialog(V_config.SUBMIT_PAPER_INFO);

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

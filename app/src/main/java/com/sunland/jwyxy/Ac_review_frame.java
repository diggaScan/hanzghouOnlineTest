package com.sunland.jwyxy;

import android.app.Activity;
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
import android.widget.TextView;

import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_error_question.ErrorQuestion;
import com.sunland.jwyxy.bean.i_error_question.ErrorQuestionReqBean;
import com.sunland.jwyxy.bean.i_error_question.ErrorQuestionResBean;
import com.sunland.jwyxy.bean.i_submit_paper.SubmitPaperReqBean;
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

    private MyFragmentAdapter<Frg_error_quiz> myFragmentAdapter;

    private HashMap<Integer, Integer> answer_sheet = new HashMap<>();

    private int[] answers;
    private List<String> kinds;
    private int total_quizzes;
    private List<String> titles;
    private List<String[]> choices;
    private int paper_code;

    private int ctzl;
    private List<ErrorQuestion> question_list;
    private SparseArray<SubmitQuestionInfo> sparseArray = new SparseArray<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handIntent();
        queryHzydjw(Dictionary.ERROR_QUESTION);
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
            case Dictionary.ERROR_QUESTION:
                ErrorQuestionReqBean errorQuestionReqBean = new ErrorQuestionReqBean();
                assembleBasicObj(errorQuestionReqBean);
                errorQuestionReqBean.setCtzl(ctzl);
                errorQuestionReqBean.setJyid(LocalInfo.jyid);
                return errorQuestionReqBean;
            case Dictionary.SUBMIT_PAPER_INFO:
                SubmitPaperReqBean submitPaperReqBean = new SubmitPaperReqBean();
                assembleBasicObj(submitPaperReqBean);
                submitPaperReqBean.setJyid(LocalInfo.jyid);
                submitPaperReqBean.setJymc(LocalInfo.jymc);
//                submitPaperReqBean.setXfsjid(xfsjid);
//                for (int i = 0; i < sparseArray.size(); i++) {
//                    answer_list.add(sparseArray.get(sparseArray.keyAt(i)));
//                }
//                submitPaperReqBean.setSubmitQuestionInfo(answer_list);
                return submitPaperReqBean;
        }
        return null;
    }

    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        ErrorQuestionResBean errorQuestionResBean = (ErrorQuestionResBean) bean;
        question_list = errorQuestionResBean.getErrorQuestionInfo();
        initViewPager();

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
        toolbar_title.setText(mTitle);
        toolbar_title.setTextColor(Color.BLACK);
        iv_back.setImageResource(R.drawable.ic_close_black);
        tv_count_down.setVisibility(View.GONE);
        iv_tab.setVisibility(View.GONE);
    }

    private void initViewPager() {
        List<Frg_error_quiz> fragments = new ArrayList<>();

        for (int i = 0; i < question_list.size(); i++) {
            Frg_error_quiz error_quiz = new Frg_error_quiz();
            error_quiz.setQuestion(question_list.get(i));
            fragments.add(error_quiz);
        }
        myFragmentAdapter = new MyFragmentAdapter<>(getSupportFragmentManager(), fragments);
        vp_container.setAdapter(myFragmentAdapter);
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
                onBackPressed();
                break;
            case R.id.submit:
//                Ac_main.startActivity(this);
                submit();
                break;

        }
    }

    private void submit() {
        final DialogUtils dialogUtils = new DialogUtils(this);
        dialogUtils.initDialog();
        dialogUtils.setTitle("确定要提交试卷?");
        if (answer_sheet.size() < question_list.size()) {
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
                queryHzydjw(Dictionary.SUBMIT_PAPER_INFO);


            }
        });
        dialogUtils.show();
    }

    @Override
    public void submitAnswer(SubmitQuestionInfo submitQuestionInfo, int tmid) {
        sparseArray.put(tmid, submitQuestionInfo);
    }

    @Override
    public int setToolbarLayout() {
        return R.layout.toolbar_test_main;
    }

    @Override
    public int setContentLayut() {
        return R.layout.ac_paper_frame;
    }


    public static void startActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, Ac_review_frame.class);
        intent.putExtra(FLAG, bundle);
        activity.startActivity(intent);
    }

}

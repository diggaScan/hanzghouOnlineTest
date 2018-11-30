package com.sunland.jwyxy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunland.jwyxy.bean.BaseRequestBean;
import com.sunland.jwyxy.bean.i_paper_detail.PaperDetailReqBean;
import com.sunland.jwyxy.bean.i_paper_detail.PaperDetailResBean;
import com.sunland.jwyxy.bean.i_paper_detail.QuestionInfo;
import com.sunland.jwyxy.config_utils.DialogUtils;
import com.sunland.jwyxy.config_utils.MyCountDownTimer;
import com.sunland.jwyxy.config_utils.answer_sheet.AnswerSheet;
import com.sunland.jwyxy.config_utils.answer_sheet.OnRvItemClickedListener;
import com.sunland.jwyxy.config_utils.answer_sheet.Rv_answer_sheet_adapter;
import com.sunland.jwyxy.config_utils.viewpager_config.MyFragmentAdapter;
import com.sunland.jwyxy.quiz.MyDatabase;
import com.sunland.jwyxy.quiz.OpenDbHelper;
import com.sunland.jwyxy.quiz.QuizContent;
import com.sunland.jwyxy.quiz_result.QuizResult;
import com.sunland.jwyxy.quiz_result.QuizResultDAO;
import com.sunlandgroup.def.bean.result.ResultBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class Ac_paper_frame extends Ac_base_query implements QuizFragment.CommChannel {

    public static final String FLAG = "Ac_paper_frame";


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
    public ImageView answer_tab;
    private boolean isTabClicked;

    private MyCountDownTimer myCountDownTimer;
    private MyFragmentAdapter myFragmentAdapter;
    private Rv_answer_sheet_adapter rvanswersheetadapter;

    private HashMap<Integer, Integer> answer_sheet = new HashMap<>();

    private AnswerSheet mSheet;

    private int[] answers;
    private List<String> kinds;
    private int total_quizzes;
    private List<String> titles;
    private List<String[]> choices;
    private int paper_code;

    private int xfsjid;

    private List<QuestionInfo> questionList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCountDownTimer();
        handIntent();
        initToolbar();
//        getQuizData();
        queryHzydjw(Dictionary.PAPER_DETAIL);
        initViewPager();
        initAnswetSheet();
    }

    @Override
    public int setToolbarLayout() {
        return R.layout.toolbar_test_main;
    }

    @Override
    public int setContentLayut() {
        return R.layout.ac_paper_frame;
    }


    @Override
    public BaseRequestBean assembleRequestObj(String reqName) {
        PaperDetailReqBean paperDetailReqBean = new PaperDetailReqBean();
        assembleBasicObj(paperDetailReqBean);
        paperDetailReqBean.setJyid(LocalInfo.jyid);
        paperDetailReqBean.setXfsjid(4);
        return paperDetailReqBean;
    }

    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        PaperDetailResBean paperDetailResBean = (PaperDetailResBean) bean;
        questionList = paperDetailResBean.getQuestionInfo();
        initViewPager();
    }

    private void getQuizData() {
        paper_code = 11020;
        answers = QuizContent.answers;
        kinds = new ArrayList<>();
        kinds.add("单选");
        total_quizzes = QuizContent.titles.length;
        titles = Arrays.asList(QuizContent.titles);
        choices = QuizContent.choice;
    }

    private void initCountDownTimer() {
        myCountDownTimer = new MyCountDownTimer(1200000, 1000, Ac_paper_frame.this, tv_count_down);
        myCountDownTimer.start();
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
                xfsjid = bundle.getInt("xfsjid", -1);
            }
        }
    }

    private void initToolbar() {
        toolbar_title.setText(mTitle);
        toolbar_title.setTextColor(Color.BLACK);
        iv_back.setImageResource(R.drawable.ic_close_black);
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {

            QuizFragment quizFragment = new QuizFragment();
            quizFragment.setQuestion(questionList.get(i));
            fragments.add(quizFragment);
        }
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
        vp_container.setAdapter(myFragmentAdapter);

    }

    private void initAnswetSheet() {
        mSheet = new AnswerSheet(this);
        rvanswersheetadapter = new Rv_answer_sheet_adapter(this, answer_sheet, total_quizzes);
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

    @OnClick({R.id.back_press, R.id.submit, R.id.answer_tab})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back_press:
                if (isTabClicked) {
                    showAnswerSheet();
                }
                backPressed();
                break;
            case R.id.submit:
                if (isTabClicked) {
                    showAnswerSheet();
                }
                submit();
                break;
            case R.id.answer_tab:
                showAnswerSheet();
        }
    }

    public void backPressed() {
        final DialogUtils dialogUtils = new DialogUtils(this);
        dialogUtils.initDialog();
        dialogUtils.setTitle("确定放弃测试吗?");
        dialogUtils.setDescription("本次考试将不会被记录");
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
                onBackPressed();
            }
        });
        dialogUtils.show();
    }

    @Override
    public void notifyDataSetChange() {
        rvanswersheetadapter.notifyDataSetChanged();
    }

    private void calculateAndStoreResult() {
        int marks = 0;
        int correct_nums = 0;
        int wrong_nums = 0;
        StringBuilder correct_quiz = new StringBuilder();
        StringBuilder wrong_quiz = new StringBuilder();
        if (!answer_sheet.isEmpty()) {
            for (Map.Entry<Integer, Integer> entry : answer_sheet.entrySet()) {
                if (answers[entry.getKey() - 1] == entry.getValue()) {
                    marks += 10;
                    correct_nums += 1;
                    correct_quiz.append("/" + entry.getKey());
                } else {
                    wrong_nums += 1;
                    wrong_quiz.append("/" + entry.getKey());
                }
            }
        }
        OpenDbHelper.createDb(this);
        MyDatabase myDatabase = OpenDbHelper.getDb();
        final QuizResultDAO dao = myDatabase.getQuizResultDao();
        final QuizResult quizResult = new QuizResult();
        quizResult.time_stamp = System.currentTimeMillis();
        quizResult.paper_code = paper_code;
        quizResult.total_quiz = total_quizzes;
        quizResult.correct_nums = correct_nums;
        quizResult.wrong_nums = wrong_nums;
        quizResult.correct_quiz = correct_quiz.toString();
        quizResult.wrong_quiz = wrong_quiz.toString();
        quizResult.mark = marks;
        Log.d("result", quizResult.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insert(quizResult);
            }
        }).start();
//        Ac_main.startActivity(this);
        finish();
    }

    private void submit() {
        final DialogUtils dialogUtils = new DialogUtils(this);
        dialogUtils.initDialog();
        dialogUtils.setTitle("确定要提交试卷?");
        if (answer_sheet.size() < total_quizzes) {
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
                calculateAndStoreResult();
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

    public static void startActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, Ac_paper_frame.class);
        intent.putExtra(FLAG, bundle);
        activity.startActivity(intent);
    }

}

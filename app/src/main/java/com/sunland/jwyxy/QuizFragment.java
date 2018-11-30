package com.sunland.jwyxy;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunland.jwyxy.bean.i_error_question.ErrorQuestion;
import com.sunland.jwyxy.bean.i_paper_detail.ChoiceInfo;
import com.sunland.jwyxy.bean.i_paper_detail.QuestionInfo;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class QuizFragment extends Frg_base {

    private static final int SINGLE_CHOICE = 0;
    public static final String FLAG = "QuizFragment";
    public static final int TEST = 0;
    public static final int REVIEW = 1;
    @BindView(R.id.quiz_kind)
    public TextView tv_kind;
    @BindView(R.id.current_order)
    public TextView tv_order;
    @BindView(R.id.total_quizs)
    public TextView tv_nums;
    @BindView(R.id.question)
    public TextView tv_question;
    @BindView(R.id.choice)
    public LinearLayout ll_choice;
    @BindView(R.id.choice_one)
    public Button btn_a;
    @BindView(R.id.choice_two)
    public Button btn_b;
    @BindView(R.id.choice_three)
    public Button btn_c;
    @BindView(R.id.choice_four)
    public Button btn_d;
    @BindView(R.id.choice_a)
    public TextView tv_a;
    @BindView(R.id.choice_b)
    public TextView tv_b;
    @BindView(R.id.choice_c)
    public TextView tv_c;
    @BindView(R.id.choice_d)
    public TextView tv_d;
    @BindView(R.id.pass_line)
    public TextView tv_line;
    @BindView(R.id.tip)
    public TextView tv_tip;

    private boolean hasChoosen = false;
    private Button clicked_btn = null;

    private String kind;
    private int position;
    private int quiz_num;
    private String title;
    private String[] choices;

    private Context mContext;

    private HashMap<Integer, Integer> answer_sheet;

    private QuestionInfo questionInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        handleAnswerIfExist();
        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.quiz_item;
    }

    @Override
    public void initWidget() {
//        handleBundle();
        List<ChoiceInfo> choiceInfos=questionInfo.getChoiceInfo();
        tv_kind.setText(questionInfo.getTmlx());
        tv_order.setText(questionInfo.getTmid()+questionInfo.getTifl());
//        tv_nums.setText("/" + quiz_num);
        tv_question.setText(questionInfo.getTmmc());
        tv_a.setText(choiceInfos.get(0).getXxnr());
        tv_b.setText(choiceInfos.get(1).getXxnr());
        tv_c.setText(choiceInfos.get(2).getXxnr());
        tv_d.setText(choiceInfos.get(3).getXxnr());
    }

    @OnClick({R.id.choice_one, R.id.choice_two, R.id.choice_three, R.id.choice_four})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.choice_one:
                changeStyleIfClick(btn_a, 1, hasChoosen);
                break;
            case R.id.choice_two:
                changeStyleIfClick(btn_b, 2, hasChoosen);

                break;
            case R.id.choice_three:
                changeStyleIfClick(btn_c, 3, hasChoosen);

                break;
            case R.id.choice_four:
                changeStyleIfClick(btn_d, 4, hasChoosen);

                break;
        }
    }

    public void setQuestion(QuestionInfo question) {
        this.questionInfo = question;
    }


    private void handleBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int type = bundle.getInt("type");
            if (type == REVIEW) {
                tv_line.setVisibility(View.GONE);
                tv_tip.setVisibility(View.GONE);
            }
            kind = bundle.getString("kind");
            position = bundle.getInt("position");
            Log.d("life", "position value" + position);
            quiz_num = bundle.getInt("quiz_num");
            title = bundle.getString("title");
            choices = bundle.getStringArray("choices");
        }
    }

    private void changeStyleIfClick(Button button, int btn_id, boolean status) {

        if (hasChoosen) {
            clicked_btn.setBackgroundResource(R.drawable.quiz_choice_background);
            clicked_btn.setTextColor(getResources().getColor(R.color.med_color_primary));
            if (clicked_btn.equals(button)) {
                ((CommChannel) mContext).removeAnswer(position);
                ((CommChannel) mContext).notifyDataSetChange();
                hasChoosen = false;
                clicked_btn = null;
                return;
            }
            button.setBackgroundResource(R.drawable.clicked_quiz_choice_background);
            button.setTextColor(Color.WHITE);
            clicked_btn = button;
            ((CommChannel) mContext).addAnswer(position, btn_id);
            ((CommChannel) mContext).notifyDataSetChange();
            hasChoosen = true;
            ((CommChannel) mContext).scrollToNext(position, hasChoosen);
        } else {
            button.setBackgroundResource(R.drawable.clicked_quiz_choice_background);
            button.setTextColor(Color.WHITE);
            clicked_btn = button;
            ((CommChannel) mContext).addAnswer(position, btn_id);
            ((CommChannel) mContext).notifyDataSetChange();
            hasChoosen = true;
            ((CommChannel) mContext).scrollToNext(position, hasChoosen);
        }
    }

    private void handleAnswerIfExist() {
        answer_sheet = ((CommChannel) mContext).getAnswerSheet();
        if (answer_sheet.containsKey(position)) {
            int answer = answer_sheet.get(position);
            switch (answer) {
                case 1:
                    setchoiceChoosen(btn_a);
                    break;
                case 2:
                    setchoiceChoosen(btn_b);
                    break;
                case 3:
                    setchoiceChoosen(btn_c);
                    break;
                case 4:
                    setchoiceChoosen(btn_d);
                    break;
            }
            hasChoosen = true;
        }
    }

    private void setchoiceChoosen(Button button) {
        button.setBackgroundResource(R.drawable.clicked_quiz_choice_background);
        button.setTextColor(Color.WHITE);
        clicked_btn = button;
    }

    public interface CommChannel {
        void addAnswer(int position, int answer);

        void removeAnswer(int position);

        HashMap<Integer, Integer> getAnswerSheet();

        void notifyDataSetChange();

        void scrollToNext(int position, boolean haschoosen);
    }
}

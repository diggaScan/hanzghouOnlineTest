package com.sunland.jwyxy;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunland.jwyxy.bean.i_error_question.ErrorQuestion;
import com.sunland.jwyxy.bean.i_paper_detail.ChoiceInfo;
import com.sunland.jwyxy.bean.i_submit_paper.SubmitQuestionInfo;
import com.sunland.jwyxy.config_utils.recycler_config.Rv_Item_decoration;

import java.util.List;

import butterknife.BindView;

public class Frg_error_quiz extends Frg_base {
    public static final String FLAG = "Frg_quiz";
    public static final int TEST = 0;
    public static final int REVIEW = 1;
    private static final int SINGLE_CHOICE = 0;
    @BindView(R.id.choice_list)
    public RecyclerView rv_choice_list;
    @BindView(R.id.quiz_kind)
    public TextView tv_kind;
    @BindView(R.id.current_order)
    public TextView tv_order;
    @BindView(R.id.total_quizs)
    public TextView tv_nums;
    @BindView(R.id.question)
    public TextView tv_question;
    public TextView tv_d;
    @BindView(R.id.pass_line)
    public TextView tv_line;
    @BindView(R.id.tip)
    public TextView tv_tip;
    @BindView(R.id.sfzq)
    public ImageView iv_sfzq; //是否正确的图标
    @BindView(R.id.zqxx)
    public TextView tv_zqxx; //正确选项
    @BindView(R.id.zqxx_des)
    public TextView tv_zxx_des;//正确选项描述

    private Button clicked_btn = null;
    private Context mContext;
    private ErrorQuestion questionInfo;
    public String tifl;//  3判断2多选1单选
    public int seq_num;
    public int total_num;
    private List<ChoiceInfo> choice_List;
    private MyChoiceAdapter choices_adapter;
    private SubmitQuestionInfo submitQuestionInfo;
    public int tmid;
    private int order;
    private int size;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        submitQuestionInfo = new SubmitQuestionInfo();
        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.quiz_item;
    }

    @Override
    public void initWidget() {
        choice_List = questionInfo.getChoiceInfo();
        choices_adapter = new MyChoiceAdapter(choice_List);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rv_choice_list.setAdapter(choices_adapter);
        rv_choice_list.setLayoutManager(manager);
        rv_choice_list.addItemDecoration(new Rv_Item_decoration(mContext));
        tv_nums.setText("/" + total_num);
        tv_order.setText(seq_num + "");
        tv_question.setText(questionInfo.getTmmc());
        switch (tifl) {
            case "1":
                tv_kind.setText("单选题");
                break;
            case "2":
                tv_kind.setText("多选题");
                break;
            case "3":
                tv_kind.setText("判断题");
                break;
        }

        tv_order.setText(String.valueOf(order));
        tv_nums.setText("/" + size);
        tv_tip.setVisibility(View.GONE);
    }

    private void changeStyleIfClick(Button button, String btn_id) {
        boolean hasChosen = (boolean) button.getTag();
        switch (tifl) {
            case "2"://多选题
                if (hasChosen) {
                    button.setBackgroundResource(R.drawable.quiz_choice_background);
                    button.setTextColor(getResources().getColor(R.color.med_color_primary));
                    button.setTag(false);
                } else {
                    button.setBackgroundResource(R.drawable.clicked_quiz_choice_background);
                    button.setTextColor(Color.WHITE);
                    button.setTag(true);
                }
                break;
            case "1":
            case "3":
                if (hasChosen) {
                    button.setBackgroundResource(R.drawable.quiz_choice_background);
                    button.setTextColor(getResources().getColor(R.color.med_color_primary));
                    button.setTag(false);
                } else {
                    button.setBackgroundResource(R.drawable.clicked_quiz_choice_background);
                    button.setTextColor(Color.WHITE);
                    button.setTag(true);
                    if (clicked_btn != null && clicked_btn != button) {
                        clicked_btn.setBackgroundResource(R.drawable.quiz_choice_background);
                        clicked_btn.setTextColor(getResources().getColor(R.color.med_color_primary));
                        clicked_btn.setTag(false);
                    }
                    clicked_btn = button;
                    ((Frg_quiz.CommChannel) mContext).scrollToNext(seq_num, true);
                }
                break;
        }
    }

    private void submitAnswer() {
        submitQuestionInfo.setTmid(questionInfo.getTmid());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rv_choice_list.getChildCount(); i++) {
            Button button = rv_choice_list.getChildAt(i).findViewById(R.id.xxid);
            if ((boolean) button.getTag()) {
                sb.append(choice_List.get(i).getXxid());
            }
        }
        submitQuestionInfo.setDaxx(sb.toString());
        ((Frg_quiz.CommChannel) mContext).submitAnswer(submitQuestionInfo, questionInfo.getTmid());
    }

    public void setResult(int sfzq, String zqxx) {
        tv_line.setVisibility(View.GONE);
        tv_tip.setVisibility(View.GONE);
        iv_sfzq.setVisibility(View.VISIBLE);
        if (sfzq == 0) {//0为错误
            iv_sfzq.setImageResource(R.drawable.ic_cross);
            tv_zxx_des.setVisibility(View.VISIBLE);
            tv_zqxx.setVisibility(View.VISIBLE);
            tv_zqxx.setText(zqxx);
        } else if (sfzq == 1) {//1为正确
            iv_sfzq.setImageResource(R.drawable.ic_tick);
        }
        for (int i = 0; i < rv_choice_list.getChildCount(); i++) {
            rv_choice_list.getChildAt(i).findViewById(R.id.xxid).setEnabled(false);
        }
    }

    public void setQuestion(ErrorQuestion question, int order, int size) {
        this.questionInfo = question;
        this.tmid = question.getTmid();
        this.tifl = questionInfo.getTifl();
        this.order = order;
        this.size = size;
    }

    public void setNum(int num) {
        this.total_num = num;
    }

    public void setSequence(int seq_num) {
        this.seq_num = seq_num;
    }

    class MyChoiceAdapter extends RecyclerView.Adapter<MyChoiceAdapter.MyViewHolder> {
        List<ChoiceInfo> dataSet;
        LayoutInflater inflater;

        public MyChoiceAdapter(List<ChoiceInfo> dataSet) {
            super();
            this.dataSet = dataSet;
            inflater = LayoutInflater.from(mContext);
        }

        @NonNull
        @Override
        public MyChoiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.rv_choice_list_item, viewGroup, false);
            return new MyChoiceAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyChoiceAdapter.MyViewHolder myViewHolder, int i) {
            final ChoiceInfo info = dataSet.get(i);
            myViewHolder.tv_xxid.setText(info.getXxid());

            myViewHolder.tv_xxid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeStyleIfClick(myViewHolder.tv_xxid, info.getXxid());
                    submitAnswer();
                }
            });
            myViewHolder.tv_xxnr.setText(info.getXxnr());
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            Button tv_xxid;
            TextView tv_xxnr;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_xxid = itemView.findViewById(R.id.xxid);
                tv_xxid.setTag(false);//初始都是未选中
                tv_xxnr = itemView.findViewById(R.id.xxnr);
            }
        }
    }


}

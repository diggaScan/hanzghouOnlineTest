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

import com.sunland.jwyxy.bean.i_paper_detail.ChoiceInfo;
import com.sunland.jwyxy.bean.i_paper_detail.QuestionInfo;
import com.sunland.jwyxy.bean.i_submit_paper.SubmitQuestionInfo;
import com.sunland.jwyxy.config_utils.recycler_config.Rv_Item_decoration;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class Frg_quiz extends Frg_base {


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
    @BindView(R.id.pass_line)
    public TextView tv_line;
    @BindView(R.id.tip)
    public TextView tv_tip;
    @BindView(R.id.sfzq)
    public ImageView iv_sfzq;
    @BindView(R.id.zqxx)
    public TextView tv_zqxx;
    @BindView(R.id.zqxx_des)
    public TextView tv_zxx_des;
    public String tifl;// // 1单选2多选3判断
    public int seq_num;//题序
    public int total_num;//总题数
    public int tmid;
    private int jgx;
    private Button clicked_btn = null;
    private Context mContext;
    private HashMap<Integer, Integer> answer_sheet;
    private QuestionInfo questionInfo;
    private List<ChoiceInfo> choice_List;
    private MyChoiceAdapter choices_adapter;
    private SubmitQuestionInfo submitQuestionInfo;
    private int clicked_num = 0;//多选时记录有多少个按钮被点击

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
        tmid = questionInfo.getTmid();
        choice_List = questionInfo.getChoiceInfo();
        choices_adapter = new MyChoiceAdapter(choice_List);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        tv_line.setText(jgx + "分");
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
    }


    public void setQuestion(QuestionInfo question) {
        this.questionInfo = question;
        this.tifl = questionInfo.getTifl();
    }

    public void setNum(int num) {
        this.total_num = num;
    }

    public void setSequence(int seq_num) {
        this.seq_num = seq_num;
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

    public void setJgx(int jgx) {
        this.jgx = jgx;
    }

    private void changeStyleIfClick(Button button, String btn_id) {
        boolean hasChosen = (boolean) button.getTag();
        switch (tifl) {
            case "2"://多选题
                if (hasChosen) {
                    button.setBackgroundResource(R.drawable.quiz_choice_background);
                    button.setTextColor(getResources().getColor(R.color.med_color_primary));
                    button.setTag(false);
                    clicked_num--;
                    if (clicked_num == 0) {
                        ((CommChannel) mContext).removeAnswer(seq_num);
                        ((CommChannel) mContext).notifyDataSetChange();
                    }
                } else {
                    clicked_num++;
                    ((CommChannel) mContext).addAnswer(seq_num, 0);
                    if (clicked_num == 1) {
                        ((CommChannel) mContext).notifyDataSetChange();
                    }
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
                    ((CommChannel) mContext).removeAnswer(seq_num);
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
                    ((CommChannel) mContext).addAnswer(seq_num, 0);
                    ((CommChannel) mContext).scrollToNext(seq_num, true);

                }
                ((CommChannel) mContext).notifyDataSetChange();
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
        ((CommChannel) mContext).submitAnswer(submitQuestionInfo, questionInfo.getTmid());
    }

    public interface CommChannel {

        void submitAnswer(SubmitQuestionInfo submitQuestionInfo, int tmid);

        void addAnswer(int position, int answer);

        void removeAnswer(int position);

        HashMap<Integer, Integer> getAnswerSheet();

        void notifyDataSetChange();

        void scrollToNext(int position, boolean haschoosen);
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
            return new MyViewHolder(view);
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

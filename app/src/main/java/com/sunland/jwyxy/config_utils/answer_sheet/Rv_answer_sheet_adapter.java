package com.sunland.jwyxy.config_utils.answer_sheet;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sunland.jwyxy.R;

import java.util.HashMap;

public class Rv_answer_sheet_adapter extends RecyclerView.Adapter<Rv_answer_sheet_adapter.MyViewHolder> {

    private static final int ANSWERED = 1;
    private static final int NOT_ANSWER = 0;

    private Context mContext;
    private HashMap<Integer, Integer> answer_sheet;
    private int quiz_nums;
    private OnRvItemClickedListener onRvItemClickedListener;

    public Rv_answer_sheet_adapter(Context context, HashMap<Integer, Integer> answer_sheet, int nums) {
        mContext = context;
        this.answer_sheet = answer_sheet;
        quiz_nums = nums;

    }

    @Override
    public int getItemViewType(int position) {
        if (answer_sheet.containsKey(position + 1)) {
            return ANSWERED;
        } else {
            return NOT_ANSWER;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(Rv_answer_sheet_adapter.MyViewHolder holder, final int position) {
        holder.btn_sequence.setText(position + 1 + "");
        holder.btn_sequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRvItemClickedListener.onItemSelected(position);
            }
        });
        if (holder.viewType == ANSWERED) {
            holder.btn_sequence.setBackgroundResource(R.drawable.clicked_quiz_choice_background);
            holder.btn_sequence.setTextColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return quiz_nums;
    }

    @Override
    public Rv_answer_sheet_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.answer_sheet_item, parent, false);
        return new MyViewHolder(view, viewType);
    }

    public void setOnRvItemClickedListener(OnRvItemClickedListener onRvItemClickedListener) {
        this.onRvItemClickedListener = onRvItemClickedListener;
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private Button btn_sequence;
        private int viewType;

        public MyViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            btn_sequence = view.findViewById(R.id.sequence);
        }
    }
}

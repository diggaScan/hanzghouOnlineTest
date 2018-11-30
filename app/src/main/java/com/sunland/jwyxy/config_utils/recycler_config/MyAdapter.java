package com.sunland.jwyxy.config_utils.recycler_config;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunland.jwyxy.R;
import com.sunland.jwyxy.bean.i_paper_list.PaperInfo;
import com.sunland.jwyxy.utils.WindowInfoUtils;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context mContext;
    private List<PaperInfo> dataSet;
    private OnItemClickedListener onItemClickedListener;
    public MyAdapter(Context context, List<PaperInfo> dataSet) {
        this.mContext = context;
        this.dataSet = dataSet;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.paper_list_item, parent, false);
        return new MyViewHolder(view);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {

        DisplayMetrics displayMetrics = WindowInfoUtils.getWindowMetrics(mContext);
        int status_height = WindowInfoUtils.getStatusBarHeight(mContext);
        Toolbar toolbar = ((Activity) mContext).findViewById(R.id.toolbar);
        int toolbar_height = toolbar.getBottom() - toolbar.getTop();
        int item_height = displayMetrics.heightPixels - status_height - toolbar_height;
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(displayMetrics.widthPixels, item_height / 3);
        holder.paper_item.setLayoutParams(ll);

        final PaperInfo paperInfo = dataSet.get(position);

        holder.paper_title.setText(paperInfo.getSjmc());
        holder.paper_category.setText(paperInfo.getSjlx());

        String nums_quie = " · " + paperInfo.getTmsl() + "道题";
        holder.paper_quiz_nums.setText(nums_quie);

        String total_mark = " · " + "总分" + paperInfo.getSjzf();
        holder.paper_total_mark.setText(total_mark);


        holder.paper_limitation.setText("限时20分钟");

        holder.start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickedListener!=null){
                    onItemClickedListener.onClicked(position,paperInfo.getXfsjid());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView paper_title;
        private TextView paper_category;
        private TextView paper_quiz_nums;
        private TextView paper_total_mark;
        private TextView paper_test_nums;
        private TextView paper_average_mark;
        private LinearLayout paper_item;
        private TextView paper_limitation;
        private TextView start_test;

        public MyViewHolder(View view) {
            super(view);
            paper_item = view.findViewById(R.id.paper_item);
            paper_title = view.findViewById(R.id.paper_title);
            paper_category = view.findViewById(R.id.paper_category);
            paper_quiz_nums = view.findViewById(R.id.paper_quiz_nums);
            paper_total_mark = view.findViewById(R.id.paper_total_marks);
            paper_test_nums = view.findViewById(R.id.paper_test_nums);
            paper_average_mark = view.findViewById(R.id.paper_average_mark);
            paper_limitation = view.findViewById(R.id.paper_limitation);
            start_test = view.findViewById(R.id.start_test);

        }
    }

    public interface OnItemClickedListener {
        void onClicked(int position, int xfsjid);
    }
}

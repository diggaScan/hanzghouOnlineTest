package com.sunland.jwyxy.config_utils.recycler_config;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_paper_list_item, parent, false);
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
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(displayMetrics.widthPixels, item_height *2/5);
        holder.paper_item.setLayoutParams(ll);

        final PaperInfo paperInfo = dataSet.get(position);

        holder.tv_sjmc.setText(paperInfo.getSjmc());
        holder.tv_sjlx.setText(paperInfo.getSjlx());

        String nums_quie = " · " + paperInfo.getTmsl() + "道题";
        holder.tv_tmsl.setText(nums_quie);

        String total_mark = " · " + "总分" + paperInfo.getSjzf();
        holder.tv_sjzf.setText(total_mark);
        holder.tv_kksj.setText(paperInfo.getKksj());
        holder.tv_jssj.setText(paperInfo.getJssj());
        holder.tv_kssc.setText("限时" + paperInfo.getKssc() + "分钟");
        holder.tv_jgx.setText(paperInfo.getJgx() + "分");
        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onClicked(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_sjmc;
        TextView tv_tmsl;
        TextView tv_sjzf;
        TextView tv_kksj;
        TextView tv_jssj;
        TextView tv_kssc;
        FrameLayout paper_item;
        TextView tv_sjlx;
        TextView tv_jgx;
        Button btn_start;

        public MyViewHolder(View view) {
            super(view);
            tv_sjmc = itemView.findViewById(R.id.sjmc);
            tv_tmsl = itemView.findViewById(R.id.tmsl);
            tv_sjzf = itemView.findViewById(R.id.sjzf);
            tv_kksj = itemView.findViewById(R.id.kksj);
            tv_jssj = itemView.findViewById(R.id.jssj);
            tv_kssc = itemView.findViewById(R.id.kssc);
            paper_item = itemView.findViewById(R.id.paper_item);
            tv_sjlx = itemView.findViewById(R.id.sjlx);
            tv_jgx = itemView.findViewById(R.id.jgx);
            btn_start = itemView.findViewById(R.id.start_test);
        }
    }

    public interface OnItemClickedListener {
        void onClicked(int position);
    }
}

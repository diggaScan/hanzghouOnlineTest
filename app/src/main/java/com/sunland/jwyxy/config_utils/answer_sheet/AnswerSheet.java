package com.sunland.jwyxy.config_utils.answer_sheet;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.sunland.jwyxy.R;
import com.sunland.jwyxy.utils.WindowInfoUtils;

public class AnswerSheet {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private Rv_answer_sheet_adapter rvanswersheetadapter;

    public AnswerSheet(Context context) {
        this.mContext = context;
    }

    public void initSheet() {
        mPopupWindow = new PopupWindow(mContext);
        View view = getPopupView();
        initWidget(view);
        mPopupWindow.setContentView(view);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setAnimationStyle(R.style.answer_sheet_haul);

        DisplayMetrics metrics = WindowInfoUtils.getWindowMetrics(mContext);
        int window_height = (int) (metrics.heightPixels * 0.63);
        int window_width = (int) (metrics.widthPixels * 0.40);
        mPopupWindow.setHeight(window_height);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mPopupWindow.setElevation(WindowInfoUtils.dp2px(mContext, 10));
        }
        mPopupWindow.setWidth(window_width);
    }


    public void show() {
        mPopupWindow.showAtLocation(((AppCompatActivity) mContext).getWindow().getDecorView(), Gravity.LEFT, 0, 0);
    }

    public void hide() {
        mPopupWindow.dismiss();
    }

    private View getPopupView() {
        return LayoutInflater.from(mContext).inflate(R.layout.answer_sheet_layout, null);
    }

    public void setAdapter(Rv_answer_sheet_adapter adapter) {
        this.rvanswersheetadapter = adapter;
    }

    private void initWidget(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.answer_id);
        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(rvanswersheetadapter);
    }

}

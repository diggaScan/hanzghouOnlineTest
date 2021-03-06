package com.sunland.jwyxy.config_utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountDownTimer extends CountDownTimer {
    private TextView textView;
    private Context mContext;
    private long warningBound;
    private OnFinishListener onFinishListener;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, Context context, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.mContext = context;
        this.textView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int min = (int) millisUntilFinished / 1000 / 60;
        int sec = (int) millisUntilFinished / 1000 % 60;
        String mins = "" + min;
        if (min < 10) {
            mins = "0" + min;
        }
        String secs = "" + sec;
        if (sec < 10) {
            secs = "0" + sec;
        }
        textView.setText(mins + " : " + secs);
        if (millisUntilFinished <= warningBound && onFinishListener != null) {
            onFinishListener.onWarning();
            warningBound = 0;
        }
    }

    @Override
    public void onFinish() {
        if (onFinishListener != null) {
            onFinishListener.onFinish();
        }
    }

    public void setWarningBound(long millisSecond) {
        this.warningBound = millisSecond;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public interface OnFinishListener {
        void onFinish();

        void onWarning();
    }
}

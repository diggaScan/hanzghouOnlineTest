package com.sunland.jwyxy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class Frg_base extends Fragment {


    private View mView;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mView);
        initWidget();
        return mView;
    }

    public View getView() {
        return mView;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract void initWidget();

    public abstract int getLayoutId();

    public void changeTitleName(String name) {
        ((Activity) mContext).setTitle(name);
    }
}

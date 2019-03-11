package com.example.tudou.mymusicss.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tudou.mymusicss.R;
import com.example.tudou.mymusicss.base.BaseFragment;

/**
 * 直播
 */
public class LiveFragment extends BaseFragment {


    public LiveFragment() {
        // Required empty public constructor
    }
    public static LiveFragment instance() {
        LiveFragment liveFragment = new LiveFragment();
        return liveFragment;
    }
    @Override
    protected int getViewResId() {
        return R.layout.fragment_live;
    }

}

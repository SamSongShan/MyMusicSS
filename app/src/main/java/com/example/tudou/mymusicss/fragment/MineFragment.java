package com.example.tudou.mymusicss.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tudou.mymusicss.R;
import com.example.tudou.mymusicss.base.BaseFragment;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {


    public MineFragment() {
        // Required empty public constructor
    }

    public static MineFragment instance() {
        MineFragment mineFragment = new MineFragment();
        return mineFragment;
    }

    @Override
    protected int getViewResId() {
        return R.layout.fragment_mine;
    }

}

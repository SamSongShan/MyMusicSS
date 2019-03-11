package com.example.tudou.mymusicss.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tudou.mymusicss.R;
import com.example.tudou.mymusicss.base.BaseFragment;

/**
 * 曲库
 */
public class MisicLibFragment extends BaseFragment {


    public MisicLibFragment() {
        // Required empty public constructor
    }

    public static MisicLibFragment instance() {
        MisicLibFragment misicLibFragment = new MisicLibFragment();
        return misicLibFragment;
    }

    @Override
    protected int getViewResId() {
        return R.layout.fragment_misic_lib;
    }

}

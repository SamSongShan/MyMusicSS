package com.example.tudou.mymusicss.base;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxDialogFragment;

/**
 * 所有DialogFragment的基类
 */
public abstract class BaseDialog extends RxDialogFragment {

    protected OnItemClickListener onItemClickListener;


    public void show(FragmentManager manager) {
        this.show(manager, "dialog");
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v);
    }
}

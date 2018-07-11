package com.example.tudou.mymusicss.base;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.tudou.mymusicss.ActivityStackManager;
import com.example.tudou.mymusicss.R;
import com.example.tudou.mymusicss.mvp.LifeCycleListener;
import com.example.tudou.mymusicss.utils.StatusBarUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * 所有Activity的基类
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    private Unbinder unbinder;
    public LifeCycleListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setStatusBarLightMode(this, R.color.colorWhite);

        }
        ActivityStackManager.getManager().addActivity(this);
        setContentView(getViewResId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//手机竖屏
        unbinder = ButterKnife.bind(this);
        init();
        loadData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //   super.onSaveInstanceState(outState);

    }

    protected abstract int getViewResId();

    protected void init() {
    }

    protected void loadData() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mListener != null) {
            mListener.onDestroy();
        }
        //移除view绑定
        if (unbinder != null) {
            unbinder.unbind();
        }
        ActivityStackManager.getManager().remove(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mListener != null) {
            mListener.onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onStop();
        }
    }

    /**
     * 设置生命周期回调函数
     *
     * @param listener
     */
    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }
    //设置空数据页

    public StatusLayoutManager setupDefaultStatusLayoutManager(View rootView, OnStatusChildClickListener onStatusChildClickListener) {
        StatusLayoutManager statusLayoutManager = new StatusLayoutManager.Builder(rootView)

                // 设置默认布局属性
//                .setDefaultEmptyText("空白了，哈哈哈哈")
//                .setDefaultEmptyImg(R.mipmap.ic_launcher)
//                .setDefaultEmptyClickViewText("retry")
//                .setDefaultEmptyClickViewTextColor(getResources().getColor(R.color.colorAccent))
//                .setDefaultEmptyClickViewVisible(false)
//
//                .setDefaultErrorText(R.string.app_name)
//                .setDefaultErrorImg(R.mipmap.ic_launcher)
//                .setDefaultErrorClickViewText("重试一波")
//                .setDefaultErrorClickViewTextColor(getResources().getColor(R.color.colorPrimaryDark))
//                .setDefaultErrorClickViewVisible(true)
//
//                .setDefaultLayoutsBackgroundColor(Color.WHITE)

                // 自定义布局
//                .setLoadingLayout(inflate(R.layout.layout_loading))
//                .setEmptyLayout(inflate(R.layout.layout_empty))
//                .setErrorLayout(inflate(R.layout.layout_error))
//
//                .setLoadingLayout(R.layout.layout_loading)
//                .setEmptyLayout(R.layout.layout_empty)
//                .setErrorLayout(R.layout.layout_error)
//
//                .setEmptyClickViewID(R.id.tv_empty)
//                .setErrorClickViewID(R.id.tv_error)

                // 设置重试事件监听器
                .setOnStatusChildClickListener(onStatusChildClickListener)
                .build();

        return statusLayoutManager;
    }


}

package com.example.tudou.mymusicss.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tudou.mymusicss.MainActivity;
import com.example.tudou.mymusicss.R;
import com.example.tudou.mymusicss.api.VersionPostApi;
import com.example.tudou.mymusicss.base.BaseFragment;
import com.example.tudou.mymusicss.base.http.HttpManager;
import com.example.tudou.mymusicss.base.listener.HttpOnNextListener;
import com.example.tudou.mymusicss.base.subscribers.ProgressSubscriber;
import com.example.tudou.mymusicss.custom.RefreshLayout;
import com.example.tudou.mymusicss.model.Login;
import com.example.tudou.mymusicss.utils.DesUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements RefreshLayout.OnRefreshListener {


    @BindView(R.id.rlf)
    RefreshLayout rlf;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init(View v) {
        rlf.setOnRefreshListener(this);

    }

    @Override
    public void onRefresh() {
        checkVersion();
    }

    private void checkVersion() {
        HttpOnNextListener httpOnNextListener = new HttpOnNextListener<String>() {
            @Override
            public void onNext(String method,String s) {

                Log.e("登录111", "onNext: " + DesUtil.decrypt(s));
                rlf.endRefresh();



            }

            @Override
            public void onError(String method,Throwable e) {
                super.onError(method,e);


            }

        };

        HttpManager instance = HttpManager.getInstance();
        ProgressSubscriber progressSubscriber = instance.doHttpDealString(new VersionPostApi("AddSignIn",new Login("Mobile", "13632840502", "123456"),httpOnNextListener, (RxAppCompatActivity) getActivity()), "加载中" + 111);




    }

}

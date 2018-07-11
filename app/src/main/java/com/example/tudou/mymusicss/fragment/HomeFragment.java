package com.example.tudou.mymusicss.fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.tudou.mymusicss.R;
import com.example.tudou.mymusicss.api.VersionPostApi;
import com.example.tudou.mymusicss.base.BaseFragment;
import com.example.tudou.mymusicss.base.http.HttpManager;
import com.example.tudou.mymusicss.base.listener.HttpOnNextListener;
import com.example.tudou.mymusicss.base.subscribers.ProgressSubscriber;
import com.example.tudou.mymusicss.utils.DesUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    protected int getViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init(View v) {
        checkVersion();
        checkVersion2();
        checkVersion3();
        checkVersion4();
    }
    private void checkVersion() {
        HttpOnNextListener httpOnNextListener = new HttpOnNextListener<String>() {
            @Override
            public void onNext(String s) {

                Log.e("fra登录", "onNext: " + DesUtil.decrypt(s));


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        };

        HttpManager instance = HttpManager.getInstance();
        ProgressSubscriber progressSubscriber = instance.doHttpDealString(new VersionPostApi(httpOnNextListener, (RxAppCompatActivity) getActivity()), "加载中" +222);


        // downLoadApp();
      /*  if (progressSubscriber != null) {
            progressSubscriber.cancleOkHttp();
        }*/


    }
    private void checkVersion2() {
        HttpOnNextListener httpOnNextListener = new HttpOnNextListener<String>() {
            @Override
            public void onNext(String s) {

                Log.e("fra登录2", "onNext: " + DesUtil.decrypt(s));


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        };

        HttpManager instance = HttpManager.getInstance();
        ProgressSubscriber progressSubscriber = instance.doHttpDealString(new VersionPostApi(httpOnNextListener, (RxAppCompatActivity) getActivity()), "加载中" +222);


        // downLoadApp();
      /*  if (progressSubscriber != null) {
            progressSubscriber.cancleOkHttp();
        }*/


    }
    private void checkVersion3() {
        HttpOnNextListener httpOnNextListener = new HttpOnNextListener<String>() {
            @Override
            public void onNext(String s) {

                Log.e("fra登录3", "onNext: " + DesUtil.decrypt(s));


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        };

        HttpManager instance = HttpManager.getInstance();
        ProgressSubscriber progressSubscriber = instance.doHttpDealString(new VersionPostApi(httpOnNextListener, (RxAppCompatActivity) getActivity()), "加载中" +222);


        // downLoadApp();
      /*  if (progressSubscriber != null) {
            progressSubscriber.cancleOkHttp();
        }*/


    }
    private void checkVersion4() {
        HttpOnNextListener httpOnNextListener = new HttpOnNextListener<String>() {
            @Override
            public void onNext(String s) {

                Log.e("fra登录4", "onNext: " + DesUtil.decrypt(s));


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        };

        HttpManager instance = HttpManager.getInstance();
        ProgressSubscriber progressSubscriber = instance.doHttpDealString(new VersionPostApi(httpOnNextListener, (RxAppCompatActivity) getActivity()), "加载中" +222);


        // downLoadApp();
      /*  if (progressSubscriber != null) {
            progressSubscriber.cancleOkHttp();
        }*/


    }
}

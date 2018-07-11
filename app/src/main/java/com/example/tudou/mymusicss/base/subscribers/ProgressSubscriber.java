package com.example.tudou.mymusicss.base.subscribers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.example.tudou.mymusicss.base.Api.BaseApi;
import com.example.tudou.mymusicss.base.Api.BaseApiString;
import com.example.tudou.mymusicss.base.RxRetrofitApp;
import com.example.tudou.mymusicss.base.http.cookie.CookieResulte;
import com.example.tudou.mymusicss.base.listener.HttpOnNextListener;
import com.example.tudou.mymusicss.base.utils.AppUtil;
import com.example.tudou.mymusicss.base.http.cookie.CookieDbUtil;
import com.example.tudou.mymusicss.custom.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


import java.lang.ref.SoftReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by WZG on 2016/7/16.
 */
public class ProgressSubscriber<T> extends DisposableObserver<T> implements LoadingDialog.SSDialogCancel {
    /*是否弹框*/
    private boolean showPorgress = true;
    /* 软引用回调接口*/
    private HttpOnNextListener mSubscriberOnNextListener;
    /*软引用反正内存泄露*/
    private SoftReference<RxAppCompatActivity> mActivity;
    /*加载框可自己定义*/
    private ProgressDialog pd;
    /*请求数据*/
    private BaseApi api;
    private BaseApiString apiString;
    private LoadingDialog loadingDialog;
    private boolean cancel;


    /**
     * 构造
     *
     * @param api
     */
    public ProgressSubscriber(BaseApi api, String hint) {
        this.api = api;
        this.mSubscriberOnNextListener = api.getListener();
        this.mActivity = new SoftReference<>(api.getRxAppCompatActivity());
        setShowPorgress(api.isShowProgress());
        if (api.isShowProgress()) {
            initProgressDialog(api.isCancel(), hint);
        }
    }

    /**
     * 构造
     *
     * @param apiString
     */
    public ProgressSubscriber(BaseApiString apiString, String hint) {
        this.apiString = apiString;
        this.mSubscriberOnNextListener = apiString.getListener();
        this.mActivity = new SoftReference<>(apiString.getRxAppCompatActivity());
        setShowPorgress(apiString.isShowProgress());
        if (apiString.isShowProgress()) {
            initProgressDialog(apiString.isCancel(), hint);
        }
    }


    /**
     * 初始化加载框
     */
    private void initProgressDialog(boolean cancel, String hint) {
        this.cancel = cancel;
        Context context = mActivity.get();


        if (loadingDialog == null && context != null) {

            loadingDialog = LoadingDialog.newInstance(hint);
            loadingDialog.show(mActivity.get().getSupportFragmentManager());
            loadingDialog.setCancelable(cancel);
            loadingDialog.setSSDialogCancel(this);

        }
        //showProgressDialog();
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!isShowPorgress()) {
            return;
        }

        Context context = mActivity.get();
        if (loadingDialog == null || context == null) {
            return;
        } else {
            loadingDialog.show(mActivity.get().getSupportFragmentManager());

        }


    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (!isShowPorgress()) return;
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    protected void onStart() {
        super.onStart();
        /*缓存并且有网*/
        if (api == null ? apiString.isCache() && AppUtil.isNetworkAvailable(RxRetrofitApp.getApplication())
                : api.isCache() && AppUtil.isNetworkAvailable(RxRetrofitApp.getApplication())) {
             /*获取缓存数据*/
            CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(api == null ? apiString.getUrl() : api.getUrl());
            if (cookieResulte != null) {
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < (api == null ? apiString.getCookieNetWorkTime() : api.getCookieNetWorkTime())) {
                    if (mSubscriberOnNextListener != null) {
                        mSubscriberOnNextListener.onCacheNext(cookieResulte.getResulte());
                    }
                    onComplete();
                    if (!isDisposed()) {
                        dispose();
                    }
                }
            }
        }
    }


    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        dismissProgressDialog();
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        /*需要緩存并且本地有缓存才返回*/
        if (api == null ? apiString.isCache() : api.isCache()) {
            String url = api == null ? apiString.getUrl() : api.getUrl();
            Observable.just(url).subscribe(new Observer<String>() {
                @Override
                public void onComplete() {
                    Log.e(api==null?apiString.getUrl():api.getUrl(), "onComplete: ");

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(api==null?apiString.getUrl():api.getUrl(), "onError: ");

                    errorDo(e);
                }

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    Log.e("onSubscribe", "onSubscribe: ");

                }

                @Override
                public void onNext(String s) {
                    Context context = mActivity.get();

                    /*获取缓存数据*/
                    CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(s);
                    if (cookieResulte == null) {
                        Toast.makeText(context, "网络中断，也未获取缓存，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                    }
                    long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                    if (time < (api == null ? apiString.getCookieNoNetWorkTime() : api.getCookieNoNetWorkTime())) {
                        if (mSubscriberOnNextListener != null) {
                            mSubscriberOnNextListener.onCacheNext(cookieResulte.getResulte());
                        }
                    } else {
                        CookieDbUtil.getInstance().deleteCookie(cookieResulte);
                        Toast.makeText(context, "网络中断，缓存过期，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            errorDo(e);
        }
    }

    /*错误统一处理*/
    private void errorDo(Throwable e) {
        Context context = mActivity.get();
        if (context == null) return;
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "错误" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onError(e);
        }
    }


    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {

        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }

    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }


    public boolean isShowPorgress() {
        return showPorgress;
    }

    /**
     * 是否需要弹框设置
     *
     * @param showPorgress
     */
    public void setShowPorgress(boolean showPorgress) {
        this.showPorgress = showPorgress;
    }

    public void cancleOkHttp() {
        dismissProgressDialog();
        onCancelProgress();
    }


    @Override
    public void onCancel() {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onCancel();
        }
        onCancelProgress();
    }
}
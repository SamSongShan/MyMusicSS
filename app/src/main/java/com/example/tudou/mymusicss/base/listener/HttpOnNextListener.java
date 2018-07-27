package com.example.tudou.mymusicss.base.listener;


import io.reactivex.Observable;

/**
 * 成功回调处理
 * Created by WZG on 2016/7/16.
 */
public abstract class HttpOnNextListener<T> {
    /**
     * 成功后回调方法
     * @param t
     */
    public abstract void onNext(String method,T t);

    /**
     * 緩存回調結果
     * @param string
     */
    public void onCacheNext(String method,String string){

    }

    /**
     * 成功后的ober返回，扩展链接式调用
     * @param observable
     */
    public void onNext(String method,Observable observable){

    }

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     * @param e
     */
    public  void onError(String method,Throwable e){

    }

    /**
     * 取消回調
     */
    public void onCancel(String method){

    }


}
package com.example.tudou.mymusicss.api;

import com.example.tudou.mymusicss.HttpPostService;
import com.example.tudou.mymusicss.base.Api.BaseApiString;
import com.example.tudou.mymusicss.base.listener.HttpOnNextListener;
import com.example.tudou.mymusicss.model.Login;
import com.example.tudou.mymusicss.utils.DesUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by tudou on 2018/5/7.
 */

public class VersionPostApi extends BaseApiString {

    private boolean all;

    public VersionPostApi(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);


        setShowProgress(true);
        setCancel(true);
        setCache(false);
        setMethod("AddSignIn");
        setCookieNetWorkTime(60);
        setCookieNoNetWorkTime(24 * 60 * 60);
    }


    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        Gson gson = new GsonBuilder().create();
        String mobile = gson.toJson(new Login("Mobile", "13632840502", "123456"));

        return service.getAllVedioBy(DesUtil.encrypt(mobile));
    }

}

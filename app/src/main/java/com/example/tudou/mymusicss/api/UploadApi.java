package com.example.tudou.mymusicss.api;



import com.example.tudou.mymusicss.HttpPostService;
import com.example.tudou.mymusicss.base.Api.BaseApiString;
import com.example.tudou.mymusicss.base.listener.HttpOnNextListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Retrofit;

/**
 * 上传请求api
 * Created by WZG on 2016/10/20.
 */

public class UploadApi extends BaseApiString {
    /*需要上传的文件*/
    private MultipartBody.Part part;


    public UploadApi(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setShowProgress(true);
        setCancel(false);
        setMethod("UpLoadImg");
    }

    public MultipartBody.Part getPart() {
        return part;
    }

    public void setPart(MultipartBody.Part part) {
        this.part = part;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        return service.uploadImage("WU039",getPart());
    }

}

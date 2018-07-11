package com.example.tudou.mymusicss.base.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.example.tudou.mymusicss.base.Api.BaseApi;
import com.example.tudou.mymusicss.base.Api.BaseApiString;
import com.example.tudou.mymusicss.base.RxRetrofitApp;
import com.example.tudou.mymusicss.base.exception.RetryWhenNetworkException;
import com.example.tudou.mymusicss.base.http.cookie.CookieInterceptor;
import com.example.tudou.mymusicss.base.listener.HttpOnNextListener;
import com.example.tudou.mymusicss.base.subscribers.ProgressSubscriber;
import com.example.tudou.mymusicss.utils.DesUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * http交互处理类
 * Created by WZG on 2016/7/16.
 */
public class HttpManager {
    private volatile static HttpManager INSTANCE;
    private String hint;
    private boolean useClientAuth;
    private OkHttpClient okHttpClient;

    //构造方法私有
    private HttpManager() {
    }

    //获取单例
    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public ProgressSubscriber doHttpDeal(BaseApi basePar, String hint) {

        this.hint = hint;
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);
        builder.addInterceptor(new CookieInterceptor(basePar.isCache(), basePar.getUrl()));
        if (RxRetrofitApp.isDebug()) {

            builder.addInterceptor(getHttpLoggingInterceptor());
        }


        /*创建retrofit对象*/
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(basePar.getBaseUrl())
                .build();


        /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(basePar, hint);
        Observable observable = basePar.getObservable(retrofit)
                 /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                        basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*生命周期管理*/
//                .compose(basePar.getRxAppCompatActivity().bindToLifecycle())
                .compose(basePar.getRxAppCompatActivity().bindUntilEvent(ActivityEvent.PAUSE))
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map(basePar);


        /*链接式对象返回*/
        HttpOnNextListener httpOnNextListener = basePar.getListener();
        if (httpOnNextListener != null ) {
            httpOnNextListener.onNext(observable);
        }

        /*数据回调*/
        observable.subscribe(subscriber);
        return subscriber;
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public ProgressSubscriber doHttpDealString(BaseApiString basePar, String hint) {

        this.hint = hint;
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS)
                .addInterceptor(new CookieInterceptor(basePar.isCache(), basePar.getUrl()));


        if (RxRetrofitApp.isDebug())

        {
            builder.addInterceptor(getHttpLoggingInterceptor());
             //builder.addInterceptor(new LoggingInterceptor());
        }


                                      /*创建retrofit对象*/
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(basePar.getBaseUrl())
                .build();


                                      /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(basePar, hint);
        Observable observable = basePar.getObservable(retrofit)
                 /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                        basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*生命周期管理*/
//                .compose(basePar.getRxAppCompatActivity().bindToLifecycle())
                .compose(basePar.getRxAppCompatActivity().bindUntilEvent(ActivityEvent.PAUSE))
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map(basePar);


                                      /*链接式对象返回*/
        HttpOnNextListener httpOnNextListener = basePar.getListener();
        if (httpOnNextListener != null )

        {
            httpOnNextListener.onNext(observable);
        }

        /*数据回调*/
        observable.subscribe(subscriber);

        return subscriber;
    }
  /*  *//**
     * 处理https请求
     *
     * @param basePar 封装的请求数据
     *//*
    public void doHttpsDealString(BaseApiString basePar, String hint) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            String certificateAlias = Integer.toString(0);
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.
                    generateCertificate(context.getResources().openRawResource(R.raw.fullchain)));
            SSLContext sslContext = SSLContext.getInstance("TLS");
            final TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.MINUTES)
                    .readTimeout(60, TimeUnit.MINUTES)
                    .writeTimeout(60, TimeUnit.MINUTES)
                    .retryOnConnectionFailure(false)
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    })
                    .build();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        SSLContext sslSocketFactory = null;
        try {
            sslSocketFactory = createSSLSocketFactory(basePar.getRxAppCompatActivity(), R.raw.fullchain, "");
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        this.hint = hint;
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS)
                .addInterceptor(new CookieInterceptor(basePar.isCache(), basePar.getUrl()))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {

                        Certificate[] localCertificates = new Certificate[0];
                        try { //获取证书链中的所有证书
                            localCertificates = session.getPeerCertificates();

                        } catch (SSLPeerUnverifiedException e) {
                            e.printStackTrace();
                        } //打印所有证书内容
                        for (Certificate c : localCertificates) {
                            Log.d(TAG, "verify: " + c.toString());
                        }
                        try { //将证书链中的第一个写到文件
                            createFileWithByte(localCertificates[0].getEncoded());
                        } catch (CertificateEncodingException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } //写到文件

                    private void createFileWithByte(byte[] bytes) { // TODO Auto-generated method stub *//**/

    /**
     * 创建File对象，其中包含文件所在的目录以及文件的命名
     *//**//*
                        File file = new File(Environment.getExternalStorageDirectory(), "ca.cer"); // 创建FileOutputStream对象
                        FileOutputStream outputStream = null; // 创建BufferedOutputStream对象
                        BufferedOutputStream bufferedOutputStream = null;
                        try { // 如果文件存在则删除
                            if (file.exists()) {
                                file.delete();
                            } // 在文件系统中根据路径创建一个新的空文件
                            file.createNewFile(); // 获取FileOutputStream对象
                            outputStream = new FileOutputStream(file); // 获取BufferedOutputStream对象
                            bufferedOutputStream = new BufferedOutputStream(outputStream); // 往文件所在的缓冲输出流中写byte数据
                            bufferedOutputStream.write(bytes); // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
                            bufferedOutputStream.flush();
                        } catch (Exception e) { // 打印异常信息
                            e.printStackTrace();
                        } finally { // 关闭创建的流对象
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (bufferedOutputStream != null) {
                                try {
                                    bufferedOutputStream.close();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                    }
                })
                .sslSocketFactory(sslSocketFactory.getSocketFactory(), new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        X509Certificate[] x509Certificates = new X509Certificate[0];
                        return x509Certificates;
                    }
                });


        if (RxRetrofitApp.isDebug())

        {
            builder.addInterceptor(getHttpLoggingInterceptor());
            // builder.addInterceptor(new LoggingInterceptor());
        }


                                      *//*创建retrofit对象*//*
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(basePar.getBaseUrl())
                .build();


                                      *//*rx处理*//*
        ProgressSubscriber subscriber = new ProgressSubscriber(basePar, hint);
        Observable observable = basePar.getObservable(retrofit)
                 *//*失败后的retry配置*//*
                .retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                        basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                *//*生命周期管理*//*
//                .compose(basePar.getRxAppCompatActivity().bindToLifecycle())
                .compose(basePar.getRxAppCompatActivity().bindUntilEvent(ActivityEvent.PAUSE))
                *//*http请求线程*//*
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                *//*回调线程*//*
                .observeOn(AndroidSchedulers.mainThread())
                *//*结果判断*//*
                .map(basePar);


                                      *//*链接式对象返回*//*
        SoftReference<HttpOnNextListener> httpOnNextListener = basePar.getListener();
        if (httpOnNextListener != null && httpOnNextListener.get() != null)

        {
            httpOnNextListener.get().onNext(observable);
        }

        *//*数据回调*//*
        observable.subscribe(subscriber);

    }*/
    private SSLContext createSSLSocketFactory(Context context, int res, String password)
            throws CertificateException,
            NoSuchAlgorithmException,
            IOException,
            KeyStoreException,
            KeyManagementException {
        KeyManager[] kms = null;
        TrustManagerFactory tmf = null;
        // 实例化SSL上下文
        SSLContext sslContext = SSLContext.getInstance("TLS");
        if (res > 0 && !TextUtils.isEmpty(password)) {
            InputStream inputStream = context.getResources().openRawResource(res);
            // 获得信任库
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(inputStream, password.toCharArray());
            // 实例化信任库
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            // 初始化信任库
            tmf.init(keyStore);
            if (useClientAuth) {
                try {
                    String KEYSTORE_PASSWORD = "pass";
                    // 获得密钥库
                    KeyStore keyStoreM = KeyStore.getInstance("PKCS12");//JKS
                    keyStoreM.load(inputStream, KEYSTORE_PASSWORD.toCharArray());
                    // 实例化密钥库
                    KeyManagerFactory kmf = KeyManagerFactory
                            .getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    // 初始化密钥工厂
                    kmf.init(keyStoreM, KEYSTORE_PASSWORD.toCharArray());
                    kms = kmf.getKeyManagers();
                } catch (UnrecoverableKeyException e) {
                    e.getMessage();
                }
            }
            // 初始化SSL上下文
            sslContext.init(kms, tmf.getTrustManagers(), new SecureRandom());
        } else {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
            }};
            sslContext.init(null, trustAllCerts, new SecureRandom());
        }
        return sslContext;
    }


    /**
     * 在getInstance在Application中，若需要是用https，需要先调用此方法
     * 并且部分参数、方法、协议等可能需要改动
     *
     * @param httpsHost
     * @param res
     * @param password
     */
    public static void prepareHttps(String httpsHost, int res, String password) {
//        mHttpsHost = httpsHost;
//        mRes = res;
//        password = mPassWord;
    }


    /**
     * 日志输出
     * 自行判定是否添加
     *
     * @return
     */

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {

                String decrypt = DesUtil.decrypt(message);
                Log.e("RxRetrofit", "Retrofit====Message:" + decrypt);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}

class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        long t1 = System.nanoTime();//请求发起的时间
        String method = request.method();
        JSONObject jsonObject = new JSONObject();
        if ("POST".equals(method) || "PUT".equals(method)) {
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                if (body != null) {
                    for (int i = 0; i < body.size(); i++) {
                        try {
                            jsonObject.put(body.name(i), body.encodedValue(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.e("request", String.format("发送请求 %s on %s  %nRequestParams:%s%nMethod:%s",
                        request.url(), chain.connection(), jsonObject.toString(), request.method()));
            } else {
                Buffer buffer = new Buffer();
                RequestBody requestBody = request.body();
                if (requestBody != null) {
                    request.body().writeTo(buffer);
                    String body = buffer.readUtf8();
                    Log.e("request", String.format("发送请求 %s on %s  %nRequestParams:%s%nMethod:%s",
                            request.url(), chain.connection(), body, request.method()));
                }
            }
        } else {
            Log.e("request", String.format("发送请求 %s on %s%nMethod:%s",
                    request.url(), chain.connection(), request.method()));
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        Log.e("request",
                String.format("Retrofit接收响应: %s %n返回json:【%s】 %n耗时：%.1fms",
                        response.request().url(),
                        responseBody.string(),
                        (t2 - t1) / 1e6d
                ));
        return response;
    }

}


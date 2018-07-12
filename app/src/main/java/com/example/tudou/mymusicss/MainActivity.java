package com.example.tudou.mymusicss;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tudou.mymusicss.api.UploadApi;
import com.example.tudou.mymusicss.api.VersionPostApi;
import com.example.tudou.mymusicss.base.BaseActivity;
import com.example.tudou.mymusicss.base.download.DownInfo;
import com.example.tudou.mymusicss.base.download.HttpDownManager;
import com.example.tudou.mymusicss.base.http.HttpManager;
import com.example.tudou.mymusicss.base.listener.HttpDownOnNextListener;
import com.example.tudou.mymusicss.base.listener.HttpOnNextListener;
import com.example.tudou.mymusicss.base.listener.upload.ProgressRequestBody;
import com.example.tudou.mymusicss.base.listener.upload.UploadProgressListener;
import com.example.tudou.mymusicss.base.subscribers.ProgressSubscriber;
import com.example.tudou.mymusicss.custom.DownloadDialog;
import com.example.tudou.mymusicss.custom.RefreshLayout;
import com.example.tudou.mymusicss.utils.DesUtil;
import com.example.tudou.mymusicss.utils.LogUtils;
import com.example.tudou.mymusicss.utils.PermissionsUtil;
import com.example.tudou.mymusicss.utils.PhoneUtil;
import com.example.tudou.mymusicss.utils.ToastUtil;


import java.io.File;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.R.attr.versionName;


public class MainActivity extends BaseActivity implements PermissionsUtil.CheckVersion, View.OnClickListener, OnStatusChildClickListener {



    private DownloadDialog downloadDialog;
    private String filePath;
    private Handler handler = new Handler();
    private boolean isForce = false;//是否强制升级

    private boolean isExit = false;//退出标识


    private int INSTALL_PACKAGES_REQUESTCODE = 121;
    private int GET_UNKNOWN_APP_SOURCES = 124;
    private FragmentManager fragmentManager;

    @Override
    protected int getViewResId() {
        return R.layout.activity_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void init() {

       /* viewById = (RefreshLayout) findViewById(R.id.refreshable_view);
        //延时1s执行
        this.viewById.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                *//*if (fragmentManager == null) {
                    fragmentManager = getSupportFragmentManager();

                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.fgt, new HomeFragment(), "0");
                transaction.commitAllowingStateLoss();*//*
                uploadeDo();
                checkVersion();
            }
        });*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionsUtil.checkPermissions(this, this, 0, 1, 2, 3, 4, 5, 6, 7, 8);

        } else {
            checkVersion();
            uploadeDo();

        }
        /*StatusLayoutManager statusLayoutManager = setupDefaultStatusLayoutManager(ll, null);
        statusLayoutManager.showErrorLayout();*/
    }


    private void checkVersion() {
        HttpOnNextListener httpOnNextListener = new HttpOnNextListener<String>() {
            @Override
            public void onNext(String s) {

                Log.e("登录111", "onNext: " + DesUtil.decrypt(s));
                //viewById.endRefresh();
                // startActivity(new Intent(MainActivity.this,MainActivity.class));
                // finish();


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);


            }

        };

        HttpManager instance = HttpManager.getInstance();
        ProgressSubscriber progressSubscriber = instance.doHttpDealString(new VersionPostApi(httpOnNextListener, MainActivity.this), "加载中" + 111);


        // downLoadApp();
      /*  if (progressSubscriber != null) {
            progressSubscriber.cancleOkHttp();
        }*/


    }


    private void downLoadApp() {
        downloadDialog = DownloadDialog.newInstance(PhoneUtil.getAppName(this) + "versionName", isForce);
        downloadDialog.show(getFragmentManager(), "download");

        filePath = Environment.getExternalStorageDirectory() + "/Download/" + PhoneUtil.getAppName(this) +
                "_" + versionName + ".apk";
        Log.e("loge", "Download: " + filePath);

        DownInfo downInfo = new DownInfo("http://www.hfhlife.com/Resource/version/az.apk");
        downInfo.setId(0);
        downInfo.setSavePath(filePath);
        downInfo.setUpdateProgress(true);

        HttpDownManager instance = HttpDownManager.getInstance();

        downInfo.setListener(new HttpDownOnNextListener() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onStart() {
                LogUtils.e("下载开始", "下载开始");
            }

            @Override
            public void onComplete() {


                LogUtils.e("下载完成", "下载完成");
                ProgressBar pb = downloadDialog.getProgressBar();
                pb.setProgress(100);
                TextView btnInstall = downloadDialog.getBtnInstall();
                btnInstall.setText("安装");
                btnInstall.setSelected(true);
                btnInstall.setClickable(true);
                btnInstall.setOnClickListener(MainActivity.this);
            }

            @Override
            public void updateProgress(long readLength, long countLength) {
                ProgressBar pb = downloadDialog.getProgressBar();
                TextView btnInstall = downloadDialog.getBtnInstall();

                if (pb != null) {
                    LogUtils.e("111", (int) (readLength * 100 / countLength) + "");

                    pb.setProgress((int) (readLength * 100 / countLength));
                }
                if (btnInstall != null) {
                    btnInstall.setText("下载中(" + (int) (readLength * 100 / countLength) + "%)");
                }
                LogUtils.e("readLength", readLength + "");
                LogUtils.e("countLength", countLength + "");


            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("下载失败", e.getMessage());

            }
        });
        instance.startDown(downInfo);


    }

    /**
     * 跳转到安装页面
     */
    private void jumpInstall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                installApk();//安装应用的逻辑(写自己的就可以)
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            installApk();
        }

    }

    private void installApk() {
        File apkFile = new File(filePath);

        if (apkFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");

            } else {

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//给目标应用一个临时的授权
                Uri uriForFile = FileProvider.getUriForFile(this, PhoneUtil.getAppProcessName(this) + ".FileProvider", apkFile);
                intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");

            }
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());

        }
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();
            //System.exit(0);
        } else {
            ToastUtil.initToast(this, "再按一次退出" + PhoneUtil.getAppName(this));
            isExit = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 5000);//5秒内再按后退键真正退出
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == INSTALL_PACKAGES_REQUESTCODE) {//8.0权限
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                installApk();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
            }
        } else {//权限申请
            PermissionsUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        }
    }

    @Override
    public void checkVersionSuccess() {//授权成功
        checkVersion();
    }

    @Override
    public void checkVersionFail() {//授权失败
    }

    @Override
    public void onClick(View v) {
        jumpInstall();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_UNKNOWN_APP_SOURCES) {
            jumpInstall();
        }


    }


    /*********************************************文件上传***************************************************/

    private void uploadeDo() {
        File file = new File("/storage/emulated/0/Download/SIFE收款二维码.jpg");
        if (!file.exists()){
            ToastUtil.initToast(this,"文件不攒在");
            return;
        }
        RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("WU039", file.getName(), new ProgressRequestBody
                (requestBody,
                        new UploadProgressListener() {
                            @Override
                            public void onProgress(final long currentBytesCount, final long totalBytesCount) {

                                /*回到主线程中，可通过timer等延迟或者循环避免快速刷新数据*/
                                Observable.just(currentBytesCount)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<Long>() {

                                            @Override
                                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onNext(@io.reactivex.annotations.NonNull Long aLong) {
                                                LogUtils.e("提示:上传中", "总共" + totalBytesCount + "已传" + currentBytesCount);

                                            }

                                            @Override
                                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                                LogUtils.e("提示:上传中", e.getMessage());
                                            }

                                            @Override
                                            public void onComplete() {

                                            }


                                        });
                            }
                        }));
        UploadApi uplaodApi = new UploadApi(httpOnNextListener, this);
        uplaodApi.setPart(part);
        HttpManager manager = HttpManager.getInstance();
        manager.doHttpDealString(uplaodApi, "");
    }


    /**
     * 上传回调
     */
    HttpOnNextListener httpOnNextListener = new HttpOnNextListener<String>() {
        @Override
        public void onNext(String o) {

            LogUtils.e("上传回调onNext", DesUtil.decrypt(o));
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            LogUtils.e("上传回调onError", e.getMessage());

        }

    };

    @Override
    public void onEmptyChildClick(View view) {

    }

    @Override
    public void onErrorChildClick(View view) {

    }

    @Override
    public void onCustomerChildClick(View view) {

    }
}

package com.example.tudou.mymusicss;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;


/**
 * Activity栈管理
 *
 * @author ZhongDaFeng
 */
public class ActivityStackManager {

    private static ActivityStackManager instance = null;
    private final Map<String, Activity> activitys;

    /**
     * 私有构造
     */
    private ActivityStackManager() {
        activitys = new HashMap<>();
    }

    /**
     * 单例实例
     *
     * @return
     */
    public static ActivityStackManager getManager() {
        if (instance == null) {
            synchronized (ActivityStackManager.class) {
                if (instance == null) {
                    instance = new ActivityStackManager();
                }
            }
        }
        return instance;
    }

    /**
     * 压栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activitys.put(activity.getClass().getName(), activity);
    }

    public Activity getActivity(String name) {
        return activitys.get(name);
    }


    /**
     * 用于异地登录或者退出时清除activity
     * <p>
     * 用时放开注释 即可
     */
    public void clearActivity() {

        for (Map.Entry<String, Activity> entry : activitys.entrySet()) {

            Activity activity = entry.getValue();
            /*if (activity instanceof LoginActivity) {
            } else {
                activity.finish();
            }*/
        }

    }

    /**
     * 移除
     *
     * @param activity
     */
    public void remove(Activity activity) {
        Activity item = activitys.remove(activity.getClass().getName());
        item = null;
    }



    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Map.Entry<String, Activity> entry : activitys.entrySet()) {
           entry.getValue().finish();

        }
    }

    /**
     * 退出应用程序
     *
     * @param context
     */
    public void exitApp(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            //清除通知栏
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }
}
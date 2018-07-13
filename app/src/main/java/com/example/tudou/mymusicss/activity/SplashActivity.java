package com.example.tudou.mymusicss.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.tudou.mymusicss.MainActivity;
import com.example.tudou.mymusicss.R;
import com.example.tudou.mymusicss.ActivityStackManager;
import com.example.tudou.mymusicss.base.BaseActivity;
import com.example.tudou.mymusicss.custom.textpath.PathAnimatorListener;
import com.example.tudou.mymusicss.custom.textpath.SyncTextPathView;
import com.example.tudou.mymusicss.custom.textpath.painter.PenPainter;
import com.example.tudou.mymusicss.utils.PhoneUtil;

import java.util.Random;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.syncTextPathView)
    SyncTextPathView stp;
    private TextView[] ts;

    private View container;

    private boolean isStop = false;


    @Override
    protected int getViewResId() {
        if (ActivityStackManager.getManager().getActivity("MainActivity") != null) {
            // 应用已经启动并未被杀死，直接启动 MainActivity
            startMainActivity();
            return -1;
        }
        return R.layout.activity_splash;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void init() {

        container = findViewById(R.id.splash_container);
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{
                        getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorPrimaryDark)
                });
        container.setBackground(gd);
        container.setClickable(false);
        Random rand = new Random();
        int i = rand.nextInt(2);
        if (i == 0) {
            stp.setVisibility(View.VISIBLE);
            stp.setPathPainter(new PenPainter());
            stp.startAnimation(0, 1);
            //设置动画播放完后填充颜色
            stp.setAnimatorListener(new PathAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (!isCancel && !isStop) {
                        stp.showFillColorText();
                        startFinalAnim();
                        /*startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();*/

                    }
                }
            });
        } else {
            ts = new TextView[]{
                    (TextView) findViewById(R.id.splash_m),
                    (TextView) findViewById(R.id.splash_u),
                    (TextView) findViewById(R.id.splash_s),
                    (TextView) findViewById(R.id.splash_i),
                    (TextView) findViewById(R.id.splash_c),

            };
            ts[0].post(new Runnable() {
                @Override
                public void run() {
                    for (TextView t : ts) {
                        t.setVisibility(View.VISIBLE);
                        startTextInAnim(t);
                    }
                }
            });
        }


    }

    private void startTextInAnim(TextView t) {
        Random r = new Random();
        DisplayMetrics metrics = PhoneUtil.getMetrics(this);
        int x = r.nextInt(metrics.widthPixels * 4 / 3);
        int y = r.nextInt(metrics.heightPixels * 4 / 3);
        float s = r.nextFloat() + 4.0f;
        ValueAnimator tranY = ObjectAnimator.ofFloat(t, "translationY", y - t.getY(), 0);
        ValueAnimator tranX = ObjectAnimator.ofFloat(t, "translationX", x - t.getX(), 0);
        ValueAnimator scaleX = ObjectAnimator.ofFloat(t, "scaleX", s, 1.0f);
        ValueAnimator scaleY = ObjectAnimator.ofFloat(t, "scaleY", s, 1.0f);
        ValueAnimator alpha = ObjectAnimator.ofFloat(t, "alpha", 0.0f, 1.0f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(1800);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.play(tranX).with(tranY).with(scaleX).with(scaleY).with(alpha);
        if (t == findViewById(R.id.splash_c)) {
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startFinalAnim();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        set.start();
    }

    private void startFinalAnim() {
        final ImageView image = (ImageView) findViewById(R.id.splash_logo);
        final TextView name = (TextView) findViewById(R.id.splash_name);

        ValueAnimator alpha = ObjectAnimator.ofFloat(image, "alpha", 0.0f, 1.0f);
        alpha.setDuration(1000);
        ValueAnimator alphaN = ObjectAnimator.ofFloat(name, "alpha", 0.0f, 1.0f);
        alphaN.setDuration(1000);
        ValueAnimator tranY = ObjectAnimator.ofFloat(image, "translationY", -image.getHeight() / 3, 0);
        tranY.setDuration(1000);
        ValueAnimator wait = ObjectAnimator.ofInt(0, 100);
        wait.setDuration(1000);
        wait.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isStop) {
                            startMainActivity();
                        }

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new LinearInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                image.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        set.play(alpha).with(alphaN).with(tranY).before(wait);
        set.start();
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isStop = true;
            ActivityStackManager.getManager().exitApp(this);
            return true;//return true;拦截事件传递,从而屏蔽back键。
        }
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            isStop = true;
            ActivityStackManager.getManager().exitApp(this);            return true;//同理
        }
        return super.onKeyDown(keyCode, event);
    }
}

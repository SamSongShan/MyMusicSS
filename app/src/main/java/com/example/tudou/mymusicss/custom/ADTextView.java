package com.example.tudou.mymusicss.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.tudou.mymusicss.model.NewsEntity;
import com.example.tudou.mymusicss.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 仿京东垂直滚动广告栏
 */

public class ADTextView extends View {

    private List<NewsEntity.DataEntity> mTexts; //显示文字的数据源
    private int mSpeed; //文字出现或消失的速度 建议1~5
    private int mInterval; //文字停留在中间的时长
    private int mTextSize; //文字大小
    private int mNum; //文字段数
    private int mY = 0; //文字的Y坐标
    private int mIndex = 0; //当前的数据下标
    private int[] mColors; //绘制的颜色
    private Paint[] mPaints; //绘制的画笔
    private boolean isMove = true; //文字是否移动
    private boolean hasInit = false;
    private boolean isPaused = false;

    private OnItemClickListener OnItemClickListener;

    public ADTextView(Context context) {
        this(context, null);
    }

    public ADTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        init();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.OnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(String mUrl);
    }

    /**
     * 解析自定义属性
     */
    private void getAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ADTextView);
        mSpeed = array.getInt(R.styleable.ADTextView_speed, 1);
        mInterval = array.getInt(R.styleable.ADTextView_interval, 1500);
        mNum = array.getInt(R.styleable.ADTextView_num, 1);
        mTextSize = (int) array.getDimension(R.styleable.ADTextView_textSize, sp2px(14));
        array.recycle();
    }

    private void init() {
        mIndex = 0;
        mColors = new int[mNum];
        mPaints = new Paint[mNum];
        for (int i = 0; i < mNum; i++) {
            mColors[i] = Color.BLACK;
            mPaints[i] = initPaint(mColors[i]);
        }
    }

    private Paint initPaint(int color){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(mTextSize);
        paint.setColor(color);
        return paint;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (OnItemClickListener != null) {
                    OnItemClickListener.onClick(mTexts.get(mIndex).getNewsid());
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    //测量高度
    private int measureHeight(int heightMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {//高度至少为两倍字高
            int mTextHeight = (int) (mPaints[0].descent() - mPaints[0].ascent()); //前缀文字字高
            Paint mPaint = mPaints.length > 1 ? mPaints[1] : mPaints[0];
            int mContentTextHeight = (int) (mPaint.descent() - mPaint.ascent()); //内容文字字高
            result = Math.max(mTextHeight, mContentTextHeight) * 2;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    //测量宽度
    private int measureWidth(int widthMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else { //宽度最小十个字的宽度
            String text = "我随手一打就是十个字";
            Rect rect = new Rect();
            mPaints[1].getTextBounds(text, 0, text.length(), rect);
            result = rect.right - rect.left;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    //设置数据源
    public void setTexts(List<NewsEntity.DataEntity> texts) {
        this.mTexts = texts;
        invalidate();
    }

    //设置广告文字的停顿时间
    public void setInterval(int mInterval) {
        this.mInterval = mInterval;
    }

    //设置速度
    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }

    //文字颜色
    public void setColor(int... mColors) {
        this.mColors = mColors;
        for (int i = 0; i < mColors.length; i++) {
            mPaints[i].setColor(mColors[i]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTexts != null) {
            NewsEntity.DataEntity ad = mTexts.get(mIndex);
            String text = "【" + ad.getCategory() + "】" + ad.getFullhead();

            Rect rect0 = new Rect();
            mPaints[0].getTextBounds(text, 0, text.length(), rect0);
            /*Rect rect1 = new Rect();
            mPaints[1].getTextBounds(texts[1], 0, texts[1].length(), rect1);
            Rect rect2 = new Rect();
            mPaints[2].getTextBounds(texts[2], 0, texts[2].length(), rect2);*/

            if (mY == 0 && hasInit == false) {
                mY = getMeasuredHeight() - rect0.top;
                hasInit = true;
            }
            //移动到最上面
            if (mY <= 0 - rect0.bottom) {
                mY = getMeasuredHeight() - rect0.top;
                mIndex++;
                isPaused = false;
            }
            canvas.drawText(text, 0, mY, mPaints[0]);
//            canvas.drawText(texts[1], (rect0.right - rect0.left) + 10, mY, mPaints[1]);
//            canvas.drawText(texts[2], (rect1.right + rect0.right - rect0.left * 2) + 10 * 2, mY, mPaints[2]);
//            canvas.drawText(texts[3], (rect2.right + rect1.right + rect0.right - rect0.left * 3) + 15 * 3, mY, mPaints[3]);

            //移动到中间
            if (!isPaused && mY <= getMeasuredHeight() / 2 - (rect0.top + rect0.bottom) / 2) {
                isMove = false;
                isPaused = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        postInvalidate();
                        isMove = true;
                    }
                }, mInterval);
            }
            mY -= mSpeed;
            //循环使用数据
            if (mIndex == mTexts.size()) {
                mIndex = 0;
            }
            //如果是处于移动状态时的,则延迟绘制
            //计算公式为一个比例,一个时间间隔移动组件高度,则多少毫秒来移动1像素
            if (isMove) {
                postInvalidateDelayed(2);
            }
        }
    }

    public int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}

package com.dolphinwang.imagecoverflow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zsd on 2017/10/23.
 */

public class PagerIndicator extends LinearLayout implements CoverFlowView.OnPageChangeListener{

    private final int PADDING_LEFT = 5;
    private final int PADDING_RIGHT = 5;
    private final int PADDING_TOP = 0;
    private final int PADDING_BOTTOM = 0;

    private final int DUA = 0;

    private CoverFlowView mCoverFlowView;

    private Context mContext;

    private Timer mTimer;

    /**
     */
    private Timer mResumingTimer;
    private TimerTask mResumingTask;

    /**
     * the duration between animation.
     */
    private long mSliderDuration = 4000;

    //页面总数
    private final int PAGE_COUNT = 3;

    private final int ROUND_SIZE = 15;

    private ArrayList<ImageView> mIndicators = new ArrayList<ImageView>();

    private GradientDrawable mUnSelectedGradientDrawable;
    private GradientDrawable mSelectedGradientDrawable;


    public PagerIndicator(Context context) {
        this(context,null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void setCoverFlowView(CoverFlowView coverFlowView){
        mCoverFlowView = coverFlowView;
        mCoverFlowView.setOnPageChangeListener(this);

    }

    private void initView() {
        setOrientation(LinearLayout.HORIZONTAL);

        mTimer = new Timer();
        setTimerTask();

        mSelectedGradientDrawable = new GradientDrawable();
        mUnSelectedGradientDrawable = new GradientDrawable();

//        setUpDrawabel(mSelectedGradientDrawable,Color.GRAY, dp2px(ROUND_SIZE) , dp2px(ROUND_SIZE));
//        setUpDrawabel(mUnSelectedGradientDrawable,Color.BLACK, dp2px(ROUND_SIZE) , dp2px(ROUND_SIZE));
//        setUpDrawabel(mUnSelectedGradientDrawable,Color.argb(33,255,255,255) ,dp2px(ROUND_SIZE), dp2px(ROUND_SIZE));

        for (int i = 0; i < PAGE_COUNT; i ++){
            addIndicator();
        }
    }

    private void addIndicator(){
        ImageView indicator = new ImageView(mContext);
//            indicator.setImageDrawable(mContext.getResources().getDrawable(R.drawable.point_bg_enable));
        indicator.setLayoutParams(new LayoutParams(dp2px(ROUND_SIZE),dp2px(ROUND_SIZE)));
//            indicator.setImageDrawable(mUnSelectedGradientDrawable);
        indicator.setPadding(
                dp2px(PADDING_LEFT),
                dp2px(PADDING_TOP),
                dp2px(PADDING_RIGHT),
                dp2px(PADDING_BOTTOM));
        addView(indicator);
        mIndicators.add(indicator);
    }

    public void setPageCount(int pageCount){
        if (pageCount > PAGE_COUNT){
            removeAllViews();
            mIndicators.clear();
            for (int i = 0; i< pageCount; i++){
                addIndicator();
            }
        }
    }

    private int dp2px(int dpSize){
        return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize ,mContext.getResources().getDisplayMetrics());
    }

    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, 4000, 4000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }

    /**
     * do some action
     */
    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    if (mCoverFlowView != null){
                        mCoverFlowView.toNext();
                    }
                    // do some action
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * start auto cycle.
     * @param delay delay time
     * @param duration animation duration time.
     * @param autoRecover if recover after user touches the slider.
     */
    public void startAutoCycle(long delay,long duration,boolean autoRecover){
//        if(mCycleTimer != null) mCycleTimer.cancel();
//        if(mCycleTask != null) mCycleTask.cancel();
//        if(mResumingTask != null) mResumingTask.cancel();
//        if(mResumingTimer != null) mResumingTimer.cancel();
//        mSliderDuration = duration;
//        mCycleTimer = new Timer();
//        mAutoRecover = autoRecover;
//        mCycleTask = new TimerTask() {
//            @Override
//            public void run() {
//                mh.sendEmptyMessage(0);
//            }
//        };
//        mCycleTimer.schedule(mCycleTask,delay,mSliderDuration);
//        mCycling = true;
//        mAutoCycle = true;
    }

    /**
     * pause auto cycle.
     */
    private void pauseAutoCycle(){
//        if(mCycling){
//            mCycleTimer.cancel();
//            mCycleTask.cancel();
//            mCycling = false;
//        }else{
//            if(mResumingTimer != null && mResumingTask != null){
//                recoverCycle();
//            }
//        }
    }

    /**
     * stop the auto circle
     */
    public void stopAutoCycle(){
//        if(mCycleTask!=null){
//            mCycleTask.cancel();
//        }
//        if(mCycleTimer!= null){
//            mCycleTimer.cancel();
//        }
//        if(mResumingTimer!= null){
//            mResumingTimer.cancel();
//        }
//        if(mResumingTask!=null){
//            mResumingTask.cancel();
//        }
//        mAutoCycle = false;
//        mCycling = false;
    }

    /**
     * when paused cycle, this method can weak it up.
     */
    private void recoverCycle(){
//        if(!mAutoRecover || !mAutoCycle){
//            return;
//        }
//
//        if(!mCycling){
//            if(mResumingTask != null && mResumingTimer!= null){
//                mResumingTimer.cancel();
//                mResumingTask.cancel();
//            }
//            mResumingTimer = new Timer();
//            mResumingTask = new TimerTask() {
//                @Override
//                public void run() {
//                    startAutoCycle();
//                }
//            };
//            mResumingTimer.schedule(mResumingTask, 6000);
//        }
    }

    private void setUpDrawabel(GradientDrawable drawabel,int color, int width,int height){
        drawabel.setShape(GradientDrawable.OVAL);
        drawabel.setColor(color);
        drawabel.setSize( dp2px(width) ,
                dp2px(height) );
    }

    @Override
    public void onPageSelected(int position) {
        if (mIndicators != null
                && mIndicators.size() > 0){
            for (int i = 0; i < mIndicators.size(); i++){
                if (position == i){
//                    mIndicators.get(i).setImageDrawable(mSelectedGradientDrawable);
                    mIndicators.get(i).setImageResource(R.drawable.ic_round_selected);
                }else {
                    mIndicators.get(i).setImageResource(R.drawable.ic_round_normal);
                }
            }
        }
    }
}

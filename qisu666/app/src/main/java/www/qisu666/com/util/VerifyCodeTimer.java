package www.qisu666.com.util;

import android.os.CountDownTimer;

import www.qisu666.com.application.IDianNiuApp;

/**
 * 计时器
 * Created by Administrator on 2016/4/11.
 *
 */
public class VerifyCodeTimer extends CountDownTimer {

    private OnTimerListener listener;
    private long millisUntilFinished;
    /**
     *
     *      @param millisInFuture
     *      表示以毫秒为单位 倒计时的总数
     *      例如 millisInFuture=1000 表示1秒
     *
     *       @param countDownInterval
     *      表示 间隔 多少微秒 调用一次 onTick 方法
     *      例: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
     */
    public VerifyCodeTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setListener(OnTimerListener listener) {
        this.listener = listener;
    }

    public long getMillisUntilFinished() {
        return millisUntilFinished;
    }

    @Override
    public void onFinish() {
        if(listener!=null){
            listener.onFinish();
        }
        IDianNiuApp.verifyCodeTimer = null;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        this.millisUntilFinished = millisUntilFinished;
        if(listener!=null){
            listener.onTick(millisUntilFinished);
        }

    }

    public interface OnTimerListener{
        void onTick(long millisUntilFinished);
        void onFinish();
    }

}

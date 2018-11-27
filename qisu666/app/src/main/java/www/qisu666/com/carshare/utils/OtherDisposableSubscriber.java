package www.qisu666.com.carshare.utils;

import android.text.TextUtils;
import android.util.Log;

import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.carshare.Message;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by admin on 2018/1/16.
 */

public abstract class OtherDisposableSubscriber<T> extends DisposableSubscriber<T> {

    private final String TAG = OtherDisposableSubscriber.class.getName();

    @Override
    public void onNext(T message) {
        if (message != null){
            onSuccess(message);
        }else {
            Log.e( TAG,"onNext的数据为空");
            onFail();
        }
    }

    @Override
    public void onError(Throwable error) {
        if(error!=null){
            Log.e( TAG,"ResultSubscriber:"+error.getMessage());
            ToastUtil.showToast(R.string.toast_network_server_outage);
        } else {
            ToastUtil.showToast(R.string.toast_network_interrupt);
        }
        onFail();
    }

    @Override
    public void onComplete() {
        Log.w( TAG,"onComplete:");
    }

    public abstract void onSuccess(T bean);

    public abstract void onFail();
}

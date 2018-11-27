package www.qisu666.com.carshare;

import android.content.Context;
import android.util.Log;

import www.qisu666.common.model.HttpResult;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.model.CarListBean;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by admin on 2018/1/15.
 */

//public abstract class BaseSubscriber<T> implements Subscriber<Message<T>> {
//
//
//    @Override
//    public void onSubscribe(Subscription s) {
//        Log.e( "guanglog","onSubscribe:");
//
//    }
//
//    @Override
//    public void onNext(Message<T> message) {
//        Log.e( "guanglog","onNext:");
//    }
//
//    @Override
//    public void onError(Throwable error) {
//        if(error!=null){
//            Log.e( "guanglog","ResultSubscriber:"+error.getMessage());
//            ToastUtil.showToast(R.string.toast_network_server_outage);
//        } else {
//            ToastUtil.showToast(R.string.toast_network_interrupt);
//        }
//    }
//
//    @Override
//    public void onComplete() {
//        Log.e( "guanglog","onComplete:");
//    }

//}

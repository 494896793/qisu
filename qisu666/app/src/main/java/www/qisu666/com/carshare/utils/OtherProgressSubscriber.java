package www.qisu666.com.carshare.utils;

import www.qisu666.com.carshare.Message;
import www.qisu666.com.widget.LoadingDialog;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by admin on 2018/1/15.
 */

public abstract class OtherProgressSubscriber<T> extends OtherDisposableSubscriber<T> {

    private LoadingDialog dialog;

    protected OtherProgressSubscriber(LoadingDialog dialog) {
        this.dialog = dialog;
    }

    @Override public void onError(Throwable e) {
        LoadingDialogHelper.cancelDialog(dialog);
        super.onError(e);
        onFail();
    }

    @Override
    public void onNext(T message) {
        LoadingDialogHelper.cancelDialog(dialog);
        super.onNext(message);
    }

    @Override public void onComplete() {
        if (dialog != null) dialog.dismiss();
        super.onComplete();
    }


}

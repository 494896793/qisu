package www.qisu666.com.carshare.utils;

import www.qisu666.com.carshare.Message;
import www.qisu666.com.widget.LoadingDialog;

/**
 * Created by admin on 2018/1/15.
 */

public abstract class ProgressSubscriber<T> extends MyDisposableSubscriber<T> {

    private LoadingDialog dialog;

    protected ProgressSubscriber(LoadingDialog dialog) {
        this.dialog = dialog;
    }

    @Override public void onError(Throwable e) {
        LoadingDialogHelper.cancelDialog(dialog);
        super.onError(e);
    }

    @Override
    public void onNext(Message<T> message) {
        LoadingDialogHelper.cancelDialog(dialog);
        super.onNext(message);
    }

    @Override public void onComplete() {
        if (dialog != null) dialog.dismiss();
        super.onComplete();
    }


}

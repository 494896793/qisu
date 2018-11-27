package www.qisu666.com.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import www.qisu666.com.R;
import com.squareup.picasso.Picasso;


/**
 * Created by Administrator on 2015/8/19.
 */
public class AlertPhotoDialog extends Dialog {

    OnDialogButtonClickListener listener;
    private Context context;

    private String title = "";
    private String message,confirm = "确定",cancel = "取消";

    private int resId;
    private boolean show;
    private boolean isTwoConfirm;

    private TextView message_tv;
    private TextView title_tv;
    private TextView confirm_btn,cancel_btn;
    private LinearLayout btn_group2;
    private TextView confirm_btn2;
    private ImageView img_dialog_top;

    /**
     * 两个确认按钮
     * @param context
     */
    public AlertPhotoDialog(Context context, String confirm, String cancel, String title, int resId) {
        this(context,confirm,cancel,title,null, resId ,true, true);
    }


    /**
     * 确认取消按钮
     * @param context
     * @param message 提示信息
     * @param show true 显示 确认/取消 两个按钮  false 仅显示 确认 按钮
     */
    public AlertPhotoDialog(Context context, String confirm, String cancel, String title, String message, int resId) {
        this(context,confirm,cancel,title,message, resId ,true, false);
    }


    /**
     *
     * @param context
     * @param confirm 点击按钮文字
     * @param cancel 取消按钮文字
     * @param title 标题
     * @param message 提示信息
     * @param show true 显示 确认/取消 两个按钮  false 仅显示 确认 按钮
     */
    public AlertPhotoDialog(Context context, String confirm, String cancel, String title, String message, int resId, boolean show, boolean isTwoConfirm) {
        super(context, R.style.Dialog_Alert);
        this.context = context;
        if(confirm!=null){
            this.confirm = confirm;
        }
        if(cancel!=null){
            this.cancel = cancel;
        }
        this.message = message;
        this.title = title;
        this.show = show;
        this.resId = resId;
        this.isTwoConfirm = isTwoConfirm;
    }

    /**
     *
     * @param context
     * @param confirm 点击按钮文字
     * @param cancel 取消按钮文字
     * @param title 标题
     * @param message 提示信息
     * @param show true 显示 确认/取消 两个按钮  false 仅显示 确认 按钮
     */
    public AlertPhotoDialog(Context context, String confirm, String cancel, String title,int resId, String message, boolean show) {
        super(context, R.style.Dialog_Alert);
        this.context = context;
        if(confirm!=null){
            this.confirm = confirm;
        }
        if(cancel!=null){
            this.cancel = cancel;
        }
        this.message = message;
        this.title = title;
        this.show = show;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_alert_photo);
        iniView();
    }

    private void iniView() {
        title_tv = (TextView)findViewById(R.id.title);
        message_tv = (TextView)findViewById(R.id.message);
        confirm_btn = (TextView) findViewById(R.id.confirm_btn);
        img_dialog_top = (ImageView) findViewById(R.id.img_dialog_top);
        btn_group2 = (LinearLayout) findViewById(R.id.btn_group2);

        message_tv.setText(message);
        confirm_btn.setText(confirm);

        if (!TextUtils.isEmpty(title)){
            title_tv.setVisibility(View.VISIBLE);
            title_tv.setText(title);
        }else {
            title_tv.setVisibility(View.GONE);
        }

        Picasso.with(confirm_btn.getContext()).load(resId).into(img_dialog_top);

        if (isTwoConfirm){
            show = false;

            btn_group2.setVisibility(View.VISIBLE);

            confirm_btn2 = (TextView) findViewById(R.id.confirm_btn2);

            confirm_btn2.setVisibility(View.VISIBLE);

            confirm_btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        listener.onCancel();
                    }
                    cancel();
                }
            });
            confirm_btn2.setText(cancel);

        }


        if (show) {
            cancel_btn = (TextView) findViewById(R.id.cancel_btn);

            cancel_btn.setVisibility(View.VISIBLE);

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        listener.onCancel();
                    }
                    cancel();
                }
            });
            cancel_btn.setText(cancel);

        }

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onConfirm();
                }
                cancel();
            }
        });
    }

    /**
     * 设置监听器
     * @param listener 按钮监听器
     * */
    public void setSampleDialogListener(OnDialogButtonClickListener listener){
        this.listener = listener;
    }

    public interface OnDialogButtonClickListener{
        void onConfirm();
        void onCancel();
    }

    public TextView getMessage_tv(){
        return message_tv;
    }

}

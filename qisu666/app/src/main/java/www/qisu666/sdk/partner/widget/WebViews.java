package www.qisu666.sdk.partner.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 717219917@qq.com 2018/4/18 20:27.
 */
public class WebViews extends WebView {
    ScrollInterface web;
    public WebViews(Context context) {
        super(context);
    }

    public WebViews(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WebViews(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WebViews(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    public void setOnCustomScroolChangeListener(ScrollInterface t) {
        this.web = t;
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        web.onSChanged(l, t, oldl, oldt);
    }

    /** 定义滑动接口 */
    public interface ScrollInterface {

        public void onSChanged(int l, int t, int oldl, int oldt);
    }


}

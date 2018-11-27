package www.qisu666.com.logic;

import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import www.qisu666.com.application.IDianNiuApp;

/**
 * Created by Administrator on 2016/5/18.
 */
public class ImageLogic {

    /**
     * 发送图片请求
     * @param url
     * @param imageView
     */
    public static void sendRequest(String url, ImageView imageView, int defaultImageResId, int errorImageResId){
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, defaultImageResId, errorImageResId);
        IDianNiuApp.getInstance().getVolleyImageLoader().get(url, listener);
    }

}

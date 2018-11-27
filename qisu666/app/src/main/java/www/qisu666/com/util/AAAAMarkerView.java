package www.qisu666.com.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

import www.qisu666.com.R;

/**
 * 717219917@qq.com 2018/11/23 16:09.
 */
public class AAAAMarkerView {

    private Context context;//上下文
    private MarkerOptions options;//marker类
    private ArrayList<MarkerOptions> includeMarkers;//当前可观区域里的 聚合过之后的集合
    private LatLngBounds bounds;// 创建区域

    public AAAAMarkerView(Context context, MarkerOptions firstMarkers, Projection projection, int gridSize){
        this.context=context;
        options=new MarkerOptions();
        Point point=projection.toScreenLocation(firstMarkers.getPosition());
        Point southwestPoint = new Point(point.x - gridSize, point.y + gridSize);//范围类
        Point northeastPoint = new Point(point.x + gridSize, point.y - gridSize);//范围类
        options.title(firstMarkers.getTitle());
        bounds=new LatLngBounds(projection.fromScreenLocation(southwestPoint), projection.fromScreenLocation(northeastPoint));
        options.anchor(0.5f,0.5f).position(firstMarkers.getPosition()).title(firstMarkers.getTitle()).icon(firstMarkers.getIcon());
        includeMarkers=new ArrayList<>();
        includeMarkers.add(firstMarkers);
    }


    public LatLngBounds getBounds() {
        return bounds;
    }

    public void setPositionAndIcon(){
        int size = includeMarkers.size();
        double lat = 0.0;
        double lng = 0.0;
        if(size==1){
            options.position(new LatLng(includeMarkers.get(0).getPosition().latitude,includeMarkers.get(0).getPosition().longitude));
            options.title(includeMarkers.get(0).getTitle());
            int type = 0;//新添加 区分type
            options.icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(getView(1,1))));
        }else{
            options.position(new LatLng(includeMarkers.get(0).getPosition().latitude,includeMarkers.get(0).getPosition().longitude));
            options.title(includeMarkers.get(0).getTitle());
            options.icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(getView(size,3))));
        }
    }

    public MarkerOptions getOptions() {
        return options;
    }

    public void setOptions(MarkerOptions options) {
        this.options = options;
    }

    public void addMarker(MarkerOptions markerOptions){
        includeMarkers.add(markerOptions);
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(View view) {
        Bitmap bitmap = null;
        try {
            if (view != null) {
                view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                view.buildDrawingCache();
                bitmap = view.getDrawingCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //单个时 调用
    public View getView(int num, int type) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_carshare_juhe1, null);
        TextView txt_num = (TextView) view.findViewById(R.id.view_gaode_txt_num);               //数字
        ImageView img_portrait = (ImageView) view.findViewById(R.id.view_gaode_img_portrait);   //img
        img_portrait.setPadding(8, 8, 8, 12);
//		if (num > 1) {
//			txt_num.setText(num + "");
//		} else if (num == 1) {

//		    txt_num.setTextSize(13);
        if(type==1&&num==1){
            txt_num.setText("");
        }else{
            txt_num.setText(num + "");
        }
        txt_num.setTextColor(context.getResources().getColor(R.color.text_white));
        if (type == 0) {
//				img_portrait.setBackgroundResource(R.drawable.green_pin);
            img_portrait.setBackgroundResource(R.mipmap.yc_3);
        } else if (type == 1) {
            img_portrait.setBackgroundResource(R.mipmap.yc_4);
        } else if (type == 2) {
            img_portrait.setBackgroundResource(R.mipmap.yc_5);
        }
//			}
        return view;
    }
}

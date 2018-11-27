package www.qisu666.sdk.amap.stationMap.juhe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

import www.qisu666.com.R;

public class MarkerImageView {

    private Context context;//上下文
    private MarkerOptions options;//marker类
    private ArrayList<MarkerOptions> includeMarkers;//当前可观区域里的 聚合过之后的集合
    private LatLngBounds bounds;// 创建区域

    /**
     * 头像加载完监听
     */
    public MarkerImageView(Context context, MarkerOptions firstMarkers, Projection projection, int gridSize, int num) {
        this.context = context;
        options = new MarkerOptions();
        Point point = projection.toScreenLocation(firstMarkers.getPosition());
        Point southwestPoint = new Point(point.x - gridSize, point.y + gridSize);//范围类
        Point northeastPoint = new Point(point.x + gridSize, point.y - gridSize);//范围类
        options.title(firstMarkers.getTitle());
        bounds = new LatLngBounds(projection.fromScreenLocation(southwestPoint), projection.fromScreenLocation(northeastPoint));
        options.anchor(0.5f, 0.5f).title(firstMarkers.getTitle()).position(firstMarkers.getPosition())//设置初始化marker属性
                .icon(firstMarkers.getIcon()).snippet(firstMarkers.getSnippet());
        includeMarkers = new ArrayList<MarkerOptions>();
        includeMarkers.add(firstMarkers);
    }

    public MarkerImageView(Context context) {
        this.context = context;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public MarkerOptions getOptions() {
        return options;
    }

    public void setOptions(MarkerOptions options) {
        this.options = options;
    }

    /**
     * 添加marker
     */
    public void addMarker(MarkerOptions markerOptions) {
        includeMarkers.add(markerOptions);// 添加到列表中
    }

    /**
     * 设置聚合点的中心位置以及图标
     */
    public void setpositionAndIcon() {
        int size = includeMarkers.size();
        double lat = 0.0;
        double lng = 0.0;
        if (size == 1) {//一个,设置marker单个属性
            options.position(new LatLng(includeMarkers.get(0).getPosition().latitude, includeMarkers.get(0).getPosition().longitude));// 设置marker位置
            options.title(includeMarkers.get(0).getTitle());//单个
//			options.icon(BitmapDescriptorFactory .fromBitmap(getViewBitmap(getView(size))));

            int type = 0;//新添加 区分type
            String type_tmp = includeMarkers.get(0).getTitle();
//			type_tmp=type_tmp.substring(type_tmp.indexOf(",")+1,type_tmp.length());
            type_tmp = type_tmp.substring(type_tmp.indexOf(",") + 1, type_tmp.indexOf("."));//类型
            type = Double.valueOf(type_tmp).intValue();

//			String type_tmp2=includeMarkers.get(0).getTitle();
//			type_tmp2=type_tmp2.substring(0,type_tmp2.indexOf(","));
//			size= Integer.parseInt(type_tmp2);

            String type_tmp2 = includeMarkers.get(0).getTitle();
            type_tmp2 = type_tmp2.substring(type_tmp2.indexOf("-") + 1, type_tmp2.length());
            size = Double.valueOf(type_tmp2).intValue();

            options.icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(getView(size, type))));
        } else {// 聚合时
            int sizes = 0;
            for (MarkerOptions op : includeMarkers) {//设置marker聚合属性
                lat += op.getPosition().latitude;
                lng += op.getPosition().longitude;
                String type_tmp2 = op.getTitle();
                type_tmp2 = type_tmp2.substring(0, type_tmp2.indexOf(","));
//				type_tmp2=type_tmp2.substring(type_tmp2.indexOf(".")+1,type_tmp2.length());
                int size2 = Double.valueOf(type_tmp2).intValue();
                sizes += size2;
            }
            options.position(new LatLng(lat / size, lng / size));//设置marker的位置为中心位置为聚集点的平均位置
            options.title("d");    //多个
            options.icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(getView(sizes))));
        }
    }

    /**
     * marker视图 多个
     */
    public View getView(int num) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_gaode_imgs, null);
        TextView txt_num = (TextView) view.findViewById(R.id.view_gaode_txt_num);//数字
        txt_num.setTextColor(context.getResources().getColor(R.color.text_white));
        ImageView img_portrait = (ImageView) view.findViewById(R.id.view_gaode_img_portrait);//img

        img_portrait.setPadding(8, 8, 8, 12);
//		if (num > 1) {
        txt_num.setText(num + "");
//		} else if (num == 1) {
//			    txt_num.setText(num + "");
////			img_portrait.setBackgroundResource(R.drawable.green_pin);
//		}else { txt_num.setText(num + ""); }
        return view;
    }

    //单个时 调用
    public View getView(int num, int type) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_gaode_img, null);
        TextView txt_num = (TextView) view.findViewById(R.id.view_gaode_txt_num);//数字
        ImageView img_portrait = (ImageView) view.findViewById(R.id.view_gaode_img_portrait);//img

        img_portrait.setPadding(8, 8, 8, 12);
//		if (num > 1) {
//			txt_num.setText(num + "");
//		} else if (num == 1) {

//		    txt_num.setTextSize(13);
        txt_num.setText(num + "");
        txt_num.setTextColor(context.getResources().getColor(R.color.text_white));
        if (type == 3) {
//				img_portrait.setBackgroundResource(R.drawable.green_pin);
            img_portrait.setBackgroundResource(R.mipmap.yd_11);
        } else if (type == 2) {
            img_portrait.setBackgroundResource(R.mipmap.yd_9);
        } else if (type == 1) {
            img_portrait.setBackgroundResource(R.mipmap.yd_10);
        }


//			}
        return view;
    }


    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(View view) {
        Bitmap bitmap = null;
        try {
            if (view != null) {
                view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                view.buildDrawingCache();
                bitmap = view.getDrawingCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
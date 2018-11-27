package www.qisu666.com.event;

/**
 * Created by admin on 2018/1/17.
 */

public class CarMapEvent extends BaseEvent{

    public int position;
    public int viewId;
    public boolean isFromCarSlide;

    public String text;

    public CarMapEvent(String text){
        this.text = text;
    }

    public CarMapEvent(boolean isFromCarSlide){
        this.isFromCarSlide = isFromCarSlide;
    }

    public CarMapEvent(int viewId){
       this.viewId = viewId;
    }

    public CarMapEvent(int viewId, int position) {
        this.viewId = viewId;
        this.position = position;
    }
}

package www.qisu666.com.event;

/**
 * Created by Administrator on 2016/9/6.
 */
public class NotificationUpdateEvent extends BaseEvent {
    private int position;

    public NotificationUpdateEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

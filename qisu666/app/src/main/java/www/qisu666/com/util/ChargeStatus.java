package www.qisu666.com.util;

/**
 * Created by admin on 2016/6/7.
 */
public enum ChargeStatus {

    INSTANCE;

    public static final int STATUS_CHARGING = 3001;
    public static final int STATUS_FREE = 3000;
    public static final int STATUS_UNKNOWN = 3002;
    public static final int STATUS_PREPARE_STOP = 3003;

    private int status = STATUS_UNKNOWN;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

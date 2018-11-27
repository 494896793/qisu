package www.qisu666.common.model;

/**
 * Created by zhang on 2017/2/21.
 */

public class HttpResult<T> {
    /**
     * data : null
     * success : false
     * msg : 不存在该用户
     * code : -1
     */

    public T data;
    public boolean success;
    public String msg;
    public int code;
}

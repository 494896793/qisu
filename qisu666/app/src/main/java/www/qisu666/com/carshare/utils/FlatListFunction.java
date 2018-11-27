package www.qisu666.com.carshare.utils;

import www.qisu666.common.model.HttpResult;
import www.qisu666.com.carshare.Message;

import java.util.List;

import io.reactivex.functions.Function;

/**
 * Created by admin on 2018/1/15.
 */

public class FlatListFunction<T> implements Function<HttpResult<String>, Message<List<T>>> {

    private final Class<T> type;

    public FlatListFunction(Class<T> type) {
        this.type = type;
    }

    @Override
    public Message<List<T>> apply(HttpResult<String> bean) throws Exception {
        return MyMessageUtils.readMessageList(bean.data, type);
    }

}

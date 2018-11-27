package www.qisu666.com.carshare.utils;

import com.google.gson.reflect.TypeToken;
import www.qisu666.common.model.HttpResult;
import www.qisu666.com.carshare.Message;

import java.lang.reflect.Type;

import io.reactivex.functions.Function;

/**
 * Created by admin on 2018/1/15.
 */

public class FlatFunction<T> implements Function<HttpResult<String>, Message<T>> {

    private final Class<T> type;

    public FlatFunction(Class<T> type) {
        this.type = type;
    }

    @Override
    public Message<T> apply(HttpResult<String> bean) throws Exception {
        return MyMessageUtils.readMessage(bean.data, type);
    }
}

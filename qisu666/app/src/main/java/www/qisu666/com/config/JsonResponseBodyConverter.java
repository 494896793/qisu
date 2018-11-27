package www.qisu666.com.config;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import www.qisu666.common.model.HttpResult;

/**
 * @author ldl
 * 717219917@qq.com 2018/10/14 18:35.
 */
public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;


    JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
//        String result="{\"msg\":\"查询成功\",\"code\":1001,\"data\":[{\"pileFastNumFree\":3.0,\"chargeStationType\":\"3\",\"pileSlowNumFree\":4.0,\"latitude\":\"22.614214\",\"totalPileCount\":9.0,\"stationId\":430.0,\"longitude\":\"114.040773\"},{\"pileFastNumFree\":0.0,\"chargeStationType\":\"2\",\"pileSlowNumFree\":0.0,\"latitude\":\"22.522868\",\"totalPileCount\":1.0,\"stationId\":428.0,\"longitude\":\"113.952437\"},{\"pileFastNumFree\":4.0,\"chargeStationType\":\"3\",\"pileSlowNumFree\":5.0,\"latitude\":\"22.688115\",\"totalPileCount\":14.0,\"stationId\":434.0,\"longitude\":\"114.262216\"},{\"pileFastNumFree\":1.0,\"chargeStationType\":\"3\",\"pileSlowNumFree\":9.0,\"latitude\":\"22.557993\",\"totalPileCount\":10.0,\"stationId\":437.0,\"longitude\":\"114.032542\"},{\"pileFastNumFree\":0.0,\"chargeStationType\":\"1\",\"pileSlowNumFree\":10.0,\"latitude\":\"22.569222\",\"totalPileCount\":12.0,\"stationId\":438.0,\"longitude\":\"114.074161\"},{\"pileFastNumFree\":4.0,\"chargeStationType\":\"2\",\"pileSlowNumFree\":0.0,\"latitude\":\"22.561337\",\"totalPileCount\":5.0,\"stationId\":387.0,\"longitude\":\"113.974073\"},{\"pileFastNumFree\":3.0,\"chargeStationType\":\"2\",\"pileSlowNumFree\":0.0,\"latitude\":\"22.52379\",\"totalPileCount\":3.0,\"stationId\":427.0,\"longitude\":\"113.95326\"},{\"pileFastNumFree\":2.0,\"chargeStationType\":\"3\",\"pileSlowNumFree\":0.0,\"latitude\":\"22.503535\",\"totalPileCount\":5.0,\"stationId\":828.0,\"longitude\":\"114.05142\"},{\"pileFastNumFree\":0.0,\"chargeStationType\":\"1\",\"pileSlowNumFree\":0.0,\"latitude\":\"28.450572\",\"totalPileCount\":2.0,\"stationId\":848.0,\"longitude\":\"112.819676\"},{\"pileFastNumFree\":0.0,\"chargeStationType\":\"1\",\"pileSlowNumFree\":1.0,\"latitude\":\"22.675732\",\"totalPileCount\":1.0,\"stationId\":748.0,\"longitude\":\"113.812067\"},{\"pileFastNumFree\":0.0,\"chargeStationType\":\"3\",\"pileSlowNumFree\":0.0,\"latitude\":\"22.77317\",\"totalPileCount\":13.0,\"stationId\":367.0,\"longitude\":\"113.858168\"},{\"pileFastNumFree\":0.0,\"chargeStationType\":\"3\",\"pileSlowNumFree\":0.0,\"latitude\":\"22.700698\",\"totalPileCount\":4.0,\"stationId\":708.0,\"longitude\":\"114.041315\"},{\"pileFastNumFree\":2.0,\"chargeStationType\":\"2\",\"pileSlowNumFree\":0.0,\"latitude\":\"22.719673\",\"totalPileCount\":4.0,\"stationId\":429.";
        String result=value.string();
        HttpResult httpResult=null;
        try {
            try{
                httpResult=new HttpResult();
                JSONObject jsonObject1=new JSONObject(result);
                Map<String,Object> map=new HashMap<>();
                map.put("data",jsonObject1.opt("data"));
                String jsonString=gson.toJson(map);
                httpResult.data=jsonString;
                httpResult.msg=jsonObject1.optString("msg");
                httpResult.code=jsonObject1.optInt("code");
            }catch (Exception e){
                e.printStackTrace();
            }

            return (T) httpResult;
        } finally {
            value.close();
        }
    }
}

package www.qisu666.com.cardid;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2018/1/23.
 */

public class CardBackBean {


    /**
     * legality : {"Edited":0,"Photocopy":0,"ID Photo":1,"Screen":0,"Temporary ID Photo":0}
     * request_id : 1505963132,28e364aa-97c0-469d-8738-cd20762e0251
     * time_used : 418
     * valid_date : 2016.02.29-2026.02.28
     * issued_by : 北京市海淀区公安局
     * side : back
     */

    public LegalityBean legality;
    public String request_id;
    public int time_used;
    public String valid_date;
    public String issued_by;
    public String side;

    public static class LegalityBean {
        /**
         * Edited : 0
         * Photocopy : 0
         * ID Photo : 1
         * Screen : 0
         * Temporary ID Photo : 0
         */

        public int Edited;
        public int Photocopy;
        @SerializedName("ID Photo")
        public int _$IDPhoto138; // FIXME check this code
        public int Screen;
        @SerializedName("Temporary ID Photo")
        public int _$TemporaryIDPhoto205; // FIXME check this code
    }
}

package www.qisu666.com.model;

import com.google.gson.annotations.Since;

/**
 * Created by admin on 2018/2/6.
 */

public class CarConfirmBean {


    /**
     * idcardNum : 431024198905160032
     * relName : 李亮
     * licenseClass : 1
     * licenseIsAuth : 1
     * validFrom : 2019-09-18
     * issueDate : 2013-9-18
     * userCode : 10001624
     * idcardIsAuth : 1
     */

    public String idcardNum;
    public String relName;
    public String licenseClass;
    public int licenseIsAuth;
    public String validFrom;
    public String issueDate;
    public String userCode;
    public int idcardIsAuth;

    @Since(2.0)
    public String digitalStatus;
    @Since(2.0)
    public String digitalPassword;
    @Since(2.0)
    public String gesturesStatus;
    @Since(2.0)
    public String gesturesPassword;
}

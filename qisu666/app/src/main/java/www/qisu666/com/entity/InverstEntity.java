package www.qisu666.com.entity;

import java.util.List;

/**
 * 717219917@qq.com 2018/8/14 9:37.
 */
public class InverstEntity {

    private List<InverstProductEntity> list;
    private String hasSubscribeNumber;
    private String productNumbers;
    private String remainNumber;
    private String allTotalNumber;

    public InverstEntity() {
    }

    public List<InverstProductEntity> getList() {
        return list;
    }

    public void setList(List<InverstProductEntity> list) {
        this.list = list;
    }

    public String getHasSubscribeNumber() {
        return hasSubscribeNumber;
    }

    public void setHasSubscribeNumber(String hasSubscribeNumber) {
        this.hasSubscribeNumber = hasSubscribeNumber;
    }

    public String getProductNumbers() {
        return productNumbers;
    }

    public void setProductNumbers(String productNumbers) {
        this.productNumbers = productNumbers;
    }

    public String getRemainNumber() {
        return remainNumber;
    }

    public void setRemainNumber(String remainNumber) {
        this.remainNumber = remainNumber;
    }

    public String getAllTotalNumber() {
        return allTotalNumber;
    }

    public void setAllTotalNumber(String allTotalNumber) {
        this.allTotalNumber = allTotalNumber;
    }

}

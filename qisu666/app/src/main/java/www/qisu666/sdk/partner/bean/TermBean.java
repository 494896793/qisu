package www.qisu666.sdk.partner.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 717219917@qq.com 2018/4/13 15:02.
 */

/**认购列表中 单个认购*/
public class TermBean {

    /**总已认购分数*/
    private double hasSubscribeNumber=-1;
    /**总剩余分数*/
    private double remainNumber=-1;
    /**总分数*/
    private double allTotalNumber=-1;
    /**产品列表*/
    private List<ProductList> productList=new ArrayList<>();

    private double productNumbers=0.0;                //车辆数

    public double getProductNumbers() {
        return productNumbers;
    }

    public void setProductNumbers(double productNumbers) {
        this.productNumbers = productNumbers;
    }

    public void setHasSubscribeNumber(double hasSubscribeNumber) {
        this.hasSubscribeNumber = hasSubscribeNumber;
    }
    public double getHasSubscribeNumber() {
        return hasSubscribeNumber;
    }

    public void setRemainNumber(double remainNumber) {
        this.remainNumber = remainNumber;
    }
    public double getRemainNumber() {
        return remainNumber;
    }

    public void setAllTotalNumber(double allTotalNumber) {
        this.allTotalNumber = allTotalNumber;
    }
    public double getAllTotalNumber() {
        return allTotalNumber;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }
    public List<ProductList> getProductList() {
        return productList;
    }




}
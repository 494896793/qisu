package www.qisu666.sdk.partner.bean;

/**
 * 717219917@qq.com 2018/4/13 15:58.
 */
public class ProductList {

    /**产品周期*/
    private double period=-1;
    /**产品状态*/
    private String productStatus="";

    /**产品分数*/
    private double productNumber=-1;
    /**剩余分数*/
    private double surplusNumber=-1;
    /**座位数*/
    private double carSeatNum=-1;
    /**车牌号*/
    private String plateNumber="";
    /**产品标题*/
    private String productTitle="";
    /**融资总额*/
    private long totalAmount=-1;
    /**单笔认购金额*/
    private double subAmount=-1;
    /**产品编号*/
    private String productCode="";
    /**第几条*/
    private double ROWNO=-1;
    /**车辆编号*/
    private String carCode="";
    /**融资余额*/
    private long balance=-1;
    /**折扣*/
    private double subRebate=-1;
    /**产品类型*/
    private String productType="";
    /**产品类型描述*/
    private String productTypeCn="";
    /**图片路径*/
    private String carImgPath="";

    public String getCarImgPath() {
        return carImgPath;
    }

    public void setCarImgPath(String carImgPath) {
        this.carImgPath = carImgPath;
    }

    public void setPeriod(double period) {
        this.period = period;
    }
    public double getPeriod() {
        return period;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
    public String getProductStatus() {
        return productStatus;
    }

    public void setProductNumber(double productNumber) {
        this.productNumber = productNumber;
    }
    public double getProductNumber() {
        return productNumber;
    }

    public void setSurplusNumber(double surplusNumber) {
        this.surplusNumber = surplusNumber;
    }
    public double getSurplusNumber() {
        return surplusNumber;
    }

    public void setCarSeatNum(double carSeatNum) {
        this.carSeatNum = carSeatNum;
    }
    public double getCarSeatNum() {
        return carSeatNum;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    public String getProductTitle() {
        return productTitle;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }
    public long getTotalAmount() {
        return totalAmount;
    }

    public void setSubAmount(double subAmount) {
        this.subAmount = subAmount;
    }
    public double getSubAmount() {
        return subAmount;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getProductCode() {
        return productCode;
    }

    public void setROWNO(double ROWNO) {
        this.ROWNO = ROWNO;
    }
    public double getROWNO() {
        return ROWNO;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }
    public String getCarCode() {
        return carCode;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
    public long getBalance() {
        return balance;
    }

    public void setSubRebate(double subRebate) {
        this.subRebate = subRebate;
    }
    public double getSubRebate() {
        return subRebate;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
    public String getProductType() {
        return productType;
    }

    public void setProductTypeCn(String productTypeCn) {
        this.productTypeCn = productTypeCn;
    }
    public String getProductTypeCn() {
        return productTypeCn;
    }

}
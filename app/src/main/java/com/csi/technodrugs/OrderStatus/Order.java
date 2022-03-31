package com.csi.technodrugs.OrderStatus;

/**
 * Created by Jahid on 11/2/19.
 */
public class Order {
    String orderNo;
    String orderDate;
    String invoiceNo;
    String invoiceDate;
    String customerCode;
    String customerName;
    String storeCode;
    String storeName;
    String mpoOrderNo;
    String mpoGroup;
    String mpoGroupName;
    String srCode;
    String saleType;
    String territoryCode;
    String businessUnit;
    String paymentMode;
    String orderAmount;
    String invoiceAmount;

    public Order(String orderNo, String orderDate, String invoiceNo, String invoiceDate, String customerCode, String customerName, String storeCode, String storeName, String mpoOrderNo, String mpoGroup, String mpoGroupName, String srCode, String saleType, String territoryCode, String businessUnit, String paymentMode,String  orderAmount, String invoiceAmount) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.storeCode = storeCode;
        this.storeName = storeName;
        this.mpoOrderNo = mpoOrderNo;
        this.mpoGroup = mpoGroup;
        this.mpoGroupName = mpoGroupName;
        this.srCode = srCode;
        this.saleType = saleType;
        this.territoryCode = territoryCode;
        this.businessUnit = businessUnit;
        this.paymentMode = paymentMode;
        this.orderAmount = orderAmount;
        this.invoiceAmount = invoiceAmount;
    }
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getMpoOrderNo() {
        return mpoOrderNo;
    }

    public void setMpoOrderNo(String mpoOrderNo) {
        this.mpoOrderNo = mpoOrderNo;
    }

    public String getMpoGroup() {
        return mpoGroup;
    }

    public void setMpoGroup(String mpoGroup) {
        this.mpoGroup = mpoGroup;
    }

    public String getMpoGroupName() {
        return mpoGroupName;
    }

    public void setMpoGroupName(String mpoGroupName) {
        this.mpoGroupName = mpoGroupName;
    }

    public String getSrCode() {
        return srCode;
    }

    public void setSrCode(String srCode) {
        this.srCode = srCode;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }
    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }


}

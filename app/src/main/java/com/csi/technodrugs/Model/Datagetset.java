package com.csi.technodrugs.Model;

/**
 * Created by Jahid on 13-11-2018.
 */

public class Datagetset {
    String customerName;
    String customerCode;
    String orderDate;
    String deliverDate;
    String paymentMode;
    String orderRef;
    String productName;
    String productId;
    String packSize;
    String tradePrice;
    String quantity;
    String netAmount;
    String vat;
    String totalVat;
    String discount;
    String totalDiscount;
    String tradeValue;
    String drCode;

    public Datagetset(String customerName, String customerCode, String orderDate, String deliverDate, String paymentMode, String orderRef, String productName, String productId, String packSize, String tradePrice, String quantity, String netAmount, String vat, String totalVat, String discount, String totalDiscount, String tradeValue, String drCode) {
        this.customerName = customerName;
        this.customerCode = customerCode;
        this.orderDate = orderDate;
        this.deliverDate = deliverDate;
        this.paymentMode = paymentMode;
        this.orderRef = orderRef;
        this.productName = productName;
        this.productId = productId;
        this.packSize = packSize;
        this.tradePrice = tradePrice;
        this.quantity = quantity;
        this.netAmount = netAmount;
        this.vat = vat;
        this.totalVat = totalVat;
        this.discount = discount;
        this.totalDiscount = totalDiscount;
        this.tradeValue = tradeValue;
        this.drCode = drCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }
    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(String totalVat) {
        this.totalVat = totalVat;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getTradeValue() {
        return tradeValue;
    }

    public void setTradeValue(String tradeValue) {
        this.tradeValue = tradeValue;
    }
    public String getDrCode() {
        return drCode;
    }

    public void setDrCode(String drCode) {
        this.drCode = drCode;
    }
}

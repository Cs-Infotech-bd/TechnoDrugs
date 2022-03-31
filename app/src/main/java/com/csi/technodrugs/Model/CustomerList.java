package com.csi.technodrugs.Model;

/**
 * Created by Jahid on 1/4/19.
 */
public class CustomerList {
    String name;
    String careOf;
    String customerType;
    String territory;
    String market;
    String image;
    String address;
    public CustomerList(String name, String careOf, String customerType, String territory, String market, String image, String address) {
        this.name = name;
        this.careOf = careOf;
        this.customerType = customerType;
        this.territory = territory;
        this.market = market;
        this.image = image;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCareOf() {
        return careOf;
    }

    public void setCareOf(String careOf) {
        this.careOf = careOf;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

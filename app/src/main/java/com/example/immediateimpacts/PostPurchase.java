package com.example.immediateimpacts;

public class PostPurchase {

    private String purchId;

    private String purchDate; //temp

    private String purchType;

    private String purchItemPrice;

    private String purchShipPrice;

    private String purchTax;

    private String purchItemName;

    private String purchQuantity;

    private String purchDescription;

    private String purchSupplier;


    public PostPurchase(String purchId, String purchDate, String purchType, String purchItemPrice, String purchShipPrice, String purchTax, String purchItemName, String purchQuantity, String purchDescription, String purchSupplier) {
        this.purchId = purchId;
        this.purchDate = purchDate;
        this.purchType = purchType;
        this.purchItemPrice = purchItemPrice;
        this.purchShipPrice = purchShipPrice;
        this.purchTax = purchTax;
        this.purchItemName = purchItemName;
        this.purchQuantity = purchQuantity;
        this.purchDescription = purchDescription;
        this.purchSupplier = purchSupplier;
    }


    public String getPurchId() {
        return purchId;
    }

    public String getPurchDate() {
        return purchDate;
    }

    public String getPurchType() {
        return purchType;
    }

    public String getPurchItemPrice() {
        return purchItemPrice;
    }

    public String getPurchShipPrice() {
        return purchShipPrice;
    }

    public String getPurchTax() {
        return purchTax;
    }

    public String getPurchItemName() {
        return purchItemName;
    }

    public String getPurchQuantity() {
        return purchQuantity;
    }

    public String getPurchDescription() {
        return purchDescription;
    }

    public String getPurchSupplier() {
        return purchSupplier;
    }
}

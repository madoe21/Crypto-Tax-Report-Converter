package com.cryptotax.converter.data.in;

import com.cryptotax.converter.interfaces.DataIn;

public class KriptomatData implements DataIn {
    private String timestamp;
    private String transactionType;
    private String asset;
    private String amountTransacted;
    private String pricePerCoin;
    private String amountEUR;
    private String transactedInclusiveFees;
    private String address;
    private String notes;

    public KriptomatData(String timestamp, String transactionType, String asset, String amountTransacted,
            String pricePerCoin, String amountEUR, String transactedInclusiveFees, String address, String notes) {
        this.timestamp = timestamp;
        this.transactionType = transactionType;
        this.asset = asset;
        this.amountTransacted = amountTransacted;
        this.pricePerCoin = pricePerCoin;
        this.amountEUR = amountEUR;
        this.transactedInclusiveFees = transactedInclusiveFees;
        this.address = address;
        this.notes = notes;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getAsset() {
        return asset;
    }

    public String getAmountTransacted() {
        return amountTransacted;
    }

    public String getPricePerCoin() {
        return pricePerCoin;
    }

    public String getAmountEUR() {
        return amountEUR;
    }

    public String getTransactedInclusiveFees() {
        return transactedInclusiveFees;
    }

    public String getAddress() {
        return address;
    }

    public String getNotes() {
        return notes;
    }
}

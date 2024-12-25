package com.cryptotax.converter.data.in;

import com.cryptotax.converter.interfaces.DataIn;

public class SwissBorgData implements DataIn {
    private String localTime;
    private String timeUTC;
    private String type;
    private String currency;
    private String grossAmount;
    private String grossAmountLocalCurrency;
    private String fee;
    private String feeLocalCurrency;
    private String netAmount;
    private String netAmountLocalCurrency;
    private String note;
    private String localCurrency;

    public SwissBorgData(String localTime, String timeUTC, String type, String currency, String grossAmount,
            String grossAmountLocalCurrency, String fee, String feeLocalCurrency, String netAmount,
            String netAmountLocalCurrency,
            String note, String localCurrency) {
        this.localTime = localTime;
        this.timeUTC = timeUTC;
        this.type = type;
        this.currency = currency;
        this.grossAmount = grossAmount;
        this.grossAmountLocalCurrency = grossAmountLocalCurrency;
        this.fee = fee;
        this.feeLocalCurrency = feeLocalCurrency;
        this.netAmount = netAmount;
        this.netAmountLocalCurrency = netAmountLocalCurrency;
        this.note = note;
        this.localCurrency = localCurrency;
    }

    public String getLocalTime() {
        return localTime;
    }

    public String getTimeUTC() {
        return timeUTC;
    }

    public String getType() {
        return type;
    }

    public String getCurrency() {
        return currency;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public String getGrossAmountLocalCurrency() {
        return grossAmountLocalCurrency;
    }

    public String getFee() {
        return fee;
    }

    public String getFeeLocalCurrency() {
        return feeLocalCurrency;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public String getNetAmountLocalCurrency() {
        return netAmountLocalCurrency;
    }

    public String getNote() {
        return note;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }
}

package com.cryptotax.converter.data.out;

import com.cryptotax.converter.interfaces.DataOut;

public class BlockpitData implements DataOut {
    private String date;
    private String integrationName;
    private String label;
    private String outgoingAsset;
    private String outgoingAmount;
    private String incomingAsset;
    private String incomingAmount;
    private String feeAsset;
    private String feeAmount;
    private String comment;
    private String transactionId;

    public BlockpitData(String date, String integrationName, String label, String outgoingAsset, String outgoingAmount,
            String incomingAsset, String incomingAmount, String feeAsset, String feeAmount, String comment,
            String transactionId) {
        this.date = date;
        this.integrationName = integrationName;
        this.label = label;
        this.outgoingAsset = outgoingAsset;
        this.outgoingAmount = outgoingAmount;
        this.incomingAsset = incomingAsset;
        this.incomingAmount = incomingAmount;
        this.feeAsset = feeAsset;
        this.feeAmount = feeAmount;
        this.comment = comment;
        this.transactionId = transactionId;
    }

    public String getDate() {
        return date;
    }

    public String getIntegrationName() {
        return integrationName;
    }

    public String getLabel() {
        return label;
    }

    public String getOutgoingAsset() {
        return outgoingAsset;
    }

    public String getOutgoingAmount() {
        return outgoingAmount;
    }

    public String getIncomingAsset() {
        return incomingAsset;
    }

    public String getIncomingAmount() {
        return incomingAmount;
    }

    public String getFeeAsset() {
        return feeAsset;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public String getComment() {
        return comment;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public static String formatValue(String value) {
        if (value == null || value.isEmpty())
            return "";
        return value.replace(",", ".").trim();
    }
}

package com.cryptotax.converter.data.in;

import com.cryptotax.converter.interfaces.DataIn;

public class RevolutData implements DataIn {
    private String symbol;
    private String type;
    private String quantity;
    private String price;
    private String value;
    private String fees;
    private String date;

    public RevolutData(String symbol, String type, String quantity, String price, String value, String fees,
            String date) {
        this.symbol = symbol;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.value = value;
        this.fees = fees;
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getValue() {
        return value;
    }

    public String getFees() {
        return fees;
    }

    public String getDate() {
        return date;
    }
}

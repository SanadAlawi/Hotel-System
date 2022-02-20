package com.example.mainactivity;

public class Currency {

    private String currencyCode;
    private double currencyValue;

    public Currency(String currencyCode, double currencyValue) {
        this.currencyCode = currencyCode;
        this.currencyValue = currencyValue;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(double currencyValue) {
        this.currencyValue = currencyValue;
    }

    @Override
    public String toString() {
        return currencyCode;
    }

}

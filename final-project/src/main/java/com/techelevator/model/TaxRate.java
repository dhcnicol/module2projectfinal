package com.techelevator.model;

import java.math.BigDecimal;

public class TaxRate {
    private BigDecimal salesTax;

    public TaxRate() {}

    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(BigDecimal salesTax) {
        this.salesTax = salesTax;
    }
}

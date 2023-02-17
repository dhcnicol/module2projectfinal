package com.techelevator.model;

import java.math.BigDecimal;
import java.util.List;

public class Cart {

    private List<CartItem> cartItems;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal total;

    public Cart() {};

    public Cart(List<CartItem> cartItems,BigDecimal subtotal, BigDecimal taxAmount, BigDecimal total) {
        this.cartItems = cartItems;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.total = total;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}

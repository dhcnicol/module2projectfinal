package com.techelevator.dao;

import com.techelevator.model.CartItem;
import com.techelevator.model.Product;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface CartItemDao {

    CartItem getCartItemById(int cartItemId);

    BigDecimal getCartItemPrice(int cartItemId);


    CartItem getCartItemByProductId(int productId, int userId);

    List<CartItem> getCartItemsByUserId(int userId);

    void addCartItemToCart(int userId, int productId, int quantity);

    void deleteCartItemFromCart(int cartItemId);

}

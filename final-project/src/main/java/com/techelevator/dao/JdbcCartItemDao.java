package com.techelevator.dao;

import com.techelevator.model.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCartItemDao implements CartItemDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CartItem getCartItemById(int cartItemId) {
        String sql = "SELECT * FROM cart_item WHERE cart_item_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, cartItemId);
        if(results.next()) {
            return mapRowToCartItem(results);
        } else {
            return null;
        }
    }

    @Override
    public CartItem getCartItemByProductId(int productId, int userId) {
        String sql = "SELECT * FROM cart_item WHERE product_id = ? AND user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, productId, userId);
        if(results.next()) {
            return mapRowToCartItem(results);
        } else {
            return null;
        }
    }

    @Override
    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM cart_item WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while(results.next()) {
           cartItems.add(mapRowToCartItem(results));
        }
        return cartItems;
    }

    @Override
    public BigDecimal getCartItemPrice(int cartItemId) {
        String sql = "SELECT p.price * c.quantity " +
                "FROM cart_item AS c " +
                "LEFT OUTER JOIN product AS p ON c.product_id = p.product_id " +
                "WHERE c.cart_item_id = ?;";
        BigDecimal cartItemPrice = jdbcTemplate.queryForObject(sql, BigDecimal.class, cartItemId);
        return cartItemPrice;
    }

    @Override
    public void addCartItemToCart(int userId, int productId, int quantity) {
        String sql = "SELECT quantity FROM cart_item WHERE user_id = ? AND product_id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, productId);
        if (sqlRowSet.next()) {
            String updateCartItem = "UPDATE cart_item SET quantity = ? WHERE product_id = ? AND user_id = ?;";
            jdbcTemplate.update(updateCartItem, quantity, productId, userId);
        } else {
            String newItemSql = "INSERT INTO cart_item (user_id, product_id, quantity) VALUES (?,?,?) RETURNING cart_item_id;";
            int newCartItemId = jdbcTemplate.queryForObject(newItemSql, int.class, userId, productId, quantity);
            CartItem newCartItem = new CartItem(newCartItemId, userId, productId, quantity);
        }
    }

    @Override
    public void deleteCartItemFromCart(int cartItemId) {
        String sql = "DELETE FROM cart_item WHERE cart_item_id = ?;";
        jdbcTemplate.update(sql, cartItemId);
    }



    private CartItem mapRowToCartItem(SqlRowSet results) {
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(results.getInt("cart_item_id"));
        cartItem.setProductId(results.getInt("product_id"));
        cartItem.setUserId(results.getInt("user_id"));
        cartItem.setQuantity(results.getInt("quantity"));
        return cartItem;
    }
}
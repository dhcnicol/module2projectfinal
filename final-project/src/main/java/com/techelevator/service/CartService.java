package com.techelevator.service;

import com.techelevator.dao.CartItemDao;
import com.techelevator.dao.ProductDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;

@Component
public class CartService {

    private RestTemplate restTemplate = new RestTemplate();

    private UserDao userDao;
    private ProductDao productDao;
    private CartItemDao cartItemDao;

    public CartService(UserDao userDao, ProductDao productDao, CartItemDao cartItemDao) {
        this.userDao = userDao;
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> getCartItems(Principal principal) {
        User user = getUser(principal);
        List<CartItem> cartItems = cartItemDao.getCartItemsByUserId(user.getId());
        return cartItems;
    }

    public BigDecimal getSubtotalForCartItems(List<CartItem> cartItems) {
        BigDecimal subtotal = new BigDecimal("0.0");
        for(CartItem cartItem : cartItems) {
            int productId = cartItem.getProductId();
           Product product = productDao.getProduct(productId);
           BigDecimal price = product.getPrice();
          subtotal = subtotal.add(price.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        return subtotal;
    }

    public BigDecimal getTaxTotal(BigDecimal subtotal, BigDecimal taxRate) {
        BigDecimal taxTotal = subtotal.multiply(taxRate).setScale(2, RoundingMode.CEILING);
        return taxTotal;
    }

    public BigDecimal getTotal(BigDecimal subtotal, BigDecimal taxTotal) {
        BigDecimal total = subtotal.add(taxTotal).setScale(2, RoundingMode.CEILING);
        return total;
    }

    public void addProductToCart(Product product, int quantity, Principal principal) {
        User user = getUser(principal);
        cartItemDao.addCartItemToCart(user.getId(), product.getProductId(), quantity);
    }

    public void deleteItemFromCart(int productId, Principal principal) {
        User user = getUser(principal);
        int cartItemToDelete = cartItemDao.getCartItemByProductId(productId, user.getId()).getCartItemId();
        cartItemDao.deleteCartItemFromCart(cartItemToDelete);
    }

    private User getUser(Principal principal) {
        String username = principal.getName();
        User user = userDao.findByUsername(principal.getName());
        return user;
    }
}

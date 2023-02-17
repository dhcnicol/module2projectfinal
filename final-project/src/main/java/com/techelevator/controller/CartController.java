package com.techelevator.controller;

import com.techelevator.model.Cart;
import com.techelevator.model.CartItem;
import com.techelevator.model.Product;
import com.techelevator.service.CartService;
import com.techelevator.service.TaxService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping( path = "/cart")
public class CartController {

    private CartService cartService;
    private TaxService taxService;

    public CartController(CartService cartService, TaxService taxService) {
        this.cartService = cartService;
        this.taxService = taxService;
    }

    @RequestMapping (method = RequestMethod.GET)
    public Cart getCart(Principal principal) {
        Cart cart = new Cart();
        cart.setCartItems(cartService.getCartItems(principal));
        cart.setSubtotal(cartService.getSubtotalForCartItems(cart.getCartItems()));
        cart.setTaxAmount(cartService.getTaxTotal(cart.getSubtotal(), taxService.getTaxPercentage(principal)));
        cart.setTotal(cartService.getTotal(cart.getSubtotal(), cart.getTaxAmount()));
        return cart;
    }

    @RequestMapping(path = "/items", method = RequestMethod.POST)
    public void addItem(@Valid @RequestBody Product newProduct, int quantity, Principal principal) {
        cartService.addProductToCart(newProduct, quantity, principal );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/items/{id}", method = RequestMethod.DELETE)
    public void deleteCartItem(@PathVariable int id, Principal principal) {
        cartService.deleteItemFromCart(id, principal);
    }
}

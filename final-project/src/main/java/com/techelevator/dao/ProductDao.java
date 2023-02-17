package com.techelevator.dao;

import com.techelevator.model.Product;

import java.util.List;

public interface ProductDao {

    Product getProduct(int productId);

    List<Product> getAllProducts();

    List<Product> searchProductsByName(String name);

    List<Product> searchProductsBySku(String sku);

    List<Product> searchProductsBySkuAndName(String sku, String name);
}

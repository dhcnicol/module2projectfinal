package com.techelevator.dao;

import com.techelevator.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JdbcProductDao implements ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product getProduct(int productId) {
        Product product = null;
        String sql = "SELECT product_id, product_sku, name, description, price, image_name " +
                "FROM product " +
                "WHERE product_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, productId);
        if (results.next()) {
            product = mapRowToProduct(results);
        }
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, product_sku, name, description, price, image_name " +
                "FROM product " +
                "ORDER BY product_id";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Product product = mapRowToProduct(results);
            products.add(product);
        }
        return products;
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, product_sku, name, description, price, image_name " +
                "FROM product " +
                "WHERE name LIKE ? " +
                "ORDER BY product_id";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + name + "%");
        while (results.next()) {
            Product product = mapRowToProduct(results);
            products.add(product);
        }
        return products;
    }

    @Override
    public List<Product> searchProductsBySku(String sku) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, product_sku, name, description, price, image_name " +
                "FROM product " +
                "WHERE product_sku LIKE ? " +
                "ORDER BY product_id";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + sku + "%");
        while (results.next()) {
            Product product = mapRowToProduct(results);
            products.add(product);
        }
        return products;
    }

    @Override
    public List<Product> searchProductsBySkuAndName(String sku, String name) {
        List<Product> productsByName = searchProductsByName(name);
        List<Product> productsBySku = searchProductsBySku(sku);
        return productsBySku.stream().filter(productsByName::contains).collect(Collectors.toList());
    }

    private Product mapRowToProduct(SqlRowSet results) {
        Product product = new Product();
        product.setProductId(results.getInt("product_id"));
        product.setProductSku(results.getString("product_sku"));
        product.setProductName(results.getString("name"));
        product.setDescription(results.getString("description"));
        product.setPrice(results.getBigDecimal("price"));
        if (results.getString("image_name") != null) {
            product.setImageName(results.getString("image_name"));
        }
        return product;
    }
}

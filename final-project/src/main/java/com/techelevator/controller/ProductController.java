package com.techelevator.controller;

import com.techelevator.dao.JdbcProductDao;
import com.techelevator.dao.ProductDao;
import com.techelevator.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
@PreAuthorize("permitAll")
public class ProductController {

    private ProductDao dao;
    public ProductController(ProductDao productDao) {this.dao = productDao;}

   @RequestMapping(method = RequestMethod.GET)
    public List<Product> getProducts(@RequestParam(defaultValue = "") String sku,
                                     @RequestParam(defaultValue = "") String name){
        List<Product> products;
        if (!sku.isEmpty() && !name.isEmpty()) {
            products = dao.searchProductsBySkuAndName(sku, name);
        } else if (!sku.isEmpty() && name.isEmpty()) {
            products = dao.searchProductsBySku(sku);
        } else if (sku.isEmpty() && !name.isEmpty()) {
            products = dao.searchProductsByName(name);
        } else {
            products = dao.getAllProducts();
        }
        return products;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable int id) {
        return dao.getProduct(id);
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.dao;

import com.sg.flomast.dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Thomas
 */
public class ProductsDaoFileImpl implements ProductsDao{
    
    private static final String PRODUCTS_FILE = "Products.txt";
    private static final String DELIMITER = ",";

    @Override
    public Product getProduct(String productType) throws FloMastPersistenceException {
        List<Product> products = loadProducts();
        if (products == null) {
            return null;
        } else {
            Product productChoice = products.stream()
                    .filter(p -> p.getProductType().equalsIgnoreCase(productType))
                    .findFirst().orElse(null);
            return productChoice;
        }
    }
    
    private List<Product> loadProducts() throws FloMastPersistenceException {
        Scanner scan;
        List<Product> products = new ArrayList<>();
        
        try {
            scan = new Scanner(
                new BufferedReader(
                    new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new FloMastPersistenceException(
            "=== Could not load products data into memory.", e);
        }
        
        String currentLine;
        String[] currentTokens;
        scan.nextLine();
        while(scan.hasNextLine()) {
            currentLine = scan.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            if (currentTokens.length == 3) {
                Product currentProduct = new Product();
                currentProduct.setProductType(currentTokens[0]);
                currentProduct.setCostPerSquareFoot(new BigDecimal(currentTokens[1]));
                currentProduct.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[2]));
                products.add(currentProduct);
            } else {
                
            }
        }
        scan.close();
        
        if (!products.isEmpty()) {
            return products;
        } else {
            return null;
        }
    }
    
}

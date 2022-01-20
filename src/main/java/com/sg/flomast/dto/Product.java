/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.dto;

import java.math.BigDecimal;

/**
 *
 * @author Thomas
 */
public class Product {
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    
    public void setProductType(String productType) {
        this.productType = productType;
    }
    
    public String getProductType() {
        return productType;
    }
    
    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }
    
    public void setCostPerSquareFoot (BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }
    
    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }
    
    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }   
}

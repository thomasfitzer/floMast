/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.dao;

import com.sg.flomast.dto.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas
 */
public class FloMastDaoStubImpl implements FloMastDao {
    
    private Order order;
    private List<Order> ordersList = new ArrayList<>();
    
    public FloMastDaoStubImpl() {
        
        order = new Order();
        order.setDate(LocalDate.parse("07012023", 
                DateTimeFormatter.ofPattern("MMddyyy")));
        order.setOrderNumber(1);
        order.setCustomerName("Jones Flooring");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("5.15"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order.setMaterialCost(order.getCostPerSquareFoot().multiply(order.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot().multiply(order.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order.setTax(order.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order.getMaterialCost().add(order.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost())
                .add(order.getTax()));
        
        ordersList.add(order);
    }
    
    

    @Override
    public Order addOrder(Order order) throws FloMastPersistenceException {
        ordersList.add(order);
        return order;
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate dateChoice) throws FloMastPersistenceException {
        if (dateChoice.equals(order.getDate())) {
            return ordersList;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Order removeOrder(Order removedOrder) throws FloMastPersistenceException {
        if (removedOrder.equals(order)) {
            return order;
        } else {
            return null;
    }
    }

    @Override
    public Order editOrder(Order editedOrder) throws FloMastPersistenceException {
        if (editedOrder.getOrderNumber() == order.getOrderNumber()) {
            return order;
        } else {
            return null;
        }
    }
    
}

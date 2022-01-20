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
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;


/**
 *
 * @author Thomas
 */
public class FloMastDaoFileImplTest {
    
    private FloMastDao dao = new FloMastDaoFileImpl(dataFolder);
    
    private static String dataFolder = "src/test/resources/";
    


    @Test
    public void testAddGetOrders() throws Exception {
        LocalDate date = LocalDate.parse("01012022", 
                DateTimeFormatter.ofPattern("MMddyyyy"));
        List<Order> startOrders = dao.getOrdersByDate(date);
        
        Order order = new Order();
        order.setDate(date);
        order.setCustomerName("Snake and Sarge Flooring");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("5.15"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order.setMaterialCost(order.getCostPerSquareFoot()
                .multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot()
                .multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setTax(order.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order.getMaterialCost().add(order.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost())
                .add(order.getTax()));
        
        order = dao.addOrder(order);
        List<Order> fromDao = dao.getOrdersByDate(order.getDate());
        
        assertEquals(1, (fromDao.size() - startOrders.size()));
    }
    
    @Test
    public void testEditOrder() throws Exception {
        LocalDate date = LocalDate.parse("01012023", 
                DateTimeFormatter.ofPattern("MMddyyyy"));

        
        Order order = new Order();
        order.setDate(date);
        order.setCustomerName("Bond Flooring");
        order.setState("KY");
        order.setTaxRate(new BigDecimal("6.00"));
        order.setProductType("Laminate");
        order.setArea(new BigDecimal("150"));
        order.setCostPerSquareFoot(new BigDecimal("1.75"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order.setMaterialCost(order.getCostPerSquareFoot()
                .multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot()
                .multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setTax(order.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order.getMaterialCost().add(order.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost())
                .add(order.getTax()));
        
        order = dao.addOrder(order); 
        
        Order editedOrder = order;
        editedOrder.setCustomerName("Johnson");
        editedOrder = dao.editOrder(editedOrder);
        
        List<Order> orders = dao.getOrdersByDate(date);
        int orderNumber = editedOrder.getOrderNumber();
        
        Order orderChoice = orders.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);
        
        assertEquals(orderChoice.getCustomerName(), editedOrder.getCustomerName());

    }
    
    @Test
    public void testFailEditOrder() throws Exception {
        LocalDate date = LocalDate.parse("01012024", 
                DateTimeFormatter.ofPattern("MMddyyyy"));

        
        Order order = new Order();
        order.setDate(date);
        order.setCustomerName("Smith");
        order.setState("WA");
        order.setTaxRate(new BigDecimal("9.25"));
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("200"));
        order.setCostPerSquareFoot(new BigDecimal("2.25"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order.setMaterialCost(order.getCostPerSquareFoot()
                .multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot()
                .multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setTax(order.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order.getMaterialCost().add(order.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost())
                .add(order.getTax()));
        
        order = dao.addOrder(order);
        
        Order editedOrder = order;
        
        editedOrder.setOrderNumber(0);
        editedOrder.setCustomerName("Invalid");
        
        editedOrder = dao.editOrder(editedOrder);
        
        assertNull(editedOrder);
    } 
    
    @Test
    public void testRemoveOrder() throws Exception {

        LocalDate date = LocalDate.parse("01012025",
                DateTimeFormatter.ofPattern("MMddyyyy"));

        Order order1 = new Order();
        order1.setDate(date);
        order1.setCustomerName("Billiams");
        order1.setState("CA");
        order1.setTaxRate(new BigDecimal("25.00"));
        order1.setProductType("Wood");
        order1.setArea(new BigDecimal("100"));
        order1.setCostPerSquareFoot(new BigDecimal("5.15"));
        order1.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order1.setMaterialCost(order1.getCostPerSquareFoot()
                .multiply(order1.getArea()).setScale(2, RoundingMode.HALF_UP));
        order1.setLaborCost(order1.getLaborCostPerSquareFoot().multiply(order1.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order1.setTax(order1.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order1.getMaterialCost().add(order1.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order1.setTotal(order1.getMaterialCost().add(order1.getLaborCost())
                .add(order1.getTax()));

        dao.addOrder(order1);

        Order order2 = new Order();
        order2.setDate(date);
        order2.setCustomerName("Archie");
        order2.setState("KY");
        order2.setTaxRate(new BigDecimal("6.00"));
        order2.setProductType("Laminate");
        order2.setArea(new BigDecimal("100"));
        order2.setCostPerSquareFoot(new BigDecimal("1.75"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order2.setMaterialCost(order2.getCostPerSquareFoot()
                .multiply(order2.getArea()).setScale(2, RoundingMode.HALF_UP));
        order2.setLaborCost(order2.getLaborCostPerSquareFoot().multiply(order2.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order2.setTax(order2.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order2.getMaterialCost().add(order2.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order2.setTotal(order2.getMaterialCost().add(order2.getLaborCost())
                .add(order2.getTax()));

        order2 = dao.addOrder(order2);

        List<Order> initialOrders = dao.getOrdersByDate(date);

        dao.removeOrder(order2);

        List<Order> fromDao = dao.getOrdersByDate(date);

        //Testing to see if a row was removed from the test file.
        assertEquals(-1, (fromDao.size() - initialOrders.size()));

    }    
}

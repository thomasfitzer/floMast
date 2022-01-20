/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.service;

import com.sg.flomast.dao.AuditDao;
import com.sg.flomast.dao.AuditDaoStubImpl;
import com.sg.flomast.dao.FloMastDao;
import com.sg.flomast.dao.FloMastDaoStubImpl;
import com.sg.flomast.dao.ProductsDao;
import com.sg.flomast.dao.ProductsDaoFileImpl;
import com.sg.flomast.dao.StatesDao;
import com.sg.flomast.dao.StatesDaoFileImpl;
import com.sg.flomast.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas
 */
public class FloMastServiceLayerImplTest {

    private FloMastServiceLayer service;
    
    public FloMastServiceLayerImplTest() {
        FloMastDao dao = new FloMastDaoStubImpl();
        ProductsDao productsDao = new ProductsDaoFileImpl();
        StatesDao statesDao = new StatesDaoFileImpl();
        AuditDao auditDao = new AuditDaoStubImpl();
        
        service = new FloMastServiceLayerImpl(dao, productsDao, statesDao, auditDao);
    }

    @Test
    public void testGetOrderByDate() throws Exception{
        assertEquals(1, service.getOrdersByDate(LocalDate.of(2023, 07, 01)).size());
        
        try {
            List<Order> orders = service.getOrdersByDate(LocalDate.of(2021, 01, 10));
            fail("Expected InValidOrderException was not thrown.");
        } catch (InvalidOrderNumberException e) {
            
        }
    }
    
    @Test
    public void testGetOrder() throws Exception {
       Order order = service.getSingleOrder(LocalDate.of(2023, 07, 01), 1);
       assertNotNull(order);
       
       try {
           order = service.getSingleOrder(LocalDate.of(2023, 07, 01), 0);
           fail("ExpectedInvalidOrderNumberException was not thrown.");
       } catch(InvalidOrderNumberException e){
           
       }
    }
    
    @Test
    public void testCalcOrder() throws Exception {
        Order calcOrder = service.getSingleOrder(LocalDate.of(2023, 07, 01), 1);
        
        assertEquals(calcOrder.getMaterialCost(), new BigDecimal("515.00"));
        assertEquals(calcOrder.getLaborCost(), new BigDecimal("475.00"));
        assertEquals(calcOrder.getTax(), new BigDecimal("247.50"));
        assertEquals(calcOrder.getTotal(), new BigDecimal("1237.50"));
           
    }
    
    @Test
    public void testAddOrder() throws Exception {
        Order order = new Order();
        order.setCustomerName("Smith");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100"));
        service.addOrder(order);
        assertEquals(order, service.addOrder(order));
    }
    
    @Test
    public void testCompareOrders() throws Exception {
        Order savedOrder = service.getSingleOrder(LocalDate.of(2023, 07, 01), 1);
        
        Order editedOrder = new Order();
        editedOrder.setCustomerName("Johnson");
        
        Order updatedOrder = service.compareOrders(savedOrder, editedOrder);
        
        assertEquals(updatedOrder, savedOrder);
    }
    
    @Test
    public void testEditOrder() throws Exception {
        Order ordertoEdit = service.getSingleOrder(LocalDate.of(2023, 07, 01), 1);
        assertNotNull(ordertoEdit);
        
        
    }
    
    @Test
    public void testRemoveOrder() throws Exception {
        List<Order> orders = service.getOrdersByDate(LocalDate.of(2023, 07, 01));
        Order orderToRemove = service.getSingleOrder(LocalDate.of(2023, 07, 01), 1);
        assertNotNull(orderToRemove);
        
        service.removeOrder(orderToRemove);
        assertEquals(orderToRemove, service.removeOrder(orderToRemove));
    }
    
}

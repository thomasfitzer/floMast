/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.service;

import com.sg.flomast.dao.FloMastPersistenceException;
import com.sg.flomast.dto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Thomas
 */
public interface FloMastServiceLayer {
    
    Order addOrder(Order order) throws FloMastPersistenceException;
    
    List<Order> getOrdersByDate(LocalDate dateChoice) throws InvalidOrderNumberException, FloMastPersistenceException;
    
    Order calculateOrder(Order order) throws FloMastPersistenceException, OrderValidationException,
            StateValidationException, ProductValidationException;
    
    Order getSingleOrder(LocalDate dateChoice, int orderNumber) throws FloMastPersistenceException,
            InvalidOrderNumberException;
    
    Order compareOrders(Order savedOrder, Order editedOrder) throws FloMastPersistenceException,
            StateValidationException, ProductValidationException;
    
    Order editOrder(Order revisedOrder) throws FloMastPersistenceException, InvalidOrderNumberException;
    
    Order removeOrder(Order removedOrder) throws FloMastPersistenceException, 
            InvalidOrderNumberException;
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.dao;

import com.sg.flomast.dto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Thomas
 */
public interface FloMastDao {
    
    Order addOrder (Order order) throws FloMastPersistenceException;
    
    List<Order> getOrdersByDate(LocalDate dateChoice) throws FloMastPersistenceException;
    
    Order removeOrder(Order removedOrder) throws FloMastPersistenceException;
    
    Order editOrder(Order editedOrder) throws FloMastPersistenceException;
    
 
    
    //ADD "EXPORT ALL DATA" LATER
}

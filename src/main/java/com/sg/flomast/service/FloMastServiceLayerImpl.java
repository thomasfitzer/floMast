/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.service;

import com.sg.flomast.dao.AuditDao;
import com.sg.flomast.dao.FloMastDao;
import com.sg.flomast.dao.FloMastPersistenceException;
import com.sg.flomast.dao.ProductsDao;
import com.sg.flomast.dao.StatesDao;
import com.sg.flomast.dto.Order;
import com.sg.flomast.dto.Product;
import com.sg.flomast.dto.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Thomas
 */
public class FloMastServiceLayerImpl implements FloMastServiceLayer {
    
    private FloMastDao dao;
    private ProductsDao productsDao;
    private StatesDao statesDao;
    private AuditDao auditDao;
    
    public FloMastServiceLayerImpl(FloMastDao dao, ProductsDao productsDao, StatesDao statesDao,
            AuditDao auditDao) {
        this.dao = dao;
        this.productsDao = productsDao;
        this.statesDao = statesDao;
        this.auditDao = auditDao;        
    }

    @Override
    public Order addOrder(Order order) throws FloMastPersistenceException {
        dao.addOrder(order);
        auditDao.writeAuditEntry("Order #" + order.getOrderNumber() + 
                " for date " + order.getDate() + " ADDED.");
        return order;
        
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate dateChoice) throws InvalidOrderNumberException, FloMastPersistenceException {
        List<Order> ordersByDate = dao.getOrdersByDate(dateChoice);
        if (!ordersByDate.isEmpty()) {
            return ordersByDate;
        } else {
            throw new InvalidOrderNumberException("ERROR: No orders "
                    + "exist on that date.");
        }
    }
    
    @Override
    public Order getSingleOrder(LocalDate dateChoice, int orderNumber) throws FloMastPersistenceException, 
            InvalidOrderNumberException {
        List<Order> orders = getOrdersByDate(dateChoice);
        Order chosenOrder = orders.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);
        if (chosenOrder != null) {
            return chosenOrder;
        } else {
            throw new InvalidOrderNumberException("ERROR: No orders with that number "
                + "exist on that date.");
        }
    }

    @Override
    public Order calculateOrder(Order order) throws FloMastPersistenceException,
            OrderValidationException, StateValidationException, ProductValidationException{
        validateOrder(order);
        calcTax(order);
        calcMaterial(order);
        calcTotal(order);
        
        return order;
        
        
    }
    
    private void calcTax(Order order) throws FloMastPersistenceException, 
            StateValidationException {
        State stateChoice = statesDao.getState(order.getState());
        if(stateChoice == null) {
            throw new StateValidationException("ERROR: FloMast does not serve that state.");
        }
        order.setState(stateChoice.getState());
        order.setTaxRate(stateChoice.getTaxRate());
    }
    
    private void calcMaterial(Order order) throws FloMastPersistenceException, 
            ProductValidationException {
        Product productChoice = productsDao.getProduct(order.getProductType());
        if(productChoice == null) {
            throw new ProductValidationException("ERROR: We do not sell that product.");
        }
        
        order.setProductType(productChoice.getProductType());
        order.setCostPerSquareFoot(productChoice.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(productChoice.getLaborCostPerSquareFoot());
        
        
    }
    
    private void calcTotal (Order order) {
        order.setMaterialCost(order.getCostPerSquareFoot().multiply(order.getArea())
        .setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot().multiply(order.getArea())
        .setScale(2, RoundingMode.HALF_UP));
        order.setTax((order.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply(order.getMaterialCost().add(order.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()));
    }



    @Override
    public Order compareOrders(Order savedOrder, Order editedOrder) throws FloMastPersistenceException,
            StateValidationException, ProductValidationException {
        if(editedOrder.getCustomerName() == null
                || editedOrder.getCustomerName().trim().equals("")) {
            
        } else {
            savedOrder.setCustomerName(editedOrder.getCustomerName());
        }
        
        if (editedOrder.getState() == null
                || editedOrder.getState().trim().equals("")) {
            
        } else {
            savedOrder.setState(editedOrder.getState());
            calcTax(savedOrder);
        }
        
        if (editedOrder.getProductType() == null
                || editedOrder.getProductType().equals("")) {
            
        } else {
            savedOrder.setProductType(editedOrder.getProductType());
            calcMaterial(savedOrder);
        }
        
        if (editedOrder.getArea() == null
                || (editedOrder.getArea().compareTo(BigDecimal.ZERO)) == 0) {
            
        } else {
            savedOrder.setArea(editedOrder.getArea());
        }
        
        calcTotal(savedOrder);
        
        return savedOrder;
    }

    @Override
    public Order editOrder(Order revisedOrder) throws FloMastPersistenceException, 
            InvalidOrderNumberException {
        revisedOrder = dao.editOrder(revisedOrder);
        if (revisedOrder != null) {
            auditDao.writeAuditEntry("Order #" 
                    + revisedOrder.getOrderNumber() + " for date " 
                    + revisedOrder.getDate() + " EDITED.");
            return revisedOrder;
        } else {
            throw new InvalidOrderNumberException("ERROR: No orders with that number"
                    + " exist on that date.");
        }
    }

    @Override
    public Order removeOrder(Order removedOrder) throws FloMastPersistenceException, InvalidOrderNumberException {
        removedOrder = dao.removeOrder(removedOrder);
        if (removedOrder != null) {
            auditDao.writeAuditEntry("Order #"
                    + removedOrder.getOrderNumber() + " for date "
                    + removedOrder.getDate() + " REMOVED.");
            return removedOrder;
        } else {
            throw new InvalidOrderNumberException("ERROR: No orders with that "
                        + "exist on that date.");
        }
    }
    
    private void validateOrder(Order order) throws OrderValidationException {
        String message = "";
        
        if(order.getCustomerName().trim().isEmpty() || order.getCustomerName() == null) {
            message += "Please enter the customer's name.\n";
        }
        if(order.getState().trim().isEmpty() || order.getState() == null) {
            message += "Please enter the state.\n";
        }
        if(order.getProductType().trim().isEmpty() || order.getProductType() == null) {
            message += "Please enter the product type.\n";
        }
        if(order.getArea().compareTo(BigDecimal.ZERO) == 0 || order.getArea() == null) {
            message += "Please enter the area of the order.\n";
        }
        if(!message.isEmpty()) {
            throw new OrderValidationException(message);
        }
        
        
    }
    
}

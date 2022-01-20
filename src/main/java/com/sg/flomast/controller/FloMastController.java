/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.controller;

import com.sg.flomast.dao.FloMastPersistenceException;
import com.sg.flomast.dto.Order;
import com.sg.flomast.service.FloMastServiceLayer;
import com.sg.flomast.service.InvalidOrderNumberException;
import com.sg.flomast.service.OrderValidationException;
import com.sg.flomast.service.ProductValidationException;
import com.sg.flomast.service.StateValidationException;
import com.sg.flomast.ui.FloMastView;
import java.time.LocalDate;

/**
 *
 * @author Thomas
 */
public class FloMastController {
    FloMastServiceLayer service;
    FloMastView view;
    
    public FloMastController(FloMastServiceLayer service, FloMastView view) {
        this.service = service;
        this.view = view;
    }
    
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try{
        while (keepGoing) {
            
            menuSelection = getMenuSelection();
            

            
            
            switch (menuSelection) {
                case 1:
                    getOrdersByDate();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;

                case 5:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        exitMessage();
        } catch( FloMastPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
     }
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    
    private void addOrder() throws FloMastPersistenceException {
        try {
            Order order = service.calculateOrder(view.getNewOrderInfo());
            view.displayOrder(order);
            String response = view.saveAsk();
            if (response.equalsIgnoreCase("Y")) {
                service.addOrder(order);
                view.displayAddSuccessBanner(true, order);
            } else if (response.equalsIgnoreCase("N")) {
                view.displayAddSuccessBanner(false, order);
            } else {
                unknownCommand();
            }
        } catch (OrderValidationException | StateValidationException | ProductValidationException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    public void getOrdersByDate() throws FloMastPersistenceException{
        LocalDate dateChoice = view.inputDate();
        view.displayDateBanner(dateChoice);
        try {
            view.displayOrdersByDate(service.getOrdersByDate(dateChoice));
            view.showContinue();
        } catch (InvalidOrderNumberException e){
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    public void removeOrder() throws FloMastPersistenceException{
        view.displayRemoveOrderBanner();
        LocalDate dateChoice = view.inputDate();
        view.displayDateBanner(dateChoice);
        try {
            view.displayOrdersByDate(service.getOrdersByDate(dateChoice));
            int orderNumber = view.inputOrderNumber();
            Order order = service.getSingleOrder(dateChoice, orderNumber);
            view.displayRemoveOrderBanner();
            view.displayOrder(order);
            String response = view.removeAsk();
            if (response.equalsIgnoreCase("Y")) {
                service.removeOrder(order);
                view.displayRemoveSuccessBanner(true, order);
            } else if (response.equalsIgnoreCase("N")) {
                view.displayRemoveSuccessBanner(false, order);
            } else {
                unknownCommand();
            }
        } catch (InvalidOrderNumberException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    private void editOrder() throws FloMastPersistenceException {
        view.showEditBanner();
        try {
            LocalDate dateChoice = view.inputDate();
            int orderNumber = view.inputOrderNumber();
            Order savedOrder = service.getSingleOrder(dateChoice, orderNumber);
            Order editedOrder = view.editOrder(savedOrder);
            Order updatedOrder = service.compareOrders(savedOrder, editedOrder);
            view.showEditBanner();
            view.displayOrder(updatedOrder);
            String response = view.saveAsk();
            if (response.equalsIgnoreCase("Y")) {
                service.editOrder(updatedOrder);
                view.showEditSuccess(true, updatedOrder);
            } else if (response.equalsIgnoreCase("N")) {
                view.showEditSuccess(false, updatedOrder);
                
            } else {
                unknownCommand();
            }
        } catch (InvalidOrderNumberException |
                ProductValidationException | StateValidationException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }
    
    private void exitMessage() {
        view.displayExitBanner();
    }
}

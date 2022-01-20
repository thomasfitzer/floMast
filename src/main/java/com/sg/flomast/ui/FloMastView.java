/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.ui;

import com.sg.flomast.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Thomas
 */
public class FloMastView {
    private UserIO io;
    
    public FloMastView(UserIO io) {
        this.io = io;
    }
    
    public int printMenuAndGetSelection() {
            io.print("Flooring Program");
            io.print("1. Display Orders");
            io.print("2. Add an Order");
            io.print(("3. Edit an Order"));
            io.print("4. Remove an Order");
            io.print("5. Quit");
            
            return io.readInt("Please select from the above choices.", 1, 5);
    }
    
    //Alter prompts to show available options
    
    // ADD ORDER
    public LocalDate inputDate() {
        return io.readDate("Please enter the date for the order. (MM-DD-YYYY)",
                LocalDate.of(2000, 1, 1), LocalDate.of(2050, 12, 31));
    }
    
    public String inputName() {
        return io.readString("Please enter the name for the order.");
    }
    
    public String inputStateAbbr() {
        return io.readString("Please enter the USPS abbreviation for your state."
                + " (Note: We only serve California (CA), Kentucky (KY), Texas (TX), and "
                + "Washington (WA)", 2);
    }
    
    public String inputProduct() {
        return io.readString("Please enter the product to be ordered. (Note: "
                + "The list of available products is carpet, laminate, tile, and wood.)", 15);
    }
    
    public BigDecimal inputArea() {
        return io.readBigDecimal("Please enter the square footage of the order. "
                + "(Min. 100 square feet)", 2, BigDecimal.ZERO);
    }
    
    public Order getNewOrderInfo() {
        
        
        Order currentOrder = new Order();
        currentOrder.setDate(inputDate());
        currentOrder.setCustomerName(inputName());
        currentOrder.setState(inputStateAbbr());
        currentOrder.setProductType(inputProduct());
        currentOrder.setArea(inputArea());
        
        return currentOrder;
        
    }
    
    public void showDisplayOrdersBanner() {
        io.print("=== DISPLAY ORDERS ===");
    }
    
    public void displayOrder(Order order) {
        io.print("Date: " + order.getDate());
        io.print("Customer: " + order.getCustomerName());
        io.print("State: " + order.getState());
        io.print("Tax Rate: " + order.getTaxRate() + "%");
        io.print("Product: " + order.getProductType());
        io.print("Material Cost Per SqFt: " + io.formatMoney(order.getCostPerSquareFoot()));
        io.print("Labor Cost Per SqFt: " + io.formatMoney(order.getLaborCostPerSquareFoot()));
        io.print("Area: " + order.getArea());
        io.print("Material Cost: " + io.formatMoney(order.getMaterialCost()));
        io.print("Labor Cost: " + io.formatMoney(order.getLaborCost()));
        io.print("Tax: " + io.formatMoney(order.getTax()));
        io.print("=== TOTAL: " + io.formatMoney(order.getTotal()) + " ===");
    }
    
    public String saveAsk() {
        return io.readString("Would you like to save this order? Y/N", 1);
    }
    
    public int getOrderIdChoice() {
        return io.readInt("Please enter tan order number");
    }
    
    public void displayAddBanner() {
        io.print("===== Add Order =====");
    }
    
    public void displayAddSuccessBanner(boolean success, Order order) {
        if (success == true) {
            io.print("Order #" + order.getOrderNumber() + " was successfully added.");
        } else {
            io.print("Order was not saved.");
            showContinue();
        }
    }
    
    public void showOrderList(List<Order> orderList) {
        for (Order currentOrder : orderList) {
            io.print(currentOrder.getOrderNumber()+ ": "
                    + currentOrder.getDate() + " | "
                    + currentOrder.getCustomerName() + " | "
                    + currentOrder.getState() + " | "
                    + currentOrder.getProductType() + " | "
                    + currentOrder.getArea());
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayDateBanner(LocalDate dateChoice) {
        System.out.printf("=== Orders on %s ===\n", dateChoice);
    }
    
    public void displayOrdersByDate(List<Order> ordersByDate) {
        for (Order o : ordersByDate) {
            io.print(o.getOrderNumber() + " | " + o.getCustomerName() + " | "
                    + io.formatMoney(o.getTotal()));
        } 
    }
    
    //REMOVE ORDER
    public void displayRemoveOrderBanner() {
        io.print("===== Remove Order =====");
    }
    
    public String removeAsk() {
        return io.readString("Would you like to remove this order?", 1);
    }
    
    public void displayRemoveSuccessBanner(boolean success, Order order) {
        if (success == true) {
            io.print("Order #" + order.getOrderNumber() + " was successfully removed.");
        } else {
            io.print("Order was not removed");
            showContinue();
        }
    }
    
    public void showEditBanner() {
        io.print("=== Edit Order ===");
    }
    
    public int inputOrderNumber() {
        return io.readInt("Please enter an order number.");
    }
    
    public Order editOrder(Order order) {
        Order editedOrder = new Order();
        io.print("Customer: " + order.getCustomerName());
        editedOrder.setCustomerName(inputName());
        
        io.print("State: " + order.getState());
        editedOrder.setState(inputStateAbbr());
        
        io.print("Product: " + order.getProductType());
        editedOrder.setProductType(inputProduct());
        
        io.print("Area: " + order.getArea() + " SqFt");
        editedOrder.setArea(inputArea());
        
        return editedOrder;
    }
    
    public void showEditSuccess(boolean success, Order order) {
        if (success == true) {
            io.print("Order #" + order.getOrderNumber() + " was successfully edited.");
        } else {
            io.print("Order was not edited.");
            showContinue();
        }
    }
    
    
    public void displayShowAllBanner() {
        io.print("===== Display All Orders ====");
    }
    
    public void displayExitBanner() {
        io.print("Goodbye!");
    }
    
    public void displayUnknownCommandBanner() {
        io.print("UNKNOWN COMMAND");
    }
    
    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }
    
    public void showContinue() {
        io.readString("Please press enter to continue.");
    }
}

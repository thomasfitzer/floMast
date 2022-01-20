/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.dao;

import com.sg.flomast.dto.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Thomas
 */
public class FloMastDaoFileImpl implements FloMastDao {
    
    private int prevOrderNumber;
    private static final String HEADER = "OrderNumber,CustomerName,State,TaxRate,"
            + "ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,"
            + "MatericalCost,LaborCost,Tax,Total";
    private String dataFolder = "orders/";
    public static final String DELIMITER = ",";
    
    public FloMastDaoFileImpl(){
        
    }
    
    public FloMastDaoFileImpl(String dataFolder){
        this.dataFolder = dataFolder;
    }

    @Override
    public Order addOrder(Order order) throws FloMastPersistenceException {
        order = wipeFields(order);
        readPrevOrderNumber();
        prevOrderNumber++;
        order.setOrderNumber(prevOrderNumber);
        writePrevOrderNumber(prevOrderNumber);
        
        List<Order> orders = loadOrders(order.getDate());
        orders.add(order);
        writeOrders(orders, order.getDate());
        
        return order;
        
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate dateChoice) throws FloMastPersistenceException {
        return loadOrders(dateChoice);
    }

    @Override
    public Order removeOrder(Order removedOrder) throws FloMastPersistenceException {
        int orderNumber = removedOrder.getOrderNumber();
        
        List<Order> orders = loadOrders(removedOrder.getDate());
        Order chosenOrder = orders.stream()
                .filter(order -> order.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);
        
        if (chosenOrder != null) {
            orders.remove(chosenOrder);
            writeOrders(orders, removedOrder.getDate());
            return chosenOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order editOrder(Order editedOrder) throws FloMastPersistenceException{
        editedOrder = wipeFields(editedOrder);
        int orderNumber = editedOrder.getOrderNumber();
        
        List<Order> orders = loadOrders(editedOrder.getDate());
        Order orderChoice = orders.stream()
                .filter(order -> order.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);
        if (orderChoice != null) {
            int index = orders.indexOf(orderChoice);
            orders.set(index, editedOrder);
            writeOrders(orders, editedOrder.getDate());
            return editedOrder;
        } else {
            return null;
        }
    }


    private void readPrevOrderNumber() throws FloMastPersistenceException {
        Scanner scan;
        
        try {
            scan = new Scanner(
                new BufferedReader(
                    new FileReader(dataFolder + "PrevOrderNumber.txt")));
        } catch (FileNotFoundException e) {
            throw new FloMastPersistenceException(
                "=== Could not load order number into memory.", e);
        }
        
        int savedOrderNumber = Integer.parseInt(scan.nextLine());
        
        this.prevOrderNumber = savedOrderNumber;
        scan.close();
    }
    
    private void writePrevOrderNumber(int prevOrderNumber) throws FloMastPersistenceException{
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(dataFolder + "PrevOrderNumber.txt"));
        } catch (IOException e) {
            throw new FloMastPersistenceException(
            "=== Could not save order number.", e);
        }
        
        out.println(prevOrderNumber);
        
        out.flush();
        
        out.close();
    }
    
    private List<Order> loadOrders(LocalDate dateChoice) throws FloMastPersistenceException {
        Scanner scan;
        
        String fileDate = dateChoice.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        
        File f = new File(String.format(dataFolder + "Orders_%s.txt", fileDate));
        
        List<Order> orders = new ArrayList();
        
        if (f.isFile()) {
            try {
                scan = new Scanner(
                    new BufferedReader(
                        new FileReader(f)));
            } catch (FileNotFoundException e) {
                throw new FloMastPersistenceException(
                "=== Could not load order data into memory.", e);
            }
            String currentLine;
            String[] currentTokens;
            scan.nextLine();
            while(scan.hasNextLine()) {
                currentLine = scan.nextLine();
                currentTokens = currentLine.split(DELIMITER);
                if (currentTokens.length == 12) {
                    Order currentOrder = new Order();
                    currentOrder.setDate(LocalDate.parse(fileDate, 
                            DateTimeFormatter.ofPattern("MMddyyyy")));
                    currentOrder.setOrderNumber(Integer.parseInt(currentTokens[0]));
                    currentOrder.setCustomerName(currentTokens[1]);
                    currentOrder.setState(currentTokens[2]);
                    currentOrder.setTaxRate(new BigDecimal(currentTokens[3]));
                    currentOrder.setProductType(currentTokens[4]);
                    currentOrder.setArea(new BigDecimal(currentTokens[5]));
                    currentOrder.setCostPerSquareFoot(new BigDecimal(currentTokens[6]));
                    currentOrder.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[7]));
                    currentOrder.setMaterialCost(new BigDecimal(currentTokens[8]));
                    currentOrder.setLaborCost(new BigDecimal(currentTokens[9]));
                    currentOrder.setTax(new BigDecimal(currentTokens[10]));
                    currentOrder.setTotal(new BigDecimal(currentTokens[11]));
                    orders.add(currentOrder);
                } else {
                
            }
            }
            scan.close();
            return orders;
        } else {
            return orders;
        }
        
    }
    
    private void writeOrders(List<Order> orders, LocalDate orderDate) throws FloMastPersistenceException {
        PrintWriter out;
        
        String fileDate = orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        
        File f = new File(String.format(dataFolder + "Orders_%s.txt", fileDate));
        
        try {
            out = new PrintWriter(new FileWriter(f));
        } catch (IOException e) {
            throw new FloMastPersistenceException(
                "=== Could not save order data.", e);
        }
        
        out.println(HEADER);
        for (Order currentOrder : orders) {
            out.println(currentOrder.getOrderNumber() + DELIMITER
                        + currentOrder.getCustomerName() + DELIMITER
                        + currentOrder.getState() + DELIMITER
                        + currentOrder.getTaxRate() + DELIMITER
                        + currentOrder.getProductType() + DELIMITER
                        + currentOrder.getArea() + DELIMITER
                        + currentOrder.getCostPerSquareFoot() + DELIMITER
                        + currentOrder.getLaborCostPerSquareFoot() + DELIMITER
                        + currentOrder.getMaterialCost() + DELIMITER
                        + currentOrder.getLaborCost() + DELIMITER
                        + currentOrder.getTax() + DELIMITER
                        + currentOrder.getTotal() + DELIMITER);
            
            out.flush();
        }
        
        out.close();
    }
    
    private Order wipeFields(Order order) {
        String wipeCustomerName = order.getCustomerName().replace(DELIMITER, "");
        String wipeState = order.getState().replace(DELIMITER, "");
        String wipeProd = order.getProductType().replace(DELIMITER, "");
        
        order.setCustomerName(wipeCustomerName);
        order.setState(wipeState);
        order.setProductType(wipeProd);
        
        return order;
    }
}

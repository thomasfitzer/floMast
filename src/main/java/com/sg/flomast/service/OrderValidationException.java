/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.service;

/**
 *
 * @author Thomas
 */
public class OrderValidationException extends Exception{

    OrderValidationException(String message) {
        super(message);
    }
    
    OrderValidationException(String message, Throwable cause) {
        super(message, cause);
    }    
}

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
public class StateValidationException extends Exception{

    StateValidationException(String message) {
        super(message);
    }
    
    StateValidationException(String message, Throwable cause) {
        super(message, cause);
    }     
}

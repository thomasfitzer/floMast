/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Thomas
 */
public interface UserIO {
    void print(String msg);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);
    
    String formatMoney(BigDecimal amount);

    BigDecimal readBigDecimal(String prompt, int scale);
    
    BigDecimal readBigDecimal(String prompt, int scale, BigDecimal min);

    String readString(String prompt);
    
    String readString(String prompt, int max);

    LocalDate readDate(String prompt);  
    
    LocalDate readDate(String prompt, LocalDate min, LocalDate max);
}

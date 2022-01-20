/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.dao;

import com.sg.flomast.dto.State;

/**
 *
 * @author Thomas
 */
public interface StatesDao {
    
    State getState(String state) throws FloMastPersistenceException;
}

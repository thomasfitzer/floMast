/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast.dao;

import com.sg.flomast.dto.State;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Thomas
 */
public class StatesDaoFileImpl implements StatesDao {
    
    private static final String STATES_FILE = "Taxes.txt";
    private static final String DELIMITER = ",";

    @Override
    public State getState(String state) throws FloMastPersistenceException {
        List<State> states = loadStates();
        if (states == null) {
            return null;
        } else {
        State stateChoice = states.stream()
                .filter(s -> s.getState().equalsIgnoreCase(state))
                .findFirst().orElse(null);
        return stateChoice;
    }
    }
    
    private List<State> loadStates() throws FloMastPersistenceException {
        Scanner scan;
        List<State> states = new ArrayList<>();
        
        try {
            scan = new Scanner(
                new BufferedReader(
                    new FileReader(STATES_FILE)));
        } catch (FileNotFoundException e) {
            throw new FloMastPersistenceException(
                "=== Could not load states data into memory.", e);
            }
        String currentLine;
        String[] currentTokens;
        scan.nextLine();
        while(scan.hasNextLine()) {
            currentLine = scan.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            if (currentTokens.length == 2) {
                State currentState = new State();
                currentState.setState(currentTokens[0]);
                currentState.setTaxRate(new BigDecimal(currentTokens[1]));
                
                states.add(currentState);
                
            } else {
                
            }
        }
        scan.close();
        if (!states.isEmpty()) {
            return states;
        } else {
            return null;
        }
    }
    
}

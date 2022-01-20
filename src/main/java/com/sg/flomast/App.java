/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flomast;

import com.sg.flomast.controller.FloMastController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Thomas
 */
public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext("applicationContext.xml");
        
        FloMastController controller = ctx.getBean("controller", FloMastController.class);
        controller.run();
    }
}

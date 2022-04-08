/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;
import java.util.*;
import sharedRegions.*;

/**
 *
 * @author miguel
 */
public class Chef extends Thread{
    private States chefState;
    
    private Kitchen kitchen;
    
    private Repository repository;
    /**
   *   Instantiation of a Chef thread.
   *
   *     @param name thread name
   *     @param kit reference to the chef Kitchen
   *     @param repo reference to the general repository
   */
    
    public Chef(String name, Kitchen kitchen, Repository repository){
        super(name);
        chefState = States.WAIT_FOR_AN_ORDER;
        this.kitchen = kitchen;
        this.repository = repository;
    }
}

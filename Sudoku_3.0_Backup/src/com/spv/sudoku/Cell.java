/*
 * Cell.java
 *
 * Created on February 17, 2009, 9:55 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.spv.sudoku;

import java.util.*;

/**
 *
 * @author Administrator
 */
public class Cell {
    ArrayList<Integer> values;
    Integer value;
    int size;
            
    /** Creates a new instance of Cell */
    /*public Cell() {
        values = new ArrayList<Integer>();
        size = 9;
        value = 0;
        for(int i=1; i <= size; i++) {
            values.add(i);
        }
    }*/
    
    public Cell(int size) {
        this.size = size;
        values = new ArrayList<Integer>();
        value = 0;
        for(int i=1; i <= size; i++) {
            values.add(i);
        }
    }
    
    public void setValue(int value) {
        this.value = value;
        //values.clear();
        this.invalidate(values);
    }
    
    public Integer getValue() {
        return value;
    }
    
    public boolean clearValue() {
        if(this.isEmpty())
            return false;
        this.value = 0;
        return true;
    }
    
    public boolean invalidate(Integer value) {
        return values.remove(value);
    }

    public boolean invalidate(ArrayList<Integer> valueList) {
        return values.removeAll(valueList);
    }
    
    public void addValidValue(Integer value) {
        int i;
        for(i=0; i < values.size(); i++) {
            if(values.get(i) > value)
                break;
        }
        //if(i < values.size())
            values.add(i, value);
            
        /*else
            values.add(value);*/
        /*if(values.size() == 0 || values.get(0) < value)
            values.add(value);
        else
            values.add(0, value);*/
    }
    
    public int getNumberOfValuesExists() {
        return values.size();
    }
    
    public boolean isValidValue(Integer value) {
        return values.contains(value);
    }
    
    public boolean fixValidValue() {
        if(values.size() == 1) {
            setValue(values.get(0));
            return true;
        }
        return false;
    }
    
    public boolean isEmpty() {
        return value == 0;
    }
    
    public ArrayList<Integer> getArrayList() {
        return values;
    }
    
    public Integer getValidValue() {
        if(values.size() == 1)
            return values.get(0);
        else
            return 0;
    }
    
    public Integer nextValidValueFromList(Integer lastAssume) {
        for(Integer element: values) {
            if(element > lastAssume)
                return element;
        }
        return 0;
    }
    
    public Integer getRandomValidValueFromList() {
        if(values.size() == 0)
            return 0;
        return values.get(new Random().nextInt(values.size()));
    }
}

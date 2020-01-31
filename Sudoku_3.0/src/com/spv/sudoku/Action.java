/*
 * Action.java
 *
 * Created on March 7, 2009, 4:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.spv.sudoku;

/**
 *
 * @author white
 */
public class Action {
    public static final int FIX_VALUE = 1;
    public static final int REMOVE_VALID_VALUE = 2;
    public static final int ASSUME_VALID_VALUE = 3;
    public static final int REMOVE_CELL_VALUE = 4;
    
    int row;
    int column;
    Integer value;
    int command;
   
    /** Creates a new instance of Action */
    public Action(int command, int row, int column, Integer value) {
        this.command = command;
        this.row = row;
        this.column = column;
        this.value = value;
    }
    
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public int getCommand() {
        return command;
    }
}

/*
 * ActionLog.java
 *
 * Created on March 7, 2009, 5:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.spv.sudoku;

import java.util.*;

/**
 *
 * @author white
 */
public class ActionLog {
    Stack actionLog;
    
    /** Creates a new instance of ActionLog */
    public ActionLog() {
    }
    
    public void add(int command, int row, int column, Integer value) {
        actionLog.push(new Action(command, row, column, value));
    }

    public void add(int command, int row, int column, ArrayList<Integer> values) {
        for(Integer value: values) {
            actionLog.push(new Action(command, row, column, value));
        }
    }
}

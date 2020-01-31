/*
 * ActionStack.java
 *
 * Created on March 7, 2009, 5:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.spv.sudoku;

import java.util.*;

public class ActionStack {
    Stack<Action> actionStack;
    int assumeCount;
    
    /** Creates a new instance of ActionStack */
    public ActionStack() {
        actionStack = new Stack<Action>();
        assumeCount = 0;
    }

    public void push(int command, int row, int column, Integer value) {
        actionStack.push(new Action(command, row, column, value));
        if(command == Action.ASSUME_VALID_VALUE)
            ++assumeCount;
    }

    public void push(int command, int row, int column, ArrayList<Integer> values) {
        for(Integer value: values) {
            this.push(command, row, column, value);
        }
    }
    
    public Action pop() {
        if(actionStack.empty())
            return null;
        Action action = actionStack.pop();
        if(action.getCommand() == Action.ASSUME_VALID_VALUE)
            --assumeCount;
        return action;
    }
    
    public int getAssumeCount() {
        return assumeCount;
    }
    
    public void print() {
        for(Action action: actionStack) {
            if(action.getCommand() == Action.REMOVE_VALID_VALUE) {
                System.out.print("REMOVE: ");
            }
            else if(action.getCommand() == Action.ASSUME_VALID_VALUE) {
                System.out.print("ASSUME: ");
            }
            else if(action.getCommand() == Action.FIX_VALUE) {
                System.out.print("FIX: ");
            }
            System.out.println(action.getRow() + ", " + action.getColumn() + ", " + action.getValue());
        }
    }
    
    public void clear() {
        actionStack.clear();
        assumeCount = 0;
    }
}

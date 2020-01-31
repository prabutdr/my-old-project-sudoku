/*
 * Sudoku.java
 *
 * Created on February 17, 2009, 9:54 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.spv.sudoku;

import java.io.*;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class Sudoku {
    Cell[][] table;
    int hDiv, vDiv;
    int rowSize, colSize;
    int fillCount;
    ActionStack actionStack;
            
    /** Creates a new instance of Sudoku */
    public Sudoku() {
        this(9, 9, 3, 3);
    }
    
    public Sudoku(int rowSize, int colSize, int hDiv, int vDiv) {
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.hDiv = hDiv;
        this.vDiv = vDiv;
        table = new Cell[rowSize][colSize];
        for(int i=0; i < rowSize; i++) {
            for(int j=0; j <colSize; j++) {
                table[i][j] = new Cell();
            }
        }
        fillCount = 0;
        actionStack = new ActionStack();
    }
    
    public boolean solve() {
        int i, j, k, l;
        int prevCount;
        boolean continueFlag;
        ArrayList matchedCells = new ArrayList();
        //int steps=99;
        //while(steps-- > 0) {
        while(true) {
            prevCount = fillCount;
            continueFlag = false;
            for(i=0; i < rowSize; i++) {
                for(j=0; j < colSize; j++) {
                    if(table[i][j].getNumberOfValuesExists() == 1) {
                        setCellValue(table[i][j].getValidValue(), i, j);
                        //fillCount++;
                        continueFlag = true;
                        //System.out.println("First: " + i + "," + j);
                    }
                }
            }
            
            //-------------------------------------------------------
            if(prevCount == fillCount) {
                ArrayList<Integer> cellListCopy = new ArrayList<Integer>();
                
                for(i=0; i < rowSize; i++) {
                    for(j=0; j < colSize; j++) {
                        if(table[i][j].getNumberOfValuesExists() <= 1)
                            continue;
                        
                        //Collections.copy(cellListCopy, table[i][j].getArrayList());
                        
                        //check row wise
                        cellListCopy.clear();
                        cellListCopy.addAll(0, table[i][j].getArrayList());
                        matchedCells.clear();
                        for(k=0; k < colSize; k++) {
                            if(k != j) 
                                cellListCopy.removeAll(table[i][k].getArrayList());
                            if(j <= k && table[i][j].getNumberOfValuesExists() == table[i][k].getNumberOfValuesExists()
                              && table[i][j].getArrayList().containsAll(table[i][k].getArrayList())) 
                                matchedCells.add(table[i][k]);
                        }
                        if(table[i][j].getNumberOfValuesExists() == matchedCells.size()) {
                            for(k=0; k < colSize; k++) {
                                if(matchedCells.contains(table[i][k]))
                                    continue;
                                for(Integer value:table[i][j].getArrayList()) {
                                    if(table[i][k].invalidate(value)) {
                                        actionStack.push(Action.REMOVE_VALID_VALUE, i, k, value);
                                        continueFlag = true;
                                    }
                                }
                                /*if(table[i][k].invalidate(table[i][j].getArrayList())) {
                                    actionStack.push(Action.REMOVE_VALID_VALUE, i, k, table[i][j].getArrayList());
                                    continueFlag = true;
                                }*/
                            }
                        }
                        if(cellListCopy.size() == 1) {
                            setCellValue(cellListCopy.get(0), i, j);
                            continueFlag = true;
                        }
                        
                        
                        //check column wise
                        cellListCopy.clear();
                        cellListCopy.addAll(0, table[i][j].getArrayList());
                        matchedCells.clear();
                        for(k=0; k < rowSize; k++) {
                            if(k != i) 
                                cellListCopy.removeAll(table[k][j].getArrayList());
                           if(i <= k && table[i][j].getNumberOfValuesExists() == table[k][j].getNumberOfValuesExists()
                             && table[i][j].getArrayList().containsAll(table[k][j].getArrayList())) 
                               matchedCells.add(table[k][j]);
                        }
                        if(table[i][j].getNumberOfValuesExists() == matchedCells.size()) {
                            for(k=0; k < rowSize; k++) {
                                if(matchedCells.contains(table[k][j]))
                                    continue;
                                for(Integer value:table[i][j].getArrayList()) {
                                    if(table[k][j].invalidate(value)) {
                                        actionStack.push(Action.REMOVE_VALID_VALUE, k, j, value);
                                        continueFlag = true;
                                    }
                                }
                                /*if(table[k][j].invalidate(table[i][j].getArrayList())) {
                                    actionStack.push(Action.REMOVE_VALID_VALUE, k, j, table[i][j].getArrayList());
                                    continueFlag = true;
                                }*/
                            }
                        }
                        if(cellListCopy.size() == 1) {
                            setCellValue(cellListCopy.get(0), i, j);
                            continueFlag = true;
                        }

                        //check in current block
                        cellListCopy.clear();
                        cellListCopy.addAll(0, table[i][j].getArrayList());
                        matchedCells.clear();
                        int blockStartRow = (i / hDiv) * hDiv;
                        int blockStartCol = (j / vDiv) * vDiv;
                        int blockEndRow = blockStartRow + hDiv - 1;
                        int blockEndCol = blockStartCol + hDiv - 1;
                        for(k = blockStartRow; k <= blockEndRow; k++) {
                            for(l = blockStartCol; l <= blockEndCol; l++) {
                                if(!(i == k && j == l))
                                  cellListCopy.removeAll(table[k][l].getArrayList());
                                if(table[i][j].getNumberOfValuesExists() == table[k][l].getNumberOfValuesExists()
                                  && table[i][j].getArrayList().containsAll(table[k][l].getArrayList())) 
                                    matchedCells.add(table[k][l]);
                            }
                        }
                        if(table[i][j].getNumberOfValuesExists() == matchedCells.size()) {
                            for(k = blockStartRow; k <= blockEndRow; k++) {
                                for(l = blockStartCol; l <= blockEndCol; l++) {
                                    if(matchedCells.contains(table[k][l]))
                                        continue;
                                    for(Integer value:table[i][j].getArrayList()) {
                                        if(table[k][l].invalidate(value)) {
                                            actionStack.push(Action.REMOVE_VALID_VALUE, k, l, value);
                                            continueFlag = true;
                                        }
                                    }
                                    /*if(table[k][l].invalidate(table[i][j].getArrayList())) {
                                        //System.out.println(k + ", " + l + ", " +  table[k][l].getArrayList() + ", " + table[i][j].getArrayList());
                                        actionStack.push(Action.REMOVE_VALID_VALUE, k, l, table[i][j].getArrayList());
                                        continueFlag = true;
                                    }*/
                                }
                            }
                        }
                        
                        if(cellListCopy.size() == 1) {
                            setCellValue(cellListCopy.get(0), i, j);
                            continueFlag = true;
                        }
                    }
                }
            }
            //-------------------------------------------------------
            /* Added on 07-Mar-2009  - begin - For hard problems*/
            /*if(prevCount == fillCount && continueFlag == false) {
                Action lastAssumeAction = actionStack.lastAssumeAction();
                int nextValidValue;
                if(lastAssumeAction != null) {
                    k = lastAssumeAction.getRow();
                    l = lastAssumeAction.getColumn();
                    cancelLastAssumeAction();
                    System.out.println("Revert: " + k + "," + l + ": " + lastAssumeAction.getValue());
                    nextValidValue = table[k][l].nextValidValueFromList(lastAssumeAction.getValue());
                    if(nextValidValue == 0)
                        return false;
                }
                else {
                    k = l = -1;
                    for(i=0; i < rowSize; i++) {
                        for(j=0; j < colSize; j++) {
                            if(table[i][j].isEmpty() 
                              && ((k == -1 && l == -1) || 
                                 table[k][l].getNumberOfValuesExists() > table[i][j].getNumberOfValuesExists())) {
                                 k = i;
                                 l = j;
                            }
                        }
                    }
                    nextValidValue = table[k][l].nextValidValueFromList(0);
                }
                System.out.println("Assume: " + k + "," + l + ": " + nextValidValue);
                System.out.println(table[k][l].getArrayList());
                actionStack.push(Action.ASSUME_VALID_VALUE, k, l, nextValidValue);
                setCellValue(nextValidValue, k, l);
                continueFlag = true;
            }*/
            if(prevCount == fillCount && continueFlag == false) {
                int nextValidValue;
                k = l = -1;
                for(i=0; i < rowSize; i++) {
                    for(j=0; j < colSize; j++) {
                        if(table[i][j].isEmpty() 
                          && ((k == -1 && l == -1) || 
                             table[k][l].getNumberOfValuesExists() > table[i][j].getNumberOfValuesExists())) {
                             k = i;
                             l = j;
                        }
                    }
                }
                nextValidValue = table[k][l].nextValidValueFromList(0);
                while(nextValidValue == 0 && actionStack.getAssumeCount() != 0) {
                    //this.printStackTrace();
                    Action lastAssumeAction = cancelLastAssumeAction();
                    k = lastAssumeAction.getRow();
                    l = lastAssumeAction.getColumn();
                    //System.out.println("Revert: " + k + "," + l + ": " + lastAssumeAction.getValue());
                    /*if(k == 0 && l==5)
                        System.out.println("Next valid value:" + table[k][l].nextValidValueFromList(lastAssumeAction.getValue()));*/
                    nextValidValue = table[k][l].nextValidValueFromList(lastAssumeAction.getValue());
                    //return false;
                }
                if(nextValidValue != 0) {
                    //out.println("Assume: " + nextValidValue);
                    //this.print(null);
                    //System.out.println("Assume: " + k + "," + l + ": " + nextValidValue);
                    //System.out.println(table[k][l].getArrayList());
                    actionStack.push(Action.ASSUME_VALID_VALUE, k, l, nextValidValue);
                    setCellValue(nextValidValue, k, l);
                    continueFlag = true;
                }
            }            
            /* Added on 07-Mar-2009 - End */
                
            //System.out.println(fillCount + ", " + prevCount + ", " + continueFlag /*+ ", " + steps*/);
       
            //There is no action in last step
            if(prevCount == fillCount && continueFlag == false)
                return false;
        
            //Solved
            if(fillCount == rowSize * colSize)
                return true;
            
            
        }
       //return false;
    }
    
    public boolean setCellValue(int value, int row, int col) {
        //System.out.println(row + ", " + col);
        if(value == 0)
            return false;
        if(row >= rowSize || col >= colSize || row < 0 || col < 0 || !table[row][col].isEmpty() || !table[row][col].isValidValue(value))
            return false;
            //throw new IllegalArgumentException("Invalid row/col value");
        int i, j;

        actionStack.push(Action.REMOVE_VALID_VALUE, row, col, table[row][col].getArrayList());
        table[row][col].setValue(value);
        actionStack.push(Action.FIX_VALUE, row, col, value);
        fillCount++;
        
        //remove value from current row
        for(i=0; i < colSize; i++) {
            if(table[row][i].invalidate(value))
                actionStack.push(Action.REMOVE_VALID_VALUE, row, i, value);
        }

        //remove value from current column
        for(i=0; i < rowSize; i++) {
            if(table[i][col].invalidate(value))
                actionStack.push(Action.REMOVE_VALID_VALUE, i, col, value);
        }
        
        //remove value from current block
        int blockStartRow = (row / hDiv) * hDiv;
        int blockStartCol = (col / vDiv) * vDiv;
        int blockEndRow = blockStartRow + hDiv - 1;
        int blockEndCol = blockStartCol + hDiv - 1;
        for(i = blockStartRow; i <= blockEndRow; i++) {
            if(i == row)
                continue;
            for(j = blockStartCol; j <= blockEndCol; j++) {
                if(j == col)
                    continue;
                if(table[i][j].invalidate(value)) {
                    actionStack.push(Action.REMOVE_VALID_VALUE, i, j, value);
                //if(i == 6 && j == 0) System.out.println(value + "," + table[i][j].getArrayList());
                }
            }
        }
        return true;
    }
    
    //PrintWriter out;
    public void print(PrintWriter out) {
       /*if(out == null) out = this.out;
       else this.out = out;*/
       for(int i = 0; i < rowSize; i++) {
            for(int j = 0; j < colSize; j++) {
                out.print(table[i][j].getValue() + " ");
            }
            out.println("");
        }
       /*for(int i = 0; i < rowSize; i++) {
            for(int j = 0; j < colSize; j++) {
                out.println(i + ", " + j + ": " + table[i][j].getArrayList());
            }
            out.print("\n");
        }*/
    }
    
    public Action cancelLastAssumeAction() {
        if(actionStack.getAssumeCount() == 0)
            return null;
        Action action;
        while((action=actionStack.pop()) != null) {
            if(action.getCommand() == Action.REMOVE_VALID_VALUE) {
                /*if(action.getRow() == 6 && action.getColumn() == 0)
                    System.out.println(action.getRow() + ", " + action.getColumn() + ", " + action.getValue() + ", " + table[action.getRow()][action.getColumn()].getArrayList());*/
                table[action.getRow()][action.getColumn()].addValidValue(action.getValue());
            }
            else if(action.getCommand() == Action.FIX_VALUE) {
                table[action.getRow()][action.getColumn()].clearValue();
                fillCount--;
            }
            else if(action.getCommand() == Action.ASSUME_VALID_VALUE)
                return action;
        }
        return null;
    }
    
    public void printStackTrace() {
        actionStack.print();
    }
}

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
    public static final byte DL_UNDEFINED = 0;
    public static final byte DL_VERY_EASY = 1;
    public static final byte DL_EASY = 2;
    public static final byte DL_MEDIUM = 3;
    public static final byte DL_HARD = 4;
    public static final byte DL_VERY_HARD = 5;
    Cell[][] table;
    int hDiv, vDiv;
    int rowSize, colSize;
    int fillCount;
    ActionStack actionStack;
    int points;
    byte difficult_level;
            
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
        points = 0;
        difficult_level = Sudoku.DL_UNDEFINED;
    }
    
    public boolean solve() {
        int i, j, k, l;
        int prevCount;
        boolean continueFlag;
        ArrayList matchedCells = new ArrayList();
        while(true) {
            prevCount = fillCount;
            continueFlag = false;
            for(i=0; i < rowSize; i++) {
                for(j=0; j < colSize; j++) {
                    if(table[i][j].getNumberOfValuesExists() == 1) {
                        setCellValue(table[i][j].getValidValue(), i, j);
                        continueFlag = true;
                        points++;
                        if(difficult_level == Sudoku.DL_UNDEFINED)
                            difficult_level = Sudoku.DL_EASY;
                    }
                }
            }
            
            //-------------------------------------------------------
            if(prevCount == fillCount) {
                if(difficult_level == Sudoku.DL_UNDEFINED)
                    difficult_level = Sudoku.DL_MEDIUM;
                
                ArrayList<Integer> cellListCopy = new ArrayList<Integer>();
                
                for(i=0; i < rowSize; i++) {
                    for(j=0; j < colSize; j++) {
                        if(table[i][j].getNumberOfValuesExists() <= 1)
                            continue;
                        
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
                                        points++;
                                    }
                                }
                            }
                        }
                        if(cellListCopy.size() == 1) {
                            setCellValue(cellListCopy.get(0), i, j);
                            continueFlag = true;
                            points+=3;
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
                                        points++;
                                    }
                                }
                            }
                        }
                        if(cellListCopy.size() == 1) {
                            setCellValue(cellListCopy.get(0), i, j);
                            continueFlag = true;
                            points+=3;
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
                                            points++;
                                        }
                                    }
                                }
                            }
                        }
                        
                        if(cellListCopy.size() == 1) {
                            setCellValue(cellListCopy.get(0), i, j);
                            continueFlag = true;
                            points+=3;
                        }
                    }
                }
            }
            //-------------------------------------------------------
            /* Added on 07-Mar-2009  - begin - For hard problems*/
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
                    Action lastAssumeAction = cancelLastAssumeAction();
                    points+=5; //for back track
                    k = lastAssumeAction.getRow();
                    l = lastAssumeAction.getColumn();
                    nextValidValue = table[k][l].nextValidValueFromList(lastAssumeAction.getValue());
                }
                if(nextValidValue != 0) {
                    if(difficult_level == Sudoku.DL_UNDEFINED)
                        difficult_level = Sudoku.DL_HARD;
                    actionStack.push(Action.ASSUME_VALID_VALUE, k, l, nextValidValue);
                    points+=5; //for assume
                    setCellValue(nextValidValue, k, l);
                    continueFlag = true;
                }
            }            
            /* Added on 07-Mar-2009 - End */
                
            //There is no action in last step
            if(/*prevCount == fillCount &&*/ continueFlag == false)
                return false;
        
            //Solved
            if(fillCount == rowSize * colSize)
                return true;
        }
    }
    
    public boolean setCellValue(int value, int row, int col) {
        if(value == 0)
            return false;
        if(row >= rowSize || col >= colSize || row < 0 || col < 0 || !table[row][col].isEmpty() || !table[row][col].isValidValue(value))
            return false;
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
                }
            }
        }
        return true;
    }
    
    public void print(PrintWriter out) {
       for(int i = 0; i < rowSize; i++) {
            for(int j = 0; j < colSize; j++) {
                out.print(table[i][j].getValue() + " ");
            }
            out.println();
        }
    }
    
    public Action cancelLastAssumeAction() {
        if(actionStack.getAssumeCount() == 0)
            return null;
        Action action;
        while((action=actionStack.pop()) != null) {
            if(action.getCommand() == Action.REMOVE_VALID_VALUE) {
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
    
    public boolean isSolved() {
        return (fillCount == rowSize * colSize);
    }
    
    public int getPoints() {
        return points;
    }
    
    public int getAssumeCount() {
        return actionStack.getAssumeCount();
    }
    
    public int getDifficultLevel() {
        if(difficult_level == Sudoku.DL_HARD && this.getAssumeCount() > 1)
            difficult_level = Sudoku.DL_VERY_HARD;
        return difficult_level;
    }
    
    public int getEmptyCellCount() {
        return ((rowSize * colSize) - fillCount);
    }
    
    public int getSolutionCount() {
        int i, j, k, l;
        int prevCount;
        boolean continueFlag;
        ArrayList matchedCells = new ArrayList();
        int solutionCount = 0;
        while(true) {
            prevCount = fillCount;
            continueFlag = false;
            for(i=0; i < rowSize; i++) {
                for(j=0; j < colSize; j++) {
                    if(table[i][j].getNumberOfValuesExists() == 1) {
                        setCellValue(table[i][j].getValidValue(), i, j);
                        continueFlag = true;
                        points++;
                        if(difficult_level == Sudoku.DL_UNDEFINED)
                            difficult_level = Sudoku.DL_EASY;
                    }
                }
            }
            
            //-------------------------------------------------------
            if(prevCount == fillCount) {
                if(difficult_level == Sudoku.DL_UNDEFINED)
                    difficult_level = Sudoku.DL_MEDIUM;
                
                ArrayList<Integer> cellListCopy = new ArrayList<Integer>();
                
                for(i=0; i < rowSize; i++) {
                    for(j=0; j < colSize; j++) {
                        if(table[i][j].getNumberOfValuesExists() <= 1)
                            continue;
                        
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
                                        points++;
                                    }
                                }
                            }
                        }
                        if(cellListCopy.size() == 1) {
                            setCellValue(cellListCopy.get(0), i, j);
                            continueFlag = true;
                            points+=3;
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
                                        points++;
                                    }
                                }
                            }
                        }
                        if(cellListCopy.size() == 1) {
                            setCellValue(cellListCopy.get(0), i, j);
                            continueFlag = true;
                            points+=3;
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
                                            points++;
                                        }
                                    }
                                }
                            }
                        }
                        
                        if(cellListCopy.size() == 1) {
                            setCellValue(cellListCopy.get(0), i, j);
                            continueFlag = true;
                            points+=3;
                        }
                    }
                }
            }
            //-------------------------------------------------------
            /* Added on 07-Mar-2009  - begin - For hard problems*/
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
                    Action lastAssumeAction = cancelLastAssumeAction();
                    points+=5; //for back track
                    k = lastAssumeAction.getRow();
                    l = lastAssumeAction.getColumn();
                    nextValidValue = table[k][l].nextValidValueFromList(lastAssumeAction.getValue());
                    //System.out.println("First Revert: " + k + ", " + l + ", " + lastAssumeAction.getValue() + ", " + nextValidValue + ", " + fillCount + ", " + prevCount);
                }
                if(nextValidValue != 0) {
                    if(difficult_level == Sudoku.DL_UNDEFINED)
                        difficult_level = Sudoku.DL_HARD;
                    actionStack.push(Action.ASSUME_VALID_VALUE, k, l, nextValidValue);
                    //System.out.println("First Assume: " + k + ", " + l + ", " + nextValidValue + ", " + fillCount + ", " + prevCount);
                    points+=5; //for assume
                    setCellValue(nextValidValue, k, l);
                    continueFlag = true;
                }
            }            
            /* Added on 07-Mar-2009 - End */
                
            //Solved
            if(fillCount == rowSize * colSize) {
                solutionCount++;
                /** For testing **/
                System.out.println("Solution #" + solutionCount);
                this.print();
                /*if(solutionCount > 1000)
                    return solutionCount;
                ****************************/
                int nextValidValue = 0;
                k=l=0;
                while(nextValidValue == 0 && actionStack.getAssumeCount() != 0) {
                    Action lastAssumeAction = cancelLastAssumeAction();
                    points+=5; //for back track
                    k = lastAssumeAction.getRow();
                    l = lastAssumeAction.getColumn();
                    nextValidValue = table[k][l].nextValidValueFromList(lastAssumeAction.getValue());
                    //System.out.println("Revert: " + k + ", " + l + ", " + lastAssumeAction.getValue() + ", " + nextValidValue);
                }
                if(nextValidValue != 0) {
                    if(difficult_level == Sudoku.DL_UNDEFINED)
                        difficult_level = Sudoku.DL_HARD;
                    actionStack.push(Action.ASSUME_VALID_VALUE, k, l, nextValidValue);
                    //System.out.println("Assume: " + k + ", " + l + ", " + nextValidValue + ", " + fillCount + ", " + prevCount);
                    points+=5; //for assume
                    setCellValue(nextValidValue, k, l);
                    continueFlag = true;
                }
                else {
                    //System.out.println("return 1");
                    return solutionCount;
                }
                //return true;
            }

            //There is no action in last step
            if(/*prevCount == fillCount &&*/ continueFlag == false) {
                    //System.out.println("return 2");
                return solutionCount;
                //return false;
            }
        }
    }

    public void print() {
       for(int i = 0; i < rowSize; i++) {
            for(int j = 0; j < colSize; j++) {
                System.out.print(table[i][j].getValue() + " ");
            }
            System.out.println();
        }
    }
}

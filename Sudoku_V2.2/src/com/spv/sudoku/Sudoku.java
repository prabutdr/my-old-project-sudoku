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
    int size;
    int fillCount;
    ActionStack actionStack;
    int points;
    byte difficult_level;
            
    /** Creates a new instance of Sudoku */
    public Sudoku() {
        this(9, 3);
    }
    
    public Sudoku(int size, int hDiv) {
        if(size < 4 || hDiv < 2 || size < hDiv || (size%hDiv != 0))
            throw new IllegalArgumentException("Invalid sudoku parameters");
        this.size = size;
        this.hDiv = hDiv;
        this.vDiv = size / hDiv;
        table = new Cell[size][size];
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                table[i][j] = new Cell(size);
            }
        }
        fillCount = 0;
        actionStack = new ActionStack();
        points = -1;
        difficult_level = Sudoku.DL_UNDEFINED;
    }
    
    public Sudoku(Sudoku sudoku) {
        this(sudoku.size, sudoku.hDiv);
        /*this.size = sudoku.size;
        this.hDiv = sudoku.hDiv;
        this.vDiv = sudoku.vDiv;
        fillCount = 0;
        actionStack = new ActionStack();
        points = -1;
        difficult_level = Sudoku.DL_UNDEFINED;

        table = new Cell[size][size];
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                table[i][j] = new Cell(size);
            }
        }*/
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                this.setCellValue(sudoku.getCellValue(i, j), i, j);
            }
        }
    }    
    
    public boolean setCellValue(int value, int row, int col) {
        if(value == 0)
            return false;
        if(row >= size || col >= size || row < 0 || col < 0 || !table[row][col].isEmpty() || !table[row][col].isValidValue(value))
            return false;
        int i, j;

        actionStack.push(Action.REMOVE_VALID_VALUE, row, col, table[row][col].getArrayList());
        table[row][col].setValue(value);
        actionStack.push(Action.FIX_VALUE, row, col, value);
        fillCount++;
        
        //remove value from current row
        for(i=0; i < size; i++) {
            if(table[row][i].invalidate(value))
                actionStack.push(Action.REMOVE_VALID_VALUE, row, i, value);
        }

        //remove value from current column
        for(i=0; i < size; i++) {
            if(table[i][col].invalidate(value))
                actionStack.push(Action.REMOVE_VALID_VALUE, i, col, value);
        }
        
        //remove value from current block
        int blockStartRow = (row / hDiv) * hDiv;
        int blockStartCol = (col / vDiv) * vDiv;
        int blockEndRow = blockStartRow + hDiv - 1;
        int blockEndCol = blockStartCol + vDiv - 1;
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
    
    public Integer getCellValue(int row, int col) {
        return this.table[row][col].getValue();
    }
    
    public boolean removeCellValue(int row, int col) {
        Integer value = table[row][col].getValue();
        if(table[row][col].clearValue()) {
            fillCount--;
            actionStack.push(Action.REMOVE_CELL_VALUE, row, col, value);
            return true;
        }
        return false;
    }
    
    public boolean assumeValidCellValue(int row, int col) {
        if(row >= size || col >= size || row < 0 || col < 0 || !table[row][col].isEmpty())
            return false;
        int value = table[row][col].getRandomValidValueFromList();
        return this.setCellValue(value, row, col);
    }
    
    public void print(PrintWriter out) {
       for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                out.print(table[i][j].getValue() + " ");
            }
            out.println();
        }
    }
    
    //To print in standard output
    public void print() {
       for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                System.out.print(table[i][j].getValue() + " ");
            }
            System.out.println();
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
            else if(action.getCommand() == Action.REMOVE_CELL_VALUE) {
                table[action.getRow()][action.getColumn()].setValue(action.getValue());
                fillCount++;
            }
            else if(action.getCommand() == Action.ASSUME_VALID_VALUE)
                return action;
        }
        return null;
    }

    public void cancelAllActions() {
        Action action;
        while((action=actionStack.pop()) != null) {
            if(action.getCommand() == Action.REMOVE_VALID_VALUE) {
                table[action.getRow()][action.getColumn()].addValidValue(action.getValue());
            }
            else if(action.getCommand() == Action.FIX_VALUE) {
                table[action.getRow()][action.getColumn()].clearValue();
                fillCount--;
            }
            else if(action.getCommand() == Action.REMOVE_CELL_VALUE) {
                table[action.getRow()][action.getColumn()].setValue(action.getValue());
                fillCount++;
            }
            /*else if(action.getCommand() == Action.ASSUME_VALID_VALUE)
                return action;*/
        }
        points = -1;
        difficult_level = Sudoku.DL_UNDEFINED;        
    }
    
    public boolean cancelLastAction() {
        Action action;
        if((action=actionStack.pop()) != null) {
            if(action.getCommand() == Action.REMOVE_VALID_VALUE) {
                table[action.getRow()][action.getColumn()].addValidValue(action.getValue());
            }
            else if(action.getCommand() == Action.FIX_VALUE) {
                table[action.getRow()][action.getColumn()].clearValue();
                fillCount--;
                if((action = actionStack.peek()) != null && action.getCommand() == Action.ASSUME_VALID_VALUE)
                    actionStack.pop();
                return true;
            }
            else if(action.getCommand() == Action.REMOVE_CELL_VALUE) {
                table[action.getRow()][action.getColumn()].setValue(action.getValue());
                fillCount++;
            }
            /*else if(action.getCommand() == Action.ASSUME_VALID_VALUE)
                return action;*/
        }
        return false;
    }
    
    public void printStackTrace() {
        actionStack.print();
    }
    
    public boolean isSolved() {
        return (fillCount == size * size);
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
        //else if(difficult_level == Sudoku.DL_EASY && )
        return difficult_level;
    }
    
    public int getEmptyCellCount() {
        return ((size * size) - fillCount);
    }
    
    public void clearActionStack() {
        this.actionStack.clear();
        this.points = -1;
        this.difficult_level = Sudoku.DL_UNDEFINED;
    }
    
    private boolean tryLevel_1() {
        boolean retValue = false;
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                if(table[i][j].getNumberOfValuesExists() == 1) {
                    setCellValue(table[i][j].getValidValue(), i, j);
                    retValue = true;
                    points++;
                }
            }
        }
        return retValue;
    }
    
    private boolean tryLevel_2() {
        boolean retValue = false;
        int i, j, k, l;
        ArrayList<Integer> cellListCopy = new ArrayList<Integer>();
        ArrayList<Cell> matchedCells = new ArrayList<Cell>();

        for(i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                if(table[i][j].getNumberOfValuesExists() <= 1)
                    continue;

                //check row wise
                cellListCopy.clear();
                cellListCopy.addAll(0, table[i][j].getArrayList());
                matchedCells.clear();
                for(k=0; k < size; k++) {
                    if(k != j) 
                        cellListCopy.removeAll(table[i][k].getArrayList());
                    if(j <= k && table[i][j].getNumberOfValuesExists() == table[i][k].getNumberOfValuesExists()
                      && table[i][j].getArrayList().containsAll(table[i][k].getArrayList())) 
                        matchedCells.add(table[i][k]);
                }
                if(table[i][j].getNumberOfValuesExists() == matchedCells.size()) {
                    for(k=0; k < size; k++) {
                        if(matchedCells.contains(table[i][k]))
                            continue;
                        for(Integer value:table[i][j].getArrayList()) {
                            if(table[i][k].invalidate(value)) {
                                actionStack.push(Action.REMOVE_VALID_VALUE, i, k, value);
                                retValue = true;
                                points++;
                            }
                        }
                    }
                }
                if(cellListCopy.size() == 1) {
                    setCellValue(cellListCopy.get(0), i, j);
                    retValue = true;
                    points+=3;
                }

                //check column wise
                cellListCopy.clear();
                cellListCopy.addAll(0, table[i][j].getArrayList());
                matchedCells.clear();
                for(k=0; k < size; k++) {
                    if(k != i) 
                        cellListCopy.removeAll(table[k][j].getArrayList());
                   if(i <= k && table[i][j].getNumberOfValuesExists() == table[k][j].getNumberOfValuesExists()
                     && table[i][j].getArrayList().containsAll(table[k][j].getArrayList())) 
                       matchedCells.add(table[k][j]);
                }
                if(table[i][j].getNumberOfValuesExists() == matchedCells.size()) {
                    for(k=0; k < size; k++) {
                        if(matchedCells.contains(table[k][j]))
                            continue;
                        for(Integer value:table[i][j].getArrayList()) {
                            if(table[k][j].invalidate(value)) {
                                actionStack.push(Action.REMOVE_VALID_VALUE, k, j, value);
                                retValue = true;
                                points++;
                            }
                        }
                    }
                }
                if(cellListCopy.size() == 1) {
                    setCellValue(cellListCopy.get(0), i, j);
                    retValue = true;
                    points += 3;
                }

                //check in current block
                cellListCopy.clear();
                cellListCopy.addAll(0, table[i][j].getArrayList());
                matchedCells.clear();
                int blockStartRow = (i / hDiv) * hDiv;
                int blockStartCol = (j / vDiv) * vDiv;
                int blockEndRow = blockStartRow + hDiv - 1;
                int blockEndCol = blockStartCol + vDiv - 1;
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
                                    retValue = true;
                                    points++;
                                }
                            }
                        }
                    }
                }

                if(cellListCopy.size() == 1) {
                    setCellValue(cellListCopy.get(0), i, j);
                    retValue = true;
                    points+=3;
                }
            }
        }
        return retValue;
    }
    
    private boolean tryLevel_3(boolean checkEmptyCells) {
        if(this.getEmptyCellCount() == 0)
            return false;
        boolean retValue = false;
        int k = -1, l = -1;
        int nextValidValue = 0;

        if(checkEmptyCells) {
            for(int i=0; i < size; i++) {
                for(int j=0; j < size; j++) {
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
        
        while(nextValidValue == 0 && actionStack.getAssumeCount() != 0) {
            Action lastAssumeAction = cancelLastAssumeAction();
            points+=5; //for back track
            k = lastAssumeAction.getRow();
            l = lastAssumeAction.getColumn();
            nextValidValue = table[k][l].nextValidValueFromList(lastAssumeAction.getValue());
            //System.out.println("Revert: " + k + ", " + l + ", " + lastAssumeAction.getValue() + ", " + nextValidValue);
        }
        if(nextValidValue != 0) {
            actionStack.push(Action.ASSUME_VALID_VALUE, k, l, nextValidValue);
            points+=5; //for assume
            //System.out.println("Assume: " + k + ", " + l + ", " + nextValidValue);
            setCellValue(nextValidValue, k, l);
            retValue = true;
        }
        return retValue;
    }
    
    private boolean solve() {
        if(this.isSolved())
            return true;
        
        if(difficult_level == Sudoku.DL_UNDEFINED)
            difficult_level = Sudoku.DL_EASY;

        while(true) {
            if(this.tryLevel_1() == false) {
                if(difficult_level < Sudoku.DL_MEDIUM)
                   difficult_level = Sudoku.DL_MEDIUM;
                if(this.tryLevel_2() == false) {
                    if(difficult_level < Sudoku.DL_HARD)
                       difficult_level = Sudoku.DL_HARD;
                    if(this.tryLevel_3(true) == false) {
                        return false;
                    }
                }
            }
            if(fillCount == size * size)
                return true;
        }
    }
 
    private int getSolveCount(int maxCount) {
        if(this.getEmptyCellCount() == 0 && this.isSolved())
            return 1;

        int solutionCount = 0;

        do {
            while(true) {
                if(this.tryLevel_1() == false) {
                    if(this.tryLevel_2() == false) {
                        if(this.tryLevel_3(true) == false) {
                            break;
                        }
                    }
                }
                if(fillCount == size * size) {
                    solutionCount++;
                    if(maxCount != 0 && solutionCount >= maxCount)
                        return solutionCount;
                    break;
                }
            }
        } while(this.tryLevel_3(false));
        
        return solutionCount;
    }
            
    public int getSolutionCount() {
        Sudoku sudoku = new Sudoku(this);
        return sudoku.getSolveCount(0);
    }
    
    public boolean hasMoreThanOneSolution() {
        Sudoku sudoku = new Sudoku(this);
        return (sudoku.getSolveCount(2) == 2);
    }
    
    public Sudoku getSolution() {
        Sudoku sudoku = new Sudoku(this);
        if(sudoku.solve())
            return sudoku;
        return null;
    }
}

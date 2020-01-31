/*
 * SudokuGenerator.java
 *
 * Created on March 18, 2009, 10:17 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.spv.sudoku;

import java.util.*;
/**
 *
 * @author 
 */
public class SudokuGenerator {
    
    /** Creates a new instance of SudokuGenerator */
    public SudokuGenerator() {
    }

    public static Sudoku getRandomizedSudoku() {
        return getRandomizedSudoku(9, 3, Sudoku.DL_EASY);
    }
    
    public static Sudoku getRandomizedSudoku(int size, int hDiv, int level) {
        Sudoku tmpSudoku = new Sudoku(size, hDiv);
        Random random = new Random();
        int rcell, rrow, rcol, totalCells, minEmptyCells;
        totalCells = size * size;
        minEmptyCells = hDiv;
        
        for(int i = 1; i <= hDiv; i++) {
            do {
                rcell = random.nextInt(totalCells);
                rrow = rcell / size;
                rcol = rcell % size;
            } while(!tmpSudoku.assumeValidCellValue(rrow, rcol));
        }
        tmpSudoku = tmpSudoku.getSolution();
        
        while(minEmptyCells-- > 0) {
            do {
                rcell = random.nextInt(totalCells);
                rrow = rcell / size;
                rcol = rcell % size;
            } while(!tmpSudoku.removeCellValue(rrow, rcol));
        }

        do {
            while(tmpSudoku.getSolution().getDifficultLevel() <= level) {
                do {
                    rcell = random.nextInt(totalCells);
                    rrow = rcell / size;
                    rcol = rcell % size;
                } while(!tmpSudoku.removeCellValue(rrow, rcol));
            }
            tmpSudoku.cancelLastAction();
        } while(tmpSudoku.getSolution().getDifficultLevel() != level);

        return new Sudoku(tmpSudoku);
    }
    
    public static void main(String[] args) {
        Date startTime = new Date();
        Sudoku tmpSudoku = SudokuGenerator.getRandomizedSudoku(9, 3, Sudoku.DL_HARD);
        System.out.println();
        tmpSudoku.print();
        System.out.println("Empty cells: " + tmpSudoku.getEmptyCellCount());
        System.out.println("Solution Count: " + tmpSudoku.getSolutionCount());
        tmpSudoku = tmpSudoku.getSolution();
        System.out.println("Difficult Level: " + tmpSudoku.getDifficultLevel());
        System.out.println("Points: " + tmpSudoku.getPoints());
        System.out.println("Assume count: " + tmpSudoku.getAssumeCount());
        Date endTime = new Date();
        System.out.println("Elapsed Time: " + (endTime.getTime() - startTime.getTime()) + " milli seconds");
    }
}

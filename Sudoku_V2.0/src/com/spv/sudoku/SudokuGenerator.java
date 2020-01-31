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
        return getRandomizedSudoku(9, 3);
    }
    
    public static Sudoku getRandomizedSudoku(int size, int hDiv) {
        Sudoku tmpSudoku = new Sudoku(size, hDiv);
        Random random = new Random();
        int rcell, rrow, rcol, totalCells, minEmptyCells;
        totalCells = size * size;
        minEmptyCells = totalCells/3;
        
        //String timeValue = String.valueOf(new Date().getTime()).replaceAll("0", "");
        //for(char ch:timeValue.toCharArray()) {
        for(int i = 2 * hDiv; i >= 1; i--) {
            do {
                rcell = random.nextInt(totalCells);
                rrow = rcell / size;
                rcol = rcell % size;
                System.out.println(rrow + "," + rcol);
            } while(!tmpSudoku.assumeValidCellValue(rrow, rcol));
            //} while(!tmpSudoku.setCellValue(ch - 48, rrow, rcol));
        }
        tmpSudoku.print();
        tmpSudoku.solve();
        
       /* while(minEmptyCells-- > 0) {
            do {
                rcell = random.nextInt(totalCells);
                rrow = rcell / size;
                rcol = rcell % size;
            } while(!tmpSudoku.removeCellValue(rrow, rcol));
        }

        //do {
        Sudoku retSudoku = new Sudoku(tmpSudoku);
        while(minEmptyCells-- > 0) {
            do {
                rcell = random.nextInt(totalCells);
                rrow = rcell / size;
                rcol = rcell % size;
            } while(!tmpSudoku.removeCellValue(rrow, rcol));
        }*/
        return tmpSudoku;
    }
    
    public static void main(String[] args) {
        Sudoku tmpSudoku = SudokuGenerator.getRandomizedSudoku(6, 3);
        tmpSudoku.print();
        System.out.println("Empty cells: " + tmpSudoku.getEmptyCellCount());
    }
}

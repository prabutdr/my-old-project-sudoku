/*
 * Main_2.java
 *
 * Created on March 15, 2009, 2:24 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.spv.sudokuTest;

import com.spv.sudoku.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class Main_2 {
    
    /** Creates a new instance of Main_2 */
    public Main_2() {
    }
    
    public static void main(String[] args) {
        Date startTime = new Date();
        //PrintWriter out = new PrintWriter(System.out);

        Sudoku sudoku = new Sudoku(9, 3);
        sudoku.setCellValue(1, 0, 0);
        sudoku.setCellValue(2, 0, 1);
        sudoku.setCellValue(3, 0, 2);
        sudoku.setCellValue(4, 0, 3);
        sudoku.setCellValue(5, 0, 4);
        sudoku.setCellValue(6, 0, 5);
        sudoku.setCellValue(7, 0, 6);
        sudoku.setCellValue(8, 0, 7);
        sudoku.setCellValue(9, 0, 8);

        sudoku.print();
        int count = sudoku.getSolutionCount();
        System.out.println("Solve Count: " + count);
        /*if(sudoku.solve()) {
            out.println("Success!!");
            sudoku.print(out);
        }
        else {
            out.println("Failed!");
            sudoku.print(out);
        }*/
        Date endTime = new Date();
        System.out.println("Elapsed Time: " + (endTime.getTime() - startTime.getTime()) + " milli seconds");        
        System.out.close();
    }
}

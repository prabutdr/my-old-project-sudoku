/*
 * Main.java
 *
 * Created on February 26, 2009, 10:43 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.spv.sudokuTest;

import com.spv.sudoku.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Date startTime = new Date();
        try {
            String inFileName = "IO/input/in018.txt";
            String outFileName = "IO/output/out018.txt";

            Scanner sc = new Scanner(new File(inFileName));
            PrintWriter out = new PrintWriter(new File(outFileName));
            
            int rows = sc.nextInt();
            int cols = sc.nextInt();
            int hDiv = sc.nextInt();
            int vDiv = sc.nextInt();
            
            Sudoku sudoku = new Sudoku(rows, hDiv);
            
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    sudoku.setCellValue(sc.nextInt(), i, j); //.setCellValue(sc.nextInt(), i, j);
                }
            }
            System.out.println("Solution Count: " + sudoku.getSolutionCount());
            System.out.println("Has more than solution: " + sudoku.hasMoreThanOneSolution());
            //sudoku.print(new PrintWriter(System.out));
            sudoku.print(out);
            sudoku = sudoku.getSolution();
            if(sudoku.isSolved()) {
                out.println("Success!!");
                sudoku.print(out);
                out.println("Points: " + sudoku.getPoints());
                out.println("Assume Count: " + sudoku.getAssumeCount());
            }
            else {
                out.println("Failed!");
                sudoku.print(out);
            }
            //sudoku.printStackTrace();
            out.close();
            sc.close();
        }
        catch(FileNotFoundException fne) {
            System.out.println("FileNotFoundException: " + fne);
        }
        catch(IOException ioe) {
            System.out.println("IOException: " + ioe);
        }
        Date endTime = new Date();
        System.out.println("Elapsed Time: " + (endTime.getTime() - startTime.getTime()) + " milli seconds");
    }
}

/*
 * Main.java
 *
 * Created on February 26, 2009, 10:43 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.spv.sudoku;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Date startTime = new Date();
        try {
            String inFileName = "input/in017.txt";
            String outFileName = "output/out017.txt";

            Scanner sc = new Scanner(new File(inFileName));
            PrintWriter out = new PrintWriter(new File(outFileName));
            
            int rows = sc.nextInt();
            int cols = sc.nextInt();
            int hDiv = sc.nextInt();
            int vDiv = sc.nextInt();
            
            Sudoku sudoku = new Sudoku(rows, cols, hDiv, vDiv);
            
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    sudoku.setCellValue(sc.nextInt(), i, j); //.setCellValue(sc.nextInt(), i, j);
                }
            }
            //sudoku.print(new PrintWriter(System.out));
            sudoku.print(out);
            if(sudoku.solve()) {
                out.println("Success!!");
                sudoku.print(out);
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

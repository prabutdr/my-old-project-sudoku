/*
 * Main_3.java
 *
 * Created on March 15, 2009, 2:38 PM
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
public class Main_3 {
    
    /** Creates a new instance of Main_3 */
    public Main_3() {
    }

    public static void main(String[] args) {
        Date startTime = new Date();
        try {
            File dir = new File("input_1");
            File[] files = dir.listFiles();
            
            for(File file:files) {
                int puzzleCount = 0;
                int successCount = 0;
                Scanner sc = new Scanner(file);
                PrintWriter out = new PrintWriter(new File("output_3/" + file.getName()));

                int rows = 9;
                int cols = 9;
                int hDiv = 3;
                int vDiv = 3;
                String line;
                char ch;
                
                while(sc.hasNextLine()) {
                    line = sc.nextLine();
                    puzzleCount++;
                    System.out.println("Puzzle #" + puzzleCount);
                    Sudoku sudoku = new Sudoku(rows, cols, hDiv, vDiv);
                    Scanner scline = new Scanner(line);
                    int chpos = 0;
                    for(int i = 0; i < rows; i++) {
                        for(int j = 0; j < cols; j++) {
                            if((ch=line.charAt(chpos)) != '.')
                                sudoku.setCellValue(ch-48, i, j); 
                            chpos++;
                        }
                    }
                    //sudoku.print(new PrintWriter(System.out));
                    out.println("Sudoku #" + puzzleCount);
                    //if(puzzleCount == 6602) {
                    sudoku.print(out);//break;
                    //}
                    if(sudoku.solve()) {
                        out.println("Solution:");
                        sudoku.print(out);
                        successCount++;
                    }
                    else {
                        out.println("Failed!");
                        sudoku.print(out);
                    }
                    out.println();
                }
                //sudoku.printStackTrace();
                out.close();
                sc.close();
            }
            System.out.println("puzzle count: " + puzzleCount);
            System.out.println("Successfully Solved: " + successCount);
            System.out.println("Failed: " + (puzzleCount - successCount));
        }
        catch(FileNotFoundException fne) {
            System.out.println(fne);
        }
        catch(IOException ioe) {
            System.out.println(ioe);
        }
        Date endTime = new Date();
        System.out.println("Elapsed Time: " + (endTime.getTime() - startTime.getTime()) + " milli seconds");
    }

}

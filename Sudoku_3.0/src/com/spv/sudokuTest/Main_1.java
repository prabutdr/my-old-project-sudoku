/*
 * Main_1.java
 *
 * Created on March 14, 2009, 7:32 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.spv.sudokuTest;

import com.spv.sudoku.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class Main_1 {
    
    /** Creates a new instance of Main_1 */
    public Main_1() {
    }
    
    public static void main(String[] args) {
        Date startTime = new Date();
        try {
            File dir = new File("IO/input_3");
            System.out.println("Is directory: " + dir.isDirectory() + " " + dir.getName());
            File[] files = dir.listFiles();
            int totalPuzzleCount = 0;
            int totalSuccessCount = 0;
            
            for(File file:files) {
                Scanner sc = new Scanner(file);
                PrintWriter out = new PrintWriter(new File("IO/output_3/" + file.getName()));

                int rows = 9;
                int cols = 9;
                int hDiv = 3;
                int vDiv = 3;
                String line;
                char ch;
                int curPuzzleCount = 0;
                int curSuccessCount = 0;
                int curEmptyCount = 0;
                int curPoints = 0;
                int minPoints = 0;
                int maxPoints = 0;
                int minEmpty = 0;
                int maxEmpty = 0;
                
                while(sc.hasNextLine()) {
                    curPuzzleCount++;
                    totalPuzzleCount++;
                    line = sc.nextLine();
                    System.out.println("Puzzle #" + totalPuzzleCount);
                    Sudoku sudoku = new Sudoku(rows, hDiv);
                    Scanner scline = new Scanner(line);
                    int chpos = 0;
                    for(int i = 0; i < rows; i++) {
                        for(int j = 0; j < cols; j++) {
                            if((ch=line.charAt(chpos)) != '.')
                                sudoku.setCellValue(ch-48, i, j); 
                            chpos++;
                        }
                    }
                    out.println("Sudoku #" + totalPuzzleCount);
                    sudoku.print(out);
                    curEmptyCount += sudoku.getEmptyCellCount();
                    out.println("Empty Count: " + sudoku.getEmptyCellCount());
                        if(minEmpty == 0 || sudoku.getEmptyCellCount() < minEmpty)
                            minEmpty = sudoku.getEmptyCellCount();
                        if(sudoku.getEmptyCellCount() > maxEmpty)
                            maxEmpty = sudoku.getEmptyCellCount();
                    /*int solutionCount = sudoku.getSolutionCount();
                    if(solutionCount != 1)
                        System.out.println(".......................................Sudoku #" + totalPuzzleCount + ", " + solutionCount);*/
                    sudoku = sudoku.getSolution();
                    if(sudoku.isSolved()) {
                        out.println("Solution:");
                        sudoku.print(out);
                        curSuccessCount++;
                        curPoints +=  sudoku.getPoints();
                        if(minPoints == 0 || sudoku.getPoints() < minPoints)
                            minPoints = sudoku.getPoints();
                        if(sudoku.getPoints() > maxPoints)
                            maxPoints = sudoku.getPoints();
                        out.println("Points: " + sudoku.getPoints());
                        out.println("Assumption Count: " + sudoku.getAssumeCount());
                        out.println("Difficult Level: " + sudoku.getDifficultLevel());
                    }
                    else {
                        out.println("Failed!");
                        sudoku.print(out);
                    }
                    out.println();
                }
                out.println("puzzle count: " + curPuzzleCount);
                out.println("Successfully Solved: " + curSuccessCount);
                out.println("Failed: " + (curPuzzleCount - curSuccessCount));
                out.println("Average points: " + (curPoints/curPuzzleCount));
                out.println("Average Empty's: " + (curEmptyCount/curPuzzleCount));
                out.println("Min points: " + minPoints);
                out.println("Max points: " + maxPoints);
                out.println("Min Empty: " + minEmpty);
                out.println("Max Empty: " + maxEmpty);
                //totalPuzzleCount += curPuzzleCount;
                totalSuccessCount += curSuccessCount;
                out.close();
                sc.close();
            }
            System.out.println("puzzle count: " + totalPuzzleCount);
            System.out.println("Successfully Solved: " + totalSuccessCount);
            System.out.println("Failed: " + (totalPuzzleCount - totalSuccessCount));
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

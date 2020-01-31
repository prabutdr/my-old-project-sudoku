/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SudokuPanel.java
 *
 * Created on Apr 2, 2009, 10:14:07 PM
 */

package com.spv.sudokuui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import com.spv.sudoku.*;
/**
 *
 * @author Administrator
 */
public class SudokuPanel extends javax.swing.JPanel {

    /** Creates new form SudokuPanel */
    public SudokuPanel() {
        this(null);
    }
    public SudokuPanel(int size, int hDiv) {
        this(new Sudoku(size, hDiv));
    }

    public SudokuPanel(Sudoku sudoku) {
        initComponents();

        this.loadSudoku(sudoku);
        /*int i, j;
        if(sudoku == null)
            sudoku = new Sudoku();
        this.size = sudoku.getSize();
        this.hDiv = sudoku.getHorizontalBoxCount();
        this.vDiv = size / hDiv;
        fillCount = 0;
        actionStack = new ActionStack();

        this.setLayout(new GridLayout(vDiv, hDiv, 0, 0));
        box = new JPanel[vDiv][hDiv];
        for(i=0; i < vDiv; i++) {
            for(j=0; j < hDiv; j++) {
                box[i][j] = new JPanel(new GridLayout(hDiv, vDiv));
                box[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                if(((i + j) % 2) == 0)
                    box[i][j].setBackground(boxColor_2);
                else
                    box[i][j].setBackground(boxColor_1);
                this.add(box[i][j]);
            }
        }

        cell = new CellPanel[size][size];
        for(i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                cell[i][j] = new CellPanel(this, i, j, size);
                if(sudoku.getCellValue(i, j) != 0) {
                    cell[i][j].setFixedValue(String.valueOf(sudoku.getCellValue(i, j)));
                }
                box[i/hDiv][j/vDiv].add(cell[i][j]);
            }
        }
        actionStack.clear();

        dimension = new Dimension((int)cell[0][0].getDimension().getWidth() * size, 
                (int)cell[0][0].getDimension().getHeight() * size);
        this.setSize(dimension);*/
    }

    public void loadSudoku(Sudoku sudoku) {
        int i, j;
        if(sudoku == null)
            sudoku = new Sudoku();

        this.size = sudoku.getSize();
        this.hDiv = sudoku.getHorizontalBoxCount();
        this.vDiv = size / hDiv;
        automark = false;
        fillCount = 0;
        actionStack = new ActionStack();

        this.removeAll();
        this.setLayout(new GridLayout(vDiv, hDiv, 0, 0));
        //System.out.println(hDiv + ", " + vDiv);
        box = new JPanel[vDiv][hDiv];
        for(i=0; i < vDiv; i++) {
            for(j=0; j < hDiv; j++) {
                box[i][j] = new JPanel(new GridLayout(hDiv, vDiv));
                box[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                if(((i + j) % 2) == 0)
                    box[i][j].setBackground(boxColor_2);
                else
                    box[i][j].setBackground(boxColor_1);
                this.add(box[i][j]);
            }
        }

        //Define cells
        cell = new CellPanel[size][size];
        for(i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                cell[i][j] = new CellPanel(this, i, j, size);
                box[i/hDiv][j/vDiv].add(cell[i][j]);
            }
        }
        
        //load default cell values
        for(i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                if(sudoku.getCellValue(i, j) != 0) {
                    cell[i][j].setFixedValue(String.valueOf(sudoku.getCellValue(i, j)));
                }
            }
        }

        actionStack.clear();

        dimension = new Dimension((int)cell[0][0].getDimension().getWidth() * size,
                (int)cell[0][0].getDimension().getHeight() * size);
        this.setSize(dimension);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void cellValudeDefined(CellPanel c) {
        int i, j, row, col, value;
        fillCount++;
        row = c.getRow();
        col = c.getColumn();
        value = c.getValueAsInt();
        actionStack.push(com.spv.sudoku.Action.FIX_VALUE, row, col, value);
        /*if(fillCount == size * size && isSolved()) {
          JOptionPane.showMessageDialog(this, "Successfully Solved....");
        }*/

        //remove value from current row
        for(i=0; i < size; i++) {
            cell[row][i].removeValidValue(value);
        }

        //remove value from current column
        for(i=0; i < size; i++) {
            cell[i][col].removeValidValue(value);
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
                cell[i][j].removeValidValue(value);
            }
        }
    }

    public void cellValueRemoved(CellPanel c, int value) {
        int i, j, row, col;
        fillCount--;
        row = c.getRow();
        col = c.getColumn();
        //value = c.getValueAsInt();
        actionStack.push(com.spv.sudoku.Action.REMOVE_CELL_VALUE, row, col, value);

        //add value in current row
        for(i=0; i < size; i++) {
            cell[row][i].addValidValue(value);
        }

        //add value in current column
        for(i=0; i < size; i++) {
            cell[i][col].addValidValue(value);
        }

        //add value in current block
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
                cell[i][j].addValidValue(value);
            }
        }
    }

    public boolean isValidValue(CellPanel c, int value) {
        int i, j, row, col;
        row = c.getRow();
        col = c.getColumn();

        //look value in current row
        for(i=0; i < size; i++) {
            if(cell[row][i].getValueAsInt() == value)
                return false;
        }

        //look value in current column
        for(i=0; i < size; i++) {
            if(cell[i][col].getValueAsInt() == value)
                return false;
        }

        //look value in current block
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
                if(cell[i][j].getValueAsInt() == value)
                    return false;
            }
        }

        return true;
    }

    public boolean undo() {
        com.spv.sudoku.Action action;
        if((action=actionStack.pop()) != null) {
            if(action.getCommand() == com.spv.sudoku.Action.FIX_VALUE) {
                cell[action.getRow()][action.getColumn()].clearValue();
                return true;
            }
            else if(action.getCommand() == com.spv.sudoku.Action.REMOVE_CELL_VALUE) {
                cell[action.getRow()][action.getColumn()].setValue(action.getValue());
                return true;
            }
        }
        return false;
    }

    public boolean redo() {
        com.spv.sudoku.Action action;
        if((action=actionStack.popRedoAction()) != null) {
            if(action.getCommand() == com.spv.sudoku.Action.FIX_VALUE) {
                actionStack.backupRevActionStack();
                cell[action.getRow()][action.getColumn()].setValue(action.getValue());
                actionStack.restoreRevActionStack();
                return true;
            }
            else if(action.getCommand() == com.spv.sudoku.Action.REMOVE_CELL_VALUE) {
                actionStack.backupRevActionStack();
                cell[action.getRow()][action.getColumn()].clearValue();
                actionStack.restoreRevActionStack();
                return true;
            }
        }
        return false;
    }

    public boolean canRedo() {
        return actionStack.canRedoAction();
    }
    
    public boolean canUndo() {
        return (actionStack.peek() != null);
    }

    public boolean isSolved() {
        if(fillCount != size * size)
            return false;
        
        int i, j;
        Sudoku tmpsudoku = new Sudoku(size, hDiv);

        for(i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                if(tmpsudoku.setCellValue(cell[i][j].getValueAsInt(), i, j) == false)
                    return false;
            }
        }
        return tmpsudoku.isSolved();
    }

    public void solve() {
        int i, j;
        Sudoku tmpsudoku = new Sudoku(size, hDiv);

        for(i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                tmpsudoku.setCellValue(cell[i][j].getValueAsInt(), i, j);
            }
        }
        tmpsudoku = tmpsudoku.getSolution();
        for(i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                if(cell[i][j].getValueAsInt() != tmpsudoku.getCellValue(i, j)) {
                    cell[i][j].replaceValue(tmpsudoku.getCellValue(i, j));
                }
            }
        }
        actionStack.clear();
    }

    private void write(PrintWriter out) {
        int i, j;
        out.print(size + "," + hDiv);
        for(i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                out.print("," + cell[i][j].getValueAsInt() + "," + (cell[i][j].isValueFixed()?1:0));
            }
        }
    }

    public void save(File file) {
        try {
            PrintWriter out = new PrintWriter(file);
            this.write(out);
            out.close();
        }
        catch(FileNotFoundException fne) {
            System.out.println("FileNotFoundException: " + fne);
        }
        catch(IOException ioe) {
            System.out.println("IOException: " + ioe);
        }
    }

    public boolean loadFrom(File file) {
        try {
            int i, j;
            Scanner sc = new Scanner(file);
            sc.useDelimiter(",");
            int size = sc.nextInt();
            int hDiv = sc.nextInt();
            this.loadSudoku(new Sudoku(size, hDiv));
            for(i=0; i < size; i++) {
               for(j=0; j < size; j++) {
                   int value = sc.nextInt();
                   int isFixed = sc.nextInt();
                   if(value != 0) {
                       if(isFixed == 1)
                           cell[i][j].setFixedValue(value);
                       else
                           cell[i][j].setValue(value);
                   }
               }
            }
            actionStack.clear();
            sc.close();
        }
        catch(IOException e) {
            return false;
        }
        return true;
    }

    public void reset() {
        int i, j;
        for(i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                if(!cell[i][j].isValueFixed())
                    cell[i][j].clearValue();
            }
        }
        actionStack.clear();
    }

    public void setAutomark(boolean automark) {
        if(this.automark == automark)
            return;
        
        this.automark = automark;
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                cell[i][j].setAutomark(automark);
            }
        }
    }

    public boolean isAutomarked() {
        return automark;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private int size;
    private int hDiv, vDiv;
    private CellPanel[][] cell;
    private int fillCount;
    private ActionStack actionStack;
    private JPanel[][] box;
    private Dimension dimension;
    private boolean automark;
    //private Sudoku sudoku;

    private static final Color boxColor_1 = new java.awt.Color(217, 237, 233);
    private static final Color boxColor_2 = Color.WHITE;

    public static void main(String[] args) {
        Sudoku sudoku = SudokuGenerator.getRandomizedSudoku(9, 3, Sudoku.DL_EASY);
        sudoku.print();
        SudokuPanel sudokuPanel = new SudokuPanel(sudoku);//new SudokuPanel(6, 3);
        //sudokuPanel.loadFrom(new File("IO/output/save_1.txt"));
        javax.swing.JFrame frame = new javax.swing.JFrame("SudokuPanel demo");
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(sudokuPanel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                //super.windowClosing(e);
                System.exit(0);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                //super.windowClosing(e);
                System.exit(0);
            }
        });
  //      frame.pack();
        frame.setSize(700, 700);
        frame.setVisible(true);
        //sudokuPanel.save(new File("IO/output/save_1.txt"));
        sudokuPanel.solve();
        sudokuPanel.reset();
        sudokuPanel.setAutomark(true);
    }
}

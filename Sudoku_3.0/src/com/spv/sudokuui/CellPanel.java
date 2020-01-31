/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CellPanel.java
 *
 * Created on Mar 31, 2009, 11:37:23 PM
 */

package com.spv.sudokuui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.BevelBorder;
/**
 *
 * @author Administrator
 */
public class CellPanel extends javax.swing.JPanel {

    /** Creates new form CellPanel */
    public CellPanel(SudokuPanel parent, int row, int col) {
        this(parent, row, col, 9);
    }

    public CellPanel(SudokuPanel parent, int row, int col, int size) {
        this(parent, row, col, size, null, CellPanel.EDITABLE_VALUE);
    }

    public CellPanel(SudokuPanel parent, int row, int col, int size, String text) {
        this(parent, row, col, size, text, CellPanel.EDITABLE_VALUE);
    }

    public CellPanel(SudokuPanel parent, int row, int col, int size, String text, int valueType) {
        initComponents();

        this.size = size;
        this.parent = parent;
        this.type = valueType;
        this.row = row;
        this.col = col;
        this.automark = false;
        int gridSize = (int)(Math.ceil(Math.sqrt(size)));

        valuePanel.setLayout(new BorderLayout());
        valueLabel = new JLabel("");
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setVerticalAlignment(JLabel.CENTER);
        valueLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3 && type == CellPanel.EDITABLE_VALUE) {
                    clearValue();
                }
            }
        });
        valuePanel.add(valueLabel);

        if(type == CellPanel.FIXED_VALUE) {
            this.setFixedValue(text);
        }
        else {
            valuesPanel.setLayout(new java.awt.GridLayout(gridSize, gridSize));
            valuesLabel = new javax.swing.JLabel[size];
            validValues = new boolean[size];
            for(int i=0; i < size; i++) {
                valuesLabel[i] = new javax.swing.JLabel(String.valueOf(i+1));
                validValues[i] = true;
                valuesLabel[i].setHorizontalAlignment(JLabel.CENTER);
                valuesLabel[i].setVerticalAlignment(JLabel.CENTER);
                valuesLabel[i].setFont(valuesNFocusFont);
                valuesLabel[i].setForeground(valuesNFocusColor);
                valuesLabel[i].setBackground(Color.YELLOW);
                valuesLabel[i].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JLabel label = (JLabel)e.getComponent();
                        setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                        if(label.getText().equals(""))
                            return;
                        label.setFont(valuesFocusFont);
                        label.setForeground(valuesFocusColor);
                        label.setOpaque(true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        JLabel label = (JLabel)e.getComponent();
                        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                        if(label.getText().equals(""))
                            return;
                        label.setFont(valuesNFocusFont);
                        label.setForeground(valuesNFocusColor);
                        label.setOpaque(false);
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel label = (JLabel)e.getComponent();
                        setValue(label.getText());
                    }
                });

                valuesPanel.add(valuesLabel[i]);
            }
        }
        if(text != null) {
            this.setValue(text);
        }

        /*this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
        });*/
        dimension = new Dimension(valuesLabelSize*gridSize, valuesLabelSize*gridSize);
        this.setSize(dimension);
    }

    public void setValue(String text) {
        if(text == null || text.equals(""))
            return;
        this.type = CellPanel.EDITABLE_VALUE;
        valueLabel.setText(text);
        valueLabel.setFont(valueFont);
        valueLabel.setForeground(valueColor);
        valueLabel.setToolTipText("Right click to erase");
        CardLayout layout = (CardLayout)this.getLayout();
        layout.show(this, "valueCard");
        parent.cellValudeDefined(this);
    }

    public void setValue(Integer value) {
        this.setValue(String.valueOf(value));
    }

    public void clearValue() {
        int value = this.getValueAsInt();
        valueLabel.setText("");
        CardLayout layout = (CardLayout)this.getLayout();
        layout.show(this, "valuesCard");
        parent.cellValueRemoved(this, value);
    }

    public void setFixedValue(String text) {
        this.type = CellPanel.FIXED_VALUE;
        valueLabel.setText(text);
        valueLabel.setFont(fixedValueFont);
        valueLabel.setForeground(fixedValueColor);
        valueLabel.setToolTipText("");
        CardLayout layout = (CardLayout)this.getLayout();
        layout.show(this, "valueCard");
        parent.cellValudeDefined(this);
    }

    public void setFixedValue(Integer value) {
        this.setFixedValue(String.valueOf(value));
    }

    public void replaceValue(String text) {
        this.type = CellPanel.REPLACED_VALUE;
        valueLabel.setText(text);
        valueLabel.setForeground(replacedValueColor);
        valueLabel.setToolTipText("");
        CardLayout layout = (CardLayout)this.getLayout();
        layout.show(this, "valueCard");
        parent.cellValudeDefined(this);
    }

    public void replaceValue(Integer value) {
        this.replaceValue(String.valueOf(value));
    }
    
    public String getValue() {
        return valueLabel.getText();
    }

    public int getValueAsInt() {
        if(valueLabel.getText().equals(""))
            return 0;
        return Integer.parseInt(valueLabel.getText());
    }
    
    public boolean isValueFixed() {
        return (type == CellPanel.FIXED_VALUE);
    }

    public boolean isValueReplaced() {
        return (type == CellPanel.REPLACED_VALUE);
    }

    public boolean isValueEditable() {
        return (type == CellPanel.EDITABLE_VALUE);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public boolean isValidValue(int value) {
        if(value > size || value < 1)
            return false;
        return validValues[value - 1];
    }

    public boolean removeValidValue(int value) {
        if(isValidValue(value)) {
            validValues[value - 1] = false;
            refreshValue(value);
            return true;
        }
        return false;
    }

    public boolean addValidValue(int value) {
            //System.out.println("Adding: " + row + ", " + col + ", " + value);
        if(value > size || value < 1 || validValues[value - 1])
            return false;
        if(parent.isValidValue(this, value)) {
            validValues[value - 1] = true;
            refreshValue(value);
            return true;
        }
        return false;
    }

    public void setAutomark(boolean automark) {
        this.automark = automark;
        refreshValues();
    }

    public boolean isAutomarked() {
        return automark;
    }

    public void refreshValues() {
        for(int i=0; i < size; i++) {
            if(automark) {
                if(validValues[i])
                    valuesLabel[i].setText(String.valueOf(i + 1));
                else
                    valuesLabel[i].setText("");
            }
            else {
                valuesLabel[i].setText(String.valueOf(i + 1));
            }
        }
    }

    public void refreshValue(int value) {
        if(automark) {
            if(validValues[value - 1])
                valuesLabel[value - 1].setText(String.valueOf(value));
            else
                valuesLabel[value - 1].setText("");
        }
        else {
            valuesLabel[value - 1].setText(String.valueOf(value));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        valuesPanel = new javax.swing.JPanel();
        valuePanel = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setOpaque(false);
        setLayout(new java.awt.CardLayout());

        valuesPanel.setBackground(new java.awt.Color(255, 255, 255));
        valuesPanel.setOpaque(false);
        valuesPanel.setLayout(null);
        add(valuesPanel, "valuesCard");

        valuePanel.setBackground(new java.awt.Color(255, 255, 255));
        valuePanel.setOpaque(false);
        valuePanel.setLayout(null);
        add(valuePanel, "valueCard");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel valuePanel;
    private javax.swing.JPanel valuesPanel;
    // End of variables declaration//GEN-END:variables

    //others
    private int size;
    private javax.swing.JLabel[] valuesLabel;
    private javax.swing.JLabel valueLabel;
    private int type;
    private Dimension dimension;
    private SudokuPanel parent;
    private int row, col;
    private boolean[] validValues;
    private boolean automark;

    private static final Color valuesNFocusColor = new java.awt.Color(204, 204, 204);
    private static final Color valuesFocusColor = Color.BLUE;
    private static final Color valueColor = Color.BLUE;
    private static final Font valuesNFocusFont = new java.awt.Font("Tahoma", Font.PLAIN, 10);
    private static final Font valuesFocusFont = new java.awt.Font("Tahoma", Font.PLAIN, 10);
    private static final Font valueFont = new java.awt.Font("Tahoma", Font.BOLD, 24);
    private static final Font fixedValueFont = new java.awt.Font("Tahoma", Font.BOLD, 25);
    private static final Color fixedValueColor = Color.BLACK;
    private static final Color replacedValueColor = Color.RED;
    private static final int valuesLabelSize = 14;

    public static final int EDITABLE_VALUE = 0;
    public static final int FIXED_VALUE = 1;
    public static final int REPLACED_VALUE = 2;

    public static void main(String[] args) {
        CellPanel cellPanel = new CellPanel(new SudokuPanel(), 0, 0, 9, "8", CellPanel.EDITABLE_VALUE);
        javax.swing.JFrame frame = new javax.swing.JFrame("CellPanel demo");
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(cellPanel);
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
        frame.setSize(100, 100);
        frame.setVisible(true);
    }
}

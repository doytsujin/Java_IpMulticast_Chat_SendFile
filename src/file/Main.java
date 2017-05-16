/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
 
public class Main {
 
    public static void main(String[] args) {
        final JFrame frame = new JFrame("JFileChooser Demo");
 
        final JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setCurrentDirectory(new File("C:\\tmp"));
 
        JButton btn1 = new JButton("Show Dialog");
        btn1.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                fc.showDialog(frame, "Choose");
            }
        });
        JButton btn2 = new JButton("Show Open Dialog");
        btn2.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                int retVal = fc.showOpenDialog(frame);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    File[] selectedfiles = fc.getSelectedFiles();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedfiles.length; i++) {
                        sb.append(selectedfiles[i].getName() + "\n");
                        System.out.println(selectedfiles[i].getPath());
                    }
                    JOptionPane.showMessageDialog(frame, sb.toString());
                }
 
            }
        });
        JButton btn3 = new JButton("Show Save Dialog");
        btn3.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                fc.showSaveDialog(frame);
 
            }
        });
 
        Container pane = frame.getContentPane();
        pane.setLayout(new GridLayout(3, 1, 10, 10));
        pane.add(btn1);
        pane.add(btn2);
        pane.add(btn3);
 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}
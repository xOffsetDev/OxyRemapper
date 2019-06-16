package it.xoffset;

import it.xoffset.utils.TransformerManager;

import java.awt.*;
import java.awt.event.*;
import java.util.jar.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main
{
    private JFrame main;
    private JTextField input;
    private JTextField output;

    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            final Main window = new Main();
            window.main.setVisible(true);
        } catch (Throwable e) { }
    }

    public Main() {
        this.initialize();
    }

    private void initialize() {
        this.main = new JFrame();
        this.main.setResizable(false);
        this.main.setTitle("OxyRemapper");
        this.main.setBounds(100, 100, 296, 154);
        this.main.setDefaultCloseOperation(3);
        this.main.getContentPane().setLayout(null);
        (this.input = new JTextField()).setBounds(10, 25, 238, 20);
        this.main.getContentPane().add(this.input);
        this.input.setColumns(10);
        final JLabel lblInput = new JLabel("Input:");
        lblInput.setBounds(12, 8, 46, 14);
        this.main.getContentPane().add(lblInput);
        (this.output = new JTextField()).setColumns(10);
        this.output.setBounds(10, 65, 238, 20);
        this.main.getContentPane().add(this.output);
        final JLabel lblOutput = new JLabel("Output");
        lblOutput.setBounds(12, 48, 46, 14);
        this.main.getContentPane().add(lblOutput);
        final JButton btnin = new JButton("...");
        btnin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("Jar File", "jar");
                jFileChooser.setFileFilter(filter);
                jFileChooser.showOpenDialog(null);
                input.setText(jFileChooser.getSelectedFile().getPath());
            }
        });
        btnin.setBounds(249, 24, 32, 23);
        this.main.getContentPane().add(btnin);
        final JButton btnout = new JButton("...");
        btnout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("Jar File", "jar");
                jFileChooser.setFileFilter(filter);
                jFileChooser.showOpenDialog(null);
                output.setText(jFileChooser.getSelectedFile().getPath());
            }
        });
        btnout.setBounds(249, 64, 32, 23);
        this.main.getContentPane().add(btnout);
        final JButton btnProcess = new JButton("Process");
        btnProcess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                TransformerManager transformerManager = new TransformerManager();
                transformerManager.openJar(input.getText());
                transformerManager.Remap();
                transformerManager.save(output.getText());
                JOptionPane.showMessageDialog(null, "Done!");
            }
        });
        btnProcess.setBounds(10, 96, 270, 23);
        this.main.getContentPane().add(btnProcess);
    }
}

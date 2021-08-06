/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mngr;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author MrUnknown404
 */
public class DescriptionInputFrame extends JFrame{
    private JPanel main;
    private JEditorPane editor;
    private JScrollPane scrollPane;
    private JButton acButton;
    private JButton canButton;
    
    private static String text;
    private String name = null;
    private boolean isArchieve = false;
    
    public DescriptionInputFrame(String name, String context, boolean isArchieve){
        super(name);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        setFocusableWindowState(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                toFront();
                setState(JFrame.NORMAL);
            }
        });
        
        text = context;
        this.name = name;
        this.isArchieve = isArchieve;
        createGUI(context);
    }
    
    private void createGUI(String context){
        main = new JPanel();
        main.setLayout(new BoxLayout(main,BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        main.setAlignmentX(CENTER_ALIGNMENT);
        main.setAlignmentY(CENTER_ALIGNMENT);
        
        editor = new JEditorPane();
//        editor.setContentType("text/html");
//        editor.setEditorKit(new HTMLEditorKit());
        
        acButton = new JButton("Подтвердить");
        acButton.setPreferredSize(new Dimension(120,40));
        canButton = new JButton("Отменить");
        canButton.setPreferredSize(new Dimension(120,40));
        
        editor.setText(context == null ? "":context);
        editor.setEditable(true);
        editor.setAlignmentX(CENTER_ALIGNMENT);
        editor.setSize(400,180);
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // set new text
                    text = editor.getText();
                    // .prop editing
                    propConfigure(editor.getText());
                    
                    dispose();
                }
                switch(e.getKeyCode()){
                    case KeyEvent.VK_ESCAPE -> {
                        text = context; 
                        // .prop editing
                        propConfigure(editor.getText());
                    
                        dispose();
                    }
                }
            }
        });
        
        scrollPane = new JScrollPane(editor);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(400,200));


        acButton.addActionListener((ActionEvent e) -> {
            try {
                if (TableMethods.isModifyingAvaible()) {
                    TableMethods.setModifyingAvaibleMark(false);
                    
                    text = editor.getText();
                    propConfigure(editor.getText());
                    
                    TableMethods.setModifyingAvaibleMark(true);
                    dispose();
                } else 
                    TableMethods.getAvaibleErrorPane();
            } catch (IOException ex) { 
                Logger.getLogger(DescriptionInputFrame.class.getName()).log(Level.SEVERE, null, ex);
                try { TableMethods.setModifyingAvaibleMark(true); }
                catch (IOException ex1) { 
                    Logger.getLogger(DescriptionInputFrame.class.getName()).log(Level.SEVERE, null, ex1);
                    try { TableMethods.setModifyingAvaibleMark(true); }
                    catch (IOException ex2) { Logger.getLogger(DescriptionInputFrame.class.getName()).log(Level.SEVERE, null, ex2); }
                }
            }
        });
        canButton.addActionListener((ActionEvent e) -> {
            text = context;
            dispose();
        });
        
        JPanel upper = new JPanel();
        upper.setSize(350, 200);
        upper.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JPanel down = new JPanel();
        
        upper.add(scrollPane);  
        down.add(acButton);
        down.add(canButton);
        main.add(upper);
        main.add(down);
        add(main);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void propConfigure(String newDescription){
        //configure .prop
        String pathToProperties = TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt";
        if (isArchieve)
            pathToProperties = TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt";
        ArrayList<String> propertiesText = new ArrayList<>();

        try ( FileReader fileReader = new FileReader(pathToProperties)) { // reading
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine()) {
                String currentString = scan.nextLine();
                if (currentString.contains(name)) {
                    propertiesText.add(currentString);
                    propertiesText.add(scan.nextLine());
                    if (!isArchieve)
                        propertiesText.add(scan.nextLine());
                    propertiesText.add(scan.nextLine());
                    propertiesText.add("description: " + ((newDescription == null || newDescription.equals("")) ? " " : newDescription));
                    currentString = scan.nextLine();
                    
                    // moving throught the description to the next order
                    while (!currentString.contains("id") && scan.hasNextLine()) {
                            currentString = scan.nextLine();
                    }
                    if (scan.hasNextLine()) //if not the end of .prop file -> adding id line
                        propertiesText.add(currentString);
                } else {
                    propertiesText.add(currentString);
                }
            }
            //writing
            try ( FileWriter fileWriter = new FileWriter(pathToProperties)) {
                for (String line : propertiesText) {
                    line += "\r\n";
                    fileWriter.write(line);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DescriptionInputFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DescriptionInputFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String getValue(){
        return "".equals(text)?null:text;
    }
    protected static void setValue(String text){
        DescriptionInputFrame.text = text;
    }
}

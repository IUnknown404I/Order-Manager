package mngr.orders_functional;

import mngr.TableMethods;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import mngr.MainJFrame;

/**
 * Window for creating and editing orders
 * @author MrUnknown404
 */
public class InputOrModifyOrderFrame extends JFrame {
    private JPanel mainPanel;
    private javax.swing.JLabel returnLabel;
    private javax.swing.JFormattedTextField returnTextField;
    private javax.swing.JLabel acceptLabel;
    private javax.swing.JFormattedTextField acceptTextField;
    private javax.swing.JLabel custumerLabel;
    private javax.swing.JTextField custumerTextField;
    private javax.swing.JLabel descriptionLabel;
    private JScrollPane descriptionScroll;
    private JEditorPane descriptionEditor;
    private javax.swing.JPanel upperPanel;
    private javax.swing.JPanel downPanel;
    private javax.swing.JPanel buttonPanel;
    private JButton acceptButton;
    private JButton canselButton;
    private javax.swing.JLabel acceptButLabel;
    private boolean isAcceptDateValid = false;
    private boolean isReturnDateValid = false;
    private boolean isArchieveTab = false;
    public boolean isCurrentModifyingUser = false;
    
    /**
     * Displayed the window for adding an entry to the Main Table
     * @param frame
     * @param tab the JTabel for adding an order
     */
    public InputOrModifyOrderFrame(MainJFrame frame, JTable tab){
        super("Добавление записи");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.updateInfo();
                dispose();
            }
        });
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setFocusableWindowState(true);
        
        tab.clearSelection();
        createGUI(null, tab, -1);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    /**
     * Displayed the window for adding an entry to the Archieve Table
     * @param frame
     * @param tab the JTabel for adding an order
     * @param isArchieveTab  the boolean, must be true for this constructor
     */
    public InputOrModifyOrderFrame(MainJFrame frame, JTable tab, boolean isArchieveTab){
        super("Добавление записи в архив");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.updateInfo();
                dispose();
            }
        });
        
        this.isArchieveTab = isArchieveTab;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setFocusableWindowState(true);
        
        tab.clearSelection();
        createGUI(null, tab, -1);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    /**
     * Displayed the window for modifying selected order from the Main Table
     * @param frame
     * @param tab Main JTable
     * @param row selected row
     */
    public InputOrModifyOrderFrame(MainJFrame frame, JTable tab, int row){
        super("Редактирование записи");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    frame.updateInfo();
                    TableMethods.setModifyingAvaibleMark(true);
                    dispose();
                } catch (IOException ex) { Logger.getLogger(InputOrModifyOrderFrame.class.getName()).log(Level.SEVERE, null, ex); }
            }
        });
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setFocusableWindowState(true);
        
        createGUI(frame, tab, row);
        fillingAreas(tab, row);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void createGUI(MainJFrame frame, JTable tab, int row){
        // initialize
        mainPanel = new JPanel();
//        mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        mainPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        upperPanel = new javax.swing.JPanel();
        upperPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
//        upperPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        downPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        acceptLabel = new javax.swing.JLabel();
        if(isArchieveTab)
            archieveAcceptTextFieldConfig();
        else {
            acceptTextField = new javax.swing.JFormattedTextField(getFormatter(true));
            acceptTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    String[] dateSubString = acceptTextField.getText().split("\\.");

                    if (Integer.parseInt(dateSubString[1]) > 12 || Integer.parseInt(dateSubString[2]) > 31 ||
                        Integer.parseInt(dateSubString[1]) == 0 || Integer.parseInt(dateSubString[2]) == 0 ||
                                          Integer.parseInt(dateSubString[0]) == 0) {
                        isAcceptDateValid = false;
                        acceptButton.setEnabled(false);
                        acceptButLabel.setVisible(true);
                    } else {
                        isAcceptDateValid = true;
                        if (isAcceptDateValid && isReturnDateValid) {
                            acceptButton.setEnabled(true);
                            acceptButLabel.setVisible(false);
                        }
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
        }
        acceptTextField.setColumns(10);
        returnLabel = new javax.swing.JLabel();
        returnTextField = new javax.swing.JFormattedTextField(getFormatter(false));
        returnTextField.setColumns(10);
        returnTextField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                String[] dateSubString = returnTextField.getText().split("\\.");
                
                if(Integer.parseInt(dateSubString[1])>12 || Integer.parseInt(dateSubString[2])>31 ||
                   Integer.parseInt(dateSubString[1])==0 || Integer.parseInt(dateSubString[2])==0) {
                    isReturnDateValid = false;
                    acceptButton.setEnabled(false);
                    acceptButLabel.setVisible(true);
                } else {
                    isReturnDateValid = true;
                    if (isAcceptDateValid && isReturnDateValid) {
                        acceptButton.setEnabled(true);
                        acceptButLabel.setVisible(false);
                    }
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {   }
            @Override
            public void changedUpdate(DocumentEvent e) {   }
        });
        custumerLabel = new javax.swing.JLabel();
        custumerTextField = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();
        acceptButton = new JButton();
        canselButton = new JButton();
        acceptButLabel = new JLabel("Проверьте правильность ввода даты!");

        descriptionEditor = new JEditorPane();
        descriptionEditor.setEditable(true);
//        descriptionEditor.setDocument(new MyDoc());
        descriptionEditor.setToolTipText("Описание");
//        descriptionEditor.setContentType("html\text");
        
        descriptionScroll = new JScrollPane(descriptionEditor);
        descriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        descriptionScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        descriptionScroll.setPreferredSize(new Dimension(430,125));
        descriptionScroll.setRequestFocusEnabled(true);

        returnLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        returnLabel.setText("Срок сдачи");
        returnLabel.setFont(new java.awt.Font("Tahoma", 1, 12));

        returnTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        returnTextField.setToolTipText("Срок сдачи");

        acceptLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        acceptLabel.setText("Дата получения");
        acceptLabel.setFont(new java.awt.Font("Tahoma", 1, 12));

        acceptTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        acceptTextField.setToolTipText("Дата получения");

        custumerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        custumerLabel.setText("Заказчик");
        custumerLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        
        custumerTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        custumerTextField.setToolTipText("Заказчик");

        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setText("Описание"); //200 40
        descriptionLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        
        
        acceptButton.setText("Подтвердить");
        acceptButton.setPreferredSize(new Dimension(120,50));
        acceptButton.setFont(new java.awt.Font("Tahoma", 0, 13));
        canselButton.setText("Отменить");
        canselButton.setPreferredSize(new Dimension(120,50));
        canselButton.setFont(new java.awt.Font("Tahoma", 0, 13));
        
        acceptButLabel.setForeground(new java.awt.Color(204, 0, 0));
        acceptButLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        acceptButLabel.setVisible(false);
        
        upperPanel.setSize(400,400);
        downPanel.setSize(400,400);
        buttonPanel.setSize(150,50);
        
        // buttons config
        canselButton.addActionListener((ActionEvent e) -> { 
                if (frame != null)
                    try { TableMethods.setModifyingAvaibleMark(true); }
                    catch (IOException ex) { Logger.getLogger(InputOrModifyOrderFrame.class.getName()).log(Level.SEVERE, null, ex); }
                dispose(); 
                tab.clearSelection(); 
        });
        if(row<0) { // input
            acceptButton.addActionListener((ActionEvent e) -> {
                Path pathString = null;
                DefaultTableModel model = (DefaultTableModel) tab.getModel();
                if (custumerTextField.getText().equals(""))
                    custumerTextField.setText("None");
                
                if (isArchieveTab) { // for Archieve
                    pathString = Path.of(TableMethods.getRootPath().toString() + "\\АРХИВ",
                                        returnTextField.getText() +" "+ TableMethods.toValidNameFile(custumerTextField.getText()));
                    
                    if (Files.exists(pathString)) {
                        TableMethods.getAlreadyExistsOrderPane();
                    } 
                    else {
                        try {
                            Files.createDirectory(pathString);
                            creatingPropertyUpdate(isArchieveTab);
                            model.addRow(new Object[]{returnTextField.getText(), custumerTextField.getText(), descriptionEditor.getText(), null});
                        } catch (IOException ex) {
                            Logger.getLogger(InputOrModifyOrderFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {// for Main content
                    pathString = Path.of(TableMethods.getRootPath().toString() + "\\Текущие заказы",
                                        returnTextField.getText() +" "+ TableMethods.toValidNameFile(custumerTextField.getText()));
                    
                    if (Files.exists(pathString)){
                        TableMethods.getAlreadyExistsOrderPane();
                    } 
                    else {
                        try {
                            Files.createDirectory(pathString);
                            creatingPropertyUpdate(isArchieveTab);
                            model.addRow(new Object[]{returnTextField.getText(), acceptTextField.getText(), custumerTextField.getText(), descriptionEditor.getText(), null});
                        } catch (IOException ex) { Logger.getLogger(InputOrModifyOrderFrame.class.getName()).log(Level.SEVERE, null, ex); }
                    }
                }
                
                tab.clearSelection();
                dispose();
            });
        }
        else { // modify
            if (isArchieveTab) {
                TableMethods.getArchiveModifyErrorPane();
            } else
                acceptButton.addActionListener((ActionEvent e) -> {
                    try {
                        //check avaibility
                        if (!TableMethods.isModifyingAvaible()) {
                            TableMethods.getAvaibleErrorPane();
                            return;
                        }

                        TableMethods.setModifyingAvaibleMark(false);
                        isCurrentModifyingUser = true;

                        DefaultTableModel model = (DefaultTableModel) tab.getModel();
                        if (custumerTextField.getText() == null || custumerTextField.getText().equals("")) {
                            custumerTextField.setText("None");
                        }

                        // prop conf
                        propertyConfigure(model.getValueAt(row, 0) + " " + model.getValueAt(row, 2), new String[]{returnTextField.getText() + " " + custumerTextField.getText(), //id
                            returnTextField.getText(), //return date
                            acceptTextField.getText(), //accept date
                            custumerTextField.getText(), //custumer
                            descriptionEditor.getText()}); //description

                        // dir conf
                        try {
                            Path oldPathToFile = Path.of(TableMethods.getRootPath().toString() + "\\Текущие заказы", model.getValueAt(row, 0) + " " + model.getValueAt(row, 2));
                            Path actualPathToFile = Path.of(TableMethods.getRootPath().toString() + "\\Текущие заказы", returnTextField.getText() + " " + custumerTextField.getText());

                            if (oldPathToFile.compareTo(actualPathToFile) == 0) {
                                propertyConfigure(model.getValueAt(row, 0) + " " + model.getValueAt(row, 2), new String[]{returnTextField.getText() + " " + custumerTextField.getText(), //id
                                    returnTextField.getText(), //return date
                                    acceptTextField.getText(), //accept date
                                    custumerTextField.getText(), //custumer
                                    descriptionEditor.getText()}); //description
                            } else {
                                Files.walkFileTree(oldPathToFile, new DirectoryMoveWithDeletingVisitor(oldPathToFile, actualPathToFile));
                            }
                        } catch (IOException io) { Logger.getLogger(InputOrModifyOrderFrame.class.getName()).log(Level.SEVERE, null, io); }

                        // config the Tab
                        model.setValueAt(returnTextField.getText(), row, 0);
                        model.setValueAt(acceptTextField.getText(), row, 1);
                        model.setValueAt(custumerTextField.getText(), row, 2);
                        model.setValueAt(descriptionEditor.getText(), row, 3);

                        tab.clearSelection();
                        TableMethods.setModifyingAvaibleMark(true);
                        dispose();
                    } 
                    catch (IOException ex) {
                        if (isCurrentModifyingUser)
                            isCurrentModifyingUser = false;
                        Logger.getLogger(InputOrModifyOrderFrame.class.getName()).log(Level.SEVERE, null, ex);
                        try { TableMethods.setModifyingAvaibleMark(true); }
                        catch (IOException ioe) { Logger.getLogger(InputOrModifyOrderFrame.class.getName()).log(Level.SEVERE, null, ex); }
                    }
                });
        }
        
        // layouts config
        // the Panel with 0-2 columns
        javax.swing.GroupLayout upperPanelLayout = new javax.swing.GroupLayout(upperPanel);
        upperPanel.setLayout(upperPanelLayout);
        upperPanelLayout.setHorizontalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(returnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(acceptLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(custumerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(returnTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(acceptTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(custumerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        upperPanelLayout.setVerticalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, upperPanelLayout.createSequentialGroup()
//                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acceptLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(custumerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acceptTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(custumerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        
        // the Panel with description
        javax.swing.GroupLayout downPanelLayout = new javax.swing.GroupLayout(downPanel);
        downPanel.setLayout(downPanelLayout);
        downPanelLayout.setHorizontalGroup(
            downPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, downPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(descriptionScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(downPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        downPanelLayout.setVerticalGroup(
            downPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, downPanelLayout.createSequentialGroup()
                .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        
        // the Panel with buttons
        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(acceptButLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(acceptButton, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addGap(60, 60, 60)
                .addComponent(canselButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
//                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buttonPanelLayout.createSequentialGroup()
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(acceptButLabel))
                    .addComponent(canselButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        
        //  the main Panel
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(upperPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(downPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
//                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(upperPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        
//        mainPanel.add(upperPanel);
//        mainPanel.add(downPanel);
//        mainPanel.add(buttonPanel);
//        add(mainPanel);
    }
    /**
     * Filling in the fields of the data entry frame
     * @param tab current JTable 
     * @param row current row of the JTable
     */
    private void fillingAreas(JTable tab, int row){
                DefaultTableModel model = (DefaultTableModel) tab.getModel();
                
                returnTextField.setText(TableMethods.getFromattedData((String)model.getValueAt(row, 0)));
                acceptTextField.setText(TableMethods.getFromattedData((String)model.getValueAt(row, 1)));
                custumerTextField.setText((String)model.getValueAt(row, 2));
                descriptionEditor.setText((String)model.getValueAt(row, 3));
    }
    /**
     * Getting a formatter for fields with a date
     * @return a MaskFormatter for date
     */
    private MaskFormatter getFormatter(boolean isdefaultMaskUse){
        try {
            MaskFormatter dateFormatter = null;
            String timeStampYear = (new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) ).split("-")[0];
            
            if(isdefaultMaskUse) {
                StringBuilder builder = new StringBuilder(timeStampYear);
                builder = builder.replace(builder.length()-2, builder.length(), ""); //YYY# mask for year is ready
                
                dateFormatter = new MaskFormatter(builder.toString() + "##.##.##");
            }
            else {
                StringBuilder builder = new StringBuilder(timeStampYear);
                builder = builder.replace(builder.length()-1, builder.length(), ""); //YYY# mask for year is ready
                
                dateFormatter = new MaskFormatter(builder.toString() + "#.##.##");
            }
            
            dateFormatter.setPlaceholderCharacter('0');
            return dateFormatter;
            
        } catch (ParseException ignored) { return null; }
    }
    /**
     * Setting up the text field for entering the order receipt date
     * for the archive panel
     */
    private void archieveAcceptTextFieldConfig(){
        try {
            MaskFormatter dateFormatter = new MaskFormatter("НЕДОСТУПНО");
            dateFormatter.setPlaceholderCharacter('0');
            dateFormatter.setMask("НЕДОСТУПНО");
            
            acceptTextField = new JFormattedTextField(dateFormatter);
            acceptTextField.setEditable(false);
            isAcceptDateValid = true;
        } catch (ParseException ignored) {  }
    }
    
    /**
     * Updating configuration files when adding
     * @param isArchieve
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void creatingPropertyUpdate(boolean isArchieve) throws FileNotFoundException, IOException {
        String pathToProperties = TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt";
        ArrayList<String> propertiesText = new ArrayList<>();
        
        propertiesText.add("id: "          + returnTextField.getText() +" "+ custumerTextField.getText() +"\r\n");
        propertiesText.add("return date: " + returnTextField.getText() +"\r\n");
        if(!isArchieve) {
            propertiesText.add("accept date: " + acceptTextField.getText() +"\r\n");
            pathToProperties = TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt";
        }
        propertiesText.add("customer: "    + custumerTextField.getText() +"\r\n");
        propertiesText.add("description: " + descriptionEditor.getText() +"\r\n");
        
        try (FileWriter fileWriter = new FileWriter(pathToProperties, true)) {
            for (String line:propertiesText) {
                fileWriter.write(line);
            }
        }
    }
    
    /**
     * Updating configuration files when configuring
     * @param id
     * @param properties
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void propertyConfigure(String id, String[] properties) throws FileNotFoundException, IOException {
        // .properties conf
        String pathToProperties = TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt";
        ArrayList<String> propertiesText = new ArrayList<>();
        // reading
        try (FileReader fileReader = new FileReader(pathToProperties)) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine()) {
                String currentString = scan.nextLine();
                if (currentString.contains(id)) {
                    propertiesText.add("id: " + properties[0]);
                    propertiesText.add("return date: " + properties[1]);
                    propertiesText.add("accept date: " + properties[2]);
                    propertiesText.add("customer: " + properties[3]);
                    propertiesText.add("description: " + properties[4]);
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
        }
            //writing
            try ( FileWriter fileWriter = new FileWriter(pathToProperties)) {
                for (String line: propertiesText) {
                    line += "\r\n";
                    fileWriter.write(line);
                }
            }
    }
}

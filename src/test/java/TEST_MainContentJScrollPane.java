/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import mngr.renders_and_editors.DescriptionColumnEditor;
import mngr.renders_and_editors.ButtonRenderer;
import mngr.renders_and_editors.MainTableRenderer;
import mngr.renders_and_editors.ButtonEditor;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MrUnknown404
 */
class TEST_MainContentJScrollPane {
    private TEST_MainContentJScrollPane() {
    }
    
    private JTable mainJTable;
    private JScrollPane scroll;

    protected JScrollPane getJScrollPane(){
        String[]   tabHeaders = {"Срок сдачи", "Дата поступления", "Заказчик", "Описание", "Директория"};
        Object[][] tabContent = {
            {"01.07.2020", "10.06.1998", "Kolyan", "Mngr", null},
            {"25.07.2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"23.07 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"2 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 07 2022", "10.06.1998", "Kolyan", "Mngr", null},
        
            
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},
            {"28 08 2021", "10.06.1998", "Kolyan", "Mngr", null},};
        
        DefaultTableModel defTabModel = new DefaultTableModel();
        defTabModel.setDataVector(tabContent, tabHeaders);
        mainJTable = new JTable();
        mainJTable.setModel(defTabModel);
        mainJTable.setAutoCreateRowSorter(true);
        
        mainJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        mainJTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        mainJTable.getTableHeader().setReorderingAllowed(false);
        mainJTable.setDoubleBuffered(true);
        mainJTable.setAutoscrolls(true);
        mainJTable.setFillsViewportHeight(true);
        mainJTable.setShowGrid(true);
        mainJTable.setRowHeight(25);
        
        ((DefaultTableCellRenderer)mainJTable.getTableHeader().getDefaultRenderer())
                                        .setHorizontalAlignment(JLabel.CENTER);
        
        
        mainJTable.setDefaultRenderer(Object.class, new MainTableRenderer());
        mainJTable.getColumn("Директория").setCellRenderer(new ButtonRenderer());
        mainJTable.getColumn("Директория").setCellEditor(new ButtonEditor(new JCheckBox()));
        mainJTable.getColumn("Описание").setCellEditor(new DescriptionColumnEditor(new JTextField()));
        
        scroll = new JScrollPane(mainJTable);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        return scroll;
    }
    
    
}

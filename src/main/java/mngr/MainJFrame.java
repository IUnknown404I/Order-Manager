package mngr;

import mngr.renders_and_editors.ButtonEditor;
import mngr.renders_and_editors.DescriptionColumnEditor;
import mngr.renders_and_editors.ButtonRenderer;
import mngr.renders_and_editors.MainTableRenderer;
import mngr.renders_and_editors.ArchiveTableRender;
import mngr.renders_and_editors.ArchiveTableButtonRender;
import mngr.orders_functional.InputOrModifyOrderFrame;
import mngr.orders_functional.DirectoryMoveWithDeletingVisitor;
import mngr.orders_functional.DirectoryCopyVisitor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystemException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author MrUnknown404
 */
public class MainJFrame extends javax.swing.JFrame implements Serializable {
    public MainJFrame() throws IOException {
        try {
            // check for valid rootPath and db existing
            try {
                TableMethods.loadConfig();
            } catch (NoSuchFileException | FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "        В директории по корневому пути не найдены все нужныу системныу файлы!\n"
                            + "Восстановите их перед началом работы или укажите новый корневой путь для работы!","Системное уведомление", JOptionPane.ERROR_MESSAGE);
        }
            
            initComponents();
            initOtherComponents();
            DataFilling.tableFilling(mainJTable, archieveJTable);
            updateInfo();
            
            // check for om settings existing
            TableMethods.loadConfig();
        } catch (NoSuchFileException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "        В директории по корневому пути не найдено всех нужных системных файлов!\n"
                            + "Восстановите их перед началом работы или укажите новый корневой путь для работы!","Системное уведомление", JOptionPane.ERROR_MESSAGE);
        }
        } catch (NoSuchFileException | FileNotFoundException ex) { Logger.getLogger(TableMethods.class.getName()).log(Level.SEVERE, null, ex); }

    }
    private void initOtherComponents(){
        infoTextArea.setFont(new Font(inputRecord.getFont().getName(),Font.PLAIN, 13));
        
        setMainTable();
        setArhieveTable();
        buttonsConfig();
        tabbedPaneConfig();
        updateInfo();
        menuConfig();
    }

    private JTable mainJTable;
    private JTable archieveJTable;
    private String newPath = null;

    // заполнение и дезигн таблицы, вкладываем её в скролл и  его добавляем к палетке
    private void setMainTable(){
        // инициирую и настраиваю таблицу
        mainJTable = new JTable();
        mainJTable.setName("MainTable");
        mainJTable.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
//                mainJTable.clearSelection();
            }
            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        mainJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
//            {"2021.07.26", "1998.06.10", "ООО \"Красный ООО\"", "Поставка в Владивосток, разговаривать с тем-то, делать то-то, да и вообще в описании побольше писать", null},
//            {"2021.08.28", "1998.06.10", "Иван степаныч", "Это работа\nИвана\nСтепаныча", null},
//            {"2021.07.29", "1998.06.10", "Первый дед", "Это работа\nПервого\nдеда", null},
//            {"2021.07.30", "1998.06.10", "Второй дед", "this is demo presentation", null},
//            {"2021.08.03", "1998.06.10", "Дед третий", "Mngr", null},
//            {"2021.08.05", "1998.06.10", "Красный куб", "Mngr", null},
//            {"2022 08 06", "1998.06.10", "Акула", "###################\n" +
//"TEST###################", null},
//            {"2022 08 24", "1998.06.10", "Лента", "Mngr", null},
//            {"2022 08 29", "1998.06.10", "Владивосток", "Mngr", null},
//            {"2022 08 15", "1998.06.10", "Sheeeesh", "\nDo u know what does it's mean, brah?\n", null},
//            {"2022 08 01", "1998.06.10", "Kasha", "This is Kasha maaan", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
//            {"2022 08 31", "1998.06.10", "Kolyan", "Mngr", null},
            },
            new String [] {"Срок сдачи", "Поступление", "Заказчик", "Описание", "Директория"}) {

            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };
            @Override
            public Class getColumnClass(int columnIndex) {
                if (columnIndex==0 || columnIndex==1)
                    return Integer.class;
                else
                    return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        // задаём сортировку таблицы
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(mainJTable.getModel());
        sorter.setSortable(4, false);
        sorter.setComparator(0, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
//        sorter.toggleSortOrder(0); 
        sorter.setSortsOnUpdates(true);
        mainJTable.setRowSorter(sorter);
        
//        mainJTable.setAutoCreateRowSorter(true);
        mainJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        mainJTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        mainJTable.getTableHeader().setReorderingAllowed(false);
        mainJTable.setDoubleBuffered(true);
        mainJTable.setAutoscrolls(true);
        mainJTable.setFillsViewportHeight(true);
        mainJTable.setShowGrid(true);
        mainJTable.setRowHeight(30);
        ((DefaultTableCellRenderer)mainJTable.getTableHeader().getDefaultRenderer())
                                        .setHorizontalAlignment(JLabel.CENTER);

        // задаю рендеры и едиторы
        mainJTable.setDefaultRenderer(Object.class, new MainTableRenderer());
        mainJTable.getColumn("Директория").setCellRenderer(new ButtonRenderer());
        mainJTable.getColumn("Директория").setCellEditor(new ButtonEditor(new JCheckBox()));
        mainJTable.getColumn("Описание").setCellEditor(new DescriptionColumnEditor(new JTextField()));

        // настраиваю ширину столбцов
        TableColumn column = mainJTable.getColumnModel().getColumn(4);
        column.setMaxWidth(100);
        column.setMinWidth(60);
        column = mainJTable.getColumnModel().getColumn(3);
        column.setMaxWidth(400);
        column.setMinWidth(300);
        column = mainJTable.getColumnModel().getColumn(0);
        column.setMaxWidth(100);
        column.setMinWidth(100);
        column = mainJTable.getColumnModel().getColumn(1);
        column.setMaxWidth(100);
        column.setMinWidth(100);
        column = mainJTable.getColumnModel().getColumn(2);
        column.setMaxWidth(200);
        column.setMinWidth(200);

        // кидаю всё в скролл и его добавляю к таббид-пейну
        JScrollPane scroll = new JScrollPane(mainJTable);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sourceTabbedPane.addTab("  Текущие проекты  ", scroll);
    }
    
    private void setArhieveTable(){
        // инициирую и настраиваю таблицу
        archieveJTable = new JTable();
        archieveJTable.setName("Archieve");
        archieveJTable.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                mainJTable.clearSelection();
            }
        });
        archieveJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
//            {"2021.05.29", "wwwww", "aaaaaaaaaa", null},
//            {"2021.04.25", "222222", "ssssssss", null},
//            {"2021.06.01", "s33333", "bbbbbbbb", null},
            },
            new String [] {"Срок сдачи","Заказчик", "Описание", "Директория"}) {

            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };
            @Override
            public Class getColumnClass(int columnIndex) {
                if (columnIndex==0)
                    return Integer.class;
                else
                    return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        // задаём сортировку таблицы
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(archieveJTable.getModel());
        sorter.setSortable(3, false);
        sorter.setComparator(0, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
//        sorter.toggleSortOrder(0); 
        sorter.setSortsOnUpdates(true);
        archieveJTable.setRowSorter(sorter);
        
//        archieveJTable.setAutoCreateRowSorter(true);
        archieveJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        archieveJTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        archieveJTable.getTableHeader().setReorderingAllowed(false);
        archieveJTable.setDoubleBuffered(true);
        archieveJTable.setAutoscrolls(true);
        archieveJTable.setFillsViewportHeight(true);
        archieveJTable.setShowGrid(true);
        archieveJTable.setRowHeight(30);
        ((DefaultTableCellRenderer)archieveJTable.getTableHeader().getDefaultRenderer())
                                        .setHorizontalAlignment(JLabel.CENTER);

        // задаю рендеры и едиторы
        archieveJTable.setDefaultRenderer(Object.class, new ArchiveTableRender());
        archieveJTable.getColumn("Директория").setCellRenderer(new ArchiveTableButtonRender());
        archieveJTable.getColumn("Директория").setCellEditor(new ButtonEditor(new JCheckBox()));
        archieveJTable.getColumn("Описание").setCellEditor(new DescriptionColumnEditor(new JTextField()));

        // настраиваю ширину столбцов
        TableColumn column = archieveJTable.getColumnModel().getColumn(3);
        column.setMaxWidth(100);
        column.setMinWidth(60);
        column = archieveJTable.getColumnModel().getColumn(2);
        column.setMaxWidth(400);
        column.setMinWidth(300);
        column = archieveJTable.getColumnModel().getColumn(0);
        column.setMaxWidth(100);
        column.setMinWidth(100);
        column = archieveJTable.getColumnModel().getColumn(1);
        column.setMaxWidth(250);
        column.setMinWidth(200);

        // кидаю всё в скролл и его добавляю к таббид-пейну
        JScrollPane scroll = new JScrollPane(archieveJTable);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sourceTabbedPane.addTab("     Архив     ", scroll);
    }
    /**
     * Setting up the Tab Table
     */
    private void tabbedPaneConfig(){
        sourceTabbedPane.addChangeListener((ChangeEvent e) -> {
            updateInfo();
            mainJTable.clearSelection();
            archieveJTable.clearSelection();
        });
    }

    /**
     * Configuring the functionality of buttons on the the form
     */
    private void buttonsConfig(){
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                try {
                    updateTable();
                    updateInfo();
                    DataFilling.tableFilling(mainJTable, archieveJTable);
                } 
                catch (FileNotFoundException fnfex) {
                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, fnfex);
                    JOptionPane.showMessageDialog(null, "        В директории по корневому пути не найдено всех нужных системных файлов!\n"
                            + "Восстановите их перед началом работы или укажите новый корневой путь для работы!","Системное уведомление", JOptionPane.ERROR_MESSAGE);
                }
                catch (IOException ex) { Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex); }
            }
        });
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getCurrentTab() == 1) { //if this is Archieve tab active
                    TableMethods.getArchiveModifyErrorPane();
                    archieveJTable.clearSelection();
                } else {
                    if (mainJTable.getSelectedRow() == -1) // if selection is empty
                        TableMethods.getEmptySelectionDeletePane();
                    else if (mainJTable.getSelectedRowCount() > 1) // if selected more than 1 row
                        TableMethods.getSeveralSelectionModifyPane();
                    else // if all is ok
                    if (mainJTable.getRowSorter() != null) // if filter is on
                        new InputOrModifyOrderFrame(MainJFrame.this, mainJTable, mainJTable.getRowSorter().convertRowIndexToModel(mainJTable.getSelectedRow()));
                    else // default
                        new InputOrModifyOrderFrame(MainJFrame.this, mainJTable, mainJTable.getSelectedRow());
                }
                
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
            }
        });
        inputRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TableMethods.loadConfig();
                } catch (IOException ex) { return; }
                
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                InputOrModifyOrderFrame inputFrame;
                if (getCurrentTab()==0)
                    inputFrame= new InputOrModifyOrderFrame(MainJFrame.this, mainJTable);
                else
                    inputFrame= new InputOrModifyOrderFrame(MainJFrame.this, archieveJTable, true);
            }
        });
        deleteRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!TableMethods.isModifyingAvaible()) {
                        TableMethods.getAvaibleErrorPane();
                        return;
                    }
                    TableMethods.setModifyingAvaibleMark(false);
                    
                    
                    int[] rowsToDelete = null;
                    // for archive
                    if (getCurrentTab() == 1) {
                        rowsToDelete = archieveJTable.getSelectedRows();
                        
                        if (rowsToDelete.length == 0) // if selection is empty
                            TableMethods.getEmptySelectionDeletePane();
                        
                        else if (TableMethods.getWarningPaneDelete() == JOptionPane.YES_OPTION) {
                            //deliting files
                            TableMethods.deletingFilesBiConsumer.accept(IntStream.of(rowsToDelete)
                                    .boxed().toArray(Integer[]::new), archieveJTable);
                            // updating the .prop
                            ArrayList<String> returnDates = new ArrayList<>();
                            ArrayList<String> custumerStrings = new ArrayList<>();
                            
                            for (int row:rowsToDelete) {
                                returnDates.add((String) archieveJTable.getValueAt(row, 0));
                                custumerStrings.add((String) archieveJTable.getValueAt(row, 1));
                            }
                            try { TableMethods.deletingPropertyUpdate(returnDates.toArray(new String[0]), custumerStrings.toArray(new String[0]), getCurrentTab() == 1); }
                            catch (IOException ex) { Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex); }
                            
                            // updatind rows' indexes if sorting enabled
                            if (archieveJTable.getRowSorter() != null) {
                                rowsToDelete = TableMethods.updateRowIndexes(rowsToDelete, archieveJTable);
                                // deleting rows
                                TableMethods.deletingRowBiConsumer.accept(IntStream.of(rowsToDelete)
                                        .boxed().toArray(Integer[]::new), archieveJTable);
                            }
                        }
                    }
                    // for main content
                    else {
                        rowsToDelete = mainJTable.getSelectedRows();
                        
                        if (rowsToDelete.length == 0) // if selection is empty
                            TableMethods.getEmptySelectionDeletePane();
                        
                        else if (TableMethods.getWarningPaneDelete() == JOptionPane.YES_OPTION) {
                            //deliting files
                            TableMethods.deletingFilesBiConsumer.accept(IntStream.of(rowsToDelete)
                                    .boxed().toArray(Integer[]::new), mainJTable);
                            
                            // updating the .prop
                            ArrayList<String> returnDates = new ArrayList<>();
                            ArrayList<String> custumerStrings = new ArrayList<>();
                            
                            for (int row:rowsToDelete) {
                                returnDates.add((String) mainJTable.getValueAt(row, 0));
                                custumerStrings.add((String) mainJTable.getValueAt(row, 2));
                            }
                            try { TableMethods.deletingPropertyUpdate(returnDates.toArray(new String[0]), custumerStrings.toArray(new String[0]), getCurrentTab() == 1); }
                            catch (IOException ex) { Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex); }
                            
                            // updatind rows' indexes if sorting enabled
                            if (mainJTable.getRowSorter() != null)
                                rowsToDelete = TableMethods.updateRowIndexes(rowsToDelete, mainJTable);
                            
                            TableMethods.deletingRowBiConsumer.accept(IntStream.of(rowsToDelete)
                                    .boxed().toArray(Integer[]::new), mainJTable);
                        }
                    }
                    
                    mainJTable.clearSelection();
                    archieveJTable.clearSelection();
                    
                    updateInfo();
                    TableMethods.setModifyingAvaibleMark(true);
                    mainJTable.clearSelection();
                    archieveJTable.clearSelection();
                } catch (IOException ex) {
                    mainJTable.clearSelection();
                    archieveJTable.clearSelection();
                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void menuConfig(){
        editMenuPathItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                try {
                    mainJTable.clearSelection();
                    
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(TableMethods.getRootPath().toFile());
                    fileChooser = TableMethods.setUpdateUI(fileChooser);
                    fileChooser.setDialogTitle("Выбор корневого пути");
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    
                    // выбирать только директории
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    // показывать только директории
                    fileChooser.setFileFilter(new FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            return f.isDirectory();
                        }
                        
                        @Override
                        public String getDescription() {
                            return "";
                        }
                    });
                    
                    int choose = fileChooser.showDialog(null, "Выбрать директорию");
                    if (choose == JFileChooser.APPROVE_OPTION) {
                        newPath = fileChooser.getSelectedFile().getAbsolutePath();
                        TableMethods.checkDirectoriesAfterRootChange(Path.of(newPath));
                        TableMethods.setRootPath(newPath);
                        TableMethods.updateConfig(newPath);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        dumpUpdateMenuItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                try {
                    Files.walkFileTree(TableMethods.getRootPath(), new DirectoryCopyVisitor(TableMethods.getRootPath(), Path.of(TableMethods.getRootPath().toString()+"\\config\\dump")) {
                        boolean toSkip = false;
                        
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                            if (dir.toString().contains("АРХИВ") ||
//                                    dir.toString().contains("Текущие заказы") ||
//                                    dir.getName(dir.getNameCount()-1).toString().equals("config"))

                            Path targetPath = getToPath().resolve(getFromPath().relativize(dir));
                            if (dir.toString().contains("config")) {
                                if (dir.getName(dir.getNameCount() - 1).toString().equals("config")) {
                                    if (!Files.exists(targetPath)) {
                                        Files.createDirectory(targetPath);
                                    } 
                                } else 
                                        return FileVisitResult.SKIP_SUBTREE;
                            } 
                            else if (!dir.toString().contains("config") && !Files.exists(targetPath)) {
                                Files.createDirectory(targetPath);
                            }

                            return FileVisitResult.CONTINUE;
                        }
                    });
                    JOptionPane.showMessageDialog(null, "Обновление дампа завершено успешно!","Выполнено", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        dumpCleanMenuItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                try {
                    Object[] choices = {"Подтвердить", "Отменить"};
                    Object defaultChoice = choices[0];
                    int selected = JOptionPane.showOptionDialog(null, "Полностью очистить текущий дамп данных?","Предупреждение",
                                                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, choices, defaultChoice);
                    
                    if (selected!=JOptionPane.YES_OPTION)
                        return;
                    
                    Files.walkFileTree(TableMethods.getRootPath(), new DirectoryCopyVisitor(Path.of(TableMethods.getRootPath().toString()+"\\config\\dump"), null) {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (   (file.getName(file.getNameCount() - 1).toString().equals("actual_cont.txt")||
                                    file.getName(file.getNameCount() - 1).toString().equals("archieve_cont.txt"))  &&
                                    file.getName(file.getNameCount() - 2).toString().equals("config")                     &&
                                    file.getName(file.getNameCount() - 3).toString().equals("testdb"))
                                return FileVisitResult.CONTINUE;
                            
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            if (dir.getName(dir.getNameCount() - 1).toString().equals("dump")) {
                                return FileVisitResult.TERMINATE;
                            }
                            
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                    JOptionPane.showMessageDialog(null, "Очистка дампа завершено успешно!\n\n Не забудьте вскоре его обновить.","Выполнено", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        recoverPropMenuItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                try {
                    Object[] choices = {"Подтвердить", "Отменить"};
                    Object defaultChoice = choices[0];
                    int selected = JOptionPane.showOptionDialog(null, "Вы хотите очистить текущие системные файлы, после чего \n"
                                        + "               восстановить их из резервых данных.\n"
                                        + "                    Продолжить восстановление?","Предупреждение",
                                                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, choices, defaultChoice);
                    
                    if (selected!=JOptionPane.YES_OPTION)
                        return;
                    
                    boolean actual_cont = true;
                    boolean archieve_cont = true;
                    boolean om = true;
                    
                    if (Files.exists(Path.of(TableMethods.getRootPath().toString()+"\\config\\dump\\config\\actual_cont.txt")))
                        Files.copy(Path.of(TableMethods.getRootPath().toString()+"\\config\\dump\\config\\actual_cont.txt"),
                            Path.of(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt"),
                            StandardCopyOption.REPLACE_EXISTING);
                    else
                        actual_cont = false;
                    if (Files.exists(Path.of(TableMethods.getRootPath().toString()+"\\config\\dump\\config\\archieve_cont.txt")))
                        Files.copy(Path.of(TableMethods.getRootPath().toString()+"\\config\\dump\\config\\archieve_cont.txt"),
                            Path.of(TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt"),
                            StandardCopyOption.REPLACE_EXISTING);
                    else
                        archieve_cont = false;
                    if (Files.exists(Path.of(TableMethods.getRootPath().toString()+"\\config\\dump\\config\\om.txt")))
                        Files.copy(Path.of(TableMethods.getRootPath().toString()+"\\config\\dump\\config\\om.txt"),
                            Path.of(TableMethods.getRootPath().toString()+"\\config\\om.txt"),
                            StandardCopyOption.REPLACE_EXISTING);
                    else
                        om = false;
                    
                    if (!actual_cont && !archieve_cont && !om)
                        JOptionPane.showMessageDialog(null, "В дампе не обнаружено всех резервных системных файлов!","Ошибка", JOptionPane.ERROR_MESSAGE);
                    else 
                        JOptionPane.showMessageDialog(null, "Восстановление системных файлов завершено!","Выполнено", JOptionPane.INFORMATION_MESSAGE);
                } 
                catch (FileSystemException fsex) {
                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, fsex);
                    JOptionPane.showMessageDialog(null, "В данный момент файлы используются другим пользователем или программой!","Невозможно", JOptionPane.ERROR_MESSAGE);
                }
                catch (IOException ex) { Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex); }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        propFromTablesUpdateMenuItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                try {
                    if (!TableMethods.isModifyingAvaible())
                        return;
                    TableMethods.setModifyingAvaibleMark(false);
                    
                    Object[] choices = {"Подтвердить", "Отменить"};
                    Object defaultChoice = choices[0];
                    int selected = JOptionPane.showOptionDialog(null, "Вы хотите актуализировать системные данные по заказам?","Предупреждение",
                                                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, choices, defaultChoice);
                    
                    if (selected!=JOptionPane.YES_OPTION)
                        return;
                    
                    DataFilling.actualPropertyInformationUpdate(mainJTable, archieveJTable);
                    JOptionPane.showMessageDialog(null, "Обновление данных по заказам выполнено успешно!","Выполнено", JOptionPane.INFORMATION_MESSAGE);
                    
                    TableMethods.setModifyingAvaibleMark(true);
                } 
                catch (IOException ex) {
                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    try { TableMethods.setModifyingAvaibleMark(true); }
                    catch (IOException ex1) { Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex1); }
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        defaultHelpMenuItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                HelpFrame helpFrame = new HelpFrame(0);
                helpFrame.setLocationRelativeTo(null);
                helpFrame.setVisible(true);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        menuFunctionalHelpMenuItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                HelpFrame helpFrame = new HelpFrame(1);
                helpFrame.setLocationRelativeTo(null);
                helpFrame.setVisible(true);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        filesWorkHelpMenuItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                HelpFrame helpFrame = new HelpFrame(2);
                helpFrame.setLocationRelativeTo(null);
                helpFrame.setVisible(true);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        errorHelpMenuItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                HelpFrame helpFrame = new HelpFrame(3);
                helpFrame.setLocationRelativeTo(null);
                helpFrame.setVisible(true);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        autofillHelpMenuItem.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                mainJTable.clearSelection();
                archieveJTable.clearSelection();
                
                HelpFrame helpFrame = new HelpFrame(4);
                helpFrame.setLocationRelativeTo(null);
                helpFrame.setVisible(true);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
    
    /**
     * 
     * updates the information in the infoArea
     */
    public void updateInfo(){
        String infoString = "";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        
        infoString+="              " + timeStamp +"\n\n";
        switch(getCurrentTab()){
            case 0 -> { 
                infoString+="        Кол-во заказов: " + getMainTableRowCount() +"\n    ";
                infoString+=TableMethods.getMainTableNearestOrdersInfo(mainJTable);
                setInfoText(infoString); }
            case 1 -> {
                infoString+="Кол-во записей в архиве: " + getArchieveTableRowCount() +"\n";
                infoString+= TableMethods.getArchieveTableDateInfo(archieveJTable) +"\n";
                setInfoText(infoString);
            }
            default -> { setInfoText(""); }
        }
    }

    private void updateTable() throws IOException {
        int rowCount = mainJTable.getRowCount();
        
        for (int row=0; row<rowCount; row++, rowCount = mainJTable.getRowCount()) {
            int trueRowIndex = mainJTable.getRowSorter().convertRowIndexToModel(row);
            
            if (TableMethods.getDaysDiff((String) mainJTable.getValueAt(row, 0)) <=-6) { // detecting the necessary row
                //config .prop file
                String idProp = mainJTable.getValueAt(row, 0) +" "+ TableMethods.toValidNameFile((String) mainJTable.getValueAt(row, 2));
                ArrayList<String> propMainText = new ArrayList<>(); //will rewrite actual_cont.prop
                ArrayList<String> newPropArchieveText = new ArrayList<>(); // will append to archieve_cont.prop
                //reading
                try (FileReader fileReader = new FileReader(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt")) {
                    Scanner scan = new Scanner(fileReader);
                    while (scan.hasNextLine()) {
                        String currentLine = scan.nextLine();
                        if (currentLine.contains(idProp)) {
                            newPropArchieveText.add(currentLine); //id
                            newPropArchieveText.add(scan.nextLine()); //return date
                            scan.nextLine(); //ignore accept date
                            newPropArchieveText.add(scan.nextLine()); //customer
                            currentLine = scan.nextLine();
                            newPropArchieveText.add(currentLine); //description

                            // moving throught the description to the next order adding the lines to list
                            while (!currentLine.contains("id") && scan.hasNextLine()) {
                                currentLine = scan.nextLine();
                                newPropArchieveText.add(currentLine);
                            }
                            if (scan.hasNextLine()) //if not the end of .prop file -> adding id line
                                propMainText.add(currentLine);
                        }
                        else
                            propMainText.add(currentLine);
                    }
                }
                //re-writing main cont
                try (FileWriter writer = new FileWriter(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt")) {
                    for (String line : propMainText) {
                        writer.write(line + "\r\n");
                    }
                }
                //appending archieve info to .prop
                try (FileWriter writer = new FileWriter(TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt", true)) {
                    for (String line : newPropArchieveText) {
                        writer.write(line + "\r\n");
                    }
                }
                
                
                // deleting files and moving orders
                String currOrderFileName = mainJTable.getValueAt(row, 0) +" "+ TableMethods.toValidNameFile((String) mainJTable.getValueAt(row, 2));
                Path oldPathToFile = Path.of(TableMethods.getRootPath().toString() + "\\Текущие заказы", currOrderFileName);
                Path actualPathToFile = Path.of(TableMethods.getRootPath().toString() + "\\АРХИВ", currOrderFileName);
                
                if (Files.exists(oldPathToFile)) {
                    Files.walkFileTree(oldPathToFile, new DirectoryMoveWithDeletingVisitor(oldPathToFile, actualPathToFile));
                }
                
                // adding to the ArchieveTab
                ((DefaultTableModel) archieveJTable.getModel()).addRow(new Object[]{
                    mainJTable.getValueAt(row, 0), mainJTable.getValueAt(row, 2),
                    mainJTable.getValueAt(row, 3), null});
                // deleting from MainTab
                ((DefaultTableModel) mainJTable.getModel()).removeRow(trueRowIndex);
                
                row--;
            }
        }
    }
    
    // Getters and Setters are there
    protected int getMainTableSelectedRow() {
        return mainJTable.getSelectedRow();
    }
    protected int getMainTableRowCount() {
        return mainJTable.getRowCount();
    }
    protected int getArchieveTableSelectedRow() {
        return archieveJTable.getSelectedRow();
    }
    protected int getArchieveTableRowCount() {
        return archieveJTable.getRowCount();
    }
    protected String getInfoText() {
        return infoTextArea.getText();
    }
    protected void setInfoText(String text) {
        infoTextArea.setText(text);
    }
    protected int getCurrentTab() {
        return sourceTabbedPane.getSelectedIndex();
    }
    protected void setCurrentTab(int mark) {
        sourceTabbedPane.setSelectedIndex(mark);
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolsBar = new javax.swing.JToolBar();
        toolsPanel = new javax.swing.JPanel();
        infoTextArea = new javax.swing.JTextArea();
        inputRecord = new javax.swing.JButton();
        modifyButton = new javax.swing.JButton();
        deleteRecord = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        sourcePanel = new javax.swing.JPanel();
        // jsp = new JScrollPane(sourceTable);
        sourceTabbedPane = new javax.swing.JTabbedPane();
        menu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        recoverPropMenuItem = new javax.swing.JMenuItem();
        propFromTablesUpdateMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        editMenuPathItem = new javax.swing.JMenuItem();
        dumpMenu = new javax.swing.JMenu();
        dumpUpdateMenuItem = new javax.swing.JMenuItem();
        dumpCleanMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        defaultHelpMenuItem = new javax.swing.JMenuItem();
        menuFunctionalHelpMenuItem = new javax.swing.JMenuItem();
        filesWorkHelpMenuItem = new javax.swing.JMenuItem();
        errorHelpMenuItem = new javax.swing.JMenuItem();
        autofillHelpMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Менеджер заказов");
        setLocation(new java.awt.Point(0, 0));
        setLocationByPlatform(true);

        toolsBar.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        toolsBar.setFloatable(false);
        toolsBar.setOrientation(javax.swing.SwingConstants.VERTICAL);
        toolsBar.setAutoscrolls(true);
        toolsBar.setPreferredSize(new java.awt.Dimension(120, 205));

        toolsPanel.setAlignmentX(CENTER_ALIGNMENT);
        toolsPanel.setAlignmentY(CENTER_ALIGNMENT);
        toolsPanel.setPreferredSize(null);

        infoTextArea.setEditable(false);
        infoTextArea.setColumns(20);
        infoTextArea.setLineWrap(true);
        infoTextArea.setRows(5);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setFocusable(false);
        infoTextArea.setPreferredSize(new java.awt.Dimension(120, 45));

        inputRecord.setText("<html><center>"+"Добавить"+"<br>"+"запись"+"</center></html>");
        inputRecord.setActionCommand(""); // NOI18N
        inputRecord.setAlignmentX(0.5F);
        inputRecord.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        inputRecord.setFocusable(false);
        inputRecord.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        inputRecord.setPreferredSize(new java.awt.Dimension(25, 25));

        modifyButton.setText("Редактировать");
        modifyButton.setAlignmentX(0.5F);
        modifyButton.setFocusable(false);
        modifyButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        deleteRecord.setText("<html><center>"+"Удалить"+"<br>"+"запись"+"</center></html>");
        deleteRecord.setAlignmentX(0.5F);
        deleteRecord.setFocusable(false);
        deleteRecord.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteRecord.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        updateButton.setText("<html><center>"+"Обновить"+"<br>"+"базу"+"</center></html>");
        updateButton.setAlignmentX(0.5F);
        updateButton.setFocusable(false);
        updateButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout toolsPanelLayout = new javax.swing.GroupLayout(toolsPanel);
        toolsPanel.setLayout(toolsPanelLayout);
        toolsPanelLayout.setHorizontalGroup(
            toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoTextArea, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addGroup(toolsPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(modifyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        toolsPanelLayout.setVerticalGroup(
            toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(inputRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(modifyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(deleteRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        toolsBar.add(toolsPanel);

        sourcePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        sourceTabbedPane.setToolTipText("");
        sourceTabbedPane.setName("Content"); // NOI18N

        javax.swing.GroupLayout sourcePanelLayout = new javax.swing.GroupLayout(sourcePanel);
        sourcePanel.setLayout(sourcePanelLayout);
        sourcePanelLayout.setHorizontalGroup(
            sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourcePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sourceTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
                .addContainerGap())
        );
        sourcePanelLayout.setVerticalGroup(
            sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourceTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
        );

        sourceTabbedPane.getAccessibleContext().setAccessibleName("contentTab");

        menu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.SoftBevelBorder.RAISED));
        menu.setPreferredSize(new java.awt.Dimension(161, 30));

        fileMenu.setText("Файл");
        fileMenu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        fileMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fileMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fileMenu.setLabel("<html><div style='text-align: center;'>" + "Файл" + "</div></html>");
        fileMenu.setMargin(new java.awt.Insets(0, 4, 0, 2));
        fileMenu.setMinimumSize(new java.awt.Dimension(42, 19));
        fileMenu.setPreferredSize(new java.awt.Dimension(46, 19));

        recoverPropMenuItem.setText("Восстановить системные файлы");
        recoverPropMenuItem.setPreferredSize(new java.awt.Dimension(231, 26));
        fileMenu.add(recoverPropMenuItem);

        propFromTablesUpdateMenuItem.setText("Обновить данные по заказам");
        propFromTablesUpdateMenuItem.setPreferredSize(new java.awt.Dimension(231, 26));
        fileMenu.add(propFromTablesUpdateMenuItem);

        menu.add(fileMenu);

        editMenu.setText("Функции");
        editMenu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        editMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editMenu.setMargin(new java.awt.Insets(0, 3, 0, 2));
        editMenu.setPreferredSize(new java.awt.Dimension(64, 19));

        editMenuPathItem.setText("Изменить корневой путь");
        editMenuPathItem.setPreferredSize(new java.awt.Dimension(168, 26));
        editMenu.add(editMenuPathItem);

        dumpMenu.setText("Дамп информации");
        dumpMenu.setPreferredSize(new java.awt.Dimension(168, 26));

        dumpUpdateMenuItem.setText("Обновить");
        dumpUpdateMenuItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        dumpUpdateMenuItem.setMargin(new java.awt.Insets(0, 14, 0, 2));
        dumpUpdateMenuItem.setPreferredSize(new java.awt.Dimension(110, 24));
        dumpMenu.add(dumpUpdateMenuItem);

        dumpCleanMenuItem.setText("Очистить");
        dumpCleanMenuItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        dumpCleanMenuItem.setMargin(new java.awt.Insets(0, 14, 0, 2));
        dumpCleanMenuItem.setPreferredSize(new java.awt.Dimension(110, 24));
        dumpMenu.add(dumpCleanMenuItem);

        editMenu.add(dumpMenu);

        menu.add(editMenu);

        helpMenu.setText("Справка");
        helpMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        helpMenu.setMargin(new java.awt.Insets(0, 3, 0, 2));
        helpMenu.setPreferredSize(new java.awt.Dimension(63, 19));

        defaultHelpMenuItem.setText("Общая справка");
        defaultHelpMenuItem.setPreferredSize(new java.awt.Dimension(130, 26));
        helpMenu.add(defaultHelpMenuItem);

        menuFunctionalHelpMenuItem.setText("Функционал меню");
        menuFunctionalHelpMenuItem.setPreferredSize(new java.awt.Dimension(130, 26));
        helpMenu.add(menuFunctionalHelpMenuItem);

        filesWorkHelpMenuItem.setText("Работа с заказами");
        filesWorkHelpMenuItem.setPreferredSize(new java.awt.Dimension(130, 26));
        helpMenu.add(filesWorkHelpMenuItem);

        errorHelpMenuItem.setText("Ошибки");
        errorHelpMenuItem.setPreferredSize(new java.awt.Dimension(130, 26));
        helpMenu.add(errorHelpMenuItem);

        autofillHelpMenuItem.setText("Автозаполнение");
        autofillHelpMenuItem.setPreferredSize(new java.awt.Dimension(130, 26));
        helpMenu.add(autofillHelpMenuItem);

        menu.add(helpMenu);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(toolsBar, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourcePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourcePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolsBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        // sourcePanel.add(jsp);
        // add(jsp);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new MainJFrame().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem autofillHelpMenuItem;
    private javax.swing.JMenuItem defaultHelpMenuItem;
    private javax.swing.JButton deleteRecord;
    private javax.swing.JMenuItem dumpCleanMenuItem;
    private javax.swing.JMenu dumpMenu;
    private javax.swing.JMenuItem dumpUpdateMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem editMenuPathItem;
    private javax.swing.JMenuItem errorHelpMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem filesWorkHelpMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JTextArea infoTextArea;
    private javax.swing.JButton inputRecord;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenuItem menuFunctionalHelpMenuItem;
    private javax.swing.JButton modifyButton;
    private javax.swing.JMenuItem propFromTablesUpdateMenuItem;
    private javax.swing.JMenuItem recoverPropMenuItem;
    private javax.swing.JPanel sourcePanel;
    // private JScrollPane jsp;
    private javax.swing.JTabbedPane sourceTabbedPane;
    private javax.swing.JToolBar toolsBar;
    private javax.swing.JPanel toolsPanel;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables

}

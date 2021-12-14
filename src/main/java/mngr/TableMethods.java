<<<<<<< Updated upstream
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mngr;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import mngr.orders_functional.DirectoryCopyVisitor;

/**
 * Сlass with methods for working with a table and it's content
 * @author MrUnknown404
 */
public class TableMethods {
    private TableMethods(){}
    
    private static Path rootPath = Path.of("D:\\Soft\\NetBeans-12.4\\beansfold\\testdb");
    private static String lastFolderPath = "";
    private static TreeSet<String> listingSet;
    
    protected static BiConsumer<Integer[], JTable> deletingFilesBiConsumer = (rows, tab) -> {
        Path pathToDelete = null;
        FileVisitor<Path> fileVisitorToDelete = new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        };

        if (rows.length != 0) {
            for (int row : rows) {
                if (tab.getName().equals("Archieve")) {
                    try {
                        pathToDelete = Path.of(getRootPath().toString() + "\\АРХИВ", tab.getValueAt(row, 0) + " " + toValidNameFile((String) tab.getValueAt(row, 1)));
                        Files.walkFileTree(pathToDelete, fileVisitorToDelete);
                    } catch (IOException ex) {
                        Logger.getLogger(TableMethods.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        pathToDelete = Path.of(getRootPath().toString() + "\\Текущие заказы", tab.getValueAt(row, 0) + " " + toValidNameFile((String) tab.getValueAt(row, 2)));
                        Files.walkFileTree(pathToDelete, fileVisitorToDelete);
                    } catch (IOException ex) {
                        Logger.getLogger(TableMethods.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    };
    protected static BiConsumer<Integer[], JTable> deletingRowBiConsumer = (rows, tab) -> {
        if (rows.length != 0) {
            for (int row : rows) {
                ((DefaultTableModel) tab.getModel()).removeRow(row);
            }
        }
    };
    
    /**
     * Updating the line numbers according to the sorting
     * @param rowsIndexes int array with indexes of rows you want to delete
     * @param tab JTable from which you want to delete any rows
     * @return updated indexes of rows according to the current sorting
     */
    public static int[] updateRowIndexes(int[] rowsIndexes, JTable tab){
        int[] rowsToDelete = rowsIndexes;
        int i = 0;
        
        for (int row : rowsToDelete) {
            rowsToDelete[i] = tab.getRowSorter().convertRowIndexToModel(row);
            i++;
        }
        descendingSort(rowsToDelete);
        
        return rowsToDelete;
    }
    
    /**
     * 
     * @param toValidString
     * @return 
     */
    public static String toValidNameFile(String toValidString){
        String validNameString = "";
        
        for (char letter:toValidString.toCharArray()) {
            if (letter==' ' || Character.isLetterOrDigit(letter))
                validNameString+=letter;
        }
        return validNameString;
    }
    
    public static String getMainTableNearestOrdersInfo(JTable mainTab) {
        int red = 0, yellow = 0, gray = 0;

        for (int row = 0; row < mainTab.getRowCount(); row++)
            if (TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) <= -6) {}
            else if (TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) <  0 &&
                     TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) >  -6) gray++;
            else if (TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) <= 3) red++;
            else if (TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) <= 7) yellow++;

        return "      Красная зона: "     + red    +"\n"+
               "           Жёлтая зона: " + yellow +"\n"+
               "            Серая зона: " + gray   +"\n";
    }
    
    public static String getArchieveTableDateInfo(JTable archieveTab) {
        int oneMonth = 0, twoMonth = 0, threeMonth = 0;
        
        for (int row = 0; row < archieveTab.getRowCount(); row++) {
            int dateDif = TableMethods.getMonthDiff((String) archieveTab.getValueAt(row, 0));
            
            if (dateDif >= -1) oneMonth++;
            else if (dateDif >= -2) twoMonth++;
            else if (dateDif >= -3) threeMonth++;
        }

        return "     Давностью в месяц: "  + oneMonth   +"\n"+
               "   Давностью в 2 месяца: " + twoMonth   +"\n"+
               "   Давностью в 3 месяца: " + threeMonth +"\n";
    }
    
    /**
     * Calculates the diff of current date to input date in days
     * @param value date in YYYY.MM.DD format
     * @return returns the difference in whole days as int
     */
    public static int getDaysDiff(String value) {
        String inputDate = getFromattedData(value);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date dateOne = null;
        Date dateTwo = new Date();

        try {
            dateOne = format.parse(inputDate);
        } catch (ParseException e) { e.printStackTrace(); }

        // The number of days between dates in milliseconds
        long difference = dateOne.getTime() - dateTwo.getTime();
        // Converting the number of days between dates from milliseconds to days
        int days = (int) (difference / (24 * 60 * 60 * 1000)); // 24h * 60min * 60sec * 1000milsec)
        
        return days;
    }
    
    /**
     * Calculates the diff of current date to input date in months
     * @param value date in YYYY.MM.DD format
     * @return returns the difference in whole months as int
     */
    public static int getMonthDiff(String value) {
        String inputDate = getFromattedData(value);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date dateOne = null;
        Date dateTwo = new Date();

        try {
            dateOne = format.parse(inputDate);
        } catch (ParseException e) { e.printStackTrace(); }

        // The number of days between dates in milliseconds
        long difference = dateOne.getTime() - dateTwo.getTime();
        // Converting the number of days between dates from milliseconds to months
        int months = (int) (difference / (24 * 60 * 60 * 1000) / 30); // (30d * 24h * 60min * 60sec * 1000milsec)
        
        return months;
    }
    
    /**
     * Returns a formatted date from the input string
     * @param input
     * @return formatted date
     */
    public static String getFromattedData(String input){
        if (input==null)
            return null;
        
        String inputDate = input;
        if (!inputDate.contains(".") || inputDate.indexOf(".") == inputDate.lastIndexOf(".")) {
            StringBuilder builder = new StringBuilder(inputDate.strip());
            while (builder.indexOf(" ") != -1) {
                builder.setCharAt(builder.indexOf(" "), '.');
            }
            
            inputDate = builder.toString();
        }
        return inputDate;
    }
    
    /**
     * Updates the ui for the entered object, translates it into rus
     * @param choose JFileChooser for update
     * @return the updated chooser
     */
    public static JFileChooser setUpdateUI(JFileChooser choose) {
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.lookInLabelText", "Смотреть в");
        UIManager.put("FileChooser.folderNameLabelText", "Имя файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файла");
                
        UIManager.put("FileChooser.homeFolderToolTipText", "Домой");

        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");

        UIManager.put("FileChooser.lookInLabelText", "Папка");
        UIManager.put("FileChooser.saveInLabelText", "Папка");
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файлов");

        UIManager.put("FileChooser.upFolderToolTipText", "На один уровень вверх");
        UIManager.put("FileChooser.newFolderToolTipText", "Создание новой папки");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
        UIManager.put("FileChooser.fileNameHeaderText", "Имя");
        UIManager.put("FileChooser.fileSizeHeaderText", "Размер");
        UIManager.put("FileChooser.fileTypeHeaderText", "Тип");
        UIManager.put("FileChooser.fileDateHeaderText", "Изменен");
        UIManager.put("FileChooser.fileAttrHeaderText", "Атрибуты");

        UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
        choose.updateUI();

        return choose;
    }
    
    /**
     * Sorting int array in descending order using the merge algorithm
     * by mergeSort(..) and merge(..) methods
     * @param array int[] array
     */
    public static void descendingSort(int[] array){
//        Arrays.sort(array);
        mergeSort(array, 0, array.length-1);
        
        for(int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }
    /**
     * Recursive algorithm of sorting int[], which
     * which divides the array into sub-arrays and calculates them
     * @param array target array
     * @param left left border of array
     * @param right right border of array
     */
    private static void mergeSort(int[] array, int left, int right) {
        if (right <= left)
            return;
        int mid = (left + right) / 2;
        
        mergeSort(array, left, mid);
        mergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }
    /**
     * Merges and sorts the elements of the incoming array
     * @param array target array
     * @param left left border of array
     * @param mid middle position of array
     * @param right right border of array
     */
    private static void merge(int[] array, int left, int mid, int right) {
        // calculating lengths
        int lengthLeft = mid - left + 1;
        int lengthRight = right - mid;

        // creating temporary subarrays
        int leftArray[] = new int[lengthLeft];
        int rightArray[] = new int[lengthRight];

        // copying our sorted subarrays into temporaries
        for (int i = 0; i < lengthLeft; i++) {
            leftArray[i] = array[left + i];
        }
        for (int i = 0; i < lengthRight; i++) {
            rightArray[i] = array[mid + i + 1];
        }

        // iterators containing current index of temp subarrays
        int leftIndex = 0;
        int rightIndex = 0;

        // copying from leftArray and rightArray back into array
        for (int i = left; i < right + 1; i++) {
            // if there are still uncopied elements in R and L, copy minimum of the two
            if (leftIndex < lengthLeft && rightIndex < lengthRight) {
                if (leftArray[leftIndex] < rightArray[rightIndex]) {
                    array[i] = leftArray[leftIndex];
                    leftIndex++;
                } else {
                    array[i] = rightArray[rightIndex];
                    rightIndex++;
                }
            } // if all the elements have been copied from rightArray, copy the rest of leftArray
            else if (leftIndex < lengthLeft) {
                array[i] = leftArray[leftIndex];
                leftIndex++;
            } // if all the elements have been copied from leftArray, copy the rest of rightArray
            else if (rightIndex < lengthRight) {
                array[i] = rightArray[rightIndex];
                rightIndex++;
            }
        }
    }
    
    /**
     * Displaying a warning about deleting the selected rows
     * @return JOptionPane OK or CANSEL int result
     */
    public static int getWarningPaneDelete(){
        Object[] choices = {"Подтвердить", "Отменить"};
        Object defaultChoice = choices[0];
        
        return JOptionPane.showOptionDialog(null, "Вы хотите удалить выделенные поля?","Предупреждение",
                                                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, choices, defaultChoice);
    }
    /**
     * Error output about an empty selection area
     */
    public static void getEmptySelectionDeletePane(){
        JOptionPane.showMessageDialog(null, "Не выделено ни одного поля!","Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Error output about an empty selection area
     */
    public static void getSeveralSelectionModifyPane(){
        JOptionPane.showMessageDialog(null, "      Выделено несколько полей!\nВыберите одно для редактирования.","Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Error output about attempting to modify order that is in archieve
     */
    public static void getArchiveModifyErrorPane(){
        JOptionPane.showMessageDialog(null, "Объекты в архиве нельзя редактировать!","Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    public static void getAlreadyExistsOrderPane(){
        JOptionPane.showMessageDialog(null, "Такой заказ уже существует!","Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    public static void getAvaibleErrorPane(){
        JOptionPane.showMessageDialog(null, "Другой пользователь вносит изменения в текущий момент!\n               "+
                                            "          Повторите попытку позже",
                                            "Недоступно", JOptionPane.ERROR_MESSAGE);
    }
    
    public static Path getRootPath(){
        return rootPath;
    }
    public static void setRootPath(String newPath){
        rootPath = Path.of(newPath);
    }
    
    public static void loadConfig() throws FileNotFoundException, IOException {
        String configPath;
        try (FileReader fr = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fr);
            configPath = null;
            while (scan.hasNextLine()) {
                String currentString = scan.nextLine();
                
                if (currentString.contains("rootPath")) {
                    configPath = currentString.split(" = ")[1];
                }
            }
        }
        rootPath = Path.of(configPath);
    }
    
    public static void checkDirectoriesAfterRootChange(Path newRootPath) {
        try {
            ArrayList<Path> files = new ArrayList<>(); //get any files
            try (Stream<Path> paths = Files.walk(newRootPath)) {
                paths
                    .filter(Files::isRegularFile)
                    .forEach(files::add);
            }
            ArrayList<Path> directories = new ArrayList<>(); // get any dirs
            try (Stream<Path> paths = Files.walk(newRootPath)) {
                paths
                    .filter(Files::isDirectory)
                    .forEach(directories::add);
            }
            
            if (!directories.contains(Path.of(newRootPath.toString()+"\\Текущие заказы"))) {
                Files.createDirectory(Path.of(newRootPath.toString()+"\\Текущие заказы"));
            }
            if (!directories.contains(Path.of(newRootPath.toString()+"\\АРХИВ"))) {
                Files.createDirectory(Path.of(newRootPath.toString()+"\\АРХИВ"));
            }
            if (!directories.contains(Path.of(newRootPath.toString()+"\\config"))) {
                Files.createDirectory(Path.of(newRootPath.toString()+"\\config"));
                Files.createDirectory(Path.of(newRootPath.toString()+"\\config\\dump"));
                
                if (Files.exists(Path.of(getRootPath().toString()+"\\config\\dump")))
                    Files.walkFileTree(Path.of(getRootPath().toString()+"\\config\\dump"),
                            new DirectoryCopyVisitor(Path.of(getRootPath().toString()+"\\config\\dump"), Path.of(newRootPath.toString()+"\\config\\dump")));
                
                if (Files.exists(Path.of(getRootPath().toString()+"\\config\\actual_cont.txt")))
                        Files.move(Path.of(getRootPath().toString()+"\\config\\actual_cont.txt"), Path.of(newRootPath.toString()+"\\config"));
                else
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\actual_cont.txt"));
                
                if (Files.exists(Path.of(getRootPath().toString()+"\\config\\archieve_cont.txt")))
                        Files.move(Path.of(getRootPath().toString()+"\\config\\archieve_cont.txt"), Path.of(newRootPath.toString()+"\\config"));
                else
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\archieve_cont.txt"));
                
                if (Files.exists(Path.of(getRootPath().toString()+"\\config\\om.txt")))
                        Files.move(Path.of(getRootPath().toString()+"\\config\\om.txt"), Path.of(newRootPath.toString()+"\\config"));
                else {
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\om.txt"));
                    
                    FileWriter writer = new FileWriter(newRootPath.toString()+"\\config\\om.txt");
                    writer.write("rootPath = " + newRootPath.toString() + "\r\n");
                    writer.write("isAvaible = true\r\n");
                    writer.write("isModifyingAvaible = true\r\n");
                    writer.close();
                }
            } else {
                ArrayList<Path> configFiles = new ArrayList<>();
                try (Stream<Path> paths = Files.walk(Path.of(newRootPath.toString()+"\\config"))) {
                    paths
                        .filter(Files::isRegularFile)
                        .forEach(configFiles::add);
                }
                if (!configFiles.contains(Path.of(newRootPath.toString()+"\\config\\om.txt"))) {
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\om.txt"));
                    
                    FileWriter writer = new FileWriter(newRootPath.toString()+"\\config\\om.txt");
                    writer.write("rootPath = " + newRootPath.toString() + "\r\n");
                    writer.write("isAvaible = true\r\n");
                    writer.write("isModifyingAvaible = true\r\n");
                    writer.close();
                } else {
                    FileReader reader = new FileReader(newRootPath.toString()+"\\config\\om.txt");
                    Scanner scan = new Scanner(reader);
                    ArrayList<String> omText = new ArrayList<>();
                    
                    while (scan.hasNext())
                        omText.add(scan.nextLine());
                    reader.close();
                    
                    if (omText.size()==0) {
                        FileWriter writer = new FileWriter(newRootPath.toString() + "\\config\\om.txt");
                        writer.write("rootPath = " + newRootPath.toString() + "\r\n");
                        writer.write("isAvaible = true\r\n");
                        writer.write("isModifyingAvaible = true\r\n");
                        writer.close();
                    } else {
                        omText.remove(0);
                        FileWriter writer = new FileWriter(newRootPath.toString()+"\\config\\om.txt");
                        writer.write("rootPath = " + newRootPath.toString() + "\r\n");
                        for (String line:omText)
                            writer.write(line +"\r\n");
                        writer.close();
                    }
                    
                    if (Files.exists(Path.of(newRootPath.toString()+"\\config\\dump")))
                        if (Files.exists(Path.of(getRootPath().toString()+"\\config\\dump")))
                            Files.walkFileTree(Path.of(getRootPath().toString()+"\\config\\dump"),
                                new DirectoryCopyVisitor(Path.of(getRootPath().toString()+"\\config\\dump"), Path.of(newRootPath.toString()+"\\config\\dump")));
                }
                
                if (!configFiles.contains(Path.of(newRootPath.toString()+"\\config\\actual_cont.txt")))
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\actual_cont.txt"));
                if (!configFiles.contains(Path.of(newRootPath.toString()+"\\config\\archieve_cont.txt")))
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\archieve_cont.txt"));
            }
            
        } catch (IOException ex) {
            Logger.getLogger(TableMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void updateConfig(String newRootPath) throws FileNotFoundException, IOException {// mark .prop as *in using*
        setConfiguringAvaibleMark(false);

        ArrayList<String> propertiesText = new ArrayList<>();
        // reading
        try ( FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine()) {
                String currentString = scan.nextLine();
                if (currentString.contains("rootPath")) {
                    propertiesText.add(currentString.split(" = ")[0] + " = " + newRootPath);
                } else {
                    propertiesText.add(currentString);
                }
            }
        }
        //writing
        try ( FileWriter fileWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
            for (String line : propertiesText) {
                line += "\r\n";
                fileWriter.write(line);
            }
        }
        setConfiguringAvaibleMark(true);
    }
    public static void deletingPropertyUpdate(String[] returnDates, String[] custumerStrings, boolean isArchieve) throws FileNotFoundException, IOException {
        String pathToProperties = getRootPath().toString()+"\\config\\archieve_cont.txt";
        if(!isArchieve)
            pathToProperties = getRootPath().toString()+"\\config\\actual_cont.txt";
        
        ArrayList<String> propertiesText = new ArrayList<>();
        ArrayList<String> targetsID = new ArrayList<>();
        for (int i=0; i<returnDates.length; i++) {
            targetsID.add(returnDates[i]+" "+custumerStrings[i]);
        }
        
        // reading
        try (FileReader fileReader = new FileReader(pathToProperties)) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine()) {
                String currentString = scan.nextLine();
                if (currentString.contains("id:") && targetsID.contains(currentString.split("id: ")[1])) {
                    currentString = scan.nextLine();
                    
                    // moving throught the description to the next order
                    while (!currentString.contains("id") && scan.hasNextLine()) {
                            currentString = scan.nextLine();
                    }
                    if (scan.hasNextLine()) //if not the end of .prop file -> adding id line
                        propertiesText.add(currentString);
                } else
                    propertiesText.add(currentString);
            }
        }
        
        //writing
        try (FileWriter fileWriter = new FileWriter(pathToProperties)) {
            for (String line:propertiesText) {
                line += "\r\n";
                fileWriter.write(line);
            }
        }
    }
    public static boolean isConfiguringAvaible() throws FileNotFoundException, IOException{
        // check avaibility
        String isAvaible = "";
        try (FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine() && !isAvaible.contains("isAvaible"))
                isAvaible = scan.nextLine();
        }
        return isAvaible.split(" = ")[1].equals("true");
    }
    public static void setConfiguringAvaibleMark(boolean isAvaible) throws IOException {
        ArrayList<String> propText = new ArrayList<>();
        try (FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine())
                propText.add(scan.nextLine());
        }
        
        if (isAvaible)
            // mark .prop as *in using*
            try ( FileWriter avaibilityWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
                for (String line:propText) {
                    if (line.contains("isAvaible"))
                        line = "isAvaible = true";
                    avaibilityWriter.write(line+"\r\n");
                }
            }
        else
            // mark .prop as free for use
            try ( FileWriter avaibilityWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
                for (String line:propText) {
                    if (line.contains("isAvaible"))
                        line = "isAvaible = false";
                    avaibilityWriter.write(line+"\r\n");
                }
            }
    }
    public static boolean isModifyingAvaible() throws FileNotFoundException, IOException{
        // check avaibility
        String isModifying = "";
        try (FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine() && !isModifying.contains("isModifyingAvaible"))
                isModifying = scan.nextLine();
        }
        return isModifying.split(" = ")[1].equals("true");
    }
    public static void setModifyingAvaibleMark(boolean isAvaible) throws IOException {
        ArrayList<String> propText = new ArrayList<>();
        try (FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine())
                propText.add(scan.nextLine());
        }
        
        if (isAvaible)
            // mark .prop as *in using*
            try ( FileWriter avaibilityWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
                for (String line:propText) {
                    if (line.contains("isModifyingAvaible"))
                        line = "isModifyingAvaible = true";
                    avaibilityWriter.write(line+"\r\n");
                }
            }
        else
            // mark .prop as free for use
            try ( FileWriter avaibilityWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
                for (String line:propText) {
                    if (line.contains("isModifyingAvaible"))
                        line = "isModifyingAvaible = false";
                    avaibilityWriter.write(line+"\r\n");
                }
            }
    }
}
=======
package mngr;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import mngr.orders_functional.DirectoryCopyVisitor;
import mngr.orders_functional.DirectoryMoveWithDeletingVisitor;

/**
 * Сlass with methods for working with a table and their content
 * @author MrUnknown404
 */
public class TableMethods {
    private TableMethods(){}
    
    private static Path rootPath = Path.of("D:\\Soft\\NetBeans-12.4\\beansfold\\testdb");
    private static String lastFolderPath = "";
    private static TreeSet<String> listingSet;
    
    /**
     * A consumer for deleting files by the input rows of the specified table
     */
    protected static BiConsumer<Integer[], JTable> deletingFilesBiConsumer = (rows, tab) -> {
        Path pathToDelete = null;
        FileVisitor<Path> fileVisitorToDelete = new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        };

        if (rows.length != 0) {
            for (int row : rows) {
                if (tab.getName().equals("Archieve")) {
                    try {
                        pathToDelete = Path.of(getRootPath().toString() + "\\АРХИВ", tab.getValueAt(row, 0) + " " + toValidNameFile((String) tab.getValueAt(row, 1)));
                        Files.walkFileTree(pathToDelete, fileVisitorToDelete);
                    } catch (IOException ex) {
                        Logger.getLogger(TableMethods.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        pathToDelete = Path.of(getRootPath().toString() + "\\Текущие заказы", tab.getValueAt(row, 0) + " " + toValidNameFile((String) tab.getValueAt(row, 2)));
                        Files.walkFileTree(pathToDelete, fileVisitorToDelete);
                    } catch (IOException ex) {
                        Logger.getLogger(TableMethods.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    };
    /**
     * A consumer for deleting table rows
     */
    protected static BiConsumer<Integer[], JTable> deletingRowBiConsumer = (rows, tab) -> {
        if (rows.length != 0) {
            for (int row : rows) {
                ((DefaultTableModel) tab.getModel()).removeRow(row);
            }
        }
    };
    
    /**
     * Updating the line numbers according to the sorting
     * @param rowsIndexes int array with indexes of rows you want to delete
     * @param tab JTable from which you want to delete any rows
     * @return updated indexes of rows according to the current sorting
     */
    public static int[] updateRowIndexes(int[] rowsIndexes, JTable tab){
        int[] rowsToDelete = rowsIndexes;
        int i = 0;
        
        for (int row : rowsToDelete) {
            rowsToDelete[i] = tab.getRowSorter().convertRowIndexToModel(row);
            i++;
        }
        descendingSort(rowsToDelete);
        
        return rowsToDelete;
    }
    
    /**
     * Returns a valid file name format
     * @param toValidString string we need to be valid
     * @return validString
     */
    public static String toValidNameFile(String toValidString){
        String validNameString = "";
        
        for (char letter:toValidString.toCharArray()) {
            if (letter==' ' || letter=='(' || letter==')' || letter=='_' || letter=='-' || Character.isLetterOrDigit(letter))
                validNameString+=letter;
        }
        return validNameString.strip();
    }
    /**
     * returns distributed information of active orders' periods
     * @param mainTab Main JTable of active orders
     * @return String of the info
     */
    public static String getMainTableNearestOrdersInfo(JTable mainTab) {
        int red = 0, yellow = 0, gray = 0;

        for (int row = 0; row < mainTab.getRowCount(); row++)
            if (TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) <= -6) {}
            else if (TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) <  0 &&
                     TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) >  -6) gray++;
            else if (TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) <= 3) red++;
            else if (TableMethods.getDaysDiff((String) mainTab.getValueAt(row, 0)) <= 7) yellow++;

        return "      Красная зона: "     + red    +"\n"+
               "           Жёлтая зона: " + yellow +"\n"+
               "            Серая зона: " + gray   +"\n";
    }
    /**
     * returns distributed information of archieved orders' periods
     * @param archieveTab Archieve JTavle
     * @return String of the info
     */
    public static String getArchieveTableDateInfo(JTable archieveTab) {
        int oneMonth = 0, twoMonth = 0, threeMonth = 0;
        
        for (int row = 0; row < archieveTab.getRowCount(); row++) {
            int dateDif = TableMethods.getMonthDiff((String) archieveTab.getValueAt(row, 0));
            
            if (dateDif >= -1) oneMonth++;
            else if (dateDif >= -2) twoMonth++;
            else if (dateDif >= -3) threeMonth++;
        }

        return "     Давностью в месяц: "  + oneMonth   +"\n"+
               "   Давностью в 2 месяца: " + twoMonth   +"\n"+
               "   Давностью в 3 месяца: " + threeMonth +"\n";
    }
    
    /**
     * Calculates the diff of current date to input date in days
     * @param value date in YYYY.MM.DD format
     * @return returns the difference in whole days as int
     */
    public static int getDaysDiff(String value) {
        String inputDate = getFromattedData(value);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date dateOne = null;
        Date dateTwo = new Date();

        try {
            dateOne = format.parse(inputDate);
        } catch (ParseException e) { e.printStackTrace(); }

        // The number of days between dates in milliseconds
        long difference = dateOne.getTime() - dateTwo.getTime();
        // Converting the number of days between dates from milliseconds to days
        int days = (int) (difference / (24 * 60 * 60 * 1000)); // 24h * 60min * 60sec * 1000milsec)
        
        return days;
    }
    
    /**
     * Calculates the diff of current date to input date in months
     * @param value date in YYYY.MM.DD format
     * @return returns the difference in whole months as int
     */
    public static int getMonthDiff(String value) {
        String inputDate = getFromattedData(value);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date dateOne = null;
        Date dateTwo = new Date();

        try {
            dateOne = format.parse(inputDate);
        } catch (ParseException e) { e.printStackTrace(); }

        // The number of days between dates in milliseconds
        long difference = dateOne.getTime() - dateTwo.getTime();
        // Converting the number of days between dates from milliseconds to months
        int months = (int) (difference / (24 * 60 * 60 * 1000) / 30); // (30d * 24h * 60min * 60sec * 1000milsec)
        
        return months;
    }
    
    /**
     * Returns a formatted date from the input string
     * @param input Data as String
     * @return formatted date
     */
    public static String getFromattedData(String input){
        if (input==null)
            return null;
        
        String inputDate = input;
        if (!inputDate.contains(".") || inputDate.indexOf(".") == inputDate.lastIndexOf(".")) {
            StringBuilder builder = new StringBuilder(inputDate.strip());
            while (builder.indexOf(" ") != -1) {
                builder.setCharAt(builder.indexOf(" "), '.');
            }
            
            inputDate = builder.toString();
        }
        return inputDate;
    }
    
    /**
     * Updates the ui for the entered object, translates it into rus
     * @param choose JFileChooser for update
     * @return the updated chooser
     */
    public static JFileChooser setUpdateUI(JFileChooser choose) {
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.lookInLabelText", "Смотреть в");
        UIManager.put("FileChooser.folderNameLabelText", "Имя файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файла");
                
        UIManager.put("FileChooser.homeFolderToolTipText", "Домой");

        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");

        UIManager.put("FileChooser.lookInLabelText", "Папка");
        UIManager.put("FileChooser.saveInLabelText", "Папка");
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файлов");

        UIManager.put("FileChooser.upFolderToolTipText", "На один уровень вверх");
        UIManager.put("FileChooser.newFolderToolTipText", "Создание новой папки");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
        UIManager.put("FileChooser.fileNameHeaderText", "Имя");
        UIManager.put("FileChooser.fileSizeHeaderText", "Размер");
        UIManager.put("FileChooser.fileTypeHeaderText", "Тип");
        UIManager.put("FileChooser.fileDateHeaderText", "Изменен");
        UIManager.put("FileChooser.fileAttrHeaderText", "Атрибуты");

        UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
        choose.updateUI();

        return choose;
    }
    
    /**
     * Sorting int array in descending order using the merge algorithm
     * by mergeSort(..) and merge(..) methods
     * @param array int[] array
     */
    public static void descendingSort(int[] array){
//        Arrays.sort(array);
        mergeSort(array, 0, array.length-1);
        
        for(int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }
    /**
     * Recursive algorithm of sorting int[], which
     * which divides the array into sub-arrays and calculates them
     * @param array target array
     * @param left left border of array
     * @param right right border of array
     */
    private static void mergeSort(int[] array, int left, int right) {
        if (right <= left)
            return;
        int mid = (left + right) / 2;
        
        mergeSort(array, left, mid);
        mergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }
    /**
     * Merges and sorts the elements of the incoming array
     * @param array target array
     * @param left left border of array
     * @param mid middle position of array
     * @param right right border of array
     */
    private static void merge(int[] array, int left, int mid, int right) {
        // calculating lengths
        int lengthLeft = mid - left + 1;
        int lengthRight = right - mid;

        // creating temporary subarrays
        int leftArray[] = new int[lengthLeft];
        int rightArray[] = new int[lengthRight];

        // copying our sorted subarrays into temporaries
        for (int i = 0; i < lengthLeft; i++) {
            leftArray[i] = array[left + i];
        }
        for (int i = 0; i < lengthRight; i++) {
            rightArray[i] = array[mid + i + 1];
        }

        // iterators containing current index of temp subarrays
        int leftIndex = 0;
        int rightIndex = 0;

        // copying from leftArray and rightArray back into array
        for (int i = left; i < right + 1; i++) {
            // if there are still uncopied elements in R and L, copy minimum of the two
            if (leftIndex < lengthLeft && rightIndex < lengthRight) {
                if (leftArray[leftIndex] < rightArray[rightIndex]) {
                    array[i] = leftArray[leftIndex];
                    leftIndex++;
                } else {
                    array[i] = rightArray[rightIndex];
                    rightIndex++;
                }
            } // if all the elements have been copied from rightArray, copy the rest of leftArray
            else if (leftIndex < lengthLeft) {
                array[i] = leftArray[leftIndex];
                leftIndex++;
            } // if all the elements have been copied from leftArray, copy the rest of rightArray
            else if (rightIndex < lengthRight) {
                array[i] = rightArray[rightIndex];
                rightIndex++;
            }
        }
    }
    
    /**
     * updating table fields (replace the deprecated orders from actual ones to archive)
     * @param mainJTable - table of current orders
     * @param archieveJTable - table of archive orders
     * @throws java.io.IOException
     */
    public static void updateTable(JTable mainJTable, JTable archieveJTable) throws IOException {
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
    
    /**
     * replace the deprecated order from actual ones to archive
     * @param orderRow the deprecated order's row number
     * @param maintTable mainJTable of actual content
     * @param archiveTable archiveJTable of archive orders
     * @throws java.io.IOException
     */
    public static void replaceOrderToArchive(int orderRow, JTable maintTable, JTable archiveTable) throws IOException {
        int trueRowIndex = maintTable.getRowSorter().convertRowIndexToModel(orderRow);
        
        //config .prop file
                String idProp = maintTable.getValueAt(orderRow, 0) +" "+ TableMethods.toValidNameFile((String) maintTable.getValueAt(orderRow, 2));
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
                String currOrderFileName = maintTable.getValueAt(orderRow, 0) +" "+ TableMethods.toValidNameFile((String) maintTable.getValueAt(orderRow, 2));
                Path oldPathToFile = Path.of(TableMethods.getRootPath().toString() + "\\Текущие заказы", currOrderFileName);
                Path actualPathToFile = Path.of(TableMethods.getRootPath().toString() + "\\АРХИВ", currOrderFileName);
                
                if (Files.exists(oldPathToFile)) {
                    Files.walkFileTree(oldPathToFile, new DirectoryMoveWithDeletingVisitor(oldPathToFile, actualPathToFile));
                }
                
                // adding to the ArchieveTab
                ((DefaultTableModel) archiveTable.getModel()).addRow(new Object[]{
                    maintTable.getValueAt(orderRow, 0), maintTable.getValueAt(orderRow, 2),
                    maintTable.getValueAt(orderRow, 3), null});
                // deleting from MainTab
                ((DefaultTableModel) maintTable.getModel()).removeRow(trueRowIndex);
    }
    
    /**
     * Displaying a warning about deleting the selected rows
     * @return JOptionPane OK or CANSEL int result
     */
    public static int getWarningPaneDelete(){
        Object[] choices = {"Удалить", "Отменить"};
        Object defaultChoice = choices[0];
        
        return JOptionPane.showOptionDialog(null, "Вы хотите удалить выделенные поля?","Предупреждение",
                                                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, choices, defaultChoice);
    }
    public static int getWarningPaneReplace(){
        Object[] choices = {"Переместить", "Отменить"};
        Object defaultChoice = choices[0];
        
        return JOptionPane.showOptionDialog(null, "Переместить выделенные поля в архив?","Предупреждение",
                                                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, choices, defaultChoice);
    }
    /**
     * Error output about an empty selection area
     */
    public static void getEmptySelectionDeletePane(){
        JOptionPane.showMessageDialog(null, "Не выделено ни одного поля!","Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Error output about an empty selection area
     */
    public static void getSeveralSelectionModifyPane(){
        JOptionPane.showMessageDialog(null, "      Выделено несколько полей!\nВыберите одно для редактирования.","Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Error output about attempting to modify order that is in archieve
     */
    public static void getArchiveModifyErrorPane(){
        JOptionPane.showMessageDialog(null, "Объекты в архиве нельзя редактировать!","Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Error output about attempting to duplicate order
     */
    public static void getAlreadyExistsOrderPane(){
        JOptionPane.showMessageDialog(null, "Такой заказ уже существует!","Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Error output indicating that another user is making changes at the current moment
     */
    public static void getAvaibleErrorPane(){
        JOptionPane.showMessageDialog(null, "Другой пользователь вносит изменения в текущий момент!\n               "+
                                            "          Повторите попытку позже",
                                            "Недоступно", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Getter for active rootPath
     * @return root path
     */
    public static Path getRootPath(){
        return rootPath;
    }
    /**
     * Setter for active rootPath + config files changing
     * @param newPath new root path
     * @throws IOException if file not found
     */
    public static void setRootPath(String newPath) throws IOException{
        rootPath = Path.of(newPath);
        
        try (FileWriter writer = new FileWriter("src\\rootPath.txt")) {
            writer.write("rootPath = " + rootPath);
        }
    }
    
    /**
     * The method that reads the root path should be called before initializing the program components
     * @throws FileNotFoundException if file not found
     * @throws IOException if unable to write
     */
    public static void loadConfig() throws FileNotFoundException, IOException {
        String configPath;
        try (FileReader fr = new FileReader("src\\rootPath.txt")) {
            Scanner scan = new Scanner(fr);
            configPath = null;
            while (scan.hasNextLine()) {
                String currentString = scan.nextLine();
                
                if (currentString.contains("rootPath")) {
                    configPath = currentString.split(" = ")[1];
                }
            }
        }
        rootPath = Path.of(configPath);
    }
    /**
     * Checking the necessary directories and files when changing the root path.
     * In case of non-compliance with the new path, copies from the old one or creates new ones
     * @param newRootPath new root path as Path
     */
    public static void checkDirectoriesAfterRootChange(Path newRootPath) {
        try {
            ArrayList<Path> files = new ArrayList<>(); //get any files
            try (Stream<Path> paths = Files.walk(newRootPath)) {
                paths
                    .filter(Files::isRegularFile)
                    .forEach(files::add);
            }
            ArrayList<Path> directories = new ArrayList<>(); // get any dirs
            try (Stream<Path> paths = Files.walk(newRootPath)) {
                paths
                    .filter(Files::isDirectory)
                    .forEach(directories::add);
            }
            
            if (!directories.contains(Path.of(newRootPath.toString()+"\\Текущие заказы"))) {
                Files.createDirectory(Path.of(newRootPath.toString()+"\\Текущие заказы"));
            }
            if (!directories.contains(Path.of(newRootPath.toString()+"\\АРХИВ"))) {
                Files.createDirectory(Path.of(newRootPath.toString()+"\\АРХИВ"));
            }
            if (!directories.contains(Path.of(newRootPath.toString()+"\\config"))) {
                Files.createDirectory(Path.of(newRootPath.toString()+"\\config"));
                Files.createDirectory(Path.of(newRootPath.toString()+"\\config\\dump"));
                
                if (Files.exists(Path.of(getRootPath().toString()+"\\config\\dump")))
                    Files.walkFileTree(Path.of(getRootPath().toString()+"\\config\\dump"),
                            new DirectoryCopyVisitor(Path.of(getRootPath().toString()+"\\config\\dump"), Path.of(newRootPath.toString()+"\\config\\dump")));
                
                if (Files.exists(Path.of(getRootPath().toString()+"\\config\\actual_cont.txt")))
                        Files.copy(Path.of(getRootPath().toString()+"\\config\\actual_cont.txt"), Path.of(newRootPath.toString()+"\\config\\actual_cont.txt"));
                else
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\actual_cont.txt"));
                
                if (Files.exists(Path.of(getRootPath().toString()+"\\config\\archieve_cont.txt")))
                        Files.copy(Path.of(getRootPath().toString()+"\\config\\archieve_cont.txt"), Path.of(newRootPath.toString()+"\\config\\archieve_cont.txt"));
                else
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\archieve_cont.txt"));
                
                if (Files.exists(Path.of(getRootPath().toString()+"\\config\\om.txt")))
                        Files.copy(Path.of(getRootPath().toString()+"\\config\\om.txt"), Path.of(newRootPath.toString()+"\\config\\om.txt"));
                else {
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\om.txt"));
                    
                    FileWriter writer = new FileWriter(newRootPath.toString()+"\\config\\om.txt");
                    writer.write("rootPath = " + newRootPath.toString() + "\r\n");
                    writer.write("isAvaible = true\r\n");
                    writer.write("isModifyingAvaible = true\r\n");
                    writer.close();
                }
            } else {
                ArrayList<Path> configFiles = new ArrayList<>();
                try (Stream<Path> paths = Files.walk(Path.of(newRootPath.toString()+"\\config"))) {
                    paths
                        .filter(Files::isRegularFile)
                        .forEach(configFiles::add);
                }
                if (!configFiles.contains(Path.of(newRootPath.toString()+"\\config\\om.txt"))) {
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\om.txt"));
                    
                    try (FileWriter writer = new FileWriter(newRootPath.toString()+"\\config\\om.txt")) {
                        writer.write("rootPath = " + newRootPath.toString() + "\r\n");
                        writer.write("isAvaible = true\r\n");
                        writer.write("isModifyingAvaible = true\r\n");
                    }
                } else {
                    ArrayList<String> omText;
                    try (FileReader reader = new FileReader(newRootPath.toString()+"\\config\\om.txt")) {
                        Scanner scan = new Scanner(reader);
                        omText = new ArrayList<>();
                        while (scan.hasNext())
                            omText.add(scan.nextLine());
                    }
                    
                    if (omText.isEmpty()) {
                        try (FileWriter writer = new FileWriter(newRootPath.toString() + "\\config\\om.txt")) {
                            writer.write("rootPath = " + newRootPath.toString() + "\r\n");
                            writer.write("isAvaible = true\r\n");
                            writer.write("isModifyingAvaible = true\r\n");
                        }
                    } else {
                        omText.remove(0);
                        try (FileWriter writer = new FileWriter(newRootPath.toString()+"\\config\\om.txt")) {
                            writer.write("rootPath = " + newRootPath.toString() + "\r\n");
                            for (String line:omText)
                                writer.write(line +"\r\n");
                        }
                    }
                    
                    if (Files.exists(Path.of(newRootPath.toString()+"\\config\\dump")))
                        if (Files.exists(Path.of(getRootPath().toString()+"\\config\\dump")))
                            Files.walkFileTree(Path.of(getRootPath().toString()+"\\config\\dump"),
                                new DirectoryCopyVisitor(Path.of(getRootPath().toString()+"\\config\\dump"), Path.of(newRootPath.toString()+"\\config\\dump")));
                }
                
                if (!configFiles.contains(Path.of(newRootPath.toString()+"\\config\\actual_cont.txt")))
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\actual_cont.txt"));
                if (!configFiles.contains(Path.of(newRootPath.toString()+"\\config\\archieve_cont.txt")))
                    Files.createFile(Path.of(newRootPath.toString()+"\\config\\archieve_cont.txt"));
            }
            
        } catch (IOException ex) {
            Logger.getLogger(TableMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Updates the configuration with the new root path
     * @param newRootPath nwe root path as String
     * @throws FileNotFoundException if file not found
     * @throws IOException if unable to write
     */
    public static void updateConfig(String newRootPath) throws FileNotFoundException, IOException {// mark .prop as *in using*
        setConfiguringAvaibleMark(false);

        ArrayList<String> propertiesText = new ArrayList<>();
        // reading
        try ( FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine()) {
                String currentString = scan.nextLine();
                if (currentString.contains("rootPath")) {
                    propertiesText.add(currentString.split(" = ")[0] + " = " + newRootPath);
                } else {
                    propertiesText.add(currentString);
                }
            }
        }
        //writing
        try ( FileWriter fileWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
            for (String line : propertiesText) {
                line += "\r\n";
                fileWriter.write(line);
            }
        }
        setConfiguringAvaibleMark(true);
    }
    
    /**
     * A method that updates the configuration when one or more orders in the tables are deleted
     * @param returnDates return dates of orders to delete as array of String
     * @param custumerStrings custumers' names of orders to delete as array of String
     * @param isArchieve boolean is Archieve tab accepting
     * @throws FileNotFoundException if file not found
     * @throws IOException if unable to write
     */
    public static void deletingPropertyUpdate(String[] returnDates, String[] custumerStrings, boolean isArchieve) throws FileNotFoundException, IOException {
        String pathToProperties = getRootPath().toString()+"\\config\\archieve_cont.txt";
        if(!isArchieve)
            pathToProperties = getRootPath().toString()+"\\config\\actual_cont.txt";
        
        ArrayList<String> propertiesText = new ArrayList<>();
        ArrayList<String> targetsID = new ArrayList<>();
        for (int i=0; i<returnDates.length; i++) {
            targetsID.add(returnDates[i]+" "+custumerStrings[i]);
        }
        
        // reading
        try (FileReader fileReader = new FileReader(pathToProperties)) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine()) {
                String currentString = scan.nextLine();
                if (currentString.contains("id:") && targetsID.contains(currentString.split("id: ")[1])) {
                    currentString = scan.nextLine();
                    
                    // moving throught the description to the next order
                    while (!currentString.contains("id") && scan.hasNextLine()) {
                            currentString = scan.nextLine();
                    }
                    if (scan.hasNextLine()) //if not the end of .prop file -> adding id line
                        propertiesText.add(currentString);
                } else
                    propertiesText.add(currentString);
            }
        }
        
        //writing
        try (FileWriter fileWriter = new FileWriter(pathToProperties)) {
            for (String line:propertiesText) {
                line += "\r\n";
                fileWriter.write(line);
            }
        }
    }
    /**
     * Method that returns an access parameter for changing the program configuration
     * @return can be configure or not
     * @throws FileNotFoundException if file not found
     * @throws IOException for file errors
     */
    public static boolean isConfiguringAvaible() throws FileNotFoundException, IOException{
        // check avaibility
        String isAvaible = "";
        try (FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine() && !isAvaible.contains("isAvaible"))
                isAvaible = scan.nextLine();
        }
        return isAvaible.split(" = ")[1].equals("true");
    }
    /**
     * A method that changes the current access state to change the program configuration to "available" or "not available"
     * @param isAvaible boolean that will be written in configure file
     * @throws IOException for file errors
     */
    public static void setConfiguringAvaibleMark(boolean isAvaible) throws IOException {
        ArrayList<String> propText = new ArrayList<>();
        try (FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine())
                propText.add(scan.nextLine());
        }
        
        if (isAvaible)
            // mark .prop as *in using*
            try ( FileWriter avaibilityWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
                for (String line:propText) {
                    if (line.contains("isAvaible"))
                        line = "isAvaible = true";
                    avaibilityWriter.write(line+"\r\n");
                }
            }
        else
            // mark .prop as free for use
            try ( FileWriter avaibilityWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
                for (String line:propText) {
                    if (line.contains("isAvaible"))
                        line = "isAvaible = false";
                    avaibilityWriter.write(line+"\r\n");
                }
            }
    }
    
    /**
     * Method that returns the access parameter for changing the configuration of orders
     * @return can be modify or not
     * @throws FileNotFoundException if file not found
     * @throws IOException for file errors
     */
    public static boolean isModifyingAvaible() throws FileNotFoundException, IOException{
        // check avaibility
        String isModifying = "";
        try (FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine() && !isModifying.contains("isModifyingAvaible"))
                isModifying = scan.nextLine();
        }
        return isModifying.split(" = ")[1].equals("true");
    }
    /**
     * A method that changes the current access state to change the configuration of orders to "available" or "not available"
     * @param isAvaible boolean that will be written in configure file
     * @throws IOException for file errors
     */
    public static void setModifyingAvaibleMark(boolean isAvaible) throws IOException {
        ArrayList<String> propText = new ArrayList<>();
        try (FileReader fileReader = new FileReader(getRootPath().toString()+"\\config\\om.txt")) {
            Scanner scan = new Scanner(fileReader);
            while (scan.hasNextLine())
                propText.add(scan.nextLine());
        }
        
        if (isAvaible)
            // mark .prop as free for use
            try ( FileWriter avaibilityWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
                for (String line:propText) {
                    if (line.contains("isModifyingAvaible"))
                        line = "isModifyingAvaible = true";
                    avaibilityWriter.write(line+"\r\n");
                }
            }
        else
            // mark .prop as *in using*
            try ( FileWriter avaibilityWriter = new FileWriter(getRootPath().toString()+"\\config\\om.txt")) {
                for (String line:propText) {
                    if (line.contains("isModifyingAvaible"))
                        line = "isModifyingAvaible = false";
                    avaibilityWriter.write(line+"\r\n");
                }
            }
    }
    
    /**
     * A method that reset the main-table selection interval
     * @param table main-table of actual orders
     * @param shouldClear boolean check for full-clear or reset (restore the previous selected row)
     */
    public static void resetSelection(JTable table, boolean shouldClear) {
        try {
            int selectedRow = 0;
            int selectedColumn = 0;
//            if(!shouldClear && table.getSelectedRowCount() > 0) {
            if(!shouldClear) {
                selectedRow = table.getSelectedRow();
                selectedColumn = table.getSelectedColumn();
            }
            
            table.setRowSelectionInterval(0, 0);
            table.setColumnSelectionInterval(0, 0);
            table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            table.setColumnSelectionInterval(table.getColumnCount() - 1, table.getColumnCount() - 1);
            table.clearSelection();
            
//            if(!shouldClear && table.getSelectedRowCount() > 0) {
            if(!shouldClear) {
                table.setRowSelectionInterval(selectedRow, selectedRow);
                table.setColumnSelectionInterval(selectedColumn, selectedColumn);
            }
        } catch (IllegalArgumentException  ignored) {}
    }
}
>>>>>>> Stashed changes

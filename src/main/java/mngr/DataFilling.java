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
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.stream.Stream;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MrUnknown404
 */
class DataFilling {
    protected static void tableFilling(JTable mainTab, JTable archieveTab) throws IOException, FileNotFoundException {
        //clear tables
        ((DefaultTableModel) mainTab.getModel()).setRowCount(0);
        ((DefaultTableModel) archieveTab.getModel()).setRowCount(0);
        
        ArrayList<Path> actualDirectories = getDirs(Path.of(TableMethods.getRootPath().toString()+"\\Текущие заказы\\"));
        ArrayList<Path> archieveDirectories = getDirs(Path.of(TableMethods.getRootPath().toString()+"\\АРХИВ\\"));
        String regex = "20\\d\\d\\.[01]\\d\\.[0-3]\\d\\s\\S.*";
        
        // for actual orders
        for (Path dirPath: actualDirectories) {
            String dirName = dirPath.getName(dirPath.getNameCount()-1).toString();
            String returnDate = null;
            String acceptDate = null;
            String customer = null;
            String description = null;
            
            if (dirName.matches(regex)) {
                //check prop Text for existing current order
                try (FileReader fileReader = new FileReader(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt")) {
                    Scanner scan = new Scanner(fileReader);
                    ArrayList<String> propText = new ArrayList<>();
                    String currentLine = "";
                    //filling fields (if lines for this order exists) and propText 
                    while (scan.hasNextLine()) {
                        currentLine = scan.nextLine();
                        if (currentLine.contains("id") && currentLine.contains(dirName)) {
                            propText.add(currentLine); // id
                            currentLine = scan.nextLine(); //return date
                            propText.add(currentLine); 
                            returnDate = currentLine.split(": ")[1];
                            currentLine = scan.nextLine(); //acceptDate
                            propText.add(currentLine); 
                            if (currentLine.split(": ").length==1) acceptDate = new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime());
                            else acceptDate = currentLine.split(": ")[1];
                            currentLine = scan.nextLine(); //custumer
                            propText.add(currentLine); 
                            customer = currentLine.split(": ")[1];
                            
                            currentLine = scan.nextLine(); //description
                            if (currentLine.split(": ").length==1) description = "";
                            else description = currentLine.split(": ")[1] +"\r\n";
                            while (scan.hasNext() && !currentLine.contains("id")) { 
                                propText.add(currentLine); 
                                if (!currentLine.contains("description"))
                                    description += currentLine +"\r\n";
                                currentLine = scan.nextLine();
                            }
                            if (currentLine.contains("id")) propText.add(currentLine);
                        } 
                        else
                            propText.add(currentLine);
                    }
                    
                    //check fiels (if not null -> dont write anything in prop and just create row in table
                    if (returnDate!=null&&acceptDate!=null&&customer!=null&&description!=null) {
                        ((DefaultTableModel) mainTab.getModel()).addRow(new Object[]{returnDate, acceptDate, customer, description, null});
                    } else { //create new row and adding info abour the order to prop file
                        ((DefaultTableModel) mainTab.getModel()).addRow(new Object[]{dirName.split(" ")[0],
                                    new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()), dirName.split(" ")[1], "", null});
                        
                        propText.add("id: " + dirName);
                        propText.add("return date: " + dirName.split(" ")[0]);
                        propText.add("accept date: " + new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()));
                        propText.add("customer: " + dirName.split(" ")[1]);
                        propText.add("description: ");
                        setPropText(Path.of(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt"), propText);
                    }
                }
            }
        }
        
        // for archieve orders
        for (Path dirPath: archieveDirectories) {
            String dirName = dirPath.getName(dirPath.getNameCount()-1).toString();
            String returnDate = null;
            String customer = null;
            String description = null;
            
            if (dirName.matches(regex)) {
                //check prop Text for existing current order
                try (FileReader fileReader = new FileReader(TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt")) {
                    Scanner scan = new Scanner(fileReader);
                    ArrayList<String> propText = new ArrayList<>();
                    String currentLine = "";
                    //filling fields (if lines for this order exists) and propText 
                    while (scan.hasNextLine()) {
                        currentLine = scan.nextLine();
                        if (currentLine.contains("id") && currentLine.contains(dirName)) {
                            propText.add(currentLine); // id
                            currentLine = scan.nextLine(); //return date
                            propText.add(currentLine); 
                            returnDate = currentLine.split(": ")[1];
                            currentLine = scan.nextLine(); //custumer
                            propText.add(currentLine); 
                            customer = currentLine.split(": ")[1];
                            
                            currentLine = scan.nextLine(); //description
                            if (currentLine.split(": ").length==1) description = "";
                            else description = currentLine.split(": ")[1] +"\r\n";
                            while (scan.hasNext() && !currentLine.contains("id")) { 
                                propText.add(currentLine); 
                                if (!currentLine.contains("description"))
                                    description += currentLine +"\r\n";
                                currentLine = scan.nextLine(); 
                            }
                            if (currentLine.contains("id")) propText.add(currentLine);
                        } 
                        else
                            propText.add(currentLine);
                    }
                    
                    //check fiels (if not null -> dont write anything in prop and just create row in table
                    if (returnDate!=null&&customer!=null&&description!=null) {
                        ((DefaultTableModel) archieveTab.getModel()).addRow(new Object[]{returnDate, customer, description, null});
                    } else { //create new row and adding info abour the order to prop file
                        ((DefaultTableModel) archieveTab.getModel()).addRow(new Object[]{dirName.split(" ")[0], dirName.split(" ")[1], "", null});
                        
                        propText.add("id: " + dirName);
                        propText.add("return date: " + dirName.split(" ")[0]);
                        propText.add("customer: " + dirName.split(" ")[1]);
                        propText.add("description: ");
                        setPropText(Path.of(TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt"), propText);
                    }
                }
            }
        }
    }
    
    protected static void actualPropertyInformationUpdate(JTable mainTab, JTable archieveTab) throws IOException {
        markingPropertyFiles(mainTab, TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt");
        markingPropertyFiles(archieveTab, TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt");
        
        deleteMarkedPropertyFiles(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt");
        deleteMarkedPropertyFiles(TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt");
    }
    
    private static void markingPropertyFiles(JTable table, String pathToProperty) throws FileNotFoundException, IOException {
        //for actual 
        ArrayList<String> propText = new ArrayList<>();
        ArrayList<String> mainTableIDs = new ArrayList<>();
        
        if (!table.getName().equals("Archieve"))
            for (int i=0; i<table.getRowCount(); i++)
                mainTableIDs.add((String)table.getValueAt(i, 0)+" "+(String)table.getValueAt(i, 2));
        else
            for (int i=0; i<table.getRowCount(); i++)
                mainTableIDs.add((String)table.getValueAt(i, 0)+" "+(String)table.getValueAt(i, 1));
        
        try (FileReader fileReader = new FileReader(pathToProperty)) {
            Scanner scan = new Scanner(fileReader);
            String currentString = "";
            while (scan.hasNextLine()) {
                currentString = scan.nextLine();
                if(currentString.contains("id")) { //marking as 
                    if (mainTableIDs.contains(currentString.split(": ")[1])) {
                        propText.add("@isChecked");
                        propText.add(currentString);
                        mainTableIDs.remove(currentString.split(": ")[1]);
                    } else {
                        propText.add("@Outdated");
                        propText.add(currentString);
                    }
                }
                else
                    propText.add(currentString);
            }
        }
        //writing new property info to file
        setPropText(Path.of(pathToProperty), propText);
    }
    
    private static void deleteMarkedPropertyFiles(String pathToProperty) throws FileNotFoundException, IOException {
        ArrayList<String> propText = new ArrayList<>();
        FileReader reader = new FileReader(pathToProperty);
        Scanner scan = new Scanner(reader);
        
        while (scan.hasNext())
            propText.add(scan.nextLine());
        reader.close();
        
        boolean isOutdated = false ;
        FileWriter writer = new FileWriter(pathToProperty);
        for (String line: propText) {
            if (line.contains("@Outdated")) {
                isOutdated = true;
            }
            else if (line.contains("@isChecked")) {
                isOutdated = false;
            }
            else if (!isOutdated) {
                writer.write(line +"\r\n");
            }
        }
        writer.close();
    }
    
    private static void setPropText(Path toProp, ArrayList<String> propText) throws FileNotFoundException, IOException {
        try ( FileWriter writer = new FileWriter(toProp.toString())) {
            for (String line : propText) {
                writer.write(line + "\r\n");
            }
        }
    }
    private static ArrayList<Path> getDirs(Path toSearchArea) throws IOException {
        ArrayList<Path> directories = new ArrayList<>();
            try (Stream<Path> paths = Files.walk(toSearchArea)) {
                paths
                    .filter(Files::isDirectory)
                    .forEach(directories::add);
            }
            directories.remove(0);
            
            return directories;
    }
}
=======
package mngr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.stream.Stream;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * The class is used to populate tables with content and update information in configuration files
 * @author MrUnknown404
 */
class DataFilling {
    /**
     * updating tables and configurations (files)
     * @param mainTab MainTable JTable
     * @param archieveTab Archieve JTable
     * @throws IOException for file errors
     * @throws FileNotFoundException if file not found
     */
    protected static void tableFilling(JTable mainTab, JTable archieveTab) throws IOException, FileNotFoundException {
        //clear tables
        ((DefaultTableModel) mainTab.getModel()).setRowCount(0);
        ((DefaultTableModel) archieveTab.getModel()).setRowCount(0);
        
        ArrayList<Path> actualDirectories = getDirs(Path.of(TableMethods.getRootPath().toString()+"\\Текущие заказы\\"));
        ArrayList<Path> archieveDirectories = getDirs(Path.of(TableMethods.getRootPath().toString()+"\\АРХИВ\\"));
        String regex = "20\\d\\d\\.[01]\\d\\.[0-3]\\d\\s\\S.*";
        
        // for actual orders
        for (Path dirPath: actualDirectories) {
            String dirName = dirPath.getName(dirPath.getNameCount()-1).toString();
            String returnDate = null;
            String acceptDate = null;
            String customer = null;
            String description = null;
            
            if (dirName.matches(regex)) {
                //check prop Text for existing current order
                try (FileReader fileReader = new FileReader(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt")) {
//                try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt"), "UTF-8"))) {
                    Scanner scan = new Scanner(fileReader);
                    ArrayList<String> propText = new ArrayList<>();
                    String currentLine = "";
                    //filling fields (if lines for this order exists) and propText 
                    while (scan.hasNextLine()) {
                        currentLine = scan.nextLine();
//                        if (currentLine.contains("id") && currentLine.contains(dirName)) {
                        if (currentLine.contains("id") && currentLine.split(": ")[1].equals(dirName)) {
                            propText.add(currentLine); // id
                            currentLine = scan.nextLine(); //return date
                            propText.add(currentLine); 
                            returnDate = currentLine.split(": ")[1];
                            currentLine = scan.nextLine(); //acceptDate
                            propText.add(currentLine); 
                            if (currentLine.split(": ").length==1) acceptDate = new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime());
                            else acceptDate = currentLine.split(": ")[1];
                            currentLine = scan.nextLine(); //custumer
                            propText.add(currentLine); 
                            customer = currentLine.split(": ")[1];
                            
                            currentLine = scan.nextLine(); //description
                            if (currentLine.split(": ").length==1) description = "";
                            else description = currentLine.split(": ")[1] +"\r\n";
                            while (scan.hasNext() && !currentLine.contains("id")) { 
                                propText.add(currentLine); 
                                if (!currentLine.contains("description"))
                                    description += currentLine +"\r\n";
                                currentLine = scan.nextLine();
                            }
                            if (currentLine.contains("id")) propText.add(currentLine);
                        } 
                        else
                            propText.add(currentLine);
                    }
                    
                    //check fiels (if not null -> dont write anything in prop and just create row in table
                    if (returnDate!=null&&acceptDate!=null&&customer!=null&&description!=null) {
                        ((DefaultTableModel) mainTab.getModel()).addRow(new Object[]{returnDate, acceptDate, customer, description, null});
                    } else { //create new row and adding info abour the order to prop file
                        ((DefaultTableModel) mainTab.getModel()).addRow(new Object[]{dirName.split(" ")[0],
                                    new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()), dirName.substring(11), "", null});
                        
                        propText.add("id: " + dirName);
                        propText.add("return date: " + dirName.split(" ")[0]);
                        propText.add("accept date: " + new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()));
                        propText.add("customer: " + dirName.substring(11));
                        propText.add("description: ");
                        setPropText(Path.of(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt"), propText);
                    }
                }
            }
        }
        
        // for archieve orders
        for (Path dirPath: archieveDirectories) {
            String dirName = dirPath.getName(dirPath.getNameCount()-1).toString();
            String returnDate = null;
            String customer = null;
            String description = null;
            
            if (dirName.matches(regex)) {
                //check prop Text for existing current order
                try (FileReader fileReader = new FileReader(TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt")) {
                    Scanner scan = new Scanner(fileReader);
                    ArrayList<String> propText = new ArrayList<>();
                    String currentLine = "";
                    //filling fields (if lines for this order exists) and propText 
                    while (scan.hasNextLine()) {
                        currentLine = scan.nextLine();
                        if (currentLine.contains("id") && currentLine.split(": ")[1].equals(dirName)) {
                            propText.add(currentLine); // id
                            currentLine = scan.nextLine(); //return date
                            propText.add(currentLine); 
                            returnDate = currentLine.split(": ")[1];
                            currentLine = scan.nextLine(); //custumer
                            propText.add(currentLine); 
                            customer = currentLine.split(": ")[1];
                            
                            currentLine = scan.nextLine(); //description
                            if (currentLine.split(": ").length==1) description = "";
                            else description = currentLine.split(": ")[1] +"\r\n";
                            while (scan.hasNext() && !currentLine.contains("id")) { 
                                propText.add(currentLine); 
                                if (!currentLine.contains("description"))
                                    description += currentLine +"\r\n";
                                currentLine = scan.nextLine(); 
                            }
                            if (currentLine.contains("id")) propText.add(currentLine);
                        } 
                        else
                            propText.add(currentLine);
                    }
                    
                    //check fiels (if not null -> dont write anything in prop and just create row in table
                    if (returnDate!=null&&customer!=null&&description!=null) {
                        ((DefaultTableModel) archieveTab.getModel()).addRow(new Object[]{returnDate, customer, description, null});
                    } else { //create new row and adding info abour the order to prop file
                        ((DefaultTableModel) archieveTab.getModel()).addRow(new Object[]{dirName.split(" ")[0], dirName.substring(11), "", null});
                        
                        propText.add("id: " + dirName);
                        propText.add("return date: " + dirName.split(" ")[0]);
                        propText.add("customer: " + dirName.substring(11));
                        propText.add("description: ");
                        setPropText(Path.of(TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt"), propText);
                    }
                }
            }
        }
    }
    /**
     * Updating the current information in the configurations for the current orders in the table.
     * Deleting unnecessary fields in files and updating new ones. <code>markingPropertyFiles</code> and
     * <code>deleteMarkedPropertyFiles</code> used.
     * @param mainTab MainTable JTable
     * @param archieveTab Archieve JTable
     * @throws IOException for file errors
     */
    protected static void actualPropertyInformationUpdate(JTable mainTab, JTable archieveTab) throws IOException {
        markingPropertyFiles(mainTab, TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt");
        markingPropertyFiles(archieveTab, TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt");
        
        deleteMarkedPropertyFiles(TableMethods.getRootPath().toString()+"\\config\\actual_cont.txt");
        deleteMarkedPropertyFiles(TableMethods.getRootPath().toString()+"\\config\\archieve_cont.txt");
    }
    /**
     * Marking unnecessary and relevant fields in configuration files
     * @param table JTable for updating the information which
     * @param pathToProperty Path to the conf file
     * @throws FileNotFoundException if file not found
     * @throws IOException for file errors
     */
    private static void markingPropertyFiles(JTable table, String pathToProperty) throws FileNotFoundException, IOException {
        //for actual 
        ArrayList<String> propText = new ArrayList<>();
        ArrayList<String> mainTableIDs = new ArrayList<>();
        
        if (!table.getName().equals("Archieve"))
            for (int i=0; i<table.getRowCount(); i++)
                mainTableIDs.add((String)table.getValueAt(i, 0)+" "+(String)table.getValueAt(i, 2));
        else
            for (int i=0; i<table.getRowCount(); i++)
                mainTableIDs.add((String)table.getValueAt(i, 0)+" "+(String)table.getValueAt(i, 1));
        
        try (FileReader fileReader = new FileReader(pathToProperty)) {
            Scanner scan = new Scanner(fileReader);
            String currentString = "";
            while (scan.hasNextLine()) {
                currentString = scan.nextLine();
                if(currentString.contains("id")) { //marking as 
                    if (mainTableIDs.contains(currentString.split(": ")[1])) {
                        propText.add("@isChecked");
                        propText.add(currentString);
                        mainTableIDs.remove(currentString.split(": ")[1]);
                    } else {
                        propText.add("@Outdated");
                        propText.add(currentString);
                    }
                }
                else
                    propText.add(currentString);
            }
        }
        //writing new property info to file
        setPropText(Path.of(pathToProperty), propText);
    }
    
    /**
     * removing markers and outdated fields
     * @param pathToProperty String path to config files
     * @throws FileNotFoundException if file not found
     * @throws IOException for file errors
     */
    private static void deleteMarkedPropertyFiles(String pathToProperty) throws FileNotFoundException, IOException {
        ArrayList<String> propText = new ArrayList<>();
        FileReader reader = new FileReader(pathToProperty);
        Scanner scan = new Scanner(reader);
        
        while (scan.hasNext())
            propText.add(scan.nextLine());
        reader.close();
        
        boolean isOutdated = false ;
        FileWriter writer = new FileWriter(pathToProperty);
        for (String line: propText) {
            if (line.contains("@Outdated")) {
                isOutdated = true;
            }
            else if (line.contains("@isChecked")) {
                isOutdated = false;
            }
            else if (!isOutdated) {
                writer.write(line +"\r\n");
            }
        }
        writer.close();
    }
    /**
     * writing the configuration to the appropriate file
     * @param toProp Path to config files
     * @param propText the text that will be written in file
     * @throws FileNotFoundException if file not found
     * @throws IOException for file errors
     */
    private static void setPropText(Path toProp, ArrayList<String> propText) throws FileNotFoundException, IOException {
        try ( FileWriter writer = new FileWriter(toProp.toString())) {
            for (String line : propText) {
                writer.write(line + "\r\n");
            }
        }
    }
    /**
     * getting directories by the specified path
     * @param toSearchArea Path to directory to search in
     * @return ArrayList of directories Paths
     * @throws IOException for file errors
     */
    private static ArrayList<Path> getDirs(Path toSearchArea) throws IOException {
        ArrayList<Path> directories = new ArrayList<>();
            try (Stream<Path> paths = Files.walk(toSearchArea)) {
                paths
                    .filter(Files::isDirectory)
                    .forEach(directories::add);
            }
            directories.remove(0);
            
            return directories;
    }
}
>>>>>>> Stashed changes

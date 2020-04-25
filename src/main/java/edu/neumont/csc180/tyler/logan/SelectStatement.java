package edu.neumont.csc180.tyler.logan;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SelectStatement {
    Scanner reader = new Scanner(System.in);
    SelectStatementRegex ssRegex = new SelectStatementRegex();
    String selectPart1;
    String selectPart2;
    String selectPart3;
    boolean validQuery;

    public void selectQuery() throws IOException {
        while(!validQuery){
            System.out.println("Format:");
            System.out.println("SELECT col1,col2");
            System.out.println("FROM table");
            System.out.println("WHERE col2 [>= OR > OR = OR < OR <=] 'value';");
            System.out.println("*Note: WHERE statement is optional. If not needed, put semicolon at the end of FROM statement instead");
            System.out.println("Enter your query OR type 'Quit' to go back to the menu:");
//            selectPart1 = reader.nextLine();
//            if(selectPart1.equalsIgnoreCase("Quit")){
//                break;
//            }
//            selectPart2 = reader.nextLine();
//            if(selectPart2.charAt(selectPart2.length()-1) != ";"){
////            selectPart3 = reader.nextLine();
//            }
//            else{
//                selectPart3 = "";
//            }
            String filler = reader.nextLine();
            selectPart1 = "SELECT col";
            selectPart2 = "FROM testTable1";
            selectPart3 = "WHERE name = 'LoganTyler';";
            validQuery = isQueryRegexValid(selectPart1, selectPart2, selectPart3);
        }
    }

    private boolean isQueryRegexValid(String part1, String part2, String part3) throws IOException {
        if(Pattern.matches(ssRegex.getSelectPart1Regex(),part1)){
            if(Pattern.matches(ssRegex.getSelectPart2Regex(), part2)){
                if(!part3.equals("")){
                    if(Pattern.matches(ssRegex.getSelectPart3Regex(),part3)){
                        return isQueryPartsValid(part1, part2, part3);
                    }
                    else{
                        System.out.println("Third line of query was inputted incorrectly. Did you forget to put the " +
                                "filepath in single quotes and put a semicolon at the end?");
                        return false;
                    }
                }
                else{
                    return isQueryPartsValid(part1, part2, part3);
                }
            }
            else{
                System.out.println("Second line of query was inputted incorrectly. Did you input the table name correctly'?\n");
                return false;
            }
        }
        else{
            System.out.println("First line of query was inputted incorrectly. Did you leave any spaces when " +
                    "listing your column names?\n");
            return false;
        }
    }

    private boolean isQueryPartsValid(String part1, String part2, String part3) throws IOException {
        String tableName = "tables/" + part2.substring(5) + ".txt";
        System.out.println(tableName);
        Path tablePath  = Paths.get(tableName);
        if(!(Files.exists(tablePath) && Files.isRegularFile(tablePath))){ //check for file in tables dir
            System.out.println("Table reference does not exist or has been altered.\n");
            return false;
        }
        BufferedReader fileReader = new BufferedReader(new FileReader(tableName));
        String filePath = fileReader.readLine();
        Path dataPath = Paths.get(filePath);
        if(!(Files.exists(dataPath) && Files.isRegularFile(dataPath))){ //check for file in tables dir
            System.out.println("Data file in table reference has been moved or altered.\n");
            return false;
        }
        //array for cols in file - check if inputted cols are in file
        List<String> colListInFile = new ArrayList<>();
        fileReader.readLine();
        String commaListInFile = fileReader.readLine();
        String[] columns = commaListInFile.split(",");
        for(String column:columns){
            colListInFile.add(column);
        }
        List<String> inputtedColumns = new ArrayList<>();
        String commaListOfColumns = part1.substring(7);
        String[] cols = commaListOfColumns.split(",");
        for(String col:cols){
            inputtedColumns.add(col);
        }
        if(!part3.equals("")){
            String extraCol = part3.substring(part3.indexOf(" ") + 1, part3.indexOf(" ", part3.indexOf(" ") + 1));
            inputtedColumns.add(extraCol); //last column will be WHERE column if part3 is not empty
        }
        if(!colListInFile.containsAll(inputtedColumns)){
            System.out.println(colListInFile);
            System.out.println(inputtedColumns);
            System.out.println("One or more columns inputted do not exist in the table.");
            return false;
        }
        beginSelectQuery(part1, part2, part3, inputtedColumns, colListInFile, dataPath); //first query, second query, third query, columns in query, all columns in table, path to data
        return true;
    }

    private void beginSelectQuery(String part1, String part2, String part3, List<String> inputtedColumns, List<String> colListInFile, Path dataPath) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(String.valueOf(dataPath)));
        String fileLine = null;
        for(int i=0; i<inputtedColumns.size()-1;i++){
            System.out.print(String.format("%-25s", inputtedColumns.get(i)));
        }
        System.out.println("");
        System.out.println("------------------------------------------------------------------------------------------");
        while((fileLine = fileReader.readLine()) != null){
            if(!part3.equals("")){
                String whereColumn = inputtedColumns.get(inputtedColumns.size() - 1);
                int indexOfWhere =  colListInFile.indexOf(whereColumn);
                String[] dataColumns = fileLine.split(";");
                String[] partsOfQuery = part3.split(" ");
                String value = partsOfQuery[3].substring(1, partsOfQuery[3].length() - 1);
                String operator = partsOfQuery[2];
                switch(operator){
                    case "<=":
                        if(!(dataColumns[indexOfWhere].compareTo(value) == -1 || dataColumns[indexOfWhere].compareTo(value) == 0)){
                            continue;
                        }
                        break;
                    case "<":
                        if(!(dataColumns[indexOfWhere].compareTo(value) == -1)){
                            continue;
                        }
                        break;
                    case "=":
                        if(!(dataColumns[indexOfWhere].compareTo(value) == 0)){
                            continue;
                        }
                        break;
                    case ">":
                        if(!(dataColumns[indexOfWhere].compareTo(value) == 1)){
                            continue;
                        }
                        break;
                    case ">=":
                        if(!(dataColumns[indexOfWhere].compareTo(value) == 1 || dataColumns[indexOfWhere].compareTo(value) == 0)){
                            continue;
                        }
                        break;
                    default:
                        System.out.println("Invalid operator");
                }
            String[] dataColumnsForQuery = fileLine.split(";");
            for(int i=0;i<inputtedColumns.size()-1;i++){
                String data = dataColumnsForQuery[Integer.parseInt(colListInFile.get(colListInFile.indexOf(inputtedColumns.get(i))))];
                System.out.print(String.format("%-25s", data));
            }
                System.out.println("");
            }
        }
    }
}

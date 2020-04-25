package edu.neumont.csc180.tyler.logan;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            selectPart1 = "SELECT name";
            selectPart2 = "FROM testTable1";
            selectPart3 = "WHERE name > 'Anthony';";
            validQuery = isQueryRegexValid(selectPart1, selectPart2, selectPart3);
        }
    }

    private boolean isQueryRegexValid(String part1, String part2, String part3) throws IOException {
        if(Pattern.matches(ssRegex.getSelectPart1Regex(),part1)){
            if(Pattern.matches(ssRegex.getSelectPart2Regex(), part2)){
                if(!part3.equals("")){
                    if(Pattern.matches(ssRegex.getSelectPart2Regex(),part3)){
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
        String tableName = part2.substring(5) + ".txt";
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
        return false;
    }
}

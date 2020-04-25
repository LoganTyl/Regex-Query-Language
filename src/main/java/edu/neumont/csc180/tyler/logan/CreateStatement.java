package edu.neumont.csc180.tyler.logan;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class CreateStatement {
    Scanner reader = new Scanner(System.in);
    CreateStatementRegex csRegex = new CreateStatementRegex();
    String createPart1;
    String createPart2;
    String createPart3;
    boolean validQuery;

    public void createQuery() throws IOException {
        validQuery = false;
        while(!validQuery){
            System.out.println("Format:");
            System.out.println("CREATE TABLE 'table' (arg1,arg2,arg3):");
            System.out.println("line format /<regex with column name in its own regex group>/:");
            System.out.println("file 'filepath';\n");
            System.out.println("Enter your query OR type 'Quit' to go back to the menu:");
//            createPart1 = reader.nextLine();
//            if(createPart1.equalsIgnoreCase("Quit")){
//                break;
//            }
//            createPart2 = reader.nextLine();
//            createPart3 = reader.nextLine();
            String filler = reader.nextLine();
            createPart1 = "CREATE TABLE 'testTable1' (name,col):";
            createPart2 = "line format /([A-Z][a-zA-Z]*) - ([a-zA-Z0-9]+)/:";
            createPart3 = "file 'C:\\Users\\Logan Tyler\\Desktop\\Classes\\Quarter 7\\Open Source Platforms Dev\\Regex Query Language\\testFiles\\1.txt';";
            validQuery = isQueryRegexValid(createPart1, createPart2, createPart3);
        }
    }

    private boolean isQueryRegexValid(String part1, String part2, String part3) throws IOException {
        if(Pattern.matches(csRegex.getCreatePart1Regex(),part1)){
            if(Pattern.matches(csRegex.getCreatePart2Regex(), part2)){
                if(Pattern.matches(csRegex.getCreatePart3Regex(),part3)){
                    return isQueryPartsValid(part1, part2, part3);
                }
                else{
                    System.out.println("Third line of query was inputted incorrectly. Did you forget to put the " +
                            "filepath in single quotes and put a semicolon at the end?");
                    return false;
                }
            }
            else{
                System.out.println("Second line of query was inputted incorrectly. Did you forget to put a " +
                        "space between 'format' and '/'?\n");
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
        String argsString = part1.substring(part1.indexOf("(") + 1, part1.indexOf(")"));
        String[] args = argsString.split(",");
        String tableName = part1.replace("CREATE TABLE '", "");
        tableName = tableName.substring(0, tableName.indexOf("'"));
        part2 = part2.replace("line format /","");
        part2 = part2.concat("///////");
        part2 = part2.replace("/:///////","");
        String[] matches = Pattern.compile(csRegex.getGroupingRegex())
                .matcher(part2)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);
        if(args.length != matches.length){
            System.out.println("The amount of column names and regex are not equal. Did you put " +
                    "every regex in its own group?\n");
            return false;
        }
        part3 = part3.substring(6);
        part3 = part3.replace("';", "");
        Path path = Paths.get(part3);
        if(!(Files.exists(path) && Files.isRegularFile(path))){
            System.out.println("Given file does not exist. Please make sure file path is correct.\n");
            return false;
        }
        BufferedReader fileReader = new BufferedReader(new FileReader(part3));
        String fileLine = null;
        while((fileLine = fileReader.readLine()) != null){
            if(!Pattern.matches(part2, fileLine)){
                System.out.println("A line in the file does not match the provided regex.");
                return false;
            }
        }
        if(uniqueTableName(tableName)){
            createTableFile(part2, args, matches, tableName);
            return true;
        }
        else{
            System.out.println("A table with this name already exists");
            return false;
        }
    }

    private void createTableFile(String completeRegex, String[] columnNames, String[] columnRegexes, String tableName) throws IOException {
        File tableFile = new File("tables/" + tableName + ".txt");
        if (!tableFile.createNewFile()) {
            System.out.println("A table with this name already exists");
        } else {
            BufferedWriter writer = new BufferedWriter(new FileWriter(tableFile, true));
            writer.write(tableFile.getAbsolutePath());
            writer.newLine();
            writer.write(completeRegex);
            writer.newLine();
            for(int i=0; i<columnNames.length; i++){
                writer.write(columnNames[i] + ": " + columnRegexes[i]);
                writer.newLine();
            }
            writer.close();
        }
    }

    private boolean uniqueTableName(String tableName){
        File[] tables = new File("tables/").listFiles();
        for(File table: tables){
            if(table.getName().equals(tableName + ".txt")){
                return false;
            }
        }
        return true;
    }
}
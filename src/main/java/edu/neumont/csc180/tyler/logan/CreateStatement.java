package edu.neumont.csc180.tyler.logan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class CreateStatement {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    CreateStatementRegex csRegex = new CreateStatementRegex();
    String createStatement;
    String createPart1;
    String createPart2;
    String createPart3;
    boolean validQuery;
    int argCount;

    public void createQuery() throws IOException {
        validQuery = false;
        argCount = 0;
        createStatement = "";
        while(!validQuery){
            System.out.println("Format:");
            System.out.println("CREATE TABLE 'table' (arg1,arg2,arg3):");
            System.out.println("line format /<regex with column name in its own regex group>/:");
            System.out.println("file 'filepath';\n");
            System.out.println("Enter your query:");
            createPart1 = reader.readLine();
            createPart2 = reader.readLine();
            createPart3 = reader.readLine();
            validQuery = isQueryRegexValid(createPart1, createPart2, createPart3);
        }
    }

    private boolean isQueryRegexValid(String part1, String part2, String part3){
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

    private boolean isQueryPartsValid(String part1, String part2, String part3){
        String[] args = part1.split(",");
        String lineFormatRegex = part2.replace("line format /","");
        lineFormatRegex.concat("///////");
        lineFormatRegex = part2.replace("/:///////","");
        String[] matches = Pattern.compile("\\((([^()]*+)(?:(/?R)(/?2))*)\\)")
                .matcher(part2)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);
        if(args.length != matches.length){
            System.out.println(args);
            System.out.println(matches);
            System.out.println("The amount of column names and regex are not equal. Did you put " +
                    "every regex in its own group?\n");
            return false;
        }
        String filepath = part3.replace("file '", "");
        filepath = part3.replace("';", "");
        Path path = Paths.get(filepath);
        if(!(Files.exists(path) && Files.isRegularFile(path))){
            System.out.println("Given file does not exist. Please make sure file path is correct.\n");
            return false;
        }
        //check each line of file meets regex
        //create ref file in tables dir
    }
}

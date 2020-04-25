package edu.neumont.csc180.tyler.logan;

public class SelectStatementRegex {
    private final String selectPart1Regex = "SELECT [a-zA-Z0-9_*]+(,[a-zA-Z0-9_]+)*";
    private final String selectPart2Regex = "FROM [a-zA-Z][a-zA-Z0-9]*";
    private final String selectPart3Regex = "WHERE [a-zA-Z0-9_]+ [>=|>|=|<|<=] '.+'";

    public String getSelectPart1Regex() {
        return selectPart1Regex;
    }

    public String getSelectPart2Regex() {
        return selectPart2Regex;
    }

    public String getSelectPart3Regex() {
        return selectPart3Regex;
    }
}

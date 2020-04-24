package edu.neumont.csc180.tyler.logan;

public class CreateStatementRegex {
    private final String createPart1Regex = "CREATE TABLE '[a-zA-Z][a-zA-Z0-9]*' \\([a-zA-Z0-9_]+(,[a-zA-Z0-9_]+)*\\)";
    private final String createPart2Regex = "line format \\/.+\\/:";
    private final String createPart3Regex = "file '.+';";
    private final String groupingRegex = "\\((([^()]*+)(?:(/?R)(/?2))*)\\)";

    public String getGroupingRegex() {
        return groupingRegex;
    }

    public String getCreatePart1Regex() {
        return createPart1Regex;
    }

    public String getCreatePart2Regex() {
        return createPart2Regex;
    }

    public String getCreatePart3Regex() {
        return createPart3Regex;
    }
}

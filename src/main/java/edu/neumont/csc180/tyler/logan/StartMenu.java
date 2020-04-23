package edu.neumont.csc180.tyler.logan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartMenu {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public void queryOption() throws IOException {
        while(true){
            System.out.println("0) Create Table\n1) Select Query\n2)Exit Application");
            System.out.println("Choose an Option: ");
            String option = reader.readLine();
            switch(option){
                case "0":
                    //create table query
                    break;
                case "1":
                    //select query;
                    break;
                case "2":
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please select either 0, 1, or 2");
        }
        }
    }
}

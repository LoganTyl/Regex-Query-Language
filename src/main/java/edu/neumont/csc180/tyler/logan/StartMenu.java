package edu.neumont.csc180.tyler.logan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartMenu {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    CreateStatement createStatement = new CreateStatement();

    public void queryOption() throws IOException {
        while(true){
            System.out.println("\n0) Create Table\n1) Select Query\n2) Exit Application\n");
            System.out.print("Choose an Option: ");
            String option = reader.readLine();
            switch(option){
                case "0":
                    createStatement.createQuery();
                    break;
                case "1":
                    //select query;
                    System.out.println("Select Query");
                    break;
                case "2":
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please select either 0, 1, or 2");
        }
        }
    }
}

package main.java.recipe;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;

/**
 *
 * @author Team Recipe
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        RecipeList recipelist = new RecipeList();
        Boolean login = false;

        String command;
        System.out.println("Welcome to recipe application");
        
        while (true) {
            System.out.println("Do you have an account? (Y/N)");
            command = sc.nextLine();

            if (command.equalsIgnoreCase("Y")) {
                System.out.println("Login:");
                login = User.login();
                for (int i = 0; i < 2; i++) { //3 attempts for login
                    if (login) {
                        System.out.println("Login successful!");
                        break;
                    }
                    System.out.println("Wrong username or password!");
                    login = User.login();
                }
                if (login) break;
            }

            if (command.equalsIgnoreCase("N")) {
                System.out.println("Please register a new account!");
                int status = User.register();
                if (status == 1) {
                    System.out.println("Register successfully!");
                }
            }
        }
        
        while (login) {
            System.out.println("\nGive command:");
            command = sc.nextLine();
            if (command.equalsIgnoreCase("list")) {
                System.out.println(recipelist);
            }
            if(command.equalsIgnoreCase("add")) {
                recipelist.addRecipe();
            }
            if (command.equalsIgnoreCase("delete")) {
                recipelist.deleteRecipe();
            }
            if(command.equalsIgnoreCase("searchtag")){
                System.out.println("Give recipe tags");
                String tags = sc.nextLine();
                recipelist.searchByTags(tags);
            }
            if(command.equalsIgnoreCase("searchname")){
                System.out.println("Give recipe name");
                String name = sc.nextLine();
                recipelist.searchByName(name);
            }
            if(command.equalsIgnoreCase("searchid")){
                System.out.println("Give recipe id ");
                int i = Integer.parseInt(sc.nextLine());
                System.out.println(recipelist.searchById(i));
            }
            if(command.equalsIgnoreCase("searching")){
                System.out.println("Give ingredients");
                String ingredients= sc.nextLine();
                for (Recipe recipe : recipelist.searchByIngredients(ingredients)) {
                    System.out.println(recipe);
                }
            }
            if(command.equalsIgnoreCase("suggest")){
                recipelist.suggestRecipe();
            }
            if(command.equalsIgnoreCase("help") || command.equalsIgnoreCase("commands")) {
                System.out.println("Available commands: list, add, delete, searchtag, "
                        + "searchname, searchid, searching, suggest, exit");
            }
            if(command.equalsIgnoreCase("exit")) {
                break;
            }
            if(command.equalsIgnoreCase("drop1337ez")) {
                recipelist.dropTable();
            }
        }

        //stmt.executeUpdate("DROP TABLE IF EXISTS test");
        //stmt.executeUpdate("CREATE TABLE users (ID SERIAL,username varchar(255), password varchar(255), allergies varchar(1000))");
        //stmt.executeUpdate("CREATE TABLE recipes (ID SERIAL, name varchar(255), ingredients varchar(1000), instructions varchar(10000), tags varchar(1000))");
        //stmt.executeUpdate("CREATE TABLE test (name varchar(255),pena varchar(255))");
        //stmt.executeUpdate("INSERT INTO test VALUES ('testing',('again','asd'))");
        //stmt.executeUpdate("INSERT INTO users VALUES ('pena', 'pena', ('peanut','fish'))");    
        //ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        //For printing 'Show tables'
        /*
        ResultSet rs = stmt.executeQuery("SELECT table_name\n" +
                "  FROM information_schema.tables\n" +
                " WHERE table_schema='public'\n" +
                "   AND table_type='BASE TABLE';");
         */
 /*
        while (rs.next()) {
            //rs.getString(1),rs.getString(2)....
            System.out.println(rs.getString(1));
            //System.out.println("Read from DB: " + rs.getTimestamp("tick"));
        }
         */
    }
}
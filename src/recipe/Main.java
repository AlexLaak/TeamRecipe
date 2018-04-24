/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipe;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author Team Recipe
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Connection connection = getConnection();

        Statement stmt = connection.createStatement();

        Scanner sc = new Scanner(System.in);
        RecipeList recipelist = new RecipeList();

        String command;
        System.out.println("Welcome to recipe application");
        while (true) {
            System.out.println("Give command");
            command = sc.nextLine();
            if (command.equals("list")) {
                System.out.println(recipelist);
            }
        }

        //stmt.executeUpdate("DROP TABLE IF EXISTS test");
        //stmt.executeUpdate("CREATE TABLE users (username varchar(255), password varchar(255), allergies varchar(1000))");
        //stmt.executeUpdate("CREATE TABLE recipes (name varchar(255), ingredients varchar(1000), instructions varchar(10000), tags varchar(1000))");
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

    //Connection to database. Dont modify this
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI("postgres://hkullkvwogqmsk:57f03e071224c52d64f523e55c0105096f73c5e1fc5519ac6e4af2461419ebdd@ec2-54-217-217-142.eu-west-1.compute.amazonaws.com:5432/d1ur8rbqa8ddlp");
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        return DriverManager.getConnection(dbUrl, username, password);
    }

}

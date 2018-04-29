/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.recipe;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author Kenji Fam
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String allergies;

    public User() {
    }

    public User(String username, String password, String allergies) {
        this.username = username;
        this.password = password;
        this.allergies = allergies;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
    
    @Override
    public String toString(){
        return "User: " + this.id + " " + this.username + " " + this.password + " " + this.allergies;
    }
    
    public static int register() {
        int status = 0;
        try {
            Connection connection = getConnection();
            Statement stm = connection.createStatement();
            Scanner s = new Scanner(System.in);
            System.out.print("Username: ");
            String username = s.nextLine();
            System.out.print("Password: ");
            String password = s.nextLine();
            System.out.print("Allergies: ");
            String allergies = s.nextLine();
            
            status = stm.executeUpdate("INSERT INTO users (username, password, allergies) VALUES ('" + username + "','" + password + "','" + allergies + "')");
            connection.close();
        } catch (URISyntaxException | SQLException e) {
            System.out.println(e);
        }
        return status;
    }
    
    public static int delete() {
        int status=0;
        try{
            Connection connection = getConnection();
            Statement stm = connection.createStatement();
            Scanner s = new Scanner(System.in);
            System.out.print("Delete user with id: ");
            int id = s.nextInt();
            ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE id = '" + id + "'");
            
            if (rs.next()) {
                status = stm.executeUpdate("DELETE FROM users WHERE id = '" + id + "'");
            } else {
                System.out.println("There is not a user with that ID!");
            }           
            connection.close();
        }catch(URISyntaxException | SQLException e) {
            System.out.println(e);
        }
        return status;
    }
    
    public static boolean login() {
        boolean status = false;
        try{
            Connection connection = getConnection();
            Statement stm = connection.createStatement();
            Scanner s = new Scanner(System.in);
            System.out.print("Username: ");
            String username = s.nextLine();
            System.out.print("Password: ");
            String password = s.nextLine();
            ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE username = '" + username + "' and password = '" + password + "'");
            status = rs.next();
            connection.close();
        }catch(URISyntaxException | SQLException e){
            System.out.println(e);
        }
        return status;
    }
    
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI("postgres://hkullkvwogqmsk:57f03e071224c52d64f523e55c0105096f73c5e1fc5519ac6e4af2461419ebdd@ec2-54-217-217-142.eu-west-1.compute.amazonaws.com:5432/d1ur8rbqa8ddlp");
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        return DriverManager.getConnection(dbUrl, username, password);
    }
}

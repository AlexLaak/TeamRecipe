/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.recipe.UI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.recipe.User;

/**
 * FXML Controller class
 *
 * @author Kenji Fam
 */
public class LoginController {
    
    @FXML
    private StackPane loginPane;
    
    @FXML
    private GridPane grid;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Text actiontarget;
    
    @FXML
    private TextField username_reg;
    
    @FXML
    private PasswordField password_reg;
    
    @FXML
    private TextField allergies_reg;
    
    private static User user;

    /**
     * Initializes the controller class.
     */
    public void initialize() {
        
    }

    public static User getUser() {
        return user;
    }
    
    @FXML
    private void login() {
        user = checkUser();
            if (user != null) {
                try {
                    actiontarget.setText("Login successful!");
                    Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
                    Stage stage = (Stage) grid.getScene().getWindow();
                    stage.setScene(new Scene(root, 800, 600));
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            else actiontarget.setText("Wrong username or password!");
    }
    
    private User checkUser() {
        boolean status = false;
        try{
            String username = usernameField.getText();
            String password = passwordField.getText();
            Connection connection = getConnection();
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE username = '" + username + "' and password = '" + password + "'");
            status = rs.next();
            connection.close();
            if(!status) return null;
            String allergy = rs.getString("allergies");
            return new User(username,password,allergy);
        }catch(URISyntaxException | SQLException e){
            System.out.println(e);
        }
        return null;
    }
    
    @FXML
    private void showRegister() throws IOException, URISyntaxException, SQLException, ClassNotFoundException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(loginPane.getScene().getWindow());
        dialog.setTitle("Register");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("register.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.setHeaderText("Fill in the form below to create new account");
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        
        ButtonType reg = new ButtonType("Create new account", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(reg);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == reg) {
            LoginController loginController = fxmlLoader.getController();
            loginController.register();
        }
    }
    
    private void register() {
        try {
            Connection connection = getConnection();
            Statement stm = connection.createStatement();
            String username = username_reg.getText();
            String password = password_reg.getText();
            String allergies = allergies_reg.getText();
            stm.executeUpdate("INSERT INTO users (username, password, allergies) VALUES ('" + username + "','" + password + "','" + allergies + "')");
            connection.close();
        } catch (URISyntaxException | SQLException e) {
            System.out.println(e);
        }
    }
    
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI("postgres://hkullkvwogqmsk:57f03e071224c52d64f523e55c0105096f73c5e1fc5519ac6e4af2461419ebdd@ec2-54-217-217-142.eu-west-1.compute.amazonaws.com:5432/d1ur8rbqa8ddlp");
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        return DriverManager.getConnection(dbUrl, username, password);
    }
}

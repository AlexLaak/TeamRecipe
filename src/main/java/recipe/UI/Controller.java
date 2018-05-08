/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.recipe.UI;

import main.java.recipe.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kenji Fam
 */
public class Controller{
    
    @FXML
    private BorderPane mainPanel;
    
    @FXML
    private StackPane contentPanel;
    
    private RecipeList recipelist;
    
    private static ArrayList<Recipe> recipeArray;
    
    /**
     * Initializes the controller class.
     */
    public void initialize() throws ClassNotFoundException, SQLException, URISyntaxException {
        recipeArray = RecipeList.getAllRecipes();
        showHome();
    }
    
    public static ArrayList<Recipe> getRecipeArray() {
        return recipeArray;
    }
    
    @FXML
    public void showListRecipe() throws ClassNotFoundException, SQLException, URISyntaxException, IOException {
        /*recipelist = new RecipeList();
        System.out.println(recipelist);*/
        Pane listPane = FXMLLoader.load(getClass().getResource("recipelist.fxml"));
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(listPane); 
    }
    
    @FXML
    public void showAddRecipe() throws IOException, URISyntaxException, SQLException, ClassNotFoundException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add New Recipe");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("form.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.setHeaderText("Fill in the information for adding new recipe");
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            FormController formController = fxmlLoader.getController();
            formController.addRecipe();
            recipeArray = RecipeList.getAllRecipes();
            showListRecipe(); 
        }
    }
    
    @FXML
    public void showHome() {
        Text scenetitle = new Text("WELCOME TO FIFTH RECIPE");
        scenetitle.setId("welcome-text");
        scenetitle.setFont(Font.font("Arial", 30.0));
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(scenetitle);
    }
    
    @FXML
    public void showUser() throws IOException {
        Pane userPane = FXMLLoader.load(getClass().getResource("userpage.fxml"));
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(userPane); 
    }
    
    @FXML
    private void signout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to sign out?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("login.fxml"));
                Parent root = fxmlLoader.load();
                LoginController loginController = fxmlLoader.getController();
                loginController.signout();
                Stage stage = (Stage) mainPanel.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
            } catch (IOException ex) {
                System.out.println(ex);
            }
        });
    }
}

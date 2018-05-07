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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add New Recipe");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("form.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

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
}

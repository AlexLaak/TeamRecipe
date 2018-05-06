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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Kenji Fam
 */
public class Controller{
    
    @FXML
    private BorderPane mainPanel;
    
    @FXML
    private Pane contentPanel;
    
    private RecipeList recipelist;
    
    private static ArrayList<Recipe> recipeArray;
    

    /**
     * Initializes the controller class.
     */
    public void initialize() throws ClassNotFoundException, SQLException, URISyntaxException {
        recipeArray = RecipeList.getAllRecipes();
    }
    
    @FXML
    public void showListRecipe() throws ClassNotFoundException, SQLException, URISyntaxException, IOException {
        /*recipelist = new RecipeList();
        System.out.println(recipelist);*/
        Pane listPane = FXMLLoader.load(getClass().getResource("recipelist.fxml"));
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(listPane); 
    }

    public static ArrayList<Recipe> getRecipeArray() {
        return recipeArray;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.recipe.UI;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.java.recipe.Recipe;
import main.java.recipe.RecipeList;

/**
 * FXML Controller class
 *
 * @author Kenji Fam
 */
public class RecipelistController {
    
    @FXML
    private VBox boxRecipe;
    
    @FXML
    private Label idLabel;
    
    @FXML
    private Label id;
    
    @FXML
    private Text name;
    
    @FXML
    private Label tagsLabel;
    
    @FXML
    private Text tags;
    
    @FXML
    private Label ingredientsLabel;
    
    @FXML
    private Text ingredients;
    
    @FXML
    private Label instructionsLabel;
    
    @FXML
    private Text instructions;
    
    @FXML
    private ImageView recipeImage;
    
    private RecipeList recipelist;
    
    private ArrayList<Recipe> recipeArray;

    /**
     * Initializes the controller class.
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     * @throws java.net.URISyntaxException
     */
    public void initialize() throws ClassNotFoundException, SQLException, URISyntaxException {
        recipelist = new RecipeList();
        recipeArray = Controller.getRecipeArray();
        boxRecipe = showList(recipeArray, boxRecipe);
    }
    
    private VBox showList(ArrayList<Recipe> recipeArray, VBox vb) {
        recipeArray.forEach(rcp -> {
            VBox rcpBox = new VBox();
            Text rcpTitle = new Text("Recipe #" + rcp.getId() + " " + rcp.getName());
            Text rcpTags = new Text("\tTags: " + rcp.getTags());
            rcpBox.getChildren().addAll(rcpTitle, rcpTags);
            rcpBox.setOnMouseClicked(event -> showRecipe(rcp));
            vb.getChildren().add(rcpBox);
            VBox.setMargin(rcpBox, new Insets(10, 0, 20, 10));
        });
        return vb;
    }
    
    private void showRecipe(Recipe rcp) {
        idLabel.setText("Recipe ID");
        tagsLabel.setText("Tags");
        ingredientsLabel.setText("Ingredients");
        instructionsLabel.setText("Instruction");
        id.setText("" + rcp.getId());
        name.setText(rcp.getName());
        tags.setText(rcp.getTags());
        ingredients.setText(rcp.getIngredients());
        instructions.setText(rcp.getInstructions());
        recipeImage.setImage(new Image(MainUI.class.getResourceAsStream("recipe.jpg")));
    }
    
}

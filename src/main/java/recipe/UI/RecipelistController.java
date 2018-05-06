/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.recipe.UI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private AnchorPane mainPane;
    
    @FXML
    private VBox boxRecipe;
    
    @FXML
    private Button btn_edit;
    
    @FXML
    private Button btn_delete;
    
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
    
    @FXML
    private AnchorPane secondAnchor;
    
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
        recipeArray = RecipeList.getAllRecipes();
        recipeArray.sort((Recipe o1, Recipe o2) -> o1.getId() - o2.getId());  // After update the recipe go to the end of the list in database -> need sort
        showList();
        showRecipe(recipeArray.get(recipeArray.size() - 1));
    }
    
    public void showList() {
        boxRecipe.getChildren().clear();
        recipeArray.forEach(rcp -> {
            VBox rcpBox = new VBox();
            Text rcpTitle = new Text("Recipe #" + rcp.getId() + " " + rcp.getName());
            Text rcpTags = new Text("\tTags: " + rcp.getTags());
            rcpBox.getChildren().addAll(rcpTitle, rcpTags);
            rcpBox.setOnMouseClicked(event -> showRecipe(rcp));
            boxRecipe.getChildren().add(rcpBox);
            VBox.setMargin(rcpBox, new Insets(10, 0, 20, 10));
        });
    }
    
    public void showRecipe(Recipe rcp) {
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
        btn_edit.setOnAction(event -> {
            try {
                showEditRecipeDialog(rcp);
            } catch (URISyntaxException | SQLException | ClassNotFoundException ex) {
                Logger.getLogger(RecipelistController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void showAddRecipeForm() throws IOException {
        Pane listPane = FXMLLoader.load(getClass().getResource("form.fxml"));
        secondAnchor.getChildren().clear();
        secondAnchor.getChildren().add(listPane); 
    }
    
    public void showEditRecipeDialog(Recipe rcp) throws URISyntaxException, SQLException, ClassNotFoundException {
        if(rcp == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Recipe Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the recipe you want to edit.");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Edit Recipe");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("form.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        FormController formController = fxmlLoader.getController();
        formController.editRecipe(rcp);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            rcp = formController.updateRecipe(rcp);
            recipeArray = RecipeList.getAllRecipes();
            recipeArray.sort((Recipe o1, Recipe o2) -> o1.getId() - o2.getId());
            showList();
            showRecipe(rcp);
        }
    }
    
}

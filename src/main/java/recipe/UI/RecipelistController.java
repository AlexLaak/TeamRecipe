/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.recipe.UI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
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
    
    @FXML
    private TextField searchField;
    
    @FXML
    private RadioButton radiobtn_id;
    
    @FXML
    private RadioButton radiobtn_name;
    
    @FXML
    private RadioButton radiobtn_ingre;
    
    @FXML
    private RadioButton radiobtn_tags;
    
    @FXML
    private Button btn_suggest;
    
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
        showList(recipeArray);
        showRecipe(recipeArray.get(recipeArray.size() - 1));
    }
    
    public void showList(ArrayList<Recipe> recipeArray) {
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
        btn_delete.setOnAction(event -> {
            deleteRecipe(rcp);
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
            dialog.setHeaderText("Change here the information for editing recipe");
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
            showList(recipeArray);
            showRecipe(rcp);
        }
    }
    
    public void deleteRecipe(Recipe rcp) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this recipe?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
            try {
                Connection connection = FormController.getConnection();
                Statement stm = connection.createStatement();
                stm.executeUpdate("DELETE FROM recipes WHERE id = '" + rcp.getId() + "'");
                recipeArray = RecipeList.getAllRecipes();
                recipeArray.sort((Recipe o1, Recipe o2) -> o1.getId() - o2.getId());
                showList(recipeArray);
                showRecipe(recipeArray.get(recipeArray.size() - 1));
                connection.close();
            } catch (URISyntaxException | SQLException | ClassNotFoundException ex) {
                System.out.println(ex);
            }
        });
    }
    
    
    @FXML
    public void search() {
        ArrayList<Recipe> list = new ArrayList<>();
        String search = searchField.getText();
        if (radiobtn_id.isSelected()) list = searchById(search);
        if (radiobtn_name.isSelected()) list = searchByName(search);
        if (radiobtn_tags.isSelected()) list = searchByTags(search);
        if (radiobtn_ingre.isSelected()) list = searchByIngrdients(search);
        if (!list.isEmpty()) {
            secondAnchor.setVisible(true);
            showList(list);
            showRecipe(list.get(0));
        } else {
            boxRecipe.getChildren().clear();
            Text txt = new Text("Your search \" " + search + " \" did not match any recipes");
            txt.wrappingWidthProperty().bind(boxRecipe.widthProperty());
            boxRecipe.getChildren().add(txt);
            secondAnchor.setVisible(false);
        }
    }
    
    public ArrayList<Recipe> searchById(String search) {
        ArrayList<Recipe> list = new ArrayList<>();
        recipeArray.forEach((Recipe rcp) -> {
            if (search.equals("" + rcp.getId())) list.add(rcp);
        });
        return list;
    }
    
    public ArrayList<Recipe> searchByName(String search) {
        ArrayList<Recipe> list = new ArrayList<>();
        recipeArray.forEach((Recipe rcp) -> {
            if (rcp.getName().toLowerCase().contains(search.toLowerCase())) list.add(rcp);
        });
        return list;
    }
    
    public ArrayList<Recipe> searchByTags(String search) {
        ArrayList<Recipe> list = new ArrayList<>();
        String[] tags = search.split(",");
        for (int i = 0; i < tags.length; i++) tags[i] = tags[i].toLowerCase().trim();
        recipeArray.forEach((Recipe rcp) -> {
            for (int i = 0; i < tags.length; i++) {
                if (!rcp.getTags().toLowerCase().contains(tags[i])) {
                    break;
                }
                if (i == tags.length - 1 && rcp.getTags().toLowerCase().contains(tags[i])) {
                    list.add(rcp);
                }
            }
        });
        return list;
    }
    
    public ArrayList<Recipe> searchByIngrdients(String search) {
        ArrayList<Recipe> list = new ArrayList<>();
        String[] ingres = search.split(",");
        for (int i = 0; i < ingres.length; i++) ingres[i] = ingres[i].toLowerCase().trim();
        recipeArray.forEach((Recipe rcp) -> {
            for (int i = 0; i < ingres.length; i++) {
                if (!rcp.getIngredients().toLowerCase().contains(ingres[i])) {
                    break;
                }
                if (i == ingres.length - 1 && rcp.getIngredients().toLowerCase().contains(ingres[i])) {
                    list.add(rcp);
                }
            }
        });
        return list;
    }
    
    public void suggest() {
        
    }
}

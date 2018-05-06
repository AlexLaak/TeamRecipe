/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.recipe.UI;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.java.recipe.Recipe;

/**
 * FXML Controller class
 *
 * @author Kenji Fam
 */
public class FormController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField ingredientsField;

    @FXML
    private TextArea instructionsField;

    @FXML
    private TextField tagsField;

    public void addRecipe() throws URISyntaxException, SQLException {
        String name = nameField.getText();
        String ingredients = ingredientsField.getText();
        String instructions = instructionsField.getText();
        String tags = tagsField.getText();

        Connection connection = getConnection();
        Statement stm = connection.createStatement();
        stm.executeUpdate("INSERT INTO recipes (name,ingredients,instructions,tags) VALUES ('" + name + "','" + ingredients + "','" + instructions + "','" + tags + "')");
        
        connection.close();
    }

    public void editRecipe(Recipe rcp) {
        nameField.setText(rcp.getName());
        ingredientsField.setText(rcp.getIngredients());
        instructionsField.setText(rcp.getInstructions());
        tagsField.setText(rcp.getTags());
    }

    public Recipe updateRecipe(Recipe rcp) throws URISyntaxException, SQLException {
        rcp.setName(nameField.getText());
        rcp.setIngredients(ingredientsField.getText());
        rcp.setInstructions(instructionsField.getText());
        rcp.setTags(tagsField.getText());

        Connection connection = getConnection();
        Statement stm = connection.createStatement();
        stm.executeUpdate("UPDATE recipes SET name = '" + rcp.getName() + "', ingredients = '" + rcp.getIngredients() + "', instructions = '" 
                + rcp.getInstructions() + "', tags = '" + rcp.getTags() + "' WHERE id = '" + rcp.getId() + "'");
        connection.close();
        
        return rcp;
    }
    
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI("postgres://hkullkvwogqmsk:57f03e071224c52d64f523e55c0105096f73c5e1fc5519ac6e4af2461419ebdd@ec2-54-217-217-142.eu-west-1.compute.amazonaws.com:5432/d1ur8rbqa8ddlp");
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        return DriverManager.getConnection(dbUrl, username, password);
    }
}

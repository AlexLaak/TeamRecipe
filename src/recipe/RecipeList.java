/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipe;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author macbookpro
 */
public class RecipeList {
    private String id;
    private String name;
    private String ingredients;
    private String instructions;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIngredients() {
        return ingredients;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public static ArrayList<RecipeList> getAllRecipes() throws ClassNotFoundException, SQLException {
    Connection conn = DBConnection.getDBConnection().getConnection();
    Statement stm = conn.createStatement();
    String sql = "SELECT * from recipes;";
    ResultSet rst = stm.executeQuery(sql);
    ArrayList<RecipeList> recipeList = new ArrayList<>();
    while (rst.next()) {
        RecipeList recipe = new RecipeList(rst.getString("id"), rst.getString("name"), rst.getString("ingredients"), rst.getString("instructions"));
        recipe.setId(rst.getString("id"));
        recipe.setName(rst.getString("name"));
        recipe.setIngredients(rst.getString("ingredients"));
        recipe.setInstructions(rst.getString("instructions"));
        recipeList.add(recipe);
    }
    return customerList;
}

}
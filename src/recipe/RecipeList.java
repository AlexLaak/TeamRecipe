/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipe;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Kasperi
 */
public class RecipeList {

    ArrayList<Recipe> recipes;

    public RecipeList() throws ClassNotFoundException, SQLException, URISyntaxException {
        recipes = getAllRecipes();
    }

    public Recipe searchById(int id) {
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }
        return null;
    }

    public Recipe searchByName(String name) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(name)) {
                return recipe;
            }
        }
        return null;
    }

    public Recipe searchByTags(String tag) { //returns the recipe if it contains atleast 1 of the given tags
        ArrayList<String> taglist = new ArrayList();
        if (tag.charAt(0) == '(') {
            String tags = tag.substring(1, tag.length() + 1);
            String[] split = tags.split(",");
            for (String string : split) {
                taglist.add(string);
            }
        }
        for (Recipe recipe : recipes) {
            ArrayList<String> recipetaglist = new ArrayList();
            if (recipe.getTags().charAt(0) == '(') {
                String tags = recipe.getTags().substring(1, tag.length() + 1);
                String[] split = tags.split(",");
                for (String string : split) {
                    recipetaglist.add(string);
                }
            }else{
                if(taglist.contains(recipe.getTags())){
                    return recipe;
                }
            }
            for (String string : recipetaglist) {
                if(taglist.contains(string)){
                    return recipe;
                }
            }
        }
        return null;
    }

    public static ArrayList<Recipe> getAllRecipes() throws ClassNotFoundException, SQLException, URISyntaxException { // run this once on program startup to get all recipes from database to local which will speed up the recipe searches
        Connection connection = getConnection();
        Statement stm = connection.createStatement();
        String sql = "SELECT * from recipes;";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Recipe> recipeList = new ArrayList<>();
        while (rst.next()) {
            Recipe recipe;
            recipe = new Recipe();
            recipe.setId(rst.getInt("id"));
            recipe.setName(rst.getString("name"));
            recipe.setIngredients(rst.getString("ingredients"));
            recipe.setInstructions(rst.getString("instructions"));
            recipeList.add(recipe);
        }
        return recipeList;
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI("postgres://hkullkvwogqmsk:57f03e071224c52d64f523e55c0105096f73c5e1fc5519ac6e4af2461419ebdd@ec2-54-217-217-142.eu-west-1.compute.amazonaws.com:5432/d1ur8rbqa8ddlp");
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        return DriverManager.getConnection(dbUrl, username, password);
    }
}

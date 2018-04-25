package main.java.recipe;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Kasperi
 */
public class RecipeList {

    ArrayList<Recipe> recipes;

    public RecipeList() throws ClassNotFoundException, SQLException, URISyntaxException {
        recipes = getAllRecipes();
    }

    public void searchById(int id) {
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                System.out.println(recipe);
                break;
            }
        }
    }

    public void searchByName(String name) {             //prints all the recipes which name contains the given string
        ArrayList<Recipe> list = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if(recipe.getName().toLowerCase().contains(name.toLowerCase())){
                list.add(recipe);
            }
        }
        for (Recipe recipe : list) {
            System.out.println(recipe);
        }
    }
    
    public ArrayList<Recipe> searchByIngredients(String ingredients){ //prints all recipes which ingredients constains given ingredients
        ArrayList<Recipe> list = new ArrayList<>();
        String[] ingre = ingredients.split(",");
        for (Recipe recipe : recipes) {
            for(int i = 0; i < ingre.length; i++){
                if(!recipe.getIngredients().toLowerCase().contains(ingre[i].toLowerCase())){
                    break;
                }
                if(i == ingre.length - 1 && recipe.getIngredients().toLowerCase().contains(ingre[i].toLowerCase())){
                    list.add(recipe);
                }
            }
        }
        return list;
    }

    public void searchByTags(String tag) {          //prints all the recipes that contain all of the given tags(tags must be separated by ",")
        ArrayList<Recipe> list = new ArrayList<>();
        String[] tags = tag.split(",");
        for (Recipe recipe : recipes) {
            for (int i = 0; i < tags.length; i++) {
                if (!recipe.getTags().toLowerCase().contains(tags[i].toLowerCase())) {
                    break;
                }
                if (i == tags.length - 1 && recipe.getTags().toLowerCase().contains(tags[i].toLowerCase())) {
                    list.add(recipe);
                }
            }
        }
        for (Recipe recipe : list) {
            System.out.println(recipe);
        }
    }

    public void addRecipe() throws ClassNotFoundException, SQLException, URISyntaxException {
        Connection connection = getConnection();
        Statement stm = connection.createStatement();
        Scanner s = new Scanner(System.in);
        System.out.println("Name:");
        String name = s.nextLine();
        System.out.println("Ingredients e.g. (2kg_bacon,3_egg):");
        String ingre = s.nextLine();
        System.out.println("Instructions:");
        String instru = s.nextLine();
        System.out.println("Tags e.g. (finnish,easy):");
        String tags = s.nextLine();
        /*Recipe recipe;
        recipe = new Recipe(name, tags, ingre, instru);
        recipes.add(recipe);*/

        stm.executeUpdate("INSERT INTO recipes (name,ingredients,instructions,tags) VALUES ('" + name + "','" + ingre + "','" + instru + "','" + tags + "')");
        recipes = getAllRecipes();
    }

    @Override
    public String toString() {
        for (Recipe recipe : recipes) {
            System.out.println(recipe);
        }
        return "";
    }
    
    public Recipe suggestRecipe() {                         //suggest
        
        return recipes.get(0);
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
            recipe.setTags(rst.getString("tags"));
            recipeList.add(recipe);
        }
        connection.close();
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

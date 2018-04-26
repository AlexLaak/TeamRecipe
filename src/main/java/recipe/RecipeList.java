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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

    public Recipe searchById(int id) {
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }
        return null;
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
    
    public void deleteRecipe() throws ClassNotFoundException, SQLException, URISyntaxException {
        Connection connection = getConnection();
        Statement stm = connection.createStatement();
        Scanner s = new Scanner(System.in);
        System.out.println("ID that you want to delete:");
        int id = s.nextInt();
        Recipe recipe = searchById(id);
        if (recipe != null){
            System.out.println("DELETE FROM recipes WHERE id = '" + recipe.getId() + "'");
            stm.executeUpdate("DELETE FROM recipes WHERE id = '" + recipe.getId() + "'");
            //stm.executeQuery("SELECT SETVAL((SELECT pg_get_serial_sequence('recipes', 'id')), 13, false);");
            int max = 0;
            ResultSet rs = stm.executeQuery("SELECT id FROM recipes ORDER BY id DESC LIMIT 1");
            while (rs.next()) {
                max = rs.getInt(1);
            }
            stm.executeQuery("SELECT SETVAL((SELECT pg_get_serial_sequence('recipes', 'id')), " + max + ", true);");
            recipes = getAllRecipes();
        } else {
            System.out.println("There is not a recipe with that ID!");
        }
    }

    @Override
    public String toString() {
        for (Recipe recipe : recipes) {
            System.out.println(recipe);
        }
        return "";
    }
    
    public void suggestRecipe() {                         //suggest
        
        System.out.println("What ingredients you have?");
        Scanner s = new Scanner(System.in);
        String answer = s.nextLine();
        String[] ing = answer.split(",");
        Queue<Recipe> que = searchHelper(answer);
        
        if (que.isEmpty()) {
            System.out.println("No suggestions based on ingredients!");
            return;
        }
        
        while (true) {
            Recipe recipe = que.poll();
            System.out.println(recipe);
            System.out.println("You are missing the following ingredients: " + missingIngredients(answer, recipe));
            //System.out.println(que.poll());
            System.out.println("Do you accept this suggestion? Y/N");
            if (s.nextLine().equalsIgnoreCase("Y")) break;
            if (que.isEmpty()) {
                System.out.println("No more suggestions");
                break;
            }
        }
    }
    
    public ArrayList<String> missingIngredients(String ingredients, Recipe recipe){
        ArrayList<String> missing = new ArrayList<>();
        String[] recipeing = recipe.getIngredients().split(",");
        String[] ingre = ingredients.split(",");
        boolean found = false;
        for (String ing : recipeing) {
            for (String ingr : ingre) {
                if(ing.contains(ingr)){
                    found = true;
                }
            }
            if(found){
                found = false;
            }else{
                missing.add(ing);
            }
        }
        
        return missing;
    }
    
    public Queue<Recipe> searchHelper(String ingridients) {
        Queue<Recipe> resultQue = new LinkedList<Recipe>();
        String[] ing = ingridients.split(",");
        int bestest = 0;
        
        for (Recipe recipe : recipes) {
            int secondBestest = 0;
            for (int i = 0; i < ing.length; i++) {
                if (recipe.getIngredients().contains(ing[i])) {
                    secondBestest++;
                }
            }
            if (secondBestest >= bestest) {           
                bestest = secondBestest;
                Collections.reverse((List<?>) resultQue);
                resultQue.add(recipe);
                Collections.reverse((List<?>) resultQue);
            }
            
        }
        return resultQue;
    }

    public static ArrayList<Recipe> getAllRecipes() throws ClassNotFoundException, SQLException, URISyntaxException { // run this once on program startup to get all recipes from database to local which will speed up the recipe searches
        Connection connection = getConnection();
        Statement stm = connection.createStatement();
        String rstt;
        String sql = "SELECT * from recipes;";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Recipe> recipeList = new ArrayList<>();
        while (rst.next()) {
            Recipe recipe;
            recipe = new Recipe();
            recipe.setId(rst.getInt("id"));
            recipe.setName(rst.getString("name"));
            rstt = rst.getString("ingredients");
            if (rstt.endsWith(")")) recipe.setIngredients(rstt.substring(1,rstt.length()-1));
                else recipe.setIngredients(rst.getString("ingredients"));
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

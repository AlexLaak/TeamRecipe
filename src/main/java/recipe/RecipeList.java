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
import java.util.TreeSet;

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
            if (recipe.getName().toLowerCase().contains(name.toLowerCase())) {
                list.add(recipe);
            }
        }
        for (Recipe recipe : list) {
            System.out.println(recipe);
        }
    }

    public ArrayList<Recipe> searchByIngredients(String ingredients) { //prints all recipes which ingredients constains given ingredients
        ArrayList<Recipe> list = new ArrayList<>();
        String[] ingre = ingredients.split(",");
        for (Recipe recipe : recipes) {
            for (int i = 0; i < ingre.length; i++) {
                if (!recipe.getIngredients().toLowerCase().contains(ingre[i].toLowerCase())) {
                    break;
                }
                if (i == ingre.length - 1 && recipe.getIngredients().toLowerCase().contains(ingre[i].toLowerCase())) {
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

    //Adding recipe column by column and inserting it to the database
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
        connection.close();
    }

    public void deleteRecipe() throws ClassNotFoundException, SQLException, URISyntaxException {
        Connection connection = getConnection();
        Statement stm = connection.createStatement();
        Scanner s = new Scanner(System.in);
        System.out.println("ID that you want to delete:");
        int id = s.nextInt();
        Recipe recipe = searchById(id);
        if (recipe != null) {
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
        connection.close();
    }

    @Override
    public String toString() {
        for (Recipe recipe : recipes) {
            System.out.println(recipe);
        }
        return "";
    }

    public void suggestRecipe(User user) {                         //suggest

        System.out.println("What ingredients you have?");
        Scanner s = new Scanner(System.in);
        String answer = s.nextLine();
        Queue<Recipe> que = searchHelper(answer, user);

        if (que.isEmpty()) {
            System.out.println("No suggestions based on ingredients!");
            return;
        }

        while (true) {
            Recipe recipe = que.poll();
            System.out.println(recipe);
            ArrayList<String> miss = missingIngredients(answer, recipe);
            if (miss.isEmpty()) {
                System.out.println("You have all the ingredients.");
            } else {
                System.out.println("You are missing the following ingredients: " + miss);
            }
            //System.out.println(que.poll());
            System.out.println("Do you accept this suggestion? Y/N");
            if (s.nextLine().equalsIgnoreCase("Y")) {
                System.out.println("");
                System.out.println("Recipe name: " + recipe.getName());
                System.out.println("Recipe ingridients: " + recipe.getIngredients());
                System.out.println("Recipe instructions: " + recipe.getInstructions());
                System.out.println("Recipe tags: " + recipe.getTags());
                break;
            }
            if (que.isEmpty()) {
                System.out.println("No more suggestions");
                break;
            }
        }
    }

    public ArrayList<String> missingIngredients(String ingredients, Recipe recipe) {
        ArrayList<String> missing = new ArrayList<>();
        String[] recipeing = recipe.getIngredients().split(",");
        String[] ingre = ingredients.split(",");
        boolean found = false;
        for (String ing : recipeing) {
            for (String ingr : ingre) {
                if (ing.contains(ingr)) {
                    found = true;
                }
            }
            if (found) {
                found = false;
            } else {
                missing.add(ing);
            }
        }

        return missing;
    }

    public Queue<Recipe> searchHelper(String ingridients, User user) {
        Queue<Recipe> resultQue = new LinkedList<Recipe>();
        String[] ing = ingridients.split(",");
        String[] alrg = user.getAllergies().split(",");
        ArrayList<Recipe> recipes1 = new ArrayList<>();
        //int bestest = 0;

        boolean badRecipe = false;

        for (Recipe recipe : recipes) {
            badRecipe = false;
            for (String string : alrg) {
                if (recipe.getIngredients().contains(string)) {
                    badRecipe = true;
                    break;
                }
            }
            if (!badRecipe) {
                int score = 0;
                for (int i = 0; i < ing.length; i++) {
                    if (recipe.getIngredients().contains(ing[i])) {
                        score++;
                    }
                }
                recipe.setScoreGiven(score);
                recipes1.add(recipe);
                
                /*if (secondBestest >= bestest) {
                    bestest = secondBestest;
                    Collections.reverse((List<?>) resultQue);
                    resultQue.add(recipe);
                    Collections.reverse((List<?>) resultQue);
                }*/
            }
        }
        Collections.sort(recipes1);
        for (Recipe recipe : recipes1) {
            resultQue.add(recipe);
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
            if (rstt.endsWith(")")) {
                recipe.setIngredients(rstt.substring(1, rstt.length() - 1));
            } else {
                recipe.setIngredients(rst.getString("ingredients"));
            }
            recipe.setInstructions(rst.getString("instructions"));
            recipe.setTags(rst.getString("tags"));
            recipeList.add(recipe);
        }
        connection.close();
        return recipeList;
    }

    public void dropTable() throws URISyntaxException, SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        Statement stm = connection.createStatement();
        Scanner s = new Scanner(System.in);
        System.out.println("Select which table to drop and create (1=Users , 2=Recipes , 0=Cancel");
        int num = s.nextInt();
        if (num == 1) {
            stm.executeUpdate("DROP TABLE IF EXISTS users");
            stm.executeUpdate("CREATE TABLE users (ID SERIAL,username varchar(255), password varchar(255), allergies varchar(1000))");
            System.out.println("Table users dropped and created again");
        }
        if (num == 2) {
            stm.executeUpdate("DROP TABLE IF EXISTS recipes");
            stm.executeUpdate("CREATE TABLE recipes (ID SERIAL, name varchar(255), ingredients varchar(1000), instructions varchar(10000), tags varchar(1000))");
            System.out.println("Table recipes dropped and created again");
        } else {
            System.out.println("Dropping cancelled");
        }
        recipes = getAllRecipes();
        connection.close();
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI("postgres://hkullkvwogqmsk:57f03e071224c52d64f523e55c0105096f73c5e1fc5519ac6e4af2461419ebdd@ec2-54-217-217-142.eu-west-1.compute.amazonaws.com:5432/d1ur8rbqa8ddlp");
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        return DriverManager.getConnection(dbUrl, username, password);
    }
}

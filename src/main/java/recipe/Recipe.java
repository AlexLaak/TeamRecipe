package main.java.recipe;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author TeamRecipe
 */
public class Recipe implements Comparable<Recipe>{
    private int id;
    private String name;
    private String tags;
    private String ingredients;
    private String instructions;
    private int scoreGiven;

    public Recipe() {
    }

    public Recipe(String name, String tags, String ingredients, String instructions) {
        this.name = name;
        this.tags = tags;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.scoreGiven = 0;
    }

    public int getScoreGiven() {
        return scoreGiven;
    }

    public void setScoreGiven(int scoreGiven) {
        this.scoreGiven = scoreGiven;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTags(){
        return this.tags;
    }
    public void setTags(String tags){
        this.tags = tags;
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
    
    @Override
    public String toString(){
        return this.id + " " + this.name + " " + this.tags + " " + this.ingredients + " " + this.instructions;
    }

    @Override
    public int compareTo(Recipe o) {
        return o.scoreGiven - this.scoreGiven;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.recipe.UI;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import main.java.recipe.User;

/**
 * FXML Controller class
 *
 * @author Kenji Fam
 */
public class UserpageController {
    
    @FXML
    private Text username;
    
    @FXML
    private Text allergies;
    
    private User user;

    /**
     * Initializes the controller class.
     */
    public void initialize() {
        user = LoginController.getUser();
        showUserInfo(user);
    }
    
    private void showUserInfo(User user) {
        username.setText(user.getUsername());
        allergies.setText(user.getAllergies());
    }
    
}

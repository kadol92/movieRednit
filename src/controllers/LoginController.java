package controllers;

import app.utilities.DataBaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Created by Kamil on 2017-01-17.
 */
public class LoginController {
    @FXML private Controller controller;
    private DataBaseConnection dataBaseConnection;

    @FXML
    private VBox loginPanel;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordTextField;
@FXML private Label loginInfoLabel;

    public void init(Controller controller, DataBaseConnection dataBaseConnection){
        this.dataBaseConnection = dataBaseConnection;
        this.controller = controller;
        loginInfoLabel.setVisible(false);
    }
    public VBox getLoginPanel() {
        return loginPanel;
    }


    public void loginToServer(){
        loginInfoLabel.setVisible(false);
        if(dataBaseConnection.loginToDB(userNameTextField.getText(), passwordTextField.getText())){
            controller.getMenuItemProfile().setVisible(true);
            controller.showProfile();
        }
        else{
            loginInfoLabel.setVisible(true);
            System.out.println("Niepoprawne login lub hasło!");
        }
    }

    public void registerNewUser(){
        controller.registerIntoDB();
    }

}

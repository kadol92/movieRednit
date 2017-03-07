package controllers;

import app.data.DataBaseConfig;
import app.utilities.DataBaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Created by Kamil on 2017-01-24.
 */
public class RegisterController {
    public TextField nameField;
    public TextField surnameField;
    public TextField loginField;
    public TextField emailField;
    public TextField instagramField;
    public PasswordField passwordField;
    public Controller controller;
    public Label registerInfo;
    public Button registerButton;
    public VBox registerPanel;

    public void init(Controller controller){
        this.controller = controller;
        registerInfo.setVisible(false);
    }

    public void loginUser(){
        controller.loginToDB();
    }

    public void registerNewUser(){
        if(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || loginField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            registerInfo.setVisible(true);
            registerInfo.setText("Podaj wymagane dane!");
        }
        else{
            DataBaseConnection dataBaseConnection = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD);
            boolean result = dataBaseConnection.registerNewUser(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), instagramField.getText(), emailField.getText());
            if(result){
                registerInfo.setVisible(true);
                registerInfo.setText("Zarejestrowano pomyślnie!");
                registerButton.setDisable(true);
            }
            else{
                registerInfo.setVisible(true);
                registerInfo.setText("Błąd przy dodawaniu!");
            }

        }
    }
}

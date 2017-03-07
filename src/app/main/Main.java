package app.main;

import app.data.DataBaseConfig;
import app.utilities.DataBaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/sample.fxml"));
        primaryStage.setTitle("MovieRednit");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
       /*DataBaseConnection dataBaseConnection = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD);
        boolean result = dataBaseConnection.registerNewUser("kamill3092", "z", "Kam", "Dol", "https://www.instagram.com/kadol92/", "kamil.dolny92@gmail.com");
        if(result){
            System.out.println("Dodano usera");
        }
        else{
            System.out.println("Bład przy dodawaniu");
        }*/
        launch(args);
    }
}

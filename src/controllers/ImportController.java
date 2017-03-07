package controllers;

import app.utilities.FilmwebConnection;
import info.talacha.filmweb.connection.FilmwebException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Kamil on 2017-01-12.
 */
public class ImportController {
    @FXML
    private Button importButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TextField loginFiled;
    @FXML
    private PasswordField passwordField;
    @FXML
    public Label infoLabel;
    private FilmwebConnection filmwebConnection;

    public void importMoviesFromFilmweb() {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    filmwebConnection.getUserFilmVotes();
                } catch (FilmwebException e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            setinfoLabelText("Błędne login lub hasło!");
                            importButton.setDisable(false);

                        }
                    });
                }

                return null;
            }
        };

        Task<Void> taskBar = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                while (!filmwebConnection.getNumberSet()) ;
                double m = filmwebConnection.getNumberTitles();
                while (filmwebConnection.getNumberSet()) {
                    double l = filmwebConnection.getCurrentNumberTitle();

                    progressBar.setProgress(l / m);
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setinfoLabelText("Zakończono pobieranie");
                        importButton.setDisable(false);


                        System.out.println("KOniec");
                    }
                });

                return null;
            }
        };

        setinfoLabelVisible(true);
        setinfoLabelText("Pobieram...");
        importButton.setDisable(true);
        filmwebConnection = null;
        filmwebConnection = new FilmwebConnection(loginFiled.getText(), passwordField.getText());
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(taskBar);
        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.setName("Pobieranie Filmweb");
        thread1.start();
        thread2.start();


    }

    public synchronized void setinfoLabelText(String text) {
        infoLabel.setText(text);
    }

    public synchronized void setinfoLabelVisible(boolean visible) {
        infoLabel.setVisible(visible);
    }

    private Stage importWindow;
    private FXMLLoader loaderImport;
    private Parent importParent;

    public void init() {

        importWindow = new Stage();
        //login = FXMLLoader.load(getClass().getResource("../controllers/loginWindow.fxml"));
        loaderImport = new FXMLLoader(getClass().getResource("/view/importFilmweb.fxml"));

        try {
            importParent = loaderImport.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        importWindow.initModality(Modality.APPLICATION_MODAL);
        importWindow.setTitle("MovieRednit - Import ocen z Filmweb.pl");
        importWindow.setScene(new Scene(importParent, 600, 200));
        importWindow.setResizable(false);
        importWindow.show();
        System.out.println("Inicjalizacja importu");
        //infoLabel.setVisible(false);
    }
}

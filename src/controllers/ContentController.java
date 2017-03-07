package controllers;

import app.data.DataBaseConfig;
import app.utilities.DataBaseConnection;
import app.utilities.Multikino;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2017-01-17.
 */
public class ContentController {

    @FXML
    private ImageView movieImage;
    @FXML
    private Label movieTitleLabel;
    @FXML
    private Label movieDateLabel;
    @FXML
    private Label movieGenreLabel;
    @FXML
    private Button interestedButton;
    @FXML
    private Button setAsInterestedButton;
    @FXML
    private Label interestedNumberLabel;
    @FXML
    private Controller controller;
    @FXML
    private VBox content;
    @FXML
    private Label genrePercetageLabel;
    private Multikino multikino;
    private Stage interestedWindow;
    private FXMLLoader loader;
    private Parent interestedParent;
    private InterestedController interestedController;
    private double percentageValue;

    public void init(Multikino multikino, Controller controller) {
        this.multikino = multikino;
        this.controller = controller;
        this.interestedController = new InterestedController();
        movieImage.setImage(new Image(multikino.getMovieItemImage(0)));
        movieTitleLabel.setText(multikino.getMovieItemTitle(0));
        movieDateLabel.setText("Premiera: " + multikino.getMovieItemDate(0));
        movieGenreLabel.setText(multikino.getMovieItemGenre(0));

    }

    public VBox getContent() {
        return content;
    }

    public void displayMovieInfo() {
        Task<Void> getDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    DataBaseConnection db = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD);

                    int number = db.getInterestedNumber(controller.getCinema(), controller.getMovieList().getSelectionModel().getSelectedItem().toString());
                    interestedNumberLabel.setText("Liczba osób, która chce zobaczyć ten film: " + Integer.toString(number));
                    if (number == 0) interestedButton.setDisable(true);
                    else interestedButton.setDisable(false);
                    String[] genres = multikino.getMovieItemGenre(controller.getMovieList().getSelectionModel().getSelectedIndex()).split("/");
                    List<Double> genrePercentage = new ArrayList<Double>();

                    for (String e : genres) {
                        genrePercentage.add(db.getUserPercentageGenre(e, Controller.getUser().getUserID()));
                    }
                    Double sum = new Double(0.0);
                    for (Double d : genrePercentage) {
                        sum += d;
                    }

                    percentageValue = sum = (sum / genrePercentage.size()) * 10.0;
                    Controller.setPercentageValue(percentageValue);
                    genrePercetageLabel.setText(String.format("%.2f", sum) + "% w Twoim guście");
                });
                return null;
            }
        };
        Thread getDataThread = new Thread(getDataTask);
        getDataThread.setDaemon(true);
        getDataThread.start();
    }

    public void changeView() throws Exception {

        movieImage.setImage(new Image("loading.gif"));
        interestedNumberLabel.setText("Liczba osób, która chce zobaczyć ten film: 0");
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                movieImage.setImage(new Image(multikino.getMovieItemImage(controller.getMovieList().getSelectionModel().getSelectedIndex())));

                return null;
            }

        };

        displayMovieInfo();
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
        movieTitleLabel.setText(controller.getMovieList().getSelectionModel().getSelectedItem().toString());
        movieDateLabel.setText("Premiera: " + multikino.getMovieItemDate(controller.getMovieList().getSelectionModel().getSelectedIndex()));
        movieGenreLabel.setText(multikino.getMovieItemGenre(controller.getMovieList().getSelectionModel().getSelectedIndex()));
    }

    public static String cinema;
    public static String movieTitle;

    public void showInterested() {
        Controller.setMovieTitle(controller.getMovieList().getSelectionModel().getSelectedItem().toString());
        interestedWindow = new Stage();
        //login = FXMLLoader.load(getClass().getResource("../controllers/loginWindow.fxml"));
        loader = new FXMLLoader(getClass().getResource("/view/interested.fxml"));

        try {
            interestedParent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        interestedWindow.initModality(Modality.APPLICATION_MODAL);
        interestedWindow.setTitle(controller.getMovieList().getSelectionModel().getSelectedItem().toString() + " - zainteresowani seansem [ " + Controller.getCinema() + " ]");
        interestedWindow.setScene(new Scene(interestedParent, 800, 300));
        interestedWindow.setResizable(false);
        interestedWindow.show();
    }


    public void setAsInterested() {
        DataBaseConnection db = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD);
        db.setAsInterested(Controller.getUser().getUserID(), controller.getMovieList().getSelectionModel().getSelectedItem().toString(), controller.getCinema(), percentageValue);
        displayMovieInfo();
    }
}

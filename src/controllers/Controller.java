package controllers;

import app.data.DataBaseConfig;
import app.data.User;
import app.utilities.DataBaseConnection;
import app.utilities.Multikino;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ListView getMovieList() {
        return movieList;
    }

    public void setMovieList(ListView movieList) {
        this.movieList = movieList;
    }

    @FXML
    private ListView movieList;

    public MenuItem getMenuItemProfile() {
        return menuItemProfile;
    }

    @FXML
    private MenuItem menuItemProfile;


    private Stage importWindow;
    private Multikino multikino;
    private FXMLLoader loaderImport;
    private static User user;

    public synchronized static User getUser() {
        return user;
    }

    public static void setUser(User userlogin) {
        user = userlogin;
    }

    private Parent importParent;
    private static List<String> cinemaList;
    @FXML
    private ContentController contentController;
    @FXML
    private LoginController loginController;
    @FXML
    private ProfileController profileController;
    @FXML
    private RegisterController registerController;
    private DataBaseConnection dataBaseConnection;
    private ImportController importController;



    private static String cinema;
    public static void setCinema(String cinema) {
        Controller.cinema = cinema;
    }
    public static double getPercentageValue() {
        return percentageValue;
    }

    public static void setPercentageValue(double percentageValue) {
        Controller.percentageValue = percentageValue;
    }

    private static double percentageValue;

    public static String getMovieTitle() {
        return movieTitle;
    }

    public static void setMovieTitle(String movieTitle) {
        Controller.movieTitle = movieTitle;
    }

    private static String movieTitle;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Inicjalizacja");
        dataBaseConnection = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD, this);
        multikino = new Multikino("gdansk");
        cinemaList = new ArrayList<>();
        multikino.getOutgoingMovie();
        cinemaList = multikino.getCinemaList();
        contentController.init(multikino, this);
        loginController.init(this, dataBaseConnection);
        registerController.init(this);
        importController = new ImportController();
        movieList.getItems().addAll(multikino.getMovieTitle());
        movieList.getSelectionModel().select(0);
        movieList.setDisable(true);
    }

    public static List<String> getCinemaList() {
        return cinemaList;
    }

    public static String getCinema() {
        return cinema;
    }

    public void changeView() {

        try {
            contentController.changeView();
            contentController.getContent().setVisible(true);
            loginController.getLoginPanel().setVisible(false);
            profileController.getProfilePanel().setVisible(false);
            registerController.registerPanel.setVisible(false);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProfile() {
        movieList.setDisable(false);
        contentController.getContent().setVisible(false);
        loginController.getLoginPanel().setVisible(false);
        profileController.getProfilePanel().setVisible(true);
        registerController.registerPanel.setVisible(false);
        int index = profileController.cinemaListChoiceBox.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            index = 0;
        }
        profileController.init(user, this);
       profileController.cinemaListChoiceBox.getSelectionModel().select(index);
        cinema = profileController.cinemaListChoiceBox.getSelectionModel().getSelectedItem();

    }

    public void loginToDB() {
        contentController.getContent().setVisible(false);
        loginController.getLoginPanel().setVisible(true);
        profileController.getProfilePanel().setVisible(false);
        registerController.registerPanel.setVisible(false);
    }

    public void registerIntoDB() {
        contentController.getContent().setVisible(false);
        loginController.getLoginPanel().setVisible(false);
        profileController.getProfilePanel().setVisible(false);
        registerController.registerPanel.setVisible(true);
    }

    public void importMoviesFilmweb() {

        importController.init();

    }


}

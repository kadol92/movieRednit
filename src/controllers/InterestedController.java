package controllers;

import app.data.DataBaseConfig;
import app.data.User;
import app.utilities.DataBaseConnection;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Kamil on 2017-01-12.
 */
public class InterestedController implements Initializable {
    @FXML
    private ListView interestedList;
    @FXML
    private Label userFullName;
    @FXML
    private Label userEmail;
    @FXML
    private Hyperlink userInstagram;
    @FXML
    private ImageView userImage;
    @FXML
    private Label interestedTasteValue;
    @FXML
    private Label interestedGenre;
    private Stage interestedWindow;
    private FXMLLoader loader;
    private Parent interestedParent;
    private Controller controller;
    private List<User> interestedUsers;

    public void init(String title, String cinema) {

    }

    private void setUserImage() {
        Task<Void> setImage = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                userImage.setImage(new Image(interestedUsers.get(interestedList.getSelectionModel().getSelectedIndex()).getInstagramImage()));
                return null;
            }
        };

        Thread t = new Thread(setImage);
        t.setDaemon(true);
        t.start();

    }

    public void changeView() {
        userFullName.setText(interestedUsers.get(interestedList.getSelectionModel().getSelectedIndex()).toString());
        userEmail.setText(interestedUsers.get(interestedList.getSelectionModel().getSelectedIndex()).getEmail());
        userInstagram.setText(interestedUsers.get(interestedList.getSelectionModel().getSelectedIndex()).getInstagram());
        double percentageValue = interestedUsers.get(interestedList.getSelectionModel().getSelectedIndex()).getPercentageValue();
        double similarity = 100.0 - Math.abs(Controller.getPercentageValue() - percentageValue);
        interestedTasteValue.setText("Gustopodobna w " + String.format("%.2f", similarity) + "%");
        setUserImage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        interestedUsers = new ArrayList<>();
        DataBaseConnection db = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD);
        interestedUsers.addAll(db.getInterestedPeople(Controller.getMovieTitle(), Controller.getCinema()));
        interestedList.getItems().addAll(interestedUsers);
        interestedList.getSelectionModel().select(0);
        userFullName.setText(interestedUsers.get(interestedList.getSelectionModel().getSelectedIndex()).toString());
        userEmail.setText(interestedUsers.get(interestedList.getSelectionModel().getSelectedIndex()).getEmail());
        userInstagram.setText(interestedUsers.get(interestedList.getSelectionModel().getSelectedIndex()).getInstagram());
        double percentageValue = interestedUsers.get(interestedList.getSelectionModel().getSelectedIndex()).getPercentageValue();
        double similarity = 100.0 - Math.abs(Controller.getPercentageValue() - percentageValue);
        interestedTasteValue.setText("Gustopodobna w " + String.format("%.2f", similarity) + "%");
        setUserImage();
    }
}

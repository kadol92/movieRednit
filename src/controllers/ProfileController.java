package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import app.data.User;

/**
 * Created by Kamil on 2017-01-18.
 */
public class ProfileController {
    @FXML
    private ImageView profileImage;
    @FXML
    private Label userFullName;
    @FXML private Label userEmail;
    @FXML private Hyperlink instagramURL;
    @FXML private Label interestedTasteValue;
    @FXML private Label interestedGenre;
    @FXML private VBox profilePanel;
    private boolean init = false;
    private Controller controller;
    @FXML public ChoiceBox<String> cinemaListChoiceBox;

    public void init(User user, Controller controller){
        this.controller = controller;

        cinemaListChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                try{
                    Controller.setCinema(cinemaListChoiceBox.getItems().get(newValue.intValue()));
                }catch (IndexOutOfBoundsException e){

                }


            }
        });

        Task<Void> loadImage = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                profileImage.setImage(new Image("profileImage.gif"));
                profileImage.setImage(new Image(user.getInstagramImage()));
                return null;
            }
        };
        cinemaListChoiceBox.getItems().clear();
        cinemaListChoiceBox.getItems().addAll(Controller.getCinemaList());
        Thread t = new Thread(loadImage);
        t.start();

        userFullName.setText("Witaj, " + user.getFirstName() + " " + user.getLastName());
        userEmail.setText(user.getEmail());
        instagramURL.setText(user.getInstagram());

    }

    public void importMoviesFilmweb(){
        controller.importMoviesFilmweb();
    }
    public VBox getProfilePanel() {
        return profilePanel;
    }

    public void setProfileImage(ImageView profileImage) {
        this.profileImage = profileImage;
    }

    public void setUserFullName(Label userFullName) {
        this.userFullName = userFullName;
    }
    public void setUserEmail(Label userEmail) {
        this.userEmail = userEmail;
    }

    public void setInstagramURL(Hyperlink instagramURL) {
        this.instagramURL = instagramURL;
    }

    public void setInterestedTasteValue(Label interestedTasteValue) {
        this.interestedTasteValue = interestedTasteValue;
    }

    public void setInterestedGenre(Label interestedGenre) {
        this.interestedGenre = interestedGenre;
    }

    public void setProfilePanel(VBox profilePanel) {
        this.profilePanel = profilePanel;
    }
}

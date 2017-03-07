package app.data;

/**
 * Created by Kamil on 2017-01-17.
 */
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String instagram;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getInstagramImage() {
        return instagramImage;
    }

    public int getUserID() {
        return userID;
    }

    private String instagramImage;
    private int userID;

    public double getPercentageValue() {
        return percentageValue;
    }

    private double percentageValue;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public User(String firstName, String lastName, String email, String instagram, String instagramImage, int userID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.instagram = instagram;
        this.instagramImage = instagramImage;
        this.userID = userID;
    }

    public User(String firstName, String lastName, String email, String instagram, String instagramImage, int userID, double percentageValue) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.instagram = instagram;
        this.instagramImage = instagramImage;
        this.userID = userID;
        this.percentageValue = percentageValue;
    }

}

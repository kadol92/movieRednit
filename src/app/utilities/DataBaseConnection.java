package app.utilities;

import app.data.User;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import controllers.Controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2017-01-17.
 */
public class DataBaseConnection {
    private String url;
    private String userName;
    private String password;
    private Controller controller;

    public DataBaseConnection(String url, String userName, String password, Controller controller) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.controller = controller;

    }

    public DataBaseConnection(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public boolean registerNewUser(String userName, String password, String firstName, String lastName, String instagram, String email) {

        String instagramImage = new String();
        try {
            if (!instagram.isEmpty()) {
                Document document = Jsoup.connect(instagram).get();
                Elements image = document.select("meta").attr("property", "og:image");
                instagramImage = image.get(15).attr("content");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(this.url, this.userName, this.password);

            PreparedStatement statement = connection.prepareStatement("INSERT INTO users values(id,?,?,?,?,?,?,?)");
            statement.setString(1, userName);
            statement.setString(2, GenerateHash.getHash(password));
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, instagramImage);
            statement.setString(6, instagram);
            statement.setString(7, email);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean loginToDB(String userName, String userPassword) {
        boolean result = false;
        try {
            Connection connection = DriverManager.getConnection(this.url, this.userName, this.password);

            PreparedStatement statement = connection.prepareStatement("select * from users where userName = ?;");
            statement.setString(1, userName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                String name = resultSet.getString("username");
                String password = resultSet.getString("password");

                if (name.equals(userName) && password.equals(GenerateHash.getHash(userPassword))) {
                    int ID = resultSet.getInt("id");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String instagramImage = resultSet.getString("instagram");
                    String instagram = resultSet.getString("instagramlink");
                    String email = resultSet.getString("email");
                    this.controller.setUser(new User(firstName, lastName, email, instagram, instagramImage, ID));
                    result = true;
                }


            }


            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void deleteUserList(int userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection(this.url, this.userName, this.password);
            statement = connection.prepareStatement("DELETE FROM users_filmweb_votes WHERE userId = ?");
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean addImportedList(int userId, String movieTitle, int movieYear, int rate) {

        Connection connection = null;
        PreparedStatement statement = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(this.url, this.userName, this.password);
            statement = connection.prepareStatement("INSERT INTO users_filmweb_votes VALUES(id_filmweb, ?, (SELECT id FROM movieslist WHERE name=? and year=?), ?)");
            statement.setInt(1, userId);
            statement.setString(2, movieTitle);
            statement.setInt(3, movieYear);
            statement.setInt(4, rate);
            statement.executeUpdate();
            result = true;

        } catch (MySQLIntegrityConstraintViolationException e) {

        } catch (SQLException e) {

        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        return result;
    }

    public void setAsInterested(int userId, String movieTitle, String cinema, double perventageValue) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(this.url, this.userName, this.password);
            statement = connection.prepareStatement("DELETE FROM interested_in_film where userId=? AND movieTitle = ? AND cinema = ?");
            statement.setInt(1, userId);
            statement.setString(2, movieTitle);
            statement.setString(3, cinema);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        statement = null;
        try {

            statement = connection.prepareStatement("INSERT INTO interested_in_film values(id, ?, ?, ?, ?)");

            statement.setInt(1, userId);
            statement.setString(2, movieTitle);
            statement.setString(3, cinema);
            statement.setDouble(4, perventageValue);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getInterestedNumber(String cinema, String movieTitle) {
        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;
        try {
            connection = DriverManager.getConnection(this.url, this.userName, this.password);
            statement = connection.prepareStatement("SELECT COUNT(*) as number FROM interested_in_film WHERE cinema = ? AND movieTitle = ?");

            statement.setString(1, cinema);
            statement.setString(2, movieTitle);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getInt("number");

        } catch (MySQLIntegrityConstraintViolationException e) {

        } catch (SQLException e) {

        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        return result;
    }

    public List<User> getInterestedPeople(String movieTitle, String cinema) {
        List<User> interestedList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement1 = null;

        try {
            connection = DriverManager.getConnection(this.url, this.userName, this.password);
            statement = connection.prepareStatement("SELECT u.id, u.firstName, u.lastName, u.instagram, u.instagramlink, u.email, inte.percentage FROM users u JOIN interested_in_film inte ON u.id = inte.userId WHERE u.id IN (SELECT userId FROM interested_in_film WHERE movieTitle = ? AND cinema = ?) AND inte.movieTitle=? AND inte.cinema = ?");
            statement.setString(1, movieTitle);
            statement.setString(2, cinema);
            statement.setString(3, movieTitle);
            statement.setString(4, cinema);
            System.out.println(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int ID = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String instagramImage = resultSet.getString("instagram");
                String instagram = resultSet.getString("instagramlink");
                String email = resultSet.getString("email");
                double percentage = resultSet.getDouble("percentage");
                interestedList.add(new User(firstName, lastName, email, instagram, instagramImage, ID, percentage));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interestedList;

    }

    public double getUserPercentageGenre(String genre, int userId){
        Connection connection = null;
        PreparedStatement statement = null;
        double result = 0;
        try{
            connection = DriverManager.getConnection(this.url, this.userName, this.password);
            statement = connection.prepareStatement("SELECT AVG(rate) as average FROM `users_filmweb_votes` WHERE movieId IN (SELECT id FROM movieslist WHERE geners LIKE ?) AND userId = ?");
            statement.setString(1, "%" + genre + "%");
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            result = resultSet.getDouble("average");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  result;
    }


}

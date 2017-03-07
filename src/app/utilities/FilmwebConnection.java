package app.utilities;

import app.data.DataBaseConfig;
import app.utilities.GenerateHash;
import controllers.Controller;
import info.talacha.filmweb.api.FilmwebApi;
import info.talacha.filmweb.connection.FilmwebException;
import info.talacha.filmweb.models.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



/**
 * Created by Kamil on 2016-12-14.
 */
public class FilmwebConnection {
    private String userName;
    private String password;
    private FilmwebApi filmwebApi;
    private List<String> userMovieTitle;
    private List<Integer> userMovieVote;
    private List<Integer> userMovieDate;
    private boolean numberSet;
    private int numberTitle;
    private int currentNumberTitle;



    public FilmwebConnection(String userName, String password ){
        this.userName = userName;
        this.password = password;
        this.filmwebApi = new FilmwebApi();
        this.numberSet = false;
        this.numberTitle = 0;
        this.currentNumberTitle = 0;
        this.userMovieVote = new ArrayList<>();
        this.userMovieTitle = new ArrayList<>();
        this.userMovieDate = new ArrayList<>();
    }

    public synchronized boolean getNumberSet(){
        return this.numberSet;
    }
    public synchronized void setNumberSet(boolean numberSet){
        this.numberSet = numberSet;
    }
    public synchronized int getNumberTitles(){
        return this.numberTitle;
    }
    public synchronized void setNumberTitle(int numberTitle){
        this.numberTitle = numberTitle;
    }

    public synchronized int getCurrentNumberTitle(){
        return this.currentNumberTitle;
    }
    public synchronized void setCurrentNumberTitle(int currentNumberTitle){
        this.currentNumberTitle = currentNumberTitle;
    }

    public void getUserFilmVotes() throws FilmwebException{
        Document document = null;
        try {
            //Create user to connect with Filmweb and get ID
            User user = this.filmwebApi.login(this.userName, this.password);
            String method = "getUserFilmVotes [" + user.getId()+ ",100,200]\\n";
            //String method = "getFilmInfoFull [473111]\\n";
            String api_id = "android";
            String api_key = "qjcGhW2JnvGT9dfCt3uT_jozR3s";
            String signature =  method+api_id+api_key;

            document = Jsoup.connect("https://ssl.filmweb.pl/api?version=1.0&appId=android&methods=" + method + "&signature=1.0," + GenerateHash.getHash(signature)).get();

            String [] userMovies = document.toString().split(Pattern.quote("["));
            int index = 0;
            List<Integer> userMovieList = new ArrayList<>();
            List<Integer> userMovieVote = new ArrayList<>();

            for (String t : userMovies) {
                String[] movies = t.split(",");
                if (index > 1) {
                    userMovieList.add(Integer.parseInt(movies[0]));
                    userMovieVote.add(Integer.parseInt(movies[2]));
                }

                index++;
            }

            setCurrentNumberTitle(0);
            setNumberTitle(userMovieList.size());
            Thread.sleep(100);
            setNumberSet(true);

            DataBaseConnection db = new DataBaseConnection(DataBaseConfig.URL, DataBaseConfig.USER_NAME, DataBaseConfig.PASSWORD);
            db.deleteUserList(Controller.getUser().getUserID());
            for(int i =0; i < userMovieList.size(); i++){
                //System.out.println(getFilmDetails(userMovieList.get(i)).toString());
                String [] doc = getFilmDetails(userMovieList.get(i)).toString().replace("\\\"", "").replace("null", "\"\"").split(Pattern.quote("["));
                String [] movieSplitted = doc[1].split("\"");
                String movieTitle = movieSplitted[1];
                String [] movieYearSplited = movieSplitted[6].split(",");
                String movieYear = movieYearSplited[1];
                //System.out.println("Rok = " + movieYear);
                this.userMovieDate.add(Integer.parseInt(movieYear));
                this.userMovieTitle.add(movieTitle);
                this.userMovieVote.add(userMovieVote.get(i));
                setCurrentNumberTitle(i+1);
                if(db.addImportedList(Controller.getUser().getUserID(), movieTitle, Integer.parseInt(movieYear), userMovieVote.get(i))){
                    System.out.println("DODANO | Id = " + i + " |  Tytuł: " + movieTitle + ", Rok: " + movieYear + ", Ocena: " + userMovieVote.get(i));
                }
                else{
                    System.out.println("ERROR  | Id = " + i + " |  Tytuł: " + movieTitle + ", Rok: " + movieYear + ", Ocena: " + userMovieVote.get(i));

                }




            }
            setNumberSet(false);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private Document getFilmDetails(long id){
        Document document = null;
        try {

            String method = "getFilmInfoFull [" + Long.toString(id)+ "]\\n";
            String api_id = "android";
            String api_key = "qjcGhW2JnvGT9dfCt3uT_jozR3s";
            String signature =  method+api_id+api_key;

            document = Jsoup.connect("https://ssl.filmweb.pl/api?version=1.0&appId=android&methods=" + method + "&signature=1.0," + GenerateHash.getHash(signature)).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return document;

    }

    /*private String generateMD5Hash(String signature){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(signature.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return  sb.toString();
    }*/

}

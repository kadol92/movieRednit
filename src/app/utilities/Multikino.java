package app.utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kamil on 2017-01-11.
 */
public class Multikino {
    public List<String> getMovieTitle() {
        return movieTitle;
    }

    public List<String> getMovieImage() {
        return movieImage;
    }

    public List<String> getMovieDate() {
        return movieDate;
    }

    public List<String> getMovieGenre() {
        return movieGenre;
    }

    public String getMovieItemTitle(int index) {
        return movieTitle.get(index);

    }

    public String getMovieItemImage(int index) {
        return movieImage.get(index);
    }

    public String getMovieItemDate(int index) {
        return movieDate.get(index);
    }

    public String getMovieItemGenre(int index) {
        return movieGenre.get(index);
    }

    public static final String url = "https://multikino.pl/pl/repertuar";
    private String city;
    private List<String> movieTitle;
    private List<String> movieImage;
    private List<String> movieDate;
    private List<String> movieGenre;

    public Multikino(String city) {
        this.city = city;
        this.movieImage = new ArrayList<>();
        this.movieDate = new ArrayList<>();
        this.movieGenre = new ArrayList<>();
        this.movieTitle = new ArrayList<>();
    }

    public void getOutgoingMovie() {


        try {
            Document document = Jsoup.connect(url).get();
            Elements movies = document.select("span.genre_names");
            Elements moviesTitle = document.select("p.desc");
            Elements moviesDate = document.select("span.date");
            Elements movieImage = document.select("div.grid-element-img"); //attr("style").replace("background-image:url('//", "http://").replace("');", "");
            Elements movieGenre = document.select("span.genre_names");
            //System.out.println(movies.first().text());
            int j = 0;
            int k = 0;
            int size = 0;
            for (int i = 0; i < moviesDate.size(); i++) {
                if (moviesTitle.get(i).text().equals("Zwiastuny filmowe") || moviesTitle.get(i).text().equals("ArchiwumFilmów")) {
                    j++;
                    size++;
                }
                this.movieTitle.add(moviesTitle.get(j).text());
                this.movieDate.add(moviesDate.get(i).text().replace("Premiera: ", ""));
                if(moviesTitle.get(j).text().equals("Wojna o planetę małp")){
                    this.movieGenre.add("Dramat / Akcja / Sci-Fi");
                }
                else{
                    this.movieGenre.add(movieGenre.get(k).text().replace("Science-Fiction", "Sci-Fi").replace("Animowany", "Animacja"));
                    k++;
                }

                this.movieImage.add(movieImage.get(j).attr("style").replace("background-image:url('//", "http://").replace("');", ""));
                //System.out.println("Id = " + i + " | Tytuł: " + moviesTitle.get(j).text() + ", " + moviesDate.get(i).text().replace("Premiera: ", "") + ", Gatunki: " + movieGenre.get(i).text() + ", Obraz: " + movieImage.get(j).attr("style").replace("background-image:url('//", "http://").replace("');", ""));
                j++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
        }

    }

    public List<String> getCinemaList(){
        List<String> cinematList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("div.dropdown-list > ul > li");

            for(Element e : elements){
                cinematList.add(e.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cinematList;
    }


}

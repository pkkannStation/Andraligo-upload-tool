package aut.control;

import aut.model.Movie;
import aut.model.MovieRegister;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Patrick
 */
public class IDHandler {

    private int currentMovieId;

    private MovieRegister movieRegister;

    private HashMap<Integer, String> deleteList;

    public void refresh() {

        ArrayList<Movie> movies = movieRegister.getObjects();
        for (Movie m : movies) {
            int id = m.getId();
            if (currentMovieId < id) {
                currentMovieId = id;
            }
        }
    }

    public void init(MovieRegister movieRegister) {
        this.movieRegister = movieRegister;
    }

    public int currentMovieId() {
        return currentMovieId;
    }

    public int nextMovieId() {
        this.currentMovieId++;
        return this.currentMovieId;
    }

}

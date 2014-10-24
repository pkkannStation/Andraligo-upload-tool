
package aut.model;

import aut.control.IDHandler;
import aut.db.DAO;

/**
 *
 * @author Patrick
 */
public class MovieRegister extends Register<Movie> {

    public MovieRegister(DAO<Movie> dao, IDHandler idHandler) {
        super(dao, idHandler);
    }
    
    public Movie create(String title, String quality) {
        int id = idHandler.nextMovieId();
        Movie m = new Movie(id, title, quality);
        insert(m);
        return m;
    }

}

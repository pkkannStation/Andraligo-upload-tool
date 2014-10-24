
package aut.db;

import aut.model.Movie;
import aut.utils.DBUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patrick
 */
public class MovieDAO implements DAO<Movie> {

    @Override
    public void insert(Movie object) {
        try {
            Statement st = DBUtil.getStatement();
            
            int id = object.getId();
            String title = object.getTitle();
            String quality = object.getQuality();
            
            String sql = "INSERT INTO movie (id, title, quality) VALUES("+id+", '"+title+"', '"+quality+"');";
            
            st.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Movie object) {
        try {
            Statement st = DBUtil.getStatement();
            
            int id = object.getId();
            String title = object.getTitle();
            String quality = object.getQuality();
            
            String sql = "UPDATE movie SET title='"+title+"', quality='"+quality+"' WHERE id="+id+";";
            
            st.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Movie object) {
        try {
            Statement st = DBUtil.getStatement();
            
            int id = object.getId();
            
            String sql = "DELETE FROM movie WHERE id="+id+";";
            
            st.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<Movie> selectAll() {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            Statement st = DBUtil.getStatement();
            
            String sql = "SELECT * FROM movie;";
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("title");
                String quality = rs.getString("quality");
                Movie m = new Movie(id, name, quality);
                movies.add(m);
            }
            return movies;
        } catch (SQLException ex) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

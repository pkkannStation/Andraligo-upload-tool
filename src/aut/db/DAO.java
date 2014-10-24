
package aut.db;

import aut.model.ModelClass;
import java.util.ArrayList;

/**
 *
 * @author Patrick
 * @param <E>
 */
public interface DAO<E extends ModelClass> {
    
    public void insert(E object);
    
    public void update(E object);
    
    public void delete(E object);
    
    public ArrayList<E> selectAll();

}


package aut.model;

import aut.control.IDHandler;
import aut.db.DAO;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Patrick
 * @param <E>
 */
public abstract class Register<E extends ModelClass> {
    
    protected final ObservableList<E> objects;
    protected final DAO<E> dao;
    protected IDHandler idHandler;
    
    public Register(DAO<E> dao, IDHandler idHandler) {
        objects = FXCollections.observableArrayList();
        this.dao = dao;
        this.idHandler = idHandler;
    }
    
    /**
     * Inserts object into register
     * @param object 
     */
    public void insert(E object) {
        objects.add(object);
        dao.insert(object);
    }
    
    /**
     * Updates object in register.
     * This method only updates object in the database.
     * You will need to update he object manually.
     * @param object 
     */
    public void update(E object) {
        dao.update(object);
    }
    
    /**
     * Gets an object based on id
     * @param id
     * @return object
     */
    public E get(int id) {
        for(E e : objects) {
            if(e.getId() == id) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * Deletes object from register
     * @param object 
     */
    public void delete(E object) {
        objects.remove(object);
        dao.delete(object);
    }
    
    /**
     * Loads all objects from database into memory.
     * This action needs to be executed at application start, or the register wont work as expected.
     */
    public void load() {
        objects.clear();
        objects.addAll(dao.selectAll());
   }
    
    public ArrayList<E> getObjects() {
        ArrayList<E> objs = new ArrayList<>(objects);
        return objs;
    }
    
    public ObservableList<E> getObservableObjects() {
        return objects;
    }

}

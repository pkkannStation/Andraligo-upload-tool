
package aut.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Patrick
 */
public class Movie extends ModelClass {
    
    private final StringProperty title;
    private final StringProperty quality;

    public Movie(int id, String title, String quality) {
        super(id);
        this.title = new SimpleStringProperty(title);
        this.quality = new SimpleStringProperty(quality);
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public String getQuality() {
        return quality.get();
    }
    
    public void setQuality(String quality) {
        this.quality.set(quality);
    }

    public StringProperty getTitleProperty() {
        return title;
    }

    public StringProperty getQualityProperty() {
        return quality;
    }
    
    @Override
    public String toString() {
        return title.get() + " : " + quality.get();
    }
    
}

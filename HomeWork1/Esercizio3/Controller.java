package controller;

import java.beans.*;
import java.io.Serializable;

/**
 * Class implementing the non visual java bean as specified by the assignament 
 * instructions. It provides two properties, "on" and "locHumidity", 
 * named as the public static strings defined below, and puts irrigation on 
 * (on = true), o viceversa, according to events fired close to certain bounds
 * of the local homidity property
 * 
 * @author Lorenzo Bellomo, 531423
 */
public class Controller implements Serializable {
  
    // property names
    public static final String PROP_ON = "on";
    public static final String PROP_LOC_HUMIDITY = "locHumidity";
    
    // the two properties of the component
    protected boolean on;
    protected int locHumidity;
    
    // necessary to fire events on property change
    private final PropertyChangeSupport propertySupport;
   
    
    /**
     * Default constructor
     */
    public Controller() {

        // intializing change support in order to fire events
        propertySupport = new PropertyChangeSupport(this);
        // default value for variables
        on = false;
        locHumidity = 0;
        
        // I have to implement locally the switching on and off of the irrigator
        // so I register to localHumidity change event in order to switch it
        addPropertyChangeListener((java.beans.PropertyChangeEvent evt) -> {
            if (evt.getPropertyName().equals(PROP_LOC_HUMIDITY)) {
                // humidity changing
                if(getOn()) {
                    // irrigator is on, I have to check soil is not too humid
                    if(getLocHumidity() >= 90)
                        setOn(false);
                } else {
                    // irrigator is off, I have to check soil is not too dry
                    if(getLocHumidity() <= 30)
                        setOn(true);
                }
            } 
        });
    }
    
    /**
     * getter method of the bound property "on"
     * @return the value of given property
     */
    public boolean getOn() {
        return on;
    }
    
    /**
     * setter method for the bound property "on"
     * @param value the new value to be put in the property
     */
    public void setOn(boolean value) {
        boolean oldValue = on;
        on = value;
        propertySupport.firePropertyChange(PROP_ON, oldValue, on);
    }
    
    /**
     * getter method of the bound property "locHumidity"
     * @return the value of given property
     */
    public int getLocHumidity() {
        return locHumidity;
    }
    
    /**
     * setter method for the bound property "locHumidity", fires a 
     * PropertyChangeEvent
     * @param value the new value to be put in the property
     */
    public void setLocHumidity(int value) {
        int oldValue = locHumidity;
        locHumidity = value;
        propertySupport.firePropertyChange(PROP_LOC_HUMIDITY, oldValue, locHumidity);
    }
    
    /**
     * method to register to PropertyChangeEvents
     * @param listener the listener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * method to unregister to PropertyChangeEvents
     * @param listener the listener to register
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}

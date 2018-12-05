package moisturesensor;

import java.beans.*;
import java.io.Serializable;
import java.util.Random;
/**
 * Class implementing the non visual java bean as specified by the assignament 
 * instructions. It provides two properties, "decreasing" and "currentHumidity", 
 * named as the public static strings defined below, and a method void that 
 * simulates the actual sensor usage. It implements Runnable since the method 
 * start will run on a non daemon thread, preventing the jFrame to not close 
 * when closing the tab. So in order to avoid it, the method start actually 
 * just makes a daemon thread run, whose code is implemented in this class 
 * and in the void run() method.
 * 
 * @author Lorenzo Bellomo, 531423
 */
public class MoistureSensor implements Serializable, Runnable {
    
    // property names
    public static final String PROP_DECREASING = "decreasing";
    public static final String PROP_CURRENT_HUMIDITY = "currentHumidity";
    // msec of sleep between every sensor read
    public static final int SLEEP_TIME = 1000;
    // maximum moisture progress rate in absolute value every reading of the 
    // sensor
    public static final int PROGRESS_RATE = 25;
    
    // the two properties of the component
    private boolean decreasing;
    private int currentHumidity;
    
    Thread thread;
    
    // necessary to fire events on property change
    private final PropertyChangeSupport propertySupport;
    
    /**
     * Default constructor
     */
    public MoistureSensor() {
        // intializing change support in order to fire events
        propertySupport = new PropertyChangeSupport(this);
        currentHumidity = 50;
        decreasing = true;    
        thread = null;
    }
    
    /**
     * getter method of the bound property "decreasing"
     * @return the value of given property
     */
    public boolean getDecreasing() {
        return decreasing;
    }
    
    /**
     * setter method for the bound property "decreasing", fires a 
     * PropertyChangeEvent
     * @param value the new value to be put in the property
     */
    public void setDecreasing(boolean value) {
        boolean oldValue = decreasing;
        decreasing = value;
        propertySupport.firePropertyChange(PROP_DECREASING, oldValue, decreasing);
    }
    
    /**
     * getter method of the bound property "currentHumidity"
     * @return the value of given property
     */
    public int getCurrentHumidity() {
        return currentHumidity;
    }
    
    /**
     * setter method for the bound property "currentHumidity", fires a 
     * PropertyChangeEvent
     * @param value the new value to be put in the property
     */
    public void setCurrentHumidity(int value) {
        int oldValue = currentHumidity;
        currentHumidity = value;
        propertySupport.firePropertyChange(PROP_CURRENT_HUMIDITY, oldValue, currentHumidity);
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
    
    /**
     * Starts the daemon thread which will execute the moisture sensor code. 
     * The thread is a daemon in order to allow the JVM to shut down the moment
     * every other non daemon thread ends.
     */
    public void start() {
        if(thread == null) {
            thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
        // else do nothing, as it means the thread is already running
    }

    @Override
    /**
     * Method that simulates the actual readings of a Moisture Sensor, 
     * respecting the value of the decreasing property.
     * The simulation works like this: every second the method computes a 
     * random number in a given range and updates the humidity value up or down
     * according to the humidity parameter
     */
    public void run() {
        Random rand = new Random();
        while (true) {
            // I'm moving humudity at a rate of [0-5] per time in order to 
            // try and emulate as much as possibile a non apocalyptic shift in 
            // humidity, but enough to see a change in a decent time
            int step = rand.nextInt(PROGRESS_RATE);

            if(step != 0) {
                // if there is no step I limit this iteration to a 1 sec sleep
                if(decreasing && currentHumidity != 0) {
                    // no point in trying to update property value if moisture is 
                    // decreasing and humidity is already at its minimum
                    if(currentHumidity - step <= 0)
                        this.setCurrentHumidity(0);
                    else
                        this.setCurrentHumidity(currentHumidity - step);
                } else if (!decreasing && currentHumidity != 100) {
                    // no point in trying to update property value if moisture is 
                    // increasing and humidity is already at its maximum
                    if(currentHumidity + step >= 100)
                        this.setCurrentHumidity(100);
                    else
                        this.setCurrentHumidity(currentHumidity + step);
                }
            }
            // sleeping 1 second
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ex) {}
        }
    }
    
}

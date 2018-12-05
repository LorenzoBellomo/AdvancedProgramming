/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllerveto;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

/**
 *Extention to previosly made controller component.
 * The idea is that the controller inherits the needed functionalities and 
 * simply adds the VetoableChangeSupport to implement the veto system, and 
 * redefines the method setOn in order to support vetos.
 * The veto event publisher and the veto event subscriber are both the same 
 * class (this one), for the reasons explained in the report
 * @author Lorenzo Bellomo
 */
public class ControllerVeto extends controller.Controller {
    
    // I just need this one more private variable to support the things
    // that I am missing from Controller (Vetos)
    private VetoableChangeSupport vetos;
    
    /**
     * Default constructor. Does the same things the superclass does but also 
     * initializes the VetoableChangeSupport and the listener to vetos too.
     */
    public ControllerVeto() {
        super();
        vetos = new VetoableChangeSupport(this);
        addVetoableChangelistener((PropertyChangeEvent evt) -> {
            // I first check I'm dealing with the right property
            if(evt.getPropertyName().equals(PROP_ON)) {
                // Right property, I have to check that the value actually changed
                if((Boolean) evt.getNewValue() && !(Boolean)evt.getOldValue()) {
                    // I'm turning the irrigator on
                    if(getLocHumidity() > 60) // Too much humidity, cant' turn on
                        throw new PropertyVetoException("Refused, Humidity too high", evt);
                    else // Ok can turn it on
                        super.setOn(true);
                } else {
                    if(getLocHumidity() < 50)
                        throw new PropertyVetoException("Refused, Humidity too low", evt);
                    else 
                        super.setOn(false);
                }
            }
        });
    }
    
    public void addVetoableChangelistener(VetoableChangeListener l) { 
        vetos.addVetoableChangeListener(l);
    }
    
     public void removeVetoableChangelistener(VetoableChangeListener l) { 
        vetos.removeVetoableChangeListener(l);
    }
     
    @Override
    /**
     * Only method to actually change from superclass, setOn fires a Vetoable
     * Change event instead of a PropertyChangeEVT
     */
    public void setOn(boolean value) {
        boolean oldValue = getOn();
        if(value == oldValue) { //same value as before
            return;
        }
        try {
            vetos.fireVetoableChange(PROP_ON, oldValue, value);
            // no veto
            on = value;
        } catch (PropertyVetoException ex) {
            // received a veto, can't change property value
            System.out.println("Irrigator switching was refused, VETOED");
        }
    }
    
}

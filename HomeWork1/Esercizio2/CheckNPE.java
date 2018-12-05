package checknpe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that implements in the main method the code of exercise 2 of the home
 * assignement. 
 * @author Lorenzo Bellomo, 531423
 */
public class CheckNPE {

    /**
     * Main class for the exercise 2.
     * It retrieves from command line the class names to check, then for
     * each of those obtains the method and constructor decleared in given
     * class. It first analyzes the constructors, and for each of them which 
     * succedes in building a new instance of an object, that new instance gets
     * put in a pool of instances that will be analyzed later when checking for 
     * NPE sensibility of methods.
     * Methods later get executed with those stored instances and with default 
     * parameters
     * 
     * @param args the command line arguments containing class names
     */
    public static void main(String[] args) {
        
        if(args.length == 0) {
            // checking that at least one class name has been passed
            System.out.println("Usage: Need at least one argument");
            return;
        }
        
        for (String className : args) {
            // for each class
        
            Class<?> reqClass;
            // tries retrieving the class name
            try {
                reqClass = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                // couldn't find that class in current directory or in PATH
               System.out.println("Couldn't find class " + className);
               // proceeding to next class name in args
               continue;
            }
            
            // a class with given name was found, starting by printing class 
            // name 
            System.out.println("{");
            System.out.println("Class name: " + className);
            // I retrieve methods and constructors defined in requested class,
            // not on his superclasses
            Method[] methods = reqClass.getDeclaredMethods();
            Constructor[] constructors = reqClass.getDeclaredConstructors();
            // in this array list I will store all the instances of reqClass 
            // that I will be able to instantiate through all constructors
            ArrayList<Object> instances = new ArrayList<>();
            // starting to print constructors
            System.out.println("constructor list : ");
            System.out.println("[");
            for(Constructor constr : constructors) {
                // for each constructor i print the name
                System.out.print("(\"" + constr.getName() + "\", parameters: ");
                // I obtain the parameter types, so I can get default instances
                Class<?>[] paramType = constr.getParameterTypes();
                // in parameters I will put default parameters later
                Object[] parameters = new Object[paramType.length];
                // I'll keep it false until I find any reference type parameter
                boolean hasReferenceType = false;
                // starting to print parameter types
                System.out.print("<");
                for (int i = 0; i < paramType.length; i++) {
                    // printing class name of the parameter
                    System.out.print(paramType[i].getName());
                    // if it's not the last one i put comma
                    if (i != paramType.length - 1)
                        System.out.print(", ");
                    // I instantiate given parameter with its corresponding 
                    // default value
                    parameters[i] = newDefaultInstance(paramType[i]);
                    // if it is not primitive I remember by changing the variable
                    if(!paramType[i].isPrimitive())
                        hasReferenceType = true;
                }
                // finally out of parameters to parse
                System.out.print(">");
                if(hasReferenceType) {
                    /* 
                     * if I found a reference type, I have to print if
                     * it is NPE sensible
                     */
                    /*
                    *Since I'm dealing with constructors, I want to create as 
                    * many instances as possible of reqClass, so that I have 
                    * more instances later (when dealing with methods), to try 
                    * and apply them
                    */ 
                    boolean sensibleNPE = false;
                    // I'll try and instantiate the object with given constructor
                    // and I'll keep this variable true if i succeed
                    boolean createdInstance = true;
                    // This is the variable where the instance will be put
                    Object instance = null;
                    try {
                        // trying to create the instance, then checking
                        // if exception occours
                        instance = constr.newInstance(parameters);
                    } catch (InvocationTargetException ex) {
                        // this is the exception that gets thrown if an 
                        // an exception caused the constructor to fail, so 
                        // O just have to check if the cause was a NPE
                        if(ex.getCause() instanceof NullPointerException) {
                            // it was a NPE
                            sensibleNPE = true;
                            createdInstance = false;
                        } else {
                            // it was another exception
                            createdInstance = false;
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InstantiationException ex) {
                        // other exceptions that can occour by trying to use
                        // given constructor, I ignore those
                        createdInstance = false;
                    }
                    if(createdInstance) {
                        // I created an instance, so I'm able to put it in the 
                        // instances pool
                        instances.add(instance);
                    }
                    if(sensibleNPE) 
                        System.out.print(" NPE sensible");
                    else 
                        System.out.print(" NOT NPE sensible");
                } else {
                    // There are no reference types, I still try and create an 
                    // instance for the pool but I limit my work to that
                    try {
                        Object instance = constr.newInstance(parameters);
                        instances.add(instance);
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    }
                }
                System.out.println(")");
            }
            System.out.println("]");
            // End of constructors, passing to methods, and I will use every 
            // instance generated before and that is in the instance pool
            // to apply the methods in order to check if the method is NPE 
            // sensible for any of those instances
            
            // the work needed for methods is very similar to the one done 
            // previously for constructors
            System.out.println("method list: ");
            System.out.println("[");
            for(Method m : methods) {
                System.out.print("(\"" + m.getName() + "\", parameters: ");
                Class<?>[] paramType = m.getParameterTypes();
                Object[] parameters = new Object[paramType.length];
                boolean hasReferenceType = false;
                System.out.print("<");
                for (int i = 0; i < paramType.length; i++) {
                    System.out.print(paramType[i].getName());
                    if (i != paramType.length - 1)
                        System.out.print(", ");
                    // obtaining the default value for given parameter
                    parameters[i] = newDefaultInstance(paramType[i]);
                    if(!paramType[i].isPrimitive())
                        hasReferenceType = true;
                }
                // ended parameters, might have to check if NPE sensible
                System.out.print(">");
                boolean sensibleNPE = false;
                if(hasReferenceType) {
                    if(instances.isEmpty()) {
                        // I couldn't create any instance to try the methods, so 
                        // I can't check at all
                        System.out.println("No valid instances to try NPE sensibility)");
                        continue;
                    }
                    // I'll try methods with each and every one of the instances
                    Iterator<Object> iterator = instances.iterator();
                    // I'll stop when out of instances or if I get a NPE
                    while (iterator.hasNext() && !sensibleNPE) {
                        Object instance = iterator.next();
                        try {
                            m.invoke(instance, parameters);
                        } catch (InvocationTargetException ex) {
                            // I have to check if I got a NPE
                            if(ex.getCause() instanceof NullPointerException) 
                            sensibleNPE = true;
                        } catch (IllegalAccessException | IllegalArgumentException ex) {
                            // other non clear error in invocation
                        }
                    }
                    if(sensibleNPE) 
                        System.out.print(" NPE sensible");
                    else 
                        System.out.print(" NOT NPE sensible");
                }
                System.out.println(")");
            }
            System.out.println("]");
            System.out.println("}");
            // Iteration on next class in args
        }
        
    }

    /**
     * Creates a default instance of the given class (0 for numeric, false
     * for boolean and null for reference)
     * @param aClass Class object to instantiate
     * @return the instance according to requirements
     */
    private static Object newDefaultInstance(Class<?> aClass) {
        if(aClass.isPrimitive()) {
            // numeric type or boolean
            if(aClass.equals(boolean.class)) 
                return false;
            else 
                return 0;
        } else // reference type
            return null;
    }
}

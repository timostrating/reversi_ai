package Util;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;


/**
 * Stolen from an old java project done in 1.2 https://github.com/timostrating/parkingsimulator/blob/master/core/src/com/parkingtycoon/helpers/Delegate.java
 * @author Originaly GGG
 */
public class Delegate<T> {

    // TODO: if multithreading is required => build a fully Tread safe implementation of C# delegates.
    private ArrayList<T> list = new ArrayList<>();

    private int timesNotified = 0;

    public Delegate() { }
    public Delegate(boolean multiThreaded) {
        if (multiThreaded)
            throw new NotImplementedException();
    }

    public int getTimesNotified() {
        return timesNotified;
    }

    /**
     * We use this FunctionalInterface to make it possible to replace code with lambda's and method references.
     * @param <T> the generic object
     */
    @FunctionalInterface
    public interface Notifier<T> {
        void notify(T object);
    }

    /**
     * We use this FunctionalInterface to make it possible to replace code with lambda's and method references.
     * @param <T> the generic object
     */
    @FunctionalInterface
    public interface Starter<T> {
        void start(T object);
    }

    /**
     * Register a Item to the List or waiting queues depending on the fact that you enabled multithreading support or not.
     *
     * @param object the object that you would like to add.
     * @return if it was successful
     */
    public boolean register(T object) {
        return list.add(object);
    }

    /**
     * UnRegister a Item to the List or waiting queues depending on the fact that you enabled multithreading support or not.
     *
     * @param object the object that you would like to remove.
     * @return if it was successful
     */
    public boolean unregister(T object) {
        return list.remove(object);
    }


    /**
     * Foreach registered item call the Notifier
     *
     * @param notifier the Lambda or method reference that will point to the function that we will call
     */
    public void notifyObjects(Notifier<T> notifier) {
        timesNotified ++;
        for (T object : list)
            notifier.notify(object);
    }
}

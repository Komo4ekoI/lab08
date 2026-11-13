package it.unibo.mvc;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.controller.DrawNumberControllerImpl;
import it.unibo.mvc.model.DrawNumberImpl;
import java.util.Set;

/**
 * Application entry-point.
 */
public final class LaunchApp { 
    private static final String VIEW_CLASS_PREFIX = "it.unibo.mvc.view.DrawNumber";
    private static final String VIEW_CLASS_SUFIX = "View";
    private static final Collection<String> VIEW_INSTANCE_NAMES = Set.of("Swing", "StandardOutput");
    private static final int SINGLE_INSTANCE_COUNT = 3;

    private LaunchApp() { }

    /**
     * Runs the application.
     *
     * @throws ClassNotFoundException if the fetches class does not exist
     * @throws NoSuchMethodException if the 0-ary constructor do not exist
     * @throws InvocationTargetException if the constructor throws exceptions
     * @throws InstantiationException if the constructor throws exceptions
     * @throws IllegalAccessException in case of reflection issues
     * @throws IllegalArgumentException in case of reflection issues
     */
    public static void main() throws 
    ClassNotFoundException, 
    InstantiationException, 
    IllegalAccessException, 
    InvocationTargetException, 
    NoSuchMethodException {
        final var model = new DrawNumberImpl();
        final DrawNumberController app = new DrawNumberControllerImpl(model);
        for (final String viewInstanceName : VIEW_INSTANCE_NAMES) {
            final var classIntance = Class.forName(
                VIEW_CLASS_PREFIX + viewInstanceName + VIEW_CLASS_SUFIX);
            if (!DrawNumberView.class.isAssignableFrom(classIntance)) {
                throw new IllegalArgumentException(
                    classIntance + "is not a sub-class of " + DrawNumberView.class);
            }
            for (int i = 0; i < SINGLE_INSTANCE_COUNT; i++) {
                final var view = classIntance.getConstructor().newInstance();
                app.addView((DrawNumberView) view);
            }
        }
    }
}

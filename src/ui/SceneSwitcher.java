/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to use to switch JavaFX scenes
 */
public class SceneSwitcher {
    private static boolean lightTheme = true; //light theme - true, dark theme - false
    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();

    /**
     * Constructor to make this object and set stage
     * @param stage The main stage for my app
     */
    public SceneSwitcher(Stage stage) {
        this.stage = stage;
        SettingsController.setStage(stage);
    }

    /**
     * Method to switch scenes on the main stage
     * @param view The sceneView to switch to
     * @throws IOException If the view fails to load, it'll throw this exception
     */
    public void switchTo(SceneView view) throws IOException{
        // If already loaded, just reuse it
        if (scenes.containsKey(view.getPath())) {
            Scene scene = scenes.get(view.getPath());
            updateTheme();
            stage.setScene(scene);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getPath()));

            loader.setControllerFactory(controllerClass -> {
                try {
                    // Look for a constructor that takes SceneSwitcher
                    Object controller = controllerClass.getConstructor(SceneSwitcher.class)
                            .newInstance(this);
                    if (controller instanceof DashboardController dashController) {
                        HabitCardController.setDashboardController(dashController);
                        //I forget but convert to Dashboard Controller instance
                        SettingsController.setDashboardController(dashController);
                    }

                    return controller;
                } catch (NoSuchMethodException e) {
                    // Otherwise just call the no-arg constructor
                    try {
                        return controllerClass.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException |
                             InvocationTargetException | NoSuchMethodException ex) {
                        throw new RuntimeException("Failed to create controller: "
                                + controllerClass, ex);
                    }
                } catch (InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException("Failed to create controller: " +
                            controllerClass, e);
                }
            });

            Parent root = loader.load();
            final int width = 800;
            final int height = 600;
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource(getTheme()).toExternalForm());
            scenes.put(view.getPath(), scene);
            stage.setScene(scene);
        }
    }

    /**
     * Method to update the theme of the app overall
     */
    public void updateTheme(){
        for(Scene scene : scenes.values()){
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(getTheme()).toExternalForm());
        }
    }

    public static String getTheme(){
        return lightTheme ? "../resources/light-theme.css" : "../resources/dark-theme.css";
    }

    public static void setTheme(Boolean theme){
        lightTheme = theme;
    }
}

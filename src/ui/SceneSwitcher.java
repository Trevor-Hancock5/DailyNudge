/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/27/2025
 */
package ui;

import com.sun.scenario.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SceneSwitcher {
    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();

    public SceneSwitcher(Stage stage) {
        this.stage = stage;
        SettingsController.setStage(stage);
    }

    public void switchTo(SceneView view) {
        try {
            // If already loaded, just reuse it
            if (scenes.containsKey(view.getPath())) {
                stage.setScene(scenes.get(view.getPath()));
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getPath()));

            loader.setControllerFactory(controllerClass -> {
                try {
                    // Look for a constructor that takes SceneSwitcher
                    Object controller = controllerClass.getConstructor(SceneSwitcher.class).newInstance(this);
                    if(controller instanceof DashboardController dashController){
                        HabitCardController.setDashboardController(dashController); //I forget but convert to Dashboard Controller instance
                        SettingsController.setDashboardController(dashController);
                        AllHabitsController.setDashboardController(dashController);
                    }

                    return controller;
                } catch (NoSuchMethodException e) {
                    // Otherwise just call the no-arg constructor
                    try {
                        return controllerClass.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                        throw new RuntimeException("Failed to create controller: " + controllerClass, ex);
                    }
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("Failed to create controller: " + controllerClass, e);
                }});


            Parent root = loader.load();
            int width = 800;
            int height = 600;
            Scene scene = new Scene(root, width, height);
            scenes.put(view.getPath(), scene);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

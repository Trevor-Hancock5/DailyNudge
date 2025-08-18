/*
 * Course: CSC1110A-111
 * Fall 2024
 * Assignment
 * Name: Trevor Hancock
 * Last Updated: 8/18/2025
 */
package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SceneSwitcher {
    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();

    public SceneSwitcher(Stage stage) {
        this.stage = stage;
    }

    public void switchTo(String fxmlFile) {
        try {

            // If already loaded, just reuse it
            if (scenes.containsKey(fxmlFile)) {
                stage.setScene(scenes.get(fxmlFile));
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

            loader.setControllerFactory(controllerClass -> {
                try {
                    // Look for a constructor that takes SceneSwitcher
                    return controllerClass.getConstructor(SceneSwitcher.class).newInstance(this);
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
            scenes.put(fxmlFile, scene);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

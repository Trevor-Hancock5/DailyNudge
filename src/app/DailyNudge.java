/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/23/2025
 */
package app;


import javafx.application.Application;
import javafx.stage.Stage;
import model.HabitManager;
import ui.SceneSwitcher;
import ui.SceneView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DailyNudge extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        SceneSwitcher switcher = new SceneSwitcher(stage);
        SceneView.DASHBOARD.switchTo(switcher);
        stage.setTitle("DailyNudge");
        stage.show();

        //Save on window Close
        stage.setOnCloseRequest(e -> {
            if(HabitManager.saveHabits()) {  // calls FileManager under the hood
                System.out.println("Habits saved!");
            } else{
                System.out.println("Failed save.");
            }
        });
    }
}

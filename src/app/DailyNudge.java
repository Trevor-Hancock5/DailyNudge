/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package app;


import javafx.application.Application;
import javafx.stage.Stage;
import model.HabitManager;
import ui.SceneSwitcher;
import ui.SceneView;


/**
 * Class to start up the app
 */
public class DailyNudge extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneSwitcher switcher = new SceneSwitcher(stage);
        SceneView.DASHBOARD.switchTo(switcher);
        stage.setTitle("DailyNudge");
        stage.show();

        //Save on window Close
        stage.setOnCloseRequest(_ -> {
            if(HabitManager.saveHabits()) {  // calls FileManager under the hood
                System.out.println("Habits saved!");
            } else{
                System.out.println("Failed save.");
            }
        });
    }
}

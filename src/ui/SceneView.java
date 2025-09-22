/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package ui;

import java.io.IOException;

/**
 * Enum class to standardize these strings
 */
public enum SceneView {
    /**
     * Dashboard String for Scene
     */
    DASHBOARD("/ui/Dashboard.fxml"),
    /**
     * New Habit String for Scene
     */
    NEW_HABIT("/ui/NewHabit.fxml"),
    /**
     * Settings String for Scene
     */
    SETTINGS("/ui/Settings.fxml");

    private final String path;

    SceneView(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    /**
     * Method to switch to another view
     * @param switcher SceneSwitcher to use to switch
     */
    public void switchTo(SceneSwitcher switcher) throws IOException {
        switcher.switchTo(this);
    }
}

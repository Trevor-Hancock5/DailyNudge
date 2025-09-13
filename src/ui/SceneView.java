/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/27/2025
 */
package ui;

public enum SceneView {
    DASHBOARD("/ui/Dashboard.fxml"),
    NEW_HABIT("/ui/NewHabit.fxml"),
    SETTINGS("/ui/Settings.fxml"),
    ALL_HABITS("/ui/AllHabits.fxml");

    private final String path;

    SceneView(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public void switchTo(SceneSwitcher switcher){
        switcher.switchTo(this);
    }
}

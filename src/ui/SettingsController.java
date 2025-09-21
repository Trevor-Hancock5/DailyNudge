/*
 * Course: CSC1110A-111
 * Fall 2024
 * Assignment
 * Name: Trevor Hancock
 * Last Updated: 9/12/2025
 */
package ui;

import app.StorageManager;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Habit;
import model.HabitManager;

import java.io.FileWriter;
import java.util.List;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class SettingsController {
    @FXML
    private ImageView imageView;
    @FXML
    private TextField name;
    @FXML
    private Label version;
    @FXML
    private ToggleButton lightMode;
    @FXML
    private ToggleButton flexibleStr;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox backgroundVBox;

    private SceneSwitcher switcher;
    private static DashboardController dashboardController;
    private static Stage stage;
    private static String userName;
    private static String profileDir;
    private static String themePreference;
    private static String streakPreference;

    public SettingsController(SceneSwitcher switcher){
        this.switcher = switcher;
    }

    public static String[] getSettingPreferences(){
        return new String[] {userName, profileDir, themePreference, streakPreference};
    }

    public static void setUserName(String name){
        userName = name;
    }

    public static String getUserName(){
        return userName;
    }

    //TODO: Make way to choose file for picture - 1:1
    public static void setProfileDir(String dir){
        profileDir = dir;
    }

    public static void setThemePreference(String theme){
        themePreference = theme;
        SceneSwitcher.setTheme(theme.equals("light"));
    }

    public static void setStreakPreference(String streak){
        streakPreference = streak;
    }

    public static String getStreakPreference(){
        return streakPreference;
    }

    public static void setStage(Stage stage){
        SettingsController.stage = stage;
    }

    @FXML
    private void initialize(){
        scrollPane.setContent(backgroundVBox);
        scrollPane.setFitToWidth(true);
        if(userName != null) {
            name.setText(userName);
            dashboardController.title.setText(name.getText() + "'s DailyNudge");
        }

        Image img;
        try{
            img = new Image(Objects.requireNonNull(getClass().getResource(profileDir)).toExternalForm());
        } catch (NullPointerException | IllegalArgumentException e) {
            img = new Image(Objects.requireNonNull(getClass().getResource("/resources/defaultAvatar.jpg")).toExternalForm());
        }
        imageView.setImage(img);

        if(themePreference != null && !themePreference.equals("light")){
            lightMode.setSelected(false);
            lightMode.setText("Dark Mode");
        } else{
            lightMode.setSelected(true);
        }
        changeTheme();

        if(streakPreference != null && !streakPreference.equals("flex")) {
            flexibleStr.setSelected(false);
            flexibleStr.setText("Normal Streak");
        } else{
            flexibleStr.setSelected(true);
        }
        Habit.setStreakRule(!flexibleStr.isSelected());
        HabitManager.saveHabits();

        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        Circle clip = new Circle(imageView.getFitWidth()/2,imageView.getFitHeight()/2, radius);
        imageView.setClip(clip);

        version.setText("v1.0.0");
    }

    public void changeTheme(){
        if(SceneSwitcher.getTheme().contains("light")){
            backgroundVBox.setStyle("-fx-background-color: #f9f9f9;");
        } else{
            backgroundVBox.setStyle("-fx-background-color: #1e1e1e;");
        }
    }

    @FXML
    private void getName(){
        name.requestFocus();
        userName = name.getText();
        dashboardController.title.setText(userName + "'s DailyNudge");
    }

    @FXML
    private void flexibleStreak() {
        if(flexibleStr.isSelected()){
            flexibleStr.setText("Flexible Streak");
            streakPreference = "flex";
        } else{
            flexibleStr.setText("Normal Streak");
            streakPreference = "norm";
        }
        Habit.setStreakRule(!flexibleStr.isSelected());
        HabitManager.saveHabits();
    }

    @FXML
    private void feedback() {
        try {
            Desktop.getDesktop().browse(new URI("https://forms.gle/UVMA5KcSmc4Xyiqm7"));
        } catch (URISyntaxException | IOException e){
            DashboardController.showAlert("Error with opening feedback form. Sorry!");
        }
    }

    @FXML
    private void changeMode(){
        if(lightMode.isSelected()){
            lightMode.setText("Light Mode");
            switcher.setTheme(true);
            themePreference = "light";
        } else{
            lightMode.setText("Dark Mode");
            switcher.setTheme(false);
            themePreference = "dark";
        }
        switcher.updateTheme();
        changeTheme();
    }

    @FXML
    private void importData() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Habit Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            HabitManager.saveHabits(readJsonFile(selectedFile));
        }
    }

    private List<Habit> readJsonFile(File file){
        List<Habit> habits = null;
        try (FileReader reader = new FileReader(file)) {
            Gson gson = StorageManager.getGson();
            habits = gson.fromJson(reader, new TypeToken<List<Habit>>() {}.getType());
            for(Habit habit : habits){
                habit.streakAndPercentage(dashboardController.habitDate.getValue());
            }
        } catch (IOException e) {
            System.out.println("Error importing habits: " + e);
        }
        return habits;
    }

    @FXML
    private void save() throws IOException {
        HabitManager.saveHabits();
        dashboardController.updateHabits(dashboardController.allHabits.isSelected());
    }

    @FXML
    private void backup(){
        File backupFile = new File("data/userData_backup.json");
        try {
            try (FileWriter writer = new FileWriter(backupFile)) {
                Gson gson = StorageManager.getGson();
                gson.toJson(HabitManager.getHabits(), writer);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void export(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Location for Exported Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if(selectedFile != null){
            try(FileWriter file = new FileWriter(selectedFile)){
                Gson gson = StorageManager.getGson();
                gson.toJson(HabitManager.getHabits(), file);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void resetAllData() throws IOException {
        Alert check = new Alert(Alert.AlertType.CONFIRMATION);
        check.setTitle("Reset Data?");
        check.setHeaderText("Are you sure you want to reset all your data?");
        check.setContentText("Once you confirm, there is no way to retrieve your data!");
        Optional<ButtonType> result = check.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            File dataFile = new File("data/userData.json");
            if(dataFile.exists()){
                if(dataFile.delete()){
                    System.out.println("Data reset");
                    dashboardController.updateHabits(dashboardController.allHabits.isSelected());
                } else{
                    System.out.println("Reset failed");
                }
            }
        }
    }

    @FXML
    private void aboutMe(){
        DashboardController.showAlert("""
            Hello! I am Trevor Hancock, the creator of DailyNudge.
            Thank you for using DailyNudge. I enjoyed working on this project.
            I hope that it is a tool that you believe is useful.
            It was definitely a challenge to make and get right, but I am so
            glad with how far I was able to get it! 
            Let me know of any bugs through the feedback button in settings! 
            """);
    }

    @FXML
    private void backToDash() throws IOException {
        SceneView.DASHBOARD.switchTo(switcher);
        dashboardController.updateHabits(dashboardController.allHabits.isSelected());
    }

    protected static void setDashboardController(DashboardController dash){
        dashboardController = dash;
    }
}

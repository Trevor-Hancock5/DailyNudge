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
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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

public class SettingsController {
    @FXML
    ImageView imageView;
    @FXML
    TextField name;
    @FXML
    TextField age;
    @FXML
    Label version;
    @FXML
    ToggleButton lightMd;
    @FXML
    ToggleButton darkMd;
    @FXML
    ToggleButton flexibleStr;

    private SceneSwitcher switcher;
    private static DashboardController dashboardController;
    private static Stage stage;

    public SettingsController(SceneSwitcher switcher){
        this.switcher = switcher;
    }

    public static void setStage(Stage stage){
        SettingsController.stage = stage;
    }

    @FXML
    private void initialize(){
        Image img = new Image(Objects.requireNonNull(getClass().getResource("/ui/defaultAvatar.jpg")).toExternalForm());
        imageView.setImage(img);

        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        Circle clip = new Circle(imageView.getFitWidth()/2,imageView.getFitHeight()/2, radius);
        imageView.setClip(clip);

        lightMd.setSelected(true);
        darkMd.setSelected(false);
        flexibleStr.setSelected(true);
    }

    @FXML
    private void flexibleStreak() {
        System.out.println(flexibleStr.isSelected());
        System.out.println("If true, flexible streak enabled. False - normal");
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
    private void lightMode(){
        //TODO
    }

    @FXML
    private void darkMode(){
        //TODO
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
        dashboardController.updateHabits();
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
                    dashboardController.updateHabits();
                } else{
                    System.out.println("Reset failed");
                }
            }
        }
    }

    @FXML
    private void viewAllHabits(){
        SceneView.ALL_HABITS.switchTo(switcher);
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
        dashboardController.updateHabits();
    }

    protected static void setDashboardController(DashboardController dash){
        dashboardController = dash;
    }
}

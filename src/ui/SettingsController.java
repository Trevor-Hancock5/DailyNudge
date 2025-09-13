/*
 * Course: CSC1110A-111
 * Fall 2024
 * Assignment
 * Name: Trevor Hancock
 * Last Updated: 9/12/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;
import model.Habit;
import model.HabitManager;

import java.awt.*;
import java.io.File;
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

    public SettingsController(SceneSwitcher switcher){
        this.switcher = switcher;
    }

    @FXML
    private void initialize(){
        Image img = new Image(Objects.requireNonNull(getClass().getResource("/ui/defaultAvatar.jpg")).toExternalForm());
        imageView.setImage(img);  // ✅ works on ImageView

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

    }

    @FXML
    private void darkMode(){

    }

    @FXML
    private void importData(){

    }

    @FXML
    private void save() throws IOException {
        HabitManager.saveHabits();
        dashboardController.updateHabits();
    }

    @FXML
    private void backup(){

    }

    @FXML
    private void export(){

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
    private void backToDash(){
        SceneView.DASHBOARD.switchTo(switcher);
    }

    protected static void setDashboardController(DashboardController dash){
        dashboardController = dash;
    }
}

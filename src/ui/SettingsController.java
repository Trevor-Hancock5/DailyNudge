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
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;
import model.HabitManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    ToggleButton normalStr;
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

        name.requestFocus();
        lightMd.setSelected(true);
        darkMd.setSelected(false);
        flexibleStr.setSelected(true);
        normalStr.setSelected(false);
    }

    @FXML
    private void normalStreak(){

    }

    @FXML
    private void flexibleStreak(){

    }

    @FXML
    private void feedback(){

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

    }

    @FXML
    private void backToDash(){
        SceneView.DASHBOARD.switchTo(switcher);
    }

    protected static void setDashboardController(DashboardController dash){
        dashboardController = dash;
    }
}

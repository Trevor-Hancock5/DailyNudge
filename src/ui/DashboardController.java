/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 7/23/2025
 */
package ui;

import app.DailyNudge;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Habit;
import model.HabitManager;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class DashboardController {
    //Basically for each habit load a habitcard that is inputed into a slot of the grid pane.
    //(refer to chat notes)
    @FXML
    private GridPane gridpane;

    private SceneSwitcher switcher;

    public DashboardController(SceneSwitcher switcher) {
        this.switcher = switcher;
    }

    public DashboardController(){

    }

    @FXML
    private void openSettings(){
        //No UI made yet
        System.out.println("Settings");
    }

    @FXML
    private void makeNewHabit(ActionEvent event) throws IOException{
        switcher.switchTo("/ui/NewHabit.fxml");

//        FXMLLoader newHabitLoader = new FXMLLoader();
//        newHabitLoader.setLocation(getClass().getResource("../ui/NewHabit.fxml"));
//        try {
//            Parent newHabitRoot = newHabitLoader.load();
//            final int width = 583;
//            final int height = 500;
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // get current stage
//            stage.setScene(new Scene(newHabitRoot, width, height));
//            stage.setTitle("Making a New Habit");
//            stage.show();
//        } catch (IOException e) {
//            showAlert("Error loading new habit screen");
//        }
    }

    @FXML
    private void viewStats(){
       //No UI made yet
        System.out.println("stats");
    }

    @FXML
    private void initialize() throws IOException {
        List<Habit> habits = HabitManager.loadHabits();
        int column = 0;
        int row = 0;

        List<Habit> todayHabits = HabitManager.getTodayHabits();
        List<Habit> prioritizedHabits = todayHabits.stream().sorted(Comparator.comparingInt(Habit::getPriority)).toList();

        for(Habit habit: prioritizedHabits){
            FXMLLoader habitCardLoader = new FXMLLoader(getClass().getResource("HabitCard.fxml"));
            Parent habitCardRoot = habitCardLoader.load();
            HabitCardController habitCardController = habitCardLoader.getController();
            habitCardController.setHabit(habit);

            //For every 3 columns(0,1,2), increase row by 1.
            gridpane.add(habitCardRoot, column, row);
            if(++column > 2){
                column = 0;
                row++;
            }

            FXMLLoader habitDetailsLoader = new FXMLLoader(getClass().getResource("HabitDetails.fxml"));
            Parent habitDetailsRoot = habitDetailsLoader.load();
            HabitDetailsController habitDetailsController = habitDetailsLoader.getController();
            habitDetailsController.setInfo(habit);

            habitCardController.setHabitDetails(habitDetailsRoot);
        }

    }

    /**
     * Method to make an alert so any controller can pop an alert up
     */
    public static void showAlert(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}

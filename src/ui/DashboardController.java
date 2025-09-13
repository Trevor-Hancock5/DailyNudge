/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/23/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import model.Habit;
import model.HabitManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class DashboardController {
    @FXML
    private GridPane gridpane;
    @FXML
    protected DatePicker habitDate;

    public static SceneSwitcher switcher;

    public DashboardController(SceneSwitcher switcher) {
        DashboardController.switcher = switcher;
    }

    //Default controller
    public DashboardController(){}

    @FXML
    private void openSettings(){
        SceneView.SETTINGS.switchTo(switcher);
    }

    @FXML
    private void makeNewHabit() {
        SceneView.NEW_HABIT.switchTo(switcher);
    }

    @FXML
    private void viewStats(){
       //TODO - Make UI
        System.out.println("stats");
    }

    @FXML
    private void newDate() throws IOException {
        updateHabits();
    }

    @FXML
    private void initialize() throws IOException {
        NewHabitController.setDashboardController(this);
        HabitManager.loadHabits();
        habitDate.setValue(LocalDate.now());
        gridpane.setHgap(15);
        gridpane.setVgap(25);
        gridpane.setPadding(new Insets(20));
        int column = 0;
        int row = 0;

        List<Habit> todayHabits = HabitManager.getTodayHabits(habitDate.getValue());
        List<Habit> prioritizedHabits = todayHabits.stream().sorted(Comparator.comparingInt(Habit::getPriority).reversed()).toList();

        for(Habit habit: prioritizedHabits){
            habit.streakAndPercentage(habitDate.getValue());
            FXMLLoader habitCardLoader = new FXMLLoader(getClass().getResource("HabitCard.fxml"));
            Parent habitCardRoot = habitCardLoader.load();
            habitCardRoot.setStyle(
                    "-fx-background-color: lightgrey;" +   // background
                    "-fx-border-radius: 15;" +           // rounded corners
                    "-fx-background-radius: 15;" +       // match background to border radius
                    "-fx-border-color: #cccccc;" +       // border color
                    "-fx-border-width: 1;"
            );
            HabitCardController habitCardController = habitCardLoader.getController();
            habitCardController.setHabit(habit);

            //For every 3 columns(0,1,2), increase row by 1.
            gridpane.add(habitCardRoot, column, row);
            //To center each card in it's cell
            GridPane.setHalignment(habitCardRoot, HPos.CENTER); // horizontal center
            GridPane.setValignment(habitCardRoot, VPos.CENTER); // vertical center (optional)
            if(++column > 2){
                column = 0;
                row++;
            }

            FXMLLoader habitDetailsLoader = new FXMLLoader(getClass().getResource("HabitDetails.fxml"));
            Parent habitDetailsRoot = habitDetailsLoader.load();
            HabitDetailsController habitDetailsController = habitDetailsLoader.getController();
            habitCardController.setDetailsController(habitDetailsController);
            habitDetailsController.setInfo(habit);
            habitDetailsController.setGridpane(gridpane);
            habitDetailsController.setHabitCardRoot(habitCardRoot);
            habitDetailsController.setDashboardController(this);

            habitCardController.setHabitDetails(habitDetailsRoot);
        }
        //The following is to update the gridpane height and allow the scrollpane to adjust after
        //all the cards have been added
        gridpane.setMinHeight(Region.USE_PREF_SIZE);
        gridpane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridpane.setMaxHeight(Region.USE_PREF_SIZE);
    }

    public void updateHabits() throws IOException {
        if(habitDate.getValue().isAfter(LocalDate.now())){
            habitDate.setValue(LocalDate.now());
            showAlert("Can't complete future habits. Back to today!");
        }
        gridpane.getChildren().clear();
        HabitManager.loadHabits();
        int column = 0;
        int row = 0;

        List<Habit> todayHabits = HabitManager.getTodayHabits(habitDate.getValue());
        List<Habit> prioritizedHabits = todayHabits.stream().sorted(Comparator.comparingInt(Habit::getPriority).reversed()).toList();

        for(Habit habit: prioritizedHabits){
            habit.streakAndPercentage(habitDate.getValue());
            FXMLLoader habitCardLoader = new FXMLLoader(getClass().getResource("HabitCard.fxml"));
            Parent habitCardRoot = habitCardLoader.load();
            HabitCardController habitCardController = habitCardLoader.getController();
            habitCardController.setHabit(habit);

            GridPane.setHalignment(habitCardRoot, HPos.CENTER); // horizontal center
            GridPane.setValignment(habitCardRoot, VPos.CENTER); // vertical center (optional)

            //For every 3 columns(0,1,2), increase row by 1.
            gridpane.add(habitCardRoot, column, row);
            if(++column > 2){
                column = 0;
                row++;
            }

            FXMLLoader habitDetailsLoader = new FXMLLoader(getClass().getResource("HabitDetails.fxml"));
            Parent habitDetailsRoot = habitDetailsLoader.load();
            HabitDetailsController habitDetailsController = habitDetailsLoader.getController();
            habitCardController.setDetailsController(habitDetailsController);
            habitDetailsController.setInfo(habit);
            habitDetailsController.setGridpane(gridpane);
            habitDetailsController.setHabitCardRoot(habitCardRoot);
            habitDetailsController.setDashboardController(this);

            habitCardController.setHabitDetails(habitDetailsRoot);
        }

//        int column = 0;
//        int row = 0;
//
//        List<Habit> todayHabits = HabitManager.getTodayHabits();
//        List<Habit> prioritizedHabits = todayHabits.stream().sorted(Comparator.comparingInt(Habit::getPriority)).toList();
//
//        for(Habit habit: prioritizedHabits) {
//            FXMLLoader habitCardLoader = new FXMLLoader(getClass().getResource("HabitCard.fxml"));
//            Parent habitCardRoot = habitCardLoader.load();
//            HabitCardController habitCardController = habitCardLoader.getController();
//            habitCardController.setHabit(habit);
//
//            //For every 3 columns(0,1,2), increase row by 1.
//            gridpane.add(habitCardRoot, column, row);
//            if (++column > 2) {
//                column = 0;
//                row++;
//            }
//
//            FXMLLoader habitDetailsLoader = new FXMLLoader(getClass().getResource("HabitDetails.fxml"));
//            Parent habitDetailsRoot = habitDetailsLoader.load();
//            HabitDetailsController habitDetailsController = habitDetailsLoader.getController();
//            habitDetailsController.setInfo(habit);
//
//
//            habitCardController.setHabitDetails(habitDetailsRoot);
//        }
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

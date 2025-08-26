/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/23/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import model.Habit;
import model.HabitManager;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class DashboardController {
    @FXML
    private GridPane gridpane;

    public static SceneSwitcher switcher;

    public DashboardController(SceneSwitcher switcher) {
        DashboardController.switcher = switcher;
    }

    //Default controller
    public DashboardController(){}

    @FXML
    private void openSettings(){
        //TODO - Make UI
        System.out.println("Settings");
    }

    @FXML
    private void makeNewHabit() {
        switcher.switchTo("/ui/NewHabit.fxml");
    }

    @FXML
    private void viewStats(){
       //TODO - Make UI
        System.out.println("stats");
    }

    @FXML
    private void initialize() throws IOException {
        NewHabitController.setDashboardController(this);
        HabitManager.loadHabits();
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
            habitCardController.setDetailsController(habitDetailsController);
            habitDetailsController.setInfo(habit);
            habitDetailsController.setGridpane(gridpane);
            habitDetailsController.setHabitCardRoot(habitCardRoot);

            habitCardController.setHabitDetails(habitDetailsRoot);
        }
    }

    public void updateHabits() throws IOException {
        HabitManager.loadHabits();
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
            habitCardController.setDetailsController(habitDetailsController);
            habitDetailsController.setInfo(habit);
            habitDetailsController.setGridpane(gridpane);
            habitDetailsController.setHabitCardRoot(habitCardRoot);

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

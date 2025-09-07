/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/23/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Habit;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class HabitCardController {
    @FXML
    private Label habitName;
    @FXML
    private Label completionPercent;
    @FXML
    private Label currStreak;
    @FXML
    private Label longestStreak;
    @FXML
    private VBox background;

    private Habit thisHabit;
    private Parent habitDetails;
    private HabitDetailsController detailsController;
    private static DashboardController dashboardController;

    @FXML
    private void moreInfo(){
        Stage stage = new Stage();
        stage.setScene(new Scene(habitDetails));
        detailsController.setStage(stage);
        stage.setTitle("Habit Details");
        stage.show();
    }

    protected void setDetailsController(HabitDetailsController habitDetailsController){
        detailsController = habitDetailsController;
    }

    @FXML
    private void complete(){
        int completionState = thisHabit.getEntry(dashboardController.habitDate.getValue());
        if(completionState == 0) {
            background.setStyle(
                    "-fx-background-color: #58bf64;" +
                    "-fx-border-radius: 15;" +
                    "-fx-background-radius: 15;" +
                    "-fx-border-color: #333333;" +
                    "-fx-border-width: 1;"
            ); //light green

            thisHabit.complete(dashboardController.habitDate.getValue());
            setHabit(thisHabit);
        } else if(completionState == 1){
            background.setStyle(
                    "-fx-background-color: #ff575a;" +
                    "-fx-border-radius: 15;" +
                    "-fx-background-radius: 15;" +
                    "-fx-border-color: #333333;" +
                    "-fx-border-width: 1;"
            ); //red
            thisHabit.uncomplete(dashboardController.habitDate.getValue());
            setHabit(thisHabit);
        } else if(completionState == 2){
            background.setStyle(
                    "-fx-background-color: lightgrey;" +
                    "-fx-border-radius: 15;" +
                    "-fx-background-radius: 15;" +
                    "-fx-border-color: #333333;" +
                    "-fx-border-width: 1;"
            );
            thisHabit.removeEntry(dashboardController.habitDate.getValue());
            setHabit(thisHabit);
        }
    }

    protected static void setDashboardController(DashboardController dashboard){
        dashboardController = dashboard;
    }

    protected void setHabit(Habit habit){
        thisHabit = habit; //So that other methods have the habit that this card is for.
        int completionState = habit.getEntry(dashboardController.habitDate.getValue());
        if(completionState == 0) {
            background.setStyle(
                    "-fx-background-color: lightgrey;" +
                            "-fx-border-radius: 15;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-color: #333333;" +
                            "-fx-border-width: 1;"
            );
        } else if(completionState == 1){
            background.setStyle(
                    "-fx-background-color: #58bf64;" +
                            "-fx-border-radius: 15;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-color: #333333;" +
                            "-fx-border-width: 1;"
            ); //light green
        } else if(completionState == 2){
            background.setStyle(
                    "-fx-background-color: #ff575a;" +
                            "-fx-border-radius: 15;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-color: #333333;" +
                            "-fx-border-width: 1;"
            ); //red
        }

        habitName.setText(habit.getName());
        completionPercent.setText(Double.toString(habit.getCompletionPercent()));
        currStreak.setText(String.valueOf(habit.getCurrStreak()));
        longestStreak.setText(String.valueOf(habit.getLongestStreak()));
    }

    protected void setHabitDetails(Parent root){
        habitDetails = root;
    }
}

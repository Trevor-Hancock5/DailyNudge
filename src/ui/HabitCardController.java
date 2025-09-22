/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Habit;

import java.text.DecimalFormat;

/**
 * Controller for Habit Cards
 */
public class HabitCardController {
    private static DashboardController dashboardController;

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
    private Scene details;

    @FXML
    private void moreInfo(){
        if(details == null) {
            details = new Scene(habitDetails);
        }
        Stage stage = new Stage();
        stage.setScene(details);
        detailsController.setStage(stage);
        stage.setTitle("Habit Details");
        detailsController.setInfo(thisHabit);
        stage.show();
    }

    protected void setDetailsController(HabitDetailsController habitDetailsController){
        detailsController = habitDetailsController;
    }

    @FXML
    private void complete(){
        int completionState = thisHabit.getEntry(dashboardController.habitDate.getValue());
        if(completionState == 0) {
            thisHabit.complete(dashboardController.habitDate.getValue());
            setHabit(thisHabit);
        } else if(completionState == 1){
            thisHabit.uncomplete(dashboardController.habitDate.getValue());
            setHabit(thisHabit);
        } else if(completionState == 2){
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

        String borderColor;
        String partBorder = "-fx-border-color: ";
        String partBackground = "-fx-background-color: ";
        String backgroundColor;
        String pendingBackgroundColor;
        if(SceneSwitcher.getTheme().contains("light")){
            pendingBackgroundColor = partBackground + "#f9f9f9;";
            borderColor = partBorder + "#333333;";
        } else{
            pendingBackgroundColor = partBackground + "#1e1e1e;";
            borderColor = partBorder + "#cccccc;";
        }


        if(completionState == 1){
            backgroundColor = partBackground + "#58bf64;"; //Light green
        } else if(completionState == 2){
            backgroundColor = partBackground + "#ff575a;"; //red
        } else{
            backgroundColor = pendingBackgroundColor;
        }
        background.setStyle(backgroundColor +
                borderColor +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-border-width: 1;"
        );

        habitName.setText(habit.getName());
        DecimalFormat df = new DecimalFormat("#.##");
        completionPercent.setText(df.format(habit.getCompletionPercent()) + "%");
        currStreak.setText(String.valueOf(habit.getCurrStreak()));
        longestStreak.setText(String.valueOf(habit.getLongestStreak()));
    }

    protected void setHabitDetails(Parent root){
        habitDetails = root;
    }
}

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

import java.time.LocalDate;

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
        int completionState = thisHabit.getCompletionState();
        if(completionState == 0) {
            thisHabit.setCompletionState(1);
            background.setStyle("-fx-background-color: #58bf64;"); //light green
            thisHabit.complete(LocalDate.now());
            setHabit(thisHabit);
        } else if(completionState == 1){
            thisHabit.setCompletionState(2);
            background.setStyle("-fx-background-color: #ff575a;"); //red
            thisHabit.uncomplete(LocalDate.now());
            setHabit(thisHabit);
        } else if(completionState == 2){
            thisHabit.setCompletionState(0);
            background.setStyle("-fx-background-color: lightgrey");
        }
    }

    protected void setHabit(Habit habit){
        thisHabit = habit; //So that other methods have the habit that this card is for.
        int completionState = habit.getCompletionState();
        if(completionState == 0) {
            background.setStyle("-fx-background-color: lightgrey");
        } else if(completionState == 1){
            background.setStyle("-fx-background-color: #58bf64;"); //light green
        } else if(completionState == 2){
            background.setStyle("-fx-background-color: #ff575a;"); //red
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

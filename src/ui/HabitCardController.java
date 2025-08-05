/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 7/23/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Habit;

public class HabitCardController {
    @FXML
    private Label habitName;
    @FXML
    private Label completionPercent;
    @FXML
    private Label currStreak;
    @FXML
    private Label longestStreak;

    private Parent habitDetails;

    @FXML
    private void moreInfo(){
        Stage stage = new Stage();
        stage.setScene(new Scene(habitDetails));
        stage.setTitle("Habit Details");
        stage.show();
    }

    protected void setHabit(Habit habit){
        habitName.setText(habit.getName());
        completionPercent.setText(Double.toString(habit.getCompletionPercent()));
        currStreak.setText(String.valueOf(habit.getCurrStreak()));
        longestStreak.setText(String.valueOf(habit.getLongestStreak()));
    }

    protected void setHabitDetails(Parent root){
        habitDetails = root;
    }
}

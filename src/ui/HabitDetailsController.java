/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/23/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import model.Habit;
import model.HabitManager;

import java.util.ArrayList;
import java.util.Map;

public class HabitDetailsController {
    @FXML
    private Label habitName;
    @FXML
    private Label habitNote;
    @FXML
    private Label startDate;
    @FXML
    private Label priority;
    @FXML
    private Label habitLog;
    @FXML
    private Label notes;
    @FXML
    private Label frequency;
    @FXML
    private Label completionPercent;
    @FXML
    private Label currStreak;
    @FXML
    private Label longStreak;

    private Habit habit;

    protected void setInfo(Habit habit){
        this.habit = habit;
        habitName.setText(habit.getName());
        habitNote.setText(habit.getHabitNote());
        startDate.setText(habit.getStartDate());
        priority.setText(String.valueOf(habit.getPriority()));
        habitLog.setText("TODO"); //TODO
        notes.setText("TODO"); //TODO

        //format frequency
        ArrayList<Integer> freq = habit.getFrequency();
        Map<Integer, String> frequencyConversion = Map.ofEntries(
                Map.entry(1, "Monday"),
                Map.entry(2, "Tuesday"),
                Map.entry(3, "Wednesday"),
                Map.entry(4, "Thursday"),
                Map.entry(5, "Friday"),
                Map.entry(6, "Saturday"),
                Map.entry(7, "Sunday")
        );
        String formattedFreq = "";
        for(Integer i : freq){
            formattedFreq += frequencyConversion.get(i) + ", ";
        }
        if(formattedFreq.length() > 2){
            formattedFreq = formattedFreq.substring(0, formattedFreq.length() - 2);
        }

        frequency.setText(formattedFreq);
        completionPercent.setText(habit.getCompletionPercent() + "%");
        currStreak.setText(habit.getCurrStreak() + " Days");
        longStreak.setText(habit.getLongestStreak() + " Days");

    }

    @FXML
    private void deleteHabit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        HabitManager.removeHabit(habit);
        //This is so that the data file has the correct information saved to load.
        HabitManager.saveHabits();

        //TODO: Need to update the dashboard.
    }
}

/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 7/25/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Habit;

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

//    private SceneSwitcher switcher;
//
//    public HabitDetailsController(SceneSwitcher switcher){
//        this.switcher = switcher;
//    }

    protected void setInfo(Habit habit){
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
}

/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/23/2025
 */
package ui;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Habit;
import model.HabitManager;

import java.io.IOException;
import java.util.*;

public class HabitDetailsController {
    @FXML
    private TextField habitName;
    @FXML
    private TextField habitNote;
    @FXML
    private TextField startDate;
    @FXML
    private TextField priority;
    @FXML
    private TextField habitLog;
    @FXML
    private TextField notes;
    @FXML
    private TextField frequency;
    @FXML
    private TextField completionPercent;
    @FXML
    private TextField currStreak;
    @FXML
    private TextField longStreak;
    @FXML
    private Button editingButton;

    private Habit habit;
    private Stage window;
    List<TextField> textFields;
    private DashboardController dashboardController;
    private static final String NOT_EDITING = "Allow Editing";
    private static final String EDITING = "Update Habit?";

    protected void setStage(Stage stage){
        window = stage;
    }

    protected void setDashboardController(DashboardController dashboardController){
        this.dashboardController = dashboardController;
    }

    @FXML
    private void initialize(){
        textFields = new ArrayList<>(Arrays.asList(habitName, habitNote, startDate, priority, habitLog, notes,
                completionPercent, currStreak, longStreak));
        for(TextField tf : textFields){
            Text text = new Text();
            text.textProperty().bind(tf.textProperty());
            text.setFont(tf.getFont()); // ensure same font for accurate measurement

            tf.prefWidthProperty().bind(Bindings.createDoubleBinding(
                    () -> text.getLayoutBounds().getWidth() + 20, // +20 for some padding
                    text.textProperty()
            ));
            tf.setEditable(false);
        }
    }

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
    private void deleteHabit() throws IOException {
        //alert to ensure user wants to delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Confirmation");
        alert.setHeaderText("Are you sure you'd like to delete " + habitName.getText() + "?");
        alert.setContentText("The action cannot be undone!");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            HabitManager.removeHabit(habit);
            //This is so that the data file has the correct information saved to load.
            HabitManager.saveHabits();
            SceneView.DASHBOARD.switchTo(DashboardController.switcher);
            window.close();
            dashboardController.updateHabits(dashboardController.allHabits.isSelected());
        } else{
            alert.close();
        }
    }

    @FXML
    private void allowEditing(){
        if(editingButton.getText().equals(NOT_EDITING)){
            //The user wants to allow editing
            for(TextField tf : textFields){
                tf.setEditable(true);
            }
            editingButton.setText(EDITING);
        } else{
            //The user wants to stop editing and update the habit
            for(TextField tf : textFields){
                tf.setEditable(false);
            }
            editingButton.setText(NOT_EDITING);
            //TODO: Update habit values by getting the text from each box and assigning.
        }
    }
}



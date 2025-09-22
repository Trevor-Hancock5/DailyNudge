/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package ui;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Habit;
import model.HabitManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller to control the Habit Details screen
 */
public class HabitDetailsController {
    private static final String NOT_EDITING = "Allow Editing";
    private static final String EDITING = "Update Habit?";

    @FXML
    private TextField habitName;
    @FXML
    private TextField habitNote;
    @FXML
    private DatePicker startDate;
    @FXML
    private TextField priority;
    @FXML
    private TextArea habitLog;
    @FXML
    private TextField completionPercent;
    @FXML
    private TextField currStreak;
    @FXML
    private TextField longStreak;
    @FXML
    private Button editingButton;
    @FXML
    private ToggleButton mon;
    @FXML
    private ToggleButton tues;
    @FXML
    private ToggleButton wed;
    @FXML
    private ToggleButton thurs;
    @FXML
    private ToggleButton fri;
    @FXML
    private ToggleButton sat;
    @FXML
    private ToggleButton sun;

    private Habit habit;
    private Stage window;
    private List<TextField> textFields;
    private ArrayList<ToggleButton> freqButtons;
    private DashboardController dashboardController;

    protected void setStage(Stage stage){
        window = stage;
    }

    protected void setDashboardController(DashboardController dashboardController){
        this.dashboardController = dashboardController;
    }

    @FXML
    private void initialize(){
        freqButtons = new ArrayList<>(Arrays.asList(mon, tues, wed, thurs, fri, sat, sun));
        disableFreq();
        //Only included textFields I wanted to allow editing to
        textFields = new ArrayList<>(Arrays.asList(habitName, habitNote, priority));
        for(TextField tf : textFields){
            Text text = new Text();
            text.textProperty().bind(tf.textProperty());
            text.setFont(tf.getFont()); // ensure same font for accurate measurement

            final int extraWidth = 20;
            tf.prefWidthProperty().bind(Bindings.createDoubleBinding(
                    () -> text.getLayoutBounds().getWidth() + extraWidth, // +20 for some padding
                    text.textProperty()
            ));
            tf.setEditable(false);
        }

        // So it doesn't automatically get highlighted on load up
        habitName.setFocusTraversable(false);
    }

    private void disableFreq(){
        for(ToggleButton tb : freqButtons){
            tb.setDisable(true);
            tb.setStyle("-fx-opacity: .8;");
        }
    }

    protected void setInfo(Habit habit){
        this.habit = habit;
        habitName.setText(habit.getName());
        habitNote.setText(habit.getHabitNote());
        startDate.setValue(habit.getStartDate());
        priority.setText(String.valueOf(habit.getPriority()));
        habitLog.setText(habit.getHabitLog());

        //format frequency
        ArrayList<Integer> freq = habit.getFrequency();
        final int weekdays = 7;
        Map<Integer, ToggleButton> frequencyConversion = Map.ofEntries(
                Map.entry(1, mon),
                Map.entry(2, tues),
                Map.entry(3, wed),
                Map.entry(4, thurs),
                Map.entry(5, fri),
                Map.entry(6, sat),
                Map.entry(7, sun)
        );
        for(int i = 1; i < weekdays + 1; i++){
            (frequencyConversion.get(i)).setSelected(false); // initialize all to be unselected
        }
        for(Integer i : freq){
            (frequencyConversion.get(i)).setSelected(true);
        }

        completionPercent.setText(habit.getCompletionPercent() + "%");
        currStreak.setText(habit.getCurrStreak() + " Days");
        longStreak.setText(habit.getLongestStreak() + " Days");

        setButtonShape();
    }

    private void setButtonShape(){
        for(ToggleButton btn : freqButtons){
            btn.setStyle("-fx-background-radius: 50%;" +
                    "-fx-border-radius: 50%;" +
                    "-fx-min-height: 50;" +
                    "-fx-max-height: 50;" +
                    "-fx-min-width: 50;" +
                    "-fx-max-width: 50;");
        }
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
            for(ToggleButton tb : freqButtons){
                tb.setDisable(false);
                tb.setStyle("-fx-opacity: 1;");
            }
            startDate.setEditable(true);
            editingButton.setText(EDITING);
        } else{
            //The user wants to stop editing and update the habit
            for(TextField tf : textFields){
                tf.setEditable(false);
            }
            disableFreq();
            startDate.setEditable(false);

            editingButton.setText(NOT_EDITING);
            habit.setHabitNote(habitNote.getText());
            habit.setName(habitName.getText());
            habit.setStartDate(startDate.getValue());
            try{
                int newPriority = Integer.parseInt(priority.getText());
                if(newPriority < 0){
                    throw new NumberFormatException();
                }
                habit.setPriority(newPriority);
            } catch (NumberFormatException _) {
                DashboardController.showAlert("Invalid number!");
            }
            ArrayList<Integer> frequency = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                if (freqButtons.get(i - 1).isSelected()) {
                    frequency.add(i);
                }
            }
            habit.setFrequency(frequency);

            habit.streakAndPercentage(LocalDate.now());
            setInfo(habit);
        }
        setButtonShape();
    }
}



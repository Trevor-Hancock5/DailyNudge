/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import model.Habit;
import model.HabitManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controller for New Habit
 */
public class NewHabitController {
    private static DashboardController dashboardController;

    @FXML
    private DatePicker startDate;
    @FXML
    private TextField habitName;
    @FXML
    private TextField priority;
    @FXML
    private TextField habitNote;
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
    @FXML
    private Button newHabit;
    @FXML
    private VBox root;
    @FXML
    private Label freqLabel;

    private ToggleButton[] dayButtons;

    public static void setDashboardController(DashboardController controller){
        dashboardController = controller;
    }

    /**
     * To initialize the NewHabit screen
     */
    @FXML
    public void initialize() throws IOException {
        // Run after scene is displayed so focus actually takes effect
        Platform.runLater(() -> habitName.requestFocus());
        habitName.setOnAction(_ -> {
            priority.requestFocus();
            DashboardController.SOUND.playSound("textfield");
        });
        priority.setOnAction(_ -> {
            habitNote.requestFocus();
            DashboardController.SOUND.playSound("textfield");
        });
        habitNote.setOnAction(_ -> {
            mon.requestFocus();
            DashboardController.SOUND.playSound("textfield");
        });

        freqLabel.setStyle("-fx-font-size: 20;" +
                "-fx-font-weight: bold");

        dayButtons = new ToggleButton[] {mon, tues, wed, thurs, fri, sat, sun};

        // Set navigation for each toggle button
        for (int i = 0; i < dayButtons.length; i++) {
            final int idx = i;
            dayButtons[i].setOnKeyTyped(event -> {
                switch (event.getCode()) {
                    case ENTER, SPACE -> {
                        dayButtons[idx].setSelected(!dayButtons[idx].isSelected());
                        event.consume();
                    }
                }
            });
        }

        for (int i = 0; i < dayButtons.length; i++) {
            final int idx = i;
            dayButtons[i].setOnKeyReleased(event -> {
                switch (event.getCode()) {
                    case RIGHT -> {
                        if (idx + 1 < dayButtons.length) {
                            dayButtons[idx + 1].requestFocus();
                        } else {
                            // Last button → focus startDayPicker
                            startDate.requestFocus();
                        }
                        event.consume();
                    }
                    case LEFT -> {
                        if (idx - 1 >= 0){
                            dayButtons[idx - 1].requestFocus();
                        }
                        event.consume();
                    }
                }
            });
        }

        startDate.setOnAction(_ -> {
            DashboardController.SOUND.playSound("button");
            newHabit.requestFocus();
        });

        // Automatically show calendar when DatePicker gains focus
        startDate.focusedProperty().addListener((_, _, newVal) -> {
            if (newVal) { // focus gained
                startDate.show();
            }
        });

        newHabit.setOnAction(_ -> {
            try {
                makeNewHabit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        root.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                try {
                    returnToDashboard();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        startDate.setValue(dashboardController.habitDate.getValue());

        for(ToggleButton tb : dayButtons){
            tb.setOnAction(_ -> DashboardController.SOUND.playSound("toggle"));
        }

        //Initialize chosen days to be Mon through Fri
        final int weekdays = 5;
        for(int i = 0; i < weekdays; i++){
            dayButtons[i].setSelected(true);
        }
    }

    @FXML
    private void returnToDashboard() throws IOException {
        DashboardController.SOUND.playSound("button");
        SceneView.DASHBOARD.switchTo(DashboardController.getSwitcher());
    }

    @FXML
    private void makeNewHabit() throws IOException {
        String habitName = this.habitName.getText();
        if(habitName.isBlank()){
            DashboardController.showAlert("Name cannot be blank!");
        } else if(HabitManager.containsHabitName(habitName)){
            DashboardController.showAlert("Habit with same name already exists. Rename!");
        } else {
            int priority;
            try{
                priority = Integer.parseInt(this.priority.getText());
                if(priority < 0){
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e){
                DashboardController.showAlert("Invalid value entered. Priority set to 1.");
                priority = 1;
            }

            String habitNote = this.habitNote.getText();
            ArrayList<Integer> frequency = new ArrayList<>();
            final int weekdays = 7;
            for (int i = 1; i < weekdays + 1; i++) {
                if (dayButtons[i - 1].isSelected()) {
                    frequency.add(i);
                }
            }
            LocalDate startDate = this.startDate.getValue();

            HabitManager.addHabit(new Habit(habitName, priority, habitNote, frequency, startDate,
                    SettingsController.getSettingPreferences()));
            HabitManager.saveHabits();
            returnToDashboard();
            dashboardController.updateHabits();
            resetUI();
            
            DashboardController.SOUND.playSound("newhabit");
        }
    }

    private void resetUI(){
        startDate.setValue(LocalDate.now());
        habitName.clear();
        habitNote.clear();
        priority.clear();
        //Set frequency to be weekdays initially
        mon.setSelected(true);
        tues.setSelected(true);
        wed.setSelected(true);
        thurs.setSelected(true);
        fri.setSelected(true);
        sat.setSelected(false);
        sun.setSelected(false);
    }
}
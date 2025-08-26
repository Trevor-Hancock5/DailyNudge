/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/23/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import model.Habit;
import model.HabitManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class NewHabitController {
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

    private ArrayList<ToggleButton> daysOfWeek;
    private static DashboardController dashboardController;

    public static void setDashboardController(DashboardController controller){
        dashboardController = controller;
    }

    @FXML
    public void initialize() {
        startDate.setValue(LocalDate.now());
        daysOfWeek = new ArrayList<>();

        daysOfWeek.add(mon);
        daysOfWeek.add(tues);
        daysOfWeek.add(wed);
        daysOfWeek.add(thurs);
        daysOfWeek.add(fri);
        daysOfWeek.add(sat);
        daysOfWeek.add(sun);

        //Initialize chosen days to be Mon through Fri
        for(int i = 0; i < 5; i++){
            daysOfWeek.get(i).setSelected(true);
        }
    }

    @FXML
    private void returnToDashboard(){
        DashboardController.switcher.switchTo("/ui/Dashboard.fxml");
    }

    @FXML
    private void makeNewHabit() throws IOException {
        String habitName = this.habitName.getText();
        if(habitName.isBlank()){
            DashboardController.showAlert("Name cannot be blank!");
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
            for (int i = 1; i < 8; i++) {
                if (daysOfWeek.get(i - 1).isSelected()) {
                    frequency.add(i);
                }
            }
            LocalDate startDate = this.startDate.getValue();

            HabitManager.addHabit(new Habit(habitName, priority, habitNote, frequency, startDate));
            HabitManager.saveHabits();
            DashboardController.switcher.switchTo("/ui/Dashboard.fxml");
            dashboardController.updateHabits();
            resetUI();
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
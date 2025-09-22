/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Habit;
import model.HabitManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Controller for the main screen
 */
public class DashboardController {
    private static SceneSwitcher switcher;

    @FXML
    protected DatePicker habitDate;
    @FXML
    protected ToggleButton allHabits;
    @FXML
    protected Label title;
    @FXML
    private GridPane gridpane;
    @FXML
    private VBox vBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox habitsVBox;

    private final int gridGap = 50;

    /**
     * Constructor to make the Dashboard controller
     * @param switcher Initialize SceneSwitcher instance
     */
    public DashboardController(SceneSwitcher switcher) {
        DashboardController.switcher = switcher;
    }

    public static SceneSwitcher getSwitcher(){
        return switcher;
    }

    @FXML
    private void showAllHabits() throws IOException {
        updateHabits(allHabits.isSelected());
        if(allHabits.isSelected()){
            allHabits.setText("Today's\nHabits");
        } else{
            allHabits.setText("All Habits");
        }
    }

    @FXML
    private void openSettings() throws IOException {
        SceneView.SETTINGS.switchTo(switcher);
    }

    @FXML
    private void makeNewHabit() throws IOException {
        SceneView.NEW_HABIT.switchTo(switcher);
    }

    @FXML
    private void newDate() throws IOException {
        updateHabits(allHabits.isSelected());
    }

    @FXML
    private void initialize() throws IOException {
        scrollPane.setContent(habitsVBox);
        scrollPane.setFitToWidth(true); // makes VBox width match ScrollPane width
        scrollPane.setFitToHeight(true);
        fixBackgroundColor();

        NewHabitController.setDashboardController(this);
        HabitManager.loadHabits();
        habitDate.setValue(LocalDate.now());
        vBox.setOnKeyPressed(event -> {
            if(event.isControlDown() && event.getCode() == KeyCode.D){
                habitDate.requestFocus();
                habitDate.show();
            }
        });

        gridpane.setHgap(15);
        gridpane.setVgap(25);
        gridpane.setPadding(new Insets(20));
        int column = 0;
        int row = 0;

        List<Habit> todayHabits = HabitManager.getTodayHabits(habitDate.getValue());
        List<Habit> prioritizedHabits = todayHabits.stream().sorted(Comparator
                .comparingInt(Habit::getPriority).reversed()).toList();

        //Get a habit and load the settings first.
        if(!prioritizedHabits.isEmpty()) {
            prioritizedHabits.getFirst().loadSettings();
        }
        if(SettingsController.getStreakPreference() != null) {
            //True means normal streak. False means flexible streak
            Habit.setStreakRule(!SettingsController.getStreakPreference().equals("flex"));
        } else{
            Habit.setStreakRule(false); //flexible streak
        }

        if(SettingsController.getUserName() != null) {
            title.setText(SettingsController.getUserName() + "'s DailyNudge");
        }

        HabitManager.saveHabits();

        for(Habit habit: prioritizedHabits){
            habit.streakAndPercentage(habitDate.getValue());
            FXMLLoader habitCardLoader = new FXMLLoader(getClass().getResource("HabitCard.fxml"));
            Parent habitCardRoot = habitCardLoader.load();
            HabitCardController habitCardController = habitCardLoader.getController();
            habitCardController.setHabit(habit);
            habitCardRoot.getStylesheets().clear();
            habitCardRoot.getStylesheets().add(getClass().getResource(SceneSwitcher.getTheme())
                    .toExternalForm());

            //For every 3 columns(0,1,2), increase row by 1.
            gridpane.add(habitCardRoot, column, row);
            //To center each card in its cell
            GridPane.setHalignment(habitCardRoot, HPos.CENTER); // horizontal center
            GridPane.setValignment(habitCardRoot, VPos.CENTER); // vertical center (optional)
            if(++column > 2){
                column = 0;
                row++;
            }

            FXMLLoader habitDetailsLoader = new FXMLLoader(getClass()
                    .getResource("HabitDetails.fxml"));
            Parent habitDetailsRoot = habitDetailsLoader.load();
            HabitDetailsController habitDetailsController = habitDetailsLoader.getController();
            habitCardController.setDetailsController(habitDetailsController);
            habitDetailsController.setInfo(habit);
            habitDetailsController.setDashboardController(this);

            habitDetailsRoot.getStylesheets().clear();
            habitDetailsRoot.getStylesheets().add(getClass()
                    .getResource(SceneSwitcher.getTheme()).toExternalForm());

            habitCardController.setHabitDetails(habitDetailsRoot);
        }
        switcher.updateTheme();
        fixBackgroundColor();

        //The following is to update the gridpane height and allow the scrollpane to adjust after
        //all the cards have been added
        gridpane.setMinHeight(Region.USE_PREF_SIZE);
        gridpane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridpane.setMaxHeight(Region.USE_PREF_SIZE);
        gridpane.setVgap(gridGap);
    }

    private void fixBackgroundColor(){
        if(SceneSwitcher.getTheme().contains("dark")){
            habitsVBox.setStyle("-fx-background-color: #1e1e1e");
        } else{
            habitsVBox.setStyle("-fx-background-color: #f9f9f9");
        }
    }

    /**
     * Method to update the habits
     * @param allHabits If true, then show all habits. Don't otherwise
     * @throws IOException Throws if file errors occur
     */
    public void updateHabits(boolean allHabits) throws IOException {
        if(habitDate.getValue().isAfter(LocalDate.now())){
            habitDate.setValue(LocalDate.now());
            showAlert("Can't complete future habits. Back to today!");
        }
        gridpane.getChildren().clear();
        fixBackgroundColor();
        HabitManager.loadHabits();
        int column = 0;
        int row = 0;

        List<Habit> todayHabits;
        if(!allHabits) {
            todayHabits = HabitManager.getTodayHabits(habitDate.getValue());
        } else{
            todayHabits = HabitManager.getHabits();
        }
        List<Habit> prioritizedHabits = todayHabits.stream().sorted(Comparator
                .comparingInt(Habit::getPriority).reversed()).toList();

        fixBackgroundColor();

        for(Habit habit: prioritizedHabits){
            habit.setSettingPreferences(SettingsController.getSettingPreferences());

            habit.streakAndPercentage(habitDate.getValue());
            FXMLLoader habitCardLoader = new FXMLLoader(getClass().getResource("HabitCard.fxml"));
            Parent habitCardRoot = habitCardLoader.load();
            HabitCardController habitCardController = habitCardLoader.getController();
            habitCardController.setHabit(habit);
            habitCardRoot.getStylesheets().clear();
            habitCardRoot.getStylesheets().add(getClass()
                    .getResource(SceneSwitcher.getTheme()).toExternalForm());

            GridPane.setHalignment(habitCardRoot, HPos.CENTER); // horizontal center
            GridPane.setValignment(habitCardRoot, VPos.CENTER); // vertical center (optional)
            gridpane.setVgap(gridGap);

            //For every 3 columns(0,1,2), increase row by 1.
            gridpane.add(habitCardRoot, column, row);
            if(++column > 2){
                column = 0;
                row++;
            }

            FXMLLoader habitDetailsLoader = new FXMLLoader(getClass()
                    .getResource("HabitDetails.fxml"));
            Parent habitDetailsRoot = habitDetailsLoader.load();
            HabitDetailsController habitDetailsController = habitDetailsLoader.getController();
            habitCardController.setDetailsController(habitDetailsController);
            habitDetailsController.setInfo(habit);
            habitDetailsController.setDashboardController(this);

            habitDetailsRoot.getStylesheets().clear();
            habitDetailsRoot.getStylesheets().add(getClass()
                    .getResource(SceneSwitcher.getTheme()).toExternalForm());

            habitCardController.setHabitDetails(habitDetailsRoot);
        }
    }

    /**
     * Method to make an alert so any controller can pop an alert up
     * @param text The text to be shown in Header text
     */
    public static void showAlert(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}

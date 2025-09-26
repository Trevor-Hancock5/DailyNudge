/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package ui;

import app.SoundManager;
import app.StorageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Habit;
import model.HabitManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    public static final SoundManager SOUND = new SoundManager();
    private static boolean soundEffects = true;
    private static boolean backgroundMusic = true;

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

    public static void setSoundEffects(boolean on){
        soundEffects = on;
    }

    public static boolean getSoundEffects(){
        return soundEffects;
    }

    public static void setBackgroundMusic(boolean on){
        backgroundMusic = on;
    }

    public static boolean getBackgroundMusic(){
        return backgroundMusic;
    }

    @FXML
    private void showAllHabits() {
        SOUND.playSound("toggle");

        updateHabits();
        if(allHabits.isSelected()){
            allHabits.setText("Today's\nHabits");
        } else{
            allHabits.setText("All Habits");
        }
    }

    @FXML
    private void openSettings() throws IOException {
        SOUND.playSound("button");
        SceneView.SETTINGS.switchTo(switcher);
    }

    @FXML
    private void makeNewHabit() throws IOException {
        SOUND.playSound("button");
        SceneView.NEW_HABIT.switchTo(switcher);
    }

    @FXML
    private void newDate() {
        SOUND.playSound("button");
        updateHabits();
    }

    @FXML
    private void initialize() {
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

        if(backgroundMusic) {
            SOUND.playBackgroundMusic();
        }
        title.setStyle("-fx-font-size: 35;" +
                "-fx-font-weight: bold");

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
            try{
                FXMLLoader habitCardLoader = new FXMLLoader(getClass()
                        .getResource("HabitCard.fxml"));
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
                if (++column > 2) {
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
            } catch (IOException _){
                showAlert("Error loading habit cards or habit details!");
            }
        }
        switcher.updateTheme();
        fixBackgroundColor();

        //The following is to update the gridpane height and allow the scrollpane to adjust after
        //all the cards have been added
        gridpane.setMinHeight(Region.USE_PREF_SIZE);
        gridpane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridpane.setMaxHeight(Region.USE_PREF_SIZE);
        gridpane.setVgap(gridGap);

        checkTitle();
    }

    private void checkTitle(){
        if(title.getText().equals("'s DailyNudge") || title.getText().equals("null's DailyNudge")){
            title.setText("DailyNudge");
        }
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
     */
    public void updateHabits() {
        if(habitDate.getValue().isAfter(LocalDate.now())){
            habitDate.setValue(LocalDate.now());
            showAlert("Can't complete future habits. Back to today!");
        }
        boolean allHabits = this.allHabits.isSelected();
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
            habit.streakAndPercentage(habitDate.getValue());
            try {
                FXMLLoader habitCardLoader = new FXMLLoader(getClass()
                        .getResource("HabitCard.fxml"));
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
                if (++column > 2) {
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
            } catch (IOException _){
                showAlert("Error updating habit card or habit details");
            }
        }

        List<Habit> habits = HabitManager.getHabits();
        for(Habit habit : habits){
            habit.setSettingPreferences(SettingsController.getSettingPreferences());
        }
        if(!habits.isEmpty()) {
            habits.getFirst().loadSettings();
        }
        checkTitle();
    }

    /**
     * Method to make an alert so any controller can pop an alert up
     * @param text The text to be shown in Header text
     */
    public static void showAlert(String text){
        SOUND.playSound("error");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(text);

        Hyperlink link = new Hyperlink("Click here to report this error!");
        link.setOnAction(_-> {
            try {
                Desktop.getDesktop().browse(new URI("https://forms.gle/UVMA5KcSmc4Xyiqm7"));
            } catch (URISyntaxException | IOException _) {
                System.err.println("Error loading report form.");
            }
        });
        link.setWrapText(true);

        alert.getDialogPane().setContent(link);
        alert.showAndWait();
    }
}

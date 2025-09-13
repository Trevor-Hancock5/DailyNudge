/*
 * Course: CSC1110A-111
 * Fall 2024
 * Assignment
 * Name: Trevor Hancock
 * Last Updated: 9/13/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import model.Habit;
import model.HabitManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static ui.DashboardController.switcher;

public class AllHabitsController {
    @FXML
    private GridPane gridpane;

    private static DashboardController dashboardController;

    @FXML
    private void initialize() throws IOException {
        dashboardController.setAllHabitsController(this);
        System.out.println("Wooooh");
        gridpane.setHgap(25);
        gridpane.setVgap(25);

        int column = 0;
        int row = 0;

        List<Habit> todayHabits = HabitManager.getHabits();
        List<Habit> prioritizedHabits = todayHabits.stream().sorted(Comparator.comparingInt(Habit::getPriority).reversed()).toList();

        for(Habit habit: prioritizedHabits){
            habit.streakAndPercentage(LocalDate.now());
            FXMLLoader habitCardLoader = new FXMLLoader(getClass().getResource("HabitCard.fxml"));
            Parent habitCardRoot = habitCardLoader.load();
            gridpane.setMargin(habitCardRoot, new Insets(15, 15, 15, 15));
            habitCardRoot.setStyle(
                    "-fx-background-color: lightgrey;" +   // background
                            "-fx-border-radius: 15;" +           // rounded corners
                            "-fx-background-radius: 15;" +       // match background to border radius
                            "-fx-border-color: #cccccc;" +       // border color
                            "-fx-border-width: 1;"
            );
            HabitCardController habitCardController = habitCardLoader.getController();
            habitCardController.setHabit(habit);

            //For every 4 columns(0,1,2), increase row by 1.

            gridpane.add(habitCardRoot, column, row);
            //To center each card in it's cell
            GridPane.setHalignment(habitCardRoot, HPos.CENTER); // horizontal center
            GridPane.setValignment(habitCardRoot, VPos.CENTER); // vertical center (optional)
            if(++column > 3){
                column = 0;
                row++;
            }

            FXMLLoader habitDetailsLoader = new FXMLLoader(getClass().getResource("HabitDetails.fxml"));
            Parent habitDetailsRoot = habitDetailsLoader.load();
            HabitDetailsController habitDetailsController = habitDetailsLoader.getController();
            habitCardController.setDetailsController(habitDetailsController);
            habitDetailsController.setInfo(habit);
            habitDetailsController.setDashboardController(dashboardController);
            habitCardController.setHabitDetails(habitDetailsRoot);
        }
        //The following is to update the gridpane height and allow the scrollpane to adjust after
        //all the cards have been added
        gridpane.setMinHeight(Region.USE_PREF_SIZE);
        gridpane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridpane.setMaxHeight(Region.USE_PREF_SIZE);
    }

    protected void updateHabits() throws IOException {
        gridpane.setHgap(25);
        gridpane.setVgap(25);

        int column = 0;
        int row = 0;

        List<Habit> todayHabits = HabitManager.getHabits();
        List<Habit> prioritizedHabits = todayHabits.stream().sorted(Comparator.comparingInt(Habit::getPriority).reversed()).toList();

        gridpane.getChildren().clear();
        for(Habit habit: prioritizedHabits){
            habit.streakAndPercentage(LocalDate.now());
            FXMLLoader habitCardLoader = new FXMLLoader(getClass().getResource("HabitCard.fxml"));
            Parent habitCardRoot = habitCardLoader.load();
            gridpane.setMargin(habitCardRoot, new Insets(15, 15, 15, 15));
            habitCardRoot.setStyle(
                    "-fx-background-color: lightgrey;" +   // background
                            "-fx-border-radius: 15;" +           // rounded corners
                            "-fx-background-radius: 15;" +       // match background to border radius
                            "-fx-border-color: #cccccc;" +       // border color
                            "-fx-border-width: 1;"
            );
            HabitCardController habitCardController = habitCardLoader.getController();
            habitCardController.setHabit(habit);

            //For every 4 columns(0,1,2), increase row by 1.

            gridpane.add(habitCardRoot, column, row);
            //To center each card in it's cell
            GridPane.setHalignment(habitCardRoot, HPos.CENTER); // horizontal center
            GridPane.setValignment(habitCardRoot, VPos.CENTER); // vertical center (optional)
            if(++column > 3){
                column = 0;
                row++;
            }

            FXMLLoader habitDetailsLoader = new FXMLLoader(getClass().getResource("HabitDetails.fxml"));
            Parent habitDetailsRoot = habitDetailsLoader.load();
            HabitDetailsController habitDetailsController = habitDetailsLoader.getController();
            habitCardController.setDetailsController(habitDetailsController);
            habitDetailsController.setInfo(habit);
            habitDetailsController.setDashboardController(dashboardController);
            habitCardController.setHabitDetails(habitDetailsRoot);
        }
        //The following is to update the gridpane height and allow the scrollpane to adjust after
        //all the cards have been added
        gridpane.setMinHeight(Region.USE_PREF_SIZE);
        gridpane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridpane.setMaxHeight(Region.USE_PREF_SIZE);
    }

    public static void setDashboardController(DashboardController dashboardController){
        AllHabitsController.dashboardController = dashboardController;
    }

    @FXML
    private void returnToDash(){
        SceneView.DASHBOARD.switchTo(switcher);
    }
}

/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import app.StorageManager;

/**
 * HabitManager class to manage habits
 */
public class HabitManager {
    private static List<Habit> habits = new ArrayList<>();

    /**
     * Load habits up for user
     */
    public static void loadHabits() {
        List<Habit> loaded = StorageManager.loadHabits();
        if (loaded != null) {
            habits = loaded;
        }
    }

    /**
     * Method to get the habits for today
     * @param date The date the user wants to load habits for
     * @return List of habits containing today's habits
     */
    public static List<Habit> getTodayHabits(LocalDate date){
        List<Habit> todayHabits = new ArrayList<>();
        for(Habit habit : habits){
            if(habit.activeToday(date)){
                todayHabits.add(habit);
            }
        }
        return todayHabits;
    }

    /**
     * Method to save the habits
     * @return True if saved
     */
    public static boolean saveHabits() {
        return StorageManager.saveHabits(habits);
    }

    /**
     * Method to save habits with given list
     *
     * @param habits Specific list to save
     */
    public static void saveHabits(List<Habit> habits){
        StorageManager.saveHabits(habits);
    }

    /**
     * Method to add a new habit
     * @param habit The Habit to add
     */
    public static void addHabit(Habit habit) {
        habits.add(habit);
    }

    /**
     * Method to remove the habit
     * @param habit Habit to be removed
     */
    public static void removeHabit(Habit habit) {
        habits.remove(habit);
    }

    public static List<Habit> getHabits() {
        return habits;
    }

    /**
     * Method to check if habit name already exists
     * @param habitName Name to check
     * @return True if name exists, false otherwise
     */
    public static boolean containsHabitName(String habitName){
        for(Habit habit : habits){
            if(habit.getName().strip().equalsIgnoreCase(habitName.strip())){
                return true;
            }
        }
        return false;
    }
}

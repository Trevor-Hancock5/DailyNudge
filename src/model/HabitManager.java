/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/23/2025
 */
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import app.StorageManager;

public class HabitManager {
    private static List<Habit> habits = new ArrayList<>();

    public static void loadHabits() {
        List<Habit> loaded = StorageManager.loadHabits();
        if (loaded != null) {
            habits = loaded;
        }
    }

    public static List<Habit> getTodayHabits(LocalDate date){
        List<Habit> todayHabits = new ArrayList<>();
        for(Habit habit : habits){
            if(habit.activeToday(date)){
                todayHabits.add(habit);
            }
        }
        return todayHabits;
    }

    public static boolean saveHabits() {
        return StorageManager.saveHabits(habits);
    }

    public static boolean saveHabits(List<Habit> habits){
        return StorageManager.saveHabits(habits);
    }

    public static void addHabit(Habit habit) {
        habits.add(habit);
    }

    public static void removeHabit(Habit habit) {
        habits.remove(habit);
    }

    public static List<Habit> getHabits() {
        return habits;
    }

    public static boolean containsHabitName(String habitName){
        for(Habit habit : habits){
            if(habit.getName().strip().equalsIgnoreCase(habitName.strip())){
                return true;
            }
        }
        return false;
    }
}

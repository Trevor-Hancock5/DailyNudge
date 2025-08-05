/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 7/23/2025
 */
package model;

import java.util.ArrayList;
import java.util.List;
import app.StorageManager;

public class HabitManager {
    private static List<Habit> habits = new ArrayList<>();

    public static List<Habit> loadHabits() {
        List<Habit> loaded = StorageManager.loadHabits();
        if (loaded != null) {
            habits = loaded;
        }
        return habits;
    }

    public static List<Habit> getTodayHabits(){
        List<Habit> todayHabits = new ArrayList<>();
        for(Habit habit : habits){
            if(habit.activeToday()){
                todayHabits.add(habit);
            }
        }
        return todayHabits;
    }

    public static boolean saveHabits() {
        return StorageManager.saveHabits(habits);
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    public void removeHabit(Habit habit) {
        habits.remove(habit);
    }

    public List<Habit> getHabits() {
        return habits;
    }
}

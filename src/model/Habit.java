/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 8/23/2025
 */
package model;

import javafx.scene.control.ToggleButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


public class Habit {
    private HashMap<LocalDate, String> notes;
    private String habitNote;
    private String name;
    private double completionPercent;
    private int priority;
    private HashMap<String, Integer> habitLog;
    private ArrayList<Integer> frequency;
    private LocalDate startDate;
    private int currentStreak;
    private int longestStreak;
    private static boolean normalStreak = false;

    public Habit(String name, int priority, String habitNote, ArrayList<Integer> freq, LocalDate startDate){
        notes = new HashMap<>();
        this.habitNote = habitNote;
        this.name = name;
        completionPercent = 0;
        this.priority = priority;
        habitLog = new HashMap<>();
        setFrequency(freq);
        this.startDate = startDate;
        currentStreak = -1;
        longestStreak = -1;
    }

    public String getName(){
        return name;
    }

    public static void setStreakRule(boolean streakRule){
        normalStreak = streakRule;
    }

    public String getHabitNote(){
        return habitNote;
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public ArrayList<Integer> getFrequency(){
        return frequency;
    }

    public int getPriority(){
        return priority;
    }

    public double getCompletionPercent(){
        return completionPercent;
    }

    public int getCurrStreak(){
        return currentStreak;
    }

    public int getLongestStreak(){
        return longestStreak;
    }

    public boolean activeToday(LocalDate date){
        //Returns true if the habit is active today, false otherwise
        return frequency.contains(date.getDayOfWeek().getValue()) && !date.isBefore(this.startDate);
    }

    public void setStartDate(LocalDate date){
        startDate = date;
    }

    public int getEntry(LocalDate date){
        String key = date.format(DateTimeFormatter.ofPattern("M/d/yyyy"));
        //Unmarked Habit = 0
        return habitLog.getOrDefault(key, 0);
    }

    public void complete(LocalDate date){
        //If the habit is active today, return true, false otherwise. (True if date in frequency. False if not.)
        if(frequency.contains(date.getDayOfWeek().getValue())) {
            this.habitLog.put(date.format(DateTimeFormatter.ofPattern("M/d/yyyy")), 1);
            HabitManager.saveHabits();
            this.streakAndPercentage(date); // Update values
        }
    }

    public void uncomplete(LocalDate date){
        //This is useful so that if the user completes it, then marks it failed it can be removed.
        if(frequency.contains(date.getDayOfWeek().getValue())) {
            this.habitLog.put(date.format(DateTimeFormatter.ofPattern("M/d/yyyy")), 2); //2 is fail
            HabitManager.saveHabits();
            this.streakAndPercentage(date); // Update values
        }
    }

    public void removeEntry(LocalDate date){
        String key = date.format(DateTimeFormatter.ofPattern("M/d/yyyy"));
        if(habitLog.containsKey(key)){
            habitLog.remove(date.format(DateTimeFormatter.ofPattern("M/d/yyyy")));
            HabitManager.saveHabits();
            streakAndPercentage(date);
        }
    }

    public void setHabitNote(String note){
        habitNote = note;
    }

    public void setName(String newName){
        name = newName;
    }

    public void setPriority(int newPriority){
        priority = newPriority;
    }

    public void setFrequency(ArrayList<Integer> freq) {
            Collections.sort(freq);
            frequency = freq;
    }

    public void streakAndPercentage(LocalDate date){
        computeStreak(date, !normalStreak);
    }

    private void computeStreak(LocalDate date, boolean flexible){
        int numComplete = 0;
        int totalComplete = 0;
        boolean strikeOne = false;
        int tempStreak = -1;
        int longestStreak = 0;
        ArrayList<LocalDate> daysList = new ArrayList<>();
        daysList.add(date);
        ArrayList<String> freqDayList = new ArrayList<>();

        long daysSinceStart = ChronoUnit.DAYS.between(startDate, date);
        for (int day = 0; day < daysSinceStart; day++){
            daysList.add(startDate.plusDays(day));
        }

        for(int i = 0; i < daysList.size(); i++){
            LocalDate day = daysList.get(i);
            if(frequency.contains(day.getDayOfWeek().getValue())){
                freqDayList.add(day.format(DateTimeFormatter.ofPattern("M/d/yyyy")));
            }
        }

        ArrayList<String> revFreqList = freqDayList.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(ArrayList::new));
        //Reset values
        this.currentStreak = -1;
        for(int i = 0; i < revFreqList.size(); i++){
            String day = revFreqList.get(i);

            if(habitLog.containsKey(day) && habitLog.get(day) == 1){
                numComplete++;
                totalComplete++;
                strikeOne = false;
            } else{
                if(flexible){
                    if(!strikeOne){
                        strikeOne = true;
                    } else if(strikeOne){
                        strikeOne = false;
                        if(tempStreak == -1){
                            //this.currentStreak = numComplete;
                            longestStreak = numComplete;
                        } else{
                            if(numComplete > longestStreak){
                                longestStreak = numComplete;
                            }
                        }
                        tempStreak = numComplete;
                        numComplete = 0;
                    }
                } else{
                    if(tempStreak == -1){
                        //this.currentStreak = numComplete;
                        longestStreak = numComplete;
                    } else{
                        if(numComplete > longestStreak){
                            longestStreak = numComplete;
                        }
                    }
                    tempStreak = numComplete;
                    numComplete = 0;
                }
            }

        }

        //If it makes it fully through the loop, values still need to be updated
        if(this.currentStreak == -1){
            this.currentStreak = numComplete + (strikeOne ? 1 : 0);
        }
        if(this.longestStreak == -1){
            this.longestStreak = numComplete;
        } else{
            if(longestStreak > this.longestStreak) {
                this.longestStreak = longestStreak;
            }
        }

        if(currentStreak > this.longestStreak){
            this.longestStreak = currentStreak;
        }

        if(freqDayList.isEmpty()){
            this.completionPercent = 0;
        } else{
            this.completionPercent = ((double) totalComplete / freqDayList.size()) * 100;
        }
    }
}

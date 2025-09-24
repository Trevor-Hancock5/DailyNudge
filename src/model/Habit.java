/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package model;

import ui.SettingsController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to create the Habit object
 */
public class Habit {
    private static boolean normalStreak = false;
    private String habitNote;
    private String[] settingPreferences;
    private String name;
    private double completionPercent;
    private int priority;
    private final HashMap<String, Integer> habitLog;
    private ArrayList<Integer> frequency;
    private LocalDate startDate;
    private int currentStreak;
    private int longestStreak;

    /**
     * Constructor to create a new Habit
     * @param name Habit name
     * @param priority Priority for habit
     * @param habitNote User note for habit
     * @param freq Frequency of how often to do habit
     * @param startDate Day the habit was started
     * @param settingPreferences User preferences for settings
     */
    public Habit(String name, int priority, String habitNote, ArrayList<Integer> freq,
                 LocalDate startDate, String[] settingPreferences){
        this.habitNote = habitNote;
        this.name = name;
        completionPercent = 0;
        this.priority = priority;
        habitLog = new HashMap<>();
        setFrequency(freq);
        this.startDate = startDate;
        currentStreak = -1;
        longestStreak = -1;

        this.settingPreferences = settingPreferences;
        loadSettings();
    }

    public void setSettingPreferences(String[] settings){
        settingPreferences = settings;
    }

    /**
     * Method to load the settings into the Settings Controller
     */
    public void loadSettings(){
        //Setting preferences - name, profile picture directory, dark/light, flexible/normal streak
        SettingsController.setUserName(settingPreferences[0]);
        SettingsController.setProfileDir(settingPreferences[1]);
        SettingsController.setThemePreference(settingPreferences[2]);
        SettingsController.setStreakPreference(settingPreferences[3]);
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

    /**
     * Method to check if habit is active for the date
     * @param date Day to check
     * @return True if active
     */
    public boolean activeToday(LocalDate date){
        //Returns true if the habit is active today, false otherwise
        return frequency.contains(date.getDayOfWeek().getValue()) && !date.isBefore(this.startDate);
    }

    public void setStartDate(LocalDate date){
        startDate = date;
    }

    /**
     * Method to check if habit completed for date
     * @param date Date to check for
     * @return 0 for pending habit, 1 for complete, 2 for failed
     */
    public int getEntry(LocalDate date){
        String key = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        //Unmarked Habit = 0
        return habitLog.getOrDefault(key, 0);
    }

    /**
     * Method to complete the habit
     * @param date Date to check for
     */
    public void complete(LocalDate date){
        //If the habit is active today, return true, false otherwise.
        // (True if date in frequency. False if not.)
        if(!startDate.isAfter(date) && frequency.contains(date.getDayOfWeek().getValue())) {
            this.habitLog.put(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 1);
            HabitManager.saveHabits();
            this.streakAndPercentage(date); // Update values
        }
    }

    /**
     * Method to not complete the habit
     * @param date Remove completion from this date
     */
    public void uncomplete(LocalDate date){
        //This is useful so that if the user completes it, then marks it failed it can be removed.
        if(!startDate.isAfter(date) && frequency.contains(date.getDayOfWeek().getValue())) {
            this.habitLog.put(date.format(DateTimeFormatter
                    .ofPattern("MM/dd/yyyy")), 2); //2 is fail
            HabitManager.saveHabits();
            this.streakAndPercentage(date); // Update values
        }
    }

    /**
     * Method to remove an entry - completion
     * @param date Date to remove completion
     */
    public void removeEntry(LocalDate date){
        String key = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        if(habitLog.containsKey(key)){
            habitLog.remove(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
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

    /**
     * Method to set the frequency of the habit
     * @param freq List of integers for frequency
     */
    public void setFrequency(ArrayList<Integer> freq) {
        Collections.sort(freq);
        frequency = freq;
    }

    /**
     * Method to call streak and percentage
     * @param date Final date for streak
     */
    public void streakAndPercentage(LocalDate date){
        computeStreak(date, !normalStreak);
    }

    /**
     * Method to get the habit log of the Habit
     * @return String for habit log
     */
    public String getHabitLog(){
        final int year = 6;
        final int dayOfMonth = 5;
        //Have to convert to LocalDate, sort, then convert back to String (Otherwise, random order)
        List<LocalDate> days = new ArrayList<>();
        for(String day : habitLog.keySet()){
            days.add(LocalDate.of(Integer.parseInt(day.substring(year)),
                    Integer.parseInt(day.substring(0, 2)),
                    Integer.parseInt(day.substring(3, dayOfMonth))));
        }

        days = days.stream().sorted().toList();

        StringBuilder ret = new StringBuilder();
        for(LocalDate day : days){
            String formattedDay = day.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            if(!ret.isEmpty()) {
                ret.append(", ").append(formattedDay);
            } else{
                ret.append(formattedDay);
            }
        }
        return ret.toString();
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

        for (LocalDate day : daysList) {
            if (frequency.contains(day.getDayOfWeek().getValue())) {
                freqDayList.add(day.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            }
        }

        ArrayList<String> freqList = freqDayList.stream().distinct().sorted()
                .collect(Collectors.toCollection(ArrayList::new));
        //Reset values
        this.currentStreak = -1;
        for (String day : freqList) {
            if (habitLog.containsKey(day) && habitLog.get(day) == 1) {
                numComplete++;
                totalComplete++;
                strikeOne = false;
            } else {
                if (flexible) {
                    if (!strikeOne) {
                        strikeOne = true;
                    } else {
                        strikeOne = false;
                        if (tempStreak == -1) {
                            //this.currentStreak = numComplete;
                            longestStreak = numComplete;
                        } else {
                            if (numComplete > longestStreak) {
                                longestStreak = numComplete;
                            }
                        }
                        tempStreak = numComplete;
                        numComplete = 0;
                    }
                } else {
                    if (tempStreak == -1) {
                        //this.currentStreak = numComplete;
                        longestStreak = numComplete;
                    } else {
                        if (numComplete > longestStreak) {
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
            final int toPercent = 100;
            this.completionPercent = ((double) totalComplete / freqDayList.size()) * toPercent;
        }
    }
}

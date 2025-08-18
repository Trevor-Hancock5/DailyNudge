/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 7/25/2025
 */
package model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


public class Habit {
    private HashMap<LocalDate, String> notes;
    private String habitNote;
    private String name;
    private double completionPercent;
    private int priority;
    private Set<String> habitLog;
    private ArrayList<Integer> frequency;
    private LocalDate startDate;
    private int currentStreak;
    private int longestStreak;

    public Habit(String name, int priority, String habitNote, ArrayList<Integer> freq, LocalDate startDate){
        notes = new HashMap<>();
        this.habitNote = habitNote;
        this.name = name;
        completionPercent = 0;
        this.priority = priority;
        habitLog = new HashSet<>();
        setFrequency(freq);
        this.startDate = startDate;
        currentStreak = -1;
        longestStreak = -1;
    }

    public String getName(){
        return name;
    }

    public String getHabitNote(){
        return habitNote;
    }

    public String getStartDate(){
        return startDate.format(DateTimeFormatter.ofPattern("M/d/yyyy"));
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

    public boolean activeToday(){
        //Returns true if the habit is active today, false otherwise
        return frequency.contains(LocalDate.now().getDayOfWeek().getValue());
    }

    private void setStartDate(LocalDate date){
        //This stuff will happen in newHabit UI

        startDate = date;
    }

    protected boolean complete(LocalDate date){
        //True if date in frequency. False if not.
        if(frequency.contains(date.getDayOfWeek().getValue())) {
            this.habitLog.add(date.format(DateTimeFormatter.ofPattern("M/d/yyyy")));
            this.streakAndPercentage(); // Update values
            return true;
        } else{
            return false;
        }
    }

    protected void takeNote(String note, LocalDate date){
        notes.put(date, note);
    }

    protected void setHabitNote(String note){
        habitNote = note;
    }

    protected void setName(String newName){
        name = newName;
    }

    protected void setPriority(int newPriority){
        priority = newPriority;
    }

    private void setFrequency(ArrayList<Integer> freq) {
        //Use this code for the ui.
//        ArrayList<Character> validDays = new ArrayList<>(Arrays.asList('M', 'T', 'W', 'R', 'F', 'S', 'J'));
//        Map<Character, Integer> dayConversion = Map.ofEntries(
//                Map.entry('M', 1),
//                Map.entry('T', 2),
//                Map.entry('W', 3),
//                Map.entry('R', 4),
//                Map.entry('F', 5),
//                Map.entry('S', 6),
//                Map.entry('J', 7)
//        );
//        char again = 'y';
//        while (again == 'y'){
//            System.out.print("\nEnter a day, daily, or weekdays: ");
//            String day = scan.next().toUpperCase();
//            if (validDays.contains(day.charAt(0))){
//                frequency.add(dayConversion.get(day.charAt(0)));
//                System.out.print("Add another day (y or n): ");
//                again = scan.next().toLowerCase().charAt(0);
//            } else if(day.startsWith("D")){
//                frequency = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7));
//                again = 'n'; //Stop loop
//            } else if(day.startsWith("WD")){
//                frequency = new ArrayList<>(Arrays.asList(1,2,3,4,5));
//                again = 'n'; //Stop loop
//            } else{
//                System.out.println("Enter a valid day please - MTWRFSJ or D or WD");
//                //Go again
//            }
//    }
            Collections.sort(freq);
            frequency = freq;
    }

    private void streakAndPercentage(){
        int numComplete = 0;
        int totalComplete = 0;
        boolean strikeOne = false;
        int tempStreak = -1;
        int longestStreak = 0;
        ArrayList<LocalDate> daysList = new ArrayList<>();
        daysList.add(LocalDate.now());
        ArrayList<String> freqDayList = new ArrayList<>();

        long daysSinceStart = ChronoUnit.DAYS.between(startDate, LocalDate.now());
        for (int day = 0; day < daysSinceStart; day++){
            daysList.add(startDate.plusDays(day));
        }

        for(int i = 0; i < daysList.size(); i++){
            LocalDate day = daysList.get(i);
            if(frequency.contains(day.getDayOfWeek().getValue())){
                freqDayList.add(day.toString());
            }
        }

        ArrayList<String> revFreqList = freqDayList.stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toCollection(ArrayList::new));
        for(int i = 0; i < revFreqList.size(); i++){
            String day = revFreqList.get(i);
            if(habitLog.contains(day)){
                numComplete++;
                totalComplete++;
                strikeOne = false;
            } else if(!strikeOne){
                strikeOne = true;
            } else if(strikeOne){
                strikeOne = false;
                if(tempStreak == -1){
                    this.currentStreak = numComplete;
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

        //If it makes it fully through the loop, values still need to be updated
        if(this.currentStreak == -1){
            this.currentStreak = numComplete;
        }
        if(this.longestStreak == -1){
            this.longestStreak = numComplete;
        } else{
            this.longestStreak = longestStreak;
        }

        if(freqDayList.isEmpty()){
            this.completionPercent = 0;
        } else{
            this.completionPercent = ((double) totalComplete / freqDayList.size()) * 100;
        }
    }
}

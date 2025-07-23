/*
 * Course: CSC1110A-111
 * Fall 2024
 * Assignment
 * Name: Trevor Hancock
 * Last Updated: 7/23/2025
 */
package model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Habit {
    HashMap<LocalDate, String> notes;
    String habitNote;
    String name;
    double completionPercent;
    int priority;
    Set<String> habitLog;
    ArrayList<Integer> frequency;
    LocalDate startDate;
    int currentStreak;
    int longestStreak;
    Scanner scan;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    public Habit(String name, int priority, String habitNote, Scanner scan){
        this.scan = scan;
        notes = new HashMap<>();
        this.habitNote = habitNote;
        this.name = name;
        completionPercent = 0;
        this.priority = priority;
        habitLog = new HashSet<>();
        frequency = new ArrayList<>();
        setFrequency();
        startDate = setStartDate();
        currentStreak = -1;
        longestStreak = -1;
    }

    private LocalDate setStartDate(){
        //Rework this to have it have today as the default and ask instead for a change.
        System.out.print("When would you like the start date to be for this habit?\nEnter (T) " +
                "for today or (C) for custom: ");
        String startDate = scan.next();
        LocalDate formattedDate = null;
        if(startDate.toUpperCase().charAt(0) == 'T'){
            return LocalDate.now();
        } else{
            boolean badInput;
            String customDate = "";
            do{
                try {
                    System.out.print("Enter the date(yyyy-mm-dd): ");
                    customDate = scan.next();
                    formattedDate = LocalDate.of(
                            Integer.parseInt(customDate.substring(0, 4)),
                            Integer.parseInt(customDate.substring(5,7)),
                            Integer.parseInt(customDate.substring(8)));
                    badInput = false;
                } catch (DateTimeException e) {
                    System.out.println("Bad value. Try again!");
                    badInput = true;
                }
            } while(badInput);
            return formattedDate;
        }
    }

//Only add date to habit log if completed and specific day of week listed in frequency, otherwise don't list
    private void complete(){
        LocalDate formattedDate = null;
        ArrayList<Integer> weekdays = new ArrayList<>();
        weekdays.add(1);
        weekdays.add(2);
        weekdays.add(3);
        weekdays.add(4);
        weekdays.add(5);

        System.out.print("Enter 0 for today, 1 for custom date: ");
        int input = Integer.parseInt(scan.next());
        if(input == 1){
            boolean badInput = true;
            String customDate = "";
            while (badInput) {
                try{
                    System.out.print("Enter in the date(yyyy-mm-dd): ");
                    String date = scan.next();
                    formattedDate = LocalDate.of(Integer.parseInt(date.substring(0,4)),
                            Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8)));
                    if (LocalDate.now().compareTo(formattedDate) >= 0 && formattedDate.compareTo(this.startDate) >= 0){
                        badInput = false;
                    } else{
                        System.out.println("Date has to be today or in the past, can't be in the future!");
                    }
                } catch (RuntimeException e) {
                    System.out.println("For dev, enter passcode: ");
                    int code = scan.nextInt();
                    if (code == 4079){
                        System.out.println(e + "\n");
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    } else {
                        System.out.println("Error. Try again");
                    }
                }
            } if(frequency.contains(formattedDate)) {
                this.habitLog.add(formattedDate.format(formatter));
                this.streakAndPercentage(); // Update values
            } else{
                System.out.println("Given date not in frequency.");
            }

        } else{
            //Assuming complete for today can only happen if in frequency
            habitLog.add(formattedDate.format(formatter));
            this.streakAndPercentage(); //Update values
        }
    }

    private void takeNote(){
        char again;
        do{
            System.out.println("Enter note here: ");
            String note = scan.next();
            System.out.print("Enter (T) for today, or (C) for custom: ");
            String input = scan.next();
            LocalDate date = null;
            if(input.toUpperCase().charAt(0) == 'T'){
                date = LocalDate.now();
            } else{
                boolean badInput;
                do{
                    try{
                        System.out.print("Enter date(yyyy-mm-dd): ");
                        String testDate = scan.next();
                        date = LocalDate.of(
                                Integer.parseInt(testDate.substring(0,4)),
                                Integer.parseInt(testDate.substring(5,7)),
                                Integer.parseInt(testDate.substring(8)));
                        badInput = false;
                    } catch (RuntimeException e){
                        System.out.println("Error. Try again! \n");
                        badInput = true;
                    }
                } while(badInput);
            }

            notes.put(date, note);
            System.out.print("Would you like to add another note(y or n)?");
            String another = scan.next();
            again = another.toLowerCase().charAt(0);
        } while (again == 'y');
    }

    private void setHabitNote(){
        System.out.println("Enter a new note for " + this.name + ": ");
        habitNote = scan.next();
    }

    private void setName(){
        System.out.print("Enter changed name for " + name + ": ");
        name = scan.nextLine();
    }

    private void setPriority(){
        System.out.println("Enter changed priority here: ");
        priority = scan.nextInt();
    }

    private void setFrequency(){
        System.out.println("Setting frequency");
        System.out.println("Monday = M\nTuesday = T\nWednesday = W\nThursday = R\nFriday = F\nSaturday = S\n" +
                "Sunday = J\nDaily = D\nWeekdays = WD");
        ArrayList<Character> validDays = new ArrayList<>(Arrays.asList('M', 'T', 'W', 'R', 'F', 'S', 'J'));
        HashMap<Character, Integer> dayConversion = (HashMap<Character, Integer>) Map.ofEntries(
                Map.entry('M', 1),
                Map.entry('T', 2),
                Map.entry('W', 3),
                Map.entry('R', 4),
                Map.entry('F', 5),
                Map.entry('S', 6),
                Map.entry('J', 7)
        );
        char again = 'y';
        while (again == 'y'){
            System.out.print("\nEnter a day, daily, or weekdays: ");
            String day = scan.next().toUpperCase();
            if (validDays.contains(day.charAt(0))){
                frequency.add(dayConversion.get(day));
                System.out.print("Add another day (y or n): ");
                again = scan.next().toLowerCase().charAt(0);
            } else if(day.startsWith("D")){
                frequency = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7));
                again = 'n'; //Stop loop
            } else if(day.startsWith("WD")){
                frequency = new ArrayList<>(Arrays.asList(1,2,3,4,5));
                again = 'n'; //Stop loop
            } else{
                System.out.println("Enter a valid day please - MTWRFSJ or D or WD");
                //Go again
            }

            frequency = (ArrayList<Integer>) frequency.stream().sorted().toList();
        }
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

        //TODO
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

        ArrayList<String> revFreqList = (ArrayList <String>) freqDayList.stream().distinct().sorted(Comparator.reverseOrder()).toList();
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

    @Override
    public String toString() {
        return String.format("""
                Habit Name: %s
                Habit Note: %s
                Priority: %d
                Completion Percent: %.2f
                Current Streak: %d
                Longest Streak: %d
                """, name, habitNote, priority, completionPercent, currentStreak, longestStreak);
    }
}

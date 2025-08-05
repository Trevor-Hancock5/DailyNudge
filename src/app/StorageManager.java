/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 7/23/2025
 */
package app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Habit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
//    Possibly creating default files if none exist yet
    private static final File USER_DATA = new File("data/userData.json");
    private static Gson gson = new Gson();

    public static List<Habit> loadHabits(){
        List<Habit> ret = new ArrayList<>();
        try(FileReader reader = new FileReader(USER_DATA)){
            Type habitListType = new TypeToken<List<Habit>>(){}.getType();
            ret = gson.fromJson(reader, habitListType);
        } catch(IOException e){
            System.out.println(e);
        }
        return ret;
    }

    public static boolean saveHabits(List<Habit> habits){
        boolean ret;
        try(FileWriter writer = new FileWriter(USER_DATA)){
            gson.toJson(habits, writer);
            ret = true;
        } catch (IOException e){
            System.out.println(e);
            ret = false;
        }
        return ret;
    }
}

/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/26/2025
 */
package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Habit;
import ui.DashboardController;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to manager storage
 */
public class StorageManager {
    private static final File USER_DATA = new File("data/userData.json");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter out, LocalDate value) throws IOException {
                    out.value(value.toString());
                }
                @Override
                public LocalDate read(JsonReader in) throws IOException {
                    return LocalDate.parse(in.nextString());
                }
            })
            .create();

    public static Gson getGson(){
        return GSON;
    }

    /**
     * Method to load up the habits
     * @return A list of habits from the data
     */
    public static List<Habit> loadHabits(){
        List<Habit> ret = new ArrayList<>();
        if(USER_DATA.exists()) {
            try (FileReader reader = new FileReader(USER_DATA)) {
                Type habitListType = new TypeToken<List<Habit>>() {
                }.getType();
                ret = GSON.fromJson(reader, habitListType);
            } catch (IOException e) {
                DashboardController.showAlert("Error loading data into Dashboard!");
            }
        }
        return ret;
    }

    /**
     * Method to save habits
     * @param habits List of habits to save
     * @return True if saved
     */
    public static boolean saveHabits(List<Habit> habits){
        boolean ret;
        try(FileWriter writer = new FileWriter(USER_DATA)){
            GSON.toJson(habits, writer);
            ret = true;
        } catch (IOException e){
            DashboardController.showAlert("Error saving habit data!");
            ret = false;
        }
        return ret;
    }
}

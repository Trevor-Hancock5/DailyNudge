/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 7/23/2025
 */
package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Habit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final File USER_DATA = new File("data/userData.json");
    private static final Gson gson = new GsonBuilder()
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

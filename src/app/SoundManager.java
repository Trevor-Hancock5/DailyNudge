/*
 * Course: CSC1110A-111
 * Fall 2024
 * Assignment
 * Name: Trevor Hancock
 * Last Updated: 9/22/2025
 */
package app;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final double defaultVolume = 0.25;
    private static MediaPlayer backgroundPlayer;
    private final String backgroundDefaultPath = (getClass().getResource("../resources/sounds/backgroundMusic.mp3")).toExternalForm();
    private Map<String, MediaPlayer> sounds;

    public SoundManager(){
        sounds = new HashMap<>();
        final String startPath = "../resources/sounds/";
        sounds.put("button", new MediaPlayer(new Media(getClass().getResource(startPath + "button.mp3").toExternalForm())));
        sounds.put("toggle", new MediaPlayer(new Media(getClass().getResource(startPath + "toggleButton.mp3").toExternalForm())));
        sounds.put("textfield", new MediaPlayer(new Media(getClass().getResource(startPath + "textField.mp3").toExternalForm())));
        sounds.put("complete", new MediaPlayer(new Media(getClass().getResource(startPath + "complete.mp3").toExternalForm())));
        sounds.put("fail", new MediaPlayer(new Media(getClass().getResource(startPath + "fail.mp3").toExternalForm())));
        sounds.put("pending", new MediaPlayer(new Media(getClass().getResource(startPath + "pending.mp3").toExternalForm())));
        sounds.put("error", new MediaPlayer(new Media(getClass().getResource(startPath + "error.mp3").toExternalForm())));
        sounds.put("me", new MediaPlayer(new Media(getClass().getResource(startPath + "me.mp3").toExternalForm()))); // Use when user clicks about me
        sounds.put("newhabit", new MediaPlayer(new Media(getClass().getResource(startPath + "newHabit.mp3").toExternalForm()))); //Sound for a new habit being made
    }

    public void playSound(String typeOfSound){
        //Pending needs to be louder. Fail should be a little quieter. Textfield quiety
        //TODO implement the button, toggle, and textfield sounds everywhere!!
        //TODO: Implement a button in settings to disable background or disable sound effects
        MediaPlayer mediaPlayer = sounds.get(typeOfSound.toLowerCase());
        mediaPlayer.stop();
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }

    public void playBackgroundMusic(){
        playBackgroundMusic(backgroundDefaultPath);
    }

    public void playBackgroundMusic(String filePath){
        Media sound = new Media(filePath);
        backgroundPlayer = new MediaPlayer(sound);
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.setVolume(defaultVolume);
        backgroundPlayer.play();
    }

    public void stopBackgroundMusic(){
        backgroundPlayer.stop();
    }

    public void resumeBackgroundMusic(){
        if(backgroundPlayer != null){
            backgroundPlayer.play();
        } else{
            playBackgroundMusic(backgroundDefaultPath);
        }
    }

    public void setBackgroundVolume(double volume){
        //0.0 to 1.0
        if(backgroundPlayer != null){
            backgroundPlayer.setVolume(volume);
        } else{
            playBackgroundMusic(backgroundDefaultPath);
        }
    }
}

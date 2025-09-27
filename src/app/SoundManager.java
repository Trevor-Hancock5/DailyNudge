/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/26/2025
 */
package app;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import ui.DashboardController;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage all sound related operations for the app
 */
public class SoundManager {
    private static double musicVolume = 0.25;
    private static MediaPlayer backgroundPlayer;
    private final String backgroundDefaultPath = (getClass()
            .getResource("../resources/sounds/backgroundMusic.mp3")).toExternalForm();
    private final Map<String, MediaPlayer> sounds;

    /**
     * Constructor to initialize different sound effects
     */
    public SoundManager(){
        sounds = new HashMap<>();
        final String startPath = "../resources/sounds/";
        sounds.put("button", new MediaPlayer(new Media(getClass()
                .getResource(startPath + "button.mp3").toExternalForm())));
        sounds.put("toggle", new MediaPlayer(new Media(getClass()
                .getResource(startPath + "toggleButton.mp3").toExternalForm())));
        sounds.put("textfield", new MediaPlayer(new Media(getClass()
                .getResource(startPath + "textField.mp3").toExternalForm())));
        sounds.put("complete", new MediaPlayer(new Media(getClass()
                .getResource(startPath + "complete.mp3").toExternalForm())));
        sounds.put("fail", new MediaPlayer(new Media(getClass()
                .getResource(startPath + "fail.mp3").toExternalForm())));
        sounds.put("pending", new MediaPlayer(new Media(getClass()
                .getResource(startPath + "pending.mp3").toExternalForm())));
        sounds.put("error", new MediaPlayer(new Media(getClass()
                .getResource(startPath + "error.mp3").toExternalForm())));
        // Use when user clicks about me
        sounds.put("me", new MediaPlayer(new Media(getClass()
                .getResource(startPath + "me.mp3").toExternalForm())));
        //Sound for a new habit being made
        sounds.put("newhabit", new MediaPlayer(new Media(getClass()
                .getResource(startPath + "newHabit.mp3").toExternalForm())));
    }

    /**
     * Method to set the background music volume
     * @param vol volume to set it to
     */
    public static void setMusicVolume(double vol){
        musicVolume = vol;
        backgroundPlayer.setVolume(musicVolume);
    }

    /**
     * Method to play a sound effect
     * @param typeOfSound Sound effect in the sounds Map
     */
    public void playSound(String typeOfSound){
        if(DashboardController.getSoundEffects()) {
            MediaPlayer mediaPlayer = sounds.get(typeOfSound.toLowerCase());
            mediaPlayer.stop();
            if (typeOfSound.equals("pending")) {
                mediaPlayer.setVolume(0.8);
            } else if (typeOfSound.equals("fail") || typeOfSound.equals("textfield")) {
                mediaPlayer.setVolume(0.35);
            } else {
                mediaPlayer.setVolume(0.5);
            }
            mediaPlayer.play();
        }
    }

    /**
     * Method to start the background music
     */
    public void playBackgroundMusic(){
        playBackgroundMusic(backgroundDefaultPath);
    }

    private void playBackgroundMusic(String filePath){
        Media sound = new Media(filePath);
        backgroundPlayer = new MediaPlayer(sound);
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.setVolume(musicVolume);
        backgroundPlayer.stop(); //This is to ensure that if they stop and then start it can play
        backgroundPlayer.play();
    }

    /**
     * Method to stop the background music
     */
    public void stopBackgroundMusic(){
        backgroundPlayer.stop();
    }

    /**
     * Method to resume the background music
     */
    public void resumeBackgroundMusic(){
        if(backgroundPlayer != null){
            backgroundPlayer.play();
        } else{
            playBackgroundMusic(backgroundDefaultPath);
        }
    }
}

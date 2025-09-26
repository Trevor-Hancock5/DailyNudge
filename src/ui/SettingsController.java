/*
 * DailyNudge
 * Name: Trevor Hancock
 * Last Updated: 9/21/2025
 */
package ui;

import app.SoundManager;
import app.StorageManager;
import com.google.gson.Gson;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Habit;
import model.HabitManager;

import java.awt.Desktop;
import java.io.FileWriter;
import java.util.List;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller class for settings.
 */
public class SettingsController {
    private static DashboardController dashboardController;
    private static Stage stage;
    private static String userName;
    private static String profileDir;
    private static String themePreference;
    private static String streakPreference;

    @FXML
    private ImageView imageView;
    @FXML
    private TextField name;
    @FXML
    private Label version;
    @FXML
    private ToggleButton lightMode;
    @FXML
    private ToggleButton flexibleStr;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox backgroundVBox;
    @FXML
    private ToggleButton music;
    @FXML
    private ToggleButton effects;
    @FXML
    private Slider musicSlider;
    @FXML
    private Label general;
    @FXML
    private Label appearance;
    @FXML
    private Label sound;
    @FXML
    private Label data;
    @FXML
    private Label about;

    private final SceneSwitcher switcher;
    private final Image defaultProfile = new Image(Objects.requireNonNull(getClass()
            .getResource("/resources/defaultAvatar.jpg")).toExternalForm());
    private Label[] labels;

    /**
     * Constructor to set SceneSwitcher
     * @param switcher The SceneSwitcher instance
     */
    public SettingsController(SceneSwitcher switcher){
        this.switcher = switcher;
    }

    public static String[] getSettingPreferences(){
        return new String[] {userName, profileDir, themePreference, streakPreference};
    }

    public static void setUserName(String name){
        userName = name;
    }

    public static String getUserName(){
        return userName;
    }

    public static void setProfileDir(String dir){
        profileDir = dir;
    }

    /**
     * Setting the theme for the app
     * @param theme "light" for light mode and "dark" for dark mode
     */
    public static void setThemePreference(String theme){
        if(theme != null) {
            themePreference = theme;
            SceneSwitcher.setTheme(theme.equals("light"));
        }
    }

    public static void setStreakPreference(String streak){
        streakPreference = streak;
    }

    public static String getStreakPreference(){
        return streakPreference;
    }

    public static void setStage(Stage stage){
        SettingsController.stage = stage;
    }

    @FXML
    private void initialize(){
        labels = new Label[] {general, appearance, sound, data, about};
        for(int i = 0; i < 5; i++){
            labels[i].setStyle("-fx-font-size: 35;" +
                    "-fx-font-weight: bold");
        }

        scrollPane.setContent(backgroundVBox);
        scrollPane.setFitToWidth(true);
        name.setText(userName);
        updateName();

        effects.setSelected(true);
        music.setSelected(true);
        musicSlider.valueProperty().addListener((_, _, newVal) -> SoundManager.setMusicVolume(((Double) newVal / 100)));

        Image img;
        try{
            img = new Image(Objects.requireNonNull(new File(profileDir).toURI().toString()));
        } catch (NullPointerException | IllegalArgumentException e) {
            img = defaultProfile;
        }
        try {
            setImage(img);
        } catch (Exception e) {
            setImage(defaultProfile);
        }

        if(themePreference != null && !themePreference.equals("light")){
            lightMode.setSelected(false);
            lightMode.setText("Dark Mode");
        } else{
            lightMode.setSelected(true);
        }
        changeTheme();

        if(streakPreference != null && !streakPreference.equals("flex")) {
            flexibleStr.setSelected(false);
            flexibleStr.setText("Normal Streak");
        } else{
            flexibleStr.setSelected(true);
        }
        Habit.setStreakRule(!flexibleStr.isSelected());
        HabitManager.saveHabits();

        version.setText("v1.0.0");
    }

    private void setImage(Image image){
        // Get min dimension to crop to square
        double size = Math.min(image.getWidth(), image.getHeight());

        // Center offsets
        double x = (image.getWidth() - size) / 2;
        double y = (image.getHeight() - size) / 2;

        // Snapshot to WritableImage
        PixelReader reader = image.getPixelReader();
        WritableImage cropped = new WritableImage(reader, (int)x, (int)y, (int)size, (int)size);

        // Now put it into ImageView
        imageView.setImage(cropped);

        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        Circle clip = new Circle(imageView.getFitWidth()/2, imageView.getFitHeight()/2, radius);
        imageView.setClip(clip);
    }

    private void changeTheme(){
        if(SceneSwitcher.getTheme().contains("light")){
            backgroundVBox.setStyle("-fx-background-color: #f9f9f9;");
        } else{
            backgroundVBox.setStyle("-fx-background-color: #1e1e1e;");
        }
    }

    @FXML
    private void chooseProfile(){
        DashboardController.SOUND.playSound("button");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png")
        );
        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            profileDir = file.getAbsolutePath();
            try {
                setImage(new Image(Objects.requireNonNull(file.toURI().toString())));
            } catch (NullPointerException e){
                setImage(defaultProfile);
            }
        }
    }

    @FXML
    private void getName(){
        updateName();
        name.setStyle("-fx-background-color: lightgreen;" +
                "-fx-text-fill: black;");
        Glow glow = new Glow(0.3);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.LIMEGREEN);
        dropShadow.setRadius(20);

        glow.setInput(dropShadow);

        name.setEffect(glow);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(_ -> {
            name.setStyle("");
            name.setEffect(null);
        });
        pause.play();
        
        DashboardController.SOUND.playSound("textfield");
//        dashboardController.updateHabits();
    }

    private void updateName(){
        userName = name.getText();
        if(!(userName == null || userName.isBlank())) {
            dashboardController.title.setText(userName + "'s DailyNudge");
            name.setText(userName);
        } else{
            dashboardController.title.setText("DailyNudge");
        }
        dashboardController.updateHabits();
        HabitManager.saveHabits();
    }

    @FXML
    private void flexibleStreak() {
        if(flexibleStr.isSelected()){
            flexibleStr.setText("Flexible Streak");
            streakPreference = "flex";
        } else{
            flexibleStr.setText("Normal Streak");
            streakPreference = "norm";
        }
        Habit.setStreakRule(!flexibleStr.isSelected());
        HabitManager.saveHabits();
        
        DashboardController.SOUND.playSound("toggle");
    }

    @FXML
    private void feedback() {
        try {
            DashboardController.SOUND.playSound("textfield"); //More positive sound!
            Desktop.getDesktop().browse(new URI("https://forms.gle/UVMA5KcSmc4Xyiqm7"));
        } catch (URISyntaxException | IOException e){
            DashboardController.showAlert("Error with opening feedback form. Sorry!");
        }
    }

    @FXML
    private void changeMode(){
        DashboardController.SOUND.playSound("toggle");

        if(lightMode.isSelected()){
            lightMode.setText("Light Mode");
            SceneSwitcher.setTheme(true);
            themePreference = "light";
        } else{
            lightMode.setText("Dark Mode");
            SceneSwitcher.setTheme(false);
            themePreference = "dark";
        }
        switcher.updateTheme();
        changeTheme();
    }

    private List<Habit> readJsonFile(File file){
        List<Habit> habits = null;
        try (FileReader reader = new FileReader(file)) {
            Gson gson = StorageManager.getGson();
            habits = gson.fromJson(reader, new TypeToken<List<Habit>>() { } .getType());
            for(Habit habit : habits){
                habit.streakAndPercentage(dashboardController.habitDate.getValue());
            }
        } catch (IOException e) {
            DashboardController.showAlert("Error importing habits");
        }
        return habits;
    }

    @FXML
    private void importData() {
        DashboardController.SOUND.playSound("button");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Habit Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        fileChooser.setInitialDirectory(new File("."));

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                HabitManager.saveHabits(readJsonFile(selectedFile));
            } catch (com.google.gson.JsonSyntaxException _){
                DashboardController.showAlert("Error importing data!");
                HabitManager.saveHabits(); // Reset userData so it loads next time
                Platform.exit(); //Closes blank screen
            }
            dashboardController.updateHabits();
            updateName();
            
            DashboardController.SOUND.playSound("textfield"); //Positive sound to indicate success
        }
    }

    @FXML
    private void save() {
        changeTheme();
        updateName();
        dashboardController.updateHabits();
        HabitManager.saveHabits();
        
        DashboardController.SOUND.playSound("textfield"); //Positive sound
    }

    @FXML
    private void backup() {
        updateName();
        
        DashboardController.SOUND.playSound("textfield"); // Positive sound

        File backupFile = new File("data/userData_backup.json");
        try (FileWriter writer = new FileWriter(backupFile)) {
            Gson gson = StorageManager.getGson();
            gson.toJson(HabitManager.getHabits(), writer);
        } catch (IOException e) {
            DashboardController.showAlert("Error backing up data");
        }
    }

    @FXML
    private void export() {
        DashboardController.SOUND.playSound("button");

        updateName();
        HabitManager.saveHabits();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Location for Exported Data");
        fileChooser.getExtensionFilters().add(new FileChooser
                .ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if(selectedFile != null){
            try(FileWriter file = new FileWriter(selectedFile)){
                Gson gson = StorageManager.getGson();
                gson.toJson(HabitManager.getHabits(), file);
            } catch (IOException e){
                DashboardController.showAlert("Error exporting data!");
            }
        }
    }

    @FXML
    private void resetAllData() {
        Alert check = new Alert(Alert.AlertType.CONFIRMATION);
        check.setTitle("Reset Data?");
        check.setHeaderText("Are you sure you want to reset all your data?");
        check.setContentText("Once you confirm, there is no way to retrieve your data!");
        
        DashboardController.SOUND.playSound("pending"); //Slightly negative sound

        Optional<ButtonType> result = check.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            File dataFile = new File("data/userData.json");
            if(dataFile.exists()){
                if(dataFile.delete()){
                    DashboardController.SOUND.playSound("textfield"); //positive sound to indicate success

                    dashboardController.updateHabits();
                    HabitManager.saveHabits();
                    name.setText("");
                    setImage(defaultProfile);
                    dashboardController.title.setText("DailyNudge");
                } else{
                    DashboardController.showAlert("Reset Failed");
                }
            }
        }
    }

    @FXML
    private void aboutMe(){
        //TODO: DOn't use the dash.alert bro!
        DashboardController.SOUND.playSound("me");

        DashboardController.showAlert("""
                Hello! I am Trevor Hancock, the creator of DailyNudge.
                Thank you for using DailyNudge. I enjoyed working on this project.
                I hope that it is a tool that you believe is useful.
                It was definitely a challenge to make and get right, but I am so
                glad with how far I was able to get it!
                Let me know of any bugs through the feedback button in settings!
                """);
    }

    @FXML
    private void backToDash() {
        DashboardController.SOUND.playSound("button");

//        updateName();
        SceneView.DASHBOARD.switchTo(switcher);
        dashboardController.updateHabits();
    }

    @FXML
    private void backgroundMusic(){
        DashboardController.SOUND.playSound("toggle");

        if(music.isSelected()){
            DashboardController.setBackgroundMusic(true);
            DashboardController.SOUND.resumeBackgroundMusic();
        } else{
            DashboardController.setBackgroundMusic(false);
            DashboardController.SOUND.stopBackgroundMusic();
        }
    }

    @FXML
    private void soundEffects(){
        DashboardController.SOUND.playSound("toggle");

        if(effects.isSelected()){
            DashboardController.setSoundEffects(true);
        } else{
            DashboardController.setSoundEffects(false);
        }
    }

    protected static void setDashboardController(DashboardController dash){
        dashboardController = dash;
    }
}

/*
 * Course: CSC1110A-111
 * Fall 2024
 * Assignment
 * Name: Trevor Hancock
 * Last Updated: 9/12/2025
 */
package ui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class SettingsController {
    @FXML
    ImageView imageView;

    @FXML
    private void initialize(){
        Image img = new Image(Objects.requireNonNull(getClass().getResource("/ui/defaultAvatar.jpg")).toExternalForm());
        imageView.setImage(img);  // ✅ works on ImageView

        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        Circle clip = new Circle(imageView.getFitWidth()/2,imageView.getFitHeight()/2, radius);
        imageView.setClip(clip);
    }
}

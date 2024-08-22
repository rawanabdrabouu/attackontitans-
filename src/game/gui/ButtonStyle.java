package game.gui;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class ButtonStyle extends Button {

    public ButtonStyle(String text) {
        super(text);

        // Create a scale transition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), this);
        scaleTransition.setToX(1.2); // Scale to 120% on X axis
        scaleTransition.setToY(1.2); // Scale to 120% on Y axis

        // Create an inverse scale transition for when the mouse exits
        ScaleTransition reverseTransition = new ScaleTransition(Duration.millis(100), this);
        reverseTransition.setToX(1); // Scale back to original size on X axis
        reverseTransition.setToY(1); // Scale back to original size on Y axis

        // Add event handlers for mouse enter and exit
        setOnMouseEntered(event -> {
            scaleTransition.play();
            setStyle("-fx-background-color: linear-gradient(to bottom, yellow, gray, black);");
        });
        setOnMouseExited(event -> {
            reverseTransition.play();
            setStyle("");
        });
    }
}

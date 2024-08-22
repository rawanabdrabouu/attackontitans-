package game.gui;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
public class MainMenue {

    public static Scene create() {
        ImageView logo = new ImageView(new Image("/Source/logo.png"));
        logo.setX(500);
        logo.setY(15);
        logo.setFitWidth(600);
        logo.setPreserveRatio(true);

        Button start = new Button("New Game");
        start.setPrefSize(300, 75);
        start.setFont(Font.font("Arial", 26));
        start.setOnAction(e -> {
            // Action to perform when the start button is clicked
            System.out.println("Start button clicked");
        });

        Button quit = new Button("Quit");
        quit.setPrefSize(300, 75);
        quit.setFont(Font.font("Arial", 26));
        quit.setOnAction(e -> {
            // Action to perform when the quit button is clicked
            System.out.println("Quit button clicked");
        });

        VBox buttons = new VBox();
        buttons.getChildren().add(start);
        buttons.getChildren().add(quit);
        buttons.setLayoutX(650);
        buttons.setLayoutY(400);
        buttons.setSpacing(20);

        Pane root = new Pane(logo);
        Image img = new Image("/Source/Background.jpeg");
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);
        root.getChildren().add(buttons);

        return new Scene(root, 1600, 900, Color.rgb(33, 41, 50));
    }
}


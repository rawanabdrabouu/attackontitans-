package game.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ViewManager {
    private static final int HEIGHT = 700;
    private static final int WIDTH = 900;
    private Scene mainScene;
    private Stage mainStage;
    private Pane mainPane;
    private static final String ICON_PATH = "/Source/Background.jpg";
    private static final String WINDOW_TITLE = "Attack on Titans";
    private static final String LOGO_PATH = "/Source/logo.png"; // Path to your PNG logo file

    private ButtonStyle startButton; // Declare startButton as a class-level variable

    public ViewManager() {
        mainPane = new Pane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createBackground();
        createStartButton();
        mainStage.getIcons().add(new Image(ICON_PATH));
        mainStage.setTitle(WINDOW_TITLE);
        addLogo();
    }

    private void addLogo() {
        Image logoImage = new Image(getClass().getResourceAsStream(LOGO_PATH));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setLayoutX(10); // Set the X position of the logo
        logoImageView.setLayoutY(10); // Set the Y position of the logo
        mainPane.getChildren().add(logoImageView);
    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void createBackground() {
        String imagePath = "/Source/Background.jpg";
        Image backgroundImage = new Image(imagePath, 900, 700, false, false);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
    }

    private void createStartButton() {
        ButtonStyle startButton = new ButtonStyle("Start");
        Tooltip startTooltip = new Tooltip("Click to start the game");
        Tooltip.install(startButton, startTooltip);
        startButton.setOnAction(event -> {
            goToNextScene();
        });
        startButton.setLayoutX((WIDTH - 190) / 2);
        startButton.setLayoutY(HEIGHT - 90);

        mainPane.getChildren().addAll(startButton);
    }

    private List<String> playerNames = new ArrayList<>();

private void goToNextScene() {
    // Create a new pane for the next scene
    Pane nextPane = new Pane();
    String imagePath = "/Source/Background.jpg";

    // Set the background image for the next pane
    Image backgroundImage = new Image(getClass().getResourceAsStream(imagePath), 900, 700, false, false);
    BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
    nextPane.setBackground(new Background(background));

    // Create buttons for selecting game mode
    ButtonStyle easyButton = new ButtonStyle("Easy");
    ButtonStyle hardButton = new ButtonStyle("Hard");
    ButtonStyle gameInstructionButton = new ButtonStyle("Instructions");

    // Set the positions of the buttons
    easyButton.setLayoutX((WIDTH - 300) / 2);
    easyButton.setLayoutY(HEIGHT - 250);
    hardButton.setLayoutX((WIDTH - 300) / 2);
    hardButton.setLayoutY(HEIGHT - 200);
    gameInstructionButton.setLayoutX((WIDTH - 300) / 2);
    gameInstructionButton.setLayoutY(HEIGHT - 150);

    // Create a text field for player name
    TextField playerNameField = new TextField();
    playerNameField.setPromptText("Enter your name");
    playerNameField.setLayoutX((WIDTH - 190) / 2);
    playerNameField.setLayoutY(HEIGHT - 300);

    // Add event handlers to the buttons
    easyButton.setOnAction(event -> {
        // Handle easy mode selection
        String playerName = playerNameField.getText();
        if (playerName.isEmpty()) {
            showNotification("Please enter a name to start the game.");
        } else {
            playerNames.add(playerName);
            // Launch the inGame class
            inGame game = new inGame();
            game.setViewManager(this); // Set the ViewManager instance
            try {
                game.start(mainStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    hardButton.setOnAction(event -> {
        // Handle hard mode selection
        String playerName = playerNameField.getText();
        if (playerName.isEmpty()) {
            showNotification("Please enter a name to start the game.");
        } else {
            playerNames.add(playerName);
            // Add logic for hard mode if necessary
        }
    });

    gameInstructionButton.setOnAction(event -> {
        // Show game instructions
        GameInstructionPane();
    });

    // Add buttons and text field to the pane
    nextPane.getChildren().addAll(easyButton, hardButton, playerNameField, gameInstructionButton);

    // Create and set the next scene with the new pane
    Scene nextScene = new Scene(nextPane, WIDTH, HEIGHT);
    mainStage.setScene(nextScene);
}
    private void GameInstructionPane() {
        // Create a new pane for the next scene
        Pane nextPane = new Pane();
        String imagePath = "/Source/Background.jpg";

        // Set the background image for the next pane
        Image backgroundImage = new Image(getClass().getResourceAsStream(
                imagePath), 900, 700, false, false);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, null);
        nextPane.setBackground(new Background(background));

        // Create a gray transparent box
        Rectangle grayBox = new Rectangle(150, 100, 600, 400);
        grayBox.setFill(Color.rgb(128, 128, 128, 0.5)); // Gray with 50% transparency

        // Create a label for the game instructions
        Label instructions = new Label("Game Instructions:\n- Press 'Start' to begin\n- Select 'Easy' or 'Hard' mode\n- Defend the city from Titans");
        instructions.setFont(Font.font("Arial", 18));
        instructions.setTextFill(Color.WHITE);
        instructions.setWrapText(true);

        // Create a stack pane to center the instructions label
        StackPane centerPane = new StackPane();
        centerPane.setLayoutX((WIDTH - 600) / 2);
        centerPane.setLayoutY((HEIGHT - 400) / 2);
        centerPane.getChildren().add(instructions);

        // Create a button to go back to the main scene
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> goToNextScene());
        backButton.setLayoutX((WIDTH - 100) / 2);
        backButton.setLayoutY(HEIGHT - 100);

        // Add the gray box, centered instructions, and the back button to the next pane
        nextPane.getChildren().addAll(grayBox, centerPane, backButton);

        // Create and set the next scene with the new pane
        Scene nextScene = new Scene(nextPane, WIDTH, HEIGHT);
        mainStage.setScene(nextScene);
    }

    private void showNotification(String message) {
        Stage notificationStage = new Stage();
        notificationStage.initOwner(mainStage);
        notificationStage.setTitle("Notification");

        StackPane notificationPane = new StackPane();
        notificationPane.setStyle("-fx-background-color: lightyellow; -fx-border-color: black; -fx-padding: 10;");

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setTextFill(Color.BLACK);

        notificationPane.getChildren().add(messageLabel);

        Scene notificationScene = new Scene(notificationPane, 300, 100);
        notificationStage.setScene(notificationScene);

        // Show the notification
        notificationStage.show();

        // Fade in and fade out the notification
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), notificationPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), notificationPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(2));

        fadeOut.setOnFinished(event -> notificationStage.close());

        fadeIn.play();
        fadeOut.play();
    }
    public void returnToStartScene() {
        mainStage.setScene(mainScene);
    }
}

package game.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import game.engine.Battle;
import game.engine.lanes.Lane;
import game.engine.weapons.PiercingCannon;
import game.engine.weapons.SniperCannon;
import game.engine.weapons.VolleySpreadCannon;
import game.engine.weapons.WallTrap;
import game.engine.weapons.Weapon;
import game.engine.weapons.WeaponRegistry;
import game.engine.weapons.factory.FactoryResponse;
import game.engine.weapons.factory.WeaponFactory;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Tile extends Application {
	VBox weaponBoxContainer = new VBox();
    private String selectedWeapon;
    private int[] purchasedWeapons; // Array to store purchased weapon codes
    private Label resourcesLabel = new Label(); // Label to display the resources gathered
    private WeaponFactory weaponFactory;
    private GridPane gridPane = new GridPane();
    private Battle battle;
    private Stage primaryStage;  // Declare primaryStage as a field
   private WeaponRegistry weaponRegistry;
   private Lane lane;
   private  FactoryResponse factoryResponse;
   private StackPane rootPane;
   private StackPane mainPane;
   private StackPane additionalPane;

   
   // Initialize your Battle instance
//   WeaponShop weaponShop = new WeaponShop(battle); // Pass the Battle instance to WeaponShop

    private int getResourcesGatheredFromBattle() {
        return battle != null ? battle.getResourcesGathered() : 100;
    }
    public Tile() {
        // Initialize weaponRegistry here, possibly by creating a new instance
        this.weaponRegistry = new WeaponRegistry(0, 0);
    }

//
//    public WeaponShop(Battle battle) {
//        this.battle = battle;
////        int numofturns=battle.getNumberOfTurns();
////        int score = battle.getScore();
////        int spanTitandistance=battle.getTitanSpawnDistance();
////        int approach=battle.();
//    }

    private void updateResourcesLabel() {
        int resources = getResourcesGatheredFromBattle();
        resourcesLabel.setText("Resources Gathered: " + resources);
        resourcesLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        resourcesLabel.setLayoutX(10);
        resourcesLabel.setLayoutY(10);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
//        WeaponShop weaponShop = new WeaponShop(battle);
        rootPane = new StackPane();
        mainPane = createMainPane();
        additionalPane = createAdditionalPane();

        rootPane.getChildren().add(mainPane);
        Pane root = new Pane();
        root.setPrefSize(1900, 900); // Set preferred size for the root Pane
        //root.getChildren().addAll(gridPane);
        resourcesLabel.setText("LABEL");
        resourcesLabel.setTextFill(Color.BEIGE);
        resourcesLabel.setLayoutX(1); // Set the x position of the resourcesLabel
        resourcesLabel.setLayoutY(0);
        Label test= new Label("TEST LABEL");
        test.setTextFill(Color.BEIGE);
        
        test.setTranslateX(500);
        
    	

        // Add the resources label to the pane
        root.getChildren().addAll( test,resourcesLabel);
        Scene scene = new Scene(root, 1900, 900);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/Source/styles.css").toExternalForm());
        loadBackground(); // Load background after setting the scene
        initialize();

        createWeaponGrid(); // Create weapon grid after setting the scene

       // updateResourcesLabel(); // Update the resources label text
       // resourcesLabel.setLayoutX(10); // Set the x position of the resourcesLabel
       // resourcesLabel.setLayoutY(880); // Set the y position of the resourcesLabel

        primaryStage.setTitle("Weapon Shop");
        primaryStage.setWidth(1800);
        primaryStage.setHeight(800);
        primaryStage.show();
    }

    private void initialize() {
        try {
            weaponFactory = new WeaponFactory();
            purchasedWeapons = new int[4];
            battle = new Battle(1, 0, 5, 3, 250); // Initialize the battle instance
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void loadBackground() {
        Image backgroundImage = new Image("/Source/WeaponShopbg.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1800);
        backgroundImageView.setFitHeight(900);

        Pane root = new Pane();
        root.getChildren().add(backgroundImageView);

        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
    }

    private void createWeaponGrid() {
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(50));
        gridPane.setHgap(50);
        gridPane.setVgap(50);
        gridPane.setLayoutX(150);
        gridPane.setLayoutY(150);
        // Add column constraints to push the images to the right
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.CENTER);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().addAll(column1, column2);
    

        int col = 0;
        for (int i = 1; i < 5; i++) {
            String imagePath = "/Source/WeaponsShopSelection/" + i + ".png";
            Image weaponImage = new Image(getClass().getResourceAsStream(imagePath));
            ImageView weaponImageView = new ImageView(weaponImage);
            weaponImageView.setFitWidth(200);
            weaponImageView.setFitHeight(200);

            GridPane weaponDetails = createWeaponDetailsPane(i, weaponImageView);

            Button buyButton = createBuyButton(i); // Modify createBuyButton method if it doesn't need the Lane parameter
            gridPane.add(buyButton, col, 0); // Add the buy button underneath the image
            gridPane.add(weaponImageView, col, 1);
            gridPane.add(weaponDetails, col, 2);
            col += 2;
        }
      Rectangle rectangle = new Rectangle(100, 100);
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);
        rectangle.setFill(Color.BLANCHEDALMOND);
        gridPane.add(rectangle, 9, 0);
        
        Label test= new Label("TEST LABEL");
        test.setTextFill(Color.BEIGE);
        
        test.setTranslateX(500);
        
   
        Scene scene = primaryStage.getScene();
        Pane root = (Pane) scene.getRoot();
        
        root.getChildren().add(gridPane);
    }
    private GridPane createWeaponDetailsPane(int weaponIndex, ImageView weaponImageView) {
        GridPane weaponDetails = new GridPane();
        weaponDetails.setPadding(new Insets(10));
        weaponDetails.setHgap(20);
        weaponDetails.setVgap(20);
        weaponDetails.setBackground(
                new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        weaponDetails.setVisible(false);
        WeaponRegistry weaponRegistry = weaponFactory.getWeaponShop().get(weaponIndex);
        Weapon weapon = weaponRegistry.buildWeapon();

        ScaleTransition scaleInTransition = new ScaleTransition(Duration.millis(200), weaponImageView);
        scaleInTransition.setToX(1.5);
        scaleInTransition.setToY(1.5);
        scaleInTransition.setAutoReverse(true);
        ScaleTransition scaleOutTransition = new ScaleTransition(Duration.millis(200), weaponImageView);
        scaleOutTransition.setToX(1.0);
        scaleOutTransition.setToY(1.0);
        scaleOutTransition.setAutoReverse(true);

        // Check if the player has enough resources to buy the weapon
   
        weaponImageView.setOnMouseEntered(event -> {
            weaponImageView.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-background-radius: 10;");
            weaponDetails.getChildren().clear(); // Clear previous details
            addWeaponDetails(weaponDetails, weaponRegistry, weapon); // Add weapon details with white font color
            weaponDetails.setLayoutX(weaponImageView.getLayoutX() + weaponImageView.getFitWidth() + 10);
            weaponDetails.setLayoutY(weaponImageView.getLayoutY());
            weaponDetails.setVisible(true); // Show details on hover
            scaleInTransition.playFromStart();
        });

        weaponImageView.setOnMouseExited(event -> {
            weaponImageView.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-background-radius: 10;");
            weaponDetails.setVisible(false); // Hide details on mouse exit
            scaleOutTransition.playFromStart();
        });
        return weaponDetails;
    }






//    privaste Map<Integer, String> weaponImageMap = new HashMap<>();
//    WeaponShop weaponShop = new WeaponShop(battle);

    
//    private void initializeWeaponImageMap() {
//        // Populate the map with mappings of weapon indexes to image paths
//        weaponImageMap.put(1, "/Source/WeaponsShopSelection/1.png");
//        weaponImageMap.put(2, "/Source/WeaponsShopSelection/2.png");
//        weaponImageMap.put(3, "/Source/WeaponsShopSelection/3.png");
//        weaponImageMap.put(4, "/Source/WeaponsShopSelection/4.png");}
//    private ArrayList<FactoryResponse> boughtWeapons = new ArrayList<>();

    private Button createBuyButton(int weaponIndex) {
        int col = 0;

        int weaponPrice = weaponFactory.getWeaponShop().get(weaponIndex).getPrice();
        Button buyButton = new Button("Buy ($" + weaponPrice + ")");
        buyButton.getStyleClass().add("buy-button");
        buyButton.setOnAction(event -> {
            try {
                if (lane != null) {
                   
                	Weapon weapon = weaponRegistry.buildWeapon();
                	lane.addWeapon(weapon);
                	System.out.println("Weapon added to lane: " + weapon);
                    // Add the purchased weapon to the selected lane
                    updateResourcesLabel();
                } else {
                    showAlert("No lane selected. Please select a lane.");
                }
            } catch (Exception e) {
                showAlert("An unexpected error occurred. Weapon purchase failed.");
                e.printStackTrace();
            }

        });

        // Create a dropdown menu for selecting a lane
        VBox laneSelectionBox = new VBox();
        Label selectLaneLabel = new Label("Select a Lane:");
        ComboBox<Lane> laneComboBox = new ComboBox<>();
        laneComboBox.getItems().addAll(battle.getLanes()); // Assuming getLanes() returns a list of lanes
        laneComboBox.setOnAction(e -> {
            lane = laneComboBox.getValue();
        });
        laneSelectionBox.getChildren().addAll(selectLaneLabel, laneComboBox);

        VBox container = new VBox();
        container.getChildren().addAll(buyButton, laneSelectionBox);
        gridPane.add(container, col, 0); // Add the buy button and lane selection box
        col += 2;

        return buyButton;
    }



//
//    private void showAlert(String message) {
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle("Purchase Information");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }

private Map<Integer, String> purchasedWeaponImages = new HashMap<>();

//private void displayBoughtWeapon(FactoryResponse response) {
//    Weapon weapon = response.getWeapon();
//    String imagePath = weaponImageMap.get(response.getWeapon());
//    Image weaponImage = new Image(getClass().getResourceAsStream(imagePath));
//    ImageView weaponImageView = new ImageView(weaponImage);
//    weaponImageView.setFitWidth(50);
//    weaponImageView.setFitHeight(50);
//    weaponBoxContainer.getChildren().add(weaponImageView);
//
//    // Add the weapon image path to the map
//    purchasedWeaponImages.put(response.getWeapon(), imagePath);
//
//    // Add the weapon image to the grid pane
//    gridPane.add(weaponImageView, 0, gridPane.getRowCount());
//
//    // Create a scale transition for the drop animation
//    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), weaponImageView);
//    scaleTransition.setToX(0.5);
//    scaleTransition.setToY(0.5);
//    scaleTransition.setCycleCount(2);
//    scaleTransition.setAutoReverse(true);
//    scaleTransition.play();
//
//    // Print the bought weapon in the console
//    System.out.println("Bought Weapon: " + weapon);
//    updateResourcesLabel();
//}


    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Purchase Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addWeaponDetails(GridPane weaponDetails, WeaponRegistry weaponRegistry, Weapon weapon) {
        String type = "";
        if (weapon instanceof PiercingCannon) {
            type = "Piercing Cannon";
        } else if (weapon instanceof SniperCannon) {
            type = "Sniper Cannon";
        } else if (weapon instanceof VolleySpreadCannon) {
            type = "Volley Spread Cannon";
        } else if (weapon instanceof WallTrap) {
            type = "Wall Trap";
        } else {
            type = "Unknown";
        }

        Label nameLabel = new Label("Name: " + weaponRegistry.getName());
        Label typeLabel = new Label("Type: " + type);
        Label damageLabel = new Label("Damage: " + weaponRegistry.getDamage());
        Label priceLabel = new Label("Price: $" + weaponRegistry.getPrice());

        if (weapon instanceof VolleySpreadCannon) {
            VolleySpreadCannon volleySpreadCannon = (VolleySpreadCannon) weapon;
            Label maxRangeLabel = new Label("Max Range: " + volleySpreadCannon.getMaxRange());
            Label minRangeLabel = new Label("Min Range: " + volleySpreadCannon.getMinRange());
            weaponDetails.add(maxRangeLabel, 0, 4);
            weaponDetails.add(minRangeLabel, 0, 5);
            minRangeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
            maxRangeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        }

        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        typeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        damageLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

        weaponDetails.add(nameLabel, 0, 0);
        weaponDetails.add(typeLabel, 0, 1);
        weaponDetails.add(damageLabel, 0, 2);
        weaponDetails.add(priceLabel, 0, 3);
    }
// // Assuming displayBoughtWeapon method is uncommented and modified to utilize purchasedWeaponImages map
//    private void displayBoughtWeapon(FactoryResponse response) {
//        Weapon weapon = response.getWeapon();
//        String imagePath = "/Source/WeaponsShopSelection/" + response.getWeapon().getCode() + ".png";
//        Image weaponImage = new Image(getClass().getResourceAsStream(imagePath));
//        ImageView weaponImageView = new ImageView(weaponImage);
//        weaponImageView.setFitWidth(50);
//        weaponImageView.setFitHeight(50);
//        weaponBoxContainer.getChildren().add(weaponImageView);
//
//        // Add the weapon image path to the map
//        purchasedWeaponImages.put(response.getWeapon().(), imagePath);
//
//        // Add the weapon image to the grid pane
//        gridPane.add(weaponImageView, 0, gridPane.getRowCount());
//
//        // Create a scale transition for the drop animation
//        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), weaponImageView);
//        scaleTransition.setToX(0.5);
//        scaleTransition.setToY(0.5);
//        scaleTransition.setCycleCount(2);
//        scaleTransition.setAutoReverse(true);
//        scaleTransition.play();
//
//        // Print the bought weapon in the console
//        System.out.println("Bought Weapon: " + weapon);
//        updateResourcesLabel();
//    }
    private StackPane createMainPane() {
        StackPane mainPane = new StackPane();
        mainPane.setPadding(new Insets(10));
        mainPane.getChildren().addAll(
                new Label("Main Weapon Shop Pane"),
                createGoToAdditionalPaneButton()
        );
        return mainPane;
    }
    private Button createGoToAdditionalPaneButton() {
        Button button = new Button("Go to Additional Pane");
        button.setOnAction(event -> {
            rootPane.getChildren().remove(mainPane);
            rootPane.getChildren().add(additionalPane);
        });
        return button;
    }

    private StackPane createAdditionalPane() {
    	
        StackPane additionalPane = new StackPane();
        additionalPane.setPadding(new Insets(10));
        additionalPane.getChildren().addAll(
                new Label("Additional Pane"),
                createGoBackButton()
        );
        additionalPane.setVisible(false);
        return additionalPane;
    }
    private Button createGoBackButton() {
        Button button = new Button("Go Back");
        button.setOnAction(event -> {
            rootPane.getChildren().remove(additionalPane);
            rootPane.getChildren().add(mainPane);
        });
        return button;
    }


    public static void main(String[] args) {
    	
        launch(args);
    }
}

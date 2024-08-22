	package game.gui;
	
	import java.io.IOException;
	import java.io.InputStream;
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.Collection;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	
	import game.engine.Battle;
	import game.engine.base.Wall;
	import game.engine.lanes.Lane;
	import game.engine.titans.AbnormalTitan;
	import game.engine.titans.ArmoredTitan;
	import game.engine.titans.ColossalTitan;
	import game.engine.titans.PureTitan;
	import game.engine.titans.Titan;
	import game.engine.titans.TitanRegistry;
	import game.engine.weapons.PiercingCannon;
	import game.engine.weapons.SniperCannon;
	import game.engine.weapons.VolleySpreadCannon;
	import game.engine.weapons.WallTrap;
	import game.engine.weapons.Weapon;
	import game.engine.weapons.WeaponRegistry;
	import game.engine.weapons.factory.FactoryResponse;
	import game.engine.weapons.factory.WeaponFactory;
	import javafx.animation.FadeTransition;
	import javafx.animation.ScaleTransition;
	import javafx.application.Application;
	import javafx.geometry.HPos;
	import javafx.geometry.Insets;
	import javafx.geometry.Pos;
	import javafx.scene.Scene;
	import javafx.scene.control.Alert;
	import javafx.scene.control.Alert.AlertType;
	import javafx.scene.control.Button;
	import javafx.scene.control.ComboBox;
	import javafx.scene.control.ContentDisplay;
	import javafx.scene.control.Label;
	import javafx.scene.control.ListCell;
	import javafx.scene.control.ListView;
	import javafx.scene.image.Image;
	import javafx.scene.image.ImageView;
	import javafx.scene.layout.Background;
	import javafx.scene.layout.BackgroundFill;
	import javafx.scene.layout.ColumnConstraints;
	import javafx.scene.layout.CornerRadii;
	import javafx.scene.layout.GridPane;
	import javafx.scene.layout.HBox;
	import javafx.scene.layout.Pane;
	import javafx.scene.layout.StackPane;
	import javafx.scene.layout.VBox;
	import javafx.scene.paint.Color;
	import javafx.stage.Modality;
	import javafx.stage.Stage;
	import javafx.util.Duration;
	import javafx.util.Pair;
	
	public class inGame extends Application {
		private Wall wall;
		private Battle easyGame;
		private static final int HEIGHT = 700;
		private static final int WIDTH = 900;
		VBox weaponBoxContainer = new VBox();
		private String selectedWeapon;
		private int[] purchasedWeapons; // Array to store purchased weapon codes
		private Label resourcesLabel = new Label(); // Label to display the resources gathered
		private WeaponFactory weaponFactory;
		private GridPane gridPane = new GridPane();
		private Stage primaryStage; // Declare primaryStage as a field
		private WeaponRegistry weaponRegistry;
		private Lane lane;
		private FactoryResponse factoryResponse;
		private StackPane rootPane;
		private StackPane mainPane;
		private StackPane additionalPane;
		private Map<Wall, StackPane> laneDisplayMap;
		private VBox lanesContainer;
	    private ViewManager viewManager; // Add ViewManager reference
	
		// Initialize the map with empty lists for each lane
		Map<Lane, List<Weapon>> laneWeaponsMap = new HashMap<>();
	
		public inGame() {
			this.weaponRegistry = new WeaponRegistry(0, 0);
			laneDisplayMap = new HashMap<>();
			lanesContainer = new VBox();
			try {
				this.weaponFactory = new WeaponFactory(); // Initialize weaponFactory
			} catch (IOException e) {
				System.err.println("Error initializing weaponFactory: " + e.getMessage());
				// Handle the exception as needed
			}
		}
	
		@Override
		public void start(Stage primaryStage) {
			this.primaryStage = primaryStage; // Assign primaryStage to the field
			GridPane root = new GridPane();
			root.setPadding(new Insets(10));
			root.setHgap(10);
			root.setVgap(10);
			mainPane = new StackPane();
	
			wall = new Wall(100);
			try {
				easyGame = new Battle(1, 0, 0, 3, 250); // Initialize easyGame
			} catch (IOException e) {
				System.err.println("Error initializing easyGame: " + e.getMessage());
				// Handle the exception as needed
			}
	
			// Create dummy Lane objects for the example
			Lane lane1 = new Lane(new Wall(wall.getBaseHealth()));
			Lane lane2 = new Lane(new Wall(wall.getBaseHealth()));
			Lane lane3 = new Lane(new Wall(wall.getBaseHealth()));
	
			// Add titans to the lanes
			easyGame.getBattlePhase();
			for (Titan titan : easyGame.getApproachingTitans()) {
				switch (easyGame.getNumberOfTitansPerTurn()) {
				case 1:
					lane1.addTitan(titan);
					break;
				case 2:
					lane2.addTitan(titan);
					break;
				case 3:
					lane3.addTitan(titan);
					break;
				default:
					// Handle invalid lane number
					break;
				}
			}
			// Create your JavaFX components here
			Collection<Lane> lanesCollection = easyGame.getLanes();
			List<Lane> lanes = new ArrayList<>(lanesCollection);
	
			for (int i = 0; i < lanes.size(); i++) {
				Lane currentLane = lanes.get(i);
				System.out.println(currentLane);
				StackPane lanePane = createLane(currentLane);
				root.add(lanePane, 0, i);
			}
	
			Button someOtherButton = new Button("Weapon Shop");
			someOtherButton.setOnAction(event -> {
				shoprun();
			});
			Button passTurnButton = new Button("Pass Turn");
			passTurnButton.setOnAction(event -> {
				easyGame.passTurn();
			});
			// Add a button to go back to the start scene
	        Button backButton = new Button("Back to Start");
	        backButton.setOnAction(event -> {
	            if (viewManager != null) {
	                viewManager.returnToStartScene();
	            }
	        });
			root.add(someOtherButton, 0, 3);
			root.add(passTurnButton, 0, 5);
	        root.add(backButton, 0, 6);
	
			Scene scene = new Scene(root, 1500, 800);
			primaryStage.setTitle("Lane Display Example");
			primaryStage.setScene(scene);
			primaryStage.show();
	
			// Call createWeaponGrid after the scene is shown
		}
		 public void setViewManager(ViewManager viewManager) {
		        this.viewManager = viewManager;
		    }
	
		private void initialize() {
			try {
				weaponRegistry = new WeaponRegistry(0, 0);
				easyGame = new Battle(1, 0, 0, 3, 25); // Initialize easyGame
			} catch (IOException e) {
				System.err.println("Error initializing easyGame: " + e.getMessage());
				// Handle the exception as needed
			}
	
		}
	
		private void createWeaponGrid() {
			gridPane = new GridPane();
			gridPane.setPadding(new Insets(20));
			gridPane.setHgap(50);
			gridPane.setVgap(50);
			gridPane.setLayoutX(100); // Move the gridPane to the right
			gridPane.setLayoutY(100);
	
			// Add column constraints to push the images to the right
			ColumnConstraints column1 = new ColumnConstraints();
			column1.setHalignment(HPos.CENTER);
			ColumnConstraints column2 = new ColumnConstraints();
			column2.setHalignment(HPos.CENTER);
			gridPane.getColumnConstraints().addAll(column1, column2);
	
			int col = 0;
			for (int i = 0; i < 4; i++) {
				String imagePath = "/Source/WeaponsShopSelection/" + (i + 1) + ".png"; // Adjust the image path index
				Image weaponImage = new Image(getClass().getResourceAsStream(imagePath));
				ImageView weaponImageView = new ImageView(weaponImage);
				weaponImageView.setFitWidth(150);
				weaponImageView.setFitHeight(150);
	
				GridPane weaponDetails = createWeaponDetailsPane(i + 1, weaponImageView); // Pass (i + 1) as the weapon
																							// index
	
				Pair<Button, Weapon> buyButton = createBuyButton(i + 1); // Pass (i + 1) as the weapon index
				VBox container = new VBox();
				container.getChildren().addAll(buyButton.getKey(), weaponImageView, weaponDetails); // Add image, details,
																									// and button to a
																									// container
				container.setAlignment(Pos.TOP_RIGHT); // Center the elements in the container
				gridPane.add(container, col, 3);
	
				col += 1;
			}
	
			// Create a back button
			Button backButton = new Button("Back to Game");
			backButton.setOnAction(event -> backToGame());
	
			Scene scene = primaryStage.getScene();
			Pane root = (Pane) scene.getRoot();
			root.getChildren().add(gridPane);
			// Add lane selection ComboBox to the scene
			root.getChildren().add(backButton);
			VBox laneSelectionBox = new VBox();
			Label selectLaneLabel = new Label("Select a Lane:");
			selectLaneLabel.setTextFill(Color.WHITE);
	
			ComboBox<Lane> laneComboBox = new ComboBox<>();
			laneComboBox.getItems().addAll(easyGame.getLanes()); // Assuming getLanes() returns a list of lanes
	
			// Add a listener to the itemsProperty
			laneComboBox.itemsProperty().addListener((obs, oldItems, newItems) -> {
				System.out.println("Lanes:");
				for (Lane lane : newItems) {
					System.out.println("- " + lane.toString()); // Customize the output as needed
				}
			});
	
			laneComboBox.setOnAction(e -> {
				lane = laneComboBox.getValue();
			});
	
			laneSelectionBox.getChildren().addAll(selectLaneLabel, laneComboBox);
			laneSelectionBox.setLayoutX(150); // Adjust the layout as needed
			laneSelectionBox.setLayoutY(50); // Adjust the layout as needed
			root.getChildren().add(laneSelectionBox);
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
	
		// Method to get the weapons for a specific lane
		public List<Weapon> getWeaponsForLane(Lane lane) {
			return laneWeaponsMap.get(lane);
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
	
		private Label shopLabel; // Declare shopLabel as a class variable
	
		private List<Integer> purchasedWeaponsIndices = new ArrayList<>();
	
		// Define separate ArrayLists for each lane's bought weapons
		private final ArrayList<ArrayList<Weapon>> boughtWeaponsForLanes = new ArrayList<>(
				Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
	
public Pair<Button, Weapon> createBuyButton(int weaponIndex) {
    int col = 0;
    if (shopLabel == null) {
        shopLabel = new Label("Resources: " + easyGame.getResourcesGathered());
        shopLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        shopLabel.setAlignment(Pos.BOTTOM_CENTER);
        gridPane.add(shopLabel, 0, 0, 2, 1);
    }

    int weaponPrice = weaponFactory.getWeaponShop().get(weaponIndex).getPrice();
    Button buyButton = new Button("Buy ($" + weaponPrice + ")");
    buyButton.getStyleClass().add("buy-button");
    Weapon weapon = weaponFactory.getWeaponShop().get(weaponIndex).buildWeapon();
    buyButton.setOnAction(event -> {
        try {
            if (lane != null) {
                lane.addWeapon(weapon);
                System.out.println("Weapon added to lane: " + weapon);

                easyGame.setResourcesGathered(easyGame.getResourcesGathered() - weaponPrice);
                int resources = easyGame.getResourcesGathered();
                shopLabel.setText("Resources: " + resources);

                // Store the purchased weapon index
                purchasedWeaponsIndices.add(weaponIndex);

                // Print the list of purchased weapons
                System.out.println("List of purchased weapons:");
                for (Integer index : purchasedWeaponsIndices) {
                    WeaponRegistry registry = weaponFactory.getWeaponShop().get(index);
                    System.out.println("- " + registry.getName());
                }

                // Show a notification for successful purchase
                showNotification("Weapon bought successfully!");

            } else {
                showNotification("No lane selected. Please select a lane.");
            }
        } catch (Exception e) {
            showAlert("An unexpected error occurred. Weapon purchase failed.");
            e.printStackTrace();
        }
    });

    VBox container = new VBox();
    container.getChildren().add(buyButton); // Add only the buy button to the container

    gridPane.add(container, col, 0);

    col += 2;

    return new Pair<>(buyButton, weapon);
}
	private void showNotification(String message) {
	    Stage notificationStage = new Stage();
	    notificationStage.initOwner(primaryStage);
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
	
		// private ArrayList<Weapon> getBoughtWeaponsForLane(int laneIndex) {
		// ArrayList<Weapon> boughtWeapons = new ArrayList<>();
		// for (Integer index : boughtWeaponsForLanes.get(laneIndex)) {
		// WeaponRegistry registry = weaponFactory.getWeaponShop().get(index);
		// Weapon weapon = registry.buildWeapon();
		// boughtWeapons.add(weapon);
		// }
		// return boughtWeapons;
		// }
	//private ArrayList<Weapon> getBoughtWeaponsForLane(int laneIndex) {
	//    ArrayList<Weapon> boughtWeapons = new ArrayList<>();
	//    for (Integer index : boughtWeaponsForLanes.get(laneIndex)) {
	//        WeaponRegistry registry = weaponFactory.getWeaponShop().get(index);
	//        Weapon weapon = registry.buildWeapon();
	//        boughtWeapons.add(weapon);
	//    }
	//    return boughtWeapons;
	//}
	
		private ListView<WeaponRegistry> weaponsListView; // Declare as a class-level variable
		// Method to add a weapon to a specific lane
	
		public void addWeaponToLane(Lane lane, Weapon weapon) {
			List<Weapon> weapons = laneWeaponsMap.get(lane);
			weapons.add(weapon);
		}
	
		private void createWeaponsListView() {
			weaponsListView = new ListView<>();
			for (Integer index : purchasedWeaponsIndices) {
				WeaponRegistry registry = weaponFactory.getWeaponShop().get(index);
				weaponsListView.getItems().add(registry);
			}
			weaponsListView.getStyleClass().add("list-view");
	
			// Customize the cell factory to include images
			weaponsListView.setCellFactory(param -> new ListCell<WeaponRegistry>() {
				private final ImageView imageView = new ImageView();
				{
					setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				}
	
				@Override
				protected void updateItem(WeaponRegistry item, boolean empty) {
					super.updateItem(item, empty);
	
					if (empty || item == null) {
						setText(null);
						setGraphic(null);
					} else {
						setText(getWeaponName(item.buildWeapon()));
						String imagePath = "/Source/WeaponsShopSelection/" + getWeaponCode(item.buildWeapon()) + ".png";
						System.out.println("Loading image from path: " + imagePath);
						InputStream imageStream = getClass().getResourceAsStream(imagePath);
						if (imageStream == null) {
							System.err.println("Image not found at path: " + imagePath + ". Using default image.");
							imageStream = getClass().getResourceAsStream("/Source/WeaponsShopSelection/default.png");
						}
						if (imageStream != null) {
							Image image = new Image(imageStream);
							imageView.setImage(image);
							imageView.setFitWidth(20);
							imageView.setFitHeight(20);
							setGraphic(imageView);
						} else {
							System.err.println("Default image not found. Unable to load image.");
						}
					}
				}
			});
		}
	
		public void refreshLaneDisplay(Lane lane) {
			StackPane lanePane = createLane(lane);
			laneDisplayMap.put(lane.getLaneWall(), lanePane);
	
			// Find the lane's current index in the container and replace the old StackPane
			int index = findLaneIndex(lane.getLaneWall());
			if (index >= 0) {
				lanesContainer.getChildren().set(index, lanePane);
			} else {
				lanesContainer.getChildren().add(lanePane);
			}
		}
	
		private int findLaneIndex(Wall wall) {
			for (int i = 0; i < lanesContainer.getChildren().size(); i++) {
				StackPane stackPane = (StackPane) lanesContainer.getChildren().get(i);
				if (laneDisplayMap.get(wall) == stackPane) {
					return i;
				}
			}
			return -1;
		}
	
		private void chooseWeaponFromList(Lane laneData, VBox laneContainer) {
			// Create a new window or dialog to display the list of purchased weapons
			Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
	
			createWeaponsListView(); // Initialize weaponsListView
	
			// Add a button to confirm the selection
			Button selectButton = new Button("Select");
			selectButton.getStyleClass().add("dialog-button");
			selectButton.setOnAction(event -> {
				// Get the selected weapon
				WeaponRegistry selectedRegistry = weaponsListView.getSelectionModel().getSelectedItem();
				if (selectedRegistry != null) {
					Weapon selectedWeapon = selectedRegistry.buildWeapon();
					laneData.addWeapon(selectedWeapon);
					System.out.println("Selected weapon added to lane: " + selectedRegistry.getName());
	
					// Update the lane display in the GUI with the selected weapon's image
					String imagePath = "/Source/WeaponsShopSelection/" + selectedRegistry.getCode() + ".png";
					try {
						InputStream imageStream = getClass().getResourceAsStream(imagePath);
						if (imageStream != null) {
							Image image = new Image(imageStream);
							ImageView selectedWeaponImageView = new ImageView(image);
							selectedWeaponImageView.setFitWidth(50);
							selectedWeaponImageView.setFitHeight(50);
							laneContainer.getChildren().add(selectedWeaponImageView);
	
							// Close the dialog with fade-out animation
							FadeTransition fadeOut = new FadeTransition(Duration.millis(300), dialog.getScene().getRoot());
							fadeOut.setFromValue(1.0);
							fadeOut.setToValue(0.0);
							fadeOut.setOnFinished(e -> dialog.close());
							fadeOut.play();
						} else {
							System.err.println("Image file not found: " + imagePath);
						}
					} catch (Exception e) {
						System.err.println("Failed to load image: " + imagePath);
						e.printStackTrace();
					}
				}
			});
	
			VBox dialogVBox = new VBox(weaponsListView, selectButton);
			dialogVBox.setAlignment(Pos.CENTER);
			dialogVBox.setPadding(new Insets(10));
			dialogVBox.getStyleClass().add("dialog");
	
			// Add fade-in animation
			FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dialogVBox);
			fadeIn.setFromValue(0.0);
			fadeIn.setToValue(1.0);
			fadeIn.play();
	
			Scene dialogScene = new Scene(dialogVBox, 300, 200);
			dialog.setScene(dialogScene);
			dialog.setTitle("Choose Weapon");
			dialog.showAndWait();
		}
	
		private void showAlert(String message) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Purchase Information");
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
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
	//	private void createWeaponsListView() {
	//	    selectWeaponComboBox.getItems().clear(); // Clear existing items
	//	    for (Integer index : purchasedWeaponsIndices) {
	//	        WeaponRegistry registry = weaponFactory.getWeaponShop().get(index);
	//	        selectWeaponComboBox.getItems().add(registry.buildWeapon()); // Assuming buildWeapon() creates a new instance
	//	    }
	//	}
	
		private void backToGame() {
			// Transition back to the in-game screen
			start(primaryStage);
		}
	
		private StackPane createLane(Lane laneData) {
			VBox laneContainer = new VBox();
			laneContainer.setPrefSize(1500, 100);
			laneContainer.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
	
			Label wallHealthLabel = new Label("Wall Health: " + laneData.getLaneWall().getCurrentHealth());
			Label dangerLevelLabel = new Label("Danger Level: " + laneData.getDangerLevel());
			Label weaponsLabel = new Label("Available Weapons:");
	
			ListView<String> weaponsListView = new ListView<>();
			for (Weapon weapon : laneData.getWeapons()) {
				weaponsListView.getItems().add(getWeaponName(weapon));
			}
	
			// Add a label and ListView for purchased weapons
			Label purchasedWeaponsLabel = new Label("Purchased Weapons:");
			ListView<String> purchasedWeaponsListView = new ListView<>();
			for (Weapon weapon : laneData.getWeapons()) {
				purchasedWeaponsListView.getItems().add(getWeaponName(weapon));
			}
	
			// Add titan information
			GridPane titanGrid = new GridPane();
			titanGrid.setHgap(10);
			titanGrid.setVgap(10);
	
			int row = 0;
			for (Titan titan : laneData.getTitans()) {
				Label titanInfoLabel = new Label("Titan: " + titan.getCurrentHealth());
				Label titanHealthLabel = new Label("Health: " + titan.getDamage());
				Label titanPositionLabel = new Label("Position: " + titan.getHeightInMeters());
				Label titanSpeedLabel = new Label("Speed: " + titan.getSpeed());
	
				titanGrid.add(titanInfoLabel, 0, row);
				titanGrid.add(titanHealthLabel, 1, row);
				titanGrid.add(titanPositionLabel, 2, row);
				titanGrid.add(titanSpeedLabel, 3, row);
				row++;
			}
	
			VBox laneInfoBox = new VBox(5);
			laneInfoBox.getChildren().addAll(wallHealthLabel, dangerLevelLabel, weaponsLabel, weaponsListView,
					purchasedWeaponsLabel, purchasedWeaponsListView, titanGrid);
			laneContainer.getChildren().add(laneInfoBox);
	
			// Add a button to open the weapon selection dialog
			Button selectWeaponButton = new Button("Select Weapon");
			selectWeaponButton.setOnAction(event -> {
				chooseWeaponFromList(laneData, laneContainer);
			});
	
			VBox buttonContainer = new VBox(selectWeaponButton);
			buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);
			buttonContainer.setPadding(new Insets(5));
			laneContainer.getChildren().add(buttonContainer);
	
			StackPane stackPane = new StackPane();
			stackPane.getChildren().add(laneContainer);
			return stackPane;
		}
	
		private String getWeaponName(Weapon weapon) {
			if (weapon instanceof PiercingCannon) {
				return "Piercing Cannon";
			} else if (weapon instanceof SniperCannon) {
				return "Sniper Cannon";
			} else if (weapon instanceof VolleySpreadCannon) {
				return "Volley Spread Cannon";
			} else if (weapon instanceof WallTrap) {
				return "Wall Trap";
			} else {
				return "Unknown Weapon";
			}
		}
	
		private String getWeaponCode(Weapon weapon) {
			if (weapon instanceof PiercingCannon) {
				return "1";
			} else if (weapon instanceof SniperCannon) {
				return "2";
			} else if (weapon instanceof VolleySpreadCannon) {
				return "3";
			} else if (weapon instanceof WallTrap) {
				return "4";
			} else {
				return "default"; // Use default for unknown weapons
			}
		}
	
		private String getTitanName(Titan titan) {
			System.out.println("Getting name for titan: " + titan.getClass().getSimpleName());
			if (titan instanceof PureTitan) {
				return "Pure Titan";
			} else if (titan instanceof AbnormalTitan) {
				return "Abnormal Titan";
			} else if (titan instanceof ArmoredTitan) {
				return "Armored Titan";
			} else if (titan instanceof ColossalTitan) {
				return "Colossal Titan";
			} else {
				return "Unknown";
			}
		}
	
		private String getTitanNameFromRegistry(TitanRegistry titan) {
			int code = titan.getCode(); // Assuming getCode() returns the titan's code
			switch (code) {
			case 1:
				return "Pure Titan";
			case 2:
				return "Abnormal Titan";
			case 3:
				return "Armored Titan";
			case 4:
				return "Colossal Titan";
			default:
				return "Unknown Titan";
			}
		}
	
		private void shoprun() {
			loadBackground();
	//    Pane nextPane = new Pane();
	//    String imagePath = "/Source/WeaponShopbg.jpg";
	// // Set the background image for the next pane
	//    Image backgroundImage = new Image(getClass().getResourceAsStream(
	//            imagePath), 900, 700, false, false);
	//    BackgroundImage background = new BackgroundImage(backgroundImage,
	//            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
	//            BackgroundPosition.DEFAULT, null);
	//    nextPane.setBackground(new Background(background));
	
			initialize();
	
			// Create and set the next scene with the new pane
	//    Scene nextScene = new Scene(nextPane, 1900, 900);
	//    primaryStage.setScene(nextScene);
			createWeaponGrid(); // Create weapon grid after setting the scene
		}
	
		public static void main(String[] args) {
			launch(args);
		}
	}

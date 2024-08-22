package game.gui;

import game.engine.titans.Titan;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class TitanView extends StackPane {
    private final Titan titan;
    private final ImageView imageView;

    public TitanView(Titan titan, Image image) {
        this.titan = titan;
        this.imageView = new ImageView(image);
        this.getChildren().add(this.imageView);
    }

    public Titan getTitan() {
        return titan;
    }
}
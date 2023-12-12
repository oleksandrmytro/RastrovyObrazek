package rastrovyObrazek;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

public class Main extends Application {

    private ImageView imageView;
    private CheckBox topAnchorCheckBox;
    private CheckBox bottomAnchorCheckBox;
    private CheckBox leftAnchorCheckBox;
    private CheckBox rightAnchorCheckBox;
    private BorderPane mainLayout;
    private GridPane checkboxPanel;
    private VBox colorPanel;
    private Rectangle colorDisplay;
    private Text colorText;
    private Menu menu = new Menu("File");
    private MenuBar menuBar;

    @Override
    public void start(Stage primaryStage) {
        // create the menu

        MenuItem loadPicture = new MenuItem("Load picture...");
        MenuItem reset = new MenuItem("Reset");
        menu.getItems().addAll(loadPicture, reset);
        menuBar = new MenuBar(menu);

        // create the checkboxes
        topAnchorCheckBox = new CheckBox();
        bottomAnchorCheckBox = new CheckBox();
        leftAnchorCheckBox = new CheckBox();
        rightAnchorCheckBox = new CheckBox();

        // create the image view
        imageView = new ImageView();
        imageView.setPreserveRatio(false);
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClickOnImage);

        // create the checkbox panel with white border
        checkboxPanel = new GridPane();
        checkboxPanel.setAlignment(Pos.CENTER);
        checkboxPanel.setPrefSize(200, 250);
        checkboxPanel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 10; -fx-padding: 10;");

        checkboxPanel.add(topAnchorCheckBox, 1, 0);
        checkboxPanel.add(bottomAnchorCheckBox, 1, 2);
        checkboxPanel.add(leftAnchorCheckBox, 0, 1);
        checkboxPanel.add(rightAnchorCheckBox, 2, 1);
        checkboxPanel.setVgap(80);
        checkboxPanel.setHgap(50);

        // create the color display panel
        colorDisplay = new Rectangle(150, 150);
        colorDisplay.setStroke(Color.BLACK);
        colorDisplay.setFill(Color.web("#606060"));
        colorText = new Text();
        colorText.setFill(Color.WHITE);
        colorPanel = new VBox(10, colorDisplay, colorText);
        colorPanel.setAlignment(Pos.CENTER);
        colorPanel.setPrefSize(200, 250);
        colorPanel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 10; -fx-padding: 10;");

        // create the main layout
        VBox rightPanel = new VBox(10, checkboxPanel, colorPanel);
        rightPanel.setAlignment(Pos.CENTER);
        mainLayout = new BorderPane(imageView, menuBar, rightPanel, null, null);
        mainLayout.setPrefSize(800, 600);
        mainLayout.setStyle("-fx-background-color: #606060");

        // set up the stage
        primaryStage.setScene(new Scene(mainLayout));
        primaryStage.show();

        // add listeners to the window size
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> adjustImageView());
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> adjustImageView());

        // handle the menu items
        loadPicture.setOnAction(e -> loadPicture());
        reset.setOnAction(e -> reset());

        // handle the checkboxes
        topAnchorCheckBox.setOnAction(e -> adjustImageView());
        bottomAnchorCheckBox.setOnAction(e -> adjustImageView());
        leftAnchorCheckBox.setOnAction(e -> adjustImageView());
        rightAnchorCheckBox.setOnAction(e -> adjustImageView());
    }

    private void loadPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BMP Images", "*.bmp"),
                new FileChooser.ExtensionFilter("JPG Images", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF Images", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            reset();
            try {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                adjustImageView();
            } catch (Exception e) {
                showErrorDialog("Error loading image", "An error occurred while loading the image.");
            }
        }
    }

    private void reset() {
        imageView.setImage(null);
        topAnchorCheckBox.setSelected(false);
        bottomAnchorCheckBox.setSelected(false);
        leftAnchorCheckBox.setSelected(false);
        rightAnchorCheckBox.setSelected(false);
        colorText.setText("");
        colorDisplay.setFill(null);
    }

    private void adjustImageView() {
        if (imageView.getImage() == null) {
            return;
        }

        double imgWidth = imageView.getImage().getWidth();
        double imgHeight = imageView.getImage().getHeight();
        double paneWidth = mainLayout.getWidth() - checkboxPanel.getWidth();
        double paneHeight = mainLayout.getHeight() - menuBar.getHeight();

        if(!topAnchorCheckBox.isSelected() || !bottomAnchorCheckBox.isSelected() ||
                !leftAnchorCheckBox.isSelected() && !rightAnchorCheckBox.isSelected()) {
            imageView.setFitHeight(imgHeight);
            BorderPane.setAlignment(imageView, Pos.CENTER);
        }
        
        if (topAnchorCheckBox.isSelected() && bottomAnchorCheckBox.isSelected()) {
            imageView.setFitHeight(paneHeight);
        } else if (bottomAnchorCheckBox.isSelected()) {
            imageView.setFitHeight(imgHeight);
            BorderPane.setAlignment(imageView, Pos.BOTTOM_CENTER);
        } else if (topAnchorCheckBox.isSelected()) {
            imageView.setFitHeight(imgHeight);
            BorderPane.setAlignment(imageView, Pos.TOP_CENTER);
        }
        
        if (leftAnchorCheckBox.isSelected() && rightAnchorCheckBox.isSelected()) {
            imageView.setFitWidth(paneWidth);
        } else if (leftAnchorCheckBox.isSelected()) {
            imageView.setFitWidth(imgWidth);
            BorderPane.setAlignment(imageView, Pos.CENTER_LEFT);
        } else if (rightAnchorCheckBox.isSelected()) {
            imageView.setFitWidth(imgWidth);
            BorderPane.setAlignment(imageView, Pos.CENTER_RIGHT);
        }
        // set the image size and position based on the checkboxes
//    if (topAnchorCheckBox.isSelected() && bottomAnchorCheckBox.isSelected()) {
//        imageView.setFitHeight(paneHeight);
//        imageView.setY(0);
//    } else if (topAnchorCheckBox.isSelected()) {
//        imageView.setFitHeight(imgHeight);
//        BorderPane.setAlignment(imageView, Pos.TOP_CENTER);
//    } else if (bottomAnchorCheckBox.isSelected()) {
//        imageView.setFitHeight(imgHeight);
//        BorderPane.setAlignment(imageView, Pos.BOTTOM_CENTER);
//    } else {
//        imageView.setFitHeight(imgHeight);
//        imageView.setY((paneHeight - imgHeight) / 2);
//    }
//
//    if (leftAnchorCheckBox.isSelected() && rightAnchorCheckBox.isSelected()) {
//        imageView.setFitWidth(paneWidth);
//        imageView.setX(0);
//    } else if (leftAnchorCheckBox.isSelected()) {
//        imageView.setFitWidth(imgWidth);
//        BorderPane.setAlignment(imageView, Pos.CENTER_LEFT);
//    } else if (rightAnchorCheckBox.isSelected()) {
//        imageView.setFitWidth(imgWidth);
//        BorderPane.setAlignment(imageView, Pos.CENTER_RIGHT);
//    } else {
//        imageView.setFitWidth(imgWidth);
//        imageView.setX((paneWidth - imgWidth) / 2);
//    }
    }

    private void handleMouseClickOnImage(MouseEvent event) {
        Image image = imageView.getImage();
        if (image != null) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                Color pixelColor = image.getPixelReader().getColor(x, y);
                colorDisplay.setFill(pixelColor);
                colorText.setText(String.format("RGB (%d, %d, %d)",
                        (int) (pixelColor.getRed() * 255),
                        (int) (pixelColor.getGreen() * 255),
                        (int) (pixelColor.getBlue() * 255)));
            }
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

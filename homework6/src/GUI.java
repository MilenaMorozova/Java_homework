import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;


public class GUI {
    private Stage primaryStage;
    private Scene primaryScene;

    private TableView<MyImage> tablePrimaryScene;

    private int widthWindow;
    private int heightWindow;

    private double widthButton = 80;
    private double heightButton = 20;

    private ObservableList<MyImage> images;
    private VBox selectedItem;

    public GUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
        images = FXCollections.observableArrayList();

        widthWindow = 600;
        heightWindow = 400;

        createPrimaryScene();
    }

    private TableView<MyImage> createTableView(){
        TableView<MyImage> table = new TableView<MyImage>(images);
        table.setMaxWidth(widthWindow*0.6);

        TableColumn<MyImage, ImageView> imageColumn = new TableColumn<MyImage, ImageView>("Image");
        imageColumn.setCellValueFactory(cellData -> {
            ImageView imageView = new ImageView(SwingFXUtils.toFXImage(cellData.getValue().getSource(), null));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(60);
            imageView.setFitHeight(50);
            return new SimpleObjectProperty<>(imageView);
        });
        table.getColumns().add(imageColumn);

        TableColumn<MyImage, String> nameColumn = new TableColumn<MyImage, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<MyImage, String>("name"));
        table.getColumns().add(nameColumn);

        TableColumn<MyImage, String> widthAndHeightColumn = new TableColumn<MyImage, String>("Sizes");
        widthAndHeightColumn.setCellValueFactory(cellData -> {
            String string = cellData.getValue().getWidth() + " x " + cellData.getValue().getHeight();
            return new SimpleStringProperty(string);
        });
        table.getColumns().add(widthAndHeightColumn);

        TableColumn<MyImage, Double> sizeColumn = new TableColumn<MyImage, Double>("Size(Kb)");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<MyImage, Double>("sizeInKilobytes"));
        table.getColumns().add(sizeColumn);

        for(TableColumn<MyImage, ?> column: table.getColumns()){
            column.setStyle("-fx-alignment: CENTER");
        }

        return table;
    }

    private void createSelectedItem(){
        TableView.TableViewSelectionModel<MyImage> selectionModel = tablePrimaryScene.getSelectionModel();

        ImageView imageView = new ImageView();
        imageView.setFitHeight(0.5 * heightWindow);
        imageView.setFitWidth(0.4 * widthWindow);
        imageView.setPreserveRatio(true);

        VBox texts = new VBox(10);

        Button compareButton = new Button("Compare with...");
        compareButton.setPrefSize(widthButton * 1.5, heightButton);
        compareButton.setOnMouseClicked(mouseEvent -> createSceneForImageComparison(selectionModel.getSelectedItem()));

        selectedItem = new VBox(20, imageView, texts, compareButton);
        selectedItem.setAlignment(Pos.CENTER);
        selectedItem.setMinWidth(0.4 * widthWindow);
        selectedItem.setMinHeight(heightWindow);
        selectedItem.setVisible(false);

        selectionModel.selectedItemProperty().addListener(new ChangeListener<MyImage>(){

            @Override
            public void changed(ObservableValue<? extends MyImage> observableValue, MyImage oldImage, MyImage newImage) {
                if(newImage != null){
                    imageView.setImage(SwingFXUtils.toFXImage(newImage.getSource(), null));
                    texts.getChildren().setAll(new Text("Image name: " + newImage.getName()),
                            new Text("Sizes: " + newImage.getWidth() + " x " + newImage.getHeight()),
                            new Text("Size(Kb): " + newImage.getSizeInKilobytes()));
                    selectedItem.setVisible(true);
                }
                if(images.isEmpty()){
                    selectedItem.setVisible(false);
                }
            }
        });
    }

    private void createPrimaryScene(){
        //-------------------------top panel--------------------------------
        Button addButton = new Button("Add image");
        addButton.setPrefSize(widthButton, heightButton);
        addButton.setOnMouseClicked(mouseEvent -> addImage());

        Button removeButton = new Button("Delete image");
        removeButton.setPrefSize(widthButton, heightButton);
        removeButton.setOnMouseClicked(mouseEvent -> removeImage());

        ObservableList<String> availableValues = FXCollections.observableArrayList("Size", "Name");
        ComboBox<String> comboBox = new ComboBox<String>(availableValues);
        comboBox.setValue("-");
        comboBox.setOnAction(event -> {
            if (comboBox.getValue().equals("Size")) {
                images.sort(MyImage.SizeComparator);
            } else if (comboBox.getValue().equals("Name")) {
                images.sort(MyImage.NameComparator);
            }
        });

        HBox topPanelPrimaryScene = new HBox(10., addButton, removeButton, new Text("Sort by: "), comboBox);
        topPanelPrimaryScene.setAlignment(Pos.CENTER);
        //------------------------------------------------------------------------
        tablePrimaryScene = createTableView();

        VBox temp = new VBox(10, topPanelPrimaryScene, tablePrimaryScene);

        createSelectedItem();
        HBox root = new HBox(10, temp, selectedItem);
        root.setAlignment(Pos.CENTER);

        primaryScene = new Scene(root, widthWindow, heightWindow);
    }

    private void createSceneForImageComparison(MyImage image){
        //-------------------------create Scene---------------------------------------------
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image.getSource(), null));
        imageView.setFitHeight(0.4* heightWindow);
        imageView.setFitWidth(0.4 * widthWindow);
        imageView.setPreserveRatio(true);
        AnchorPane.setTopAnchor(imageView, 10.);

        ImageView imageView1 = new ImageView();
        imageView1.setFitHeight(0.4 * heightWindow);
        imageView1.setFitWidth(0.4 * widthWindow);
        imageView1.setPreserveRatio(true);
        AnchorPane.setTopAnchor(imageView1, imageView.getFitHeight()+20.);

        Text text = new Text("Coefficient of difference: ");
        AnchorPane.setBottomAnchor(text, 40.);

        Button backButton = new Button("Back");
        backButton.setPrefSize(0.1*widthWindow, 20);
        backButton.setOnMouseClicked(mouseEvent -> show());
        AnchorPane.setBottomAnchor(backButton, 10.);

        AnchorPane anchorPane = new AnchorPane(imageView, imageView1, text, backButton);

        TableView<MyImage> tableCompareScene = createTableView();
        HBox root = new HBox(10, anchorPane, tableCompareScene);
        root.setAlignment(Pos.CENTER);

        Scene compareScene = new Scene(root, widthWindow, heightWindow);
        //------------------------------------------------------------------------------
        //---------------------Create Table Listener------------------------------------
        TableView.TableViewSelectionModel<MyImage> selectionModel = tableCompareScene.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<MyImage>() {
            @Override
            public void changed(ObservableValue<? extends MyImage> observableValue, MyImage oldImage, MyImage newImage) {
                if (newImage != null){
                    imageView1.setImage(SwingFXUtils.toFXImage(newImage.getSource(), null));
                    text.setText("Coefficient of difference: " + image.calcCoefficientDifferenceWith(newImage));
                }
            }
        });
        //-------------------------------------------------------------------------------
        primaryStage.setScene(compareScene);
        primaryStage.setTitle("Compare images");
        primaryStage.show();
    }

    private void addImage(){
        FileChooser fileChooser = new FileChooser();
        List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
        if(files != null) {
            for (File file : files) {
                images.add(new MyImage(file.getPath()));
            }
        }
    }

    private void removeImage(){
        MyImage image = tablePrimaryScene.getSelectionModel().getSelectedItem();
        images.remove(image);
    }

    public void show(){
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Images");
        primaryStage.show();
    }
}
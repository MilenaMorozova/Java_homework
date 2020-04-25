package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import static javafx.geometry.Insets.EMPTY;

public class GUI {
    Stage primaryStage;
    private int widthWindow, heightWindow;
    private VBox floors;
    private VBox passengersRequestsOnFloors;
    private Group elevators;
    private Font font;

    private Image[] imageOfPeople;
    private Image imageOfElevator;

    private ArrayList<TranslateTransition> elevatorTransitions;
    private ArrayList<Passenger> newPeopleOnFloors;

    private int numberOfFloors;
    private int numberOfElevators;

    private double fontSize;
    private double sizeOfPeopleImages;
    private double widthOfElevatorImage;
    private double heightOfElevatorImage;
    private double spacingBetweenElevators;
    private double spacingBetweenPassengers;
    private double spacingBetweenContainers;
    private double passengersLayoutX;

    private int millisPerOneFloor = 1000;

    public GUI(Stage primaryStage) {
        newPeopleOnFloors = new ArrayList<Passenger>();
        this.primaryStage = primaryStage;
        elevatorTransitions = new ArrayList<TranslateTransition>();
        loadImages();
    }

    protected void init(int numberOfElevators, int numberOfFloors) {
        this.numberOfElevators = numberOfElevators;
        this.numberOfFloors = numberOfFloors;

        calcParameterSizes();
        font = Font.font("Cambria", FontWeight.BOLD, fontSize);
    }

    private void calcParameterSizes() {
        fontSize = 18;
        sizeOfPeopleImages = fontSize + 10;
        widthOfElevatorImage = 50;//45
        heightOfElevatorImage = 70;//64
        spacingBetweenElevators = 0.2 * widthOfElevatorImage;
        spacingBetweenPassengers = 5;
        spacingBetweenContainers = 10;
        passengersLayoutX = fontSize + 2 * spacingBetweenContainers + numberOfElevators * widthOfElevatorImage +
                (numberOfElevators - 1) * spacingBetweenElevators;
        widthWindow = (int) (passengersLayoutX + 4 * sizeOfPeopleImages + 3 * spacingBetweenPassengers);
        heightWindow = (int) (heightOfElevatorImage * (numberOfFloors));
    }

    private void initFloors() {
        Label[] labels = new Label[numberOfFloors];
        for (int i = numberOfFloors; i > 0; i--) {
            labels[numberOfFloors - i] = new Label(String.valueOf(i));
            labels[numberOfFloors - i].setFont(font);
            labels[numberOfFloors - i].setMinHeight(heightOfElevatorImage);
        }
        floors = new VBox(labels);
        floors.setAlignment(Pos.CENTER);
    }

    private StackPane createStackPane(Passenger passenger) {
        Label label = new Label(String.valueOf(passenger.getToFloor()));
        label.setFont(font);
        ImageView imageView = createImageView(imageOfPeople[passenger.getImageViewNumber()], sizeOfPeopleImages, sizeOfPeopleImages);
        return new StackPane(imageView, label);
    }

    private void initQueuesOfPassengersOnFloors() {
        HBox[] hBoxes = new HBox[numberOfFloors];
        for (int i = 0; i < numberOfFloors; i++) {
            hBoxes[i] = new HBox(spacingBetweenPassengers);
            hBoxes[i].setMinHeight(heightOfElevatorImage);
        }
        passengersRequestsOnFloors = new VBox(hBoxes);
        passengersRequestsOnFloors.setLayoutX(passengersLayoutX);
    }

    private ImageView createImageView(Image image, double width, double height) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    private void loadImages() {
        int size = 5;
        //load images of passengers
        imageOfPeople = new Image[size];
        for (int i = 0; i < size; i++) {
            try {
                imageOfPeople[i] = new Image(new FileInputStream(".\\src\\images\\meeple" + (i + 1) + ".jpg"));
            } catch (FileNotFoundException e) {
                e.getMessage();
            }
        }
        //load image of elevator
        try {
            imageOfElevator = new Image(new FileInputStream(".\\src\\images\\elevator.png"));
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
    }

    private void initElevators() {
        elevators = new Group();
        double startX = fontSize + spacingBetweenContainers;
        double startY = heightWindow - heightOfElevatorImage - 1;
        for (int i = 0; i < numberOfElevators; i++) {
            ImageView imageView = createImageView(imageOfElevator, widthOfElevatorImage, heightOfElevatorImage);
            FlowPane flowPane = new FlowPane();
            flowPane.setMaxSize(widthOfElevatorImage, heightOfElevatorImage);
            flowPane.setHgap(spacingBetweenPassengers);
            flowPane.setAlignment(Pos.CENTER);

            StackPane stackPane = new StackPane(imageView, flowPane);
            stackPane.setLayoutX(startX);
            elevators.getChildren().add(stackPane);
            startX += spacingBetweenElevators + widthOfElevatorImage;
        }
        elevators.setLayoutY(startY);
    }

    private void addElevatorTranslateTransitions() {
        for (int i = 0; i < numberOfElevators; i++) {
            TranslateTransition transition = new TranslateTransition();
            transition.setAutoReverse(true);
            transition.setCycleCount(1);
            transition.setInterpolator(Interpolator.LINEAR);
            transition.setNode(elevators.getChildren().get(i));
            elevatorTransitions.add(transition);
        }
    }

    public void createRoot() {
        initFloors();
        initElevators();
        initQueuesOfPassengersOnFloors();
        addElevatorTranslateTransitions();
        Pane root = new Pane(floors, elevators, passengersRequestsOnFloors);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, EMPTY)));

        Scene scene = new Scene(root, widthWindow, heightWindow);
        AnimationTimer animationTimer = new AnimationTimer() {

            @Override
            public void handle(long l) {
                if (newPeopleOnFloors.isEmpty() == false) {
                    for (int i = newPeopleOnFloors.size() - 1; i >= 0; i--) {
                        addPassengerOnFloor(newPeopleOnFloors.get(i));
                    }
                }
            }
        };
        animationTimer.start();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Elevators");
        primaryStage.show();
    }

    protected void addPassengerInList(Passenger passenger) {
        newPeopleOnFloors.add(passenger);
    }

    private void addPassengerOnFloor(Passenger passenger) {
        HBox temp = (HBox) passengersRequestsOnFloors.getChildren().get(numberOfFloors - passenger.getCurrentFloor());
        StackPane stackPane = createStackPane(passenger);
        temp.getChildren().add(stackPane);
        newPeopleOnFloors.remove(passenger);
    }

    private void addNewFloorInElevatorRoute(int indexOfElevator, int toFloor) {
        StackPane elevator = (StackPane) elevators.getChildren().get(indexOfElevator);
        FlowPane flowPane = (FlowPane) elevator.getChildren().get(1);
        Text newFloor = new Text();
        newFloor.setFont(font);
        newFloor.setText(String.valueOf(toFloor));
        flowPane.getChildren().add(newFloor);
    }

    protected void addPassengerInElevator(Passenger passenger, int indexOfElevator) {
        HBox temp = (HBox) passengersRequestsOnFloors.getChildren().get(numberOfFloors - passenger.getCurrentFloor());
        Iterator<Node> iter = temp.getChildren().iterator();

        while (iter.hasNext()) {
            StackPane stackPanePassenger = (StackPane) iter.next();
            Label label = (Label) stackPanePassenger.getChildren().get(1);

            if (label.getText().equals(String.valueOf(passenger.getToFloor()))) {
                addNewFloorInElevatorRoute(indexOfElevator, passenger.getToFloor());
                iter.remove();
                break;
            }
        }
    }

    protected void removePassengerFromElevator(int indexOfElevator, Passenger passenger) {
        StackPane elevator = (StackPane) elevators.getChildren().get(indexOfElevator);
        FlowPane targetFloors = (FlowPane) elevator.getChildren().get(1);
        ObservableList<Node> floorNumbers = targetFloors.getChildren();
        for (int i = 0; i < floorNumbers.size(); i++) {
            Text text = (Text) floorNumbers.get(i);
            if (text.getText().equals(String.valueOf(passenger.getToFloor()))) {
                floorNumbers.remove(i);
                break;
            }
        }
    }

    private void moveElevator(TranslateTransition transition, double toY) {
        transition.setByY(toY);
        transition.setDuration(Duration.millis(millisPerOneFloor));
        transition.play();
    }

    protected void runElevator(int indexOfElevator, Elevator elevator) {
        TranslateTransition transition = elevatorTransitions.get(indexOfElevator);
        elevator.changeDirection();
        double toY = 0;
        if (elevator.removePassengerFromElevator() == false && elevator.addPassengersInElevator() == false) {
            switch (elevator.getDirection()) {
                case UP:
                    toY -= heightOfElevatorImage;
                    elevator.increaseCurrentFloor();
                    break;
                case DOWN:
                    toY += heightOfElevatorImage;
                    elevator.decreaseCurrentFloor();
                    break;
                case STOP:
                    return;
            }
        }
        else{
            elevator.setDirection(Direction.UP);
        }
        transition.setOnFinished(actionEvent -> runElevator(indexOfElevator, elevator));
        moveElevator(transition, toY);
    }
}

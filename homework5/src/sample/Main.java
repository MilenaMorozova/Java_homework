package sample;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GUI gui = new GUI(stage);
        ElevatorController elevatorController = new ElevatorController(gui, 3, 10);
        elevatorController.mainloop();
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}

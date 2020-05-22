import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GUI gui = new GUI(primaryStage);
        gui.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

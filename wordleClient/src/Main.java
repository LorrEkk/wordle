import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = mainLoader.load();
        Controller controller = mainLoader.getController();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Wordle");
        stage.setScene(scene);
        stage.show();

        Keyboard keyboard = new Keyboard();
        Engine engine = new Engine(controller, keyboard);
        controller.setKeyboard(keyboard);

        scene.setOnKeyPressed(event -> {
            if (event.isAltDown()) {
                KeyCode code = event.getCode();
                if (code.isLetterKey()) {
                    engine.addLetterToWordBuilder(event.getText());
                }
            } else {
                switch (event.getCode()) {
                    case BACK_SPACE -> engine.deleteLetterFromWordBuilder();
                    case ENTER -> engine.guess();
                    default -> engine.addLetterToWordBuilder(event);
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

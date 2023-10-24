import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

public class Controller {
    private Keyboard gameKeyboard;
    @FXML private GridPane wordleGridPane;
    @FXML private GridPane keyboardPane;


    public void setKeyboard(Keyboard gameKeyboard) {
        this.gameKeyboard = gameKeyboard;
    }

    public void setTileLetter(int index, String letter) { //set the single letter on the game board
        Label tile = (Label) wordleGridPane.getChildren().get(index);
        tile.setText(letter);
    }

    public void setTileColor(int index, String color) { //set the color of the single letter
        Label tile = (Label) wordleGridPane.getChildren().get(index);
        tile.setStyle("-fx-background-color: " + color + ";" + "-fx-border-color: " + color + ";");
    }

    public void resetKeyboard() { //set the colors of letters on the keyboard to light-gray
        for (int i = 0; i < 30; i++) {
            Label tile = (Label) wordleGridPane.getChildren().get(i);

            tile.setStyle("-fx-background-color: #2b2b2b;" + "-fx-border-color: #525252;");
            tile.setText("");
        }
    }

    public void displayKeyboard() { //display updated colors of the letters on the keyboard
        for (int i = 0; i < 35; i++) {
            Label keyboardTile = (Label) keyboardPane.getChildren().get(i);

            char tileLetter = keyboardTile.getText().toCharArray()[0];
            int color = gameKeyboard.getLetterUsage(tileLetter);

            if (color == 1) {
                keyboardTile.setStyle("-fx-background-color: #009c24;");
            } else if (color == 2) {
                keyboardTile.setStyle("-fx-background-color: #ffbf00;");
            } else if (color == 3) {
                keyboardTile.setStyle("-fx-background-color: #525252;");
            } else if (color == 0) {
                keyboardTile.setStyle("-fx-background-color: #7a7a7a;");
            }
        }
    }

    public boolean showGameOverPopup(boolean result) {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("popup.fxml"));
            dialog.setDialogPane(fxmlLoader.load());
            dialog.initStyle(StageStyle.UTILITY);
            PopupController dialogController = fxmlLoader.getController();

            dialogController.setPopupText(result);

            Optional<ButtonType> selectedOption = dialog.showAndWait(); //display the popup and wait for the button to be pressed

            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.YES) {
                return true;
            } else if (selectedOption.isPresent() && selectedOption.get() == ButtonType.CLOSE) {
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean showErrorAlert() {
        try {
            Dialog<ButtonType> errorDialog = new Dialog<>();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("errorAlert.fxml"));
            errorDialog.setDialogPane(fxmlLoader.load());
            errorDialog.initStyle(StageStyle.UTILITY);

            Optional<ButtonType> selectedOption = errorDialog.showAndWait(); //display the popup and wait for the button to be pressed

            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.YES) {
                return true;
            } else if (selectedOption.isPresent() && selectedOption.get() == ButtonType.CLOSE) {
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

}

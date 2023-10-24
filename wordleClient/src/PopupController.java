import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopupController { //display game over popup
    @FXML private Label popupLabel;


    public void setPopupText(boolean result) {
        if (result) {
            popupLabel.setText("You won!");
        } else {
            popupLabel.setText("You lost!");
        }
    }
}

package escapefromuniversity.inGame.end;

import escapefromuniversity.model.gameObject.player.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * The controller of the fxml file that displays the launcher window.
 */
public class EndController {

    @FXML
    private Button outcome, cfu, mark, exit;
    private final Player player;

    public EndController(Player player) {
        this.player = player;
    }

    /* Show the view. */
    @FXML
    void initialize() {
        if (this.player.isGraduated()) {
            outcome.setText("COMPLIMENTI DOTTORE! TI SEI LAUREATO ED HAI VINTO!");
        } else {
            outcome.setText("CHE PECCATO, HAI FALLITO LA TUA CARRIERA UNIVERSITARIA, HAI PERSO :(");
        }
        cfu.setText("CFU: " + this.player.getCredits());
        mark.setText("Mark: " + this.player.getFinalMark());
    }

    /* Closes the game. */
    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }

}
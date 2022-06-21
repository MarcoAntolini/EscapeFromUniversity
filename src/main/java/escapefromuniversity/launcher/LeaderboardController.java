package escapefromuniversity.launcher;

import escapefromuniversity.ReadFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;

import static escapefromuniversity.launcher.LauncherView.*;

public class LeaderboardController {

    @FXML
    private Button backButton;

    @FXML
    private GridPane leaderboard;

    @FXML
    void back(ActionEvent event) {
        launcherWindow.setScene(launcher);
    }

    public void createLeaderboard() throws IOException {
        ReadFile reader = new ReadFile("score", "score.txt");
        for(int i=0; i<10; i++) {
            (leaderboard = new GridPane()).addRow(i, new TextField(reader.newLine()));
        }
        reader.close();
    }
}

package escapefromuniversity.inGame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import escapefromuniversity.WriteFile;
import escapefromuniversity.controller.map.LayersControllerImpl;
import escapefromuniversity.inGame.end.EndController;
import escapefromuniversity.launcher.LauncherView;
import escapefromuniversity.menu.MenuController;
import escapefromuniversity.menu.MenuControllerImpl;
import escapefromuniversity.model.GameModel;
import escapefromuniversity.model.GameModelImpl;
import escapefromuniversity.model.GameState;
import escapefromuniversity.model.basics.HitBox;
import escapefromuniversity.model.gameObject.GameObject;
import escapefromuniversity.model.gameObject.enemy.Boss;
import escapefromuniversity.model.gameObject.player.Player;
import escapefromuniversity.quiz.QuizController;
import escapefromuniversity.shop.ShopController;
import escapefromuniversity.shop.ShopControllerImpl;
import escapefromuniversity.utilities.LauncherResizer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Implements all the methods defined in its interface {@link GameController}.
 */
public class GameControllerImpl implements GameController {
    private static final long DELTA = 200;
    private static final double MILLI_TO_SECOND = 0.001;
    private final GameModel gameModel;
    private GameView gameView;
    private final ShopController shopController;
    private final MenuController menuController = new MenuControllerImpl(this);
    private final LayersControllerImpl layersController;
    private GameState gameState;
    private GameState prevGameState;
    private List<Integer> gameObjID = new LinkedList<>();

    /**
     * Instantiates a new GameController and initializes the corresponding GameModel
     * and GameView and KeyHandler making the game start.
     */
    public GameControllerImpl() {
        this.gameModel = new GameModelImpl(this);
        this.gameObjID = this.getGameObjectID();
        this.layersController = new LayersControllerImpl(this.gameModel.getGameInit().getMap(),
                this.gameModel.getPlayer());
        this.shopController = new ShopControllerImpl(this, this.gameModel);
        this.setGameState(GameState.PLAY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameLoop() {
        if (this.continueGame()) {
            switch (this.getGameState()) {
                case PLAY:
                case FIGHT:
                case GRADUATED:
                    this.updateModel(DELTA);
                    this.checkSpriteAnimation();
                    break;
                case QUIZ:
                    this.startQuiz(this.gameModel.getCurrentBoss());
                    break;
                case MENU:
                    this.menuController.startView();
                    break;
                case SHOP_MENU:
                    this.shopController.startView();
                    break;
                default:
                    break;
            }
        }
        if (this.getGameState() == GameState.WIN) {
            this.saveScore(this.gameModel.getPlayerFinalMark());
        }
    }

    /*
     * Checks if the player has won the game or if he lost and returns true if the
     * game is not finished, false otherwise.
     */
    private boolean continueGame() {
        return this.getGameState() != GameState.LOST && this.getGameState() != GameState.WIN;
    }

    /* Calls the update method in the model. */
    private void updateModel(final long deltaTime) {
        this.gameModel.updateGame((double) deltaTime * MILLI_TO_SECOND);
    }

    /* Returns a list with all the IDs of the game objects. */
    private List<Integer> getGameObjectID() {
        return this.gameModel.getAllDynamicGameObj().stream()
                .map(GameObject::getID)
                .collect(Collectors.toList());
    }

    /*  */
    private void checkSpriteAnimation() {
        final List<Integer> ids = this.getGameObjectID();
        for (final Integer id : ids) {
            if (this.gameView.containThisID(id)) {
                this.gameView.updateSpriteAnimation(id, this.gameModel.getStateID(id));
            } else {
                this.gameView.addSpriteAnimation(id, this.gameModel.getStateID(id), this.gameModel.getTypeID(id));
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerLife() {
        return this.gameModel.getPlayer().getLife();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerCredits() {
        return this.gameModel.getPlayer().getCredits();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startQuiz(final Boss boss) {
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("layouts/Quiz.fxml"));
            Parent gameRoot;
            final QuizController quizController = new QuizController(this);
            loader.setController(quizController);
            gameRoot = loader.load();
            Scene quiz = new Scene(gameRoot, LauncherResizer.sceneWidth, LauncherResizer.sceneHeight);
            LauncherView.launcherWindow.setScene(quiz);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameState(final GameState gameState) {
        this.prevGameState = this.gameState;
        this.gameState = gameState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameState getGameState() {
        return gameState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameState getPrevGameState() {
        return prevGameState;
    }

    /* Saves the final mark and its date and time in the leaderboard text file. */
    private void saveScore(final int mark) {
        try {
            WriteFile w = new WriteFile("EscapeFromUniversity", "score.txt");
            DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            w.writeNewLine(mark + "\t\t" + dtf4.format(LocalDateTime.now()));
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer() {
        return this.gameModel.getPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuController getMenuController() {
        return this.menuController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShopController getShopController() {
        return this.shopController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HitBox getHitBoxID(final int id) {
        return this.gameModel.getHitBoxID(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameView(final GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameView getGameView() {
        return this.gameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameModel getModel() {
        return this.gameModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPresentID(final int id) {
        return gameObjID.contains(id);
    }

    @Override
    public boolean isGraduated(final GameState gameState) {
        return gameState == GameState.GRADUATED;
    }

    @Override
    public boolean isOver(final GameState gameState) {
        return gameState == GameState.LOST;
    }

    private void showEnd(final Player player) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("layouts/End.fxml"));
        Parent gameRoot;
        final EndController endController = new EndController(this.getPlayer());
        loader.setController(endController);
        gameRoot = loader.load();
        Scene end = new Scene(gameRoot, LauncherResizer.sceneWidth, LauncherResizer.sceneHeight);
        LauncherView.launcherWindow.setScene(end);
    }
}

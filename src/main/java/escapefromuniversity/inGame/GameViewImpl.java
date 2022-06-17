package escapefromuniversity.inGame;

import escapefromuniversity.menu.MenuViewImpl;
import escapefromuniversity.model.GameState;
import escapefromuniversity.utilities.OSFixes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import static escapefromuniversity.utilities.LauncherResizer.screenHeight;
import static escapefromuniversity.utilities.LauncherResizer.screenWidth;

public class GameViewImpl extends JFrame implements GameView, KeyListener {

	private GameController controller;
	private final JFrame window;
	private JPanel pause;
	private BufferedImage tempScreen;

	public GameViewImpl() {
		this.window = new JFrame();
		this.window.setUndecorated(true);
		this.window.setSize(screenWidth, screenHeight);
		this.window.setResizable(false);
		this.window.setTitle("Escape From University");
		String logo = OSFixes.getLocation("images", "logo.png"); // TODO change icon
		this.window.setIconImage(Toolkit.getDefaultToolkit().getImage(logo));
		this.window.setVisible(true);
		//	private final JPanel gamePanel;

		this.window.addKeyListener(this);

		this.pause = new JPanel();
		this.tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) this.tempScreen.getGraphics();

		this.controller.setGameState(GameState.MENU);
		drawToScreen();
		drawToImage();
	}

	public void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(this.tempScreen, 0, 0, screenWidth, screenHeight, null);
		g.dispose();
	}

	public void drawToImage () {
//      g2d.setFont(null); TODO choose font
		switch (this.controller.getGameState()) { //pauseBG forse non qua
			case DIALOGUE:
				this.addPauseBG();
				break;
			case FIGHT:
				break;
			case MENU:
				this.addPauseBG();
				new MenuViewImpl();
				break;
			case PLAY:
				this.removePauseBG();
				break;
			case QUIZ:
				this.addPauseBG();
//				new QuizView().start();	stage?? togliere startcompetition
				break;
			case SHOP:
				this.addPauseBG();
//				showShop
				break;
			case LOST:
				break;
			case WIN:
				break;
			default:
				break;
		}
	}

	@Override
	public void update() {
		this.drawToImage();
		
	}

	@Override
	public void setGameController(final GameController controller) {
		this.controller = controller;
	}

	public void addPauseBG () {
		Color color = new Color(0,0,0,205);
		this.pause.setBackground(color);
		this.window.add(this.pause);
	}

	public void removePauseBG () {
		this.window.remove(this.pause);
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if(this.controller != null) {
			this.controller.pressKey(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		if(this.controller != null) {
			this.controller.releaseKey(key);
		}
	}
}

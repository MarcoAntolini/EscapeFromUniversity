package escapefromuniversity.inGame;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JProgressBar;

import java.awt.Rectangle;
import java.awt.*;

public class GameHUDPanel extends JLayeredPane{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JLabel creditsCounter = new JLabel();
	private JProgressBar playerLifeBar;
	private final Font font = new Font("OCR A Extended", Font.PLAIN, 20);
	private final Image creditsImage;
	private final Rectangle playerLifeBarPosition;
	private final Rectangle creditsPosition;
	private final Rectangle creditsCounterPosition;
	
	GameHUDPanel(final double windowRatio){
		this.creditsCounter.setText("0");
		this.creditsCounter.setFont(font);
		this.creditsCounter.setForeground(Color.white);
		this.creditsPosition = new Rectangle((int) (10 * windowRatio), (int) (50 * windowRatio), (int) (50 * windowRatio), (int) (50 * windowRatio));
		this.creditsCounterPosition = new Rectangle((int) (60 * windowRatio), (int) (50 * windowRatio), (int) (50 * windowRatio), (int) (50 * windowRatio));
		this.creditsCounter.setBounds(creditsCounterPosition);
		this.creditsImage = new ImageIcon(this.getClass().getResource("file immagine")).getImage().getScaledInstance((int) creditsPosition.getWidth(),
                (int) creditsPosition.getHeight(), 
                Image.SCALE_SMOOTH);
		this.playerLifeBarPosition = new Rectangle((int) (10 * windowRatio), (int) (10 * windowRatio), (int) (300 * windowRatio), (int) (30 * windowRatio));
		this.add(this.creditsCounter);
		this.setOpaque(false);
	}
	
	public void setLifeBar(final int maxLife) {
		this.remove(this.playerLifeBar);
		this.playerLifeBar = new JProgressBar(0, maxLife);
		this.playerLifeBar.setBounds(this.playerLifeBarPosition);
		this.playerLifeBar.setForeground(new Color(150, 0, 0));
		this.add(this.playerLifeBar);
	}
	
	public void updateCreditsCounter(final int credits) {
		this.creditsCounter.setText("" + credits);
	}
	
	public void updateLife(final int life) {
		this.playerLifeBar.setValue(life);
	}
	
	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.creditsImage, this.creditsPosition.getX(), this.creditsPosition.getY(), null);
		Toolkit.getDefaultToolkit().sync();
	}

}
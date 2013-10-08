package usrinput;

import gui.WoWCanvas;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import main.Player;


public class KeyControl extends KeyAdapter {
	
	private Player player;
	private WoWCanvas canvas;
	
	public KeyControl(Player player, WoWCanvas canvas) {
		this.player = player;
		this.canvas = canvas;
	}
	
	/**
	 * keyPressed
	 * Handles the keyPressed event of the keyboard.
	 * If an arrow key is pressed, the player is moved. 
	 * @param ke KeyEvent that occured
	 */
	public void keyPressed(KeyEvent ke) {
		/* Calculate new movement offset. */
		switch (ke.getKeyCode()) {
			/*case KeyEvent.VK_ESCAPE :
				player.setRelativeMouse(false);
				canvas.showCursor();
				break;
			case KeyEvent.VK_D :
				if(ke.isShiftDown())
					player.setDebugMode(!player.getDebugMode());
				break;
			case KeyEvent.VK_S :
				if(player.getDebugMode())
					player.setStepFlag(true);
				break;
			case KeyEvent.VK_L :
				if(player.getDebugMode())
					player.setLevelCompleted(true);
				break;*/
		}
	}
}

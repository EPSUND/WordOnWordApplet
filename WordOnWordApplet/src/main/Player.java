package main;
import gui.WoWCanvas;

import java.util.Vector;

import usrinput.KeyControl;
import usrinput.MouseControl;
import usrinput.MouseMotionControl;

/**
 * Player class
 * A class for handling player input, powerups, scoring and lives
 * @author Erik Sundholm
 * @version %G%
 */
public class Player {

	protected int score, numWords;
	
	/*The player's mouse motion controller*/
	private MouseMotionControl mouseMotionController;
	/*The player's mouse controller*/
	private MouseControl mouseController;
	/*The player's key controller*/
	private KeyControl keyController;

	/**
	 * Player constructor
	 * Creates a player on stage.
	 * @param stage The stage that shows the Player
	 * @param x x-position
	 * @param y y-position
	 */
	public Player(WoWCanvas canvas, CharacterRectDropper charRectDropper, CharacterRect[][] wordGrid) {
		score = 0;
		numWords = 0;
		
		/*Initiate the player's mouse controllers*/
		mouseMotionController = new MouseMotionControl(wordGrid);
		mouseController = new MouseControl(charRectDropper, wordGrid);
		mouseController.addCharacterRectListener(mouseMotionController);
		/*Initiate the player's key controller*/
		keyController = new KeyControl(this, canvas);
	}
	
	public void calcScore(Vector<Word> words)
	{
		//Reset the score
		score = 0;
		
		//Add the score for all the words
		for(Word word : words)
		{
			score += word.getScore();
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public void setNumWords(int numWords)
	{
		this.numWords = numWords;
	}
	
	public int getNumWords()
	{
		return numWords;
	}
	
	public MouseMotionControl getMouseMotionController() {
		return mouseMotionController;
	}
	
	public MouseControl getMouseController() {
		return mouseController;
	}
	
	public KeyControl getKeyController() {
		return keyController;
	}
	
	public void moveDropZoneBlock(CharacterRectDropper charRectDropper)
	{
		//If we have not moved the mouse yet
		if(mouseMotionController.mousePos == null)
		{
			return;
		}
		
		charRectDropper.moveDropZoneBlock(mouseMotionController.mousePos);
	}
	
	public void startGame()
	{
		mouseMotionController.startGame();
		mouseController.startGame();
	}
	
	public void restartGame()
	{
		numWords = 0;
		score = 0;
		mouseMotionController.restartGame();
		mouseController.restartGame();
	}
}

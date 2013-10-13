package main;
import gui.JokerDialog;
import gui.NewGameDialog;
import gui.WoWCanvas;
import hscore.WoWHighScoreSystem;
import hscore.HighScoreListDialog;
import hscore.WoWHighScoreTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sound.SoundPlayer;
import utils.Helpers;

public class WordOnWord extends JApplet implements Runnable {
	
	public static final int DEFAULT_WINDOW_WIDTH = 545;
	public static final int DEFAULT_WINDOW_HEIGHT = 540;
	
	public static final int STATUS_FIELD_SPACING = 15;
	public static final int DROP_ZONE_SPACING = 15;
	public static final int WORD_RING_BIG_SPACING = 12;//10
	public static final int WORD_RING_SMALL_SPACING = 6;//10
	
	public static final int CANVAS_GRID_WIDTH = 490;//560
	public static final int CANVAS_GRID_HEIGHT = 490;//560
	
	//Vocabulary constants
	
	//Alfabets
	public static final String SWEDISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ";//ÅÄÖ?
	public static final String ENGLISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String GERMAN_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜß";
	
	//Languages
	public static final int SWEDISH = 0;
	public static final int ENGLISH = 1;
	public static final int GERMAN = 2;
	
	//Vocabulary modes
	public static final int PURE_RANDOM = 0;
	public static final int WEEK_VOCABULARY = 1;
	public static final int NEW_VOCABULARY = 2;
	
	//GUI items
	
	public JPanel wowPanel;
	public JFrame wowFrame;
	public JMenuBar menuBar;
	public JMenu gameMenu;
	public JMenu optionsMenu;
	public JMenu actionMenu;
	public JMenu helpMenu;
	public JMenuItem restartItem;
	public JMenuItem highScoreItem;
	public JMenuItem muteSoundEffectsItem;
	public JMenuItem startItem;
	public JMenuItem jokerItem;
	public JMenuItem howToPlayItem;
	public JMenuItem controlsItem;
	public JMenuItem aboutItem;
	
	public JokerDialog jokerDialog;
	public NewGameDialog newGameDialog;
	
	public WoWCanvas wowCanvas;
	
	//Game resources
	public static CharacterPicker characterPicker;
	public static WordChecker wordChecker;
	public static WoWHighScoreSystem highScoreSystem;
	public static SoundPlayer soundPlayer;
	
	public GameEngine gameEngine;
	
	private Thread gameThread;
	
	private volatile boolean start;
	
	public static int language;
	
	public void destroy()
	{
		//Dispose sound resources
		soundPlayer.disposePlayer();
	}
	
	public void stop()
	{
		//Gör något vettigt
	}
	
	public void start()
	{
		//Gör något vettigt
	}
	
	public void run()
	{
		try
		{
			do {
				//Show the new game dialog where game options can be specified
				newGameDialog.setVisible(true);
				//Set the language to use in the game
				language = newGameDialog.getLanguage();
				//Set the language of the high score system
				highScoreSystem.setActiveLanguage(language);
				//Initiate the character picker
				characterPicker.setVocabularyMode(newGameDialog.getVocabularyMode());
				//Set the language for the word checker
				wordChecker.setLanguage(language);
				
				gameEngine.initWorld();	// Initialize game variables.
				
				if(!gameEngine.getRestart())//Only do this once
				{
					/* Set the applet size. Extend the height somewhat for the status field and the menu bar*/
					setSize(gameEngine.getPlayingFieldWidth(), gameEngine.getPlayingFieldHeight() + menuBar.getHeight());
				}
				else//We have restarted and should set the restart flag to false
				{
					gameEngine.setRestart(false);
					
				}
				
				//Play the intro tune
				soundPlayer.playClip("intro");
				
				/*Start the game. */
				gameEngine.game();
				} while(gameEngine.getRestart());
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, Helpers.getStackTraceString(e), "Word on Word: An exception has occured", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void init()
	{
		start = false;
		
	    try {
	    	//Create the applet's GUI on the event-dispatching thread
	        SwingUtilities.invokeAndWait(new Runnable() {
	            public void run() {
	                createGUI();
	            }
	        });
	        
	        //Create game resources
	        createGameResources();
	        //Start a new game
	        newGame();
	        
	    } catch (Exception e) {
	        System.err.println("WordOnWord: The game could not initialised properly");
	        e.printStackTrace();
	        return;
	    }
	}
	
	private void createGameResources()
	{
		//Make the character picker
		characterPicker = makeWoWCharacterPicker();
		//Make the word checker
		wordChecker = makeWoWWordChecker();
		//Make the highscore system
		highScoreSystem = new WoWHighScoreSystem(new HighScoreListDialog(new WoWHighScoreTableModel()));
		//Make sound player
		soundPlayer = makeWoWSoundPlayer();
		//Make the game engine
		gameEngine = new GameEngine(wowCanvas);
	}
	
	private void createGUI()
	{
		/*Get the frame's panel*/
		wowPanel = (JPanel)getContentPane();

		/* Create the menu bar. */
		menuBar = new JMenuBar();

		/* Create the game menu */
		gameMenu = new JMenu("Game");
		
		/*Add game menu to menu bar*/
		menuBar.add(gameMenu);
		
		/* Create the game menu items */
		restartItem = new JMenuItem("Restart");
		highScoreItem = new JMenuItem("High score");
		
		/*Add the game menu items to the menu*/
		gameMenu.add(restartItem);
		gameMenu.add(highScoreItem);
		
		/*Create the options menu*/
		optionsMenu = new JMenu("Options");
		
		/*Add the options menu to the menu bar*/
		menuBar.add(optionsMenu);
		
		/*Create the options menu items*/
		muteSoundEffectsItem = new JMenuItem("Mute sound effects");
		
		/*Add the options menu items to the menu*/
		optionsMenu.add(muteSoundEffectsItem);
		
		//Make the action menu
		actionMenu = new JMenu("Action");
		
		//Add the action menu to the menu bar
		menuBar.add(actionMenu);
		
		//Create the start menu item
		startItem = new JMenuItem("Start");
		//Create the joker menu item
		jokerItem = new JMenuItem("Joker");
		
		//Add the action menu items
		actionMenu.add(startItem);
		//Add the jokerItem when we have clicked the start item
		
		//Make the help menu
		helpMenu = new JMenu("Help");
		
		//Add the help menu to the menu bar
		menuBar.add(helpMenu);
		
		//Create the "how to play" menu item
		howToPlayItem = new JMenuItem("How to play");
		//Create the controls menu item
		controlsItem = new JMenuItem("Controls");
		//Create the about menu item
		aboutItem = new JMenuItem("About");
		
		//Add the help menu items
		helpMenu.add(howToPlayItem);
		helpMenu.add(controlsItem);
		helpMenu.add(aboutItem);
		
		/* Install the menu bar in the frame. */
		gameMenu.getPopupMenu().setLightWeightPopupEnabled(false);
		
		/*Set the menu bar*/
		setJMenuBar(menuBar);
		
		/*Make the canvas*/
		wowCanvas = new WoWCanvas();
		
		/* Add the canvas to the panel. */
		wowPanel.add(wowCanvas);
		
		/* Enable double buffering. */
		wowCanvas.createStrategy();
		
		//Make the joker dialog
		jokerDialog = new JokerDialog(SwingUtilities.windowForComponent(this));
		//Make the new game
		newGameDialog = new NewGameDialog();
		
		/*Add listeners*/
		
		restartItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				characterPicker.resetCharacterPicker();
				gameEngine.setRestart(true);
				actionMenu.removeAll();
				actionMenu.add(startItem);
			}
		});
		
		highScoreItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				highScoreSystem.showHighScoreList();
			}
		});
		
		muteSoundEffectsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(soundPlayer.isMuted())
				{
					soundPlayer.unmute();
					muteSoundEffectsItem.setText("Mute sound effects");
				}
				else
				{
					soundPlayer.mute();
					muteSoundEffectsItem.setText("Unmute sound effects");
				}
			}
		});
		
		startItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				gameEngine.startGame();
				actionMenu.remove(startItem);
				actionMenu.add(jokerItem);
			}
		});
		
		jokerItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//Show joker dialog
				jokerDialog.setVisible(true);
				
				if(jokerDialog.getJoker() != null)
				{
					gameEngine.addJokerBlock(jokerDialog.getJoker());
					actionMenu.remove(jokerItem);
				}
			}
		});
		
		howToPlayItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(null,
						"OBJECTIVE OF THE GAME:\n" +
						"The objective of the game is to make words with the letters that appear at the top of the screen.\n" +
						"Score is awarded for each word made.\n" + 
						"The score awarded for a word is determined by the length of the word and which letters were used.\n" +
						"The goal of the game is to arrange the received letters to get as much points as possible.\n\n" +
						"GAMEPLAY:\n" +
						"The game begins with a preparation phase where the player recives 5 intial letter blocks.\n" + 
						"These blocks can be arranged with the mouse(by dragging them) until the player is satisfied with their placement.\n" + 
						"The start menu item in the action menu is then pressed which starts the actual game.\n" +
						"Blocks are from that point received from the top left corner of the screen and the player can drop these blocks with the mouse.\n" + 
						"New letter blocks are added until the grid is full at which point the game is over.\n\n" +
						"JOKER\n" +
						"The player has one joker block that can be used at any point during the game(except during the preparation phase).\n" + 
						"This joker is placed by pressing the joker menu item in the action menu, which appears after the player presses start.\n\n" +
						"BLOCK OPTIONS:\n" +
						"When the game is started is the player given three options on which letter blocks to use:\n\n" +
						"The week's blocks:\n" +
						"Use this week's letters\n" +
						"There is a new selection of letters for the game every week.\n" + 
						"Great if you want to compete with someone on equal terms.\n\n" +
						"Random blocks:\n" + 
						"Generate a new selection of letters.\n" + 
						"If you are tired of this week's words\n\n" +
						"Totally random blocks:\n" + 
						"Pick letters entirely at random.\n" + 
						"In the other modes are the distribution of letters done according to how common the letters are.\n" + 
						"Here are letters picked randomly from the alfabet.", 
						"How to play", JOptionPane.INFORMATION_MESSAGE, null);
			}
		});
		
		controlsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(null, 
						"BEGIN GAME:\n" +
						"The game is started by pressing the start menu item in the action menu.\n" +
						"ADD JOKER:\n" +
						"The joker block is added by pressing the joker menu item in the action menu.\n" + 
						"The joker menu item appears in the action menu after the player presses start.\n" +
						"DROPPING BLOCKS:\n" +
						"Blocks are dropped by moving the mouse pointer to the grid column you wish to drop the block in and then clicking the left mouse button.\n" +
						"ARRANGING BLOCKS:\n" +
						"Blocks are arranged by dragging them with the mouse. Can only be done in the preparation phase.\n" +
						"TURNING ON AND OFF SOUNDS:\n" +
						"The game's sound effects can be turned on and off in the options menu.\n" +
						"VIEW HIGH SCORE LIST:\n" +
						"The game has a high score list which can viewed by pressing the high score menu item in the game menu.\n" +
						"RESTART GAME:\n" +
						"The game can be restarted by pressing the restart menu item in the game menu.",
						"Controls", JOptionPane.INFORMATION_MESSAGE, null);
			}
		});
		
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(null,
						"DEVELOPED BY:\n" +
						"Erik Sundholm 2013",
						"About", JOptionPane.INFORMATION_MESSAGE, null);
			}
		});
	}
	
	private void newGame() {
		start=true;
		gameMenu.remove(0);
		gameMenu.add(restartItem, 0);
		
		//Start the game thread
	    gameThread = new Thread(this);
	    gameThread.start();
	}
	
	private CharacterPicker makeWoWCharacterPicker()
	{
		return new CharacterPicker();
	}
	
	private WordChecker makeWoWWordChecker()
	{
		return new WordChecker();
	}
	
	private SoundPlayer makeWoWSoundPlayer()
	{
		SoundPlayer soundPlayer = new SoundPlayer();
		
		soundPlayer.loadClip("sounds/intro.wav", "intro");
		soundPlayer.loadClip("sounds/word_1.wav", "word_1");
		soundPlayer.loadClip("sounds/word_2.wav", "word_2");
		soundPlayer.loadClip("sounds/word_3.wav", "word_3");
		soundPlayer.loadClip("sounds/word_4.wav", "word_4");
		soundPlayer.loadClip("sounds/word_5.wav", "word_5");
		soundPlayer.loadClip("sounds/word_6.wav", "word_6");
		soundPlayer.loadClip("sounds/word_7.wav", "word_7");
		
		return soundPlayer;
	}
}

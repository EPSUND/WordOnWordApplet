package main;
import gui.WoWCanvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.Random;

public class GameEngine {
	
	//Grid constants
	public static final int GRID_COLUMNS = 7;
	public static final int GRID_ROWS = 7;
	public static final int CELL_WIDTH = WordOnWord.CANVAS_GRID_WIDTH / GRID_COLUMNS;
	public static final int CELL_HEIGHT = WordOnWord.CANVAS_GRID_HEIGHT / GRID_ROWS;
	public static final int DROP_ZONE_HEIGHT = CELL_HEIGHT + WordOnWord.DROP_ZONE_SPACING;
	public static final int STATUS_FIELD_WIDTH = CELL_WIDTH + WordOnWord.STATUS_FIELD_SPACING;
	
	//Character Rectangle constants
	public static final int CHAR_RECT_DROP_SPEED = 5;
	public static final int CHAR_RECT_MOVE_SPEED = 4;
	
	private WoWCanvas canvas;
	private Player player;
	private volatile boolean restart, jokerAdded;
	private boolean inPrepPhase, isGameOver;
	private int pf_width, pf_height;
	
	//The grid containing the words
	private CharacterRect[][] wordGrid;
	//The mechanism responsible for dropping character blocks
	private CharacterRectDropper charRectDropper;
	//The generated words
	private Vector<Word> words;
	
	//The lock for the words vector
	private Object wordsLock;
	
	public GameEngine(WoWCanvas canvas) {
		this.canvas = canvas;
		restart = false;
		jokerAdded = false;
		inPrepPhase = true;
		isGameOver = false;
		
		wordGrid = new CharacterRect[GRID_ROWS][GRID_COLUMNS];
		
		//Set to null just in case
		for(int i = 0; i < GRID_ROWS; i++)
		{
			for(int j = 0; j < GRID_COLUMNS; j++)
			{
				wordGrid[i][j] = null;
			}
		}
		
		charRectDropper = new CharacterRectDropper();
		words = new Vector<Word>();
		
		wordsLock = new Object();
	}
	
	/**
	 * game
	 * The game main loop. Calls the updating and painting of objects
	 * every FRAME_DELAY ms.
	 */
	public void game() {
		/* Update the game as long as it is running. */
		while (canvas.isVisible() && !restart) {
			updateWorld();	// Update the player and NPCs.
			paintWorld();	// Paint the objects and background scenes.
			
			try { 
				Thread.sleep(WoWCanvas.FRAME_DELAY);		// Wait a given interval.
			} catch (InterruptedException e) {
				// Ignore.
			}
		}
		//music.level.stop();
		return;
	}
	
	/**
	 * initWorld
	 * Executes the initial creating of the world.
	 */
	public void initWorld() {
		if(!restart) 
		{
			//Make the player
			player = new Player(canvas, charRectDropper, wordGrid);
			
			pf_width = WordOnWord.CANVAS_GRID_WIDTH + STATUS_FIELD_WIDTH;
			pf_height = WordOnWord.CANVAS_GRID_HEIGHT + DROP_ZONE_HEIGHT;
			
			setCanvasSize(pf_width, pf_height);
			
			makeInitialCharRects();
			
			/*Make the player's controllers listeners to the canvas*/
			canvas.initMouseListeners(player.getMouseMotionController(), player.getMouseController());
		}
		else
		{	
			//Initiate the preperation phase again
			inPrepPhase = true;
			//Reset the game over flag
			isGameOver = false;
			//Reset the joker flag
			jokerAdded = false;
			//Reset score, number of words and set the mouse controller as being in the preperation phase
			player.restartGame();
			//Clear words
			words.clear();
			//Clear grid
			for(int i = 0; i < GRID_ROWS; i++)
			{
				for(int j = 0; j < GRID_COLUMNS; j++)
				{
					wordGrid[i][j] = null;
				}
			}
			//Reset the character block dropper
			charRectDropper.resetCharacterRectDropper();
			//Make the initial character blocks again
			makeInitialCharRects();
		}
	}
	
	/**
	 * updateWorld
	 * Updates the game world
	 */
	public void updateWorld() 
	{
		Vector<GridPos> gridPositions;
		
		//Update falling blocks
		gridPositions = charRectDropper.updateFallingBlocks(wordGrid, pf_width, pf_height);
		
		if(!inPrepPhase)//If we have placed our initial character blocks
		{
			//Check for new words
			for(GridPos gridPos : gridPositions)
			{
				checkForWords(gridPos);
			}
			
			//Check if our actions resulted in any new words and get the length of the longest word
			int wordLength = isPartOfWord(gridPositions);
			//Play a sound clip if any new words were found
			if(wordLength > 0)
			{
				playWordSound(wordLength);
			}
			
			//Move the current block in the drop zone according to how the mouse moves
			player.moveDropZoneBlock(charRectDropper);
			
			//The game is over and we should should register the player's score
			if(!isGameOver && jokerAdded && !charRectDropper.getHasBlocksLeft())
			{
				isGameOver = true;
				
				//Run the score registration in a seperate thread to prevent the rendering from being paused
				new Thread()
				{
					public void run()
					{
						WordOnWord.highScoreSystem.registerScore(new Object[]{player.getScore(), player.getNumWords()});	
					}
				}.start();
			}
		}
	}
	
	/**
	 * paintWorld
	 * Paints all the background scenes and objects on canvas.
	 */
	public void paintWorld() {
		/* Initiate the graphics context */
		canvas.initGraphicsContext();
		
		//Paint the background
		canvas.paintBackground();
		
		//Paint the status bar
		canvas.paintStatusField(player.getScore(), player.getNumWords());
		
		//Paint the drop zone
		canvas.paintDropZone();
		
		//Paint the word grid
		for(int i = 0; i < GRID_ROWS; i++)
		{
			for(int j = 0; j < GRID_COLUMNS; j++)
			{
				if(wordGrid[i][j] != null)
				{
					canvas.paint(wordGrid[i][j]);
				}
			}
		}
		
		//Paint the character blocks in the drop zone
		canvas.paint(charRectDropper);
		
		synchronized(wordsLock)
		{
			//Paint the words word rings
			for(Word word : words)
			{
				canvas.paint(word);
			}
		}
		
		//Show the graphics buffer
		canvas.showGraphicsBuffer();
	}
	
	public void setRestart(boolean val) {
		restart = val;
	}
	
	public boolean getRestart() {
		return restart;
	}
	
	public void setPlayingFieldWidth(int width) {
		pf_width = width;
	}
	
	public int getPlayingFieldWidth() {
		return pf_width;
	}
	
	public void setPlayingFieldHeight(int height) {
		pf_height = height;
	}
	
	public int getPlayingFieldHeight() {
		return pf_height;
	}
	
	public WoWCanvas getCanvas() {
		return canvas;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	private void setCanvasSize(int width, int height)
	{
		/*Set the canvas size*/
		canvas.setPlayingFieldWidth(width);
		canvas.setPlayingFieldHeight(height);
		canvas.setBounds(0, 0, width, height);
	}
	
	private void makeInitialCharRects()
	{
		//Make the 5 starting blocks
		charRectDropper.addFallingBlock(new CharacterRect(getCanvasXPos(1), getCanvasYPos(0)));
		charRectDropper.addFallingBlock(new CharacterRect(getCanvasXPos(2), getCanvasYPos(0)));
		charRectDropper.addFallingBlock(new CharacterRect(getCanvasXPos(3), getCanvasYPos(0)));
		charRectDropper.addFallingBlock(new CharacterRect(getCanvasXPos(4), getCanvasYPos(0)));
		charRectDropper.addFallingBlock(new CharacterRect(getCanvasXPos(5), getCanvasYPos(0)));
	}
	
	private void addWord(Word newWord)
	{	
		synchronized(wordsLock)
		{
			//To make sure that words of length 1 are not added when they shouldn't
			for(Word word : words)
			{
				if(newWord.isInsideWord(word))//We are trying to insert a word of length 1 whose space is already occupied
				{
					return;
				}
				else if(word.isInsideWord(newWord))//The word is inside the word we are trying to insert. The word should be of length 1 and there can only be one such word.
				{
					words.remove(word);
					break;
				}
			}
			
			//Add word
			words.add(newWord);
			
			//Recalculate the score
			player.calcScore(words);
			//Set the number of words
			player.setNumWords(words.size());
		}
	}
	
	private void checkCurrentGridForWords()
	{
		Vector<GridPos> gridpositions =  new Vector<GridPos>();
		
		//Get all character blocks in the grid
		for(int i = 0; i < GRID_ROWS; i++)
		{
			for(int j = 0; j < GRID_COLUMNS; j++)
			{
				if(wordGrid[i][j] != null)
				{
					gridpositions.add(new GridPos(i, j));
				}
			}
		}
		
		//Check all character blocks for words
		for(GridPos gridPos : gridpositions)
		{
			checkForWords(gridPos);
		}
		
		//Check if our actions resulted in any new words and get the length of the longest word
		int wordLength = isPartOfWord(gridpositions);
		//Play a sound clip if any new words were found
		if(wordLength > 0)
			playWordSound(wordLength);
	}
	
	private ArrayList<Word> findBestWords(ArrayList<Word> foundWords)
	{
		int numOfWordComb = (int)Math.pow(2, foundWords.size());
		
		ArrayList<ArrayList<Word>> wordCombinations = new ArrayList<ArrayList<Word>>(numOfWordComb);
		
		//Get all possible word combinations
		
		//For every combination
		for(int i = 0; i < numOfWordComb; i++)
		{
			//Add a new combination
			ArrayList<Word> comb = new ArrayList<Word>();
			wordCombinations.add(comb);
			
			//For every word
			for(int j = 0; j < foundWords.size(); j++)
			{
				//Check if the word should be part of the combination
				if((((int)Math.pow(2, j)) & i) == ((int)Math.pow(2, j)))//Is j set in i?
				{
					comb.add(foundWords.get(j));
				}
			}
		}
		
		Iterator<ArrayList<Word>> combIterator = wordCombinations.iterator();
		
		//Get all valid combinations
		while(combIterator.hasNext())
		{
			boolean combInvalid = false;
			ArrayList<Word> comb = combIterator.next();

			for(int i = 0; i < comb.size(); i++)
			{
				for(int j = i + 1; j < comb.size(); j++)
				{
					//If a word intersects another word or is part of another word
					if(comb.get(i).intersectsWord(comb.get(j)) || 
					   comb.get(i).isInsideWord(comb.get(j)) || 
					   comb.get(j).isInsideWord(comb.get(i)))
					{
						combInvalid = true;
						break;
					}
				}
				
				if(combInvalid)
				{
					break;
				}
			}
			
			if(combInvalid)
			{
				combIterator.remove();
			}
		}
		
		ArrayList<Word> bestComb = new ArrayList<Word>();
		int bestScore = Integer.MIN_VALUE;
		int score;
		
		for(ArrayList<Word> comb : wordCombinations)
		{
			score = 0;
			for(Word word : comb)
			{
				score += word.getScore();
			}
			
			if(score > bestScore)
			{
				bestScore = score;
				bestComb = comb;
			}
		}
		
		return bestComb;
	}
	
	private void checkForWords(String wordString, int startRow, int endRow, int startColumn, int endColumn)
	{
		//All words possible with the word string
		ArrayList<Word> foundWords = new ArrayList<Word>();
		//The subset of found words that we are actually going to use
		ArrayList<Word> usedWords;
		
		int oldStartRow = startRow;
		int oldEndRow = endRow;
		int oldStartColumn = startColumn;
		int oldEndColumn = endColumn;
		String oldWordString = wordString;
		
		if(startRow == endRow)//If this is a horizontal word
		{
			do
			{
				//Find all words of the word string
				while(!wordString.isEmpty())
				{	
					if(WordOnWord.wordChecker.isValidWord(wordString))
					{
						if(wordString.length() > 1 || //Accept all words longer than 1
						   (wordString.length() == 1 && WordOnWord.language == WordOnWord.SWEDISH && (wordString.equals("Å") || wordString.equals("Ö"))) ||//Accept å and ö for swedish
						   (wordString.length() == 1 && WordOnWord.language == WordOnWord.ENGLISH && (wordString.equals("A") || wordString.equals("I"))))  //Accept a and i for english
							//No one letter words for german
						{
							foundWords.add(new Word(wordString, startRow, endRow, startColumn, endColumn));
						}
					}
					
					if(wordString.length() > 1)
					{
						wordString = wordString.substring(0, wordString.length() - 1);
					}
					else//Can't divide up the string any more
					{
						wordString = "";
					}
					
					endColumn--;
				}
				
				endColumn = oldEndColumn;
				
				//Increment the startColumn
				startColumn++;
				//Get the next word string to test
				if(startColumn <= endColumn)
					wordString = oldWordString.substring(startColumn - oldStartColumn, oldWordString.length());
				
			} while(startColumn <= endColumn);
			
			
		}
		else//This is a vertical word
		{
			do
			{
				//Try all substrings of the character sequence
				while(!wordString.isEmpty())
				{
					if(WordOnWord.wordChecker.isValidWord(wordString))
					{
						if(wordString.length() > 1 || //Accept all words longer than 1
						  (wordString.length() == 1 && WordOnWord.language == WordOnWord.SWEDISH && (wordString.equals("Å") || wordString.equals("Ö"))) ||//Accept å and ö for swedish
						  (wordString.length() == 1 && WordOnWord.language == WordOnWord.ENGLISH && (wordString.equals("A") || wordString.equals("I"))))  //Accept a and i for english
						   //No one letter words for german
						{
							foundWords.add(new Word(wordString, startRow, endRow, startColumn, endColumn));
						}
					}
					
					if(wordString.length() > 1)
					{
						wordString = wordString.substring(0, wordString.length() - 1);
					}
					else//Can't divide up the string any more
					{
						wordString = "";
					}
					
					endRow--;
				}
				
				endRow = oldEndRow;
				
				//Increment the startRow
				startRow++;
				//Get the next word string to test
				if(startRow <= endRow)
					wordString = oldWordString.substring(startRow - oldStartRow, oldWordString.length());
				
			} while(startRow <= endRow);
		}
		
		//Find the combination of words that give the most points
		usedWords = findBestWords(foundWords);
		
		//Add the words
		for(Word word : usedWords)
		{
			addWord(word);
		}
	}
	
	private void checkForWords(GridPos gridPos)
	{
		boolean inCharSequence;
		StringBuilder stringBuilder;
		
		boolean wordsUpdated = false;
		
		synchronized(wordsLock)
		{
			//Remove all previously saved words for the grid position's row and column
			while(!wordsUpdated)
			{
				wordsUpdated = true;
				
				for(Word word : words)
				{
					if(word.startRow == gridPos.row && word.startRow == word.endRow ||
					   word.startColumn == gridPos.column && word.startColumn == word.endColumn)
					{
						words.remove(word);
						wordsUpdated = false;
						break;
					}
				}
			}
		}
		
		boolean emptyFirstCell;
		int startIndex = 0;
		int startColumn = 0;
		int endColumn = 0;
		
		while(startIndex < GRID_COLUMNS)
		{
			emptyFirstCell = false;
			inCharSequence = false;
			stringBuilder = new StringBuilder();
			
			for(int i = startIndex; i < GRID_COLUMNS; i++)
			{	
				if(wordGrid[gridPos.row][i] != null)
				{
					stringBuilder.append(wordGrid[gridPos.row][i].c);
					if(!inCharSequence)
					{
						//We have encountered a character sequence that could be a word
						inCharSequence = true;
						//Save the start column of the character sequence
						startColumn = i;
					}
				}
				else if(inCharSequence)//We have reached the end of a character sequence
				{
					String word = stringBuilder.toString();
					endColumn = i - 1;
					
					checkForWords(word, gridPos.row, gridPos.row, startColumn, endColumn);
					
					inCharSequence = false;
					//We have processed a character sequence and should move on to a new start element
					break;
				}
				else//The first element is null so we try another start element
				{
					emptyFirstCell = true;
					break;
				}
			}
			
			//We have a character sequence which continues to the last column
			if(inCharSequence)
			{
				String word = stringBuilder.toString();
				endColumn = GRID_COLUMNS - 1;
				
				checkForWords(word, gridPos.row, gridPos.row, startColumn, endColumn);
			}
			
			if(emptyFirstCell)
			{
				startIndex++;
			}
			else
			{
				startIndex = endColumn + 1;
			}
		}
		
		int startRow = 0;
		int endRow = 0;
		startIndex = 0;
		
		//Check for vertical words
		while(startIndex < GRID_ROWS)
		{
			emptyFirstCell = false;
			inCharSequence = false;
			stringBuilder = new StringBuilder();
			//oldStartIndex = startIndex;
			
			for(int i = startIndex; i < GRID_ROWS; i++)
			{
				if(wordGrid[i][gridPos.column] != null)
				{
					stringBuilder.append(wordGrid[i][gridPos.column].c);
					if(!inCharSequence)
					{
						//We have encountered a character sequence that could be a word
						inCharSequence = true;
						//Save the start row of the character sequence
						startRow = i;
					}
				}
				else if(inCharSequence)//We have reached the end of a character sequence
				{
					String word = stringBuilder.toString();
					endRow = i - 1;
					
					checkForWords(word, startRow, endRow, gridPos.column, gridPos.column);
					
					inCharSequence = false;
					//We have processed a character sequence and should move on to a new start element
					break;
				}
				else//The first element is null so we can try another start element
				{
					emptyFirstCell = true;
					break;
				}
			}
			
			//We have a character sequence which continues to the last row
			if(inCharSequence)
			{
				String word = stringBuilder.toString();
				endRow = GRID_ROWS - 1;
				
				checkForWords(word, startRow, endRow, gridPos.column, gridPos.column);
			}
			
			if(emptyFirstCell)
			{
				startIndex++;
			}
			else
			{
				startIndex = endRow + 1;
			}
		}
	}
	
	public void startGame()
	{
		inPrepPhase = false;
		//Signal to the mouse controllers that the preperation phase has ended
		player.startGame();
		//Initiate the character block dropper
		charRectDropper.initCharacterRectDropper();
		//Check the initial character blocks for words
		checkCurrentGridForWords();
	}
	
	public void addJokerBlock(char c)
	{
		charRectDropper.addJokerBlock(new CharacterRect(c, GameEngine.STATUS_FIELD_WIDTH, 0));
		jokerAdded = true;
	}
	
	private int isPartOfWord(Vector<GridPos> gridPositions)
	{	
		synchronized(wordsLock)
		{
			int wordLength = 0;
			
			for(GridPos gridPos : gridPositions)
			{
				for(Word word : words)
				{
					if(word.insideWordBounds(gridPos) && word.toString().length() > wordLength)
					{
						wordLength = word.toString().length();
					}
				}
			}
			
			return wordLength;
		}
	}
	
	private void playWordSound(int wordLength)
	{	
		//Play the apprioriate word sound(determined by the length of the word)
		WordOnWord.soundPlayer.playClip("word_" + wordLength);
	}
	
	//Static methods
	
	public static int getCanvasXPos(int column)
	{	
		return STATUS_FIELD_WIDTH + column * CELL_WIDTH;
	}
	
	public static int getGridColumn(int xPos)
	{
		if((xPos - STATUS_FIELD_WIDTH) / CELL_WIDTH < 0)
		{
			return 0;
		}
		else
		{
			return (xPos - STATUS_FIELD_WIDTH) / CELL_WIDTH;
		}
	}
	
	public static int getCanvasYPos(int row)
	{	
		return DROP_ZONE_HEIGHT + row * CELL_HEIGHT;
	}
	
	public static int getGridRow(int yPos)
	{	
		if((yPos - DROP_ZONE_HEIGHT) / CELL_HEIGHT < 0)
		{
			return 0;
		}
		else
		{
			return (yPos - DROP_ZONE_HEIGHT) / CELL_HEIGHT;
		}
	}
}

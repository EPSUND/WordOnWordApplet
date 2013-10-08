package gui;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.awt.Point;

import main.CharacterRect;
import main.CharacterRectDropper;
import main.GameEngine;
import main.Word;
import main.WordOnWord;

import usrinput.MouseControl;
import usrinput.MouseMotionControl;
import utils.Helpers;

public class WoWCanvas extends Canvas {
	
	private static final long serialVersionUID = 1L;
	public static final int FRAME_DELAY = 10;		// Update interval.
	
	//Colors
	public static final Color BACKGROUND_COLOR = Color.BLACK;
	private static final Color STATUS_TEXT_COLOR = Color.WHITE;
	private static final Color WIN_TEXT_COLOR = Color.GREEN;
	private static final Color LOSE_TEXT_COLOR = Color.RED; 
	
	//Textures
	private static final String BACKGROUND_IMAGE = "brick_texture.jpg";
	private static final String STATUS_FIELD_IMAGE = "wood_texture2.jpg";
	private static final String DROP_ZONE_IMAGE = "wood_texture2.jpg";
	
	/* Size of a single pane (Standard size of objects). */
	public final int PANE_SIZE = 30;
	
	private ImageCache imageCache;	// The image cache for the canvas.	
	
	private Graphics2D graphicsContext;
	
	public BufferStrategy strategy; // Double buffered strategy.
	
	/*Fonts*/
	private Font statusFieldFont; //The font of the text in the status field
	private Font winLoseFont; //The font to write the win or lose messages
	private Font wordFont;
	
	private int pf_width, pf_height;
	
	/**
	 * BreakoutCanvas constructor
	 * Creates a canvas for the game.
	 */
	public WoWCanvas() {
		imageCache = new ImageCache();			// Create an image cache.
		
		/*Make the font for the text in the status field*/
		statusFieldFont = new Font("Serif", Font.BOLD, 20);
		/*Make the font for the text in the win/lose messages*/
		winLoseFont = new Font("SansSerif", Font.BOLD, 40);
		/*Make the font to use for the words*/
		wordFont = new Font("SansSerif", Font.BOLD, 50);
		
		/* Set the size of the canvas. */
		this.setBounds(0, 0, WordOnWord.DEFAULT_WINDOW_WIDTH + GameEngine.STATUS_FIELD_WIDTH, WordOnWord.DEFAULT_WINDOW_HEIGHT + GameEngine.DROP_ZONE_HEIGHT);
	}
	
	public void initMouseListeners(MouseMotionControl mouseMotionController, MouseControl mouseController) {
		addMouseMotionListener(mouseMotionController);
		addMouseListener(mouseController);
	}

	/**
	 * createStrategy
	 * Creates a double buffering strategy for this canvas.
	 */
	public void createStrategy() {
		this.setVisible(true);	// Show the canvas.

		/* Make sure that the canvas is visible. */
		if (this.isDisplayable()) {
			/* Create double buffering. */
			this.createBufferStrategy(2);
			this.strategy = this.getBufferStrategy();

			/* Set the focus. */
			this.requestFocus();
		} else {
			System.err.println("WoW: Could not enable double buffering.");
			System.exit(1);	// Exit Word on Word.
		}
	}

	/**
	 * imageChache
	 * Gets the canvas-specific image cache.
	 * return image cache
	 */
	public ImageCache getImageCache() {
		return imageCache;
	}
	
	/**
	 * initGraphicsContext
	 * Initiates the graphics context
	 */
	public void initGraphicsContext() {
		graphicsContext = (Graphics2D) strategy.getDrawGraphics();
	}
	
	/**
	 * showGraphicsBuffer
	 * Makes the next available graphics buffer visible
	 */
	public void showGraphicsBuffer() {
		/*Dispose the graphics object.*/
		graphicsContext.dispose();
		/*Show buffer.*/
		strategy.show();
	}
	
	/**
	 * paintBackground
	 * Paints the background
	 */
	public void paintBackground() {
		/* Set up the background */
		graphicsContext.setBackground(BACKGROUND_COLOR);
		/*Clear the canvas*/
		paint(graphicsContext);
		/*Draw the background image*/
		graphicsContext.drawImage(imageCache.getImage(Helpers.getResourceURIString(this, "images/" + BACKGROUND_IMAGE)), 0, 0, getWidth(), getHeight(), this);
		
		//Draw the grid
		
		graphicsContext.setColor(Color.WHITE);
		
		for(int i = 0; i <= WordOnWord.CANVAS_GRID_WIDTH; i += GameEngine.CELL_WIDTH)
		{
			graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH + i, GameEngine.DROP_ZONE_HEIGHT, GameEngine.STATUS_FIELD_WIDTH + i, getHeight());
		}
		
		for(int i = 0; i <= WordOnWord.CANVAS_GRID_HEIGHT; i += GameEngine.CELL_HEIGHT)
		{
			graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH, GameEngine.DROP_ZONE_HEIGHT + i, getWidth(), GameEngine.DROP_ZONE_HEIGHT + i);
		}
	}
	
	public void paint(CharacterRect charRect)
	{	
		//Draw the rectangle the character will be placed in
		graphicsContext.setColor(Color.YELLOW);
		graphicsContext.fillRect(charRect.x, charRect.y, charRect.width, charRect.height);
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawRect(charRect.x, charRect.y, charRect.width, charRect.height);
		
		//Draw the character
		
		graphicsContext.setFont(wordFont);
		graphicsContext.setColor(Color.BLACK);
		
		// Get font metrics
		FontMetrics metrics = graphicsContext.getFontMetrics(wordFont);
		// Get the height of the character in this font and render context
		int charHeight = metrics.getHeight();
		// Get the width of the character in this font and render context
		int charWidth = metrics.charWidth(charRect.c);
		//Draw the character
		graphicsContext.drawString(String.valueOf(charRect.c), 
								  charRect.x + charRect.width / 2 - charWidth / 2, 
								  charRect.y + charRect.height / 2 + charHeight / 4);//Varför funkar /4? Borde vara /2.
	}
	
	public void paintCharacter(int row, int column, char c)
	{
		//Draw the rectangle the character will be placed in
		graphicsContext.setColor(Color.GRAY);
		graphicsContext.fillRect(GameEngine.STATUS_FIELD_WIDTH + column * GameEngine.CELL_WIDTH, GameEngine.DROP_ZONE_HEIGHT + row * GameEngine.CELL_HEIGHT, GameEngine.CELL_WIDTH, GameEngine.CELL_HEIGHT);
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawRect(GameEngine.STATUS_FIELD_WIDTH + column * GameEngine.CELL_WIDTH, GameEngine.DROP_ZONE_HEIGHT + row * GameEngine.CELL_HEIGHT, GameEngine.CELL_WIDTH, GameEngine.CELL_HEIGHT);
		
		//Draw the character
		
		graphicsContext.setFont(wordFont);
		graphicsContext.setColor(Color.BLACK);
		
		// Get font metrics
		FontMetrics metrics = graphicsContext.getFontMetrics(wordFont);
		// Get the height of the character in this font and render context
		int charHeight = metrics.getHeight();
		// Get the width of the character in this font and render context
		int charWidth = metrics.charWidth(c);
		//Draw the character
		graphicsContext.drawString(String.valueOf(c), 
								  GameEngine.STATUS_FIELD_WIDTH + column * GameEngine.CELL_WIDTH + GameEngine.CELL_WIDTH / 2 - charWidth / 2, 
								  GameEngine.DROP_ZONE_HEIGHT + row * GameEngine.CELL_HEIGHT + GameEngine.CELL_HEIGHT / 2 + charHeight / 4);//Varför funkar /4? Borde vara /2.
	}
	
	/**
	 * paintStatusField
	 * Paints the status field
	 */
	public void paintStatusField(int score, int words) {
		//Draw background texture for the status field
		graphicsContext.drawImage(imageCache.getImage(Helpers.getResourceURIString(this, "images/" + STATUS_FIELD_IMAGE)), 0, GameEngine.DROP_ZONE_HEIGHT, GameEngine.STATUS_FIELD_WIDTH, getHeight() - GameEngine.DROP_ZONE_HEIGHT, this);
		//Make sure the graphics context uses the status field font
		graphicsContext.setFont(statusFieldFont);
		//Draw the score
		drawShadowedText(0, GameEngine.DROP_ZONE_HEIGHT + GameEngine.CELL_HEIGHT - 2, "Score:", statusFieldFont, STATUS_TEXT_COLOR);
		drawShadowedText(0, GameEngine.DROP_ZONE_HEIGHT + GameEngine.CELL_HEIGHT + graphicsContext.getFontMetrics().getHeight() - 2, Integer.toString(score), statusFieldFont, STATUS_TEXT_COLOR);
		//Draw the number of words
		drawShadowedText(0, GameEngine.DROP_ZONE_HEIGHT + GameEngine.CELL_HEIGHT * 2 - 2, "Words:", statusFieldFont, STATUS_TEXT_COLOR);
		drawShadowedText(0, GameEngine.DROP_ZONE_HEIGHT + GameEngine.CELL_HEIGHT * 2 + graphicsContext.getFontMetrics().getHeight() - 2, Integer.toString(words), statusFieldFont, STATUS_TEXT_COLOR);
		//Draw the line separating the status field from the playing field
		Stroke oldStroke = graphicsContext.getStroke();
		graphicsContext.setStroke(new BasicStroke(2));//Make the line thicker
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH, GameEngine.DROP_ZONE_HEIGHT, GameEngine.STATUS_FIELD_WIDTH, pf_height);
		graphicsContext.setStroke(oldStroke);
	}
	
	public void paintDropZone()
	{
		//Draw the background texture for the drop zone
		graphicsContext.drawImage(imageCache.getImage(Helpers.getResourceURIString(this, "images/" + DROP_ZONE_IMAGE)), 0, 0, getWidth(), GameEngine.DROP_ZONE_HEIGHT, this);
		//Draw the line separating the drop zone from the word grid
		Stroke oldStroke = graphicsContext.getStroke();
		graphicsContext.setStroke(new BasicStroke(2));//Make the line thicker
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawLine(0, GameEngine.DROP_ZONE_HEIGHT, pf_width, GameEngine.DROP_ZONE_HEIGHT);
		graphicsContext.setStroke(oldStroke);
	}
	
	public void paint(CharacterRectDropper charRectDropper)
	{
		//Must have synchronized access to the character block dropper
		charRectDropper.paintBlocks(this);
	}
	
	/**
	 * paintLoseMessage
	 * Paints the lose message
	 */
	public void paintLoseMessage() {
		/*Make sure the graphics context uses the win/lose font*/
		graphicsContext.setFont(winLoseFont);
		/*Draw the lose message's shadow*/
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawString("GAME OVER", (pf_width / 2) - 170 + 2, pf_height / 2 + 2);
		/*Draw the text of the lose message*/
		graphicsContext.setColor(LOSE_TEXT_COLOR);
		graphicsContext.drawString("GAME OVER", (pf_width / 2) - 170, pf_height / 2);
	}
	
	/**
	 * paintWinMessage
	 * Paints the win message
	 */
	public void paintWinMessage() {
		/*Make sure the graphics context uses the win/lose font*/
		graphicsContext.setFont(winLoseFont);
		/*Draw the shadow of the win message*/
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawString("YOU WIN!", (pf_width / 2) - 170 + 2, pf_height / 2 + 2);
		/*Draw the text of the win message*/
		graphicsContext.setColor(WIN_TEXT_COLOR);
		graphicsContext.drawString("YOU WIN!", (pf_width / 2) - 170, pf_height / 2);
	}

	/**
	 * @param width the width to set
	 */
	public void setPlayingFieldWidth(int width) {
		pf_width = width;
	}

	/**
	 * @return the playing field width
	 */
	public int getPlayingFieldWidth() {
		return pf_width;
	}

	/**
	 * @param height the height to set
	 */
	public void setPlayingFieldHeight(int height) {
		pf_height = height;
	}

	/**
	 * @return the playing field height
	 */
	public int getPlayingFieldHeight() {
		return pf_height;
	}
	
	private void drawShadowedText(int x, int y, String text, Font font, Color color)
	{
		//Set the font
		graphicsContext.setFont(font);
		//Draw the shadow
		graphicsContext.setColor(Color.BLACK);
		graphicsContext.drawString(text, x + 2, y + 2);
		//Draw the text
		graphicsContext.setColor(color);
		graphicsContext.drawString(text, x, y);
	}
	
	public void paint(Word word)
	{
		//Save the stroke to be able to restore the line thickness later
		Stroke oldStroke = graphicsContext.getStroke();
		
		graphicsContext.setStroke(new BasicStroke(4));//Increase the line thickness
		graphicsContext.setColor(Color.RED);
		
		if(word.startRow == word.endRow)//We have a horizionatal word or a word of one character
		{
			//Draw the upper line
			graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH + word.startColumn * GameEngine.CELL_WIDTH + WordOnWord.WORD_RING_SMALL_SPACING * 2,
									 GameEngine.DROP_ZONE_HEIGHT + word.startRow * GameEngine.CELL_HEIGHT + WordOnWord.WORD_RING_BIG_SPACING,
									 GameEngine.STATUS_FIELD_WIDTH + word.endColumn * GameEngine.CELL_WIDTH + GameEngine.CELL_WIDTH - WordOnWord.WORD_RING_SMALL_SPACING * 2,
									 GameEngine.DROP_ZONE_HEIGHT + word.endRow * GameEngine.CELL_HEIGHT + WordOnWord.WORD_RING_BIG_SPACING);
			//Draw the lower line
			graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH + word.startColumn * GameEngine.CELL_WIDTH + WordOnWord.WORD_RING_SMALL_SPACING * 2,
									 GameEngine.DROP_ZONE_HEIGHT + word.startRow * GameEngine.CELL_HEIGHT + GameEngine.CELL_HEIGHT - WordOnWord.WORD_RING_BIG_SPACING,
									 GameEngine.STATUS_FIELD_WIDTH + word.endColumn * GameEngine.CELL_WIDTH + GameEngine.CELL_WIDTH - WordOnWord.WORD_RING_SMALL_SPACING * 2,
									 GameEngine.DROP_ZONE_HEIGHT + word.endRow * GameEngine.CELL_HEIGHT + GameEngine.CELL_HEIGHT - WordOnWord.WORD_RING_BIG_SPACING);
			
			//Draw the left arc
			graphicsContext.drawArc(GameEngine.STATUS_FIELD_WIDTH + word.startColumn * GameEngine.CELL_WIDTH + WordOnWord.WORD_RING_SMALL_SPACING, 
									GameEngine.DROP_ZONE_HEIGHT + word.startRow * GameEngine.CELL_HEIGHT + WordOnWord.WORD_RING_BIG_SPACING, 
									WordOnWord.WORD_RING_SMALL_SPACING * 2, 
									GameEngine.CELL_HEIGHT - WordOnWord.WORD_RING_BIG_SPACING * 2, 
									90, 180);
			//Draw the right arc
			graphicsContext.drawArc(GameEngine.STATUS_FIELD_WIDTH + word.endColumn * GameEngine.CELL_WIDTH + GameEngine.CELL_WIDTH - WordOnWord.WORD_RING_SMALL_SPACING * 3, 
									GameEngine.DROP_ZONE_HEIGHT + word.endRow * GameEngine.CELL_HEIGHT + WordOnWord.WORD_RING_BIG_SPACING, 
									WordOnWord.WORD_RING_SMALL_SPACING * 2, 
									GameEngine.CELL_HEIGHT - WordOnWord.WORD_RING_BIG_SPACING * 2, 
									270, 180);
		}
		else//We have a vertical word
		{
			//Draw the left line
			graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH + word.startColumn * GameEngine.CELL_WIDTH + WordOnWord.WORD_RING_BIG_SPACING,
									 GameEngine.DROP_ZONE_HEIGHT + word.startRow * GameEngine.CELL_HEIGHT + WordOnWord.WORD_RING_SMALL_SPACING * 2,
									 GameEngine.STATUS_FIELD_WIDTH + word.endColumn * GameEngine.CELL_WIDTH + WordOnWord.WORD_RING_BIG_SPACING,
									 GameEngine.DROP_ZONE_HEIGHT + word.endRow * GameEngine.CELL_HEIGHT + GameEngine.CELL_HEIGHT - WordOnWord.WORD_RING_SMALL_SPACING * 2);
			//Draw the right line
			graphicsContext.drawLine(GameEngine.STATUS_FIELD_WIDTH + word.startColumn * GameEngine.CELL_WIDTH + GameEngine.CELL_WIDTH - WordOnWord.WORD_RING_BIG_SPACING,
									 GameEngine.DROP_ZONE_HEIGHT + word.startRow * GameEngine.CELL_HEIGHT + WordOnWord.WORD_RING_SMALL_SPACING * 2,
									 GameEngine.STATUS_FIELD_WIDTH + word.endColumn * GameEngine.CELL_WIDTH + GameEngine.CELL_WIDTH - WordOnWord.WORD_RING_BIG_SPACING,
									 GameEngine.DROP_ZONE_HEIGHT + word.endRow * GameEngine.CELL_HEIGHT + GameEngine.CELL_HEIGHT - WordOnWord.WORD_RING_SMALL_SPACING * 2);
			
			//Draw the upper arc
			graphicsContext.drawArc(GameEngine.STATUS_FIELD_WIDTH + word.startColumn * GameEngine.CELL_WIDTH + WordOnWord.WORD_RING_BIG_SPACING, 
									GameEngine.DROP_ZONE_HEIGHT + word.startRow * GameEngine.CELL_HEIGHT + WordOnWord.WORD_RING_SMALL_SPACING, 
									GameEngine.CELL_WIDTH - WordOnWord.WORD_RING_BIG_SPACING * 2, 
									WordOnWord.WORD_RING_SMALL_SPACING * 2, 
									0, 180);
			//Draw the lower arc
			graphicsContext.drawArc(GameEngine.STATUS_FIELD_WIDTH + word.endColumn * GameEngine.CELL_WIDTH + WordOnWord.WORD_RING_BIG_SPACING, 
									GameEngine.DROP_ZONE_HEIGHT + word.endRow * GameEngine.CELL_HEIGHT + GameEngine.CELL_HEIGHT - WordOnWord.WORD_RING_SMALL_SPACING * 3, 
									GameEngine.CELL_WIDTH - WordOnWord.WORD_RING_BIG_SPACING * 2, 
									WordOnWord.WORD_RING_SMALL_SPACING * 2, 
									180, 180);
		}
		
		//Reset the line thickness
		graphicsContext.setStroke(oldStroke);
	}
}

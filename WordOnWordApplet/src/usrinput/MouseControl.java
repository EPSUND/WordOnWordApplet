package usrinput;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import main.CharacterRect;
import main.CharacterRectDropper;
import main.CharacterRectListener;
import main.GameEngine;

public class MouseControl implements MouseListener {

	private CharacterRectDropper charRectDropper;
	private CharacterRect[][] wordGrid;
	private boolean inPrepPhase;
	private Vector<CharacterRectListener> charRectListeners;
	
	public MouseControl(CharacterRectDropper charRectDropper, CharacterRect[][] wordGrid) {
		this.charRectDropper = charRectDropper;
		this.wordGrid = wordGrid;
		
		inPrepPhase = true;
		
		charRectListeners = new Vector<CharacterRectListener>();
	}
	
	public void addCharacterRectListener(CharacterRectListener listener)
	{
		charRectListeners.add(listener);
	}
	
	private void updateCharacterRectListeners(CharacterRect block, int row, int column)
	{
		for(CharacterRectListener listener : charRectListeners)
		{
			listener.processCharacterRect(block, row, column);
		}
	}
	
	public void startGame()
	{
		inPrepPhase = false;
	}
	
	public void restartGame()
	{
		inPrepPhase = true;
	}
	
	public void mouseClicked(MouseEvent me) {
		if(!inPrepPhase)
			charRectDropper.dropBlock(wordGrid);
	}

	public void mouseEntered(MouseEvent me) {
		//Nothing happens

	}

	public void mouseExited(MouseEvent me) {
		//Nothing happens

	}

	public void mousePressed(MouseEvent me) 
	{
		//Retrive the character block that is under the mouse 
		if(inPrepPhase)
		{
			int row = GameEngine.getGridRow(me.getY());
			int column = GameEngine.getGridColumn(me.getX());
			
			updateCharacterRectListeners(wordGrid[row][column], row, column);
		}
	}

	public void mouseReleased(MouseEvent me) 
	{
		//Make the active block null
		if(inPrepPhase)
		{
			updateCharacterRectListeners(null, -1, -1);
		}
	}
}

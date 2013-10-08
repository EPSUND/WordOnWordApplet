package usrinput;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;

import main.CharacterRect;
import main.CharacterRectListener;
import main.GameEngine;

import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.AWTException;

public class MouseMotionControl implements MouseMotionListener, CharacterRectListener {
	public Point mousePos;
	private CharacterRect activeBlock; 
	private int abRow, abColumn;
	private CharacterRect[][] wordGrid;
	private boolean inPrepPhase;
	
	public MouseMotionControl(CharacterRect[][] wordGrid) 
	{
		this.wordGrid = wordGrid;
		
		inPrepPhase = true;
		mousePos = null;
		activeBlock = null;
		abRow = -1;
		abColumn = -1;
	}
	
	public synchronized void startGame()
	{
		inPrepPhase = false;
	}
	
	public synchronized void restartGame()
	{
		inPrepPhase = true;
	}
	
	public synchronized void mouseDragged(MouseEvent me) {
		mouseMoved(me);
		
		if(inPrepPhase)
		{
			int mouseRow = GameEngine.getGridRow(mousePos.y);
			int mouseColumn = GameEngine.getGridColumn(mousePos.x);
			
			if(abRow == mouseRow && abColumn == mouseColumn)//If we have not moved the mouse outside the block's cell
			{
				//Do nothing
				return;
			}
			else
			{
				if(wordGrid[mouseRow][mouseColumn] != null)//There is a block we can switch places with
				{
					moveActiveBlock(mouseRow, mouseColumn);
				}
				else
				{
					int highestRow = GameEngine.GRID_ROWS - 1;//At the bottom
					
					for(int i = mouseRow; i < GameEngine.GRID_ROWS - 1; i++)
					{
						if(wordGrid[i + 1][mouseColumn] != null && 
						   wordGrid[i + 1][mouseColumn] != activeBlock)
						{
							highestRow = i;
							break;
						}
					}
					
					//Save the old position of the active block
					int oldRow = abRow;
					int oldColumn = abColumn;
					
					//Move the block to the right column, and the highest row possible 
					moveActiveBlock(highestRow, mouseColumn);
					
					//Update the position of all blocks above the active block's previous cell
					for(int i = oldRow; i > 0; i--)
					{
						if(wordGrid[i - 1][oldColumn] != null)//We have a block above us
						{
							//Move down the block
							moveBlock(wordGrid[i - 1][oldColumn], i, oldColumn);
						}
						else//We have no blocks above us
						{
							//We are done
							break;
						}
					}
				}
			}
		}
	}
	
	public synchronized void mouseMoved(MouseEvent me)
	{
		mousePos = new Point(me.getPoint().x, me.getPoint().y);
	}
	
	public synchronized void processCharacterRect(CharacterRect block, int row, int column)
	{
		activeBlock = block;
		abRow = row;
		abColumn = column;
	}
	
	private void moveBlock(CharacterRect block, int row, int column)
	{
		//Save the block's row and column
		int blockRow = GameEngine.getGridRow(block.y);
		int blockColumn = GameEngine.getGridColumn(block.x);
		
		//Move the block on the canvas
		block.setPosition(GameEngine.getCanvasXPos(column),
								GameEngine.getCanvasYPos(row));
		
		//Save the old block
		CharacterRect oldBlock = wordGrid[row][column];
		
		//Move the active block in the grid
		wordGrid[row][column] = block;
		
		//If there was a block already at the position we moved the block let it switch places with the active block
		if(oldBlock != null)
		{
			oldBlock.setPosition(GameEngine.getCanvasXPos(blockColumn),
					GameEngine.getCanvasYPos(blockRow));
			wordGrid[blockRow][blockColumn] = oldBlock;
		}
		else
		{
			wordGrid[blockRow][blockColumn] = null;
		}
	}
	
	private void moveActiveBlock(int row, int column)
	{
		//Move the block on the canvas
		activeBlock.setPosition(GameEngine.getCanvasXPos(column),
								GameEngine.getCanvasYPos(row));
		
		//Save the old block
		CharacterRect oldBlock = wordGrid[row][column];
		
		//Move the active block in the grid
		wordGrid[row][column] = activeBlock;
		
		//If there was a block already at the position we moved the block let it switch places with the active block
		if(oldBlock != null)
		{
			oldBlock.setPosition(GameEngine.getCanvasXPos(abColumn),
					GameEngine.getCanvasYPos(abRow));
			wordGrid[abRow][abColumn] = oldBlock;
		}
		else
		{
			wordGrid[abRow][abColumn] = null;
		}
		
		//Update the active block's row and column
		abRow = row;
		abColumn = column;
	}
}

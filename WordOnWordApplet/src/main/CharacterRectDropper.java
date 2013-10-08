package main;
import gui.WoWCanvas;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;


public class CharacterRectDropper {
	//Character rectacles that are falling and have not reached the bottom yet. These rectangles have no place in the grid yet.
	private Vector<CharacterRect> fallingCharRects;
	private CharacterRect currentBlock, nextBlock, savedBlock;
	private boolean blockAligned, jokerAdded, hasBlocksLeft;
	private int numDroppedBlocks;
	
	//CharacterRectDropper assumes the word grid is atleast (1  x 2) or (2 x 1) big
	
	public CharacterRectDropper()
	{	
		nextBlock = null;
		currentBlock = null;
		savedBlock = null;
		blockAligned = false;
		jokerAdded = false;
		hasBlocksLeft = true;
		numDroppedBlocks = 0;
		
		fallingCharRects = new Vector<CharacterRect>();
	}
	
	public synchronized void resetCharacterRectDropper()
	{
		nextBlock = null;
		currentBlock = null;
		savedBlock = null;
		blockAligned = false;
		jokerAdded = false;
		hasBlocksLeft = true;
		numDroppedBlocks = 0;
		
		fallingCharRects.clear();
	}
	
	//The character rect dropper is initially disabled and needs to be initiated before it can be used
	public synchronized void initCharacterRectDropper()
	{
		nextBlock = new CharacterRect(0, 0);
		currentBlock = new CharacterRect(GameEngine.STATUS_FIELD_WIDTH, 0);
		//Must set it here too because of the initial falling blocks
		hasBlocksLeft = true;
	}
	
	public synchronized void addJokerBlock(CharacterRect jokerBlock)
	{
		//Temporaily discard the current block and make the joker block the current block
		savedBlock = currentBlock;
		currentBlock = jokerBlock;
		
		jokerAdded = true;
		hasBlocksLeft = true;
	}
	
	public synchronized void dropBlock(CharacterRect[][] wordGrid)
	{	
		//We can't drop a block if we don't have one
		if(currentBlock == null)
			return;
		
		//Can not drop at the moment since the previous block has not reached the bottom or the block is not aligned with the grid
		if(!blockAligned || fallingCharRects.size() > 0)
			return;
		
		//Can't drop a block if the space it would occupy is already filled
		if(wordGrid[0][GameEngine.getGridColumn(currentBlock.x)] != null)
			return;
		
		//Can not drop blocks if we have already filled the grid and we have not added a joker block
		if(!jokerAdded && (numDroppedBlocks >= (GameEngine.GRID_ROWS * GameEngine.GRID_COLUMNS - 1)))
			return;
		
		//Move the block into the grid
		currentBlock.yMove(GameEngine.DROP_ZONE_HEIGHT);
		
		CharacterRect tempBlock;
		
		if(nextBlock != null && !jokerAdded)
		{
			//Move the next block into the drop zone
			nextBlock.xMove(GameEngine.STATUS_FIELD_WIDTH);
		}
		
		tempBlock = currentBlock;
		
		if(jokerAdded)
			currentBlock = savedBlock;
		else
			currentBlock = nextBlock;
		
		if(jokerAdded)
		{
			//Do nothing
		}
		else if((numDroppedBlocks + 2) < (GameEngine.GRID_ROWS * GameEngine.GRID_COLUMNS - 1))//numDroppedBlocks is incremented by 2 because we have not counted the current block yet
		{
			//Make the new next block
			nextBlock = new CharacterRect(0, 0);
		}
		else
		{
			//We have already dropped all available blocks and should not generate a new one 
			nextBlock = null;
		}
		
		//Add the block to fallingCharacterRects
		addFallingBlock(tempBlock);
	}
	
	public synchronized void paintBlocks(WoWCanvas canvas)
	{
		//Paint the blocks in the drop zone
		if(currentBlock != null)
			canvas.paint(currentBlock);
		if(nextBlock != null)
			canvas.paint(nextBlock);
		
		//Paint falling character blocks
		for(CharacterRect charRect : fallingCharRects)
		{
			canvas.paint(charRect);
		}
	}
	
	public synchronized void addFallingBlock(CharacterRect block)
	{
		fallingCharRects.add(block);
		//Increment the count of added blocks if we are not adding a joker block
		if(!jokerAdded)
		{
			numDroppedBlocks++;
		}
		else
		{
			//Reset the joker flag
			jokerAdded = false;
		}
	}
	
	public synchronized Vector<GridPos> updateFallingBlocks(CharacterRect[][] wordGrid, int pf_width, int pf_height)
	{
		boolean charRectsUpdated = false;
		int index = 0;
		int row, column;
		Vector<GridPos> blockPositions = new Vector<GridPos>();
		
		while(!charRectsUpdated)
		{
			charRectsUpdated = true;
			
			for(int i = index; i < fallingCharRects.size(); i++)
			{
				CharacterRect charRect = fallingCharRects.get(i);
				
				//Move falling block
				charRect.move(0, GameEngine.CHAR_RECT_DROP_SPEED);
				
				//Check if the falling block has reached a boundary
				if(charRect.maintainBounderies(pf_width, pf_height))
				{	
					//Get the block's position in the grid
					row = GameEngine.getGridRow(charRect.y);
					column = GameEngine.getGridColumn(charRect.x);
					//Add the block to the grid
					wordGrid[row][column] = charRect;
					//Remove the block from the falling block vector
					fallingCharRects.remove(charRect);
					//Save the position of the block in the grid
					blockPositions.add(new GridPos(row, column));
					
					index = i;
					charRectsUpdated = false;
					break;
				}
				
				//Check if the block is intersecting a character block in the grid
				for(int j = 0; j < GameEngine.GRID_ROWS; j++)
				{
					for(int k = 0; k < GameEngine.GRID_COLUMNS; k++)
					{
						if(wordGrid[j][k] != null && charRect.checkForIntersection(wordGrid[j][k]))
						{
							//Get the block's position in the grid
							row = GameEngine.getGridRow(charRect.y);
							column = GameEngine.getGridColumn(charRect.x);
							//Add the block to the grid
							wordGrid[row][column] = charRect;
							//Remove the block from the falling block vector
							fallingCharRects.remove(charRect);
							//Save the position of the block in the grid
							blockPositions.add(new GridPos(row, column));
							
							index = i;
							charRectsUpdated = false;
							break;
						}
					}
					
					if(!charRectsUpdated)
					{
						break;
					}
				}
				
				if(!charRectsUpdated)
				{
					break;
				}
			}
		}
		
		//If the dropper has no more blocks to process
		if(hasBlocksLeft && (fallingCharRects.size() == 0) && (currentBlock == null))
			hasBlocksLeft = false;//Set the approriate flag to false
		
		return blockPositions;
	}
	
	public synchronized void moveDropZoneBlock(Point targetPos)
	{	
		//If we don't have a block to move
		if(currentBlock == null)
		{
			return;
		}
		
		//If the target is to the left of the current character block
		if(GameEngine.getGridColumn(targetPos.x) < GameEngine.getGridColumn(currentBlock.x))
		{
			//Move the block to the left if possible
			if(currentBlock.x > GameEngine.STATUS_FIELD_WIDTH)
			{
				currentBlock.xMove(-GameEngine.CHAR_RECT_MOVE_SPEED);
				blockAligned = false;//We are not aligned since we are moving the block
			}
		}
		//If the target is to the right of the current character block
		else if(GameEngine.getGridColumn(targetPos.x) > GameEngine.getGridColumn(currentBlock.x))
		{
			//Kan inte komma utanför rutnätet till höger eftersom rutnätet går till högra kanten. Om högra kanten får någon GUI pryl behövs ett fall för detta.
			currentBlock.xMove(GameEngine.CHAR_RECT_MOVE_SPEED);
			blockAligned = false;//We are not aligned since we are moving the block
		}
		else//If the target and the character block is in the same grid column
		{
			int alignedPos = GameEngine.getCanvasXPos(GameEngine.getGridColumn(targetPos.x));
			
			blockAligned = false;//We may not be aligned
			
			//Move the block to the left if the move does not take us outside the grid cell
			if(currentBlock.x < alignedPos && (currentBlock.x + GameEngine.CHAR_RECT_MOVE_SPEED <= alignedPos))
			{
				currentBlock.xMove(GameEngine.CHAR_RECT_MOVE_SPEED);
			}
			//Move the block to the right if the move does not take us outside the grid cell
			else if(currentBlock.x > alignedPos && (currentBlock.x - GameEngine.CHAR_RECT_MOVE_SPEED >= alignedPos))
			{
				currentBlock.xMove(-GameEngine.CHAR_RECT_MOVE_SPEED);
			}
			//Align the block with the grid if it is not possible to make a valid move
			else if(currentBlock.x != alignedPos)
			{
				currentBlock.setXPos(alignedPos);
			}
			//We are aligned set the blockAligned flag
			else
			{
				blockAligned = true;
			}
		}
	}
	
	public synchronized boolean getHasBlocksLeft()
	{
		return hasBlocksLeft;
	}
}

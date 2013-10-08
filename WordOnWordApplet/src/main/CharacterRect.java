package main;

public class CharacterRect {
	public int x, y, width, height, left, right, top, bottom;
	public char c;
	
	public CharacterRect(int x, int y) 
	{
		c = WordOnWord.characterPicker.getCharacter();
		this.x = x;
		this.y = y;
		width = GameEngine.CELL_WIDTH;
		height = GameEngine.CELL_HEIGHT;
		this.left = x;
		this.right = x + width;
		this.bottom = y;
		this.top = y + height;
	}
	
	public CharacterRect(char c, int x, int y) 
	{
		this.c = c;
		this.x = x;
		this.y = y;
		width = GameEngine.CELL_WIDTH;
		height = GameEngine.CELL_HEIGHT;
		this.left = x;
		this.right = x + width;
		this.bottom = y;
		this.top = y + height;
	}
	
	public boolean maintainBounderies(int pf_width, int pf_height) {
		boolean reachedBorder = false;
		
		/*Check if the rectangle is outside the canvas bounderies and move it inside if that is the case*/
		if(x < GameEngine.STATUS_FIELD_WIDTH) {
			setXPos(GameEngine.STATUS_FIELD_WIDTH);
			reachedBorder = true;
		}
		else if((x + width) > pf_width) {
			setXPos(pf_width - width);
			reachedBorder = true;
		}
		if(y < GameEngine.DROP_ZONE_HEIGHT) {
			setYPos(GameEngine.DROP_ZONE_HEIGHT);
			reachedBorder = true;
		}
		else if((y + height) > pf_height) {
			setYPos(pf_height - height);
			reachedBorder = true;
		}
		
		return reachedBorder;
	}
	
	public boolean checkForIntersection(CharacterRect rect)
	{	
		if(left < rect.right && right > rect.left && 
		   bottom < rect.top && top > rect.bottom)
		{
			int[] xMove = new int[2];
			xMove[0] = left - rect.right;
			xMove[1] = right - rect.left;
			int smallestXMove = Math.abs(xMove[0]) < Math.abs(xMove[1]) ? xMove[0] : xMove[1];
			
			int[] yMove = new int[2];
			yMove[0] = bottom - rect.top;
			yMove[1] = top - rect.bottom;
			int smallestYMove = Math.abs(yMove[0]) < Math.abs(yMove[1]) ? yMove[0] : yMove[1];
			
			if(smallestXMove < smallestYMove)
			{
				xMove(-smallestXMove);
			}
			else
			{
				yMove(-smallestYMove);
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * move
	 * Moves the rectangle
	 * @param xMove The amount to move the rectangle along x
	 * @param yMove The amount to move the rectangle along y
	 */
	public void move(int xMove, int yMove) {
		x += xMove;
		left += xMove;
		right += xMove;
		y += yMove;
		bottom += yMove;
		top += yMove;
	}
	
	public void xMove(int xMove)
	{
		x += xMove;
		left += xMove;
		right += xMove;
	}
	
	public void yMove(int yMove)
	{
		y += yMove;
		bottom += yMove;
		top += yMove;
	}
	
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.left = x;
		this.right = x + width;
		this.y = y;
		this.bottom = y;
		this.top = y + height;
	}
	
	public void setXPos(int x)
	{
		this.x = x;
		this.left = x;
		this.right = x + width;
	}
	
	public void setYPos(int y)
	{
		this.y = y;
		this.bottom = y;
		this.top = y + height;
	}
}

package main;

/***
 * A class describing a position in a grid
 * @author Erik
 *
 */
public class GridPos {
	public int row, column;
	
	/***
	 * Constructor for GridPos
	 * @param row the row
	 * @param column the column
	 */
	public GridPos(int row, int column)
	{
		this.row = row;
		this.column = column;
	}
}

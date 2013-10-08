package hscore;

public class WoWHighScoreTableModel extends HighScoreTableModel {

	public WoWHighScoreTableModel()
	{
		super();
		columnNames = new String[]{"Name", "Score", "Words", "Date"};
	}
}

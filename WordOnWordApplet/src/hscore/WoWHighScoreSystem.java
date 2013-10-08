package hscore;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import utils.Helpers;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;


public class WoWHighScoreSystem implements HighScoreSystem {
	
	private static final String USERNAME = "shmiagames@gmail.com";
	private static final String PASSWORD = "holmsund19";
	
	private HighScoreListDialog highScoreDialog;
	
	private SpreadsheetService highScoreService;
	private URL[] readWSListFeedURL, writeWSListFeedURL;
	
	private int language;
	
	public WoWHighScoreSystem(URL[] unsortedListFeeds, URL[] sortedListFeeds, HighScoreListDialog highScoreListDialog)
	{
		language = 0;
		
		readWSListFeedURL = sortedListFeeds;
		writeWSListFeedURL = unsortedListFeeds;
		this.highScoreDialog = highScoreListDialog;
		
		highScoreService = new SpreadsheetService("HighScoreService");
		
		//Make a spreadsheet service
		try 
		{
			highScoreService.setUserCredentials(USERNAME, PASSWORD);
		} catch (AuthenticationException e) {
			System.err.println("Could not authenicate user");
			e.printStackTrace();
		}
	}
	
	public void setActiveLanguage(int language)
	{
		this.language = language;
	}
	
	public void registerScore(Object[] args)
	{
		int score = (Integer)args[0];
		int words = (Integer)args[1];
		
		String name = (String)JOptionPane.showInputDialog(
                null,
                "Please enter your name for the highscore list:",
                "High score",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                null,
                "");
		
		if(name != null)
		{
			registerScore(score, words, name);
			showHighScoreList();
		}
	}
	
	private void registerScore(int score, int words, String name)
	{	
		try 
		{
			ListEntry newEntry = new ListEntry();
			newEntry.getCustomElements().setValueLocal("name", name);
			newEntry.getCustomElements().setValueLocal("score", Integer.toString(score));
			newEntry.getCustomElements().setValueLocal("words", Integer.toString(words));
			newEntry.getCustomElements().setValueLocal("date", Helpers.getCurrentTimeUTC());
			highScoreService.insert(writeWSListFeedURL[language], newEntry);
		} catch (IOException e) {
			System.err.println("WordOnWord: Could not add score entry");
			e.printStackTrace();
		} catch (ServiceException e) {
			System.err.println("WordOnWord: Could not add score entry");
			e.printStackTrace();
		}
	}
	
	public void showHighScoreList()
	{
		try {
			ListFeed scoreRowFeed = highScoreService.getFeed(readWSListFeedURL[language], ListFeed.class);
			
			ArrayList<Object[]> highScoreList = new ArrayList<Object[]>();
			
			for(ListEntry row : scoreRowFeed.getEntries())
			{
				Object[] objRow = new Object[4];
				objRow[0] = row.getCustomElements().getValue("name");
				objRow[1] = row.getCustomElements().getValue("score");
				objRow[2] = row.getCustomElements().getValue("words");
				
				objRow[3] = row.getCustomElements().getValue("date");
				objRow[3] = Helpers.utcToLocalTime((String)objRow[3]);
				
				highScoreList.add(objRow);
			}
			
			highScoreDialog.setHighScoreList(highScoreList);
			highScoreDialog.setVisible(true);
		} catch (IOException e) {
			System.err.println("WordOnWord: Could not load score rows");
			e.printStackTrace();
		} catch (ServiceException e) {
			System.err.println("WordOnWord: Could not load score rows");
			e.printStackTrace();
		}
	}
}

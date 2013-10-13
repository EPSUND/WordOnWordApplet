package hscore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.WordOnWord;

import utils.Helpers;

public class WoWHighScoreSystem implements HighScoreSystem {
	/**
	 * The URL to the high score service
	 */
	private static final String HIGH_SCORE_SERVICE_URL = "http://highscoresystemes86.appspot.com/highscoresystem";
	
	private HighScoreListDialog highScoreDialog;
	
	private int language;
	
	public WoWHighScoreSystem(HighScoreListDialog highScoreListDialog)
	{
		language = 0;
		this.highScoreDialog = highScoreListDialog;
	}
	
	public void setActiveLanguage(int language)
	{
		this.language = language;
	}
	
	private String getLanguageString()
	{
		switch(language)
		{
		case WordOnWord.SWEDISH:
			return "swe";
		case WordOnWord.ENGLISH:
			return "eng";
		case WordOnWord.GERMAN:
			return "ger";
		default:
			return "swe";
		}
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
			try
			{
				URLConnection hscoreConn = getHighScoreConnection(score, words, name);
				
				if(hscoreConn != null)
				{
					hscoreConn.connect();
					showHighScoreList(hscoreConn);
				}
			}
			catch(IOException e)
			{
				System.err.println("WordOnWord: Could not connect to the highscore service");
				e.printStackTrace();
			}
		}
	}
	
	private URLConnection getHighScoreConnection(int score, int words, String name)
	{
		try 
		{
			URL registerHighScoreURL = new URL(HIGH_SCORE_SERVICE_URL + "?highScoreList=wordonword_" + getLanguageString() + 
											   "&name=" + name + 
											   "&score=" + Integer.toString(score) + 
											   "&words=" + Integer.toString(words) + 
											   "&date=" + Helpers.getCurrentTimeUTC());
			
			return registerHighScoreURL.openConnection();
			
		} catch (IOException e) {
			System.err.println("WordOnWord: Could not get a connection to the high score service");
			e.printStackTrace();
			return null;
		}
	}
	
	private URLConnection getHighScoreConnection()
	{	
		try 
		{
			URL registerHighScoreURL = new URL(HIGH_SCORE_SERVICE_URL + "?highScoreList=wordonword_" + getLanguageString());	
			return registerHighScoreURL.openConnection();	
		} catch (IOException e) {
			System.err.println("WordOnWord: Could not get a connection to the high score service");
			e.printStackTrace();
			return null;
		} 
	}
	
	public void showHighScoreList(URLConnection highScoreServiceConn)
	{
		InputStream highScoreStream = null;
		
		try {
			highScoreStream = highScoreServiceConn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(highScoreStream));
			
			String line;
			ArrayList<Object[]> highScoreList = new ArrayList<Object[]>();
			
			while((line = reader.readLine()) != null)
			{
				String[] highScoreData = line.split(",");
				Object[] objRow = new Object[4];
				
				for(String highScoreItem : highScoreData)
				{
					String[] components = highScoreItem.split("=");
					
					if(components[0].equals("name"))
					{
						objRow[0] = components[1];
					}
					else if(components[0].equals("score"))
					{
						objRow[1] = components[1];
					}
					else if(components[0].equals("words"))
					{
						objRow[2] = components[1];
					}
					else if(components[0].equals("date"))
					{
						objRow[3] = Helpers.utcToLocalTime(components[1]);
					}
				}
				
				highScoreList.add(objRow);
			}
			
			highScoreDialog.setHighScoreList(highScoreList);
			highScoreDialog.setVisible(true);
		} catch (IOException e) {
			System.err.println("WordOnWord: Could not show high score list");
			e.printStackTrace();
		}
		finally
		{
			try {
				highScoreStream.close();
			} catch (IOException e) {
				System.err.println("WordOnWord: Could not close highscore stream");
				e.printStackTrace();
			}
		}
	}
	
	public void showHighScoreList()
	{
		URLConnection highScoreConn = getHighScoreConnection();
		
		if(highScoreConn != null)
			showHighScoreList(highScoreConn);
	}
}

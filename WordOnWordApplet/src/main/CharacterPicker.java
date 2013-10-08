package main;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

import utils.Helpers;


public class CharacterPicker {
	private String vocabulary;
	private int charNum;
	private int vocabularyMode;
	
	public CharacterPicker(String vocabulary)
	{
		this.vocabulary = vocabulary;
		this.vocabularyMode = WordOnWord.NEW_VOCABULARY;
		
		charNum = 0;
	}
	
	public CharacterPicker()
	{
		charNum = 0;
	}
	
	public void setVocabularyMode(int mode)
	{
		vocabularyMode = mode;
		
		if(vocabularyMode == WordOnWord.PURE_RANDOM)
		{
			switch(WordOnWord.language)
			{
			case WordOnWord.SWEDISH:
				vocabulary = WordOnWord.SWEDISH_ALPHABET;
				break;
			case WordOnWord.ENGLISH:
				vocabulary = WordOnWord.ENGLISH_ALPHABET;
				break;
			case WordOnWord.GERMAN:
				vocabulary = WordOnWord.GERMAN_ALPHABET;
				break;
			default://The default language is english
				vocabulary = WordOnWord.ENGLISH_ALPHABET;
				break;
			}	
		}
		else if(vocabularyMode == WordOnWord.NEW_VOCABULARY)
		{
			switch(WordOnWord.language)
			{
			case WordOnWord.SWEDISH:
				vocabulary = VocabularyGenerator.getSwedishVocabulary();
				break;
			case WordOnWord.ENGLISH:
				vocabulary = VocabularyGenerator.getEnglishVocabulary();
				break;
			case WordOnWord.GERMAN:
				vocabulary = VocabularyGenerator.getGermanVocabulary();
				break;
			default://The default language is english
				vocabulary = VocabularyGenerator.getEnglishVocabulary();
				break;
			}
		}
		else if(vocabularyMode == WordOnWord.WEEK_VOCABULARY)
		{
			vocabulary = getWeekVocabulary(WordOnWord.language);
		}
	}
	
	public String getWeekVocabulary(int lang)
	{
		String fileName;
		
		switch(lang)
		{
		case WordOnWord.SWEDISH:
			fileName = "vocabularies_se.txt";
			break;
		case WordOnWord.ENGLISH:
			fileName = "vocabularies_en.txt";
			break;
		case WordOnWord.GERMAN:
			fileName = "vocabularies_ge.txt";
			break;
		default://The default language is english
			fileName = "vocabularies_en.txt";
			break;
		}
		
		String vocToUse = "";
		
		try 
		{
			URL vocabulariesURL = Helpers.getResourceURL(this, "vocabularies/" + fileName);
			BufferedReader vocabularyReader = new BufferedReader(new InputStreamReader(vocabulariesURL.openStream()));
			
			Calendar cal = Calendar.getInstance();
			
			int weekNum = cal.get(Calendar.WEEK_OF_YEAR);
			
			String voc = "";
			int vocNum = 0;
			
			//Find the week's vocabulary
			while((voc = vocabularyReader.readLine()) != null)
			{
				vocNum++;
				
				if(vocNum == weekNum)
				{
					vocToUse = voc;
					break;
				}
			}
		} 
		catch (Exception e) 
		{
			System.err.println("WordOnWord: Could not read vocabulary file");
			e.printStackTrace();
		}
		
		return vocToUse;
	}
	
	public void resetCharacterPicker()
	{
		charNum = 0;
	}
	
	private char nextCharacter()
	{
		if(vocabulary == null)
		{
			System.err.println("WordOnWord: The vocabulary is null");
			return '\0';
		}
		
		int vocLength = vocabulary.length();
		
		//Something has gone wrong if the vocabulary is the empty string
		if(vocLength == 0)
		{
			System.err.println("WordOnWord: Empty vocabulary");
			return '\0';
		}
		
		if(charNum >= vocabulary.length())
		{
			System.err.println("WordOnWord: No more characters in the vocabulary, resetting character picker");
			charNum = 0;
		}
		
		char nextChar;
		
		nextChar = vocabulary.charAt(charNum); 
		
		charNum++;
		
		//Return the next character from the vocabulary
		return nextChar;
	}
	
	private char randomCharacter()
	{
		if(vocabulary == null)
		{
			System.err.println("WordOnWord: The vocabulary is null");
			return '\0';
		}
		
		int vocLength = vocabulary.length();
		
		//Something has gone wrong if the vocabulary is the empty string
		if(vocLength == 0)
		{
			System.err.println("WordOnWord: Empty vocabulary");
			return '\0';
		}
		
		Random r = new Random();
		
		//Return a random character from the vocabulary
		return vocabulary.charAt(r.nextInt(vocLength));
	}
	
	public char getCharacter()
	{
		if(vocabularyMode == WordOnWord.PURE_RANDOM)
		{
			return randomCharacter();
		}
		else if(vocabularyMode == WordOnWord.NEW_VOCABULARY || vocabularyMode == WordOnWord.WEEK_VOCABULARY)
		{
			return nextCharacter();
		}
		else//Should never happen
		{
			return nextCharacter();
		}
	}
}

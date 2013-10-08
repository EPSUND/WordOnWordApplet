package main;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;

import utils.Helpers;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

public class WordChecker {
	//Dictionary names
	private static final String SWEDISH_DICTIONARY = "sv_SEx.dic";
	private static final String ENGLISH_DICTIONARY = "en_USx.dic";
	private static final String GERMAN_DICTIONARY = "de_DEx.dic";
	
	private HashMap<Integer, SpellChecker> spellCheckers;
	private SpellChecker activeSpellChecker;
	
	public WordChecker()
	{	
		spellCheckers = new HashMap<Integer, SpellChecker>();
		activeSpellChecker = null;
	}
	
	public void setLanguage(int language)
	{
		if(spellCheckers.containsKey(language))
		{
			activeSpellChecker = spellCheckers.get(language);
		}
		else
		{
			SpellChecker newSpellChecker;
			
			try
			{
				//Get the url of the dictionary
				URL dictionaryURL = Helpers.getResourceURL(this, getDictionaryName(language));
				//Get the dictionary as a SpellDictionaryHashMap
				SpellDictionaryHashMap dictionary = new SpellDictionaryHashMap(new InputStreamReader(dictionaryURL.openStream(), "UTF-8"));
				//Make the spell checker
				newSpellChecker = new SpellChecker(dictionary);
			}
			catch(IOException e)
			{
				System.err.println("WordOnWord: Could not make spell checker");
				e.printStackTrace();
				newSpellChecker = null;
			}
			
			if(newSpellChecker != null)
			{
				spellCheckers.put(language, newSpellChecker);
				activeSpellChecker = newSpellChecker;
			}
		}
	}
	
	public boolean isValidWord(String word)
	{
		//We can't check the spelling if we don't have a spell checker
		if(activeSpellChecker == null)
		{
			return false;
		}
		
		//Must be lower case for the spell check to work
		word = word.toLowerCase();
		
		//Make a string word tokenizer for the word
		StringWordTokenizer stringWordTokenizer = new StringWordTokenizer(word);
		
		//Check if the given word corresponds to a word in the dictionary
		if(activeSpellChecker.checkSpelling(stringWordTokenizer) == SpellChecker.SPELLCHECK_OK)
			return true;
		
		//Try checking the word again, but let the first letter be uppercase
		
		char[] letters = word.toCharArray();
		letters[0] = Character.toUpperCase(letters[0]);
		word = new String(letters);
		stringWordTokenizer = new StringWordTokenizer(word);
		
		if(activeSpellChecker.checkSpelling(stringWordTokenizer) == SpellChecker.SPELLCHECK_OK)
			return true;
		else
			return false;
	}
	
	private String getDictionaryName(int language)
	{
		switch(language)
		{
		case WordOnWord.SWEDISH:
			return "dictionaries/" + SWEDISH_DICTIONARY;
		case WordOnWord.ENGLISH:
			return "dictionaries/" + ENGLISH_DICTIONARY;
		case WordOnWord.GERMAN:
			return "dictionaries/" + GERMAN_DICTIONARY;
		default:
			return "dictionaries/" + ENGLISH_DICTIONARY;
		}
	}
}

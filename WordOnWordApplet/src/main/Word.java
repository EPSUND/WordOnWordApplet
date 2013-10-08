package main;

public class Word implements Comparable<Word> {
	private static float SYNERGY_SCORE_FACTOR = 1.0f;
	private static int WORD_LENGTH_BONUS = 3;
	
	public int startRow, endRow, startColumn, endColumn;
	private String wordString;
	
	public Word(String wordString, int startRow, int endRow, int startColumn, int endColumn)
	{
		this.wordString = wordString;
		this.startRow = startRow;
		this.endRow = endRow;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}
	
	public boolean insideWordBounds(GridPos gridPos)
	{
		return gridPos.row >= startRow && gridPos.row <= endRow &&
			   gridPos.column >= startColumn && gridPos.column <= endColumn;	
	}
	
	public boolean isInsideWord(Word word)
	{
		return startRow >= word.startRow && endRow <= word.endRow &&
			   startColumn >= word.startColumn && endColumn <= word.endColumn;
	}
	
	public boolean intersectsWord(Word word)
	{
		boolean intersectHoriz =
				//Both words are horizontal
				(startRow == endRow) && (startRow == word.startRow) &&
				//The start or end column is inside the other word
				((startColumn >= word.startColumn && startColumn <= word.endColumn) ||
		   	     (endColumn >= word.startColumn && endColumn <= word.endColumn));
		
		boolean intersectVert =
				//Both words are vertical
				(startColumn == endColumn) && (startColumn == word.startColumn) &&
				//The start or end row is inside the other word
				((startRow >= word.startRow && startRow <= word.endRow) ||
		   	     (endRow >= word.startRow && endRow <= word.endRow));
		
		return intersectHoriz || intersectVert;
	}
	
	public int getScore()
	{
		char[] wordCharacters = wordString.toCharArray();
		int score = 0;
		
		//Calculate character score
		for(char c : wordCharacters)
		{
			if(WordOnWord.language == WordOnWord.SWEDISH)
			{
				switch(c)
				{
				case 'D':
				case 'R':
				case 'S':
				case 'E':
				case 'T':
				case 'L':
				case 'A':
				case 'I':
				case 'N':
					score += 1;
					break;
				case 'O':
				case 'G':
					score += 2;
					break;
				case 'H':
				case 'M':
				case 'K':
				case 'P':
				case 'U':
					score += 3;
					break;
				case 'Ä':
				case 'Å':
				case 'F':
				case 'Ö':
				case 'B':
				case 'V':
					score += 4;
					break;
				case 'Y':
				case 'C':
				case 'J':
					score += 8;
					break;
				case 'X':
				case 'Z':
				case 'Q':
					score += 10;
					break;
				}
			}
			else if(WordOnWord.language == WordOnWord.ENGLISH)
			{
				switch(c)
				{
				case 'E':
				case 'A':
				case 'I':
				case 'O':
				case 'N':
				case 'R':
				case 'T':
				case 'L':
				case 'S':
				case 'U':
					score += 1;
					break;
				case 'D':
				case 'G':
					score += 2;
					break;
				case 'B':
				case 'C':
				case 'M':
				case 'P':
					score += 3;
					break;
				case 'F':
				case 'H':
				case 'V':
				case 'W':
				case 'Y':
					score += 4;
					break;
				case 'K':
					score += 5;
					break;
				case 'J':
				case 'X':
					score += 8;
					break;
				case 'Q':
				case 'Z':
					score += 10;
					break;
				}
			}
			else if(WordOnWord.language == WordOnWord.GERMAN)
			{
				switch(c)
				{
				case 'E':
				case 'N':
				case 'S':
				case 'I':
				case 'R':
				case 'T':
				case 'U':
				case 'A':
				case 'D':
					score += 1;
					break;
				case 'H':
				case 'G':
				case 'L':
				case 'O':
				case 'ß':
					score += 2;
					break;
				case 'M':
				case 'B':
				case 'W':
				case 'Z':
					score += 3;
					break;
				case 'C':
				case 'F':
				case 'K':
				case 'P':
					score += 4;
					break;
				case 'Ä':
				case 'J':
				case 'Ü':
				case 'V':	
					score += 6;
					break;
				case 'Ö':
				case 'X':
					score += 8;
					break;
				case 'Q':
				case 'Y':
					score += 10;
					break;
				}
			}
		}
		
		//Add synergy score
		//score += (SYNERGY_SCORE_FACTOR * score) / wordCharacters.length;
		
		//Add word score
		score += (wordCharacters.length * wordCharacters.length - 1);
		
		return score;
	}
	
	public String toString()
	{
		return wordString;
	}

	@Override
	public int compareTo(Word word) {
		if(getScore() < word.getScore())
		{
			return -1;
		}
		else if(getScore() > word.getScore())
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}

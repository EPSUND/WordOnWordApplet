package main;
import java.util.Random;


public class VocabularyGenerator {
	
	public static String getGermanVocabulary()//Get a random german vocabulary
	{
		Random r = new Random();
		
		int charNum;
		char[] vocabulary = new char[GameEngine.GRID_ROWS * GameEngine.GRID_COLUMNS - 1];// -1 because we have a joker block
		
		for(int i = 0; i < GameEngine.GRID_ROWS * GameEngine.GRID_COLUMNS - 1; i++)
		{
			charNum = r.nextInt(102364);
			
			if(charNum >= 0 && charNum < 6516)//A
			{
				vocabulary[i] = 'A';
			}
			else if(charNum >= 6516 && charNum < 8402)//B
			{
				vocabulary[i] = 'B';
			}
			else if(charNum >= 8402 && charNum < 11134)//C
			{
				vocabulary[i] = 'C';
			}
			else if(charNum >= 11134 && charNum < 16210)//D
			{
				vocabulary[i] = 'D';
			}
			else if(charNum >= 16210 && charNum < 33606)//E
			{
				vocabulary[i] = 'E';
			}
			else if(charNum >= 33606 && charNum < 35262)//F
			{
				vocabulary[i] = 'F';
			}
			else if(charNum >= 35262 && charNum < 38271)//G
			{
				vocabulary[i] = 'G';
			}
			else if(charNum >= 38271 && charNum < 43028)//H
			{
				vocabulary[i] = 'H';
			}
			else if(charNum >= 43028 && charNum < 50578)//I
			{
				vocabulary[i] = 'I';
			}
			else if(charNum >= 50578 && charNum < 50846)//J
			{
				vocabulary[i] = 'J';
			}
			else if(charNum >= 50846 && charNum < 52263)//K
			{
				vocabulary[i] = 'K';
			}
			else if(charNum >= 52263 && charNum < 55700)//L
			{
				vocabulary[i] = 'L';
			}
			else if(charNum >= 55700 && charNum < 58234)//M
			{
				vocabulary[i] = 'M';
			}
			else if(charNum >= 58234 && charNum < 68010)//N
			{
				vocabulary[i] = 'N';
			}
			else if(charNum >= 68010 && charNum < 70604)//O
			{
				vocabulary[i] = 'O';
			}
			else if(charNum >= 70604 && charNum < 71274)//P
			{
				vocabulary[i] = 'P';
			}
			else if(charNum >= 71274 && charNum < 71292)//Q
			{
				vocabulary[i] = 'Q';
			}
			else if(charNum >= 71292 && charNum < 78295)//R
			{
				vocabulary[i] = 'R';
			}
			else if(charNum >= 78295 && charNum < 85568)//S
			{
				vocabulary[i] = 'S';
			}
			else if(charNum >= 85568 && charNum < 91722)//T
			{
				vocabulary[i] = 'T';
			}
			else if(charNum >= 91722 && charNum < 96068)//U
			{
				vocabulary[i] = 'U';
			}
			else if(charNum >= 96068 && charNum < 96914)//V
			{
				vocabulary[i] = 'V';
			}
			else if(charNum >= 96914 && charNum < 98835)//W
			{
				vocabulary[i] = 'W';
			}
			else if(charNum >= 98835 && charNum < 98869)//X
			{
				vocabulary[i] = 'X';
			}
			else if(charNum >= 98869 && charNum < 98908)//Y
			{
				vocabulary[i] = 'Y';
			}
			else if(charNum >= 98908 && charNum < 100042)//Z
			{
				vocabulary[i] = 'Z';
			}
			else if(charNum >= 100042 && charNum < 100489)//Ä
			{
				vocabulary[i] = 'Ä';
			}
			else if(charNum >= 100489 && charNum < 101062)//Ö
			{
				vocabulary[i] = 'Ö';
			}
			else if(charNum >= 101062 && charNum < 101369)//ß
			{
				vocabulary[i] = 'ß';
			}
			else//Ü
			{
				vocabulary[i] = 'Ü';
			}
		}
		
		return new String(vocabulary);
	}
	
	public static String getEnglishVocabulary()//Get a random english vocabulary
	{
		Random r = new Random();
		
		int charNum;
		char[] vocabulary = new char[GameEngine.GRID_ROWS * GameEngine.GRID_COLUMNS - 1];// -1 because we have a joker block
		
		for(int i = 0; i < GameEngine.GRID_ROWS * GameEngine.GRID_COLUMNS - 1; i++)
		{
			charNum = r.nextInt(100000);
			
			if(charNum >= 0 && charNum < 8167)//A
			{
				vocabulary[i] = 'A';
			}
			else if(charNum >= 8167 && charNum < 9659)//B
			{
				vocabulary[i] = 'B';
			}
			else if(charNum >= 9659 && charNum < 12441)//C
			{
				vocabulary[i] = 'C';
			}
			else if(charNum >= 12441 && charNum < 16694)//D
			{
				vocabulary[i] = 'D';
			}
			else if(charNum >= 16694 && charNum < 29396)//E
			{
				vocabulary[i] = 'E';
			}
			else if(charNum >= 29396 && charNum < 31624)//F
			{
				vocabulary[i] = 'F';
			}
			else if(charNum >= 31624 && charNum < 33639)//G
			{
				vocabulary[i] = 'G';
			}
			else if(charNum >= 33639 && charNum < 39733)//H
			{
				vocabulary[i] = 'H';
			}
			else if(charNum >= 39733 && charNum < 46699)//I
			{
				vocabulary[i] = 'I';
			}
			else if(charNum >= 46699 && charNum < 46852)//J
			{
				vocabulary[i] = 'J';
			}
			else if(charNum >= 46852 && charNum < 47624)//K
			{
				vocabulary[i] = 'K';
			}
			else if(charNum >= 47624 && charNum < 51649)//L
			{
				vocabulary[i] = 'L';
			}
			else if(charNum >= 51649 && charNum < 54055)//M
			{
				vocabulary[i] = 'M';
			}
			else if(charNum >= 54055 && charNum < 60804)//N
			{
				vocabulary[i] = 'N';
			}
			else if(charNum >= 60804 && charNum < 68311)//O
			{
				vocabulary[i] = 'O';
			}
			else if(charNum >= 68311 && charNum < 70240)//P
			{
				vocabulary[i] = 'P';
			}
			else if(charNum >= 70240 && charNum < 70335)//Q
			{
				vocabulary[i] = 'Q';
			}
			else if(charNum >= 70335 && charNum < 76322)//R
			{
				vocabulary[i] = 'R';
			}
			else if(charNum >= 76322 && charNum < 82649)//S
			{
				vocabulary[i] = 'S';
			}
			else if(charNum >= 82649 && charNum < 91705)//T
			{
				vocabulary[i] = 'T';
			}
			else if(charNum >= 91705 && charNum < 94463)//U
			{
				vocabulary[i] = 'U';
			}
			else if(charNum >= 94463 && charNum < 95441)//V
			{
				vocabulary[i] = 'V';
			}
			else if(charNum >= 95441 && charNum < 97801)//W
			{
				vocabulary[i] = 'W';
			}
			else if(charNum >= 97801 && charNum < 97951)//X
			{
				vocabulary[i] = 'X';
			}
			else if(charNum >= 97951 && charNum < 99925)//Y
			{
				vocabulary[i] = 'Y';
			}
			else//Z
			{
				vocabulary[i] = 'Z';
			}
		}
		
		return new String(vocabulary);
	}
	
	public static String getSwedishVocabulary()//Get a random swedish vocabulary
	{
		Random r = new Random();
		
		int charNum;
		char[] vocabulary = new char[GameEngine.GRID_ROWS * GameEngine.GRID_COLUMNS - 1];// -1 because we have a joker block
		
		for(int i = 0; i < GameEngine.GRID_ROWS * GameEngine.GRID_COLUMNS - 1; i++)
		{
			charNum = r.nextInt(100000);
			
			if(charNum >= 0 && charNum < 9300)//A
			{
				vocabulary[i] = 'A';
			}
			else if(charNum >= 9300 && charNum < 10600)//B
			{
				vocabulary[i] = 'B';
			}
			else if(charNum >= 10600 && charNum < 11900)//C
			{
				vocabulary[i] = 'C';
			}
			else if(charNum >= 11900 && charNum < 16400)//D
			{
				vocabulary[i] = 'D';
			}
			else if(charNum >= 16400 && charNum < 26300)//E
			{
				vocabulary[i] = 'E';
			}
			else if(charNum >= 26300 && charNum < 28300)//F
			{
				vocabulary[i] = 'F';
			}
			else if(charNum >= 28300 && charNum < 31600)//G
			{
				vocabulary[i] = 'G';
			}
			else if(charNum >= 31600 && charNum < 33700)//H
			{
				vocabulary[i] = 'H';
			}
			else if(charNum >= 33700 && charNum < 38800)//I
			{
				vocabulary[i] = 'I';
			}
			else if(charNum >= 38800 && charNum < 39500)//J
			{
				vocabulary[i] = 'J';
			}
			else if(charNum >= 39500 && charNum < 42700)//K
			{
				vocabulary[i] = 'K';
			}
			else if(charNum >= 42700 && charNum < 47900)//L
			{
				vocabulary[i] = 'L';
			}
			else if(charNum >= 47900 && charNum < 51400)//M
			{
				vocabulary[i] = 'M';
			}
			else if(charNum >= 51400 && charNum < 60200)//N
			{
				vocabulary[i] = 'N';
			}
			else if(charNum >= 60200 && charNum < 64300)//O
			{
				vocabulary[i] = 'O';
			}
			else if(charNum >= 64300 && charNum < 66000)//P
			{
				vocabulary[i] = 'P';
			}
			else if(charNum >= 66000 && charNum < 66007)//Q
			{
				vocabulary[i] = 'Q';
			}
			else if(charNum >= 66007 && charNum < 74307)//R
			{
				vocabulary[i] = 'R';
			}
			else if(charNum >= 74307 && charNum < 80607)//S
			{
				vocabulary[i] = 'S';
			}
			else if(charNum >= 80607 && charNum < 89307)//T
			{
				vocabulary[i] = 'T';
			}
			else if(charNum >= 89307 && charNum < 91107)//U
			{
				vocabulary[i] = 'U';
			}
			else if(charNum >= 91107 && charNum < 93507)//V
			{
				vocabulary[i] = 'V';
			}
			else if(charNum >= 93507 && charNum < 93537)//W
			{
				vocabulary[i] = 'W';
			}
			else if(charNum >= 93537 && charNum < 93637)//X
			{
				vocabulary[i] = 'X';
			}
			else if(charNum >= 93637 && charNum < 94237)//Y
			{
				vocabulary[i] = 'Y';
			}
			else if(charNum >= 94237 && charNum < 94257)//Z
			{
				vocabulary[i] = 'Z';
			}
			else if(charNum >= 94257 && charNum < 95857)//Å
			{
				vocabulary[i] = 'Å';
			}
			else if(charNum >= 95857 && charNum < 97957)//Ä
			{
				vocabulary[i] = 'Ä';
			}
			else//Ö egentligen 99457, men jag tar till 100% istället
			{
				vocabulary[i] = 'Ö';
			}
		}
		
		return new String(vocabulary);
	}
}

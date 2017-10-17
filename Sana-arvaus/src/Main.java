import java.util.Arrays;
import java.util.Scanner;


public class Main {
	
	private static final Scanner scanner = new Scanner(System.in);
	
	static String HiddenWord = "koira"; //TODO option for more words
	static boolean GameIsOver = false; // helps to reset the game

	public static void main(String[] args) 
	{
		do{
		System.out.println("Tervetuloa sana-arvaus peliin!"); // re-playable intro
		System.out.println("Osaatko arvata 5 kirjaimisen sanan?");
		int CurrentTry = 5;	
		AskForAnswer(CurrentTry);
		}while(PlayAgainOrNot() == true);
	}
		
	
	
	/// Ask user for input and check its validity
	public static void AskForAnswer(int CurrentTry)
	{
		
		if(CurrentTry == 0){GameLost();} // check current tries to see if we can still play
		else if(CurrentTry > 0 )
		{
			System.out.println("Yrityksi‰ j‰ljell‰: " + CurrentTry);
			System.out.println("Arvaa sana: "); // kysyt‰‰n arvaus
			String vastaus = scanner.next();
			CurrentTry = CurrentTry - 1;
			if(vastaus.matches(HiddenWord))
			{
				GameOver();
			}
			CheckAnswer(vastaus, CurrentTry);
		}
	
	}
		
	
	
	public static void TarkistaVastaus(String vastaus, int CurrentTry)
	{
		boolean testi = false; // for debugging a weird bug more info from laatikainen
		if(GameIsOver ==false) // if the game is already over no need to check the answer
		{
		
		if(vastaus.length() < 5 )
		{
			System.out.println("Vastauksesi on liian lyhyt!");
			CurrentTry = CurrentTry + 1;
			testi =true;
			AskForAnswer(CurrentTry);
		}
		
		if(vastaus.length() > 5 )
		{
			System.out.println("Vastauksesi on liian pitk‰!");
			CurrentTry = CurrentTry + 1;
			testi = true;
			AskForAnswer(CurrentTry);
		}
		
		
			
		if (vastaus.matches(".*[A-Z].*"))
			{
				System.out.println("K‰yt‰ vain pieni‰ kirjaimia!");
				CurrentTry = CurrentTry + 1;
				AskForAnswer(CurrentTry);
		
			}
			
		if(IsIsogram(vastaus)==true & testi == false)
			{
				System.out.println("Sanasi ei ole isogrammi!");
				CurrentTry = CurrentTry + 1;
				AskForAnswer(CurrentTry);
			}
		if(GameIsOver == false)
		CheckAnswer(vastaus, CurrentTry);
		
		}
	}
	
	
	
	// checks whether the word is isogram
	static boolean IsIsogram(String vastaus)
    {
        // Convert the string in lower case letters
        vastaus = vastaus.toLowerCase();
        int len = vastaus.length();
        
        char arr[] = vastaus.toCharArray();
        
        Arrays.sort(arr);
        for(int i = 0;i < len-1;i++)
        {
            if(arr[i] == arr[i+1])
                return true;
        }
        return false;
	}
	
	public static void CheckAnswer(String vastaus,int CurrentTry)
	{
			    
			int bulls=0;
		    int cows =0;

		    int[] arr1 = new int[100];
		    int[] arr2 = new int[100];
		 
		 
		    for(int i=0; i<HiddenWord.length(); i++){ // comparing letters
		        char hwChar = HiddenWord.charAt(i); 
		        char guessChar = vastaus.charAt(i);
		        
		        if(hwChar==guessChar)//if they match at the same place it's a bull!
		            bulls++;
		        else{
		            arr1[hwChar-'0']++;
		            arr2[guessChar-'0']++;
		        }    
		    }
		 
		    for(int i=0; i<100; i++){ // counting the cows
		        cows += Math.min(arr1[i], arr2[i]);
		       
		    }
		    ShowHint(bulls, cows, vastaus, CurrentTry);
		}
	
	public static void GameOver()
	{	
		System.out.println("Hienoa voitit pelin!");
		GameIsOver = true;	
	}
	
	public static void GameLost()
	{
		System.out.println("Et voittanut t‰ll‰ kertaa. Parempi onni ensi kerralla! jouu");
		//System.exit(0);
	}
	
		
		
	/// Prints to player whether they had bulls or cows in their answer
	public static void ShowHint(int bulls, int cows, String vastaus, int CurrentTry)
	{
		System.out.println("Bulls: " + bulls + " Cows: "+ cows);
		AskForAnswer(CurrentTry);
	}
	
	/// Ask to play again and return the answer back to do while loop
	public static boolean PlayAgainOrNot() {

		System.out.println(" \n Haluatko pelata uudelleen? y/n ");
		String vastaus = scanner.next();
		if (vastaus.matches("n")) {
			System.out.println("N‰hd‰‰n taas!");
			System.exit(0);
			return false;
		} else if (vastaus.matches("y")) 
		{
			GameIsOver =false;
			return true;
		}
		return true;
	}
}

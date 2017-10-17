import java.util.Arrays;
import java.util.Scanner;

 // this is test commit to git lets see if anything changes. Hope this works.
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
			System.out.println("Yrityksiä jäljellä: " + CurrentTry);
			System.out.println("Arvaa sana: "); // ask for a guess
			String answer = scanner.next();
			CurrentTry = CurrentTry - 1;
			if(answer.matches(HiddenWord))
			{
				GameOver();
			}
			CheckAnswerValidity(answer, CurrentTry);
		}
	
	}
		
	
	
	public static void CheckAnswerValidity(String answer, int CurrentTry)
	{
		boolean testi = false; // for debugging a weird bug more info from laatikainen
		if(GameIsOver ==false) // if the game is already over no need to check the answer
		{
		
		if(answer.length() < 5 )
		{
			System.out.println("Vastauksesi on liian lyhyt!");
			CurrentTry = CurrentTry + 1;
			testi =true;
			AskForAnswer(CurrentTry);
		}
		
		if(answer.length() > 5 )
		{
			System.out.println("Vastauksesi on liian pitkä!");
			CurrentTry = CurrentTry + 1;
			testi = true;
			AskForAnswer(CurrentTry);
		}
		
		
			
		if (answer.matches(".*[A-Z].*"))
			{
				System.out.println("Käytä vain pieniä kirjaimia!");
				CurrentTry = CurrentTry + 1;
				AskForAnswer(CurrentTry);
		
			}
			
		if(IsIsogram(answer)==true & testi == false)
			{
				System.out.println("Sanasi ei ole isogrammi!");
				CurrentTry = CurrentTry + 1;
				AskForAnswer(CurrentTry);
			}
		if(GameIsOver == false) // No need to spam the hint if the game is already over
		CheckAnswer(answer, CurrentTry);
		
		}
	}
	
	
	
	// checks whether the word is isogram
	static boolean IsIsogram(String answer)
    {
        // Convert the string in lower case letters
        answer = answer.toLowerCase();
        int len = answer.length();
        
        char arr[] = answer.toCharArray();
        
        Arrays.sort(arr);
        for(int i = 0;i < len-1;i++)
        {
            if(arr[i] == arr[i+1])
                return true;
        }
        return false;
	}
	
	public static void CheckAnswer(String answer,int CurrentTry)
	{
			    
			int bulls=0;
		    int cows =0;

		    int[] arr1 = new int[100];
		    int[] arr2 = new int[100];
		 
		 
		    for(int i=0; i<HiddenWord.length(); i++){ // comparing letters
		        char hwChar = HiddenWord.charAt(i); 
		        char guessChar = answer.charAt(i);
		        
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
		    if(GameIsOver == false)
		    ShowHint(bulls, cows, answer, CurrentTry);
		}
	
	public static void GameOver()
	{	
		System.out.println("Hienoa voitit pelin!");
		GameIsOver = true;	
	}
	
	public static void GameLost()
	{
		System.out.println("Et voittanut tällä kertaa. Parempi onni ensi kerralla! jouu");
		//System.exit(0);
	}
	
		
		
	/// Prints to player whether they had bulls or cows in their answer
	public static void ShowHint(int bulls, int cows, String answer, int CurrentTry)
	{
		System.out.println("Bulls: " + bulls + " Cows: "+ cows);
		AskForAnswer(CurrentTry);
	}
	
	/// Ask to play again and return the answer back to do while loop
	public static boolean PlayAgainOrNot() {

		System.out.println(" \n Haluatko pelata uudelleen? y/n ");
		String answer = scanner.next();
		if (answer.matches("n")) {
			System.out.println("Nähdään taas!");
			System.exit(0);
			return false;
		} else if (answer.matches("y")) 
		{
			GameIsOver =false;
			return true;
		}
		return true;
	}
}

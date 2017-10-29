import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class Main {

	private static final Scanner scanner = new Scanner(System.in);
	private static final Scanner namescanner = new Scanner(System.in);
	
	static String HiddenWord = ""; // Initializing empty string
	static String Name = ""; // name of the player
	static int Score = 0; // global score of the player
	static boolean GameIsOver = false; // helps to reset the game
	static int Difficulty;
	
	public static void main(String[] args) throws IOException {

		do {
			GetRandomHiddenWord();
			System.out.println("Tervetuloa sana-arvaus peliin!"); // re-playable intro
			Score = 0;
			System.out.println("Ole hyvä ja valitse vaikeustasosi." +  "\n 1: helppo \n 2: keskitaso \n 3: vaikea");
			Difficulty = scanner.nextInt();
			System.out.println("Syötä nimesi:");
			Name = namescanner.nextLine();
			System.out.println("Tervetuloa: " + Name + "!");
			System.out.println("Osaatko arvata 5 kirjaimisen sanan?");
			int CurrentTry = 5;
			
			if(Difficulty == 1) {CurrentTry = 10;} // giving tries based on the chosen difficulty
			else if(Difficulty == 2) {CurrentTry = 5;}
			else {CurrentTry = 3;}
			AskForAnswer(CurrentTry);
		} while (PlayAgainOrNot() == true);
		FullLeaderBoard();
		AskLeaderBoard();
	}
	
	// ask user to load the leaderboard
		public static void AskLeaderBoard() throws IOException {

			System.out.println("Haluatko ladata tulostaulukon? y/n/5(näyttää viisi ensimmäistä)");
			String BoardAnswer = scanner.next();
			if (BoardAnswer.matches("n")) {

			} else if (BoardAnswer.matches("y")) {
				PrintFullLeaderBoard();
			}
			else if (BoardAnswer.matches("5")) {
				PrintTop5LeaderBoard();
			}
		}
		
		// print the full leaderboard
		public static void PrintFullLeaderBoard() {

			try {
				final Scanner reader = new Scanner(new FileReader("leaderboardfull.txt"));		
				while (reader.hasNext()) {
					String line = reader.nextLine();
					System.out.println(line);
				}				
				reader.close();
			} catch (FileNotFoundException e) {
				// error log
				System.out.println("Tapahtui virhe. Tiedostoa leaderboardfull.txt ei löytynyt ");
				e.printStackTrace();
			}
		}

		// create and write the full leaderboard
	public static void FullLeaderBoard() throws IOException {
		try {
			final PrintWriter writer = new PrintWriter(new FileWriter("leaderboardfull.txt", true));
			
			writer.println(" Nimi: " + Name + " Pisteet " + Score);
			writer.close();
		} catch (IOException e) {
			System.out.println("Ohjelmassa tapahtui virhe.");
			e.printStackTrace();
		}
	}
	 // print top 5 in the leaderboard
	public static void PrintTop5LeaderBoard() {

		try {
			final Scanner reader = new Scanner(new FileReader("leaderboardfull.txt"));
			int counter = 0;
			while (reader.hasNext() && counter < 5) {
				String line = reader.nextLine();
				System.out.println(line);
				counter++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// error log
			System.out.println("Tapahtui virhe. Tiedostoa leaderboardfull.txt ei löytynyt ");
			e.printStackTrace();
		}
	}

	// Ask user for input and check its validity
	public static void AskForAnswer(int CurrentTry) {

		if (CurrentTry == 0) {
			GameLost();
		} // check current tries to see if we can still play
		else if (CurrentTry > 0) {
			System.out.println("Yrityksiä jäljellä: " + CurrentTry);
			System.out.println("Pisteet: " + Score);
			System.out.println("Arvaa sana: "); // ask for a guess
			String answer = scanner.next();
			CurrentTry = CurrentTry - 1;
			if (answer.matches(HiddenWord)) {
				GameOver();
			}
			CheckAnswerValidity(answer, CurrentTry);
		}

	}

	public static void CheckAnswerValidity(String answer, int CurrentTry) {
		boolean testi = false; // for debugging a weird bug more info from laatikainen
								
		if (GameIsOver == false) // if the game is already over no need to check  the answer
									
		{

			if (answer.length() < 5) {
				System.out.println("Vastauksesi on liian lyhyt!");
				CurrentTry = CurrentTry + 1;
				Score -= 5;
				testi = true;
				AskForAnswer(CurrentTry);
			}

			if (answer.length() > 5) {
				System.out.println("Vastauksesi on liian pitkä!");
				CurrentTry = CurrentTry + 1;
				Score -= 5;
				testi = true;
				AskForAnswer(CurrentTry);
			}

			if (answer.matches(".*[A-Z].*")) {
				System.out.println("Käytä vain pieniä kirjaimia!");
				CurrentTry = CurrentTry + 1;
				Score -= 5;
				AskForAnswer(CurrentTry);

			}

			if (IsIsogram(answer) == true & testi == false) {
				System.out.println("Sanasi ei ole isogrammi!");
				CurrentTry = CurrentTry + 1;
				Score -= 5;
				AskForAnswer(CurrentTry);
			}
			if (GameIsOver == false) // No need to spam the hint if the game is already over										
				CheckAnswer(answer, CurrentTry);
		}
	}

	// checks whether the word is isogram
	static boolean IsIsogram(String answer) {
		// Convert the string in lower case letters
		answer = answer.toLowerCase();
		int len = answer.length();

		char arr[] = answer.toCharArray();

		Arrays.sort(arr);
		for (int i = 0; i < len - 1; i++) {
			if (arr[i] == arr[i + 1])
				return true;
		}
		return false;
	}

	public static void CheckAnswer(String answer, int CurrentTry) {

		int bulls = 0;
		int cows = 0;

		int[] arr1 = new int[100];
		int[] arr2 = new int[100];

		for (int i = 0; i < HiddenWord.length(); i++) { // comparing letters
			char hwChar = HiddenWord.charAt(i);
			char guessChar = answer.charAt(i);

			if (hwChar == guessChar)// if they match at the same place it's a bull!		
				bulls++; 
			
			else {
				arr1[hwChar - '0']++;
				arr2[guessChar - '0']++;
				
			}
		}
		Score += bulls * 5;

		for (int i = 0; i < 100; i++) { // counting the cows
			cows += Math.min(arr1[i], arr2[i]);
		}
		Score += cows * 3;
		
		if(bulls == 0 && cows == 0) {
			Score -= 10;
		}
		
		if (GameIsOver == false)
			ShowHint(bulls, cows, answer, CurrentTry);
	}

	public static void GameOver() {
		System.out.println("Hienoa voitit pelin!");
		Score += 100;
		System.out.println("Pisteet: " + Score);
		GameIsOver = true;
	}

	public static void GameLost() {
		System.out.println("Et voittanut tällä kertaa. Parempi onni ensi kerralla!");
		// System.exit(0);
	}

	/// Prints to player whether they had bulls or cows in their answer
	public static void ShowHint(int bulls, int cows, String answer, int CurrentTry) {
		System.out.println("Bulls: " + bulls + " Cows: " + cows);
		AskForAnswer(CurrentTry);
	}

	/// Ask to play again and return the answer back to do while loop
	public static boolean PlayAgainOrNot() throws IOException {

		System.out.println(" \n Haluatko pelata uudelleen? y/n ");
		String answer = scanner.next();
		if (answer.matches("n")) {
			System.out.println("Nähdään taas!");
			// System.exit(0);
			return false;
		} else if (answer.matches("y")) {
			FullLeaderBoard();
			GameIsOver = false;
			return true;
		}
		return true;
	}
	
	public static void GetRandomHiddenWord() 
	{
		String[] HiddenWords = { "koira", "torni", "marsu", "vuohi" };
		
		Random RandomHiddenWord = new Random();
		int index = RandomHiddenWord.nextInt(HiddenWords.length);
		HiddenWord = HiddenWords[index];	
		System.out.println("Sana ladattu.");
	}
}

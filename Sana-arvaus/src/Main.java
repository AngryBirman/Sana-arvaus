import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

/**
 * Word-guessing game
 * <p>
 * This game asks the player for the words to guess. Depending on the difficulty
 * the player has certain amount of tries to get the word(s) correct. After a
 * game the player can view their placement on the leaderboards
 * <p>
 * 
 *
 * 
 * @author Antti Leskinen
 * @author Antti Laatikainen
 * @author Aarni Roininen
 * @author Markus Wallin
 * @version r1.0.1
 */

public class Main {

	private static final Scanner scanner = new Scanner(System.in);
	private static final Scanner namescanner = new Scanner(System.in);

	static String HiddenWord = ""; // Initializing empty string
	static String Name = ""; // name of the player
	static int Score = 0; // global score of the player
	static boolean GameIsOver = false; // helps to reset the game
	static int Difficulty;

	/**
	 * @param main
	 *            actually runs the game
	 * @throws IOException
	 *             gives an error message to the developer if something went wrong
	 */

	public static void main(String[] args) throws IOException {

		do {
			int CurrentTry = 5;
			ResetGame();

			System.out.println("Tervetuloa sana-arvaus peliin!"); // re-playable intro
			Score = 0;
			ChooseDifficulty();
			System.out.println("Syötä nimesi:");
			Name = namescanner.nextLine();
			System.out.println("Tervetuloa: " + Name + "!");
			System.out.println("Osaatko arvata 5 kirjaimisen sanan?");

			// giving tries based on the chosen difficulty
			if (Difficulty == 1) {
				CurrentTry = 10;
			} 
			else if (Difficulty == 2) {
				CurrentTry = 5;
			} else {
				CurrentTry = 3;
			}
			AskForAnswer(CurrentTry);
		} while (PlayAgainOrNot() == true);
		CreateLeaderBoard();
		AskLeaderBoard();
	}

	/**
	 * @param AskLeaderBoard()
	 *            asks user to load the leaderboard
	 * @throws IOException
	 *             gives an error message to the developer if something went wrong
	 */
	// ask user to load the leaderboard
	public static void AskLeaderBoard() throws IOException {

		System.out.println("Haluatko ladata tulostaulukon? y/n/5(näyttää viisi ensimmäistä)");
		String BoardAnswer = scanner.next();
		if (BoardAnswer.matches("n")) {
			System.exit(0); // fix multiple runs of the game not exiting properly
		} else if (BoardAnswer.matches("y")) {
			PrintCreateLeaderBoard();
			System.exit(0);
		} else if (BoardAnswer.matches("5")) {
			PrintTop5LeaderBoard();
			System.exit(0);
		}
	}

	/**
	 * @param PrintCreateLeaderBoard
	 *            prints the leaderboard
	 * 
	 */
	// print the full leaderboard
	public static void PrintCreateLeaderBoard() {

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

	/**
	 * @param CreateLeaderBoard
	 *            creates the leaderboard
	 * @throws IOException
	 *             gives an error message to the developer if something went wrong
	 */
	// create and write the full leaderboard
	public static void CreateLeaderBoard() throws IOException {
		try {
			final PrintWriter writer = new PrintWriter(new FileWriter("leaderboardfull.txt", true));

			writer.println(" Nimi: " + Name + " Pisteet " + Score);
			writer.close();
		} catch (IOException e) {
			System.out.println("Ohjelmassa tapahtui virhe.");
			e.printStackTrace();
		}
	}

	/**
	 * @param PrintTop5LeaderBoard
	 *            prints the top 5 leaderboard
	 */
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

	/**
	 * @param AdkForAnswer
	 *            asks user to input their answer and checks its validity
	 * @throws IOException
	 *             gives an error message to the developer if something went wrong
	 */
	// Ask user for input and check its validity
	public static void AskForAnswer(int CurrentTry) throws IOException {

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

	/**
	 * @param CheckAnswerValidity()
	 *            checks that the answer is correct the length
	 */
	public static void CheckAnswerValidity(String answer, int CurrentTry) throws IOException {
		boolean testi = false; // for debugging a weird bug more info from laatikainen

		if (GameIsOver == false) // if the game is already over no need to check the answer

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

	/**
	 * @param IsIsogram
	 *            checks if the word is isogram
	 */
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

	/**
	 * @param CheckAnswer()
	 *            Checks that the player inputted the correct answer
	 * @throws IOException
	 *             gives an error message to the developer if something went wrong
	 */
	public static void CheckAnswer(String answer, int CurrentTry) throws IOException {

		if (CurrentTry > 0) {
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

			if (bulls == 0 && cows == 0) {
				Score -= 10;
			}

			if (GameIsOver == false)
				ShowHint(bulls, cows, answer, CurrentTry);
		} else {
			GameLost();
		}
	}

	/**
	 * @param GameOver()
	 *            informs the player that the game ended in victory and shows
	 *            attained score
	 */
	public static void GameOver() {
		System.out.println("Hienoa voitit pelin!");
		Score += 100;
		System.out.println("Pisteet: " + Score);
		GameIsOver = true;
	}

	/**
	 * @param GameLost
	 *            informs the player that they lost the game
	 * @throws IOException
	 *             gives an error message to the developer if something went wrong
	 */
	public static void GameLost() throws IOException {
		System.out.println("Et voittanut tällä kertaa. Parempi onni ensi kerralla!");
		PlayAgainOrNot();
	}

	/**
	 * @param ShowHint()
	 *            shows the player how many correct answer they have
	 * @throws IOException
	 *             gives an error message to the developer if something went wrong
	 */
	/// Prints to player whether they had bulls or cows in their answer
	public static void ShowHint(int bulls, int cows, String answer, int CurrentTry) throws IOException {
		if (CurrentTry > 0) {
			System.out.println("Oikea kirjain, oikea järjestys: " + bulls + " Oikea kirjain, väärä järjestys: " + cows);
			AskForAnswer(CurrentTry);
		}
	}

	/**
	 * @param PlayAgainOrNot
	 *            ask the player if they want to play again or not
	 * @throws IOException
	 *             gives an error message to the developer if something went wrong
	 */
	/// Ask to play again and return the answer back to do while loop
	public static boolean PlayAgainOrNot() throws IOException {

		System.out.println(" \n Haluatko pelata uudelleen? y/n ");
		String answer = scanner.next();
		if (answer.matches("n")) {
			System.out.println("Nähdään taas!");
			return false;
			//System.exit(0);
		} else if (answer.matches("y")) {
			CreateLeaderBoard();
			main(null);
			// GameIsOver = false;
			return true;
		}
		return true;
	}

	/**
	 * @param GetRandomHiddenWord()
	 *            loads the word to guess from a fixed list
	 * 
	 */
	public static void GetRandomHiddenWord() {
		/*
		 * String[] HiddenWords = { "koira", "torni", "marsu", "vuohi" };
		 * 
		 * Random RandomHiddenWord = new Random(); int index =
		 * RandomHiddenWord.nextInt(HiddenWords.length); HiddenWord =
		 * HiddenWords[index]; System.out.println("Sana ladattu.");
		 */

		ArrayList<String> HiddenWords = new ArrayList<String>();
		HiddenWords.add("koira");
		HiddenWords.add("torni");
		HiddenWords.add("marsu");
		HiddenWords.add("vuohi");

		Random RandomHiddenWord = new Random();
		int index = RandomHiddenWord.nextInt(HiddenWords.size());
		HiddenWord = HiddenWords.get(index);
		System.out.println("Sana ladattu.");
	}

	/**
	 * @param ResetGame()
	 *            resets the game when the game ends
	 */
	public static void ResetGame() {
		GetRandomHiddenWord();
		GameIsOver = false;

	}

	/**
	 * @param ChooseDifficulty()
	 *            Asks the player to choose their difficulty from 3 options
	 */
	public static void ChooseDifficulty() {
		// Difficulty = 0;
		System.out.println("Ole hyvä ja valitse vaikeustasosi." + "\n 1: helppo \n 2: keskitaso \n 3: vaikea");

		try {
			Difficulty = scanner.nextInt();
		} catch (Exception e) {
			System.out.println("Anna vaikeustasosi muodossa 1,2 tai 3");

		}

	}
}

import java.util.Arrays;
import java.util.Scanner;


public class Main {
	
	private static final Scanner lukija = new Scanner(System.in);
	
	static String HiddenWord = "koira"; //TODO optio useammalle sanalle
	static boolean peliOhi = false; // t�m� vain auttaa pelin resetoimisessa

	public static void main(String[] args) 
	{
		do{
		System.out.println("Tervetuloa sana-arvaus peliin!"); // intro do while jotta voi pelata uudelleen
		System.out.println("Osaatko arvata 5 kirjaimisen sanan?");
		int CurrentTry = 5;	
		kysyVastaus(CurrentTry);
		}while(uudestaanko() == true);
	}
		
	
	
	/// Kysyt��n vastausta ja n�ytet��n pelitilanne
	public static void kysyVastaus(int CurrentTry)
	{
		
		if(CurrentTry == 0){peliH�vitty();} // tarkistaa jatkuuko peli yritysten m��r�st�
		else if(CurrentTry > 0 )
		{
			System.out.println("Yrityksi� j�ljell�: " + CurrentTry);
			System.out.println("Arvaa sana: "); // kysyt��n arvaus
			String vastaus = lukija.next();
			CurrentTry = CurrentTry - 1;
			if(vastaus.matches(HiddenWord))
			{
				peliOhi();
			}
			TarkistaVastaus(vastaus, CurrentTry);
		}
	
	}
		
	
	
	public static void TarkistaVastaus(String vastaus, int CurrentTry)
	{
		boolean testi = false; // debuggaus ty�kalu outoon bugiin
		if(peliOhi ==false) // jos peli onjo p��ttynyt ei tarvitse tarkistaa
		{
		
		if(vastaus.length() < 5 )
		{
			System.out.println("Vastauksesi on liian lyhyt!");
			CurrentTry = CurrentTry + 1;
			testi =true;
			kysyVastaus(CurrentTry);
		}
		
		if(vastaus.length() > 5 )
		{
			System.out.println("Vastauksesi on liian pitk�!");
			CurrentTry = CurrentTry + 1;
			testi = true;
			kysyVastaus(CurrentTry);
		}
		
		
			
		if (vastaus.matches(".*[A-Z].*"))
			{
				System.out.println("K�yt� vain pieni� kirjaimia!");
				CurrentTry = CurrentTry + 1;
				kysyVastaus(CurrentTry);
		
			}
			
		if(onkoIsogrammi(vastaus)==true & testi == false)
			{
				System.out.println("Sanasi ei ole isogrammi!");
				CurrentTry = CurrentTry + 1;
				kysyVastaus(CurrentTry);
			}
		if(peliOhi == false)
		TarkistaOsumat(vastaus, CurrentTry);
		
		}
	}
	
	
	
	// tarkistetaan onko sana isogrammi eli esiintyyk� siin� sama kirjain useammin kuin kerran
	static boolean onkoIsogrammi(String vastaus)
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
	
	public static void TarkistaOsumat(String vastaus,int CurrentTry)
	{
			    
			int bulls=0;
		    int cows =0;

		    int[] arr1 = new int[100];
		    int[] arr2 = new int[100];
		 
		 
		    for(int i=0; i<HiddenWord.length(); i++){ // verrataan sanoja
		        char hwChar = HiddenWord.charAt(i); 
		        char guessChar = vastaus.charAt(i);
		        
		        if(hwChar==guessChar)//jos ovat samat se on bull!
		            bulls++;
		        else{
		            arr1[hwChar-'0']++;
		            arr2[guessChar-'0']++;
		        }    
		    }
		 
		    for(int i=0; i<100; i++){ // lasketaan cowsit
		        cows += Math.min(arr1[i], arr2[i]);
		       
		    }
		    TulostaVihje(bulls, cows, vastaus, CurrentTry);
		}
	
	public static void peliOhi()
	{	
		System.out.println("Hienoa voitit pelin!");
		peliOhi = true;	
	}
	
	public static void peliH�vitty()
	{
		System.out.println("Et voittanut t�ll� kertaa. Parempi onni ensi kerralla! jouu");
		//System.exit(0);
	}
	
		
		
	/// T�m� metodi vain tulostaa pelaajalle vihjeet
	public static void TulostaVihje(int bulls, int cows, String vastaus, int CurrentTry)
	{
		System.out.println("Bulls: " + bulls + " Cows: "+ cows);
		kysyVastaus(CurrentTry);
	}
	
	/// kysyt��n haluako pelaaja pelata uudelleen ja palautetaan tulos while- loopille
	public static boolean uudestaanko() {

		System.out.println(" \n Haluatko pelata uudelleen? y/n ");
		String vastaus = lukija.next();
		if (vastaus.matches("n")) {
			System.out.println("N�hd��n taas!");
			System.exit(0);
			return false;
		} else if (vastaus.matches("y")) 
		{
			peliOhi =false;
			return true;
		}
		return true;
	}
}

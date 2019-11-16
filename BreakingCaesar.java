public class BreakingCaesar {

	public static void main(String [] args) {
		//Program could be called with no parameters
		//Therefore we use an if statement to check the length of args and check that only one parameter has been called
		if(args.length<1 || args[0].length()<1) {
			//No parameters have been given
			System.out.println("Oops, you haven't given enough parameters!\r\n" + "Usage:  java BreakCaesar \"string\"");

		} else {
			//One parameter has been given therefore we carry on with the program
			String[] possibleDecrypt = new String[26];		//String Array that will hold all possible cipher shift of the program input
			double[] decryptScores = new double[26];		//double Array that will hold chi-squared scores for all possible cipher shifts

			//For loop that will loop 26 times
			//26 is how many possible decrypts there could be
			int lowestIndex = 0;
			double lowest = 999;
			for(int i=0; i<26; i++) {
				possibleDecrypt[i] = CaesarCipher(args[0], i);			//Store all possible ciphers in the possibleDecrypt Array
				decryptScores[i] = chiSquaredScore(possibleDecrypt[i]); //Then we call chiSquaresScore to score each possible decryption

				//The lowest chi-squared score will be the closest to English therefore we loop 26 times to find the lowest score
				if(decryptScores[i] < lowest) {
					lowestIndex = i;
					lowest = decryptScores[i];
				}
			}
			//Printing results to console
			System.out.println(args[0]);
			System.out.println(possibleDecrypt[lowestIndex]);
		}
		//End of program
	}

	public static String CaesarCipher(String inputString, int shift) {
		char[] charArray = inputString.toCharArray(); //splitting the input string by each character and adding them to a character array

		//looping through the characters of the input string
		for(int index=0; index<inputString.length(); index++) {
			char ch = charArray[index];		//current character of the input string

			if((ch>='a' && ch<='z') || (ch>='A' && ch<='Z')) { //check if char is a letter
				//For Capital letters
				if(ch-'A'>=0 && ch-'A'<26) {
					charArray[index] = (char) ((ch-'A' + shift) % 26  + 'A'); //replacing the old value of the character and which the new value
				}
				//For lower case letters
				if(ch-'a'>=0 && ch-'a'<26 ) {
					charArray[index] = (char) ((ch-'a' + shift) % 26  + 'a'); //replacing the old value of the character and which the new value
				}
			} else {
				//For symbols and non-letter characters we don't add any shift
				charArray[index] = ch;
			}
		}
		String output = new String(charArray); //Turning the charArray back into a string
		return output;
	}

	public static double chiSquaredScore(String possible) {
		double[] knownFreq = {0.0855, 0.0160, 0.0316, 0.0387, 0.1210, 0.0218, 0.0209, 0.0496, 0.0733, 0.0022,
							  0.0081, 0.0421, 0.0253, 0.0717, 0.0747, 0.0207, 0.0010, 0.0633, 0.0673, 0.0894,
							  0.0268, 0.0106, 0.0183, 0.0019, 0.0172, 0.0011}; //Letter frequency to break Caesar
		double[] stringFreq = new double[26];
		int validChars = 0;

		//To calculate the letter frequency we need to divide by the total letters
		//This means that we need to exclude any non-letter characters
		for (int x=0; x<possible.length(); x++) {
			char ch = possible.charAt(x);
			if((ch>='a' && ch<'z') || (ch>='A' && ch<'Z'))
				validChars++;
		}

		//Calculating the frequency frequency of each character
		int index=0;
		for(char ch='a'; ch<='z'; ch++) {
			double counter=0;
			for(int a=0; a<possible.length(); a++) {
				if(ch == possible.charAt(a)) {
					counter++;
				}
			}
			//Letter frequency = number of occurrences / total number of letters in the string
			stringFreq[index] = counter / validChars;
			index++;
		}
		//calculating the chi-squared score
		double chiScore=0.0;
		for(int q=0; q<26; q++) { //Looping 26 times
			double freqEnglish = stringFreq[q] - knownFreq[q];			//letter frequency - known frequency
			double freqEnglishSquared = Math.pow(freqEnglish, 2);		//square the value of freqEnglish
			chiScore = chiScore + (freqEnglishSquared / knownFreq[q]);	//adding the value of freqEnglishSquared / knownFreq to chiScore
		}
		return chiScore;
	}

}

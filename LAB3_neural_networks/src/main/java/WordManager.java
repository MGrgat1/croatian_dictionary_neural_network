import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class WordManager {

    /**
     *
     * For example, the word "ABCD" has the character sum 0+1+2+3 = 6
     * The largest possible value of a character is 25, or 'Z'-'A'
     * @param word
     * @return
     */
    public static double getAverageAlphabeticValue(String word) {

        int alphabetSize = 30;

        int characterSum = 0;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            int value = Character.toUpperCase(ch) - 'A';

            if(value > 30)
                value = 30;
            else
                if (value < 0)
                    value = 0;

            characterSum += value;       //the word "ABCD" has the character sum 0+1+2+3 = 6
        }



        return characterSum / ((double) word.length()*alphabetSize);
    }

    /**
     * An implementation of the Wagner and Fisher algorithm for calculating the edit distance between two strings.
     * Source: https://www.geeksforgeeks.org/java-program-to-implement-wagner-and-fisher-algorithm-for-online-string-matching/
     *
     *            P A P A R
     *          0 1 2 3 4 5
     *      P   1 0 1 2 3 4
     *      E   2 1 1 2 3 4
     *      P   3 2 2 1 2 3
     *      P   4 3 3 2 3 4
     *      E   5 4 4 3 3 4
     *      R   6 5 5 4 4 3
     *      The final distance is the last entry of the matrix: d(PEPPER, PAPAR) = 3
     */
    public static double calculateEditDistance(String word1, String word2)
    {

        int length1 = word1.length();
        int length2 = word2.length();

        // if the length of one string is 0 and the other is n, there needs to be n additions
        // to get from one string to the other, so the edit distance is n
        if (length1 == 0)
            return length2;

        if (length2 == 0)
            return length1;

        int matrix[][] = new int[length1 + 1][length2 + 1];

        for (int i = 0; i <= length1; i++)
            matrix[i][0] = i;

        for (int j = 0; j <= length2; j++)
            matrix[0][j] = j;

        //calculating the distances of prefixes of the strings will let us obtain the distance of the strings themselves
        for (int i = 1; i <= length1; i++) {
            char ch1 = word1.charAt(i - 1);

            for (int j = 1; j <= length2; j++) {
                char ch2 = word2.charAt(j - 1);

                int m = ch1 == ch2 ? 0 : 1;

                matrix[i][j] = Math.min(
                        Math.min((matrix[i - 1][j] + 1), (matrix[i][j - 1] + 1)),
                        matrix[i - 1][j - 1] + m);
            }
        }

        // the distance of the complete strings is the last entry of the matrix,
        // obtained by comparing and incrementing its previous entries
        int editDistance =  matrix[length1][length2];

        int maxEditDistance = Math.max(length1, length2);

        return editDistance / (double) maxEditDistance;
    }

    public static double getLengthParameter(String string) {
        int MAX_LENGTH = 20;

        return string.length()/((double) MAX_LENGTH);

    }

    public static double getVowelRatio(String string) {
        int count = 0;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == 'a' || string.charAt(i) == 'e'
                    || string.charAt(i) == 'i'
                    || string.charAt(i) == 'o'
                    || string.charAt(i) == 'u') {
                count++;
            }
        }
        return count/(double) string.length();
    }

    public static char getMaxLetter(String string) {
        int count = 0;

        char maxLetter = 'a';
        for (int i = 0; i < string.length(); i++) {
            char currentLetter = Character.toLowerCase(string.charAt(i));
            if (currentLetter > maxLetter) {
                maxLetter = currentLetter;
                //System.out.println("Max letter is now " + maxLetter);
            }
        }
        return maxLetter;
    }

    public static char getMinLetter(String string) {
        int count = 0;

        char minLetter = 'z';
        for (int i = 0; i < string.length(); i++) {
            char currentLetter = Character.toLowerCase(string.charAt(i));
            if (currentLetter < minLetter) {
                minLetter = currentLetter;
                //System.out.println("Min letter is now " + minLetter);
            }
        }
        return minLetter;
    }

    public static double countRepeatingLetters(String string) {
        int count = 0;

        Set<Character> charactersSoFar = new HashSet<>();
        for (int i = 0; i < string.length(); i++) {
            Character currentLetter = Character.toLowerCase(string.charAt(i));
            if (charactersSoFar.contains(currentLetter)) {
                count++;
                //System.out.println("Repeating letters count: " + count);
            }
            charactersSoFar.add(currentLetter);
        }
        return count/(double) string.length();
    }

}

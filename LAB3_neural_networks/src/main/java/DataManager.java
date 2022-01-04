import java.util.List;
import java.util.Random;

public class DataManager {

    /**
     * Separate the training set from the test set using the Monte Carlo method. The neural network will be trained
     * using words from the training set, while words from the test set will be held over and given to it only after
     * training in order to test the network.
     * @param wordsFromDataset
     * @param trainingWords
     * @param testWords
     */
    public static void createMonteCarloDivision(List<Word> wordsFromDataset, List<Word> trainingWords, List<Word> testWords) {

        long seed = System.currentTimeMillis();

        Random random = new Random(seed);
        boolean chosenForTrainingSet;
        for(Word word: wordsFromDataset) {
            chosenForTrainingSet = random.nextBoolean();
            if(chosenForTrainingSet){
                trainingWords.add(word);
            } else {
                testWords.add(word);
            }
        }

        /*
        System.out.println("Training set:");
        System.out.println(trainingWords);
        System.out.println("Test set:");
        System.out.println(testWords);

         */


    }

    public static String[] getInputLabels(List<Word> words) {

        String[] input = new String[words.size()];

        for (int i = 0; i < words.size(); i++) {
            input[i] = words.get(i).getWordString();
        }

        return input;
    }

    public static String[] getOutputLabels(List<Word> words) {

        String[] output = new String[words.size()];

        for (int i = 0; i < words.size(); i++) {
            output[i] = words.get(i).getDesiredOutputLabel();
        }

        return output;
    }

    /**
     * Four parameters: first letter, last letter, word value, length value
     * @param data
     * @return
     */
    public static double[][] convertIntoInput(String[] data, int inputSize) {

        double[][] normalizedData = new double[data.length][inputSize];


        for (int i = 0; i < data.length; i++) {
            String string = data[i];
            double firstLetterNormalized = normalizeChar(string.charAt(0));
            double lastLetterNormalized = normalizeChar(string.charAt(string.length() - 1));
            double maxLetterNormalized = normalizeChar(WordManager.getMaxLetter(string));
            double minLetterNormalized = normalizeChar(WordManager.getMinLetter(string));
            double lengthParameter = WordManager.getLengthParameter(string);
            double averageAlphabeticValue = WordManager.getAverageAlphabeticValue(string);
            double vowelRatio = WordManager.getVowelRatio(string);
            double repeatingLettersRatio = WordManager.countRepeatingLetters(string);

            normalizedData[i][0] = firstLetterNormalized;
            normalizedData[i][1] = lastLetterNormalized;
            normalizedData[i][2] = maxLetterNormalized;
            normalizedData[i][3] = minLetterNormalized;
            normalizedData[i][4] = lengthParameter;
            normalizedData[i][5] = averageAlphabeticValue;
            normalizedData[i][6] = vowelRatio;
            normalizedData[i][7] = repeatingLettersRatio;

            /*
            System.out.println("[INFO] Normalized word: " + string);
            System.out.println("First letter: " + firstLetterNormalized);
            System.out.println("Last letter: " + lastLetterNormalized);
            System.out.println("Max letter: " + maxLetterNormalized);
            System.out.println("Min letter: " + minLetterNormalized);
            System.out.println("Length: " + lengthParameter);
            System.out.println("Average alphabetic value: " + averageAlphabeticValue);
            System.out.println("Vowel ratio: " + vowelRatio);
            System.out.println("Repeating letter ratio: " + repeatingLettersRatio);

             */

        }

        return normalizedData;
    }


    private static double normalizeChar (char ch) {

        //System.out.println("Normalizing " + Character.toLowerCase(ch));
        int alphabetSize = 30;
        double alphabeticValue = Character.toLowerCase(ch) - 'a';

        //System.out.println("Alphabetic value: " + alphabeticValue);

        if (alphabeticValue > 30)
            alphabeticValue = 30;
        else if (alphabeticValue < 0.0)
            alphabeticValue = 0.0;

        double ratio = alphabeticValue/ (double) alphabetSize;

        //System.out.println("Ratio: " + ratio);

        return alphabeticValue/ (double) alphabetSize;
    }

    public static void printNormalizedData(String[] data, double[][] normalizedData) {
        for (int i = 0; i < normalizedData.length; i++) {
            System.out.print(data[i]);
            System.out.print(",");
            System.out.println();
            for (int j = 0; j < normalizedData[i].length; j++) {
                System.out.print(normalizedData[i][j]);
                System.out.print(",");
            }
            System.out.println("");
        }
    }

    /**
     * Output encoded with one-hot encoding (20 parameters)
     * @param outputLabels
     * @return
     */
    public static double[][] convertIntoOutput(String[] outputLabels) {

        double[][] normalizedOutput = new double[outputLabels.length][20];

        //one hot normalizedOutput[i] for each output label
        for (int i = 0; i < outputLabels.length; i++) {
            String string = outputLabels[i];
            if (string.equals("avokado")) normalizedOutput[i][0] = 1;
            if (string.equals("banana")) normalizedOutput[i][1] = 1;
            if (string.equals("juha")) normalizedOutput[i][2] = 1;
            if (string.equals("kečap")) normalizedOutput[i][3] = 1;
            if (string.equals("kobasica")) normalizedOutput[i][4] = 1;
            if (string.equals("kruh")) normalizedOutput[i][5] = 1;
            if (string.equals("krumpir")) normalizedOutput[i][6] = 1;
            if (string.equals("kukuruz")) normalizedOutput[i][7] = 1;
            if (string.equals("luk")) normalizedOutput[i][8] = 1;
            if (string.equals("meso")) normalizedOutput[i][9] = 1;
            if (string.equals("mlijeko")) normalizedOutput[i][10] = 1;
            if (string.equals("mrkva")) normalizedOutput[i][11] = 1;
            if (string.equals("papar")) normalizedOutput[i][12] = 1;
            if (string.equals("pivo")) normalizedOutput[i][13] = 1;
            if (string.equals("sol")) normalizedOutput[i][14] = 1;
            if (string.equals("umak")) normalizedOutput[i][15] = 1;
            if (string.equals("vino")) normalizedOutput[i][16] = 1;
            if (string.equals("viski")) normalizedOutput[i][17] = 1;
            if (string.equals("voda")) normalizedOutput[i][18] = 1;
            if (string.equals("špinat")) normalizedOutput[i][19] = 1;
        }

        return normalizedOutput;
    }

    public static int classifyOutput(double[] output) {
        int indexOfMaxParameter = -1;
        double maxParameter = 0;
        for (int i = 0; i < output.length; i++) {
            if (maxParameter < output[i]) {
                maxParameter = output[i];
                indexOfMaxParameter = i;
            }
        }

        return indexOfMaxParameter;

    }

    public static String getWordFromIndex(int indexOfMaxParameter) {
        return switch (indexOfMaxParameter) {
            case 0 -> "avokado";
            case 1 -> "banana";
            case 2 -> "juha";
            case 3 -> "kečap";
            case 4 -> "kobasica";
            case 5 -> "kruh";
            case 6 -> "krumpir";
            case 7 -> "kukuruz";
            case 8 -> "luk";
            case 9 -> "meso";
            case 10 -> "mlijeko";
            case 11 -> "mrkva";
            case 12 -> "papar";
            case 13 -> "pivo";
            case 14 -> "sol";
            case 15 -> "umak";
            case 16 -> "vino";
            case 17 -> "viski";
            case 18 -> "voda";
            case 19 -> "špinat";
            default -> "X";
        };
    }
}


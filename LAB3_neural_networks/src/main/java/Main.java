import org.encog.neural.networks.BasicNetwork;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.*;


/**
 * Korišteni izvori:
 * 1)Java Encog Neural Network
 * https://www.youtube.com/watch?v=KrYuyXutMsQ&t=298s
 *
 * 2)Indifferent languages
 * https://www.indifferentlanguages.com
 *
 */

public class Main {

    public static void main(String[] args) throws IOException {

        int INPUT_SIZE = 8;
        int OUTPUT_SIZE = 20;

        List<Word> wordsFromDataset = FileManager.getTrainingWords();
        //List<Word> testWords = FileManager.GetHoldoutWords();

        //separate the training set from the test set using the Monte Carlo method
        List<Word> trainingWords = new ArrayList<>();
        List<Word> testWords = new ArrayList<>();
        DataManager.createMonteCarloDivision(wordsFromDataset, trainingWords, testWords);

        String[] trainingInput = DataManager.getInputLabels(trainingWords);
        String[] testInput = DataManager.getInputLabels(testWords);
        String[] desiredTrainingOutput = DataManager.getOutputLabels(trainingWords);
        String[] desiredTestOutput = DataManager.getOutputLabels(testWords);


        double[][] normalizedTrainingInput = DataManager.convertIntoInput(trainingInput, INPUT_SIZE);
        double[][] normalizedTestInput = DataManager.convertIntoInput(testInput, INPUT_SIZE);
        double[][] normalizedTrainingOutput = DataManager.convertIntoOutput(desiredTrainingOutput);
        double[][] normalizedTestOutput = DataManager.convertIntoOutput(desiredTestOutput);
        
        for (int i = 0; i < 5; i++) {
            System.out.println(" " + (i + 1));
            BasicNetwork network = NeuralNetworkManager.createBasicNetwork(INPUT_SIZE, OUTPUT_SIZE);
            NeuralNetworkManager.train(network, normalizedTrainingInput, normalizedTrainingOutput, 0.04);

            /*
            System.out.println("Veličina skupa za treniranje: " + trainingWords.size());
            System.out.println("Veličina skupa za testiranje: " + testWords.size());
             */

            System.out.println("Klasifikacija seta za treniranje:");
            NeuralNetworkManager.test(network, normalizedTrainingInput, normalizedTrainingOutput);
            System.out.println("Klasifikacija seta za testiranje:");
            NeuralNetworkManager.test(network, normalizedTestInput, normalizedTestOutput);
        }

    }

}

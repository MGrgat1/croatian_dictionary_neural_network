import org.encog.engine.network.activation.*;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
import org.encog.neural.networks.training.propagation.quick.QuickPropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class NeuralNetworkManager {



    public static BasicNetwork createBasicNetwork(int inputSize, int outputSize) {
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, inputSize));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 30));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 30));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 30));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 30));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, outputSize));
        network.getStructure().finalizeStructure();
        network.reset();
        return network;
    }

    public static void train(BasicNetwork network, double[][] data, double[][] expected, double minError) {
        MLDataSet trainingSet = new BasicMLDataSet(data, expected);
        final MLTrain train = new QuickPropagation(network, trainingSet, 0.0001);
        int epoch = 1;
        int maxEpochsAllowed = 20000;
        //measuring the behavior of the error gradient
        int numberOfSnapshotsToTake = 10;
        int unitEpoch = 2000;
        double[] errorSnapshots = new double[numberOfSnapshotsToTake];
        int snapshotCounter = 0;

        //measuring the way the errors repeat and increase
        System.out.println("[INFO] Training");
        int numberOfRepeatedErrors = 0;
        int numberOfIncreasingErrors = 0;
        double error1;
        double error2 = 0.0;

        boolean trainingEndedEarly = false;


        do {
            train.iteration();
            epoch++;
            error1 = train.getError();

            double errorStep = error1-error2;

            //take a snapshot of the error gradient every few epochs
            if(epoch % unitEpoch == 0 ) {
                //System.out.println("[INFO] Epoch: " + epoch +  "\nTraining error: " + error2 + "\nError distance = " + errorStep);
                if(snapshotCounter < numberOfSnapshotsToTake) {
                    errorSnapshots[snapshotCounter] = error1;
                    snapshotCounter++;
                }
            }

            if(errorStep > 0) {
                //System.out.println("[INFO] The error is increasing");
                numberOfIncreasingErrors++;
            } else {
                numberOfIncreasingErrors = 0;     //this number will stay at 0 as long as the errors don't increase
            }

            if (Math.abs(errorStep) < 0.00001) {
                numberOfRepeatedErrors++;
                if(numberOfRepeatedErrors > 1000) {
                    //System.out.println("[INFO] Training is stuck on the same error: " + error1);
                    /*
                    trainingEndedEarly = true;
                    break;
                     */
                }
            } else {
                numberOfRepeatedErrors = 0;     //this number will stay at 0 as long as the errors don't stay on the same value
            }


            if(epoch > maxEpochsAllowed) {
                trainingEndedEarly = true;
                break;
            }

            error2 = error1;

        } while (error1 > minError);
        train.finishTraining();
        //System.out.println("Trening gotov!");
        if(trainingEndedEarly) {
            System.out.println("Trening prekinut nakon " + epoch + " epoha.");
            System.out.println("Broj epoha: " + epoch + "\nMinimalna pogreška: " + train.getError());
        } else {
            System.out.println("Broj epoha: " + epoch + "\nMinimalna pogreška: " + train.getError());
        }
        //System.out.println("[INFO] Ovako su izgledale pogreške svakih " + unitEpoch + " epoha:");
        //System.out.println(Arrays.toString(errorSnapshots));

    }

    public static void test(BasicNetwork network, double[][] input, double[][] desiredOutput) {

        MLDataSet testSet = new BasicMLDataSet(input, desiredOutput);

        int accurate = 0;
        int total = 0;
        //System.out.println("[INFO] Entering classification");
        int[] correctlyClassifiedWords = new int[20];

        for(MLDataPair pair : testSet) {
            total++;
            MLData output = network.compute(pair.getInput());
            int expectedWordIndex = DataManager.classifyOutput(pair.getIdeal().getData());
            int classifiedWordIndex = DataManager.classifyOutput(output.getData());

            String expectedWord = DataManager.getWordFromIndex(expectedWordIndex);
            String classifiedWord = DataManager.getWordFromIndex(classifiedWordIndex);

            if(expectedWord.equals(classifiedWord)) {

                //storing the information about how the correct classifications are distributed over the possible words
                correctlyClassifiedWords[classifiedWordIndex]++;

                /*
                System.out.println("");
                System.out.println("[INFO] Classification number " + total);
                System.out.println("[INFO] Input: " + pair.getInput());
                System.out.println("[INFO] Expected word: " + expectedWord);
                System.out.println("[INFO] Classified word: " + classifiedWord);
                System.out.println("[INFO] SUCCESS! The word " + expectedWord + " was CORRECTLY classified");
                 */

                accurate++;
            } else {
                //System.out.println("[INFO] FAIL!");
            }
        }
        System.out.println("Ukupno klasifikacija: " + total);
        System.out.println("Točno klasificirano: " + accurate);
        System.out.println("Točnost: " + (accurate/(double) total) * 100 + "%");
        System.out.println("Distribucija točnih klasifikacija:");
        System.out.println(Arrays.toString(correctlyClassifiedWords));
        System.out.println();

    }
}

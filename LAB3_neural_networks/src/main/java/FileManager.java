import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileManager {

    public static List<Word> GetHoldoutWords() throws IOException {

        FileReader reader = new FileReader("src/main/languages/test_words/test-pairs.txt");
        char[] chars = new char[(int) new File("src/main/languages/test_words/test-pairs.txt").length()];
        reader.read(chars);
        String content = new String(chars);
        List<String> pairs = Arrays.asList(content.split("\r"));
        reader.close();

        List<Word> words = new ArrayList<>();
        //the first loop goes through output labels, and the second loop goes through languages
        for (int i = 0; i < pairs.size(); i++) {

            String[] splitPairs = pairs.get(i).split("-");
            splitPairs[0] = splitPairs[0].substring(1);     //removing the '\r' char from the input string
            System.out.println("[INFO] Importing testing pairs:");
            System.out.println(splitPairs[0] + "/" + splitPairs[1]);
            Word word = new Word(splitPairs[0], null, splitPairs[1]);
            words.add(word);

        }
        return words;
    }

    public static List<Word> getTrainingWords() throws IOException {
        List<List<String>> wordsFromTxtFiles = FileManager.readAllTxtFiles();
        //System.out.println(wordsFromTxtFiles);

        List<String> languages = FileManager.readLanguages();
        List<String> desiredOutputLabels = FileManager.readFileNames();
        //System.out.println(desiredOutputLabels);

        List<Word> words = new ArrayList<>();
        //the first loop goes through output labels, and the second loop goes through languages
        for (int i = 0; i < wordsFromTxtFiles.size(); i++) {
            for (int j = 0; j < wordsFromTxtFiles.get(i).size(); j++) {
                Word word = new Word(wordsFromTxtFiles.get(i).get(j), languages.get(j), desiredOutputLabels.get(i));
                words.add(word);
            }
        }
        return words;
    }

    public static List<List<String>> readAllTxtFiles() throws IOException {

        File f = new File("src/main/languages/training_words/");


        String[] fileList = f.list();           // the list of all files in the folder.

        List<List<String>> words = new ArrayList<>();
        for(String fileName : fileList){
            if(fileName.endsWith(".txt")){

                FileReader reader = new FileReader("src/main/languages/training_words/"+fileName);
                char[] chars = new char[(int) new File("src/main/languages/training_words/"+fileName).length()];
                reader.read(chars);
                String content = new String(chars);
                List<String> word = Arrays.asList(content.split("\r"));
                word = word.stream().map(string -> string.substring(1)).collect(Collectors.toList());   //removing the 'r' char from the input strings
                /*
                System.out.println("[INFO] Importing input words:");
                System.out.println(word);

                 */

                reader.close();

                words.add(word);

            }
        }
        return words;
    }

    /**
     * Creates a list of all file names (avokado, banana, juha...)
     * These file names will be the desired output of the network.
     * @return
     * @throws IOException
     */
    public static List<String> readFileNames() throws IOException {

        File f = new File("src/main/languages/training_words/");


        String[] fileList = f.list();           // the list of all files in the folder.

        List<String> outputNames = new ArrayList<>();
        for(String fileName : fileList){
            if(fileName.endsWith(".txt")){
                //gets the desired output "avokado" from the file name "avokado.txt"
                String outputName = fileName.replace(".txt","");
                outputNames.add(outputName);
            }
        }
        return outputNames;
    }

    /**
     * Creates a list of all languages.
     * @return
     * @throws IOException
     */
    public static List<String> readLanguages() throws IOException {
        FileReader reader = new FileReader("src/main/languages/languages.txt");
        char[] chars = new char[(int) new File("src/main/languages/languages.txt").length()];
        reader.read(chars);
        String content = new String(chars);
        List<String> languages = Arrays.asList(content.split("\r"));
        reader.close();

        return languages;
    }

}

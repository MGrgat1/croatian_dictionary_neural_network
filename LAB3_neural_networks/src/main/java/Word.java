public class Word {

    private String wordString;
    private String language;
    private String desiredOutputLabel;


    public Word(String wordString, String language, String desiredOutputLabel) {
        this.wordString = wordString;
        this.language = language;
        this.desiredOutputLabel = desiredOutputLabel;

    }

    public String getWordString() {
        return wordString;
    }

    public String getLanguage() {
        return language;
    }

    public String getDesiredOutputLabel() {
        return desiredOutputLabel;
    }

    @Override
    public String toString() {
        return "Word{" +
                "wordString='" + wordString + '\'' +
                ", language='" + language + '\'' +
                ", desiredOutputLabel='" + desiredOutputLabel + '\'' +
                '}';
    }
}

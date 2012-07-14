package emmaguy.memrise.dictionary;

public class LearntWord implements Comparable
{
    private String word;
    private String translatedWord;

    public LearntWord(String word, String translatedWord)
    {
        this.word = word;
        this.translatedWord = translatedWord;
    }

    public String getTranslatedWord()
    {
        return translatedWord;
    }

    public String getWord()
    {
        return word;
    }

    @Override
    public int compareTo(Object o)
    {
        return word.compareTo(((LearntWord)o).getWord());
    }

    @Override
    public String toString()
    {
       return word + " • " + translatedWord;
    }
}

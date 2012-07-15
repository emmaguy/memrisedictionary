package emmaguy.memrise.dictionary;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

public class MemriseDictionaryActivity extends Activity
{
    DatabaseProvider db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db = new DatabaseProvider(this);
        List<LearntWord> words = db.GetAllWords();

        if(words.size() <= 0)
            new MemriseDictionaryWordBuilder(this).execute();
        else
            AddWordsToListView(words);
    }

    public void UpdateDbAndAddWordsToListView(List<LearntWord> words)
    {
        db.SaveWords(words);

        AddWordsToListView(words);
    }

    private void AddWordsToListView(List<LearntWord> words)
    {
        ListView lstWords = (ListView)findViewById(R.id.lstView);
        try
        {
            lstWords.setAdapter(new LearntWordAdapter(this, R.layout.learnt_word_layout, words));
        }
        catch (Exception e)
        {
            Log.e("x", e.toString());
        }
    }
}

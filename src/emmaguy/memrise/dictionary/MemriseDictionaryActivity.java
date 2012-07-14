package emmaguy.memrise.dictionary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MemriseDictionaryActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        new MemriseDictionaryWordBuilder(this).execute();
    }

    public void AddWordsToListView(List<LearntWord> words)
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

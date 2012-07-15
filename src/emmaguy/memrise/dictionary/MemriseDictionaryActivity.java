package emmaguy.memrise.dictionary;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MemriseDictionaryActivity extends Activity
{
    DatabaseProvider db;
    List<LearntWord> words;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db = new DatabaseProvider(this);
        this.words = db.GetAllWords();

        if(this.words.size() <= 0)
            new MemriseDictionaryWordBuilder(this).execute();
        else
            AddWordsToListView(this.words);

        InitialiseSearchListener();
    }

    private void InitialiseSearchListener()
    {
        final EditText searchBox = (EditText)findViewById(R.id.txtSearch);
        searchBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                String s = searchBox.getText().toString().toLowerCase().trim();
                if(s.isEmpty())
                {
                    ClearListView();
                    AddWordsToListView(words);
                    return;
                }

                List<LearntWord> filteredWords = new ArrayList<LearntWord>();
                for(LearntWord word : words)
                {
                    if(word.getWord().toLowerCase().contains(s) || word.getTranslatedWord().contains(s))
                    {
                        filteredWords.add(word);
                    }
                }

                ClearListView();
                AddWordsToListView(filteredWords);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    private void ClearListView()
    {
        ListView lstWords = (ListView)findViewById(R.id.lstView);
        lstWords.setAdapter(null);
    }

    public void UpdateDbAndAddWordsToListView(List<LearntWord> words)
    {
        db.SaveWords(words);

        AddWordsToListView(words);
    }

    private void AddWordsToListView(List<LearntWord> words)
    {
        ListView lstWords = (ListView)findViewById(R.id.lstView);
        lstWords.setAdapter(new LearntWordAdapter(this, R.layout.learnt_word_layout, words));
    }
}

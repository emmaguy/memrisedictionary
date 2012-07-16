package emmaguy.memrise.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MemriseDictionaryActivity extends Activity
{
    DatabaseProvider db;
    List<LearntWord> words;
    private String username;
    private int totalCount;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent i = getIntent();
        username = i.getStringExtra("username");
        totalCount = i.getIntExtra("totalCount", 0);

        db = new DatabaseProvider(this);
        this.words = db.GetAllWords();

        if(this.words.size() <= 0)
        {
            getWordsFromMemrise();
        }
        else
        {
            addWordsToListView(this.words);
        }

        initialiseSearchListener();
        initialiseActionBarListener();
    }

    private void initialiseActionBarListener()
    {
        ImageButton sync = (ImageButton)findViewById(R.id.sync);
        sync.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                words = new ArrayList<LearntWord>();
                db.delete();
                db.create();
                getWordsFromMemrise();
            }
        });
    }

    private void getWordsFromMemrise()
    {
        new MemriseDictionaryWordBuilder(this, username, totalCount).execute();
    }

    private void initialiseSearchListener()
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
                    clearListView();
                    addWordsToListView(words);
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

                clearListView();
                addWordsToListView(filteredWords);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    private void clearListView()
    {
        ListView lstWords = (ListView)findViewById(R.id.lstView);
        lstWords.setAdapter(null);
    }

    public void UpdateDbAndAddWordsToListView(List<LearntWord> words)
    {
        this.words = words;
        db.SaveWords(words);
        addWordsToListView(words);
    }

    private void addWordsToListView(List<LearntWord> words)
    {
        ListView lstWords = (ListView)findViewById(R.id.lstView);
        lstWords.setAdapter(new LearntWordAdapter(this, R.layout.learnt_word_layout, words));
    }
}

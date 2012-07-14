package emmaguy.memrise.dictionary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LearntWordAdapter extends ArrayAdapter<LearntWord>
{
    private final Context context;
    private final int textViewResourceId;
    private final List<LearntWord> learntWords;

    public LearntWordAdapter(Context context, int textViewResourceId, List<LearntWord> words)
    {
        super(context, textViewResourceId, words);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.learntWords = words;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        LearntWordHolder word;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(textViewResourceId, parent, false);

            word = new LearntWordHolder();
            word.Word = (TextView)row.findViewById(R.id.word);
            word.LearntWord = (TextView)row.findViewById(R.id.learntWord);
            row.setTag(word);
        }
        else
        {
            word = (LearntWordHolder)row.getTag();
        }

        LearntWord currentWord = learntWords.get(position);
        word.Word.setText(currentWord.getWord());
        word.LearntWord.setText(currentWord.getTranslatedWord());

        return row;
    }

    static class LearntWordHolder
    {
        TextView Word;
        TextView LearntWord;
    }
}

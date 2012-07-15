package emmaguy.memrise.dictionary;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemriseDictionaryWordBuilder extends AsyncTask<Void, Void, List<LearntWord>>
{
    private Context context;

    public MemriseDictionaryWordBuilder(Context context)
    {
        this.context = context;
    }

    @Override
    protected List<LearntWord> doInBackground(Void... voids)
    {
        JSONObject json = new RequestJson("http://www.memrise.com/api/1.0/itemuser/?format=json&user__username=fyska&limit=2000").getJSONFromURL();

        try
        {
            JSONArray objectJsons = json.getJSONArray("objects");
            List<LearntWord> words = new ArrayList<LearntWord>();

            for(int i = 0; i < objectJsons.length(); i++)
            {
                JSONObject obj = (JSONObject)objectJsons.get(i);

                String word = obj.getJSONObject("item").getString("word");
                String translatedWord = obj.getJSONObject("item").getString("defn");
                words.add(new LearntWord(word, translatedWord));
            }
            Collections.sort(words);
            return words;
        }
        catch (Exception e)
        {
            Log.e("x",e.toString());
        }
        return new ArrayList<LearntWord>();
    }

    @Override
    protected void onPostExecute(List<LearntWord> words)
    {
        ((MemriseDictionaryActivity)context).UpdateDbAndAddWordsToListView(words);
    }
}

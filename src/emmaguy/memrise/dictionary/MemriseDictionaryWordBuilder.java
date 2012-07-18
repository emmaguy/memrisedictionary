package emmaguy.memrise.dictionary;

import android.app.ProgressDialog;
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
    private final String username;
    private final Integer totalCount;
    private ProgressDialog dialog;

    public MemriseDictionaryWordBuilder(Context context, String username, Integer totalCount)
    {
        this.context = context;
        this.username = username;
        this.totalCount = totalCount;
    }

    @Override
    protected void onPreExecute()
    {
        dialog = ProgressDialog.show(this.context, "Syncing words", "Syncing your words from Memrise. This may take a few minutes, please wait...", true, false);
    }

    @Override
    protected List<LearntWord> doInBackground(Void... voids)
    {
        JSONObject json = new RequestJson("http://www.memrise.com/api/1.0/itemuser/?format=json&user__username=" + username + "&limit=" + totalCount).getJSONFromURL();

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
        ((MemriseDictionaryActivity)context).updateDbAndAddWordsToListView(words);
        dialog.dismiss();
    }
}

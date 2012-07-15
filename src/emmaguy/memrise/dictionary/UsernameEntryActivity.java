package emmaguy.memrise.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsernameEntryActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username_screen);

        Button btnGo = (Button) findViewById(R.id.btnGo);

        btnGo.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                String username = ((EditText) findViewById(R.id.txtUsernameEntry)).getText().toString().trim();

                if(username.isEmpty())
                {
                    return;
                }

                JSONObject json = new RequestJson("http://www.memrise.com/api/1.0/itemuser/?format=json&user__username=" + username  + "&limit=1").getJSONFromURL();

                if(json == null)
                {
                    return;
                }

                String totalCount;
                try
                {
                    totalCount = json.getJSONObject("meta").getString("total_count");
                }
                catch (JSONException e)
                {
                    Log.e("loginJson", e.toString());
                    return;
                }

                int iTotalCount = Integer.parseInt(totalCount);
                if(iTotalCount == 0)
                {
                    return;
                }

                Intent dictionaryActivity = new Intent(getApplicationContext(), MemriseDictionaryActivity.class);
                dictionaryActivity.putExtra("username", username);
                dictionaryActivity.putExtra("totalCount", iTotalCount);

                startActivity(dictionaryActivity);
            }
        });
    }
}
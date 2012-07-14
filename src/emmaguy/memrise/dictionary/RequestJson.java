package emmaguy.memrise.dictionary;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class RequestJson
{
    private String url;

    public RequestJson(String url)
    {
        this.url = url;
    }

    public JSONObject getJSONFromURL()
    {
        BufferedReader in = requestJsonFromUrl(url);
        String result = convertResultToString(in);
        return parseStringForJson(result);
    }

    private BufferedReader requestJsonFromUrl(String url)
    {
        try
        {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
            HttpGet request = new HttpGet();
            request.setHeader("Content-Type", "text/plain; charset=utf-8");
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            return new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        }
        catch (Exception e)
        {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        return null;
    }

    private String convertResultToString(BufferedReader in)
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = in.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            return sb.toString();
        }
        catch (Exception e)
        {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        return "";
    }

    private JSONObject parseStringForJson(String result)
    {
        try
        {
            return new JSONObject(result);
        }
        catch (JSONException e)
        {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return null;
    }
}

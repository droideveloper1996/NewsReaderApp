package scorecard.project.com.newsreaderapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 06/03/2017.
 */

public class QueryUtils {

    private QueryUtils() {
    }

    private static String makeHttpRequest(URL urls) throws IOException {
        String jsonString = "";
       // URL url = createUrl(urls);
        Log.i("Called","makeHttprequest()");
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        if (urls == null) {
            return jsonString;
        }

        try {
            Log.i("Establishing","Connection");
            connection = (HttpURLConnection) urls.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                Log.i("Connection Status", "OK");
                inputStream = connection.getInputStream();
                jsonString = getFromInputStream(inputStream);
                Log.i("Called Status",Integer.toString(connection.getResponseCode()));
            }
            else{
                Log.i("error message",Integer.toString(connection.getResponseCode()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonString;

    }

    private static URL createUrl(String stringUrl) {
        Log.i("Called","createUrl()-"+stringUrl);
        URL url = null;
        if (stringUrl == null) {
            Log.i("Called","null");
            return null;

        }
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String getFromInputStream(InputStream inputStream) throws IOException {
        Log.i("Called","getFromInputStream()");
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader;
        if (inputStream != null) {
            inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        Log.i("output", output.toString());
        extractFeatureFromJson(output.toString());
        return output.toString();
    }

    private static ArrayList<News> extractFeatureFromJson(String s) {
        Log.i("called","extractFeatureFromJson()");
        ArrayList<News> news = new ArrayList<News>();
        Log.i("Called","Trying to parse JsonString");

        try {
            JSONObject root = new JSONObject(s);
            JSONObject jsonObject=root.optJSONObject("response");
            JSONArray results = jsonObject.optJSONArray("results");
            for (int i = 0; i < results.length(); i++) {

                JSONObject resultObject = results.optJSONObject(i);
                String section = resultObject.optString("sectionName");
                String webTitle = resultObject.optString("webTitle");
                String type = resultObject.optString("type");
                String publicationDate = resultObject.optString("webPublicationDate");
                String webURL=resultObject.optString("webUrl");
                Log.i("section",section);
                Log.i("webTitle",webTitle);
                Log.i("type",type);
                news.add(new News(webTitle, section, type, publicationDate,webURL));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return news;
    }

    public static List<News> fetchBookData(String requestUrl) {
        Log.i("started","fetchBooksData()");
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Problem ", e.toString());
        }
        List<News> news = extractFeatureFromJson(jsonResponse);
        return news;
    }}
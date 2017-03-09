package scorecard.project.com.newsreaderapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Abhishek on 06/03/2017.
 */

public class NewsLoader extends AsyncTaskLoader {
    private String mUrl;
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        Log.i("Called","loadInBackgroung()");
        if (mUrl == null) {
            return null;
        }
        List<News> result = QueryUtils.fetchBookData(mUrl);
        return result;
    }

}

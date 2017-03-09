package scorecard.project.com.newsreaderapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<News>> {
    private ListView mListView;
    private NewsAdapter newsAdapter;
    private ArrayList<News> news;
    private TextView mEmptyTExtView;
    private ProgressBar progressBar;

    android.app.LoaderManager loaderManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String URL_TO_FETCH = "https://content.guardianapis.com/search?q=cricket&order-by=newest&api-key=24e0000b-55d7-4c59-8994-44471580c508";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        mListView = (ListView) findViewById(R.id.listView);
        mEmptyTExtView = (TextView) findViewById(R.id.empty_text_view);
        news = new ArrayList<News>();
        newsAdapter = new NewsAdapter(this, news);
        mListView.setAdapter(newsAdapter);
        getSupportActionBar().hide();
        loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);
        mListView.setEmptyView(mEmptyTExtView);


    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, URL_TO_FETCH);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
        newsAdapter.clear();
        progressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
            mListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String url = String.valueOf(newsAdapter.getItem(i).getMwebUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });

        } else {
            mEmptyTExtView.setText(R.string.no_data_found);
        }
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("onRefresh Triggered", "Fetching data");
               newsAdapter.clear();
              //  newsAdapter.add(new News("Abhishek", "Kushwaha", "Time", "Android", "https://www.google.com"));

               /* final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String mUrl = "https://content.guardianapis.com/search?q=debate&from-date=2014-01-01&api-key=test";
                        NewsLoader newsLoader = new NewsLoader(MainActivity.this, mUrl);
                        newsLoader.onStartLoading();
                        List<News> newsList=newsLoader.loadInBackground();

                        newsAdapter.addAll(newsList);

                    }
                }, 2000);*/
                loaderManager.initLoader(4, null,MainActivity.this);


                swipeRefreshLayout.setRefreshing(false);
            }


        });
        loaderManager.destroyLoader(4);
    }


    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        newsAdapter.clear();
    }
}

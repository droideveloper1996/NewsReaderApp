package scorecard.project.com.newsreaderapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<News>> {
    android.app.LoaderManager loaderManager;
    private ListView mListView;
    private NewsAdapter newsAdapter;
    private ArrayList<News> news;
    private ImageView mImageView;
    private TextView mEmptyTExtView;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
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
        mImageView = (ImageView) findViewById(R.id.empty_image_view);
        news = new ArrayList<News>();
        newsAdapter = new NewsAdapter(this, news);
        mListView.setAdapter(newsAdapter);
        getSupportActionBar().hide();
        loaderManager = getLoaderManager();
        mListView.setEmptyView(mEmptyTExtView);
        mListView.setEmptyView(mImageView);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);

        if (isConnected()) {

            loaderManager.initLoader(1, null, this);

        } else {
            mEmptyTExtView.setText(R.string.no_internet_connectivity);
            mEmptyTExtView.setPadding(0, 15, 0, 0);
            mImageView.setImageResource(R.drawable.noconnectivity);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.connectivityColor));
        }
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String url = String.valueOf(newsAdapter.getItem(i).getMwebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("onRefresh Triggered", "Fetching data");

                newsAdapter.clear();
                if (isConnected()) {
                    mImageView.setVisibility(View.GONE);
                    mEmptyTExtView.setVisibility(View.GONE);
                    loaderManager.restartLoader(1, null, MainActivity.this);
                } else {
                    mImageView.setImageResource(R.drawable.noconnectivity);
                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.connectivityColor));
                    mEmptyTExtView.setText(R.string.no_internet_connectivity);
                    mEmptyTExtView.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
        } else {
            mImageView.setImageResource(R.drawable.noresult);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.background));
            mEmptyTExtView.setText(R.string.no_data_found);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        newsAdapter.clear();
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }


}

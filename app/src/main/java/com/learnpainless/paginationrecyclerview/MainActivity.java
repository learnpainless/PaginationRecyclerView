package com.learnpainless.paginationrecyclerview;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.learnpainless.paginationrecyclerview.adapters.VideosAdapter;
import com.learnpainless.paginationrecyclerview.api.APIService;
import com.learnpainless.paginationrecyclerview.api.YoutubeApi;
import com.learnpainless.paginationrecyclerview.models.YoutubeResponse;
import com.learnpainless.paginationrecyclerview.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private VideosAdapter adapter;
    //if you are using searchview to get search result then store searched query in lastSearched variable.
    //get latest token and store in lastToken variable.
    private String lastSearched = "", lastToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.video_list);
        Button more = findViewById(R.id.more);

        adapter = new VideosAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        //load data from api.
        search("", false);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load more data
                search("", true);
            }
        });
    }

    /**
     * call this method to get response from youtube API.
     *
     * @param query String value to search on google, Empty string means get all videos.
     * @param more  if you want to load next page then pass true, this means add new items at bottom of RecyclerView.
     */
    private void search(String query, final boolean more) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Loading ...", true, false);
        String searchType = "video";
        if (query != null) {
            if (query.startsWith("#")) {
                searchType = "video";
                query = query.substring(1);
            } else if (query.startsWith("@")) {
                searchType = "channel";
                query = query.substring(1);
            }
        }
        if (!more) {
            lastSearched = query;
            lastToken = "";
        }

        Call<YoutubeResponse> youtubeResponseCall = APIService.youtubeApi.searchVideo(query, searchType, Constants.YOUTUBE_API_KEY, "snippet,id", "10", lastToken);
        youtubeResponseCall.enqueue(new Callback<YoutubeResponse>() {
            @Override
            public void onResponse(@NonNull Call<YoutubeResponse> call, @NonNull Response<YoutubeResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                YoutubeResponse body = response.body();
                if (body != null) {
                    List<YoutubeResponse.Item> items = body.getItems();
                    lastToken = body.getNextPageToken();
                    if (more) {
                        adapter.addAll(items);
                    } else {
                        adapter.replaceWith(items);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<YoutubeResponse> call, @NonNull Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }
}

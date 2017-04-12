package com.fomono.fomono.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fomono.fomono.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.loopj.android.http.AsyncHttpClient.log;

public class FomonoTrailerActivity extends YouTubeBaseActivity {

    private final static String TAG="Youtube Player";
    YouTubePlayerView youtubeplayer;
    public YouTubePlayer.OnInitializedListener onInitializedListener;
    public static final String YOUTUBE_API_KEY = "AIzaSyCyJqKKIwx2mYQxR9TmdYykRqKrXvjTXOE";

    final String MovieTrailerUrl_header = "https://api.themoviedb.org/3/movie/";
    final String MovieTrailerUrl_API_KEY = "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fomono_trailer);

        youtubeplayer = (YouTubePlayerView)findViewById(R.id.YoutubePlayerId);

        String MovieTrailerUrl = MovieTrailerUrl_header + getIntent().getLongExtra(getResources().getString(R.string.MovieId), 0) + MovieTrailerUrl_API_KEY;
        Request videoRequest = new Request.Builder().url(MovieTrailerUrl).build();

        Log.d(TAG, "Video request is "+videoRequest);
        client.newCall(videoRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Fetching video failed\n");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject MainJsonObject = new JSONObject(result);
                    JSONArray JsonResultsArray = MainJsonObject.getJSONArray("results");
                    JSONObject JsonResultsFirstEntry = JsonResultsArray.getJSONObject(0);

                    final String TrailerVideoKey = JsonResultsFirstEntry.getString("key");

                    FomonoTrailerActivity.this.runOnUiThread(() -> {
                        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                                log.d("DEBUG", "Initialization successful");
                                youTubePlayer.loadVideo(TrailerVideoKey);
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                                log.d("DEBUG", "Initialization Failed");
                            }
                        };
                        youtubeplayer.initialize(YOUTUBE_API_KEY,onInitializedListener);
                    });

                    log.d("DEBUG", "PASSED, the key is" + TrailerVideoKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}

package com.fomono.fomono.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fomono.fomono.R;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.models.movies.MovieResponse;
import com.fomono.fomono.supportclasses.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saranu on 4/6/17.
 */

public class MovieFragment extends MainListFragment {


    private final static String TAG = "Movie Fragment";
    public int moviePage = 1;
    private String sortParameter = null;
    private String searchParameter = null;
    private int buttonSelected = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        populateMovies(moviePage++, null, searchParameter);

        rvList.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) rvList.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateMovies(moviePage++, sortParameter, searchParameter);
            }
        });
        return view;
    }

    public void searchMovieList(String query) {
        clear();
        moviePage = 1;
        searchParameter = query;
        populateMovies(moviePage++, sortParameter, searchParameter);
    }

    public void refreshMovieList(String sortParam) {
        sortParameter = sortParam;
        clear();
        moviePage = 1;
        populateMovies(moviePage++, sortParameter, searchParameter);
    }


    public void populateMovies(int page, String sortParameter, String strQuery) {

        if (internetAlertDialogue.checkForInternet()) {
            smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
            getMoviesNowPlaying(strQuery, page, sortParameter);

        }
    }

    public void getMoviesNowPlaying(String stringQuery, int page, String sortParam) {

        Map<String, String> data = new HashMap<>();
        data.put("api_key", getResources().getString(R.string.movieDB_api_key));
        data.put("page", String.valueOf(page));

        if(stringQuery != null) {
            data.put("query", stringQuery);
            Call<MovieResponse> callMovie = movieDBClientRetrofit.MovieDBRetrofitClientFactory()
                    .getSearchMoviesFromServer(data);

            Log.d(TAG, "Movie URL String is " + callMovie.request().url());
            callMovie.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    try {
                        ArrayList<Movie> movies = response.body().getResults();
                        if (movies == null || movies.isEmpty()) {
                            Log.d(TAG, "No movies fetched!!");
                        } else {
                            Movie.saveOrUpdateFromList(movies);
                            fomonoEvents.addAll(movies);
                            fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                            Log.d(TAG, "response is, with page = " + page + "is " + movies.get(0));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    smoothProgressBar.setVisibility(ProgressBar.GONE);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.d(TAG, "REQUEST Failed " + t.getMessage());
                    smoothProgressBar.setVisibility(ProgressBar.GONE);
                }
            });

        } else if((sortParam != null) && !(sortParam.equals(""))) {
            if (sortParam.equals("playing_now")) {
                Call<MovieResponse> callMovie = movieDBClientRetrofit.MovieDBRetrofitClientFactory()
                        .getNowPlayingMoviesFromServer(data);

                Log.d(TAG, "Movie URL String is " + callMovie.request().url());
                callMovie.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        try {
                            ArrayList<Movie> movies = response.body().getResults();
                            if (movies == null || movies.isEmpty()) {
                                Log.d(TAG, "No movies fetched!!");
                            } else {
                                Movie.saveOrUpdateFromList(movies);
                                fomonoEvents.addAll(movies);
                                fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                                Log.d(TAG, "response is, with page = " + page + "is " + response.body().getResults().get(0));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        smoothProgressBar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d(TAG, "REQUEST Failed " + t.getMessage());
                        smoothProgressBar.setVisibility(ProgressBar.GONE);
                    }
                });

            } else if (sortParam.equals("popular")) {
                Call<MovieResponse> callMovie = movieDBClientRetrofit.MovieDBRetrofitClientFactory()
                        .getPopularMoviesFromServer(data);

                Log.d(TAG, "Movie URL String is " + callMovie.request().url());
                callMovie.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        try {
                            ArrayList<Movie> movies = response.body().getResults();
                            if (movies == null || movies.isEmpty()) {
                                Log.d(TAG, "No movies fetched!!");
                            } else {
                                Movie.saveOrUpdateFromList(movies);
                                fomonoEvents.addAll(movies);
                                fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                                Log.d(TAG, "response is, with page = " + page + "is " + response.body().getResults().get(0));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        smoothProgressBar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d(TAG, "REQUEST Failed " + t.getMessage());
                        smoothProgressBar.setVisibility(ProgressBar.GONE);
                    }
                });

            } else if (sortParam.equals("top_rated")) {
                Call<MovieResponse> callMovie = movieDBClientRetrofit.MovieDBRetrofitClientFactory()
                        .getTopRatedMoviesFromServer(data);

                Log.d(TAG, "Movie URL String is " + callMovie.request().url());
                callMovie.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        try {
                            ArrayList<Movie> movies = response.body().getResults();
                            if (movies == null || movies.isEmpty()) {
                                Log.d(TAG, "No movies fetched!!");
                            } else {
                                Movie.saveOrUpdateFromList(movies);
                                fomonoEvents.addAll(movies);
                                fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                                Log.d(TAG, "response is, with page = " + page + "is " + response.body().getResults().get(0));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        smoothProgressBar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d(TAG, "REQUEST Failed " + t.getMessage());
                        smoothProgressBar.setVisibility(ProgressBar.GONE);
                    }
                });

            } else {
                Call<MovieResponse> callMovie = movieDBClientRetrofit.MovieDBRetrofitClientFactory()
                        .getNowPlayingMoviesFromServer(data);

                Log.d(TAG, "Movie URL String is " + callMovie.request().url());
                callMovie.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        try {
                            ArrayList<Movie> movies = response.body().getResults();
                            if (movies == null || movies.isEmpty()) {
                                Log.d(TAG, "No movies fetched!!");
                            } else {
                                Movie.saveOrUpdateFromList(movies);
                                fomonoEvents.addAll(movies);
                                fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                                Log.d(TAG, "response is, with page = " + page + "is " + response.body().getResults().get(0));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        smoothProgressBar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d(TAG, "REQUEST Failed " + t.getMessage());
                        smoothProgressBar.setVisibility(ProgressBar.GONE);
                    }
                });
            }
        } else {
            Call<MovieResponse> callMovie = movieDBClientRetrofit.MovieDBRetrofitClientFactory()
                    .getNowPlayingMoviesFromServer(data);

            Log.d(TAG, "Movie URL String is " + callMovie.request().url());
            callMovie.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    try {
                        ArrayList<Movie> movies = response.body().getResults();
                        if (movies == null || movies.isEmpty()) {
                            Log.d(TAG, "No movies fetched!!");
                        } else {
                            Movie.saveOrUpdateFromList(movies);
                            fomonoEvents.addAll(movies);
                            fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                            Log.d(TAG, "response is, with page = " + page + "is " + response.body().getResults().get(0));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    smoothProgressBar.setVisibility(ProgressBar.GONE);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.d(TAG, "REQUEST Failed " + t.getMessage());
                    smoothProgressBar.setVisibility(ProgressBar.GONE);
                }
            });
        }
    }

    @Override
    public void clearSearch() {
        if (searchParameter == null) {
            return;
        }
        clear();
        moviePage = 1;
        searchParameter = null;
        populateMovies(moviePage++, null, searchParameter);
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }
}

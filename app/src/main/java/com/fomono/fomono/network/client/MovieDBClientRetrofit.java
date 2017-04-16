package com.fomono.fomono.network.client;

import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.models.movies.MovieResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


/**
 * Created by Saranu on 4/8/17.
 */

public class MovieDBClientRetrofit {
    public static final String API_MDB_BASE_URL = "https://api.themoviedb.org/";
    public static final String API_KEY = "d0af6e07311c078f8b5c7419e1e0e3c3";
    MovieDBClientRetrofit.MovieDBService MDBService;
    final static String TAG = "MovieDBClient";

    private static MovieDBClientRetrofit instance;

    public MovieDBClientRetrofit.MovieDBService MovieDBRetrofitClientFactory() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_MDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MDBService = retrofit.create(MovieDBClientRetrofit.MovieDBService.class);

        return MDBService;

    }

    public static MovieDBClientRetrofit getInstance() {
        if (instance == null) {
            instance = new MovieDBClientRetrofit();
        }
        return instance;
    }

    public interface MovieDBService {

        @GET("/3/movie/now_playing")
        Call<MovieResponse> getNowPlayingMoviesFromServer(@QueryMap Map<String, String> options);

        //@Query("token") String api_key,
        // @Query("q") String search_query, @Query("sort_by") String sort_string

        @GET("/3/movie/latest")
        Call<MovieResponse> getLatesMoviesFromServer(@QueryMap Map<String, String> options);
        //@Query("token") String api_key, @Query("q") String search_string

        @GET("/3/movie/popular")
        Call<MovieResponse> getPopularMoviesFromServer(@QueryMap Map<String, String> options);
        //@Query("token") String api_key, @Query("q") String search_string


        @GET("/3/movie/top_rated")
        Call<MovieResponse> getTopRatedMoviesFromServer(@QueryMap Map<String, String> options);
        //@Query("token") String api_key, @Query("q") String search_string

        @GET("/3/movie/upcoming")
        Call<MovieResponse> getUpcomingMoviesFromServer(@QueryMap Map<String, String> options);

        @GET("/3/movie/{movie_id}")
        Call<Movie> getMovieById(@Path("movie_id") String id, @QueryMap Map<String, String> options);
    }


    private String getApiUrl(String relativeUrl) {
        return API_MDB_BASE_URL + relativeUrl;
    }

/*
    public void callRetrofitGetMoviesNowPlaying(Context context, String stringQuery) {

        //eventBriteClientRetrofit = EventBriteClientRetrofit.getInstance();

        //TODO: Incorrect string query. Write a method to generate a string
        //Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer(stringQuery);

        Call<MovieResponse> callVenue = MovieDBRetrofitClientFactory().getNowPlayingMoviesFromServer(API_KEY);


        callVenue.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();

                if (movieResponse == null) {
                    Log.d(TAG, "NO MATCH ");

                } else {
                    movies = (ArrayList) movieResponse.getResults();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }


    public void callRetrofitGetLatestMovies(Context context, String stringQuery) {

        //eventBriteClientRetrofit = EventBriteClientRetrofit.getInstance();

        //TODO: Incorrect string query. Write a method to generate a string
        //Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer(stringQuery);

        Call<MovieResponse> callVenue = MovieDBRetrofitClientFactory().getLatesMoviesFromServer(API_KEY);


        callVenue.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();

                if (movieResponse == null) {
                    Log.d(TAG, "NO MATCH ");

                } else {
                    movies = (ArrayList) movieResponse.getResults();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }

    public void callRetrofitGetPopularMovies(Context context, String stringQuery) {

        //eventBriteClientRetrofit = EventBriteClientRetrofit.getInstance();

        //TODO: Incorrect string query. Write a method to generate a string
        //Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer(stringQuery);

        Call<MovieResponse> callVenue = MovieDBRetrofitClientFactory().getPopularMoviesFromServer(API_KEY);


        callVenue.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();

                if (movieResponse == null) {
                    Log.d(TAG, "NO MATCH ");

                } else {
                    movies = (ArrayList) movieResponse.getResults();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }

    public void callRetrofitGetTopRatedMovies(Context context, String stringQuery) {

        //eventBriteClientRetrofit = EventBriteClientRetrofit.getInstance();

        //TODO: Incorrect string query. Write a method to generate a string
        //Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer(stringQuery);

        Call<MovieResponse> callVenue = MovieDBRetrofitClientFactory().getTopRatedMoviesFromServer(API_KEY);


        callVenue.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();

                if (movieResponse == null) {
                    Log.d(TAG, "NO MATCH ");

                } else {
                    movies = (ArrayList) movieResponse.getResults();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }

    public void callRetrofitGetUpcomingMovies(Context context, String stringQuery) {

        //eventBriteClientRetrofit = EventBriteClientRetrofit.getInstance();

        //TODO: Incorrect string query. Write a method to generate a string
        //Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer(stringQuery);

        Call<MovieResponse> callVenue = MovieDBRetrofitClientFactory().getUpcomingMoviesFromServer(API_KEY);


        callVenue.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();

                if (movieResponse == null) {
                    Log.d(TAG, "NO MATCH ");

                } else {
                    movies = (ArrayList) movieResponse.getResults();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }
*/

}


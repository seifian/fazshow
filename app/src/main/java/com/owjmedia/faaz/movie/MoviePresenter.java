package com.owjmedia.faaz.movie;

import com.owjmedia.faaz.general.networking.ApiClient;
import com.owjmedia.faaz.general.networking.ApiInterface;
import com.owjmedia.faaz.movie.model.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seifian on 11/26/17.
 */

public class MoviePresenter implements MovieContract.Presenter {

    public MoviePresenter(MovieContract.View movieView) {
        mMovieView = movieView;
    }


    @Override
    public void getMovie(String token) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MovieResponse>> call = apiService.getMovie(token);

        mMovieView.setLoadingIndicator(true);
        call.enqueue(new Callback<List<MovieResponse>>() {
            @Override
            public void onResponse(Call<List<MovieResponse>> call, Response<List<MovieResponse>> response) {
                mMovieView.setLoadingIndicator(false);
                if (response.code() == 200)
                    mMovieView.showMovie(response.body());
                else
                    mMovieView.showMessage(response.message());
            }

            @Override
            public void onFailure(Call<List<MovieResponse>> call, Throwable t) {
                mMovieView.setLoadingIndicator(false);
            }
        });
    }

    private MovieContract.View mMovieView;
}

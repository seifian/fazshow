package com.owjmedia.faaz.newsdetail;

import com.owjmedia.faaz.newsdetail.model.NewsDetailResponse;
import com.owjmedia.faaz.general.networking.ApiClient;
import com.owjmedia.faaz.general.networking.ApiInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seifian on 11/12/17.
 */

public class NewsDetailPresenter implements NewsDetailContract.Presenter {

    public NewsDetailPresenter(NewsDetailContract.View newsDetailView) {
        this.mNewsDetailView = newsDetailView;
    }


    @Override
    public void getNewsDetail(String accessToken, String newsId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsDetailResponse> call = apiService.getNewsDetail(accessToken, newsId);

        mNewsDetailView.setLoadingIndicator(true);
        call.enqueue(new Callback<NewsDetailResponse>() {
            @Override
            public void onResponse(Call<NewsDetailResponse> call, Response<NewsDetailResponse> response) {
                mNewsDetailView.showNewsDetail(response.body());
                mNewsDetailView.setLoadingIndicator(false);
            }

            @Override
            public void onFailure(Call<NewsDetailResponse> call, Throwable t) {
                mNewsDetailView.setLoadingIndicator(false);
            }
        });

    }

    @Override
    public void like(String accessToken, String newsId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.like(accessToken, newsId);

        mNewsDetailView.setLoadingIndicator(true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mNewsDetailView.showLikedSuccessfully();
                mNewsDetailView.setLoadingIndicator(false);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mNewsDetailView.setLoadingIndicator(false);
            }
        });

    }

    private NewsDetailContract.View mNewsDetailView;
}

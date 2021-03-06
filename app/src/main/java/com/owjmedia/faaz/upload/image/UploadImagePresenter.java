package com.owjmedia.faaz.upload.image;

import com.owjmedia.faaz.general.networking.Injector;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by salman on 1/22/18.
 */

public class UploadImagePresenter implements UploadImageContract.Presenter {

    public UploadImagePresenter(UploadImageContract.View view) {
        this.view = view;
    }

    @Override
    public void uploadImage(MultipartBody.Part part) {
        Call<ResponseBody> call = Injector.provideApiService().uploadImage(part);

        view.setLoadingIndicator(true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                view.setLoadingIndicator(false);
                if (response.isSuccessful())
                    view.onImageUploaded();
                else
                    view.showMessage(Injector.parseError(response).getDetail());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.setLoadingIndicator(false);
                view.showConnectionError();
            }
        });
    }

    private UploadImageContract.View view;


}

package com.owjmedia.faaz.upload.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.owjmedia.faaz.R;
import com.owjmedia.faaz.general.ConnectionErrorDialog;
import com.owjmedia.faaz.general.networking.ProgressRequestBody;
import com.owjmedia.faaz.general.utils.ActivityUtils;
import com.owjmedia.faaz.general.utils.CustomWidgets.TypefaceTextView;
import com.owjmedia.faaz.general.utils.ProgressDialog;
import com.owjmedia.faaz.upload.OnUploaded;
import com.owjmedia.faaz.upload.video.UploadVideoContract;
import com.owjmedia.faaz.upload.video.UploadVideoPresenter;

import java.io.File;
import java.util.Observable;

import okhttp3.MultipartBody;


public class UploadImageFragment extends DialogFragment implements UploadImageContract.View, ProgressRequestBody.UploadCallbacks {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnUploaded) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnItemClickedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.upload_image_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityUtils.verifyStoragePermissions(getActivity());
        mProgressDialog = new ProgressDialog(getActivity());
        btnChooseFile = view.findViewById(R.id.btn_choose_image_file);
        btnUploadImage = view.findViewById(R.id.btn_upload_image);
        imgChosen = view.findViewById(R.id.img_chosen);
        prgUpload = view.findViewById(R.id.prg_upload);
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGallery();
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagePath != null)
                    upload(imagePath);

            }
        });
    }

    public void showGallery() {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/jpg");
        galleryIntent.setAction(Intent.ACTION_PICK);
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose image");
        startActivityForResult(chooserIntent, 100);
    }


    private void upload(String imagePath) {
        File file = new File(imagePath);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
        new UploadImagePresenter(this).uploadImage(filePart);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            if (data == null) {
                ActivityUtils.showToast(getActivity(), "Unable to pick image");
                return;
            }
            Uri imageUri = data.getData();
            imgChosen.setImageURI(imageUri);
            imagePath = ActivityUtils.getRealPathFromURI(imageUri, getActivity().getApplicationContext());
            btnChooseFile.setText(getString(R.string.change_image_file));
            btnUploadImage.setVisibility(View.VISIBLE);
            prgUpload.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        if (active)
            mProgressDialog.show();
        else
            mProgressDialog.cancel();
    }

    @Override
    public void showMessage(String message) {
        ActivityUtils.showToast(getActivity(), message);
        dismiss();
    }

    @Override
    public void showConnectionError() {
        new ConnectionErrorDialog().show(getFragmentManager(), null);
        dismiss();
    }

    @Override
    public void onImageUploaded() {
        mListener.onImageUploaded();
        dismiss();
    }

    @Override
    public void onProgressUpdate(int percentage) {
        prgUpload.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void finish() {
        prgUpload.setProgress(100);
    }

    OnUploaded mListener;
    ProgressDialog mProgressDialog;
    private String imagePath;
    ImageView imgChosen;
    TypefaceTextView btnChooseFile;
    TypefaceTextView btnUploadImage;
    ProgressBar prgUpload;


}

package com.owjmedia.faaz.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by salman on 11/9/17.
 */

public class UpdateProfileRequest {

    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("year_of_birth")
    @Expose
    private int yearOfBirth;

    public UpdateProfileRequest(int gender, String city, int yearOfBirth) {
        this.gender = gender;
        this.city = city;
        this.yearOfBirth = yearOfBirth;
    }
}

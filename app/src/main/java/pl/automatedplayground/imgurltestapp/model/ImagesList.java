package pl.automatedplayground.imgurltestapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adrian on 15.01.16.
 */
public class ImagesList {
    @Expose
    @SerializedName("data")
    private List<Image> data;

    public List<Image> getData() {
        return data;
    }
}

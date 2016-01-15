package pl.automatedplayground.imgurltestapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by adrian on 15.01.16.
 */
public class Image implements Serializable {
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("datetime")
    private Long datetime;
    @Expose
    @SerializedName("nsfw")
    private Boolean nsfw;
    @Expose
    @SerializedName("favourite")
    private Boolean favourite;
    @Expose
    @SerializedName("link")
    private String link;

    public String getId() {
        return id;
    }

    public Image setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Image setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Image setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getDatetime() {
        return datetime;
    }

    public Image setDatetime(Long datetime) {
        this.datetime = datetime;
        return this;
    }

    public Boolean getNsfw() {
        return nsfw;
    }

    public Image setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public Image setFavourite(Boolean favourite) {
        this.favourite = favourite;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Image setLink(String link) {
        this.link = link;
        return this;
    }

    public String getLinkForBiggerImage() {
        return link.replace(".jpg","l.jpg");
    }
}

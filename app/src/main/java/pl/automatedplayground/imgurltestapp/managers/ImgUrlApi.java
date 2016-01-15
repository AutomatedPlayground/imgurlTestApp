package pl.automatedplayground.imgurltestapp.managers;

import pl.automatedplayground.imgurltestapp.model.Album;
import pl.automatedplayground.imgurltestapp.model.ImagesList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by adrian on 15.01.16.
 */
public interface ImgUrlApi {
    // below one requires some more work ;)
    @GET("gallery/album/{id}")
    Call<Album> getAlbum(@Path("id")String albumId);

    @GET("gallery/hot/viral/0.json")
    Call<ImagesList> getViralImages();

}

package pl.automatedplayground.imgurltestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import pl.automatedplayground.imgurltestapp.managers.NetworkManager;
import pl.automatedplayground.imgurltestapp.model.Image;

public class ImagesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_list);
        NetworkManager.get().getImages("KSz6k", new NetworkManager.Callback<List<Image>>() {
            @Override
            public void onCallback(List<Image> object) {
                Toast.makeText(ImagesListActivity.this,"Got response!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String cause) {
                Toast.makeText(ImagesListActivity.this,cause,Toast.LENGTH_LONG).show();
            }
        });
    }
}

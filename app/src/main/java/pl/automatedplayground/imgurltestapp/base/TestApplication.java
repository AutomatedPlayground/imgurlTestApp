package pl.automatedplayground.imgurltestapp.base;

import android.app.Application;

import pl.automatedplayground.imgurltestapp.managers.NetworkManager;

/**
 * Created by adrian on 15.01.16.
 */
public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // let's initialize network manager
        NetworkManager.init();
    }
}

package pl.automatedplayground.imgurltestapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import pl.automatedplayground.imgurltestapp.model.Image;

public class ImageDetails extends AppCompatActivity {

    public static final String EXTRA_IMAGE = "imageextra";
    private Image mData;
    @Bind(R.id.main_image)
    ImageView mImage;
    @Bind(R.id.textView2)
    public TextView mTitle;
    @Bind(R.id.textView)
    public TextView mDate;
    @Bind(R.id.textView_content)
    public TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_image_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mData = (Image) getIntent().getSerializableExtra(EXTRA_IMAGE);
        // specify an adapter
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        // get loaded images desired size
        int mRowWidth = metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;
        Picasso.with(getApplicationContext())
                .load(mData.getLinkForBiggerImage())
                .placeholder(R.drawable.loading)
                .error(android.R.drawable.ic_delete)
                .resize(mRowWidth, mRowWidth)
                .centerInside()
                .into(mImage);
        mDate.setText(DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date(mData.getDatetime() * 1000)));
        mTitle.setText(mData.getTitle());
        mContent.setText(mData.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imagedetailsmenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem like = menu.findItem(R.id.action_like);
        if (mData.getFavourite() != null && mData.getFavourite()) {
            like.setTitle("Unlike");
        } else
            like.setTitle("Like");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_like) {
            if (mData.getFavourite() != null && mData.getFavourite())
                mData.setFavourite(false);
            else
                mData.setFavourite(true);
            Intent dataIntent = new Intent();
            dataIntent.putExtra(EXTRA_IMAGE, mData);
            setResult(Activity.RESULT_OK, dataIntent);
            invalidateOptionsMenu();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    /**
     * Create intent for showing image details
     *
     * @param ctx
     * @param data
     * @return
     */
    public static Intent createIntent(Context ctx, Image data) {
        Intent output = new Intent(ctx, ImageDetails.class);
        output.putExtra(EXTRA_IMAGE, data);
        return output;
    }
}

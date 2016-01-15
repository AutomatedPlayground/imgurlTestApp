package pl.automatedplayground.imgurltestapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.Icicle;
import pl.automatedplayground.imgurltestapp.base.DataProviderInterface;
import pl.automatedplayground.imgurltestapp.managers.NetworkManager;
import pl.automatedplayground.imgurltestapp.model.Image;

public class ImagesListActivity extends AppCompatActivity implements DataProviderInterface {

    private static final int REQUESTCODE_SHOWDETAILS = 12342;
    @Bind(R.id.imageslist)
    RecyclerView mImagesList;
    @Icicle
    protected ArrayList<Image> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_images_list);
        ButterKnife.bind(this);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mImagesList.setLayoutManager(mLayoutManager);
        if (dataset == null)
            NetworkManager.get().getImages(new NetworkManager.Callback<ArrayList<Image>>() {
                @Override
                public void onCallback(final ArrayList<Image> object) {
                    dataset = object;
                    mImagesList.post(new Runnable() {
                        @Override
                        public void run() {
                            reloadViewWithData();
                        }
                    });
                }

                @Override
                public void onError(String cause) {
                    Toast.makeText(ImagesListActivity.this, cause, Toast.LENGTH_LONG).show();
                    // TODO: add retry dialog
                }
            });
        else
            reloadViewWithData();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void reloadViewWithData() {
        // specify an adapter
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        // get loaded images desired size
        int width = metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;
        ImagesAdapter mAdapter = new ImagesAdapter(this, new ImageClickListener() {
            @Override
            public void onItemClicked(Image data) {
//                Toast.makeText(ImagesListActivity.this, "Clicked on " + data.getId(), Toast.LENGTH_SHORT).show();
                startActivityForResult(ImageDetails.createIntent(ImagesListActivity.this, data), REQUESTCODE_SHOWDETAILS);
            }
        }, width);
        mImagesList.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_SHOWDETAILS && resultCode == Activity.RESULT_OK) {
            Image img = (Image) data.getSerializableExtra(ImageDetails.EXTRA_IMAGE);
            for (int i = 0; i < dataset.size(); i++)
                if (dataset.get(i).getId().equalsIgnoreCase(img.getId()))
                    dataset.get(i).setFavourite(img.getFavourite());
            mImagesList.getAdapter().notifyDataSetChanged();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public ArrayList<Image> getData() {
        return dataset;
    }

    /**
     * View holder for list
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView2)
        public TextView mTitle;
        @Bind(R.id.textView)
        public TextView mDate;
        @Bind(R.id.imageView)
        public ImageView mImageView;
        private int mDataPosition;
        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            ButterKnife.bind(this, v);
        }

        public void setDataPosition(int position) {
            mDataPosition = position;
        }

        public int getDataPosition() {
            return mDataPosition;
        }
    }

    /**
     * Interface for click recognition
     */
    private interface ImageClickListener {
        void onItemClicked(Image data);
    }

    /**
     * Adapter for images
     */
    public class ImagesAdapter extends RecyclerView.Adapter<ViewHolder> {
        protected DataProviderInterface mData;
        private final ImageClickListener mClickListener;
        @Icicle
        protected int mRowWidth;

        public ImagesAdapter(DataProviderInterface mData, ImageClickListener clickListener, int rowWidth) {
            this.mData = mData;
            this.mClickListener = clickListener;
            this.mRowWidth = rowWidth;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_imagelist, parent, false);
            final ViewHolder vh = new ViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClicked(mData.getData().get(vh.getDataPosition()));
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Image data = mData.getData().get(position);
            if (data.getFavourite() != null && data.getFavourite()) {

            }
            holder.setDataPosition(position);
            holder.mDate.setText(DateFormat.getDateTimeInstance(
                    DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date(data.getDatetime() * 1000)));
            holder.mTitle.setText(data.getTitle());
            Picasso.with(getApplicationContext())
                    .load(data.getLink())
                    .placeholder(R.drawable.loading)
                    .error(android.R.drawable.ic_delete)
                    .resize(mRowWidth, mRowWidth)
                    .centerCrop()
                    .into(holder.mImageView);
            // let's make sure rows can be differentiated
            if (position % 2 == 0) {
                if (data.getFavourite() != null && data.getFavourite()) {
                    holder.mView.setBackgroundColor(Color.YELLOW);
                } else
                    holder.mView.setBackgroundColor(Color.LTGRAY);
            } else {
                if (data.getFavourite() != null && data.getFavourite()) {
                    holder.mView.setBackgroundColor(Color.YELLOW);
                } else
                    holder.mView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public int getItemCount() {
            if (mData.getData() == null)
                return 0;
            return mData.getData().size();
        }
    }
}

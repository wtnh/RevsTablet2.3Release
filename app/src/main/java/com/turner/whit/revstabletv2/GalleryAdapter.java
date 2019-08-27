package com.turner.whit.revstabletv2;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {


    private final GalleryItemClickListener galleryItemClickListener;
    private ArrayList<ImageModel> galleryList;
//    public ProgressBar progressBar;
//    CircularProgressDrawable circularProgress;

    GalleryAdapter(ArrayList<ImageModel> galleryList, GalleryItemClickListener galleryItemClickListener) {
        this.galleryList = galleryList;
        this.galleryItemClickListener = galleryItemClickListener;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item, parent, false));


    }


//    circularProgressDrawable.setIndeterminateDrawable(new)CircularProgressDrawable();

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position) {
        final ImageModel imageModel = galleryList.get(position);

//        final ProgressBar progressBar = (ProgressBar) ;

//        progressBar = (ProgressBar) ProgressBar.findViewById(R.id.prg);

//        circularProgress = new CircularProgressDrawable(this);

//        CircularProgressDrawable circularProgress = new CircularProgressDwarable();



        GlideApp.with(holder.galleryImageView.getContext())
                .load(imageModel.getUrl())

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
 //                       progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })

                .apply(new RequestOptions().centerCrop())

                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))

                .skipMemoryCache(true)

//removing thumbnail preview - unnecessary - just causes image to display twice

 //               .thumbnail(0.5f)

                .apply(new RequestOptions().placeholder(R.drawable.progress_animation))

//                .apply(new RequestOptions().dontAnimate())
                .apply(new RequestOptions().override(500,500))

                .into(holder.galleryImageView);

//        Animation animateSpin = (Animation) AnimationUtils.loadAnimation(Context  , R.anim.progress_animation);
//        GalleryViewHolder.startAnimation(animateSpin);


        // Set transition name same as the Image name
        ViewCompat.setTransitionName(holder.galleryImageView, imageModel.getName());

        holder.galleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryItemClickListener.onGalleryItemClickListener(holder.getAdapterPosition(), imageModel, holder.galleryImageView);
            }
        });

    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }


    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        private ImageView galleryImageView;

        GalleryViewHolder(View view) {
            super(view);
            galleryImageView = view.findViewById(R.id.galleryImage);
        }
    }

   //added this on 8/16/2019 to see if we can reduce memory load

    @Override
    public void onViewRecycled(@NonNull GalleryViewHolder holder)
    {
        super.onViewRecycled(holder);
        GlideApp.with(holder.itemView.getContext()).clear(holder.galleryImageView);
    }



}
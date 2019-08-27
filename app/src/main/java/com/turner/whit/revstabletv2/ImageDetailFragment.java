package com.turner.whit.revstabletv2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Objects;


public class ImageDetailFragment extends Fragment {

    private static final String EXTRA_IMAGE = "image_item";
    private static final String EXTRA_TRANSITION_NAME = "transition_name";

    public ImageDetailFragment() {
        // Required empty public constructor
    }

    static ImageDetailFragment newInstance(ImageModel image, String transitionName) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_IMAGE, image);
        args.putString(EXTRA_TRANSITION_NAME, transitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        final ImageModel image = getArguments().getParcelable(EXTRA_IMAGE);
        String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);

        final PhotoView imageView = view.findViewById(R.id.detail_image);
        imageView.setTransitionName(transitionName);

        assert image != null;
        GlideApp.with(Objects.requireNonNull(getActivity()))
                .asBitmap()
                .load(image.getUrl())
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        startPostponedEnterTransition();
                        imageView.setImageBitmap(resource);
                    }


                });

    }
}
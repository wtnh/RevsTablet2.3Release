package com.turner.whit.revstabletv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class RecyclerViewFragment extends Fragment implements GalleryItemClickListener {


    private static final String TAG = RecyclerViewFragment.class.getSimpleName();

    public RecyclerViewFragment() {
    }


    static Fragment newInstance() {
        return new RecyclerViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GalleryAdapter galleryAdapter = new GalleryAdapter(Utils.getData(),this);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(galleryAdapter);


    }

    @Override
    public void onGalleryItemClickListener(int position, ImageModel imageModel, ImageView imageView) {
        GalleryViewPagerFragment galleryViewPagerFragment = GalleryViewPagerFragment.newInstance(position, Utils.getData());

        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .addSharedElement(imageView, Objects.requireNonNull(ViewCompat.getTransitionName(imageView)))
                .addToBackStack(TAG)
                .replace(R.id.content, galleryViewPagerFragment)
                .commit();

    }


}
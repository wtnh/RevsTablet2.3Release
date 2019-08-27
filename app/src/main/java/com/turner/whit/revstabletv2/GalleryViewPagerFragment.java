package com.turner.whit.revstabletv2;

import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class GalleryViewPagerFragment extends Fragment {

    private static final String EXTRA_INITIAL_POS = "initial_pos";
    private static final String EXTRA_IMAGES = "images";

    public GalleryViewPagerFragment() {
    }

    static GalleryViewPagerFragment newInstance(int current, ArrayList<ImageModel> images) {
        GalleryViewPagerFragment fragment = new GalleryViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_INITIAL_POS, current);
        args.putParcelableArrayList(EXTRA_IMAGES, images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_view_pager, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        int currentItem = getArguments().getInt(EXTRA_INITIAL_POS);
        ArrayList<ImageModel> images = getArguments().getParcelableArrayList(EXTRA_IMAGES);
        GalleryPagerAdapter galleryPagerAdapter = new GalleryPagerAdapter(getChildFragmentManager(), images);


        //Added this from Subhrajyoti Sen
        HackyViewPager viewPager = view.findViewById(R.id.photo_view_pager);
        //ViewPager viewPager = (ViewPager) view.findViewById(R.id.animal_view_pager);
        viewPager.setAdapter(galleryPagerAdapter);
        viewPager.setCurrentItem(currentItem);
    }
}

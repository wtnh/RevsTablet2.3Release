package com.turner.whit.revstabletv2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<ImageModel> images;

    public GalleryPagerAdapter(FragmentManager fm, ArrayList<ImageModel> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        ImageModel image = images.get(position);
        return ImageDetailFragment.newInstance(image, image.getName());
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
package com.turner.whit.revstabletv2;

//Major portions of this code courtesy of Subhrajyoti Sen who created Glide Gallery
//See https://github.com/SubhrajyotiSen/GlideGallery for original code
//Also credits to Chris Banes who created Photo View
//See https://github.com/chrisbanes/PhotoView

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.Objects;


public class GalleryActivity extends AppCompatActivity {

    //added the following two Overrides to enable the back arrow to go back to the gallery view instead of the main screen

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Both navigation bar back press and title bar back press will trigger this method
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_gallery);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(
                    new ColorDrawable(Color.parseColor("#273134")));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            final Fragment fragment = RecyclerViewFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, fragment)
                    .commit();


        }


    // Added this on 8/16/2019 to see if we can use less memory in gallery by calling the garbage collector
    //there may be other cleanup tasks which could be added here

    @Override
    public void onDestroy() {
        super.onDestroy();

        Runtime.getRuntime().gc();
    }


}


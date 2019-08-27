package com.turner.whit.revstabletv2;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //fetch the json configuration file from github
    final String URL_GET_DATA = "https://raw.githubusercontent.com/wtnh/repo/master/revs_tablet_config1.json";
    //alternatively, use Box to get the configuration file - be sure to upload the latest version there
    //final String URL_GET_DATA = "https://revsinstitute.box.com/shared/static/fo3prq5yhchwq7kbbllrvulnf7hb0lae.json";
    public RecyclerView recyclerView;
    CarAdapter adapter;
    List<Car> carList;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setup the recycler view for the car list
        recyclerView = findViewById(R.id.recyclerView);

//        the following setting actually casues more thumbnail judder - removing it
//        recyclerView.setItemViewCacheSize(120);

        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Objects.requireNonNull(recyclerView.getLayoutManager()).setItemPrefetchEnabled(true);

        carList = new ArrayList<>();
        //initialize a new adapter; need to do this to get rid of NPE on initial start
        adapter = new CarAdapter(carList, getApplicationContext());
//        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);


        //Call loadCars - this will retrieve the JSON file from URL_GET_DATA location and build array
        loadCars();

        mDrawerList = findViewById(R.id.navList);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        //Set up the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#273134")));

        //Add items to the drawer menu
        addDrawerItems();
        setupDrawer();

        mDecorView = getWindow().getDecorView();
        DevicePolicyManager mDpm;

    }

    @Override
    protected void onStart() {
        super.onStart();
        enableKioskMode(true);

    }

    private void makeFullScreen(){

        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void enableKioskMode(boolean enabled) {

        DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // get this app package name
        ComponentName mDPM = new ComponentName(this, AdminReceiver.class);

        if (myDevicePolicyManager.isDeviceOwnerApp(this.getPackageName())) {
            // get this app package name
            String[] packages = {this.getPackageName()};
            // mDPM is the admin package, and allow the specified packages to lock task
            myDevicePolicyManager.setLockTaskPackages(mDPM, packages);
            startLockTask();
            makeFullScreen();
            //if the package has not been made device owner yet, skip startLockTask
            //this need to be done using adb
        } else {
//            Toast.makeText(getApplicationContext(),"Not owner", Toast.LENGTH_LONG).show();
        }
    }

    //this is called when we want to delete the cache
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            //ignore if we can't find Cachedir
        }
    }

    //this is called from deleteCache and iterates though the top level directory and all its children
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    //Set up the menu
    private void addDrawerItems() {
        final String[] str = {"People", "Places", "Technology", "Training Materials", "Emergency Procedures", "Volunteer Handbook", "Revs Website", "Volgistics",
        "Tappet Clatter Archives", "Gallery Map", "Cars Not on Display", "Provide Feedback", "About"};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, str);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                String url = null;
                switch (position) {
                    case 0:
                        //People:
                        url = "https://revsinstitute.box.com/s/7rewzpqscg4kmhi8yu7qmcnslvb7a9u1";
                    break;
                    case 1:
                        //Places:
                        url = "https://revsinstitute.box.com/s/f2c1hgr0hci953sd6vbivulxgwiqu9n2";
                    break;
                    case 2:
                        //Technology:
                        url = "https://revsinstitute.box.com/s/ehm6aplnr79lai45wh1asty2g1mtla04";
                    break;
                    case 3:
                        //Training materials:
                        url = "https://revsinstitute.box.com/s/qmiqh458ahkqgp7ivg2x6c1qz9mb6cje";
                    break;
                    case 4:
                        //Emergency procedures
                        url = "https://revsinstitute.box.com/s/lie2dhmpfgd4d36p5qygv4rlcvwpvfzt";
                    break;
                    case 5:
                        //Volunteer handbook
                        url = "https://revsinstitute.box.com/s/eahdfyeee12c6c4vep3svh5i71c5nlme";
                    break;
                    case 6:
                        //Revs website
                        url = "https://revsinstitute.org";
                    break;
                    case 7:
                        //Volgistics
                        url = "https://www.volgistics.com/ex2/vicnet.dll?FROM=110030";
                    break;
                    case 8:
                        //Tappet clatter archives
                        url = "https://revsinstitute.box.com/s/h5pttf8nmif3stwbu1yyvjar3hba5grq";
                    break;
                    case 9:
                        //Gallery Map
                        url = "https://revsinstitute.box.com/s/5cl0ysmd7qvenzf2ujx0oryddwrwn53k";
                    break;
                    case 10:
                        //Cars Not on Display link
                        url = "https://revsinstitute.org/cars-not-on-display/";
                    break;
                    case 11:
                        //Provide Feedback - point to survey
                        url = "https://docs.google.com/forms/d/e/1FAIpQLSen3sbYW2d3ArXgmGFtvdMInKlPXGRNXcsVDFsg4EmlqqpaAA/viewform?usp=sf_link";
                    break;
                    case 12:
                        //About Dialog
                        url = "file:///android_asset/About CarPad.html";


                    break;
 /*                   case 13:

                        //Aug 25, 2019 - removed Reload Car Data from the list so this is not used

                        //Reload Car Data - clears the carList array than calls loadCars - presents alert dialog
                        //may remove this in production version since power off/power on will do the same thing

                        carList.clear();
                        loadCars();
                        AlertDialog.Builder builder = new AlertDialog.Builder(mDrawerList.getContext());
                        builder.setTitle("Please wait while car data re-loads");
                        builder.setMessage("Wait a few seconds and return to the car list");
                        builder.setCancelable(true);
                        final AlertDialog closedialog= builder.create();
                        closedialog.show();
                        final Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            public void run() {
                                closedialog.dismiss();
                                timer2.cancel(); //this will cancel the timer of the system
                            }
                        }, 3000); // the timer will count 3 seconds....

                        return;*/
                }
                //lauch the webactivity with the urlstring selected
                bundle.putString("urlString", url);
                Intent intent = new Intent(MainActivity.this, WebActivity5.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            //Called when a drawer has settled in a completely open state.

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            //Called when a drawer has settled in a completely closed state.

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Objects.requireNonNull(getSupportActionBar()).setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Set up a drop down spinner in actionbar for sorting the car list - sort values in res/values/array.xml
        // Spinner text color is set in res/layout/spinner_item.xml
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int index = parent.getSelectedItemPosition();
                String selectedItem = parent.getItemAtPosition(pos).toString();
                switch (selectedItem)
                {
                    case "Sort Cars by Station 1>10":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                break;
                    case "Sort Cars by Station 10>1":
                        sortArrayListStationReverse();
                        recyclerView.scrollToPosition(0);
                break;
                    case "Sort Cars by Year Old>New":
                        sortArrayListYear();
                        recyclerView.scrollToPosition(0);
                break;
                    case "Sort Cars by Year New>Old":
                        sortArrayListYearReverse();
                        recyclerView.scrollToPosition(0);
                break;
                    case "Sort Cars by Make A-Z":
                        sortArrayListMake();
                        recyclerView.scrollToPosition(0);
                break;
                    case "Station 1":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                break;
                    case "Station 2":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                        recyclerView.scrollToPosition(6);
                break;
                    case "Station 3":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                        recyclerView.scrollToPosition(38);
                break;
                    case "Station 4":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                        recyclerView.scrollToPosition(51);
                 break;
                    case "Station 5":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                        recyclerView.scrollToPosition(55);
                break;
                    case "Station 6":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                        recyclerView.scrollToPosition(65);
                break;
                    case "Station 7":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                        recyclerView.scrollToPosition(75);
                break;
                    case "Station 8":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                        recyclerView.scrollToPosition(87);
                 break;
                    case "Station 9":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                        recyclerView.scrollToPosition(106);
                 break;
                    case "Station 10":
                        sortArrayListStation();
                        recyclerView.scrollToPosition(0);
                        recyclerView.scrollToPosition(116);
                 default:
                        sortArrayListStation();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return true;
    }

    //Resort the carList according to the choice in the spinner drop-down
    private void sortArrayListYear() {
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                return o1.getYear().compareTo(o2.getYear());
            }
        });
        adapter.notifyDataSetChanged();
        Runtime.getRuntime().gc();
    }

    private void sortArrayListYearReverse() {
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                return o2.getYear().compareTo(o1.getYear());
            }
        });
        adapter.notifyDataSetChanged();
        Runtime.getRuntime().gc();
    }

    private void sortArrayListMake() {
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                return o1.getMake().compareTo(o2.getMake());
            }
        });
        adapter.notifyDataSetChanged();
        Runtime.getRuntime().gc();
    }

    private void sortArrayListStation() {
        //added this to make sure cars get sorted chronologically within stations
        sortArrayListGallerySeq();
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                return o1.getStation().compareTo(o2.getStation());
            }
        });

        adapter.notifyDataSetChanged();
        Runtime.getRuntime().gc();
    }

    private void sortArrayListStationReverse() {
        //added this to make sure cars get sorted chronologically within stations
        sortArrayListGallerySeq();
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                return o2.getStation().compareTo(o1.getStation());
            }
        });
        adapter.notifyDataSetChanged();
        Runtime.getRuntime().gc();
    }
    //added gallery_seq sort function - change station sort to include this
    private void sortArrayListGallerySeq() {
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                return o1.getGallery_seq().compareTo(o2.getGallery_seq());
            }
        });
        adapter.notifyDataSetChanged();
        Runtime.getRuntime().gc();

    }

    //This is where we build up the metadata list for all the cars
    private void loadCars() {

        deleteCache(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                Car car = new Car(
                                        obj.getString("id"),
                                        obj.getString("make"),
                                        obj.getString("model"),
                                        obj.getString("year"),
                                        obj.getString("thumburl"),
                                        obj.getString("descr"),
                                        obj.getString("country"),
                                        obj.getString("gallery"),
                                        obj.getInt("gallery_seq"),
                                        obj.getInt("station"),
                                        obj.getString("fact1"),
                                        obj.getString("fact2"),
                                        obj.getString("fact3"),
                                        obj.getString("fact4"),
                                        obj.getString("fact5"),
                                        obj.getString("fact6"),
                                        obj.getString("fact7"),
                                        obj.getString("fact8"),
                                        obj.getString("articlesurl"),
                                        obj.getString("dashurl"),
                                        obj.getString("imagesurl"),
                                        obj.getString("tcurl"),
                                        obj.getString("aacurl"),
                                        obj.getString("spare")
                                );
                                carList.add(car);
                            }
                            adapter = new CarAdapter(carList, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            //added this so that cars will be sorted by year and station on startup
                            //this allows cars to be shown in chronological order within a station
                            sortArrayListGallerySeq();
                            sortArrayListStation();
                            adapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mDrawerList.getContext());
                        builder.setTitle("This device is having network issues...");
                        builder.setMessage("Please try again later");
                        builder.setCancelable(true);
                        final AlertDialog closedialog= builder.create();
                        closedialog.show();
                        final Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            public void run() {
                                closedialog.dismiss();
                                timer2.cancel(); //this will cancel the timer of the system
                            }
                        }, 5000); // the timer will count 5 seconds...
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

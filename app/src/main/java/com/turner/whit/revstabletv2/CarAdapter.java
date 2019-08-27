package com.turner.whit.revstabletv2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.turner.whit.revstabletv2.Utils.ParseJson;




public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> carList;
    private Context context;

    //added this to set up proper use of VolleyRequest????
    private RequestQueue mRequestQueue;


    //temporarily setting current position off screen so initial expanded card is not shown
    //todo change visibility toggle logic to set gone on all items on data change (sorts)
    //fix sorting so that list sorts by station and year on start up - done
    //fix adapter so that card view does not obscure cards near bottom of view - done
    //todo add car title to gallery actionbar
    //add menu item to refresh car data (prob call loadCars) - done
    //add menu item for about - done
    //add menu item for gallery map - done
    //add menu item to refresh car data or restart app - done
    //todo add error checking/alerts - particularly for loss of wifi connection!

    private static int currentPosition = -1;

    CarAdapter(List<Car> carList, Context context) {
        this.carList = carList;
        this.context = context;
    }

    @NonNull

    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_cars, parent, false);
        return new CarViewHolder(v);
    }

    private RecyclerView mRecyclerView;


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView v) {
        super.onAttachedToRecyclerView(v);

        mRecyclerView = v;

    }

    @Override
    public void onBindViewHolder(@NonNull final CarViewHolder holder, final int position) {

        final Car car = carList.get(position);
        holder.textViewYear.setText(car.getYear());
        holder.textViewMake.setText(car.getMake());
        holder.textViewModel.setText(car.getModel());
        holder.textViewDescr.setText(car.getDescr());
        holder.textViewFact1.setText(car.getFact1());
        holder.textViewFact2.setText(car.getFact2());
        holder.textViewFact3.setText(car.getFact3());
        holder.textViewFact4.setText(car.getFact4());
        holder.textViewFact5.setText(car.getFact5());
        holder.textViewFact6.setText(car.getFact6());
        holder.textViewFact7.setText(car.getFact7());
        holder.textViewFact8.setText(car.getFact8());

        //load the thumbnails

        GlideApp.with(context)

                .asBitmap()

                .load(car.getThumburl())

                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))

                .apply(RequestOptions.priorityOf(Priority.IMMEDIATE))

                .into(holder.imageViewThumb);

        //launch articles when button clicked

        holder.btnArticles.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url= car.getArticlesurl();
                Bundle bundle = new Bundle();
                bundle.putString("urlString",url);
                Intent intent = new Intent(context, WebActivity5.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        //launch dash & engine photos gallery

        holder.btnDash.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //get the URL for the image list JSON for the selected car

                String url = car.getDashurl();

                //pop a dialog since the gallery load can take some time
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Please wait while content loads");
                builder.setMessage("It may take a few seconds");
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

//                String TAG = "url is "; <--uncomment for debug
//                Log.d(TAG, url);

                //Listen for JSONObject to get returned from Volley response
                //trigger Volley request by adding it to mRequestQueue, which invokes VolleyRequest class
                //response gets passed to Utils.ParseJson method which then passes the url list to the GalleryActivity

                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        new ParseJson(response);
                    }
                };

                Response.ErrorListener listenerError = new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Error occurred ", error);
                    }
                };

                JsonObjectRequest requestUrls = new JsonObjectRequest(Request.Method.GET, url, null, listenerResponse, listenerError);

                //adding this - makes better use of VolleyRequst singleton and avaoids creating a new queue every time

                mRequestQueue = VolleyRequest.getInstance(context).getRequestQueue();

                mRequestQueue.add(requestUrls);

            }
        });


        //launch the car's page on the Revs website

        holder.btnRevsWeb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = car.getSpare();
                Bundle bundle = new Bundle();
                bundle.putString("urlString",url);
                Intent intent = new Intent(context, WebActivity5.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }

        });

        //launch the gallery sequence
        holder.btnPhotos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //get the URL for the image list JSON for the selected car

                String url = car.getImagesurl();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Please wait while content loads");
                builder.setMessage("It may take a few seconds");
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

                //String TAG = "url is "; <--uncomment for debug
                //Log.d(TAG, url);

                //Listen for JSONObject to get returned from Volley response
                //trigger Volley request by adding it to mRequestQueue, which invokes VolleyRequest singleton class
                //response gets passed to Utils.ParseJson method which then passes the url list to the GalleryActivity

                Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        new ParseJson(response);
                    }
                };

                Response.ErrorListener listenerError = new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Error occurred ", error);
                    }
                };

                JsonObjectRequest requestUrls = new JsonObjectRequest(Request.Method.GET, url, null, listenerResponse, listenerError);

                //adding this

                mRequestQueue = VolleyRequest.getInstance(context).getRequestQueue();

                mRequestQueue.add(requestUrls);

            }
        });




        holder.linearLayout.setVisibility(View.GONE);

        holder.imageViewArrow.setImageResource(R.drawable.ic_expand_more_white_12dp);

        //if the position is equal to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.imageViewArrow.setImageResource(R.drawable.ic_expand_less_white_12dp);
            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);
            //moving this line to here from below seems to improve performance
            mRecyclerView.smoothScrollToPosition(position);
        }

        holder.imageViewArrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //getting the position of the item to expand it if not visible
                currentPosition = position;
                int distance = 0;
                if (holder.linearLayout.getVisibility() == View.VISIBLE) {
                    // Its visible, so toggle visibility
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.imageViewArrow.setImageResource(R.drawable.ic_expand_more_white_12dp);
                    //reset current position to -1 so that all cards are collapsed
                    currentPosition = -1;
                    //reload the list
                } else notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {
        TextView textViewYear, textViewMake, textViewModel, textViewDescr, textViewFact1, textViewFact2, textViewFact3, textViewFact4, textViewFact5, textViewFact6,
        textViewFact7, textViewFact8;
        ImageView imageViewThumb; ImageView imageViewArrow; Button btnArticles; Button btnDash; Button btnPhotos; Button btnRevsWeb;

        LinearLayout linearLayout;


        CarViewHolder(View itemView) {
            super(itemView);

            btnArticles = itemView.findViewById(R.id.Button2);
            btnDash = itemView.findViewById(R.id.Button1);
            btnPhotos = itemView.findViewById(R.id.Button3);
            btnRevsWeb = itemView.findViewById(R.id.Button4);

            textViewYear = itemView.findViewById(R.id.textViewYear);
            textViewMake = itemView.findViewById(R.id.textViewMake);
            textViewModel = itemView.findViewById(R.id.textViewModel);
            textViewDescr = itemView.findViewById(R.id.textViewDescr);

            textViewFact1 = itemView.findViewById(R.id.textViewFact1);
            textViewFact2 = itemView.findViewById(R.id.textViewFact2);
            textViewFact3 = itemView.findViewById(R.id.textViewFact3);
            textViewFact4 = itemView.findViewById(R.id.textViewFact4);
            textViewFact5 = itemView.findViewById(R.id.textViewFact5);
            textViewFact6 = itemView.findViewById(R.id.textViewFact6);
            textViewFact7 = itemView.findViewById(R.id.textViewFact7);
            textViewFact8 = itemView.findViewById(R.id.textViewFact8);

            imageViewArrow = itemView.findViewById(R.id.imageViewArrow);

            imageViewThumb = itemView.findViewById(R.id.imageViewThumb);

            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}

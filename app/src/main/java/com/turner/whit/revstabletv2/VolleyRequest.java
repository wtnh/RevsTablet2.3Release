package com.turner.whit.revstabletv2;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//This class is a Volley singleton which can be called from anywhere in the project - currently used by
//CarAdapter to retreive URLs which point to Gallery collections

class VolleyRequest {

        private static VolleyRequest mInstance = null;
        private static RequestQueue mRequestQueue;

        //this makes Volley request applicable to the whole Apllication so we only have one queue
        private VolleyRequest(Context context){
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        static synchronized VolleyRequest getInstance(Context context){
            if (mInstance == null){
                mInstance = new VolleyRequest(context);
            }

            return mInstance;
        }

        static RequestQueue getRequestQueue(){

            return mRequestQueue;
        }
    }



package com.turner.whit.revstabletv2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


class Utils {

    private static Context context = GlobalApplication.getAppContext();


//This is a static string array used only for test purposes; uncomment and comment out the line String[] IMGS = strArray;
    /*
    static String IMGS[] = {
            "https://revsinstitute.box.com/shared/static/m2p7zq8iorc63pnx130ird503164txs2.jpg",
            "https://revsinstitute.box.com/shared/static/mq1txrlo5u77aepz2zi9jnbcfxumm8p8.jpg",
            "https://revsinstitute.box.com/shared/static/08r9dyhmxy5xpkzvuahigcguqv3wu4ah.jpg",
            "https://revsinstitute.box.com/shared/static/joqitg0iqgbrv73kg875h4eyc46kdhs5.jpg",
            "https://revsinstitute.box.com/shared/static/hx820u5c5xjm2f9t6f3fv48xtw6xme37.jpg",
            "https://revsinstitute.box.com/shared/static/0gpc6tc7yrwrv1azh3433q4i137pmovg.jpg",
            "https://revsinstitute.box.com/shared/static/x5qgwrcoeadmn0ilk0wp7gtcmo4vx4u7.jpg",
            "https://revsinstitute.box.com/shared/static/304t9b7t3beytkcqu69i25aw8uq34buh.jpg",
            "https://revsinstitute.box.com/shared/static/fxh5ig2075mmd7t099vrs7swsdlnjoaw.jpg",
            "https://revsinstitute.box.com/shared/static/ogflbr6fl56lmnp3ou8mgjd1uzf2yauc.jpg",
            "https://revsinstitute.box.com/shared/static/0piejstmiv0sgcx9x092qwllt42vo18n.jpg",
            "https://revsinstitute.box.com/shared/static/j6p3d8b0452v6esr64mhpfsbekrfvyi0.jpg",
    };
*/

    private static String[] strArray;

    static class ParseJson {

        String listOfUrls = "";

        ParseJson(JSONObject urlList) {

            //Parse the JSON object passed in from CarAdapter and build a string of image URLs
            //Note that I am not currently doing anything with the keys, only the values

            Iterator<String> keys = urlList.keys();
            int i = 0;
            while (keys.hasNext()) {
                String key = keys.next();
                String value = "";

                try {

                    value = urlList.getString(key);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                i = i + 1;

                listOfUrls = listOfUrls + value + ",";

            }

            //Create a string array from the string of URLs

            strArray = listOfUrls.split(",");

            //Call the Gallery Activity now; the GalleryActivity will call the getData method below, which uses strArray
            //to provide the image URLs to Glide - note that strArray gets renamed to IMGS to keep the original code intact

            Bundle bundle = new Bundle();
            Intent intent = new Intent(context, GalleryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }

    static ArrayList<ImageModel> getData() {

        String[] IMGS = strArray;

//        System.out.println(Arrays.toString(IMGS)); //<-- uncomment for debugging

        ArrayList<ImageModel> arrayList = new ArrayList<>();
        for (int i = 0; i < IMGS.length; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName("Image " + i);
            imageModel.setUrl(IMGS[i]);
            arrayList.add(imageModel);
//            System.out.println(i);
//            System.out.println(imageModel.getUrl());
        }

//        for (ImageModel num : arrayList) {
//            System.out.println(num);
//        }

        return arrayList;
    }

}

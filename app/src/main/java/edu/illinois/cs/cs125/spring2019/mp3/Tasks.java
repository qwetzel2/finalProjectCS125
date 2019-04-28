package edu.illinois.cs.cs125.spring2019.mp3;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
import java.io.InputStreamReader;
//import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Background tasks for use by our image recognition app.
 */
class Tasks {
    /**
     * Default logging tag for messages from app tasks.
     */
    private static final String TAG = "MP3:Tasks";

    /**
     * Default quality level for bitmap compression.
     */
    private static final int DEFAULT_COMPRESSION_QUALITY_LEVEL = 100;


    static class ProcessImageTask extends AsyncTask<Bitmap, Integer, Integer> {

        /**
         * Url for the MS cognitive services API.
         */
        private static final String MS_CV_API_URL =
                "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/analyze";

        /**
         * Default visual features to request. You may need to change this value.
         */
        private static final String MS_CV_API_DEFAULT_VISUAL_FEATURES =
                "Categories,Description,Faces,ImageType,Color,Adult,Tags";

        /**
         * Default visual features to request.
         */
        private static final String MS_CV_API_DEFAULT_LANGUAGE = "en";

        /**
         * Default visual features to request. You may need to change this value.
         */
        private static final String MS_CV_API_DEFAULT_DETAILS = "Landmarks,Celebrities";

        /**
         * Subscription key.
         */
        private static final String SUBSCRIPTION_KEY = BuildConfig.API_KEY;

        /**
         * Reference to the calling activity so that we can return results.
         */
        private WeakReference<MainActivity> activityReference;

        /**
         * Request queue to use for our API call.
         */
        private RequestQueue requestQueue;

        /**
         * Create a new talk to upload data and return the API results.
         * <p>
         * We pass in a reference to the app so that this task can be static.
         * Otherwise we get warnings about leaking the context.
         *
         * @param context         calling activity context
         * @param setRequestQueue Volley request queue to use for the API request
         */
        ProcessImageTask(final MainActivity context, final RequestQueue setRequestQueue) {
            activityReference = new WeakReference<>(context);
            requestQueue = setRequestQueue;
        }

        /**
         * Before we start draw the waiting indicator.
         */
        @Override
        protected void onPreExecute() {
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            //ProgressBar progressBar = activity.findViewById(R.id.progressBar);
            //progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * Convert an image to a byte array, upload to the Microsoft Cognitive Services API,
         * and return a result.
         *
         * @param currentBitmap the bitmap to process
         * @return unused result
         */
        protected Integer doInBackground(final Bitmap... currentBitmap) {
            /*
             * Convert the image from a Bitmap to a byte array for upload.
             */
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            currentBitmap[0].compress(Bitmap.CompressFormat.PNG,
                    DEFAULT_COMPRESSION_QUALITY_LEVEL, stream);

            // Prepare our API request
            String requestURL = Uri.parse(MS_CV_API_URL)
                    .buildUpon()
                    .appendQueryParameter("visualFeatures", MS_CV_API_DEFAULT_VISUAL_FEATURES)
                    .appendQueryParameter("details", MS_CV_API_DEFAULT_DETAILS)
                    .appendQueryParameter("language", MS_CV_API_DEFAULT_LANGUAGE)
                    .build()
                    .toString();
            Log.d(TAG, "Using URL: " + requestURL);

            /*
             * Make the API request.
             */
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, requestURL,
                    this::handleApiResponse, this::handleApiError) {
                @Override
                public Map<String, String> getHeaders() {
                    // Set up headers properly
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/octet-stream");
                    headers.put("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY);
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    // Set the body content type properly for a binary upload
                    return "application/octet-stream";
                }

                @Override
                public byte[] getBody() {
                    return stream.toByteArray();
                }
            };
            requestQueue.add(stringRequest);

            System.out.println("_____________________________________________");

            /* doInBackground can't return void, otherwise we would. */
            return 0;
        }

        /**
         * Processes a response from the image recognition API.
         *
         * @param response The JSON text of the response.
         */
        void handleApiResponse(final String response) {
            // On success, clear the progress bar and call finishProcessImage
            Log.d(TAG, "Response: " + response);
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            //ProgressBar progressBar = activity.findViewById(R.id.progressBar);
            //progressBar.setVisibility(View.INVISIBLE);
            //activity.finishProcessImage(response);
        }

        /**
         * Handles an error encountered when trying to use the image recognition API.
         *
         * @param error The error that caused the request to fail.
         */
        void handleApiError(final VolleyError error) {
            // On failure just clear the progress bar
            Log.w(TAG, "Error: " + error.toString());
            NetworkResponse networkResponse = error.networkResponse;
            if (networkResponse != null
                    && networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.w(TAG, "Unauthorized request. "
                        + "Make sure you added your API_KEY to app/secrets.properties");
            }
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }
    }





    /**
     * Compress an image in the background, then send it to Cognitive Services for identification.
     */
    static class DeckOfCardsAPIthingy {

        /**
         * Open a brand new deck of cards.
         * A-spades, 2-spades, 3-spades... followed by diamonds, clubs, then hearts.
         */
        private static final String NEW_DECK_URL =
                "https://deckofcardsapi.com/api/deck/new/";

        /**
         * Add deck_count as a GET or POST parameter to define the number of Decks you want to use.
         * The default is 1.
         */
        private static final String SHUFFLE_CARDS_URL =
                "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1";

        /**
         * The count variable defines how many cards to draw from the deck.
         * Be sure to replace deck_id with a valid deck_id.
         * We use the deck_id as an identifier so we know who is playing with what deck.
         * After two weeks, if no actions have been made on the deck then we throw it away.
         */
        private static final String DRAW_A_CARD_URL =
                "https://deckofcardsapi.com/api/deck/<<deck_id>>/draw/?count=2";

        /**
         * Piles can be used for discarding, players hands, or whatever else.
         * Piles are created on the fly, just give a pile a name and add a drawn card to the pile.
         * If the pile didn't exist before, it does now.
         * After a card has been drawn from the deck it can be moved from pile to pile.
         */
        private static final String ADDING_CARDS_TO_PILE =
                "https://deckofcardsapi.com/api/deck/<<deck_id>>/pile/<<pile_name>>/add/?cards=AS,2S";

        /**
         * Reference to the calling activity so that we can return results.
         */
        private WeakReference<MainActivity> activityReference;

        /**
         * Request queue to use for our API call.
         */
        private RequestQueue requestQueue;


        /**
         * Processes a response from the image recognition API.
         *
         * @param response The JSON text of the response.
         */
        void handleApiResponse(final String response) {
            // On success, clear the progress bar and call finishProcessImage
            Log.d(TAG, "Response: " + response);
            MainActivity activity = activityReference.get();
            /*if (activity == null || activity.isFinishing()) {
                return;
            }
            ProgressBar progressBar = activity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            activity.finishProcessImage(response);*/
        }

        /**
         * Handles an error encountered when trying to use the image recognition API.
         *
         * @param error The error that caused the request to fail.
         */
        void handleApiError(final VolleyError error) {
            // On failure just clear the progress bar
            Log.w(TAG, "Error: " + error.toString());
            NetworkResponse networkResponse = error.networkResponse;
            if (networkResponse != null
                    && networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.w(TAG, "Unauthorized request. "
                        + "Make sure you added your API_KEY to app/secrets.properties");
            }
            MainActivity activity = activityReference.get();
            /*if (activity == null || activity.isFinishing()) {
                return;
            }
            ProgressBar progressBar = activity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);*/
        }
    }
}

package edu.illinois.cs.cs125.spring2019.mp3;

//import android.Manifest;
import android.app.Activity;
//import android.app.AlertDialog;
import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
//import android.text.InputType;
import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;

//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.IOUtils;

//import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//import edu.illinois.cs.cs125.spring2019.mp3.lib.RecognizePhoto;
import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.illinois.cs.cs125.spring2019.mp3.lib.Player;


//import javax.net.ssl.HttpsURLConnection;


/**
 * Main screen for our image recognition app.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "MP3:Main";

    /** Constant to perform a read file request. */
    private static final int READ_REQUEST_CODE = 42;

    /** Constant to request an image capture. */
    private static final int IMAGE_CAPTURE_REQUEST_CODE = 1;

    /** Constant to request permission to write to the external storage device. */
    private static final int REQUEST_WRITE_STORAGE = 112;

    /** Threshold for calling something a dog or cat. */
    private static final double RECOGNITION_THRESHOLD = 0.9;

    /** Request queue for our network requests. */
    private RequestQueue requestQueue;

    /** Whether we can write to public storage. */
    private boolean canWriteToPublicStorage = false;

    /**
     * The deck being used for this instance of the game.
     */
    private String deckId;

    /**
     * Run when our activity comes into view.
     *
     * @param savedInstanceState state that was saved by the activity last time it was paused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);

        // Load the main layout for our activity
        setContentView(R.layout.activity_main);

        try {
            requestNewDeck();
        } catch (Exception e) {
            e.printStackTrace();
        }
        user = new Player("user");
        cpu = new Player("cpu");
    }

    /**
     * Request the deck from the API.
     *
     * @throws Exception Does something???
     */
    public void requestNewDeck() throws Exception {
        String url = "https://deckofcardsapi.com/api/deck/new/"; //api URL

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
                        @Override
            public void onResponse(final JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            deckId = (String) response.get("deck_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(deckId);
                        startGame();
                        }
                }, new Response.ErrorListener() {

                    @Override
                        public void onErrorResponse(final VolleyError error) {
                            // TODO: Handle error
                        }
                });
        //System.out.println(deckId);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Draw a new card from the api.
     * @param toDraw the player who is drawing the card.
     */
    public void drawACard(final Player toDraw) {
        String url = "https://deckofcardsapi.com/api/deck/" + deckId + "/draw/?count=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            JSONObject object = (JSONObject) response.getJSONArray("cards").get(0);
                            String cardID = object.get("code").toString();
                            System.out.println("-------------" + cardID + "--------------");
                            String urlToPile = "https://deckofcardsapi.com/api/deck/" + deckId + "/pile/"
                                    + toDraw.pileName + "/add/?cards=" + cardID;
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlToPile,
                                    null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(final JSONObject response) {
                                            System.out.println("______________Success________________");
                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(final VolleyError error) {
                                            // TODO: Handle error
                                        }
                                    });
                            requestQueue.add(jsonObjectRequest);







                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                // TODO: Handle error
            }
        });
        //System.out.println(deckId);
        requestQueue.add(jsonObjectRequest);
    }

    private Player user;

    private Player cpu;

    /**
     * Beginning of game.
     */
    public void startGame() {
        turn(user, cpu);
    }

    /**
     * One turn of the game.
     * @param myTurn the player who is currently playing their turn.
     * @param notMyTurn the player who is not currently taking their turn.
     */
    public void turn(final Player myTurn, final Player notMyTurn) {
        drawACard(myTurn);



    }

    /**
     * Called when an intent that we requested has finished.
     *
     * In our case, we either asked the file browser to open a file, or the camera to take a
     * photo. We respond appropriately below.
     *
     * @param requestCode the code that we used to make the request
     * @param resultCode a code indicating what happened: success or failure
     * @param resultData any data returned by the activity
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent resultData) {

        // If something went wrong we simply log a warning and return
        if (resultCode != Activity.RESULT_OK) {
            Log.w(TAG, "onActivityResult with code " + requestCode + " failed");
            if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
                //photoRequestActive = false;
            }
            return;
        }

        // Otherwise we get a link to the photo either from the file browser or the camera,
        Uri currentPhotoURI;
        if (requestCode == READ_REQUEST_CODE) {
            currentPhotoURI = resultData.getData();
        } else if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
            //currentPhotoURI = Uri.fromFile(currentPhotoFile);
            //photoRequestActive = false;
            if (canWriteToPublicStorage) {
                //addPhotoToGallery(currentPhotoURI);
            }
        } else {
            Log.w(TAG, "Unhandled activityResult with code " + requestCode);
            return;
        }

    }


    /**
     * Gets the Volley request queue for this activity. For testing purposes only.
     * @return the internal web request queue
     */
    RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * Sets the Volley request queue used by this activity. For testing purposes only.
     * @param newQueue the request queue to install
     */
    void setRequestQueue(final RequestQueue newQueue) {
        requestQueue = newQueue;
    }
}


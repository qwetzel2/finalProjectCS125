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
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
//import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;

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
        System.out.println("----------------------------------------");

        super.onCreate(savedInstanceState);
        System.out.println("----------------------------------------");

        // Load the main layout for our activity
        setContentView(R.layout.activity_main);
        System.out.println("----------------------------------------");

        try {
            requestNewDeck();
        } catch (Exception e) {
            e.printStackTrace();
        }
        user = new Player("user");
        cpu = new Player("cpu");
        System.out.println("----------------------------------------");
        king = findViewById(R.id.king);
        king.setVisibility(View.INVISIBLE);
        king.setOnClickListener(v -> {
             selectCard(king, kings); // Code here executes on main thread after user presses button
        });
        queen = findViewById(R.id.queen);
        queen.setVisibility(View.INVISIBLE);
        queen.setOnClickListener(v -> {
            selectCard(queen, queens); // Code here executes on main thread after user presses button
        });
        jack = findViewById(R.id.jack);
        jack.setVisibility(View.INVISIBLE);
        jack.setOnClickListener(v -> {
            selectCard(jack, jacks);
        });
        ten = findViewById(R.id.ten);
        ten.setVisibility(View.INVISIBLE);
        ten.setOnClickListener(v -> {
            selectCard(ten, tens); // Code here executes on main thread after user presses button
        });
        nine = findViewById(R.id.nine);
        nine.setVisibility(View.INVISIBLE);
        nine.setOnClickListener(v -> {
            selectCard(nine, nines); // Code here executes on main thread after user presses button
        });
        eight = findViewById(R.id.eight);
        eight.setVisibility(View.INVISIBLE);
        eight.setOnClickListener(v -> {
            selectCard(eight, eights); // Code here executes on main thread after user presses button
        });
        seven = findViewById(R.id.seven);
        seven.setVisibility(View.INVISIBLE);
        seven.setOnClickListener(v -> {
            selectCard(seven, sevens); // Code here executes on main thread after user presses button
        });
        six = findViewById(R.id.six);
        six.setVisibility(View.INVISIBLE);
        six.setOnClickListener(v -> {
            selectCard(six, sixes); // Code here executes on main thread after user presses button
        });
        five = findViewById(R.id.five);
        five.setVisibility(View.INVISIBLE);
        five.setOnClickListener(v -> {
            selectCard(five, fives); // Code here executes on main thread after user presses button
        });
        four = findViewById(R.id.four);
        four.setVisibility(View.INVISIBLE);
        four.setOnClickListener(v -> {
            selectCard(four, fours); // Code here executes on main thread after user presses button
        });
        three = findViewById(R.id.three);
        three.setVisibility(View.INVISIBLE);
        three.setOnClickListener(v -> {
            selectCard(three, threes); // Code here executes on main thread after user presses button
        });
        two = findViewById(R.id.two);
        two.setVisibility(View.INVISIBLE);
        two.setOnClickListener(v -> {
            selectCard(two, twos); // Code here executes on main thread after user presses button
        });
        ace = findViewById(R.id.ace);
        ace.setOnClickListener(v -> {
            selectCard(ace, aces); // Code here executes on main thread after user presses button
        });
        final ImageButton deck = findViewById(R.id.deck);
        whatIsGoingOn = findViewById(R.id.whatIsGoingOn);
        whatIsGoingOn.setText("New Game");
        buttons.add(ace);
        buttons.add(two);
        buttons.add(three);
        buttons.add(four);
        buttons.add(five);
        buttons.add(six);
        buttons.add(seven);
        buttons.add(eight);
        buttons.add(nine);
        buttons.add(ten);
        buttons.add(jack);
        buttons.add(queen);
        buttons.add(king);
        aceNumber.setVisibility(View.INVISIBLE);
        twoNumber.setVisibility(View.INVISIBLE);
        threeNumber.setVisibility(View.INVISIBLE);
        fourNumber.setVisibility(View.INVISIBLE);
        fiveNumber.setVisibility(View.INVISIBLE);
        sixNumber.setVisibility(View.INVISIBLE);
        sevenNumber.setVisibility(View.INVISIBLE);
        eightNumber.setVisibility(View.INVISIBLE);
        nineNumber.setVisibility(View.INVISIBLE);
        tenNumber.setVisibility(View.INVISIBLE);
        jackNumber.setVisibility(View.INVISIBLE);
        queenNumber.setVisibility(View.INVISIBLE);
        kingNumber.setVisibility(View.INVISIBLE);


    }

    /**
     * .
     */
    private TextView aceNumber;

    /**
     * .
     */
    private int acesDrawn;

    /**
     * .
     */
    private TextView twoNumber;

    /**
     * .
     */
    private int twosDrawn;

    /**
     * .
     */
    private TextView threeNumber;

    /**
     * .
     */
    private int threesDrawn;

    /**
     * .
     */
    private TextView fourNumber;

    /**
     * .
     */
    private int foursDrawn;

    /**
     * .
     */
    private TextView fiveNumber;

    /**
     * .
     */
    private int fivesDrawn;

    /**
     * .
     */
    private TextView sixNumber;

    /**
     * .
     */
    private int sixesDrawn;

    /**
     * .
     */
    private TextView sevenNumber;

    /**
     * .
     */
    private int sevensDrawn;

    /**
     * .
     */
    private TextView eightNumber;

    /**
     * .
     */
    private int eightsDrawn;

    /**
     * .
     */
    private TextView nineNumber;

    /**
     * .
     */
    private int ninesDrawn;

    /**
     * .
     */
    private TextView tenNumber;

    /**
     * .
     */
    private int tensDrawn;

    /**
     * .
     */
    private TextView jackNumber;

    /**
     * .
     */
    private int jacksDrawn;

    /**
     * .
     */
    private TextView queenNumber;

    /**
     * .
     */
    private int queensDrawn;

    /**
     * .
     */
    private TextView kingNumber;

    /**
     * .
     */
    private int kingsDrawn;

    /**
     * The textview that displays what is happening in the game.
     */
    private TextView whatIsGoingOn;

    /**
     * ImageButton for the aces.
     */
    private ImageButton ace;

    /**
     * ImageButton for the aces.
     */
    private ImageButton two;

    /**
     * ImageButton for the aces.
     */
    private ImageButton three;

    /**
     * ImageButton for the aces.
     */
    private ImageButton four;

    /**
     * ImageButton for the aces.
     */
    private ImageButton five;

    /**
     * ImageButton for the aces.
     */
    private ImageButton six;

    /**
     * ImageButton for the aces.
     */
    private ImageButton seven;

    /**
     * ImageButton for the aces.
     */
    private ImageButton eight;

    /**
     * ImageButton for the aces.
     */
    private ImageButton nine;

    /**
     * ImageButton for the aces.
     */
    private ImageButton ten;

    /**
     * ImageButton for the aces.
     */
    private ImageButton jack;

    /**
     * ImageButton for the aces.
     */
    private ImageButton queen;

    /**
     * ImageButton for the aces.
     */
    private ImageButton king;

    /**
     * stores the buttons that display the current cards.
     */
    private List<ImageButton> buttons = new ArrayList<>();

    /**
     * Color to indicate that a card has been selected.
     */
    private static final int COLOR_FOR_SELECTED = 17170450;

    /**
     * Color to indicate that a card has been selected.
     */
    private static final int COLOR_FOR_NON_SELECTED = 17170443;

    /**
     * sets the UI to "Select" the selected Card.
     * @param toSet the botton being selected
     * @param number the selected number
     */
    private void selectCard(final ImageButton toSet, final List number) {
        if (toSet.getVisibility() != View.VISIBLE) {
            return;
        }
        toSet.setBackgroundColor(COLOR_FOR_SELECTED);
        for (ImageButton button : buttons) {
            if (!button.equals(toSet)) {
                button.setBackgroundColor(COLOR_FOR_NON_SELECTED);
            }
        }
        selectedCards = number;
    }

    /**
     * The cards that might be transferred.
     */
    private List<String> selectedCards = new ArrayList<>();

    /**
     * List to store the amount of aces held by user.
     */
    private List<String> aces = new ArrayList<>();

    /**
     * List to store the amount of twos held by user.
     */
    private List<String> twos = new ArrayList<>();

    /**
     * List to store the amount of threes held by user.
     */
    private List<String> threes = new ArrayList<>();

    /**
     * List to store the amount of fours held by user.
     */
    private List<String> fours = new ArrayList<>();

    /**
     * List to store the amount of fives held by user.
     */
    private List<String> fives = new ArrayList<>();

    /**
     * List to store the amount of sixes held by user.
     */
    private List<String> sixes = new ArrayList<>();

    /**
     * List to store the amount of sevens held by user.
     */
    private List<String> sevens = new ArrayList<>();

    /**
     * List to store the amount of eights held by user.
     */
    private List<String> eights = new ArrayList<>();

    /**
     * List to store the amount of nines held by user.
     */
    private List<String> nines = new ArrayList<>();

    /**
     * List to store the amount of tens held by user.
     */
    private List<String> tens = new ArrayList<>();

    /**
     * List to store the amount of jacks held by user.
     */
    private List<String> jacks = new ArrayList<>();

    /**
     * List to store the amount of queens held by user.
     */
    private List<String> queens = new ArrayList<>();

    /**
     * List to store the amount of kings held by user.
     */
    private List<String> kings = new ArrayList<>();

    /**
     * Request the deck from the API.
     * @throws Exception Does something???
     */
    public void requestNewDeck() throws Exception {
        String url = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1"; //api URL

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
     * the card that was just drawn.
     */
    private String currentCard;

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
                                            System.out.println(response);
                                            currentCard = cardID;
                                            if (toDraw.equals(user)) {
                                                if (cardID.startsWith("A")) {
                                                    ace.setVisibility(View.VISIBLE);
                                                    aces.add(cardID);
                                                    aceNumber.setVisibility(View.VISIBLE);
                                                    acesDrawn++;
                                                } else if (cardID.startsWith("2")) {
                                                    two.setVisibility(View.VISIBLE);
                                                    twos.add(cardID);
                                                    twoNumber.setVisibility(View.VISIBLE);
                                                    twosDrawn++;
                                                } else if (cardID.startsWith("3")) {
                                                    threes.add(cardID);
                                                    three.setVisibility(View.VISIBLE);
                                                    threeNumber.setVisibility(View.VISIBLE);
                                                    threesDrawn++;
                                                } else if (cardID.startsWith("4")) {
                                                    fours.add(cardID);
                                                    four.setVisibility(View.VISIBLE);
                                                    fourNumber.setVisibility(View.VISIBLE);
                                                    foursDrawn++;
                                                } else if (cardID.startsWith("5")) {
                                                    fives.add(cardID);
                                                    five.setVisibility(View.VISIBLE);
                                                    fiveNumber.setVisibility(View.VISIBLE);
                                                    fivesDrawn++;
                                                } else if (cardID.startsWith("6")) {
                                                    six.setVisibility(View.VISIBLE);
                                                    sixes.add(cardID);
                                                    sixNumber.setVisibility(View.VISIBLE);
                                                    sixesDrawn++;
                                                } else if (cardID.startsWith("7")) {
                                                    seven.setVisibility(View.VISIBLE);
                                                    sevens.add(cardID);
                                                    sevenNumber.setVisibility(View.VISIBLE);
                                                    sevensDrawn++;
                                                } else if (cardID.startsWith("8")) {
                                                    eight.setVisibility(View.VISIBLE);
                                                    eights.add(cardID);
                                                    eightNumber.setVisibility(View.VISIBLE);
                                                    eightsDrawn++;
                                                } else if (cardID.startsWith("9")) {
                                                    nine.setVisibility(View.VISIBLE);
                                                    nines.add(cardID);
                                                    nineNumber.setVisibility(View.VISIBLE);
                                                    ninesDrawn++;
                                                } else if (cardID.startsWith("1")) {
                                                    ten.setVisibility(View.VISIBLE);
                                                    tens.add(cardID);
                                                    tenNumber.setVisibility(View.VISIBLE);
                                                    tensDrawn++;
                                                } else if (cardID.startsWith("J")) {
                                                    jack.setVisibility(View.VISIBLE);
                                                    jacks.add(cardID);
                                                    jackNumber.setVisibility(View.VISIBLE);
                                                    jacksDrawn++;
                                                } else if (cardID.startsWith("Q")) {
                                                    queen.setVisibility(View.VISIBLE);
                                                    queens.add(cardID);
                                                    queenNumber.setVisibility(View.VISIBLE);
                                                    queensDrawn++;
                                                } else if (cardID.startsWith("K")) {
                                                    king.setVisibility(View.VISIBLE);
                                                    kings.add(cardID);
                                                    kingNumber.setVisibility(View.VISIBLE);
                                                    kingsDrawn++;
                                                }
                                                whatIsGoingOn.setText("You have drawn a " + cardID);
                                                resetNumbers();
                                            }
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

    /**
     * Reset the textviews for the UI.
     */
    public void resetNumbers() {
        aceNumber.setText("x" + acesDrawn);
        twoNumber.setText("x" + twosDrawn);
        threeNumber.setText("x" + threesDrawn);
        fourNumber.setText("x" + foursDrawn);
        fiveNumber.setText("x" + fivesDrawn);
        sixNumber.setText("x" + sixesDrawn);
        sevenNumber.setText("x" + sevensDrawn);
        eightNumber.setText("x" + eightsDrawn);
        nineNumber.setText("x" + ninesDrawn);
        tenNumber.setText("x" + tensDrawn);
        jackNumber.setText("x" + jacksDrawn);
        queenNumber.setText("x" + queensDrawn);
        kingNumber.setText("x" + kingsDrawn);
    }

    /**
     * The phone user player.
     */
    private Player user;

    /**
     * The computer player.
     */
    private Player cpu;

    /**
     * Runs when a new deck is created.
     */
    public void startGame() {
        drawACard(user);
        drawACard(user);
        drawACard(user);
        drawACard(user);
        drawACard(cpu);
        drawACard(cpu);
        drawACard(cpu);
        drawACard(cpu);
        userTurn();
    }
    /**
     * One turn of the game.
     */
    public void userTurn() {
        whatIsGoingOn.setText("Select the cards you want to request and hit play!");
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


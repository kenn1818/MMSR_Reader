package com.example.pc.mmsr_reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pc.mmsr_reader.Activity.RatingActivity;
import com.example.pc.mmsr_reader.Class.Language;
import com.example.pc.mmsr_reader.Class.Page;
import com.example.pc.mmsr_reader.Class.Reader;
import com.example.pc.mmsr_reader.Class.Storybook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by pc on 12/11/2017.
 */

public class LibraryPopupWindowActivity extends Activity implements TextToSpeech.OnInitListener {
    public static Activity LibraryPopupWindowActivity;
    public static Storybook storybook;
    public static Reader reader;
    private ImageButton imgbtnNextPage;
    private ImageButton imgbtnPreviousPage;
    public static int currentpagenumber = 0;
    public static int currentpagearray = 0;
    ArrayList<Page> storybookPage;
    public ImageView imgvImage;
    public TextView etContent;
    public Spinner spinnerLanguageCode;

    public String storybookID;
    public String title;
    public String languageCode;
    public String description;
    public String publishdate;
    public String email;
    public String agegroupcode;
    public String lastBookNum;
    public String rating;

    DatabaseHandler myDb;
    List<String> list;
    public String getSelectedStorybookPagesUrl;
    private ProgressDialog progressDialog;
    String selectedLanguage;
    String translatenewlanguage[];
    String translatenewversion[];
    String language1[];
    String language2[];
    int storytotalpagecount;

    private final int CHECK_CODE = 0x1;
    private TextToSpeech tts;
    private UtteranceProgressListener utteranceProgressListener;

    private Button buttonSpeak;
    private Button buttonDownload;
    private Button buttonRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_popup_window);
        LibraryPopupWindowActivity = this;
        myDb = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        double width = dm.widthPixels;
        double height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.9));

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        storybookID = (String) b.get("STORYBOOKID");
        title = (String) b.get("TITLE");
        languageCode = (String) b.get("LANGUAGECODE");
        publishdate = (String) b.get("PUBLISHDATE");
        email = (String) b.get("EMAIL");
        agegroupcode = (String) b.get("AGEGROUPCODE");
        rating = (String) b.get("RATING");

        etContent = findViewById(R.id.etContent);
        spinnerLanguageCode = findViewById(R.id.spnLanguageCode);
        imgvImage = findViewById(R.id.imgvImageInPopUpWindow);

        checkTTS();

        ArrayAdapter<String> adapter;
        list = new ArrayList<String>();
        String[] AvailableLanguage = languageCode.split(" ");
        for (int i = 0; i < AvailableLanguage.length; i++) {
            if(AvailableLanguage[i].equals("EN")){
                list.add("English");
            }else if(AvailableLanguage[i].equals("BM")){
                list.add("Malay");
            }else if(AvailableLanguage[i].equals("ZH")){
                list.add("Chinese");
            }

        }

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguageCode.setAdapter(adapter);
        spinnerLanguageCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedLanguage = spinnerLanguageCode.getSelectedItem().toString();
                if(selectedLanguage.equals("English")){
                    selectedLanguage = "EN";
                }else if(selectedLanguage.equals("Malay")){
                    selectedLanguage = "BM";
                }else if(selectedLanguage.equals("Chinese")){
                    selectedLanguage = "ZH";
                }
                progressDialog.setMessage("Loading storybook content...");
                progressDialog.show();

                getSelectedStorybookPages(storybookID, selectedLanguage);
                getLanguage(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        buttonDownload = findViewById(R.id.btnDownload);
        buttonRate = findViewById(R.id.btnRateMe);

        buttonSpeak = findViewById(R.id.buttonSpeak);
        buttonSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int id = v.getId();

                if (id == R.id.buttonSpeak) {
                    String stringInput;

                    stringInput = etContent.getText().toString();

                    if (tts.isSpeaking()) {
                        Toast.makeText(getApplicationContext(), "System busy. Please try again later.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        tts.setLanguage(new Locale(selectedLanguage));

                        tts.speak(stringInput, TextToSpeech.QUEUE_ADD, null, stringInput);
                       /* for (String word : stringInput.split(" ")) {
                            tts.setLanguage(new Locale(selectedLanguage));

                            tts.speak(word, TextToSpeech.QUEUE_ADD, null, word);
                        }*/

                    } else {
                        HashMap<String, String> hash = new HashMap<String, String>();
                        hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                                String.valueOf(AudioManager.STREAM_NOTIFICATION));
                        tts.speak(stringInput, TextToSpeech.QUEUE_ADD, hash);
//                        for (String word : stringInput.split(" ")) {
//                            tts.speak(word, TextToSpeech.QUEUE_ADD, hash);
//                        }

                    }
                }
            }
        });


        imgbtnPreviousPage = findViewById(R.id.imgbtnPreviousPage);
        imgbtnPreviousPage.setVisibility(View.INVISIBLE);
        imgbtnPreviousPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentpagenumber > 0) {
                    imgbtnNextPage.setVisibility(View.VISIBLE);
                    etContent = findViewById(R.id.etContent);
                    //    getSupportActionBar().setTitle("Page " + (currentpagenumber - 1));
                    etContent.setText(storybook.getPage(currentpagearray - 1).getContent());
                    Bitmap picture = BitmapFactory.decodeByteArray(storybook.getPage(currentpagearray - 1).getMedia(), 0, storybook.getPage(currentpagearray - 1).getMedia().length);
                    imgvImage.setImageBitmap(picture);
                    currentpagenumber--;
                    currentpagearray--;
                    imgbtnPreviousPage.setVisibility(View.VISIBLE);
                    if (currentpagenumber == 0) {
                        //   getSupportActionBar().setTitle("Cover Page");
                        imgbtnPreviousPage.setVisibility(View.INVISIBLE);
                    }
                }
            }

        });

        imgbtnNextPage = findViewById(R.id.imgbtnNextPage);
        imgbtnNextPage.setVisibility(View.INVISIBLE);
        imgbtnNextPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentpagenumber < storybook.getLastPageNo()) {
                    imgbtnPreviousPage.setVisibility(View.VISIBLE);
                    etContent = findViewById(R.id.etContent);
                    // getSupportActionBar().setTitle("Page " + (currentpagenumber + 1));
                    etContent.setText(storybook.getPage(currentpagearray + 1).getContent());
                    Bitmap picture = BitmapFactory.decodeByteArray(storybook.getPage(currentpagearray + 1).getMedia(), 0, storybook.getPage(currentpagearray + 1).getMedia().length);
                    imgvImage.setImageBitmap(picture);
                    currentpagenumber++;
                    currentpagearray++;
                    imgbtnNextPage.setVisibility(View.VISIBLE);
                    if (currentpagenumber + 1 == storybook.getLastPageNo()) {
                        imgbtnNextPage.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

    }

    public void Rating(View v){
        Intent intent = new Intent(LibraryPopupWindowActivity.this, RatingActivity.class);
        intent.putExtra("STORYBOOKID", storybookID);
        intent.putExtra("TITLE", title);
        intent.putExtra("LANGUAGECODE", languageCode);
        intent.putExtra("DESCRIPTION", description);
        intent.putExtra("PUBLISHDATE", publishdate);
        intent.putExtra("EMAIL", email);
        intent.putExtra("AGEGROUPCODE", agegroupcode);
        startActivity(intent);
    }

    private void getLanguage(String selectedLanguage) {
        List<String> availablelanguagearraylist = new ArrayList<String>();
        List<String> userknownlanguagearraylist = new ArrayList<String>();

        final ArrayList<Language> UserKnownLanguage = myDb.getUserLanguage();
        for (int i = 0; i < list.size(); i++) {
            Log.e("AvailableLanguage", list.get(i));
            availablelanguagearraylist.add(list.get(i));
        }
        for (int i = 0; i < UserKnownLanguage.size(); i++) {
            Log.e("UserKnownLanguage", UserKnownLanguage.get(i).getLanguageCode());
            userknownlanguagearraylist.add(UserKnownLanguage.get(i).getLanguageCode());

        }
        List<String> union = new ArrayList<String>(availablelanguagearraylist);
        union.addAll(userknownlanguagearraylist);
        List<String> intersection = new ArrayList<String>(availablelanguagearraylist);
        intersection.retainAll(userknownlanguagearraylist);
        List<String> symmetricDifference = new ArrayList<String>(union);
        symmetricDifference.removeAll(intersection);
        for (int i = 0; i < symmetricDifference.size(); i++) {
            Log.e("symmetricDifference", symmetricDifference.get(i));

        }
        final List<String> TranslateNewLanguage = new ArrayList<String>();
        final List<String> TranslateNewVersion = new ArrayList<String>();
        final List<String> Language1 = new ArrayList<String>();
        final List<String> Language2 = new ArrayList<String>();

        for (int i = 0; i < symmetricDifference.size(); i++) {
            TranslateNewLanguage.add("Translate from " + selectedLanguage + " To " + symmetricDifference.get(i));
            Language1.add(selectedLanguage);
            Language2.add(symmetricDifference.get(i));

        }
        TranslateNewVersion.add(selectedLanguage);
        translatenewlanguage = new String[TranslateNewLanguage.size()];
        translatenewversion = new String[TranslateNewVersion.size()];
        language1 = new String[Language1.size()];
        language2 = new String[Language2.size()];
        for (
                int i = 0; i < TranslateNewLanguage.size(); i++)

        {
            translatenewlanguage[i] = TranslateNewLanguage.get(i);
        }
        for (
                int i = 0; i < TranslateNewVersion.size(); i++)

        {
            translatenewversion[i] = TranslateNewVersion.get(i);
        }
        for (
                int i = 0; i < Language1.size(); i++)

        {
            language1[i] = Language1.get(i);
        }
        for (
                int i = 0; i < Language2.size(); i++)

        {
            language2[i] = Language2.get(i);
        }

    }

    private void getSelectedStorybookPages(String storybookID, String selectedLanguage) {
        storytotalpagecount = 0;
        storybook = new Storybook();
        currentpagenumber = 0;
        currentpagearray = 0;
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        storybookPage = new ArrayList<>();
        getSelectedStorybookPagesUrl = "http://tarucmmsr.pe.hu/get_translate_page.php?storybookId=" + storybookID + "&languageCode=" + selectedLanguage;
        JsonArrayRequest request = new JsonArrayRequest(getSelectedStorybookPagesUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseJsonResponse(response);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("", error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    private void parseJsonResponse(JSONArray response) {
        if (response == null || response.length() == 0) {
            Log.e("here", "no data from response");
        }
        try {
            JSONArray arrayStory = response;
            for (int i = 0; i < arrayStory.length(); i++) {
                JSONObject storyObject = (JSONObject) response.get(i);
                String languageCode = storyObject.getString("languageCode");
                int pageNo = Integer.parseInt(storyObject.getString("pageNo"));
                String media = storyObject.getString("media");
                String content = storyObject.getString("content");
                int wordCount = Integer.parseInt(storyObject.getString("wordCount"));
                byte[] img = Base64.decode(media.getBytes(), Base64.DEFAULT);
                storybook.addPage();
                storybook.getPage(i).setLanguageCode(languageCode);
                storybook.getPage(i).setPageNo(pageNo);
                storybook.getPage(i).setMedia(img);
                storybook.getPage(i).setWordCount(wordCount);
                storybook.getPage(i).setContent(content);
                storybook.getPage(i).setStorybookID("");
                storytotalpagecount++;

            }
            etContent.setText(storybook.getPage(0).getContent());
            Bitmap picture = BitmapFactory.decodeByteArray(storybook.getPage(0).getMedia(), 0, storybook.getPage(0).getMedia().length);
            imgvImage.setImageBitmap(picture);
            imgbtnPreviousPage.setVisibility(View.INVISIBLE);
            if (currentpagenumber + 1 == storybook.getLastPageNo()) {
                imgbtnNextPage.setVisibility(View.INVISIBLE);
            } else {
                imgbtnNextPage.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            Log.e("error", e.getMessage().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //success
                tts = new TextToSpeech(this, this);

            } else {
                //failed. install voice data
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop(); //interrupts the current utterance
            tts.shutdown(); //releases the resources
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void checkTTS() {
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            // Change this to match your
            // locale
            for (String word : languageCode.split(" ")) {
                if(selectedLanguage.equals("BM")){
                    tts.setLanguage(new Locale("ms"));
                }else {
                    tts.setLanguage(new Locale(word));
                }
            }

            utteranceProgressListener = new UtteranceProgressListener() {
                @Override
                public void onStart(final String utteranceId) {

                    //Display progress of uttering
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //textViewOutput.append("\n"+ utteranceId);
                            //etContent.setText(utteranceId);
                        }
                    });
                }

                @Override
                public void onDone(String utteranceId) {

                }

                @Override
                public void onError(String utteranceId) {

                }
            };
            tts.setOnUtteranceProgressListener(utteranceProgressListener);

        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    //TODO Download
    public void Download(View v) {
        try {


            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            storybook.setStorybookID(storybookID);
            storybook.setTitle(title);
            storybook.setDesc(storybook.getPage(0).getContent());
            storybook.setLanguage(selectedLanguage);
            storybook.setStatus("for reading only");
            storybook.setAgeGroup(agegroupcode);
            storybook.setDateOfCreation(formattedDate);
            storybook.setType("Reading");
            storybook.setAuthorName(email);
            storybook.setCoverPage(storybook.getPage(0).getMedia());
            storybook.setRating(rating);

            boolean success = myDb.addStorybook(storybook);
            if (success = true) {
                for (int i = 0; i < storytotalpagecount; i++) {
                    Page pagewritten = new Page();
                    pagewritten.setStorybookID(storybookID);
                    pagewritten.setLanguageCode(selectedLanguage);
                    pagewritten.setPageNo(i);
                    pagewritten.setMedia(storybook.getPage(i).getMedia());
                    pagewritten.setWordCount(storybook.getPage(i).getWordCount());
                    pagewritten.setContent(storybook.getPage(i).getContent());
                    storybook.getPage(i).setStorybookID(myDb.getLastStorybookID() + "");
                    pagewritten.setStorybookID(storybook.getPage(i).getStorybookID());
//                            storybookPage.get(i).setStorybookID(myDb.getLastStorybookID() + "");
//                            lastBookNum = storybookPage.get(i).getStorybookID();
//                            pagewritten.setStorybookID(storybookPage.get(i).getStorybookID());
                    myDb.addPage(pagewritten);
                }
                Toast.makeText(LibraryPopupWindowActivity.this, "Add to My Storybooks successfully!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to add storybook", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(LibraryPopupWindowActivity.this, "nothing happen", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("LibraryPopupWindow", e.toString());
        }

    }
}

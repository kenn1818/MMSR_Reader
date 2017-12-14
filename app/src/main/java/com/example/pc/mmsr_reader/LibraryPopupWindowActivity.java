package com.example.pc.mmsr_reader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pc.mmsr_reader.Class.Language;
import com.example.pc.mmsr_reader.Class.Page;
import com.example.pc.mmsr_reader.Class.Reader;
import com.example.pc.mmsr_reader.Class.Storybook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 12/11/2017.
 */

public class LibraryPopupWindowActivity extends Activity{
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
        description = (String) b.get("DESCRIPTION");
        publishdate = (String) b.get("PUBLISHDATE");
        email = (String) b.get("EMAIL");
        agegroupcode = (String) b.get("AGEGROUPCODE");

        etContent = findViewById(R.id.etContent);
        spinnerLanguageCode = findViewById(R.id.spnLanguageCode);
        imgvImage = findViewById(R.id.imgvImageInPopUpWindow);
        //    getSelectedStorybookPages("s00004", "EN");

        ArrayAdapter<String> adapter;
        list = new ArrayList<String>();
        String[] AvailableLanguage = languageCode.split(" ");
        for (int i = 0; i < AvailableLanguage.length; i++) {
            list.add(AvailableLanguage[i]);
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguageCode.setAdapter(adapter);
        spinnerLanguageCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedLanguage = spinnerLanguageCode.getSelectedItem().toString();
                progressDialog.setMessage("Getting storybook content...");
                progressDialog.show();

                getSelectedStorybookPages(storybookID, selectedLanguage);
                getLanguage(selectedLanguage);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
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
}

package com.example.pc.mmsr_reader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.mmsr_reader.Class.Language;
import com.example.pc.mmsr_reader.Class.Page;
import com.example.pc.mmsr_reader.Class.Reader;
import com.example.pc.mmsr_reader.Class.Storybook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyStorybookPopupWindowActivity extends Activity implements TextToSpeech.OnInitListener {
    public static Activity MyStorybookPopupWindowActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentpagenumber=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_storybook_popup_window);
        MyStorybookPopupWindowActivity = this;
        myDb = new DatabaseHandler(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        double width = dm.widthPixels;
        double height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.9));

        checkTTS();


        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        storybookID = (String) b.get("STORYBOOKID");
        storybook = new Storybook();
        storybook = myDb.getSelectedStorybookWithStorybookID(storybookID);
        ArrayList<Page> pages = myDb.getSelectedStorybookPages(storybook.getStorybookID());
        for (int a = 0; a < pages.size(); a++) {
            storybook.addPage();
            storybook.getPage(a).setPageNo(pages.get(a).getPageNo());
            storybook.getPage(a).setContent(pages.get(a).getContent());
            storybook.getPage(a).setWordCount(pages.get(a).getWordCount());
            storybook.getPage(a).setMedia(pages.get(a).getMedia());
        }
        etContent = findViewById(R.id.etContent);
        imgvImage = findViewById(R.id.imgvImageInPopUpWindow);
        imgbtnPreviousPage = (ImageButton) findViewById(R.id.imgbtnPreviousPage);
        imgbtnNextPage = (ImageButton) findViewById(R.id.imgbtnNextPage);

        etContent.setText(storybook.getPage(currentpagenumber).getContent());
        Bitmap picture = BitmapFactory.decodeByteArray(storybook.getPage(currentpagenumber).getMedia(), 0, storybook.getPage(currentpagenumber).getMedia().length);
        imgvImage.setImageBitmap(picture);

        imgbtnPreviousPage.setVisibility(View.INVISIBLE);
        if (currentpagenumber + 1 == storybook.getLastPageNo()) {
            imgbtnNextPage.setVisibility(View.INVISIBLE);
        } else {
            imgbtnNextPage.setVisibility(View.VISIBLE);
        }

        imgbtnPreviousPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentpagenumber > 0) {
                    imgbtnNextPage.setVisibility(View.VISIBLE);
                    etContent.setText(storybook.getPage(currentpagenumber - 1).getContent());
                    Bitmap picture = BitmapFactory.decodeByteArray(storybook.getPage(currentpagenumber - 1).getMedia(), 0, storybook.getPage(currentpagenumber - 1).getMedia().length);
                    imgvImage.setImageBitmap(picture);
                    currentpagenumber--;
                    imgbtnPreviousPage.setVisibility(View.VISIBLE);
                    if (currentpagenumber == 0) {
                        imgbtnPreviousPage.setVisibility(View.INVISIBLE);
                    }
                }
            }

        });
        imgbtnNextPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentpagenumber < storybook.getLastPageNo()) {
                    imgbtnPreviousPage.setVisibility(View.VISIBLE);
                    etContent.setText(storybook.getPage(currentpagenumber + 1).getContent());
                    Bitmap picture = BitmapFactory.decodeByteArray(storybook.getPage(currentpagenumber + 1).getMedia(), 0, storybook.getPage(currentpagenumber + 1).getMedia().length);
                    imgvImage.setImageBitmap(picture);
                    currentpagenumber++;
                    imgbtnNextPage.setVisibility(View.VISIBLE);
                    if (currentpagenumber + 1 == storybook.getLastPageNo()) {
                        imgbtnNextPage.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

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
                        tts.setLanguage(new Locale(storybook.getLanguage()));

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
//            for (String word : languageCode.split(" ")) {
                if(storybook.getLanguage().equals("BM")){
                    tts.setLanguage(new Locale("ms"));
                }
//                else {
//                    tts.setLanguage(new Locale(word));
//                }
//            }

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

}

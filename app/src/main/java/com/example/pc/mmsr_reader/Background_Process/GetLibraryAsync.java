package com.example.pc.mmsr_reader.Background_Process;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pc.mmsr_reader.Class.Storybook;
import com.example.pc.mmsr_reader.Adapter.LibraryAdapter;
import com.example.pc.mmsr_reader.DatabaseHandler;
import com.example.pc.mmsr_reader.LibraryPopupWindowActivity;
import com.example.pc.mmsr_reader.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pc on 11/6/2017.
 */

public class GetLibraryAsync extends AsyncTask<Void, Void, Void> {
    DatabaseHandler mydb;
    Context context;
    ProgressDialog progressDialog;

    //private static final String GET_STORY_URL = "http://tarucmmsr.pe.hu/get_storybook_translate_list.php?title=''&dateOrder=''&titleOrder=''&ageGroup=''&languageCode=''";
    //URL url;

    ArrayList<Storybook> storybooks;
    ListView lvShowStorybook;

    //RecyclerView mRecyclerView;
    //StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    ArrayList<String> ageGroupFilter;

    public GetLibraryAsync(Context context, ListView lvShowStorybook, ArrayList<String> ageGroupFilter) {
        this.context = context;
        //this.mRecyclerView = mRecyclerView;
        //this.mStaggeredGridLayoutManager = mStaggeredGridLayoutManager;
        this.lvShowStorybook = lvShowStorybook;
        progressDialog = new ProgressDialog(context);
        this.ageGroupFilter = ageGroupFilter;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Loading storybooks");
        if (!this.progressDialog.isShowing()) {
            this.progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        LibraryAdapter libraryAdapter = new LibraryAdapter(context, storybooks);
        lvShowStorybook.setAdapter(libraryAdapter);
        //mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        //mRecyclerView.setAdapter(libraryAdapter);
    }

    @Override
    protected Void doInBackground(Void... params) {
        getLibrary();
        return null;
    }

    private void getLibrary() {
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        storybooks = new ArrayList<>();
        //TODO move this url and declare in strings.xml
        String getStoryUrl = "http://tarucmmsr.pe.hu/get_storybook_translate_list.php?";
        mydb= new DatabaseHandler(context);

        JsonArrayRequest request = new JsonArrayRequest(getStoryUrl, new Response.Listener<JSONArray>() {
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
            return;
        }

        try {
            JSONArray arrayStory = response;
            for (int i = 0; i < arrayStory.length(); i++) {
                JSONObject storyObject = (JSONObject) response.get(i);
                String storybookID = storyObject.getString("storybookID");
                String title = storyObject.getString("title");
                String description = storyObject.getString("description");
                String publishDate = storyObject.getString("publishDate");
                String ageGroupCode = storyObject.getString("ageGroupCode");
                String coverPage = storyObject.getString("coverPage");
                String languageCode = storyObject.getString("languageCode");
                String authorName = storyObject.getString("name");
                String rate = storyObject.getString("rating");
                byte[] img = Base64.decode(coverPage.getBytes(), Base64.DEFAULT);
                Storybook storybook = new Storybook(storybookID, title, description, languageCode, ageGroupCode, publishDate, authorName, img, rate);
                storybooks.add(storybook);
                Log.e("here", storybookID);
                Log.e("here", title);
                Log.e("here", description);
                Log.e("here", publishDate);
                Log.e("here", ageGroupCode);
                Log.e("here", languageCode);
                Log.e("here", authorName);
                Log.e("here", rate);
            }
            LibraryAdapter libraryAdapter = new LibraryAdapter(context, storybooks);
            lvShowStorybook.setAdapter(libraryAdapter);

            lvShowStorybook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, LibraryPopupWindowActivity.class);
                    intent.putExtra("STORYBOOKID", storybooks.get(i).getStorybookID());
                    intent.putExtra("TITLE", storybooks.get(i).getTitle());
                    intent.putExtra("LANGUAGECODE", storybooks.get(i).getLanguage());
                    intent.putExtra("DESCRIPTION", storybooks.get(i).getDesc());
                    intent.putExtra("PUBLISHDATE", storybooks.get(i).getDateOfCreation());
                    intent.putExtra("AGEGROUPCODE", storybooks.get(i).getAgeGroup());
                    context.startActivity(intent);
                }
            });

            if(storybooks.size()>0){
                //TODO in order update local DB, must clear the local DB storage
                mydb.deleteExistingRecordInStorybookTable();
                mydb.updateLocalDB(storybooks);
            }else{
                //Display no storybook
                Toast.makeText(context,"No Storybooks",Toast.LENGTH_SHORT).show();
            }
            //final DatabaseHandler mydb = new DatabaseHandler(context);


            //mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
            //mRecyclerView.setAdapter(libraryAdapter);
        } catch (JSONException e) {
            Log.e("error", e.getMessage().toString());
        }
    }



}

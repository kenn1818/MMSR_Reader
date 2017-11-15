package com.example.pc.mmsr_reader;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pc.mmsr_reader.Class.Storybook;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {

    private ArrayList<String> ageGroupFilter = new ArrayList<>();
    //private static final String GET_STORY_URL = "http://tarucmmsr.pe.hu/select_storybook.php/";
    ArrayList<Storybook> storybooks;
    ListView lvShowStorybook;
    //protected RecyclerView mRecyclerView;
    //protected StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ageGroupFilter.add("toddler");
        ageGroupFilter.add("kindergartener");
        ageGroupFilter.add("preschooler");
        ageGroupFilter.add("youngAdult");

        View rootView = inflater.inflate(R.layout.fragment_library, container, false);
        lvShowStorybook = rootView.findViewById(R.id.lvShowStorybook);
        //mRecyclerView = (RecyclerView) rootView.findViewById(R.id.libraryRecyclerView);
        //mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        //MainActivity mainActivity = (MainActivity) getActivity();

        GetLibraryAsync getLibraryAsync = new GetLibraryAsync(this.getContext(), lvShowStorybook, ageGroupFilter);
        getLibraryAsync.execute();

        LibraryAdapter libraryAdapter = new LibraryAdapter(this.getContext(),storybooks);
        lvShowStorybook.setAdapter(libraryAdapter);

        return rootView;
    }
}

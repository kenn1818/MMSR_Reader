package com.example.pc.mmsr_reader.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pc.mmsr_reader.Adapter.LibraryAdapter;
import com.example.pc.mmsr_reader.Class.Storybook;
import com.example.pc.mmsr_reader.DatabaseHandler;
import com.example.pc.mmsr_reader.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyStorybookFragment extends Fragment {

    private ArrayList<String> ageGroupFilter = new ArrayList<>();
    ArrayList<Storybook> storybooks;
    ListView lvShowMyStorybook;

    public MyStorybookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_storybook, container, false);
        lvShowMyStorybook = rootView.findViewById(R.id.lvShowMyStorybook);

        DatabaseHandler mydb = new DatabaseHandler(getContext());
        storybooks = mydb.getAllStorybook();

        LibraryAdapter libraryAdapter = new LibraryAdapter(this.getContext(),storybooks);
        lvShowMyStorybook.setAdapter(libraryAdapter);

        return rootView;
    }

}

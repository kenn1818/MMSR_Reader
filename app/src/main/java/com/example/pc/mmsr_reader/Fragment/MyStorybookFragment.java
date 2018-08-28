package com.example.pc.mmsr_reader.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pc.mmsr_reader.Adapter.LibraryAdapter;
import com.example.pc.mmsr_reader.Adapter.StorybookAdapter;
import com.example.pc.mmsr_reader.Class.Storybook;
import com.example.pc.mmsr_reader.DatabaseHandler;
import com.example.pc.mmsr_reader.LibraryPopupWindowActivity;
import com.example.pc.mmsr_reader.MyStorybookPopupWindowActivity;
import com.example.pc.mmsr_reader.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyStorybookFragment extends Fragment {

    private ArrayList<String> ageGroupFilter = new ArrayList<>();
    ArrayList<Storybook> storybooks;
    ListView lvShowMyStorybook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_storybook, container, false);
        lvShowMyStorybook = rootView.findViewById(R.id.lvShowMyStorybook);

        DatabaseHandler mydb = new DatabaseHandler(getContext());
        storybooks = mydb.getAllStorybook();

        StorybookAdapter storybookAdapter = new StorybookAdapter(this.getContext(),storybooks);
        lvShowMyStorybook.setAdapter(storybookAdapter);

        lvShowMyStorybook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MyStorybookPopupWindowActivity.class);
                intent.putExtra("STORYBOOKID", storybooks.get(i).getStorybookID());
                Log.e("STORYBOOKID", storybooks.get(i).getStorybookID());
                //MyStorybookPopupWindowActivity.currentpagenumber = 0;
                startActivity(intent);
            }
        });

        return rootView;
    }

}

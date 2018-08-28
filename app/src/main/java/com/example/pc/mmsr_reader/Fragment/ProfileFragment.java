package com.example.pc.mmsr_reader.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pc.mmsr_reader.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    TextView tvUserName,tvDob,tvPoints;
    RatingBar rating;
    Button btnHelp, btnHelpStar;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        tvUserName = rootView.findViewById(R.id.tvUserName);
        tvDob = rootView.findViewById(R.id.tvDob);
        tvPoints = rootView.findViewById(R.id.tvPoints);
        rating = rootView.findViewById(R.id.ratingBar);
        btnHelp = rootView.findViewById(R.id.btnHelp);
        btnHelpStar = rootView.findViewById(R.id.btnHelpStar);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userName = sharedPreferences.getString("userName", "");
        String userDOB = sharedPreferences.getString("userDOB", "");
        String points = sharedPreferences.getString("points", "");


        tvUserName.setText(userName);
        tvDob.setText(userDOB);
        tvPoints.setText(points);
        int point=Integer.parseInt(points);
        if(point >=50 && point <=99){
            rating.setRating(1);
        }else if(point >=100 && point <=199){
            rating.setRating(2);
        }else if(point >=200 && point <=399){
            rating.setRating(3);
        }else if(point >=400 && point <=799){
            rating.setRating(4);
        }else if(point >=800){
            rating.setRating(5);
        }

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.layout_for_fragment, new PointsFragment());
                ft.commit();
            }
        });

        btnHelpStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.layout_for_fragment, new StarFragment());
                ft.commit();
            }
        });

        return rootView;

    }

}

package com.example.words;

import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Map;


public class rightFragment extends Fragment {
    View view;
    public rightFragment() {

    }

    public void getdata(ArrayList<Map<String, String>> items){

        ListView l=(ListView)this.view.findViewById(R.id.v2);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),items, R.layout.item2,
                new String[]{words.Word.M,words.Word.L},
                new int[]{R.id.i3,R.id.w3});
        l.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_right, container, false);
        return view;
    }
}
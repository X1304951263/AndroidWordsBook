package com.example.words;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle intent=getIntent().getExtras();
        ArrayList< Map<String, String>> items= (ArrayList<Map<String, String>>) intent.getSerializable("name");
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item,
                new String[]{words.Word._ID,words.Word.W, words.Word.M, words.Word.L},
                new int[]{R.id.i1,R.id.w1, R.id.y1, R.id.l1});
        ListView list = (ListView) findViewById(R.id.lview1);
        list.setAdapter(adapter);
    }
}
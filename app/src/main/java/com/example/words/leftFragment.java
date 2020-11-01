package com.example.words;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class leftFragment extends Fragment {
    public interface click{
        void onClick(ArrayList<Map<String, String>> items);
    }

    ListView lv ;
    ArrayList<Map<String, String>> items=new ArrayList<Map<String,String>>();
    SimpleAdapter adapter;
    WordsDBHelper data;
    private click c;

    public leftFragment() {
    }
    public void onAttach(Context context) {

        super.onAttach(context);
        c=(click)context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_left, container, false);
        lv=(ListView)view.findViewById(R.id.v1);
        registerForContextMenu(lv);
        adapter = new SimpleAdapter(getActivity(), items, R.layout.item1,
                new String[]{words.Word._ID,words.Word.W},
                new int[]{R.id.i2,R.id.w2});
        getData();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text=(TextView)view.findViewById(R.id.i2);
                String strid=text.getText().toString();
                ArrayList<Map<String, String>> items=new ArrayList<Map<String, String>>();
                items=SearchUseSql(strid);
                c.onClick(items);
            }
        });
        return view;
    }
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    private void getData() {
        data=new WordsDBHelper(getContext(),"words",1);
        Cursor cursor = data.getWritableDatabase().query(words.Word.T,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            Map<String,String> item=new HashMap<>();
            item.put(words.Word._ID,cursor.getString(cursor.getColumnIndex(words.Word._ID)));
            item.put("name",cursor.getString(cursor.getColumnIndex(words.Word.W)));
            items.add(item);
        }
    }

    private ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = data.getReadableDatabase();
        String sql="select * from b where _id =?";
        Cursor c=db.rawQuery(sql,new String[]{strWordSearch} );
        return ConvertCursor2List(c);
    }
    private ArrayList<Map<String, String>> ConvertCursor2List(Cursor cursor){
        ArrayList<Map<String, String>> items=new ArrayList<Map<String,String>>();;
        while (cursor.moveToNext()){
            Map<String,String> item=new HashMap<>();
            item.put("hanyi",cursor.getString(cursor.getColumnIndex(words.Word.M)));
            item.put("example",cursor.getString(cursor.getColumnIndex(words.Word.L)));
            items.add(item);
        }
        return items;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        this.getActivity().getMenuInflater().inflate(R.menu.listmenu, menu);
    }
    public boolean onContextItemSelected( MenuItem item) {
        TextView textId=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        switch (item.getItemId()){
            //删除
            case R.id.del:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.i2);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    DeleteDialog1(strId);
                }
                break;
            //修改
           /* case R.id.change:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.i1);
                final String strId=textId.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View viewDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.gai, null, false);
                builder.setTitle("修改单词")
                        .setView(viewDialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String strNewWord = ((EditText)viewDialog.findViewById(R.id.word1)).getText().toString();
                                String strNewMeaning = ((EditText)viewDialog. findViewById(R.id.hanyi1)).getText().toString();
                                String strNewSample = ((EditText)viewDialog. findViewById(R.id.liju1)).getText().toString();
                                UpdateUseSql(strId, strNewWord, strNewMeaning, strNewSample);
                                setWordsListView(getAll());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                break;*/
        }
        return true;
    }

    public void DeleteDialog1(final String strId){
        new AlertDialog.Builder(getActivity())
                .setTitle("删除单词").setMessage("是否真的删除单词?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteUseSql(strId);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    //使用Sql语句删除单词
    public void DeleteUseSql(String strId) {
        String sql="delete from b where _id='"+strId+"'";
        SQLiteDatabase db = data.getReadableDatabase();
        db.execSQL(sql);
    }

}
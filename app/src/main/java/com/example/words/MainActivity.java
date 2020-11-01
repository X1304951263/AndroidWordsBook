package com.example.words;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements leftFragment.click{
    WordsDBHelper data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.main_land);

        }else {
            setContentView(R.layout.activity_main);
            ListView list=(ListView)findViewById(R.id.lview);
            registerForContextMenu(list);
            data=new WordsDBHelper(getApplicationContext(),"words",1);
            ArrayList<Map<String, String>> items=getAll();
            setWordsListView(items);
        }

    }
    public void onClick(ArrayList<Map<String, String>> items) {
        rightFragment r=(rightFragment)getSupportFragmentManager().findFragmentById(R.id.f2);
        r.getdata(items);
    }
    private ArrayList<Map<String, String>> getAll(){
        ArrayList<Map<String, String>> items=new ArrayList<Map<String,String>>();
        Cursor cursor = data.getWritableDatabase().query(words.Word.T,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            Map<String,String> item=new HashMap<>();
            item.put(words.Word._ID,cursor.getString(cursor.getColumnIndex(words.Word._ID)));
            item.put("name",cursor.getString(cursor.getColumnIndex(words.Word.W)));
            item.put("hanyi",cursor.getString(cursor.getColumnIndex(words.Word.M)));
            item.put("example",cursor.getString(cursor.getColumnIndex(words.Word.L)));
            items.add(item);
        }
        return items;
    }
    //为listview创建适配器
    private void setWordsListView(ArrayList<Map<String, String>> items){
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item,
                new String[]{words.Word._ID,words.Word.W, words.Word.M, words.Word.L},
                new int[]{R.id.i1,R.id.w1, R.id.y1, R.id.l1});
        ListView list = (ListView) findViewById(R.id.lview);
        list.setAdapter(adapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.caidan, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.cha:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                final View viewDialog1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.zhao, null, false);
                builder1.setTitle("查找单词")
                        .setView(viewDialog1)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String txtSearchWord=((EditText)viewDialog1.findViewById(R.id.zhao)).getText().toString();
                                ArrayList<Map<String, String>> items=null;
                                items=SearchUseSql(txtSearchWord);
                                if(items.size()>0) {
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("name",items);
                                    Intent intent=new Intent(MainActivity.this,search.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }else
                                    Toast.makeText(MainActivity.this,"没有找到",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder1.create().show();
                break;
            case R.id.zeng:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View viewDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.zeng, null, false);
                builder.setTitle("新增单词")
                        .setView(viewDialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String strWord=((EditText) viewDialog.findViewById(R.id.word)).getText().toString();
                                String strMeaning=((EditText)viewDialog.findViewById(R.id.hanyi)).getText().toString();
                                String strSample=((EditText)viewDialog.findViewById(R.id.liju)).getText().toString();
                                Insert(strWord, strMeaning, strSample);
                                ArrayList<Map<String, String>> items=getAll();
                                setWordsListView(items);
                        }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                break;
            case R.id.help:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                final View viewDialog3 = LayoutInflater.from(MainActivity.this).inflate(R.layout.helpdialog, null, false);
                builder3.setTitle("帮助框")
                        .setView(viewDialog3)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder3.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.listmenu, menu);
    }
    public boolean onContextItemSelected( MenuItem item) {
        TextView textId=null;
        TextView textWord=null;
        TextView textMeaning=null;
        TextView textSample=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        switch (item.getItemId()){
            //删除
            case R.id.del:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.i1);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    DeleteDialog(strId);
                }
                break;
          //修改
            case R.id.change:
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
                break;
        }
        return true;
    }

    private void DeleteDialog(final String strId){
        new AlertDialog.Builder(this)
                .setTitle("删除单词").setMessage("是否真的删除单词?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                                 DeleteUseSql(strId);
                                 setWordsListView(getAll());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialogInterface, int i) {

                         }
                }).create().show();
    }

            //使用insert方法增加单词
            private void Insert(String strWord, String strMeaning, String strSample) {
                SQLiteDatabase db = data.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(words.Word.W, strWord);
                values.put(words.Word.M, strMeaning);
                values.put(words.Word.L, strSample);
                long newRowId;
                newRowId = db.insert(words.Word.T,null, values);
            }

            //使用Sql语句删除单词
            private void DeleteUseSql(String strId) {
                String sql="delete from b where _id='"+strId+"'";
                SQLiteDatabase db = data.getReadableDatabase();
                db.execSQL(sql);
            }

            //使用Sql语句更新单词
            private void UpdateUseSql(String strId,String strWord, String strMeaning, String strSample) {
                SQLiteDatabase db = data.getReadableDatabase();
                String sql="update b set name=?,hanyi=?,example=? where _id=?";
                db.execSQL(sql, new String[]{strWord, strMeaning, strSample,strId});
            }

            //使用Sql语句查找
    private ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = data.getReadableDatabase();
        String sql="select * from b where name like ? order by name desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+strWordSearch+"%"});
        return ConvertCursor2List(c);
    }
    private ArrayList<Map<String, String>> ConvertCursor2List(Cursor cursor){
        ArrayList<Map<String, String>> items=new ArrayList<Map<String,String>>();;
        while (cursor.moveToNext()){
            Map<String,String> item=new HashMap<>();
            item.put(words.Word._ID,cursor.getString(cursor.getColumnIndex(words.Word._ID)));
            item.put("name",cursor.getString(cursor.getColumnIndex(words.Word.W)));
            item.put("hanyi",cursor.getString(cursor.getColumnIndex(words.Word.M)));
            item.put("example",cursor.getString(cursor.getColumnIndex(words.Word.L)));
            items.add(item);
        }
        return items;
    }


}
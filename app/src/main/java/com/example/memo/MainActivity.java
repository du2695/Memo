package com.example.memo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.memo.R.id.list;

public class MainActivity extends AppCompatActivity {

    // ListView　用アダプタ
    SimpleAdapter mAdapter = null;
    // ListView に設定するデータ
    List<Map<String, String>> mList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ListView用のアダプタのリストを生成
        mList = new ArrayList<>();

        //ListView用アダプタ生成
        mAdapter = new SimpleAdapter(
                this,
                mList,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "filename1"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        //ListViewにアダプタをセット
        ListView List = (ListView) findViewById(list);

        //ListViewが空ならテキストを表示
        List.setEmptyView(findViewById(R.id.empty_list));

        List.setAdapter(mAdapter);

        //ListViewのアイテム選択イベント
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                        AdapterView<?> parent, View view, int pos, long id) {
                    //詳細画面に渡すデータをセットし、表示
                    Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                    intent.putExtra("NAME", mList.get(pos).get("filename"));
                    intent.putExtra("TITLE", mList.get(pos).get("title"));
                    intent.putExtra("CONTENT", mList.get(pos).get("content"));
                    startActivity(intent);
                }
        });

        //ListViewをコンテキストメニューに登録
        registerForContextMenu(List);
    }




    @Override
    protected void onResume(){
        super.onResume();

        //ListView用アダプタのデータをクリア
        mList.clear();

        // アプリの保存フォルダ内のファイル一覧を取得
        String savePath = this.getFilesDir().getPath();
        File[] files = new File(savePath).listFiles();

        //ファイル名を降順でソート
        Arrays.sort(files, Collections.reverseOrder());

        //テキストファイルを取得、ListView用アダプタのリストにセット
        for (File name : files ){
            String fileName = name.getName();
            if (name.isFile() && fileName.endsWith(".txt")){
                String title = null;
                String content = null;
                String filename ;
                //ファイル読み込み
                try{
                    //ファイルオープン
                    InputStream in = this.openFileInput(fileName);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                    char[] buf = new char[(int)name.length()];
                    //タイトル読み込み
                    title = reader.readLine();
                    //内容読み込み
                    int num = reader.read(buf);
                    content = new String(buf,0,num);
                    //ファイルクローズ
                    reader.close();
                    in.close();
                }catch(Exception e){
                    Toast.makeText(this,"File read error!",Toast.LENGTH_LONG).show();
                }
                // ListView用のアダプタにデータをセット
                Map<String, String> map = new HashMap<>();
                filename = fileName.substring(0,8);
                map.put("filename1", filename);
                map.put("filename",fileName);
                map.put("title", title);
                map.put("content", content);
                mList.add(map);
            }

        }
        //ListViewのデータ変更を表示に反映
        mAdapter.notifyDataSetChanged();
    }

    // メニュー作成処理
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    // メニュー選択処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_add:
                Intent intent=new Intent(MainActivity.this,NewActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // コンテキストメニュー作成処理

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        super.onCreateContextMenu(menu, view, info);
        getMenuInflater().inflate(R.menu.main_context, menu);
    }

    // コンテキストメニュー選択処理
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_edit:
                //[編集]選択時の処理
                //編集画面へ遷移
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("NAME",mList.get(info.position).get("filename"));
                intent.putExtra("TITLE",mList.get(info.position).get("title"));
                intent.putExtra("CONTENT",mList.get(info.position).get("content"));
                startActivity(intent);
                break;

            case R.id.context_delete:
                // [削除] 選択時の処理
                // ファイル削除
                if (this.deleteFile(mList.get(info.position).get("filename"))) {
                    Toast.makeText(this, R.string.msg_del, Toast.LENGTH_SHORT).show();
                }
                // リストから削除
                mList.remove(info.position);
                // ListView のデータ変更を表示に反映
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

        return false;
    }
}

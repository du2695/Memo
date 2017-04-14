package com.example.memo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ShowActivity extends Activity {

    // 保存ファイル名
    String mFileName = "";
    // 保存なしフラグ
    boolean mNotSave = false;

    String name="";
    String title="";
    String content="";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show);

        TextView TextContent = (TextView) findViewById(R.id.TextContent) ;

        // メイン画面からの情報受け取り、TextViewに設定
        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        mFileName = name;

        title = intent.getStringExtra("TITLE");
        content = intent.getStringExtra("CONTENT");

        TextContent.setText(content);

        setTitle(title);
    }

    //メニュー作成処理
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.show,menu);
        return true;
    }

    //メニュー選択処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            //編集画面へ
            case R.id.action_edit:
                Intent intent = new Intent(ShowActivity.this,EditActivity.class);
                intent.putExtra("NAME",name);
                intent.putExtra("TITLE",title);
                intent.putExtra("CONTENT",content);
                startActivity(intent);
                break;

            //削除
            case R.id.action_delete:
                this.deleteFile(mFileName);
                mNotSave=true;
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}

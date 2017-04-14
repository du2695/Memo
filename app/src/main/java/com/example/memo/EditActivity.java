package com.example.memo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class EditActivity extends Activity {

    // 保存ファイル名
    String mFileName = "";
    // 保存なしフラグ
    boolean mNotSave = false;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        // タイトルと内容入力用の EditText を取得
        EditText TextTitle = (EditText)findViewById(R.id.EditTextTitle);
        EditText TextContent = (EditText)findViewById(R.id.EditTextContent);

        // 詳細画面からの情報受け取り、EditTextに設定
        Intent intent = getIntent();

        mFileName = intent.getStringExtra("NAME");
        TextTitle.setText(intent.getStringExtra("TITLE"));
        TextContent.setText(intent.getStringExtra("CONTENT"));

    }

    //メニュー作成処理
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit,menu);
        return true;
    }

    //メニュー選択処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_delete:
                if (!mFileName.isEmpty()){
                    this.deleteFile(mFileName);
                }
                mNotSave=true;
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //保存
    @Override
    protected void onPause() {
        super.onPause();

        if (mNotSave) {
            return;
        }

        EditText TextTitle = (EditText) findViewById(R.id.EditTextTitle);
        EditText TextContent = (EditText) findViewById(R.id.EditTextContent);
        String title = TextTitle.getText().toString();
        String content = TextContent.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, R.string.msg_destruction, Toast.LENGTH_SHORT).show();
        }

        else {
            OutputStream out;
            PrintWriter writer;
            try {
                out = this.openFileOutput(mFileName, Context.MODE_PRIVATE);
                writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));

                writer.println(title);

                writer.print(content);
                writer.close();
                out.close();

            } catch (Exception e) {
                Toast.makeText(this, "File save error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    //ボタン選択処理(保存、画面遷移)
    public void onClickButton(View v){

        EditText TextTitle = (EditText) findViewById(R.id.EditTextTitle);
        EditText TextContent = (EditText) findViewById(R.id.EditTextContent);
        String title = TextTitle.getText().toString();
        String content = TextContent.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(EditActivity.this, R.string.msg_destruction, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(EditActivity.this,MainActivity.class);
            startActivity(intent);

        }

        else {
            OutputStream out;
            PrintWriter writer;
            try {
                out = EditActivity.this.openFileOutput(mFileName, Context.MODE_PRIVATE);
                writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));

                writer.println(title);

                writer.print(content);
                writer.close();
                out.close();

                mNotSave = true;
                Intent intent = new Intent(EditActivity.this,MainActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(EditActivity.this, "File save error!", Toast.LENGTH_LONG).show();
            }
        }
    }

}

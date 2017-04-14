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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewActivity extends Activity {

    String mFileName = "";
    boolean mNotSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
    }
    //メニュー作成処理
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    //メニュー選択処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                mNotSave = true;
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();

        // [削除] で画面を閉じるとき、完了ボタンで保存されたときは、保存しない
        if (mNotSave) {
            return;
        }

        // タイトル、内容を取得
        EditText TextTitle = (EditText) findViewById(R.id.NewTextTitle);
        EditText TextContent = (EditText) findViewById(R.id.NewTextContent);
        String title = TextTitle.getText().toString();
        String content = TextContent.getText().toString();

        // タイトル、内容が空白の場合、保存しない
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, R.string.msg_destruction, Toast.LENGTH_SHORT).show();
        } else {
            // ファイル名を生成  ファイル名 ： yyyyMMdd_HHmmssSSS.txt

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.JAPAN);
            mFileName = sdf.format(date) + ".txt";

            // 保存
            OutputStream out;
            PrintWriter writer;
            try {
                out = this.openFileOutput(mFileName, Context.MODE_PRIVATE);
                writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
                // タイトル書き込み
                writer.println(title);
                // 内容書き込み
                writer.print(content);
                writer.close();
                out.close();
            } catch (Exception e) {
                Toast.makeText(this, "File save error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    //ボタン選択処理(保存、画面遷移)
    public void onClickButton1(View v) {

        // タイトル、内容を取得
        EditText TextTitle = (EditText) findViewById(R.id.NewTextTitle);
        EditText TextContent = (EditText) findViewById(R.id.NewTextContent);
        String title = TextTitle.getText().toString();
        String content = TextContent.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(NewActivity.this, R.string.msg_destruction, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(NewActivity.this, MainActivity.class);
            startActivity(intent);

        } else {

            // ファイル名を生成  ファイル名 ： yyyyMMdd_HHmmssSSS.txt

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.JAPAN);
            mFileName = sdf.format(date) + ".txt";

            OutputStream out;
            PrintWriter writer;
            try {
                out = NewActivity.this.openFileOutput(mFileName, Context.MODE_PRIVATE);
                writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));

                writer.println(title);
                writer.print(content);
                writer.close();
                out.close();
                mNotSave = true;

                Intent intent = new Intent(NewActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(NewActivity.this, "File save error!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

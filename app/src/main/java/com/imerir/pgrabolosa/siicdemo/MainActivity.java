package com.imerir.pgrabolosa.siicdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imerir.pgrabolosa.siicdemo.Model.DataManager;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    EditText mInputText;
    TextView mOutputText;
    Button mButton;
    Button mButtonHistory;
    Button mButtonReset;

    EditText mComment;
    Button mCommentSave;

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataManager.shared.loadRates();

        mInputText = (EditText) findViewById(R.id.inputText);
        mOutputText = (TextView) findViewById(R.id.outputText);
        mComment = (EditText) findViewById(R.id.comment);
        mCommentSave = (Button) findViewById(R.id.commentSave);
        mButton = (Button) findViewById(R.id.button);
        mButtonReset = (Button) findViewById(R.id.reset);
        mButtonHistory = (Button) findViewById(R.id.history);

        mPrefs = getSharedPreferences("default", MODE_PRIVATE);

        float val = mPrefs.getFloat("lastestInput", 0);
        mInputText.setText("" + val);
        convert(val, false);

        mComment.setText(loadText("comment.txt"));

        mButton.setOnClickListener(this);
        mButtonHistory.setOnClickListener(this);
        mButtonReset.setOnClickListener(this);
        mCommentSave.setOnClickListener(this);
    }

    private void convert(float value) {
        convert(value, true);
    }

    private void convert(float value, boolean saveValue) {
        try {
            float result = DataManager.shared.convertToEur(value, saveValue);
            mOutputText.setText("" + result);
            mPrefs.edit().putFloat("lastestInput", result).commit();
        } catch (Exception e) {
            Toast.makeText(this, "Not yet ready.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveText(String filename, String text) {
        try(FileOutputStream fileOutput = openFileOutput(filename, MODE_PRIVATE)) {
            fileOutput.write(text.getBytes());
            fileOutput.close();
        } catch (Exception e) {
            return; // fails silently
        }
    }

    private String loadText(String filename) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(filename)))) {
            return br.lines().collect(Collectors.joining());
        } catch (Exception e) {
            return ""; // unexpected error - fails silently
        }
    }

    @Override
    public void onClick(View sender) {
        switch(sender.getId()) {
            case R.id.button:
                String valueStr = mInputText.getText().toString();
                float value = Float.parseFloat(valueStr);
                convert(value);
                break;
            case R.id.reset:
                mInputText.setText("");
                mOutputText.setText("");
                break;
            case R.id.history:
                Intent next = new Intent(this, LogActivity.class);
                startActivity(next);
                break;
            case R.id.commentSave:
                saveText("comment.txt", mComment.getText().toString());
                break;
        }
    }
}

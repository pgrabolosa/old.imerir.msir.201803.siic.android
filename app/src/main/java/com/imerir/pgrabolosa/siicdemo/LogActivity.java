package com.imerir.pgrabolosa.siicdemo;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.imerir.pgrabolosa.siicdemo.Model.DataManager;

import java.util.Arrays;

public class LogActivity extends AppCompatActivity {

    ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mList = findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1) {
            @Override
            public int getCount() {
                return DataManager.shared.conversions.size();
            }

            @Nullable
            @Override
            public String getItem(int position) {
                return DataManager.shared.conversions.get(position)
                        .describe("USD Dollars => EUR Euros", "USD", "EUR");
            }
        };

        mList.setAdapter(adapter);
    }
}

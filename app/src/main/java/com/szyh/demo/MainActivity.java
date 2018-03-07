package com.szyh.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private String[] mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatas = getResources().getStringArray(R.array.ewaa_array);
        listView = (ListView) findViewById(R.id.activity_ewaa_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                mDatas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, SerialPortActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, AudioActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, WebSocketActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, FaceActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, RobotOperateActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, RobotStatusActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, OtherInfoActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
}

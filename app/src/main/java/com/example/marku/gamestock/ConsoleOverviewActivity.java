package com.example.marku.gamestock;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.marku.gamestock.data.ConsoleContract.PcEntry;
import com.example.marku.gamestock.data.ConsoleContract.PlaystationEntry;
import com.example.marku.gamestock.data.ConsoleContract.SwitchEntry;
import com.example.marku.gamestock.data.ConsoleContract.XboxEntry;

public class ConsoleOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console_overview);

        GridView grid = (GridView) findViewById(R.id.gridview);
        grid.setAdapter(new LogoAdapter(this));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ConsoleOverviewActivity.this, GameCatalogActivity.class);
                Uri uri;
                switch (i) {
                    case 0:
                        uri = PcEntry.CONTENT_URI;
                        break;
                    case 1:
                        uri = XboxEntry.CONTENT_URI;
                        break;
                    case 2:
                        uri = PlaystationEntry.CONTENT_URI;
                        break;
                    case 3:
                        uri = SwitchEntry.CONTENT_URI;
                        break;
                    default:
                        uri = null;

                }
                intent.setData(uri);
                intent.putExtra("Origin Activity ConsoleOverView", true);
                startActivity(intent);
            }
        });
    }
}

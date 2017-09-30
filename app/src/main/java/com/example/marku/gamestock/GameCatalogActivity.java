package com.example.marku.gamestock;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marku.gamestock.data.ConsoleContract.PcEntry;
import com.example.marku.gamestock.data.ConsoleContract.PlaystationEntry;
import com.example.marku.gamestock.data.ConsoleContract.SwitchEntry;
import com.example.marku.gamestock.data.ConsoleContract.XboxEntry;

import static com.example.marku.gamestock.R.id.count;


public class GameCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static Uri currentUri;
    private TextView mCountTextView;
    private GameCursorAdapter gameAdapter;
    public static final int GAME_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            currentUri = intent.getData();
        }

        if (currentUri.equals(PcEntry.CONTENT_URI)) {
            setTitle(R.string.pc_activity_title);
            setTheme(R.style.PcTheme);
        } else if (currentUri.equals(XboxEntry.CONTENT_URI)) {
            setTitle(R.string.xbox_activity_title);
            setTheme(R.style.XboxTheme);
        } else if (currentUri.equals(PlaystationEntry.CONTENT_URI)) {
            setTitle(R.string.playstation_activity_title);
            setTheme(R.style.PlaystationTheme);
        } else if (currentUri.equals(SwitchEntry.CONTENT_URI)) {
            setTitle(R.string.switch_activity_title);
            setTheme(R.style.SwitchTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_catalog);

        mCountTextView = (TextView) findViewById(count);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameCatalogActivity.this, EditorActivity.class);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });

        ListView gameItems = (ListView) findViewById(R.id.list);

        gameItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GameCatalogActivity.this, EditorActivity.class);
                Uri uri = ContentUris.withAppendedId(currentUri, l);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        View emptyView = findViewById(R.id.empty_view);
        gameItems.setEmptyView(emptyView);

        gameAdapter = new GameCursorAdapter(this, null);
        gameItems.setAdapter(gameAdapter);
        getLoaderManager().initLoader(GAME_LOADER, null, this);
    }

    public void clickOnSale(long id, int gameCount) {

        ContentValues values = new ContentValues();

        if (gameCount > 0) {
            int count = gameCount - 1;

            if (currentUri.equals(PcEntry.CONTENT_URI)) {
                values.put(PcEntry.COLUMN_GAME_COUNT, count);
            } else if (currentUri.equals(XboxEntry.CONTENT_URI)) {
                values.put(XboxEntry.COLUMN_GAME_COUNT, count);
            } else if (currentUri.equals(PlaystationEntry.CONTENT_URI)) {
                values.put(PlaystationEntry.COLUMN_GAME_COUNT, count);
            } else if (currentUri.equals(SwitchEntry.CONTENT_URI)) {
                values.put(SwitchEntry.COLUMN_GAME_COUNT, count);
            }
            Uri uriWithId = ContentUris.withAppendedId(currentUri, id);
            getContentResolver().update(uriWithId, values, null, null);

            gameAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_entries:
                showDeleteAllConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg_2);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteGames();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteGames() {
        int rowsDeleted = getContentResolver().delete(currentUri, null, null);
        if (rowsDeleted == 0) {
            Toast.makeText(this, getString(R.string.delete_games_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.delete_games_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection;
        if (currentUri.equals(PcEntry.CONTENT_URI)) {
            projection = new String[]{
                    PcEntry._ID,
                    PcEntry.COLUMN_GAME_NAME,
                    PcEntry.COLUMN_GAME_PRICE,
                    PcEntry.COLUMN_GAME_COUNT,
                    PcEntry.COLUMN_GAME_IMAGE
            };
        } else if (currentUri.equals(XboxEntry.CONTENT_URI)) {
            projection = new String[]{
                    XboxEntry._ID,
                    XboxEntry.COLUMN_GAME_NAME,
                    XboxEntry.COLUMN_GAME_PRICE,
                    XboxEntry.COLUMN_GAME_COUNT,
                    XboxEntry.COLUMN_GAME_IMAGE
            };
        } else if (currentUri.equals(PlaystationEntry.CONTENT_URI)) {
            projection = new String[]{
                    PlaystationEntry._ID,
                    PlaystationEntry.COLUMN_GAME_NAME,
                    PlaystationEntry.COLUMN_GAME_PRICE,
                    PlaystationEntry.COLUMN_GAME_COUNT,
                    PlaystationEntry.COLUMN_GAME_IMAGE
            };
        } else if (currentUri.equals(SwitchEntry.CONTENT_URI)) {
            projection = new String[]{
                    SwitchEntry._ID,
                    SwitchEntry.COLUMN_GAME_NAME,
                    SwitchEntry.COLUMN_GAME_PRICE,
                    SwitchEntry.COLUMN_GAME_COUNT,
                    SwitchEntry.COLUMN_GAME_IMAGE
            };
        } else {
            projection = null;
        }
        return new CursorLoader(this, currentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        gameAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        gameAdapter.swapCursor(null);
    }
}

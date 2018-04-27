package com.example.marku.gamestock;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marku.gamestock.data.ConsoleContract;
import com.example.marku.gamestock.data.ConsoleContract.PcEntry;
import com.example.marku.gamestock.data.ConsoleContract.PlaystationEntry;
import com.example.marku.gamestock.data.ConsoleContract.SwitchEntry;
import com.example.marku.gamestock.data.ConsoleContract.XboxEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int EXISTING_GAME_LOADER = 1;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Uri currentUri;
    private Uri imageUri;

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private TextView mCountTextView;
    private Button mButtonMinus;
    private Button mButtonPlus;
    private Button mImageButton;
    private ImageView mImageView;

    private static final int PC = 100;
    private static final int PC_ID = 101;
    private static final int XBOX = 200;
    private static final int XBOX_ID = 201;
    private static final int PLAYSTATION = 300;
    private static final int PLAYSTATION_ID = 301;
    private static final int SWITCH = 400;
    private static final int SWITCH_ID = 401;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ConsoleContract.CONTENT_AUTHORITY, ConsoleContract.PATH_PC, PC);
        sUriMatcher.addURI(ConsoleContract.CONTENT_AUTHORITY, ConsoleContract.PATH_PC + "/#", PC_ID);
        sUriMatcher.addURI(ConsoleContract.CONTENT_AUTHORITY, ConsoleContract.PATH_XBOX, XBOX);
        sUriMatcher.addURI(ConsoleContract.CONTENT_AUTHORITY, ConsoleContract.PATH_XBOX + "/#", XBOX_ID);
        sUriMatcher.addURI(ConsoleContract.CONTENT_AUTHORITY, ConsoleContract.PATH_PLAYSTATION, PLAYSTATION);
        sUriMatcher.addURI(ConsoleContract.CONTENT_AUTHORITY, ConsoleContract.PATH_PLAYSTATION + "/#", PLAYSTATION_ID);
        sUriMatcher.addURI(ConsoleContract.CONTENT_AUTHORITY, ConsoleContract.PATH_SWITCH, SWITCH);
        sUriMatcher.addURI(ConsoleContract.CONTENT_AUTHORITY, ConsoleContract.PATH_SWITCH + "/#", SWITCH_ID);
    }

    private boolean mGameHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mGameHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        currentUri = intent.getData();

        int match = sUriMatcher.match(currentUri);

        //Set activity theme regarding of the URI
        switch (match) {
            case PC:
            case PC_ID:
                setTheme(R.style.PcTheme);
                break;
            case XBOX:
            case XBOX_ID:
                setTheme(R.style.XboxTheme);
                break;
            case PLAYSTATION:
            case PLAYSTATION_ID:
                setTheme(R.style.PlaystationTheme);
                break;
            case SWITCH:
            case SWITCH_ID:
                setTheme(R.style.SwitchTheme);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + currentUri);
        }

        if (currentUri.equals(PcEntry.CONTENT_URI) || currentUri.equals(XboxEntry.CONTENT_URI) ||
                currentUri.equals(PlaystationEntry.CONTENT_URI) || currentUri.equals(SwitchEntry.CONTENT_URI)) {
            setTitle(R.string.editor_activity_title_new_game);
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.editor_activity_title_edit_game);
            getLoaderManager().initLoader(EXISTING_GAME_LOADER, null, this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mNameEditText = (EditText) findViewById(R.id.edit_game_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_game_price);
        mCountTextView = (TextView) findViewById(R.id.edit_game_count);
        mButtonMinus = (Button) findViewById(R.id.button_game_count_minus);
        mButtonPlus = (Button) findViewById(R.id.button_game_count_plus);
        mImageButton = (Button) findViewById(R.id.select_image);
        mImageView = (ImageView) findViewById(R.id.image_view);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mButtonMinus.setOnTouchListener(mTouchListener);
        mButtonPlus.setOnTouchListener(mTouchListener);
        mImageButton.setOnTouchListener(mTouchListener);

        mButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(mCountTextView.getText().toString());
                count += 1;
                mCountTextView.setText(String.valueOf(count));
            }
        });
        mButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(mCountTextView.getText().toString());
                if (count > 0) {
                    count -= 1;
                }
                mCountTextView.setText(String.valueOf(count));
            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToOpenImageSelector();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveGame();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_order:
                String text = "";
                if (mNameEditText.getText() != null) {
                    text = mNameEditText.getText().toString();
                }
                text += ".";
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.game_order_subject));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.game_order) + " " + text);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
            case android.R.id.home:
                if (!mGameHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentUri.equals(PcEntry.CONTENT_URI) || currentUri.equals(XboxEntry.CONTENT_URI) ||
                currentUri.equals(PlaystationEntry.CONTENT_URI) || currentUri.equals(SwitchEntry.CONTENT_URI)) {
            MenuItem menuItemDelete = menu.findItem(R.id.action_delete);
            menuItemDelete.setVisible(false);
            MenuItem menuItemOrder = menu.findItem(R.id.action_order);
            menuItemOrder.setVisible(false);
        }
        return true;
    }

    private void saveGame() {
        //Check if all required inputs were made
        if (TextUtils.isEmpty(mNameEditText.getText()) ||
                TextUtils.isEmpty(mPriceEditText.getText()) ||
                TextUtils.isEmpty(mCountTextView.getText()) ||
                imageUri == null) {
            Toast.makeText(this, getString(R.string.missing_input),
                    Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            int match = sUriMatcher.match(currentUri);

            switch (match) {
                case PC:
                case PC_ID:
                    values.put(PcEntry.COLUMN_GAME_NAME, mNameEditText.getText().toString().trim());
                    values.put(PcEntry.COLUMN_GAME_PRICE, mPriceEditText.getText().toString().trim());
                    values.put(PcEntry.COLUMN_GAME_COUNT, Integer.parseInt(mCountTextView.getText().toString()));
                    values.put(PcEntry.COLUMN_GAME_IMAGE, imageUri.toString());
                    break;

                case XBOX:
                case XBOX_ID:
                    values.put(XboxEntry.COLUMN_GAME_NAME, mNameEditText.getText().toString().trim());
                    values.put(XboxEntry.COLUMN_GAME_PRICE, mPriceEditText.getText().toString().trim());
                    values.put(XboxEntry.COLUMN_GAME_COUNT, Integer.parseInt(mCountTextView.getText().toString()));
                    values.put(XboxEntry.COLUMN_GAME_IMAGE, imageUri.toString());
                    break;

                case PLAYSTATION:
                case PLAYSTATION_ID:
                    values.put(PlaystationEntry.COLUMN_GAME_NAME, mNameEditText.getText().toString().trim());
                    values.put(PlaystationEntry.COLUMN_GAME_PRICE, mPriceEditText.getText().toString().trim());
                    values.put(PlaystationEntry.COLUMN_GAME_COUNT, Integer.parseInt(mCountTextView.getText().toString()));
                    values.put(PlaystationEntry.COLUMN_GAME_IMAGE, imageUri.toString());
                    break;

                case SWITCH:
                case SWITCH_ID:
                    values.put(SwitchEntry.COLUMN_GAME_NAME, mNameEditText.getText().toString().trim());
                    values.put(SwitchEntry.COLUMN_GAME_PRICE, mPriceEditText.getText().toString().trim());
                    values.put(SwitchEntry.COLUMN_GAME_COUNT, Integer.parseInt(mCountTextView.getText().toString()));
                    values.put(SwitchEntry.COLUMN_GAME_IMAGE, imageUri.toString());
                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI " + currentUri);
            }

            if (currentUri.equals(PcEntry.CONTENT_URI) || currentUri.equals(XboxEntry.CONTENT_URI) ||
                    currentUri.equals(PlaystationEntry.CONTENT_URI) || currentUri.equals(SwitchEntry.CONTENT_URI)) {

                Uri newUri = getContentResolver().insert(currentUri, values);

                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_insert_game_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_game_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsUpdated = getContentResolver().update(currentUri, values, null, null);

                if (rowsUpdated == 0) {
                    Toast.makeText(this, getString(R.string.editor_update_game_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_update_game_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        }
    }

    //If the delete button get clicked the user has to confirm or cancel
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteGame();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteGame() {
        if (currentUri != null) {
            int rowsDeleted = getContentResolver().delete(currentUri, null, null);
            if (rowsDeleted == 1) {
                Toast.makeText(this, getString(R.string.editor_delete_game_successful),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_game_failed),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!mGameHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
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
        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex("name");
            int priceColumnIndex = cursor.getColumnIndex("price");
            int countColumnIndex = cursor.getColumnIndex("count");
            int imageColumnIndex = cursor.getColumnIndex("image");

            String gameName = cursor.getString(nameColumnIndex);
            String gamePrice = cursor.getString(priceColumnIndex);
            Integer countName = cursor.getInt(countColumnIndex);
            String imageName = cursor.getString(imageColumnIndex);

            mNameEditText.setText(gameName);
            mPriceEditText.setText(gamePrice);
            mCountTextView.setText(Integer.toString(countName));
            mImageView.setImageURI(Uri.parse(imageName));
            imageUri = Uri.parse(imageName);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mCountTextView.setText("0");
    }

    public void tryToOpenImageSelector() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
        openImageSelector();
    }

    private void openImageSelector() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                imageUri = resultData.getData();
                final int takeFlags = resultData.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                }
                mImageView.setImageURI(imageUri);
                mImageView.invalidate();
            }
        }
    }
}

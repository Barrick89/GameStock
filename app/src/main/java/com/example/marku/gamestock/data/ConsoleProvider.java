package com.example.marku.gamestock.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.marku.gamestock.data.ConsoleContract.PcEntry;
import com.example.marku.gamestock.data.ConsoleContract.PlaystationEntry;
import com.example.marku.gamestock.data.ConsoleContract.SwitchEntry;
import com.example.marku.gamestock.data.ConsoleContract.XboxEntry;


public class ConsoleProvider extends ContentProvider {

    private ConsoleDbHelper mConsoleDbHelper;

    public static final String LOG_TAG = ConsoleProvider.class.getSimpleName();

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


    @Override
    public boolean onCreate() {
        mConsoleDbHelper = new ConsoleDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mConsoleDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        String table;

        switch (match) {
            case PC_ID:
                selection = PcEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            case PC:
                table = PcEntry.TABLE_NAME;
                break;
            case XBOX_ID:
                selection = XboxEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            case XBOX:
                table = XboxEntry.TABLE_NAME;
                break;
            case PLAYSTATION_ID:
                selection = PlaystationEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            case PLAYSTATION:
                table = PlaystationEntry.TABLE_NAME;
                break;
            case SWITCH_ID:
                selection = SwitchEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            case SWITCH:
                table = SwitchEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PC:
                return PcEntry.CONTENT_LIST_TYPE;
            case PC_ID:
                return PcEntry.CONTENT_ITEM_TYPE;
            case XBOX:
                return XboxEntry.CONTENT_LIST_TYPE;
            case XBOX_ID:
                return XboxEntry.CONTENT_ITEM_TYPE;
            case PLAYSTATION:
                return PlaystationEntry.CONTENT_LIST_TYPE;
            case PLAYSTATION_ID:
                return PlaystationEntry.CONTENT_ITEM_TYPE;
            case SWITCH:
                return SwitchEntry.CONTENT_LIST_TYPE;
            case SWITCH_ID:
                return SwitchEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PC:
                return insertPcGame(uri, contentValues);
            case XBOX:
                return insertXboxGame(uri, contentValues);
            case PLAYSTATION:
                return insertPlaystationGame(uri, contentValues);
            case SWITCH:
                return insertSwitchGame(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPcGame(Uri uri, ContentValues values) {
        String name = values.getAsString(PcEntry.COLUMN_GAME_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Game requires a name");
        }

        String price = values.getAsString(PcEntry.COLUMN_GAME_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Game requires valid price");
        }

        Integer count = values.getAsInteger(PcEntry.COLUMN_GAME_COUNT);
        if (count < 0) {
            throw new IllegalArgumentException("Game requires valid count");
        }

        String image = values.getAsString(PcEntry.COLUMN_GAME_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException("Game requires valid image");
        }

        SQLiteDatabase db = mConsoleDbHelper.getWritableDatabase();

        long id = db.insert(PcEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertXboxGame(Uri uri, ContentValues values) {
        String name = values.getAsString(XboxEntry.COLUMN_GAME_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Game requires a name");
        }

        String price = values.getAsString(XboxEntry.COLUMN_GAME_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Game requires valid price");
        }

        Integer count = values.getAsInteger(XboxEntry.COLUMN_GAME_COUNT);
        if (count < 0) {
            throw new IllegalArgumentException("Game requires valid count");
        }

        String image = values.getAsString(XboxEntry.COLUMN_GAME_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException("Game requires valid image");
        }

        SQLiteDatabase db = mConsoleDbHelper.getWritableDatabase();

        long id = db.insert(XboxEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertPlaystationGame(Uri uri, ContentValues values) {
        String name = values.getAsString(PlaystationEntry.COLUMN_GAME_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Game requires a name");
        }

        String price = values.getAsString(PlaystationEntry.COLUMN_GAME_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Game requires valid price");
        }

        Integer count = values.getAsInteger(PlaystationEntry.COLUMN_GAME_COUNT);
        if (count < 0) {
            throw new IllegalArgumentException("Game requires valid count");
        }

        String image = values.getAsString(PlaystationEntry.COLUMN_GAME_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException("Game requires valid image");
        }

        SQLiteDatabase db = mConsoleDbHelper.getWritableDatabase();

        long id = db.insert(PlaystationEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertSwitchGame(Uri uri, ContentValues values) {
        String name = values.getAsString(SwitchEntry.COLUMN_GAME_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Game requires a name");
        }

        String price = values.getAsString(SwitchEntry.COLUMN_GAME_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Game requires valid price");
        }

        Integer count = values.getAsInteger(SwitchEntry.COLUMN_GAME_COUNT);
        if (count < 0) {
            throw new IllegalArgumentException("Game requires valid count");
        }

        String image = values.getAsString(SwitchEntry.COLUMN_GAME_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException("Game requires valid image");
        }

        SQLiteDatabase db = mConsoleDbHelper.getWritableDatabase();

        long id = db.insert(SwitchEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mConsoleDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        String table;

        switch (match) {
            case PC_ID:
                selection = PcEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            case PC:
                table = PcEntry.TABLE_NAME;
                break;
            case XBOX_ID:
                selection = XboxEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            case XBOX:
                table = XboxEntry.TABLE_NAME;
                break;
            case PLAYSTATION_ID:
                selection = PlaystationEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            case PLAYSTATION:
                table = PlaystationEntry.TABLE_NAME;
                break;
            case SWITCH_ID:
                selection = SwitchEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            case SWITCH:
                table = SwitchEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        rowsDeleted = db.delete(table, selection, selectionArgs);

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PC:
                return updatePc(uri, contentValues, selection, selectionArgs);
            case PC_ID:
                selection = PcEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePc(uri, contentValues, selection, selectionArgs);
            case XBOX:
                return updateXbox(uri, contentValues, selection, selectionArgs);
            case XBOX_ID:
                selection = XboxEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateXbox(uri, contentValues, selection, selectionArgs);
            case PLAYSTATION:
                return updatePlaystation(uri, contentValues, selection, selectionArgs);
            case PLAYSTATION_ID:
                selection = PlaystationEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePlaystation(uri, contentValues, selection, selectionArgs);
            case SWITCH:
                return updateSwitch(uri, contentValues, selection, selectionArgs);
            case SWITCH_ID:
                selection = SwitchEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateSwitch(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePc(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(PcEntry.COLUMN_GAME_NAME)) {
            String name = values.getAsString(PcEntry.COLUMN_GAME_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Game requires a name");
            }
        }

        if (values.containsKey(PcEntry.COLUMN_GAME_PRICE)) {
            String price = values.getAsString(PcEntry.COLUMN_GAME_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Game requires a price");
            }
        }

        if (values.containsKey(PcEntry.COLUMN_GAME_COUNT)) {
            Integer count = values.getAsInteger(PcEntry.COLUMN_GAME_COUNT);
            if (count != null && count < 0) {
                throw new IllegalArgumentException("Game requires valid count");
            }
        }

        if (values.containsKey(PcEntry.COLUMN_GAME_IMAGE)) {
            String price = values.getAsString(PcEntry.COLUMN_GAME_IMAGE);
            if (price == null) {
                throw new IllegalArgumentException("Game requires an image");
            }
        }

        SQLiteDatabase db = mConsoleDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(PcEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private int updateXbox(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(XboxEntry.COLUMN_GAME_NAME)) {
            String name = values.getAsString(XboxEntry.COLUMN_GAME_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Game requires a name");
            }
        }

        if (values.containsKey(XboxEntry.COLUMN_GAME_PRICE)) {
            String price = values.getAsString(XboxEntry.COLUMN_GAME_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Game requires a price");
            }
        }

        if (values.containsKey(XboxEntry.COLUMN_GAME_COUNT)) {
            Integer count = values.getAsInteger(XboxEntry.COLUMN_GAME_COUNT);
            if (count != null && count < 0) {
                throw new IllegalArgumentException("Game requires valid count");
            }
        }

        if (values.containsKey(XboxEntry.COLUMN_GAME_IMAGE)) {
            String price = values.getAsString(XboxEntry.COLUMN_GAME_IMAGE);
            if (price == null) {
                throw new IllegalArgumentException("Game requires an image");
            }
        }

        SQLiteDatabase db = mConsoleDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(XboxEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private int updatePlaystation(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(PlaystationEntry.COLUMN_GAME_NAME)) {
            String name = values.getAsString(PlaystationEntry.COLUMN_GAME_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Game requires a name");
            }
        }

        if (values.containsKey(PlaystationEntry.COLUMN_GAME_PRICE)) {
            String price = values.getAsString(PlaystationEntry.COLUMN_GAME_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Game requires a price");
            }
        }

        if (values.containsKey(PlaystationEntry.COLUMN_GAME_COUNT)) {
            Integer count = values.getAsInteger(PlaystationEntry.COLUMN_GAME_COUNT);
            if (count != null && count < 0) {
                throw new IllegalArgumentException("Game requires valid count");
            }
        }

        if (values.containsKey(PlaystationEntry.COLUMN_GAME_IMAGE)) {
            String price = values.getAsString(PlaystationEntry.COLUMN_GAME_IMAGE);
            if (price == null) {
                throw new IllegalArgumentException("Game requires an image");
            }
        }

        SQLiteDatabase db = mConsoleDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(PlaystationEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private int updateSwitch(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(SwitchEntry.COLUMN_GAME_NAME)) {
            String name = values.getAsString(SwitchEntry.COLUMN_GAME_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Game requires a name");
            }
        }

        if (values.containsKey(SwitchEntry.COLUMN_GAME_PRICE)) {
            String price = values.getAsString(SwitchEntry.COLUMN_GAME_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Game requires a price");
            }
        }

        if (values.containsKey(SwitchEntry.COLUMN_GAME_COUNT)) {
            Integer count = values.getAsInteger(SwitchEntry.COLUMN_GAME_COUNT);
            if (count != null && count < 0) {
                throw new IllegalArgumentException("Game requires valid count");
            }
        }

        if (values.containsKey(SwitchEntry.COLUMN_GAME_IMAGE)) {
            String price = values.getAsString(SwitchEntry.COLUMN_GAME_IMAGE);
            if (price == null) {
                throw new IllegalArgumentException("Game requires an image");
            }
        }

        SQLiteDatabase db = mConsoleDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(SwitchEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}

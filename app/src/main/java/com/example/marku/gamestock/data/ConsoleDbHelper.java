package com.example.marku.gamestock.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.marku.gamestock.data.ConsoleContract.PcEntry;
import com.example.marku.gamestock.data.ConsoleContract.PlaystationEntry;
import com.example.marku.gamestock.data.ConsoleContract.SwitchEntry;
import com.example.marku.gamestock.data.ConsoleContract.XboxEntry;


public class ConsoleDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "console.db";

    public ConsoleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PC_TABLE = "CREATE TABLE IF NOT EXISTS " + PcEntry.TABLE_NAME + " ("
                + PcEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PcEntry.COLUMN_GAME_NAME + " TEXT NOT NULL, "
                + PcEntry.COLUMN_GAME_PRICE + " TEXT NOT NULL DEFAULT 0.00, "
                + PcEntry.COLUMN_GAME_COUNT + " INTEGER NOT NULL DEFAULT 0,"
                + PcEntry.COLUMN_GAME_IMAGE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PC_TABLE);

        String SQL_CREATE_XBOX_TABLE = "CREATE TABLE IF NOT EXISTS " + XboxEntry.TABLE_NAME + " ("
                + XboxEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + XboxEntry.COLUMN_GAME_NAME + " TEXT NOT NULL, "
                + XboxEntry.COLUMN_GAME_PRICE + " TEXT NOT NULL DEFAULT 0.00, "
                + XboxEntry.COLUMN_GAME_COUNT + " INTEGER NOT NULL DEFAULT 0,"
                + XboxEntry.COLUMN_GAME_IMAGE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_XBOX_TABLE);

        String SQL_CREATE_PLAYSTATION_TABLE = "CREATE TABLE IF NOT EXISTS " + PlaystationEntry.TABLE_NAME + " ("
                + PlaystationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PlaystationEntry.COLUMN_GAME_NAME + " TEXT NOT NULL, "
                + PlaystationEntry.COLUMN_GAME_PRICE + " TEXT NOT NULL DEFAULT 0.00, "
                + PlaystationEntry.COLUMN_GAME_COUNT + " INTEGER NOT NULL DEFAULT 0,"
                + PlaystationEntry.COLUMN_GAME_IMAGE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PLAYSTATION_TABLE);

        String SQL_CREATE_SWITCH_TABLE = "CREATE TABLE IF NOT EXISTS " + SwitchEntry.TABLE_NAME + " ("
                + SwitchEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SwitchEntry.COLUMN_GAME_NAME + " TEXT NOT NULL, "
                + SwitchEntry.COLUMN_GAME_PRICE + " TEXT NOT NULL DEFAULT 0.00, "
                + SwitchEntry.COLUMN_GAME_COUNT + " INTEGER NOT NULL DEFAULT 0,"
                + SwitchEntry.COLUMN_GAME_IMAGE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_SWITCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

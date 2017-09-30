package com.example.marku.gamestock.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ConsoleContract {

    private ConsoleContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.marku.gamestock";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PC = "pc";
    public static final String PATH_XBOX = "xbox";
    public static final String PATH_PLAYSTATION = "playstation";
    public static final String PATH_SWITCH = "switch";

    public static abstract class PcEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PC);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PC;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PC;

        public static final String TABLE_NAME = "pc";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_GAME_NAME = "name";
        public static final String COLUMN_GAME_PRICE = "price";
        public static final String COLUMN_GAME_COUNT = "count";
        public static final String COLUMN_GAME_IMAGE = "image";
    }

    public static abstract class XboxEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_XBOX);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_XBOX;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_XBOX;

        public static final String TABLE_NAME = "xbox";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_GAME_NAME = "name";
        public static final String COLUMN_GAME_PRICE = "price";
        public static final String COLUMN_GAME_COUNT = "count";
        public static final String COLUMN_GAME_IMAGE = "image";
    }

    public static abstract class PlaystationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLAYSTATION);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYSTATION;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYSTATION;

        public static final String TABLE_NAME = "playstation";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_GAME_NAME = "name";
        public static final String COLUMN_GAME_PRICE = "price";
        public static final String COLUMN_GAME_COUNT = "count";
        public static final String COLUMN_GAME_IMAGE = "image";
    }

    public static abstract class SwitchEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SWITCH);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SWITCH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SWITCH;

        public static final String TABLE_NAME = "switch";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_GAME_NAME = "name";
        public static final String COLUMN_GAME_PRICE = "price";
        public static final String COLUMN_GAME_COUNT = "count";
        public static final String COLUMN_GAME_IMAGE = "image";
    }
}

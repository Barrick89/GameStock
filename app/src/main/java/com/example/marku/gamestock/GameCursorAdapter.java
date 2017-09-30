package com.example.marku.gamestock;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class GameCursorAdapter extends CursorAdapter {

    private final GameCatalogActivity activity;

    public GameCursorAdapter(GameCatalogActivity context, Cursor cursor) {
        super(context, cursor, 0);
        activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name =  view.findViewById(R.id.name);
        TextView price =  view.findViewById(R.id.price);
        TextView count =  view.findViewById(R.id.count);
        ImageView image = view.findViewById(R.id.image_view);

        int nameColumnIndex = cursor.getColumnIndex("name");
        int priceColumnIndex = cursor.getColumnIndex("price");
        int countColumnIndex = cursor.getColumnIndex("count");
        int imageColumnIndex = cursor.getColumnIndex("image");

        String gameName = cursor.getString(nameColumnIndex);
        String gamePrice = cursor.getString(priceColumnIndex);
        final int gameCount = cursor.getInt(countColumnIndex);
        String imageName = cursor.getString(imageColumnIndex);

        name.setText(gameName);
        price.setText(gamePrice);
        count.setText(String.valueOf(gameCount));
        image.setImageURI(Uri.parse(imageName));

        Button sellButton = view.findViewById(R.id.sell_button);
        final long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickOnSale(id, gameCount);
            }
        });
    }
}

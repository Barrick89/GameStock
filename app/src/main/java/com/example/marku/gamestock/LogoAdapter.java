package com.example.marku.gamestock;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class LogoAdapter extends BaseAdapter {

    private Context mContext;

    private Integer[] mImageIds = {
            R.drawable.pc_logo, R.drawable.xbox_logo,
            R.drawable.playstation_logo, R.drawable.switch_logo
    };

    public LogoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView image;
        if (view == null) {
            image = new ImageView(mContext);
            image.setLayoutParams(new GridView.LayoutParams(300, 300));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setPadding(30, 30, 30, 30);
        } else {
            image = (ImageView) view;
        }
        image.setImageResource(mImageIds[i]);
        return image;
    }
}

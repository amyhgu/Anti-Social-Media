package com.example.arafatm.anti_socialmedia.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.arafatm.anti_socialmedia.R;

public class GroupAdapter extends BaseAdapter {
    private Context mContext;

    public GroupAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) view;
        }

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200, 200);
        imageView.setLayoutParams(params);

        imageView.setBackgroundResource(R.drawable.round_coner_of_each_image);

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_group_default, R.drawable.ic_group_default,
            R.drawable.ic_group_default, R.drawable.ic_group_default,
            R.drawable.ic_group_default, R.drawable.ic_group_default,
            R.drawable.ic_group_default, R.drawable.ic_group_default,
            R.drawable.ic_group_default, R.drawable.ic_group_default,
            R.drawable.ic_group_default, R.drawable.ic_group_default,
            R.drawable.ic_group_default, R.drawable.ic_group_default,
            R.drawable.ic_group_default, R.drawable.ic_group_default,
            R.drawable.ic_group_default, R.drawable.ic_group_default,
            R.drawable.ic_group_default, R.drawable.ic_group_default,
    };
}

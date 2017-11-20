package com.example.pc.mmsr_reader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pc.mmsr_reader.Class.Storybook;

import java.util.ArrayList;

public class LibraryAdapter extends ArrayAdapter<String> {
    //  String[] storybookTitle;
    //  String[] storybookDescription;
    //  int[] coverpage;

    Context mContext;
    ArrayList<Storybook> libStorybooks;

    public LibraryAdapter(@NonNull Context context, ArrayList<Storybook> libStorybooks) {
        super(context, R.layout.activity_library_list_item);
        //    this.storybookTitle = storybookTitle;
        //    this.storybookDescription = storybookDescription;
        this.mContext = context;
        this.libStorybooks = libStorybooks;

    }

    @Override
    public int getCount() {
        return (libStorybooks == null) ? 0 : libStorybooks.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.activity_library_list_item, parent, false);
            mViewHolder.mCoverPage = convertView.findViewById(R.id.imgStoryCover);
            mViewHolder.mStorybookTitle = convertView.findViewById(R.id.txtStoryTitle);
            mViewHolder.mAuthorName = convertView.findViewById(R.id.txtStoryAuthor);
            mViewHolder.mStoryDesc = convertView.findViewById(R.id.txtDesc);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        final Storybook current = libStorybooks.get(position);
        Bitmap cover = BitmapFactory.decodeByteArray(current.getCoverPage(), 0, current.getCoverPage().length);
        mViewHolder.mCoverPage.setImageBitmap(cover);
        mViewHolder.mStorybookTitle.setText(current.getTitle());
        mViewHolder.mAuthorName.setText(current.getEmail());
        mViewHolder.mStoryDesc.setText(current.getDesc());
        //  mViewHolder.mStorybookDescription.setText(storybookDescription[position]);
        // return super.getView(position, convertView, parent);
        return convertView;
    }

    static class ViewHolder {
        ImageView mCoverPage;
        TextView mStorybookTitle;
        TextView mAuthorName;
        TextView mStoryDesc;
        RatingBar mRating;
        //TextView mStorybookDescription;
    }

}
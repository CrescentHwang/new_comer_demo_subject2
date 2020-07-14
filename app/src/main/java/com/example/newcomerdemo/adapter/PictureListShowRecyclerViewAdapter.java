package com.example.newcomerdemo.adapter;

import android.content.Context;
import android.graphics.Picture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newcomerdemo.R;
import com.example.newcomerdemo.model.PictureItem;
import com.example.newcomerdemo.model.PicturesUrlGetter;

import java.util.ArrayList;

public class PictureListShowRecyclerViewAdapter
        extends RecyclerView.Adapter<PictureListShowRecyclerViewAdapter.PictureListViewHolder> {
    private ArrayList<PictureItem> mItems;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    // 借助接口将 itemView 的 onClick() 方法暴露给外界
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public PictureListShowRecyclerViewAdapter(Context context, ArrayList<PictureItem> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @NonNull
    @Override
    public PictureListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // todo 圆角！
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_picture_list_show, parent, false);
        return new PictureListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureListViewHolder holder, final int position) {
        final PictureItem item = mItems.get(position);
        Glide.with(mContext)
                .load(item.getmUrl())
                .thumbnail(0.5f)
                .error(R.drawable.picture_err)
                .into(holder.mImageView);
        holder.mDescriptionTV.setText(item.getmDescription());
        holder.mCreateTimeTV.setText(item.getmCreateTime().substring(0,10));
        holder.mViewNumTV.setText(item.getmViewNumber()+"");

        // 为 itemView 添加点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class PictureListViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mDescriptionTV;
        TextView mCreateTimeTV;
        TextView mViewNumTV;

        public PictureListViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_picture_list_image_view);
            mDescriptionTV = itemView.findViewById(R.id.item_pitcure_description);
            mCreateTimeTV = itemView.findViewById(R.id.item_pitcure_create_time);
            mViewNumTV = itemView.findViewById(R.id.item_pitcure_view_number);
        }
    }
}

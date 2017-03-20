package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.util.MeasureUtil;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<String> imageList;

    public RecyclerViewAdapter(Context context, List<String> imageList) {
        this.mContext = context;
        this.imageList = imageList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.images);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardView = inflater.inflate(R.layout.item_image, null, false);
        int width = MeasureUtil.getBase4Width(MeasureUtil.getScreenWidth(mContext));
        cardView.setLayoutParams(new LinearLayoutCompat.LayoutParams(width, width));
        ViewHolder viewHolder = new ViewHolder(cardView);
        viewHolder.imageView = (ImageView) cardView.findViewById(R.id.images);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView iv = holder.imageView;
        Glide.with(mContext).load(imageList.get(position)).into(iv);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

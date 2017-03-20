package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.mvp.model.Sort;
import com.xiaoxian.trade.util.MeasureUtil;

import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.MyViewHolder>{
    private Context context;
    private List<Sort> dataList;
    private LayoutInflater inflater;
    private SortAdapterOnClickListener listener;

    public interface SortAdapterOnClickListener{
        void onClick(int item, int pos);
    }

    public void setSortAdapterOnClickListener(SortAdapterOnClickListener listener) {
        this.listener = listener;
    }

    public SortAdapter(Context context, List<Sort> dataList) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView image1, image2, image3;
        public MyViewHolder(View view) {
            super(view);
            text = (TextView)view.findViewById(R.id.item_sort_text);
            image1 = (ImageView) view.findViewById(R.id.item_sort_image1);
            image2 = (ImageView) view.findViewById(R.id.item_sort_image2);
            image3 = (ImageView) view.findViewById(R.id.item_sort_image3);

            int width = MeasureUtil.getBase3Width(MeasureUtil.getScreenWidth(context));
            image1.setLayoutParams(new LinearLayout.LayoutParams(width, width));
            image2.setLayoutParams(new LinearLayout.LayoutParams(width, width));
            image3.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_sort, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.text.setText(dataList.get(position).getKind());
        Glide.with(context).load(dataList.get(position).getFile1().getUrl()).into(holder.image1);
        Glide.with(context).load(dataList.get(position).getFile2().getUrl()).into(holder.image2);
        Glide.with(context).load(dataList.get(position).getFile3().getUrl()).into(holder.image3);
        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position, 1);
            }
        });
        holder.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position, 2);
            }
        });
        holder.image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position, 3);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

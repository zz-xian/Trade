package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.mvp.model.Kind;

import java.util.List;

public class KindAdapter extends BaseAdapter {
    private Context mContext;
    private List<Kind> kindList;
    private LayoutInflater inflater;

    public KindAdapter(Context context, List<Kind> kindList) {
        mContext = context;
        this.kindList = kindList;
    }

    @Override
    public int getCount() {
        return kindList.size();
    }

    @Override
    public Object getItem(int position) {
        return kindList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView kindText, descText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_kind, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_kind_image);
            viewHolder.kindText = (TextView) convertView.findViewById(R.id.item_kind_text1);
            viewHolder.descText = (TextView) convertView.findViewById(R.id.item_kind_text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.kindText.setText(kindList.get(position).getKind());
        viewHolder.descText.setText(kindList.get(position).getDesc());
        Glide.with(mContext).load(kindList.get(position).getImage().getUrl()).into(viewHolder.imageView);
        return convertView;
    }
}

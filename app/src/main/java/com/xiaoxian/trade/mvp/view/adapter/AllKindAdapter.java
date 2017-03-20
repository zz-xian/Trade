package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.mvp.model.Kind;

import java.util.List;

public class AllKindAdapter extends BaseAdapter {
    private Context mContext;
    private List<Kind> kindList;
    private LayoutInflater inflater;

    public AllKindAdapter(Context context, List<Kind> kindList) {
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
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_kind_list, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_kind_list_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(kindList.get(position).getKind());
        return convertView;
    }
}

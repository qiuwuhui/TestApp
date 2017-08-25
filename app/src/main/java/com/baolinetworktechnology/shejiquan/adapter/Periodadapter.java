package com.baolinetworktechnology.shejiquan.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.NineGridTestModel;
import com.baolinetworktechnology.shejiquan.view.NineGridTestLayout;

import java.util.List;

public class Periodadapter extends RecyclerView.Adapter<Periodadapter.ViewHolder>{

    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;

    public Periodadapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<NineGridTestModel> list) {
        mList = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.period_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.layout.setUrlList(mList.get(position).urlList);
    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        NineGridTestLayout layout;

        public ViewHolder(View view) {
            super(view);
            layout = (NineGridTestLayout) view.findViewById(R.id.layout_nine_grid);
        }
    }
    private int getListSize(List<NineGridTestModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
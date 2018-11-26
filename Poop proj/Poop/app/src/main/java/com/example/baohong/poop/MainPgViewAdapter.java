package com.example.baohong.poop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MainPgViewAdapter extends RecyclerView.Adapter<MainPgViewAdapter.ViewHolder> {
    private List<ListItem> listItems;
    private Context context;

    public MainPgViewAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MainPgViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_listitem, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainPgViewAdapter.ViewHolder viewHolder, int i) {
        ListItem listItem = listItems.get(i);
        viewHolder.heading.setText(listItem.getHead());
        viewHolder.desc.setText(listItem.getDesc());

        Picasso.get()
                .load(listItem.getImgURL())
                .into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView heading, desc;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.heading);
            desc = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

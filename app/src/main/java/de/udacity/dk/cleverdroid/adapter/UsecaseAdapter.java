package de.udacity.dk.cleverdroid.adapter;

import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.udacity.dk.cleverdroid.R;

/**
 * Created by Deniz Kalem on 03.12.2017.
 */

public class UsecaseAdapter extends RecyclerView.Adapter<UsecaseAdapter.MyViewHolder> {

    private List<String> usecaseList;
    private RecyclerViewClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView description;
        private RecyclerViewClickListener listener;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            description = view.findViewById(R.id.tv_usecase);
            this.listener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public UsecaseAdapter(List<String> usecaseList, RecyclerViewClickListener listener) {
        this.usecaseList = usecaseList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        if (parent.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            int height = parent.getMeasuredHeight() / 4;
            int width = parent.getMeasuredWidth();

            itemView.setLayoutParams(new RecyclerView.LayoutParams(width, height));
        } else {
//            int height = parent.getMeasuredHeight() / 2;
//            int width = parent.getMeasuredWidth();
//
//            itemView.setLayoutParams(new RecyclerView.LayoutParams(width, height));
        }

        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String usecase = usecaseList.get(position);
        holder.description.setText(usecase);
    }

    @Override
    public int getItemCount() {
        return usecaseList.size();
    }

}


package de.udacity.dk.cleverdroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.data.Usecase;

/**
 * Created by Deniz Kalem on 03.12.2017.
 */

public class UsecaseAdapter extends RecyclerView.Adapter<UsecaseAdapter.MyViewHolder> {

    private List<Usecase> usecaseList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description;

        public MyViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.tv_usecase);
        }
    }

    public UsecaseAdapter(List<Usecase> usecaseList) {
        this.usecaseList = usecaseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        int height = parent.getMeasuredHeight() / 4;
        int width = parent.getMeasuredWidth();

        itemView.setLayoutParams(new RecyclerView.LayoutParams(width, height));


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Usecase usecase = usecaseList.get(position);
        holder.description.setText(usecase.getDescription());
    }

    @Override
    public int getItemCount() {
        return usecaseList.size();
    }
}


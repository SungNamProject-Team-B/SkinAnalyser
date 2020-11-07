package com.Aha.pimple;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Skin> myList;
    public static class MyViewHolder extends  RecyclerView.ViewHolder{
    public ImageView RowImage;

    public MyViewHolder(View view){
        super(view);
        RowImage = view.findViewById(R.id.rv);
    }
    }

    public MyAdapter(ArrayList<Skin> myDataset) {
    myList = myDataset;}

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.camera, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (null != myList ? myList.size() : 0);
    }


}



package com.example.btl_java.RecycleView.Home.AutoScrollBook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_java.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    List<String> list_img_book;

    public Adapter(Context context, List<String> list_img_book) {
        this.context = context;
        this.list_img_book = list_img_book;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rcv_auto_scroll_book,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list_img_book.get(position)).into(holder.img_book_scroll);
        holder.img_book_scroll.getLayoutParams().height=500;
    }

    @Override
    public int getItemCount() {
        return list_img_book.size()>0?list_img_book.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_book_scroll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_book_scroll = itemView.findViewById(R.id.img_book_scroll);
        }
    }
}

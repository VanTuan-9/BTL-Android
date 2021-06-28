package com.example.btl_java.RecycleView.Home.ListChild;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_java.ActivityBook;
import com.example.btl_java.R;

import java.util.List;

public class ItemBookAdapter extends RecyclerView.Adapter<ItemBookAdapter.Viewholder> {

    List<Book> books;
    Context context;

    public ItemBookAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item_book,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        Glide.with(context).load(books.get(position).getUrlBook()).into(holder.img_book);
        holder.tv_name_book.setText(books.get(position).getNameBook());

        holder.ln_item_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityBook.class);
                intent.putExtra("book",books.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size() > 0 ? books.size():0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView img_book;
        TextView tv_name_book;
        LinearLayout ln_item_book;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            img_book = itemView.findViewById(R.id.img_book);
            tv_name_book = itemView.findViewById(R.id.tv_name_book);
            ln_item_book = itemView.findViewById(R.id.ln_item_book);
        }
    }
}

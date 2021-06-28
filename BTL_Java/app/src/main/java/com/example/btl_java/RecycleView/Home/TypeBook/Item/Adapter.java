package com.example.btl_java.RecycleView.Home.TypeBook.Item;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_java.ActivityBook;
import com.example.btl_java.R;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.RecycleView.Home.ListChild.ContentBook;
import com.example.btl_java.login.User;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {
    Context context;
    List<Book> books;
    User user;

    public Adapter(Context context, List<Book> books, User user) {
        this.context = context;
        this.books = books;
        this.user = user;
//        this.contentBooks =contentBooks;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new Viewholder(inflater.inflate(R.layout.layout_item_book,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        Glide.with(context).load(books.get(position).getUrlBook()).into(holder.img_book);
        holder.tv_name_book.setText(books.get(position).getNameBook());
        holder.tv_name_book.setSelected(true);
        holder.ln_item_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityBook.class);
                intent.putExtra("book", books.get(position));
                intent.putExtra("User", user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(books.size()<=0) return 0;
        else
            return books.size()>10?10:books.size();
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

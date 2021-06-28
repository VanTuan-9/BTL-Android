package com.example.btl_java.RecycleView.Search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_java.ActivityBook;
import com.example.btl_java.R;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.login.User;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {

    List<Book> books;
    Context context;
    User user;

    public Adapter(List<Book> books, Context context, User user) {
        this.books = books;
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rcv_item_search,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        Glide.with(context).load(books.get(position).getUrlBook()).into(holder.img_book_search);
        holder.tv_name_book_search.setText(books.get(position).getNameBook());
        holder.tv_author_book_search.setText(books.get(position).getAuthor());
        holder.rl_item_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityBook.class);
                intent.putExtra("book",books.get(position));
                intent.putExtra("User", user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView img_book_search;
        TextView tv_name_book_search;
        TextView tv_author_book_search;
        RelativeLayout rl_item_search;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            img_book_search = itemView.findViewById(R.id.img_book_search);
            tv_name_book_search = itemView.findViewById(R.id.tv_name_book_search);
            tv_author_book_search = itemView.findViewById(R.id.tv_author_book_search);
            tv_author_book_search = itemView.findViewById(R.id.tv_author_book_search);
            rl_item_search = itemView.findViewById(R.id.rl_item_search);
        }
    }
}

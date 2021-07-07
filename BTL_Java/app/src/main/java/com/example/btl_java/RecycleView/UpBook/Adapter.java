package com.example.btl_java.RecycleView.UpBook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_java.ActivityBook;
import com.example.btl_java.R;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.RecycleView.Home.ListChild.BookUp;
import com.example.btl_java.login.User;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {
    List<BookUp> bookUps =new ArrayList<>();
    Context context;
    User user;

    public Adapter(List<BookUp> bookUps, Context context, User user) {
        this.bookUps = bookUps;
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layoutitembook,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        Glide.with(context).load(bookUps.get(position).getUrlBook()).into(holder.avtBook);
        holder.tvNameBook.setText(bookUps.get(position).getNameBook());
        holder.tvAuthor.setText(bookUps.get(position).getAuthor());
        holder.tvLike.setText(bookUps.get(position).getLikes()+"");
        holder.tvView.setText(bookUps.get(position).getView()+"");
        if(bookUps.get(position).getClassify() == 0)
            holder.ln_cmt.setVisibility(View.GONE);
        else{
            holder.rl_item_bookup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityBook.class);
                    intent.putExtra("User",user);
                    Book book = new Book(bookUps.get(position).getIdBook(),
                            bookUps.get(position).getNameBook(),
                            bookUps.get(position).getUrlBook(),
                            bookUps.get(position).getAuthor(),
                            bookUps.get(position).getNXB(),
                            bookUps.get(position).getLanguageBook(),
                            bookUps.get(position).getDayXB(),
                            bookUps.get(position).getNumberPages(),
                            bookUps.get(position).getCategory(),
                            bookUps.get(position).getView(),
                            bookUps.get(position).getLikes(),
                            bookUps.get(position).getContent(),
                            bookUps.get(position).getTimeCreated(),
                            bookUps.get(position).getShortContent());
                    intent.putExtra("book",book);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bookUps.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView avtBook;
        TextView tvNameBook, tvAuthor, tvView, tvLike, tvComment;
        LinearLayout ln_cmt;
        RelativeLayout rl_item_bookup;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            avtBook = itemView.findViewById(R.id.avtBook);
            tvNameBook = itemView.findViewById(R.id.tvNameBook);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvView = itemView.findViewById(R.id.tvView);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvComment = itemView.findViewById(R.id.tvComment);
            ln_cmt = itemView.findViewById(R.id.ln_cmt);
            rl_item_bookup = itemView.findViewById(R.id.rl_item_bookup);
        }
    }
}

package com.example.btl_java.RecycleView.Home.BXH;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_java.ActivityBook;
import com.example.btl_java.R;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.RecycleView.Home.ListChild.ContentBook;
import com.example.btl_java.login.User;

import java.util.ArrayList;
import java.util.List;

public class AdapterBXH extends RecyclerView.Adapter<AdapterBXH.ViewHolder> {
    List<Book> itemBXHS;
    int kt;
    Context context;
    User user;

    public AdapterBXH(List<Book> itemBXHS, Context context, int kt, User user) {
        this.itemBXHS = itemBXHS;
        this.context = context;
        this.kt = kt;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rcv_bxh,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(itemBXHS.get(position).getUrlBook()).into(holder.img_book_bxh);
        holder.tv_name_book_bxh.setText(itemBXHS.get(position).getNameBook());
        holder.tv_author_book_bxh.setText(itemBXHS.get(position).getAuthor());
        if(kt == 1){
            if(itemBXHS.get(position).getLikes() >= 1000000)
                holder.tv_like_book_bxh.setText((float)itemBXHS.get(position).getLikes()/1000000+"M");
            else if(itemBXHS.get(position).getLikes() >= 1000)
                holder.tv_like_book_bxh.setText((float)itemBXHS.get(position).getLikes()/1000+"K");
            else
                holder.tv_like_book_bxh.setText(itemBXHS.get(position).getLikes()+"");
        }
        else{
            if(itemBXHS.get(position).getView() >= 1000000)
                holder.tv_like_book_bxh.setText((float)itemBXHS.get(position).getView()/1000000+"M");
            else if(itemBXHS.get(position).getView() >= 1000)
                holder.tv_like_book_bxh.setText((float)itemBXHS.get(position).getView()/1000+"K");
            else
                holder.tv_like_book_bxh.setText(itemBXHS.get(position).getView()+"");
        }
        holder.tv_rank_bxh.setText((position+1)+"");
        holder.rl_item_bxh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityBook.class);
                intent.putExtra("book", itemBXHS.get(position));
                intent.putExtra("User", user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(itemBXHS.size()>3)   return 3;
        return (itemBXHS.size()>0?itemBXHS.size():0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_book_bxh;
        TextView tv_name_book_bxh, tv_author_book_bxh, tv_like_book_bxh, tv_rank_bxh;
        RelativeLayout rl_item_bxh;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_book_bxh = itemView.findViewById(R.id.img_book_bxh);
            tv_name_book_bxh = itemView.findViewById(R.id.tv_name_book_bxh);
            tv_author_book_bxh = itemView.findViewById(R.id.tv_author_book_bxh);
            tv_like_book_bxh = itemView.findViewById(R.id.tv_like_book_bxh);
            tv_rank_bxh = itemView.findViewById(R.id.tv_rank_bxh);
            rl_item_bxh = itemView.findViewById(R.id.rl_item_bxh);
        }
    }
}

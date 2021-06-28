package com.example.btl_java.RecycleView.Home.ListChild;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_java.R;

import java.util.List;

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.Viewholder> {
    List<Comment> comments;
    Context context;
    String link_imgUser;

    public ListCommentAdapter(List<Comment> comments, Context context, String link_imgUser) {
        this.comments = comments;
        this.context = context;
        this.link_imgUser = link_imgUser;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new Viewholder(inflater.inflate(R.layout.layoutfragmentitemcomment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Glide.with(context).load(link_imgUser).into(holder.imgAvtUserCmt);
        holder.UserName.setText(comments.get(position).getUserName());
        holder.Time.setText(comments.get(position).getTimeCMT());
        holder.Cmt.setText(comments.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size() > 0? comments.size():0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imgAvtUserCmt;
        TextView UserName;
        TextView Cmt;
        TextView Time;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imgAvtUserCmt = (ImageView) itemView.findViewById(R.id.imgAvtUserCmt);
            UserName = itemView.findViewById(R.id.UserName);
            Cmt = itemView.findViewById(R.id.Cmt);
            Time = itemView.findViewById(R.id.Time);
        }
    }
}
